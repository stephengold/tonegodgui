// Gradle script to build and publish the tonegodgui project

plugins {
    `java-library`  // to build JVM libraries
    `maven-publish` // to publish artifacts to Maven repositories
    signing         // to sign artifacts for publication
}

val group = "com.github.stephengold"
val artifact = "tonegodgui"
val libraryVersion = "0.1.12-SNAPSHOT"
val baseName = "${artifact}-${libraryVersion}" // for artifacts
val websiteUrl = "https://github.com/stephengold/tonegodgui"
val javaVendor = System.getProperty("java.vendor")
val javaVersion = JavaVersion.current()

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api("org.jmonkeyengine:jme3-core:3.9.0-alpha2")
}

tasks.withType<JavaCompile>().all { // Java compile-time options:
    options.compilerArgs.add("-Xdiags:verbose")
    if (javaVersion.isCompatibleWith(JavaVersion.VERSION_14)) {
        // Suppress warnings that source value 7 is obsolete.
        options.compilerArgs.add("-Xlint:-options")
    }
    options.compilerArgs.add("-Xlint:unchecked")
    //options.setDeprecation(true) // to provide detailed deprecation warnings
    options.encoding = "UTF-8"
    if (javaVersion.isCompatibleWith(JavaVersion.VERSION_1_10)) {
        options.release = 8
    }
}

tasks.withType<Javadoc>().all {
    // Disable doclint for JDK15+ to avoid "no comment" warnings.
    if (javaVersion.isCompatibleWith(JavaVersion.VERSION_15)) {
        (options as CoreJavadocOptions).apply {
            addStringOption("Xdoclint:none", "-quiet")
        }
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds") // to disable caching of snapshots
}

// Register publishing tasks:

tasks.register("install") {
    dependsOn("publishMavenPublicationToMavenLocal")
    description = "Installs the Maven artifacts to the local repository."
}
tasks.register("release") {
    dependsOn("publishMavenPublicationToCentralRepository")
    description = "Stages the Maven artifacts to the Central Publisher Portal."
}

tasks.jar {
    archiveBaseName.set(baseName)
    doLast {
        println("built using Java $javaVersion ($javaVendor)")
    }
    manifest {
        attributes["Created-By"] = "$javaVersion ($javaVendor)"
    }
}

java.withJavadocJar()
tasks.named<Jar>("javadocJar") { archiveBaseName.set(baseName) }
tasks.register<Jar>("sourcesJar") {
    archiveBaseName.set(baseName)
    archiveClassifier.set("sources")
    description = "Creates a JAR of Java sourcecode."
    from(sourceSets.main.get().java) // default is ".allSource", which includes resources
}

tasks.named("assemble") { dependsOn("module", "moduleAsc", "pom", "pomAsc") }
tasks.register<Copy>("module") {
    dependsOn("generateMetadataFileForMavenPublication")
    description = "Copies the module metadata to build/libs."
    from("build/publications/maven/module.json")
    into("build/libs")
    rename("module.json", baseName + ".module")
}
tasks.register<Copy>("moduleAsc") {
    dependsOn("signMavenPublication")
    description = "Copies the signature of the module metadata to build/libs."
    from("build/publications/maven/module.json.asc")
    into("build/libs")
    rename("module.json.asc", baseName + ".module.asc")
}
tasks.register<Copy>("pom") {
    dependsOn("generatePomFileForMavenPublication")
    description = "Copies the Maven POM to build/libs."
    from("build/publications/maven/pom-default.xml")
    into("build/libs")
    rename("pom-default.xml", baseName + ".pom")
}
tasks.register<Copy>("pomAsc") {
    dependsOn("signMavenPublication")
    description = "Copies the signature of the Maven POM to build/libs."
    from("build/publications/maven/pom-default.xml.asc")
    into("build/libs")
    rename("pom-default.xml.asc", baseName + ".pom.asc")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.named("sourcesJar"))
            artifactId = artifact
            from(components["java"])
            groupId = group
            pom {
                description = "Tonegod's graphical user-interface library for jMonkeyEngine."
                developers {
                    developer {
                        name = "Cris aka \"t0neg0d\""
                    }
                }
                inceptionYear = "2012"
                licenses {
                    license {
                        distribution = "repo"
                        name = "FreeBSD License"
                        url = "https://opensource.org/licenses/BSD-2-Clause"
                    }
                }
                name = "${group}:${artifact}"
                scm {
                    connection = "scm:git:git://github.com/stephengold/tonegodgui.git"
                    developerConnection = "scm:git:ssh://github.com:stephengold/tonegodgui.git"
                    url = websiteUrl + "/tree/master"
                }
                url = websiteUrl
            }
            version = libraryVersion
        }
    }
    // Staging to the Central Publisher Portal relies on the existence of 2 properties
    // (centralUsername and centralPassword)
    // which should be set in the ~/.gradle/gradle.properties file
    // or by -P options on the command line.
    repositories {
        maven {
            credentials {
                username = if (hasProperty("centralUsername")) property("centralUsername") as String else "Unknown user"
                password = if (hasProperty("centralPassword")) property("centralPassword") as String else "Unknown password"
            }
            name = "Central"
            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
        }
    }
}
tasks.named("generateMetadataFileForMavenPublication") { dependsOn("pom") }
tasks.named("publishMavenPublicationToMavenLocal") {
    dependsOn("assemble")
    doLast { println("installed locally as " + baseName) }
}
tasks.named("publishMavenPublicationToCentralRepository") { dependsOn("assemble") }

// Register tasks to sign the Maven artifacts for publication:

// Signing relies on the existence of 3 properties
// (signing.keyId, signing.password, and signing.secretKeyRingFile)
// which should be set in the ~/.gradle/gradle.properties file
// or by -P options on the command line.

signing {
    sign(publishing.publications["maven"])
}
tasks.withType<Sign>().all {
    onlyIf { project.hasProperty("signing.keyId") }
}
tasks.named("signMavenPublication") { dependsOn("module") }
