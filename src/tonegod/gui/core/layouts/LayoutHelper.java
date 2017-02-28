/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core.layouts;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;

/**
 *
 * @author t0neg0d
 */
public class LayoutHelper {
	private static Vector4f	padding = new Vector4f(5,5,5,5);
	private static float	lfHeight = 20;
	private static Vector2f	absPos = new Vector2f(0,0),
							pos = new Vector2f(0,0),
							dim = new Vector2f(0,0);
	private static Vector2f	prevSS = new Vector2f(),
							prevQuad = new Vector2f(),
							ss = new Vector2f(),
							quad = new Vector2f();
	
	/**
	 * Sets the default padding between Elements
	 * @param pad 
	 */
	public static void setPadding(float pad) { padding.set(pad,pad,pad,pad); }
	/**
	 * Sets the default padding between Elements
	 * @param padLeft
	 * @param padRight
	 * @param padTop
	 * @param padBottom 
	 */
	public static void setPadding(float padLeft, float padRight, float padTop, float padBottom) { padding.set(padLeft,padRight,padTop,padBottom); }
	/**
	 * Sets the size of the line feed
	 * @param feed 
	 */
	public static void setLineFeedHeight(float feed) { lfHeight = feed; }
	/**
	 * Resets the position to 0,0
	 */
	public static void reset() { pos.set(0,0); }
	/**
	 * Resets the x position to 0
	 */
	public static void resetX() { pos.setX(0); }
	/**
	 * Resets the y position to 0
	 */
	public static void resetY() { pos.setY(0); }
	/**
	 * Advances the x position by el.getWidth()
	 * @param el 
	 */
	public static void advanceX(Element el) { advanceX(el, false); }
	/**
	 * Advances the x position by el.getWidth() + the default padding if true
	 * @param el
	 * @param pad 
	 */
	public static void advanceX(Element el, boolean pad) {
		pos.addLocal(el.getWidth(),0);
		if (pad)
			pos.addLocal(padding.x,0);
	}
	/**
	 * Advances the x position by the specified number of pixels
	 * @param x 
	 */
	public static void advanceX(float x) { pos.addLocal(x,0); }
	/**
	 * Advances the y position by el.getHeight()
	 * @param el 
	 */
	public static void advanceY(Element el) { advanceY(el, false, false); }
	/**
	 * Advances the y position by el.getHeight() + the default padding if true
	 * @param el
	 * @param pad 
	 */
	public static void advanceY(Element el, boolean pad) { advanceY(el, pad, false); }
	/**
	 * Advances the y position by el.getHeight() + the default padding OR the default line feed if true
	 * @param el
	 * @param pad
	 * @param lineFeed 
	 */
	public static void advanceY(Element el, boolean pad, boolean lineFeed) {
		pos.addLocal(0,el.getHeight());
		if (lineFeed)
			pos.addLocal(0, lfHeight);
		else if (pad)
			pos.addLocal(0, padding.y);
	}
	/**
	 * Advances the y position by the specified number of pixels
	 * @param y 
	 */
	public static void advanceY(float y) { pos.addLocal(0,y); }
	/**
	 * Sets the position to the specified x, y coords
	 * @param w
	 * @param h
	 * @return the updated position (pre-existing instance)
	 */
	public static Vector2f absPosition(float w, float h) {
		absPos.set(w, h);
		return absPos;
	}
	/**
	 * Access the set position Vector2f
	 * @return the pre-existing instance
	 */
	public static Vector2f position() { return pos; }
	/**
	 * Sets and returns the dimensions Vector2f to the specified width, height
	 * @param w
	 * @param h
	 * @return the updated dimensions (pre-existing instance)
	 */
	public static Vector2f dimensions(float w, float h) {
		dim.set(w, h);
		return dim;
	}
	/**
	 * Read the default padding between elements
	 * @return amount
	 */
	public static float pad() { return padding.x; }
	/**
	 * Returns the default line feed height
	 * @return height
	 */
	public static float feed() { return lfHeight; }
	/**
	 * Repositions the provided element using  quadrant aligned ratios
	 * @param screen The application's Screen instance
	 * @param prevResolution A Vector2f containing the previous screen resolution
	 * @param el The Element to reposition
	 */
	public static void reposition(ElementManager screen, Vector2f prevResolution, Element el) {
		prevSS.set(prevResolution);
		prevQuad.set(prevSS).divideLocal(3);
		ss.set(screen.getWidth(),screen.getHeight());
		quad.set(ss).divideLocal(3);
		
		float X = el.getPosition().x;
		float Y = el.getPosition().y;
		float centerX = (X+(el.getWidth()/2));
		float centerY = (Y+(el.getHeight()/2));
		int xQuad = (int)Math.floor(centerX/prevQuad.x);
		int yQuad = (int)Math.floor(centerY/prevQuad.y);
		float rightX = el.getAbsoluteWidth();
		float rightY = el.getAbsoluteHeight();
		
		float	nextX = 0,
				nextY = 0,
				ratioX = 0,
				ratioY = 0;
		
		switch (xQuad) {
			case 0:
				ratioX	= (X-(prevQuad.x*xQuad))/prevQuad.x;
				nextX	= (quad.x*xQuad)+(quad.x*ratioX);
				break;
			case 1:
				ratioX	= (centerX-(prevQuad.x*xQuad))/prevQuad.x;
				nextX	= (quad.x*xQuad)+(quad.x*ratioX)-(el.getWidth()/2);
				break;
			case 2:
				ratioX = (rightX-(prevQuad.x*xQuad))/prevQuad.x;
				nextX = (quad.x*xQuad)+(quad.x*ratioX)-el.getWidth();
				break;
		}
		switch (yQuad) {
			case 0:
				ratioY	= (Y-(prevQuad.y*yQuad))/prevQuad.y;
				nextY	= (quad.y*yQuad)+(quad.y*ratioY);
				break;
			case 1:
				ratioY	= (centerY-(prevQuad.y*yQuad))/prevQuad.y;
				nextY	= (quad.y*yQuad)+(quad.y*ratioY)-(el.getHeight()/2);
				break;
			case 2:
				ratioY = (rightY-(prevQuad.y*yQuad))/prevQuad.y;
				nextY = (quad.y*yQuad)+(quad.y*ratioY)-el.getHeight();
				break;
		}
		
		el.setPosition(nextX, nextY);
	}
}