package tonegod.gui.controls.extras;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import java.util.logging.Logger;

/**
 * Utility methods for portability between jMonkeyEngine versions 3.0 and 3.1.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class Portability {
    // *************************************************************************
    // constants

    /**
     * message logger for this class
     */
    final private static Logger logger = Logger.getLogger(
            Portability.class.getName());
    // *************************************************************************
    // constructors

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private Portability() {
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Interpolate between two vectors, storing the result in a 3rd vector.
     *
     * @param baseVector 1st vector (not null, unaffected)
     * @param goalVector 2nd vector (not null, unaffected)
     * @param goalFraction weight given to 2nd vector (typically &ge;0, &le;1)
     * @param storeVector result stored here (not null, modified)
     *
     * @see com.jme3.math.Vector2f#interpolateLocal(com.jme3.math.Vector2f,
     * float)
     * @see com.jme3.math.Vector2f#interpolateLocal(com.jme3.math.Vector2f,
     * com.jme3.math.Vector2f, float)
     */
    public static void interpolate(Vector2f baseVector, Vector2f goalVector,
            float goalFraction, Vector2f storeVector) {
        float baseFraction = 1f - goalFraction;

        storeVector.x = baseFraction * baseVector.x
                + goalFraction * goalVector.x;
        storeVector.y = baseFraction * baseVector.y
                + goalFraction * goalVector.y;
    }

    /**
     * Interpolate between two colors, storing the result in a 3rd color.
     *
     * @param baseColor 1st color (not null, unaffected)
     * @param goalColor 2nd color (not null, unaffected)
     * @param goalFraction weight given to 2nd color (typically &ge;0, &le;1)
     * @param storeColor result stored here (not null, modified)
     *
     * @see com.jme3.math.ColorRGBA#interpolateLocal(com.jme3.math.ColorRGBA,
     * float)
     * @see com.jme3.math.ColorRGBA#interpolateLocal(com.jme3.math.ColorRGBA,
     * com.jme3.math.ColorRGBA, float)
     */
    public static void interpolate(ColorRGBA baseColor, ColorRGBA goalColor,
            float goalFraction, ColorRGBA storeColor) {
        float baseFraction = 1f - goalFraction;

        storeColor.r = baseFraction * baseColor.r
                + goalFraction * goalColor.r;
        storeColor.g = baseFraction * baseColor.g
                + goalFraction * goalColor.g;
        storeColor.b = baseFraction * baseColor.b
                + goalFraction * goalColor.b;
        storeColor.a = baseFraction * baseColor.a
                + goalFraction * goalColor.a;
    }
}
