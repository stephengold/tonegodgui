package tonegod.gui.core;

import java.util.logging.Logger;

/**
 * Version strings for the Tonegod GUI library.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TonegodGuiVersion {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final public static Logger logger
            = Logger.getLogger(TonegodGuiVersion.class.getName());
    // *************************************************************************
    // constructors

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private TonegodGuiVersion() {
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Read the terse version string for this library.
     *
     * @return branch and revision (not null, not empty)
     */
    public static String versionShort() {
        return "master 0.1.11";
    }
}
