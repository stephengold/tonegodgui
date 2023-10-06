package tonegod.gui.controls.extras.emitter;

import com.jme3.app.Application;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ImageRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import tonegod.gui.controls.extras.Portability;
import tonegod.gui.core.Screen;
import tonegod.gui.framework.animation.Interpolation;
import tonegod.gui.framework.animation.TemporalAction;
import tonegod.gui.framework.core.AnimElement;
import tonegod.gui.framework.core.QuadData;
import tonegod.gui.framework.core.TextureRegion;
import tonegod.gui.framework.core.Transformable;

/**
 *
 * @author t0neg0d
 */
public class ElementEmitter implements Control, Transformable {
	private List<TemporalAction> actions = new ArrayList<>();
	private Screen screen;
	private Application app;
	private int particlesPerSecond;
	private float targetInterval = 1f;
	private float currentInterval = 0f;
	private boolean isEnabled = false;
	private boolean isActive = true;
	private boolean centerVelocity = true;
	protected Map<String, Influencer> influencers = new LinkedHashMap<>();
	protected ElementParticle[] quads;
	protected AnimElement particles;
	private float emitterWidth, emitterHeight;
	private Vector2f emitterPosition = new Vector2f();
	private Texture tex, emitterShape;
	private ImageRaster ir;
	private ColorRGBA tempColor = new ColorRGBA();
	private Interpolation interpolation = Interpolation.linear;
	protected int activeParticleCount = 0;
	
	// Sprite Info
	private int spriteRows, spriteCols, spriteFPS;
	private int spriteWidth, spriteHeight;
	
	// Globals
	private boolean useFixedForce = false;
	private float minforce = .25f;
	private float maxforce = .25f;
	private boolean useFixedLife = false;
	private float highLife = .5f;
	private float lowLife = .1f;
	private boolean useFixedDirection = false;
	private Vector2f fixedDirection = new Vector2f(0,1);
	private float fixedDirectionStrength = 1f;
	
	private Node targetElement = null;
	private Node rootNode = null;
	
	protected boolean ignoreMouse = true;
	protected boolean isMovable = false;
	
	public ElementEmitter(Screen screen, Vector2f position, float emitterWidth, float emitterHeight) {
		
		this.screen = screen;
		this.app = screen.getApplication();
		this.emitterWidth = emitterWidth;
		this.emitterHeight = emitterHeight;
		this.emitterPosition.set(position);
		
		particles = new AnimElement(app.getAssetManager()) {
			@Override
			public void animElementUpdate(float tpf) {  }
		};
		particles.setOrigin(new Vector2f(0,0));
		particles.setPosition(0,0);
		particles.setRotation(0);
		particles.setScale(1,1);
		
		GravityInfluencer g = new GravityInfluencer(this);
		addInfluencer(g);
		DirectionInfluencer pd = new DirectionInfluencer(this);
		addInfluencer(pd);
		ColorInfluencer c = new ColorInfluencer(this);
		addInfluencer(c);
		SizeInfluencer s = new SizeInfluencer(this);
		addInfluencer(s);
		RotationInfluencer r = new RotationInfluencer(this);
		addInfluencer(r);
		ImpulseInfluencer i = new ImpulseInfluencer(this);
		addInfluencer(i);
		AlphaInfluencer a = new AlphaInfluencer(this);
		addInfluencer(a);
		SpriteInfluencer sp = new SpriteInfluencer(this);
		addInfluencer(sp);
	}
	
	/**
	 * Add the specified influencer to the emitter's influencer chain.
	 * @param influencer which influencer to add
	 */
	public final void addInfluencer(Influencer influencer) {
		influencers.put(influencer.getClass().getName(), influencer);
	}
	
	/**
	 * Access the first instance of the specified Influencer class.
         * @param <T> type of influencer
	 * @param c Influencer class to find
	 * @return a pre-existing instance or null
	 */
        @SuppressWarnings("unchecked")
	public <T extends Influencer> T getInfluencer(Class<T> c) {
		return (T) influencers.get(c.getName());
	}
	
	/**
	 * Removes the first instance of the provided Influencer class
	 * @param c Influencer class to remove
	 */
	public void removeInfluencer(Class c) {
		influencers.remove(c.getName());
	}
	
	public void addInfluencer(String key, Influencer influencer) {
		influencers.put(key, influencer);
	}
	
	public void setSprite(String spriteImagePath, int spriteRows, int spriteCols, int spriteFPS) {
		this.spriteRows = spriteRows;
		this.spriteCols = spriteCols;
		this.spriteFPS = spriteFPS;
		
		tex = app.getAssetManager().loadTexture(spriteImagePath);
		particles.setTexture(tex);
		
		spriteWidth = tex.getImage().getWidth()/spriteCols;
		spriteHeight = tex.getImage().getHeight()/spriteRows;
		
		int index = 0;
		for (int y = spriteRows-1; y > -1; y--) {
			for (int x = 0; x < spriteCols; x++) {
				particles.addTextureRegion("sprite" + index, spriteWidth*x, spriteHeight*y, spriteWidth, spriteHeight);
				index++;
			}
		}
	}
	
	public void setSprite(Texture texSprite, int spriteRows, int spriteCols, int spriteFPS) {
		this.tex = texSprite;
		this.spriteRows = spriteRows;
		this.spriteCols = spriteCols;
		this.spriteFPS = spriteFPS;
		
		particles.setTexture(tex);
		
		spriteWidth = tex.getImage().getWidth()/spriteCols;
		spriteHeight = tex.getImage().getHeight()/spriteRows;
		
		int index = 0;
		for (int y = spriteRows-1; y > -1; y--) {
			for (int x = 0; x < spriteCols; x++) {
				particles.addTextureRegion("sprite" + index, spriteWidth*x, spriteHeight*y, spriteWidth, spriteHeight);
				index++;
			}
		}
	}
	
	public int getSpriteRowCount() { return this.spriteRows; }
	
	public int getSpriteColCount() { return this.spriteCols; }
	
	public int getSpritesPerSecond() { return this.spriteFPS; }
	
	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}
	
	public Interpolation getInterpolation() {
		return this.interpolation;
	}
	
	public void setEmitterShape(Texture texture) {
		emitterShape = texture;
		ir = ImageRaster.create(emitterShape.getImage());
	}
	
	public void setEmitterShape(String texturePath) {
		emitterShape = app.getAssetManager().loadTexture(texturePath);
		ir = ImageRaster.create(emitterShape.getImage());
	}
	
	public void clearEmitterShape() {
		this.emitterShape = null;
	}
	
	public void setEmitterWidth(float emitterWidth) {
		this.emitterWidth = emitterWidth;
	}
	
	public float getEmitterWidth() {
		return this.emitterWidth;
	}
	
	public void setEmitterHeight(float emitterHeight) {
		this.emitterHeight = emitterHeight;
	}
	
	public float getEmitterHeight() {
		return this.emitterHeight;
	}
	
	@Override
	public void update(float tpf) {
		for (TemporalAction a : actions) {
			a.act(tpf);
		}
		for (TemporalAction a : actions) {
			if (a.getTime() >= a.getDuration()) {
				actions.remove(a);
				break;
			}
		}
		for (ElementParticle p : quads) {
			if (p.active) {
				p.update(tpf);
			}
		}
		particles.update(tpf);
		if (isEnabled) {
			if (isActive) {
				currentInterval += tpf;
				if (currentInterval >= targetInterval) {
					int numParticles = (int)(currentInterval/targetInterval);
					emitNextParticle(numParticles);
					currentInterval -= targetInterval;
				}
			}
		}
	}
	
	public void resetCurrentInterval() { currentInterval = 0; }
	
	public void setCurrentIntervalToTarget() {
		currentInterval = targetInterval;
	}
	
	public Node getTargetElement() {
		return this.targetElement;
	}
	
	public Node getRootNode() {
		return this.rootNode;
	}
	
	public void setMaxParticles(int maxParticles) {
		quads = new ElementParticle[maxParticles];
		for (int i = 0; i < maxParticles; i++) {
			ElementParticle p = new ElementParticle();
			String key = "sprite0";// + (FastMath.nextRandomInt(0, particles.getTextureRegions().size()-1));
			TextureRegion region = particles.getTextureRegion(key);
			p.particle = particles.addQuad(String.valueOf(i), key,
				new Vector2f(-region.getRegionWidth(),-region.getRegionHeight()),
				new Vector2f(region.getRegionWidth()/2,region.getRegionHeight()/2)
			);
			p.particle.userIndex = i;
			p.initialize(true);
			quads[i] = p;
		}
	//	particles.centerQuads();
		particles.initialize();
		particles.update(0);
		particles.updateModelBound();
	}
	
	public void setParticlesPerSecond(int particlesPerSecond) {
		this.particlesPerSecond = particlesPerSecond;
		this.targetInterval = 1f/particlesPerSecond;
		currentInterval = 0;
	}
	
	public int getParticlesPerSecond() {
		return this.particlesPerSecond;
	}
	
	public void startEmitter() {
		startEmitter(null);
	}
	
	public void startEmitter(Node targetElement) {
		this.targetElement = targetElement;
		if (targetElement == null)	screen.getGUINode().attachChild(particles);
		else						targetElement.attachChild(particles);
		rootNode = screen.getGUINode();
		rootNode.addControl(this);
		this.isEnabled = true;
		currentInterval = 0;
		update(0.001f);
	}
	
	public void stopEmitter() {
		destroyEmitter();
	}
	
	public void destroyEmitter() {
		this.isEnabled = false;
		particles.removeFromParent();
		rootNode.removeControl(this);
	//	try { screen.getGUINode().removeControl(this); }
	//	catch (Exception ex) {  }
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean getIsActive() { return this.isActive; }
	
	public boolean getIsEnabled() { return this.isEnabled; }
	
	private void emitNextParticle(int numParticles) {
		for (ElementParticle p : quads) {
			if (!p.particle.getIsVisible()) {
				p.initialize(false);
				activeParticleCount++;
				numParticles--;
				if (numParticles == 0) {
					break;
				}
			}
		}
	}

	public ElementParticle emitSingleParticle() {
		ElementParticle particleEmitted = null;
		for (ElementParticle p : quads) {
			if (!p.particle.getIsVisible()) {
				p.initialize(false);
				activeParticleCount++;
				particleEmitted = p;
				break;
			}
		}
		return particleEmitted;
	}

	public void setUseFixedForce(boolean useFixedForce) {
		this.useFixedForce = useFixedForce;
	}
	
	public boolean getUseFixedForce() {
		return this.useFixedForce;
	}
	
	public float getMinForce() {
		return minforce/100f;
	}

	public float getMaxForce() {
		return maxforce/100f;
	}

	public void setForce(float force) {
		this.minforce = force*100f;
		this.maxforce = force*100f;
	}

	public void setMinForce(float minforce) {
		this.minforce = minforce*100f;
	}

	public void setMaxForce(float maxforce) {
		this.maxforce = maxforce*100f;
	}

	public void setMinMaxForce(float minforce, float maxforce) {
		this.minforce = minforce*100f;
		this.maxforce = maxforce*100f;
	}

	public void setUseFixedLife(boolean useFixedLife) {
		this.useFixedLife = useFixedLife;
	}
	
	public boolean getUseFixedLife() {
		return this.useFixedLife;
	}
	
	public float getHighLife() {
		return highLife;
	}

	public void setHighLife(float highLife) {
		this.highLife = highLife;
	}

	public float getLowLife() {
		return lowLife;
	}

	public void setLowLife(float lowLife) {
		this.lowLife = lowLife;
	}

	public void setLife(float life) {
		this.lowLife = life;
		this.highLife = life;
	}

	public void setLowHighLife(float lowlife, float highlife) {
		this.lowLife = lowlife;
		this.highLife = highlife;
	}

	public void setUseFixedDirection(boolean useFixedDirection) {
		this.useFixedDirection = useFixedDirection;
	}
	
	public void setUseFixedDirection(boolean useFixedDirection, Vector2f fixedDirection) {
		this.useFixedDirection = useFixedDirection;
		this.fixedDirection.set(fixedDirection).normalizeLocal();
	}
	
	public void setFixedDirection(Vector2f fixedDirection) {
		this.fixedDirection.set(fixedDirection).normalizeLocal();
	}
	
	public boolean getUseFixedDirection() { return this.useFixedDirection; }
	
	public Vector2f getFixedDirection() { return this.fixedDirection; }
	
	public void setFixedDirectionStrength(float fixedDirectionStrength) {
		this.fixedDirectionStrength = fixedDirectionStrength;
	}
	
	public float getFixedDirectionStrength() {
		return this.fixedDirectionStrength;
	}
	
	public AnimElement getParticles() {
		return this.particles;
	}
	
	public ElementParticle getParticle(int index) {
		if (index > -1 && index < quads.length)
			return quads[index];
		else
			return null;
	}
	
	public void removeParticle(int index) {
		if (index > -1 && index < quads.length)
			quads[index].killParticle();
	}
	
	public void removeParticle(ElementParticle p) {
		int index = 0;
		for (ElementParticle particle : quads) {
			if (particle == p) {
				quads[index].killParticle();
				break;
			}
			index++;
		}
	}
	
	public void removeAllParticles() {
		for (ElementParticle p : quads) {
			p.killParticle();
		}
	}
	
	public void emitAllParticles() {
		for (ElementParticle p : quads) {
			if (!p.active) {
				p.initialize(false);
				activeParticleCount++;
			}
		}
	}
	
	public void emitNumParticles(int count) {
		for (ElementParticle p : quads) {
			if (count > 0) {
				if (!p.active) {
					p.initialize(false);
					activeParticleCount++;
					count--;
				}
			} else
				break;
		}
	}
	
	public void setCenterVelocity(boolean center) {
		this.centerVelocity = center;
	}
	
	public int getActiveParticleCount() { return activeParticleCount; }
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return this;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		
	}

	@Override
	public void render(RenderManager rm, ViewPort vp) {
		
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void read(JmeImporter im) throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	//<editor-fold desc="TRANSFORMABLE">
	@Override
	public void setPositionX(float x) {
		this.emitterPosition.x = x;
	}
	@Override
	public void setPositionY(float y) {
		this.emitterPosition.y = y;
	}
	@Override
	public void setPosition(float x, float y) {
		this.emitterPosition.set(x,y);
	}
	@Override
	public void setPosition(Vector2f pos) {
		this.emitterPosition.set(pos);
	}
	@Override
	public void setRotation(float rotation) {
		this.particles.setRotation(rotation);
	}
	@Override
	public void setScaleX(float scaleX) {
		this.particles.setScaleX(scaleX);
	}
	@Override
	public void setScaleY(float scaleY) {
		this.particles.setScaleY(scaleY);
	}
	@Override
	public void setScale(float x, float y) {
		this.particles.setScale(x,y);
		
	}
	@Override
	public void setScale(Vector2f scale) {
		this.particles.setScale(scale);
	}
	@Override
	public void setOrigin(float x, float y) {
		this.particles.setOrigin(x,y);
	}
	@Override
	public void setOrigin(Vector2f origin) {
		this.particles.setOrigin(origin);
	}
	@Override
	public void setOriginX(float originX) {
		this.particles.setOriginX(originX);
	}
	@Override
	public void setOriginY(float originY) {
		this.particles.setOriginY(originY);
	}
	@Override
	public void setColor(ColorRGBA color) {
		this.particles.setColor(color);
	}
	@Override
	public void setColorR(float r) {
		this.particles.setColorR(r);
	}
	@Override
	public void setColorG(float g) {
		this.particles.setColorG(g);
	}
	@Override
	public void setColorB(float b) {
		this.particles.setColorB(b);
	}
	@Override
	public void setColorA(float a) {
		this.particles.setColorA(a);
	}
	@Override
	public void setTCOffsetX(float x) {
		
	}
	@Override
	public void setTCOffsetY(float y) {
		
	}
	@Override
	public void setDimensions(Vector2f dim) {
		this.particles.setDimensions(dim);
	}
	@Override
	public void setDimensions(float w, float h) {
		this.particles.setDimensions(w,h);
	}
	@Override
	public void setWidth(float w) {
		this.particles.setWidth(w);
	}
	@Override
	public void setHeight(float h) {
		this.particles.setHeight(h);
	}
	@Override
	public void setIgnoreMouse(boolean ignoreMouse) {
		this.ignoreMouse = ignoreMouse;
	}
	@Override
	public void setIsMovable(boolean isMovable) {
		this.isMovable = isMovable;
	}
	@Override
	public float getPositionX() {
		return emitterPosition.x;
	}
	@Override
	public float getPositionY() {
		return emitterPosition.y;
	}
	@Override
	public float getRotation() {
		return particles.getRotation();
	}
	@Override
	public float getScaleX() {
		return particles.getScaleX();
	}
	@Override
	public float getScaleY() {
		return particles.getScaleY();
	}
	@Override
	public Vector2f getOrigin() {
		return this.particles.getOrigin();
	}
	@Override
	public float getOriginX() {
		return this.particles.getOriginX();
	}
	@Override
	public float getOriginY() {
		return this.particles.getOriginY();
	}
	@Override
	public ColorRGBA getColor() {
		return this.particles.getColor();
	}
	@Override
	public float getColorR() {
		return this.particles.getColorR();
	}
	@Override
	public float getColorG() {
		return this.particles.getColorG();
	}
	@Override
	public float getColorB() {
		return this.particles.getColorB();
	}
	@Override
	public float getColorA() {
		return this.particles.getColorA();
	}
	@Override
	public float getWidth() {
		return this.particles.getWidth();
	}
	@Override
	public float getHeight() {
		return this.particles.getHeight();
	}
	@Override
	public float getTCOffsetX() {
		return this.particles.getTCOffsetX();
	}
	@Override
	public float getTCOffsetY() {
		return this.particles.getTCOffsetY();
	}
	@Override
	public void setPositionZ(float z) { this.particles.setPositionZ(z); }
	@Override
	public float getPositionZ() {
		return this.particles.getPositionZ();
	}
	@Override
	public Vector2f getPosition() { return this.particles.getPosition(); }
	@Override
	public Vector2f getScale() { return this.particles.getScale(); }
	@Override
	public Vector2f getDimensions() {
		return this.particles.getDimensions();
	}
	@Override
	public Vector2f getTCOffset() {
		return this.particles.getTCOffset();
	}
	@Override
	public void setSkew(Vector2f skew) {
		this.particles.setSkew(skew);
	}
	@Override
	public void setSkew(float x, float y) {
		this.particles.setSkew(x,y);
	}
	@Override
	public void setSkewX(float x) {
		this.particles.setSkewX(x);
	}
	@Override
	public void setSkewY(float y) {
		this.particles.setSkewY(y);
	}
	@Override
	public Vector2f getSkew() {
		return particles.getSkew();
	}
	@Override
	public float getSkewX() {
		return particles.getSkewX();
	}
	@Override
	public float getSkewY() {
		return particles.getSkewY();
	}
	@Override
	public boolean getIgnoreMouse() {
		return this.ignoreMouse;
	}
	@Override
	public boolean getIsMovable() {
		return this.isMovable;
	}
	
	@Override
	public void addAction(TemporalAction action) {
		action.setTransformable(this);
		actions.add(action);
	}
	@Override
	public boolean getContainsAction(TemporalAction action) {
		return actions.contains(action);
	}
	//</editor-fold>
	
	public class ElementParticle {
		public QuadData particle;
		public Vector2f initialPosition = new Vector2f();
		public Vector2f position = new Vector2f();
		public Vector2f velocity = new Vector2f();
		public float randforce;
		public ColorRGBA color = new ColorRGBA();
		public float size = 1;
		public float life;
		public float startlife;
		public float angle;
		public float rotateSpeed;
		public boolean rotateDir;
		public boolean active = false;
		public float blend;
		private Map<String,Object> data = new HashMap<>();
		private float diffX, diffY;
		
		public void update(float tpf) {
			life -= tpf;
			blend = (startlife - life) / startlife;
			if (interpolation != null) blend = interpolation.apply(blend);
			if (life <= 0) {
				killParticle();
				return;
			}
			
			for (Influencer inf : influencers.values()) {
				if (inf.getIsEnabled())
					inf.update(this, tpf);
			}
			
			particle.setPosition(position);
			particle.setScaleX(size);
			particle.setScaleY(size);
			particle.getOrigin().set(spriteWidth*0.5f,spriteHeight*0.5f);
			particle.setColor(color);
			particle.setRotation(angle);
		};
		
		public void initialize(boolean hide) {
			diffX = FastMath.rand.nextFloat();
			diffY = FastMath.rand.nextFloat();
			
			if (emitterShape != null) {
				ir.getPixel((int)(diffX*(emitterShape.getImage().getWidth())),(int)(diffY*(emitterShape.getImage().getHeight())), tempColor);
				while (tempColor.r < 0.2f) {
					diffX = FastMath.rand.nextFloat();
					diffY = FastMath.rand.nextFloat();
					ir.getPixel((int)(diffX*(emitterShape.getImage().getWidth())),(int)(diffY*(emitterShape.getImage().getHeight())), tempColor);
				}
			}
			
			diffX *= emitterWidth;
			diffY *= emitterHeight;
			
			position.set(emitterPosition);
			position.subtractLocal(spriteWidth*0.5f,spriteHeight*0.5f);
			position.addLocal(diffX,diffY);
			initialPosition.set(position);
			
			if (!useFixedForce)
				randforce = (FastMath.nextRandomFloat()*(maxforce-minforce))+minforce;
			else
				randforce = maxforce;
			
			if (!centerVelocity) {
				float velX = FastMath.rand.nextFloat();
				if (FastMath.rand.nextBoolean()) velX = -velX;
				float velY = FastMath.rand.nextFloat();
				if (FastMath.rand.nextBoolean()) velY = -velY;
				velocity.set(velX,velY);
			} else {
				velocity.set(1/emitterWidth*diffX, 1/emitterHeight*diffY);
				velocity.subtractLocal(0.5f,0.5f);
			}
			
			if (useFixedDirection) {
                                Portability.interpolate(velocity, 
                                        fixedDirection, fixedDirectionStrength, 
                                        velocity);
                        }		
			velocity.multLocal(randforce);
			
			if (useFixedLife)
				life = highLife;
			else {
				startlife = (highLife - lowLife) * FastMath.nextRandomFloat() + lowLife ;
				life = startlife;
			}
			rotateDir = FastMath.rand.nextBoolean();
			rotateSpeed = FastMath.rand.nextFloat();
			size = 1;
			
			for (Influencer inf : influencers.values()) {
				inf.initialize(this);
			}
			
			active = !hide;
			if (hide)	particle.hide();
			else		particle.show();
			update(0);
		}
		
		public void killParticle() {
			active = false;
			activeParticleCount--;
			particle.hide();
		}
		
		public void putData(String key, Object object) {
			data.put(key, object);
		}
		
		public Object getData(String key) {
			return data.get(key);
		}
	}
}
