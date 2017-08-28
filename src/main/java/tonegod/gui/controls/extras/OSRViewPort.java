/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.OSRBridge;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.listeners.MouseButtonListener;
import tonegod.gui.listeners.MouseFocusListener;
import tonegod.gui.listeners.MouseMovementListener;
import tonegod.gui.listeners.MouseWheelListener;

/**
 *
 * @author t0neg0d
 */
public class OSRViewPort extends Element implements MouseButtonListener, MouseMovementListener, MouseWheelListener, MouseFocusListener {
	private OSRBridge bridge;
	private boolean rotateEnabled = true;
	private boolean useLeftMouseRotate = false;
	private boolean zoomEnabled = true;
	private boolean enabled = false;
	private boolean mouseLook = false;
	private int lastX = 0, lastY = 0;
	private Element elOverlay;
	
	/**
	 * Creates a new instance of the OSRViewPort control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public OSRViewPort(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the OSRViewPort control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public OSRViewPort(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the OSRViewPort control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public OSRViewPort(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the OSRViewPort control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Element
	 */
	public OSRViewPort(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the OSRViewPort control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public OSRViewPort(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the OSRViewPort control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public OSRViewPort(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the OSRViewPort control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public OSRViewPort(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, null);
		
		if (defaultImg != null) {
			elOverlay = new Element(
				screen,
				UID + ":Overlay",
				new Vector2f(0,0),
				dimensions.clone(),
				new Vector4f(0,0,0,0),
				defaultImg
			);
			elOverlay.setScaleNS(true);
			elOverlay.setScaleEW(true);
			elOverlay.setDocking(Docking.NW);
			elOverlay.setIsResizable(true);
			elOverlay.setIsMovable(false);
			elOverlay.setIgnoreMouse(true);

			addChild(elOverlay);
		}
	}
	
	/**
	 * Creates an instance of the OSRBridge class for rendering an off-screen ViewPort
	 * @param root The Node containing the scene to render
	 * @param width The render width
	 * @param height The render height
	 */
	public void setOSRBridge(Node root, int width, int height) {
		bridge = new OSRBridge(screen.getApplication().getRenderManager(), width, height, root);
		addOSRBridge(bridge);
		bridge.getChaseCamera().setDragToRotate(true);
		bridge.getChaseCamera().setHideCursorOnRotate(false);
	}

	/**
	 * Alters whether the rotation control responds to the left mouse button.
	 * @param useLeftMouseRotate true &rarr; respond, false &rarr; don't respond
	 */
	public void setLeftMouseButtonRotation(boolean useLeftMouseRotate) {
		this.useLeftMouseRotate = useLeftMouseRotate;
	}
	
	/**
	 * Sets the background color of the OSRViewPort (default is transparent)
	 * @param color desired background color
	 */
	public void setBackgroundColor(ColorRGBA color) {
		bridge.getViewPort().setBackgroundColor(color);
	}
	
	/**
	 * Sets the default distance between the camera and the focus node
	 * @param distance desired distance
	 */
	public void setCameraDistance(float distance) {
		bridge.getChaseCamera().setDefaultDistance(distance);
	}

	/**
	 * Alter the default horizontal rotation of the camera.
	 * @param angleInRads desired rotation angle (in radians)
	 */
	public void setCameraHorizonalRotation(float angleInRads) {
		bridge.getChaseCamera().setDefaultHorizontalRotation(angleInRads);
	}

	/**
	 * Alter the default vertical rotation of the camera.
	 * @param angleInRads desired angle (in radians)
	 */
	public void setCameraVerticalRotation(float angleInRads) {
		bridge.getChaseCamera().setDefaultVerticalRotation(angleInRads);
	}

	/**
	 * Alter the minimum distance between the camera and the focus node.
	 * @param distance zoom distance
	 */
	public void setCameraMinDistance(float distance) {
		bridge.getChaseCamera().setMinDistance(distance);
	}

	/**
	 * Alter the maximum distance between the camera and the focus node.
         * @param distance zoom distance
	 */
	public void setCameraMaxDistance(float distance) {
		bridge.getChaseCamera().setMaxDistance(distance);
	}

	/**
	 * Alter the minimum limit for vertical camera rotation.
	 * @param angleInRads desired minimum angle (in radians)
	 */
	public void setCameraMinVerticalRotation(float angleInRads) {
		bridge.getChaseCamera().setMinVerticalRotation(angleInRads);
	}

	/**
	 * Sets the maximum limit for vertical camera rotation
	 * @param angleInRads desired maximum angle (in radians)
	 */
	public void setCameraMaxVerticalRotation(float angleInRads) {
		bridge.getChaseCamera().setMaxVerticalRotation(angleInRads);
	}
	
	/**
	 * Enable/disable camera rotation control for the OSRViewPort.
	 * @param rotateEnabled true &rarr; enable, false &rarr; disable
	 */
	public void setUseCameraControlRotate(boolean rotateEnabled) {
		this.rotateEnabled = rotateEnabled;
	}
	
	/**
	 * Enable/disable camera zoom control for the OSRViewPort
	 * @param zoomEnabled true &rarr; enable, false &rarr; disable
	 */
	public void setUseCameraControlZoom(boolean zoomEnabled) {
		this.zoomEnabled = zoomEnabled;
	}
	
	/**
	 * Returns the OSRBridge instance for the OSRViewPort
	 * @return OSRBridge
	 */
	public OSRBridge getOSRBridge() {
		return this.bridge;
	}
	
	@Override
	public void controlShowHook() {
		bridge.getViewPort().setEnabled(true);
	}
	@Override
	public void controlHideHook() {
		bridge.getViewPort().setEnabled(false);
	}
	
	@Override
	public void onMouseLeftPressed(MouseButtonEvent evt) {
		if (Screen.isAndroid()) {
			this.enabled = true;
			setHasFocus(true);
		}
		if (rotateEnabled && useLeftMouseRotate) {
			mouseLook = true;
			if (!Screen.isAndroid()) screen.getApplication().getInputManager().setCursorVisible(false);
			bridge.getChaseCamera().onAction("ChaseCamToggleRotate", evt.isPressed(), bridge.getCurrentTPF());
		}
		evt.setConsumed();
	}
	@Override
	public void onMouseLeftReleased(MouseButtonEvent evt) {
		if (rotateEnabled && useLeftMouseRotate) {
			mouseLook = false;
			if (!Screen.isAndroid()) screen.getApplication().getInputManager().setCursorVisible(true);
			bridge.getChaseCamera().onAction("ChaseCamToggleRotate", evt.isPressed(), bridge.getCurrentTPF());
		}
		evt.setConsumed();
	}
	@Override
	public void onMouseRightPressed(MouseButtonEvent evt) {
		if (rotateEnabled && !useLeftMouseRotate) {
			mouseLook = true;
			if (!Screen.isAndroid()) screen.getApplication().getInputManager().setCursorVisible(false);
			bridge.getChaseCamera().onAction("ChaseCamToggleRotate", evt.isPressed(), bridge.getCurrentTPF());
		}
		evt.setConsumed();
	}
	@Override
	public void onMouseRightReleased(MouseButtonEvent evt) {
		if (Screen.isAndroid()) {
			if (!mouseLook)
				this.enabled = false;
			setHasFocus(false);
		}
		if (rotateEnabled && !useLeftMouseRotate) {
			mouseLook = false;
			if (!Screen.isAndroid()) screen.getApplication().getInputManager().setCursorVisible(true);
			bridge.getChaseCamera().onAction("ChaseCamToggleRotate", evt.isPressed(), bridge.getCurrentTPF());
		}
		evt.setConsumed();
	}
	@Override
	public void onMouseMove(MouseMotionEvent evt) {
		if (mouseLook) {
			if (enabled) {
				if (evt.getY() > lastY)
					bridge.getChaseCamera().onAnalog("ChaseCamUp", -evt.getDY()*(bridge.getCurrentTPF()/2), bridge.getCurrentTPF());
				else
					bridge.getChaseCamera().onAnalog("ChaseCamDown", evt.getDY()*(bridge.getCurrentTPF()/2), bridge.getCurrentTPF());
				if (evt.getX() > lastX)
					bridge.getChaseCamera().onAnalog("ChaseCamMoveRight", evt.getDX()*(bridge.getCurrentTPF()/2), bridge.getCurrentTPF());
				else
					bridge.getChaseCamera().onAnalog("ChaseCamMoveLeft", -evt.getDX()*(bridge.getCurrentTPF()/2), bridge.getCurrentTPF());
			}
			lastX = evt.getX();
			lastY = evt.getY();
			evt.setConsumed();
		}
	}
	@Override
	public void onMouseWheelPressed(MouseButtonEvent evt) {  }
	@Override
	public void onMouseWheelReleased(MouseButtonEvent evt) {  }
	@Override
	public void onMouseWheelUp(MouseMotionEvent evt) {
		if (zoomEnabled) {
			if (enabled) {
				bridge.getChaseCamera().onAnalog("ChaseCamZoomIn", evt.getDeltaWheel()*(bridge.getCurrentTPF()/4), bridge.getCurrentTPF());
			}
		}
		evt.setConsumed();
	}
	@Override
	public void onMouseWheelDown(MouseMotionEvent evt) {
		if (zoomEnabled) {
			if (enabled) {
				bridge.getChaseCamera().onAnalog("ChaseCamZoomIn", evt.getDeltaWheel()*(bridge.getCurrentTPF()/4), bridge.getCurrentTPF());
			}
		}
		evt.setConsumed();
	}
	@Override
	public void onGetFocus(MouseMotionEvent evt) {
		this.enabled = true;
		setHasFocus(true);
	}
	@Override
	public void onLoseFocus(MouseMotionEvent evt) {
		if (!mouseLook)
			this.enabled = false;
		setHasFocus(false);
	}
}
