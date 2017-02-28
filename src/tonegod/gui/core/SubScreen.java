package tonegod.gui.core;

import tonegod.gui.style.Style;
import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import tonegod.gui.controls.form.Form;
import tonegod.gui.controls.lists.ComboBox;
import tonegod.gui.controls.menuing.AutoHide;
import tonegod.gui.controls.menuing.Menu;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.util.ModalBackground;
import tonegod.gui.controls.util.ToolTip;
import tonegod.gui.core.Element.Borders;
import tonegod.gui.core.utils.ScaleUtil;
import tonegod.gui.style.StyleManager.CursorType;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.EffectManager;
import tonegod.gui.effects.cursor.CursorEffects;
import tonegod.gui.framework.core.AnimElement;
import tonegod.gui.framework.core.AnimLayer;
import tonegod.gui.framework.core.AnimManager;
import tonegod.gui.framework.core.QuadData;
import tonegod.gui.listeners.*;

/**
 *
 * @author t0neg0d
 */
public class SubScreen implements ElementManager, Control {
	private String UID;
	private Screen screen;
	private Application app;
	private SubScreenBridge bridge;
	private Geometry geom;
	
	protected Spatial spatial;
	private Map<String, Element> elements = new HashMap<>();
	private Ray elementZOrderRay = new Ray();
	private Vector3f guiRayOrigin = new Vector3f();
	
	private Vector2f tempElementOffset = new Vector2f();
	private Map<Integer,Vector2f> elementOffsets = new HashMap<>();
	private Map<Integer,Element> contactElements = new HashMap<>();
	private Map<Integer,Element> eventElements = new HashMap<>();
	private Map<Integer,Borders> eventElementResizeDirections = new HashMap<>();
	
	private Element eventElement = null;
	private Element targetElement = null;
	private Element keyboardElement = null;
	private Element tabFocusElement = null;
	private Form focusForm = null;
	private Vector2f eventElementOriginXY = new Vector2f();
	private float eventElementOffsetX = 0;
	private float eventElementOffsetY = 0;
	private float targetElementOffsetX = 0;
	private float targetElementOffsetY = 0;
	private Borders eventElementResizeDirection = null;
	private Element mouseFocusElement = null;
	private Element contactElement = null;
	private Element previousMouseFocusElement = null;
	private boolean focusElementIsMovable = false;
	private boolean mousePressed = false;
	private boolean mouseLeftPressed = false;
	private boolean mouseRightPressed = false;
	private boolean mouseWheelPressed = false;
	
	private float zOrderCurrent = .5f;
	private float zOrderStepMajor = .01f;
	private float zOrderStepMinor = 0.0001f;
	
	private String clipboardText = "";
	
	protected Node subScreenNode = new Node("t0neg0dGUI");
	private Material mat;
	
	private Vector2f mouseXY = new Vector2f(0,0);
	private boolean SHIFT = false;
	private boolean CTRL = false;
	private boolean ALT = false;
	
	private ElementQuadGrid mesh;
	
	// AnimLayer & 2D framework support
	private Map<String, AnimLayer> layers = new LinkedHashMap<>();
	private float layerZOrderCurrent = .4999f;
	private AnimElement eventAnimElement = null;
	private QuadData eventQuad = null;
	private AnimElement targetAnimElement = null;
	private QuadData targetQuad = null;
	private AnimElement mouseFocusAnimElement = null;
	private AnimElement previousMouseFocusAnimElement = null;
	private AnimElement mouseFocusQuad = null;
	private float eventAnimOffsetX = 0;
	private float eventAnimOffsetY = 0;
	private float eventQuadOffsetX = 0;
	private float eventQuadOffsetY = 0;
	
	/**
	 * Creates an instance of the SubScreen control.
	 * 
	 * @param screen The Application Screen class
         * @param geom geometry of the control
	 */
	public SubScreen(Screen screen, Geometry geom) {
		this(screen, UIDUtil.getUID(), geom);
	}
	
	/**
	 * Creates an instance of the SubScreen control.
	 * 
	 * @param screen The Application Screen class
	 * @param UID A Unique String ID for the SubScreen
         * @param geom geometry of the control
	 */
	public SubScreen(Screen screen, String UID, Geometry geom) {
		this.UID = UID;
		this.screen = screen;
		this.app = screen.getApplication();
		this.geom = geom;
		this.elementZOrderRay.setDirection(Vector3f.UNIT_Z);
	}
	
	public String getUID() {
		return this.UID;
	}
	
	public void setSubScreenBridge(int width, int height, Node root) {
		root.addControl(this);
		this.bridge = new SubScreenBridge(app.getRenderManager(), width, height, root);
		screen.getGUINode().addControl(this.bridge);
		mat = new Material(screen.getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("ColorMap", this.bridge.getTexture());
		geom.setMaterial(mat);
	}
	
	public Material getMaterial() {
		return mat;
	}
	
	public Texture getTexture() {
		return getSubScreenBridge().getTexture();
	}
	
	public Geometry getGeometry() {
		return this.geom;
	}
	
	public SubScreenBridge getSubScreenBridge() {
		return this.bridge;
	}
	
	public ElementQuadGrid getDefaultMesh() {
		return mesh;
	}
	
	/**
	 * Returns the JME application associated with the Screen
	 * @return Application app
	 */
	@Override
	public Application getApplication() {
		return this.app;
	}
	
	/**
	 * Return the width of the current Viewport
	 * 
	 * @return float width
	 */
	@Override
	public float getWidth() {
		return bridge.getCamera().getWidth();
	}
	
	/**
	 * Returns the height of the current Viewport
	 * 
	 * @return  float height
	 */
	@Override
	public float getHeight() {
		return bridge.getCamera().getHeight();
	}
	
	@Override
	public void update(float tpf) {  }

	@Override
	public void render(RenderManager rm, ViewPort vp) {  }
	
	/**
	 *  Adds an Element to the Screen and scene graph
	 * @param element The Element to add
	 */
	@Override
	public void addElement(Element element) {
		if (element instanceof AutoHide)
			element.hide();
		
		if (getElementById(element.getUID()) != null) {
		//	try {
		//		throw new ConflictingIDException();
		//	} catch (ConflictingIDException ex) {
		//		Logger.getLogger(Element.class.getName()).log(Level.SEVERE, "The child element '" + element.getUID() + "' (" + element.getClass() + ") conflicts with a previously added child element in parent Screen.", ex);
		//		System.exit(0);
		//	}
		} else {
			elements.put(element.getUID(), element);
			if (!element.getInitialized()) {
				element.setY(getHeight()-element.getHeight()-element.getY());
				element.orgPosition = element.getPosition().clone();
				element.orgPosition.setY(element.getY());
				element.setInitialized();
			}
			subScreenNode.attachChild(element);

			// Set initla z-order
			getNextZOrder(true);
		//	element.initZOrder(zOrderCurrent);
			element.resize(element.getX()+element.getWidth(), element.getY()+element.getHeight(), Borders.SE);
		}
	}
	
	/**
	 *  Adds an Element to the Screen and scene graph
	 * @param element The Element to add
	 */
	@Override
	public void addElement(Element element, boolean hide) {
		if (element instanceof AutoHide)
			element.hide();
		
		if (getElementById(element.getUID()) != null) {
		//	try {
		//		throw new ConflictingIDException();
		//	} catch (ConflictingIDException ex) {
		//		Logger.getLogger(Element.class.getName()).log(Level.SEVERE, "The child element '" + element.getUID() + "' (" + element.getClass() + ") conflicts with a previously added child element in parent Screen.", ex);
		//		System.exit(0);
		//	}
		} else {
			elements.put(element.getUID(), element);
			if (!element.getInitialized()) {
				element.setY(getHeight()-element.getHeight()-element.getY());
				element.orgPosition = element.getPosition().clone();
				element.orgPosition.setY(element.getY());
				element.setInitialized();
			}
			subScreenNode.attachChild(element);

			// Set initla z-order
			getNextZOrder(true);
		//	element.initZOrder(zOrderCurrent);
			element.resize(element.getX()+element.getWidth(), element.getY()+element.getHeight(), Borders.SE);
			
			if (hide)
				element.hide();
		}
	}
	
	/**
	 * Removes an Element from the Screen and scene graph
	 * @param element The Element to remove
	 */
	@Override
	public void removeElement(Element element) {
		elements.remove(element.getUID());
		float shiftZ = element.getLocalTranslation().getZ();
		for (Element el : elements.values()) {
			if (!(el instanceof ToolTip)) {
				if (el.getLocalTranslation().getZ() > shiftZ) {
					el.move(0,0,-zOrderStepMajor);
				}
			}
		}
		zOrderCurrent -= zOrderStepMajor;
		element.removeFromParent();
		element.cleanup();
	}
	
	/**
	 * Returns the Element with the associated ID.  If not found, returns null
	 * @param UID The String ID of Element to find
	 * @return Element element
	 */
	@Override
	public Element getElementById(String UID) {
		Element ret = null;
		if (elements.containsKey(UID)) {
			ret = elements.get(UID);
		} else {
			for (Element el : elements.values()) {
				ret = el.getChildElementById(UID);
				if (ret != null) {
					break;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Returns the guiNode used by the Screen
	 * @return Node
	 */
	@Override
	public Node getGUINode() {
		return subScreenNode;
	}
	
	@Override
	public Texture getAtlasTexture() { return screen.getAtlasTexture(); }
	
	@Override
	public Texture createNewTexture(String texturePath) {
		Texture newTex = app.getAssetManager().loadTexture(texturePath);
		newTex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
		newTex.setMagFilter(Texture.MagFilter.Bilinear);
		newTex.setWrap(Texture.WrapMode.Repeat);
		return newTex;
	}
	
	@Override
	public float[] parseAtlasCoords(String texturePath) {
		float[] coords = new float[4];
		
		if (texturePath != null) {
			StringTokenizer st = new StringTokenizer(texturePath, "|");
			if (st.countTokens() == 4) {
				try {
					String token = st.nextToken();
					coords[0] = Float.parseFloat(token.substring(token.indexOf('=')+1));
					token = st.nextToken();
					coords[1] = Float.parseFloat(token.substring(token.indexOf('=')+1));
					token = st.nextToken();
					coords[2] = Float.parseFloat(token.substring(token.indexOf('=')+1));
					token = st.nextToken();
					coords[3] = Float.parseFloat(token.substring(token.indexOf('=')+1));
				} catch (Exception ex) { throwParserException(); }
			} else throwParserException();
		}
		return coords;
	}
	
	private void throwParserException() {
		try {
			throw new java.text.ParseException("The provided texture information does not conform to the expected standard of ?x=(int)&y=(int)&w=(int)&h=(int)", 0);
		} catch (ParseException ex) {
			Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, "The provided texture information does not conform to the expected standard of ?x=(int)&y=(int)&w=(int)&h=(int)", ex);
		}
	}
	
	// Z-ORDER
	/**
	 * Returns the next available z-order
	 * @param stepMajor Return the z-order incremented by a major step if true, a minor step if false
	 * @return float zOrder
	 */
	public float getNextZOrder(boolean stepMajor) {
		if (stepMajor)
			zOrderCurrent += zOrderStepMajor;
		else
			zOrderCurrent += zOrderStepMinor;
		return zOrderCurrent;
	}
	
	/**
	 * Brings the element specified to the front of the zOrder list shifting other below to keep all
	 * Elements within the current z-order range.
	 * 
	 * @param topMost The Element to bring to the front
	 */
	@Override
	public void updateZOrder(Element topMost) {
	//	zOrderCurrent = zOrderInit;
		String topMostUID = topMost.getUID();
		float shiftZ = topMost.getLocalTranslation().getZ();
		
		for (Element el : elements.values()) {
			if (topMost.getIsGlobalModal()) {  }
			else if (topMost.getIsModal()) {  }
			else {
				if (!el.getIsGlobalModal() && !el.getIsModal()) {
					if (el.getLocalTranslation().getZ() > shiftZ) {
						el.move(0,0,-zOrderStepMajor);
					}
				}
			}
		}
		topMost.setLocalTranslation(topMost.getLocalTranslation().setZ(Float.valueOf(zOrderCurrent)));
	}
	
	/**
	 * Returns the zOrder major step value
	 * @return float
	 */
	@Override
	public float getZOrderStepMajor() {
		return this.zOrderStepMajor;
	}
	
	/**
	 * Returns the zOrder minor step value
	 * @return float
	 */
	@Override
	public float getZOrderStepMinor() {
		return this.zOrderStepMinor;
	}
	
	/**
	 * Stored the current mouse position as a Vector2f
	 * @param x The mouse's current X coord
	 * @param y The mouse's current Y coord
	 */
	private void setMouseXY(float x, float y) {
		mouseXY.set(x, y);
	}
	
	/**
	 * Returns a Vector2f containing the last stored mouse X/Y coords
	 * @return Vector2f mouseXY
	 */
	@Override
	public Vector2f getMouseXY() {
		return this.mouseXY;
	}
	
	@Override
	public Vector2f getTouchXY() {
		return this.screen.getTouchXY();
	}
	
	// Raw Input handlers
//	public void beginInput() {  }
//	public void endInput() {  }
//	public void onJoyAxisEvent(JoyAxisEvent evt) {  }
//	public void onJoyButtonEvent(JoyButtonEvent evt) {  }
	public void onMouseMotionEvent(MouseMotionEvent oldEvt, MouseMotionEvent evt) {
		setMouseXY(evt.getX(),evt.getY());
		if (!mousePressed) {
			mouseFocusElement = getEventElement(evt.getX(), evt.getY());
			if (mouseFocusElement != previousMouseFocusElement) {
				if (previousMouseFocusElement instanceof MouseFocusListener) {
					((MouseFocusListener)previousMouseFocusElement).onLoseFocus(evt);
				}
				if (mouseFocusElement instanceof MouseFocusListener) {
					((MouseFocusListener)mouseFocusElement).onGetFocus(evt);
				}
				previousMouseFocusElement = mouseFocusElement;
			}
			if (mouseFocusElement != null) {
				focusElementIsMovable = mouseFocusElement.getIsMovable();
				
				if (mouseFocusElement instanceof MouseWheelListener) {
					if (evt.getDeltaWheel() > 0) {
						((MouseWheelListener)mouseFocusElement).onMouseWheelDown(evt);
					} else if (evt.getDeltaWheel() < 0) {
						((MouseWheelListener)mouseFocusElement).onMouseWheelUp(evt);
					}
					oldEvt.setConsumed();
				}
			}
			if (mouseFocusElement instanceof MouseMovementListener) {
				((MouseMovementListener)mouseFocusElement).onMouseMove(evt);
			}
		} else {
			if (eventElement != null) {
				if (mouseLeftPressed) {
					focusElementIsMovable = contactElement.getIsMovable();
					if (eventElementResizeDirection != null) {
						eventElement.resize(evt.getX(), evt.getY(), eventElementResizeDirection);
					} else if (focusElementIsMovable) {
						eventElement.moveTo(evt.getX()-eventElementOffsetX, evt.getY()-eventElementOffsetY);
					}
				}

				if (eventElement instanceof MouseMovementListener) {
					((MouseMovementListener)eventElement).onMouseMove(evt);
				}
			}
		}
	}
	public void onMouseButtonEvent(MouseButtonEvent oldEvt, MouseButtonEvent evt) {
		if (evt.isPressed()) {
			mousePressed = true;
			eventElement = getEventElement(evt.getX(), evt.getY());
			if (eventElement != null) {
				if (eventElement.getResetKeyboardFocus())
					resetTabFocusElement();
			} else
				resetTabFocusElement();
			switch (evt.getButtonIndex()) {
				case 0:
					mouseLeftPressed = true;
				//	eventElement = getEventElement(evt.getX(), evt.getY());
					if (eventElement != null) {
						if (eventElement.getEffectZOrder())
							updateZOrder(eventElement.getAbsoluteParent());
						if (eventElement.getResetKeyboardFocus())
							this.setTabFocusElement(eventElement);
						if (eventElement.getIsDragDropDragElement())
							targetElement = null;
						if (eventElement.getIsResizable()) {
							float offsetX = evt.getX();
							float offsetY = evt.getY();
							Element el = eventElement;
							if (offsetX > el.getAbsoluteX() && offsetX < el.getAbsoluteX()+el.getResizeBorderWestSize()) {
								if (offsetY > el.getAbsoluteY() && offsetY < el.getAbsoluteY()+el.getResizeBorderNorthSize()) {
									eventElementResizeDirection = Borders.NW;
								} else if (offsetY > (el.getAbsoluteHeight()-el.getResizeBorderSouthSize()) && offsetY < el.getAbsoluteHeight()) {
									eventElementResizeDirection = Borders.SW;
								} else {
									eventElementResizeDirection = Borders.W;
								}
							} else if (offsetX > (el.getAbsoluteWidth()-el.getResizeBorderEastSize()) && offsetX < el.getAbsoluteWidth()) {
								if (offsetY > el.getAbsoluteY() && offsetY < el.getAbsoluteY()+el.getResizeBorderNorthSize()) {
									eventElementResizeDirection = Borders.NE;
								} else if (offsetY > (el.getAbsoluteHeight()-el.getResizeBorderSouthSize()) && offsetY < el.getAbsoluteHeight()) {
									eventElementResizeDirection = Borders.SE;
								} else {
									eventElementResizeDirection = Borders.E;
								}
							} else {
								if (offsetY > el.getAbsoluteY() && offsetY < el.getAbsoluteY()+el.getResizeBorderNorthSize()) {
									eventElementResizeDirection = Borders.N;
								} else if (offsetY > (el.getAbsoluteHeight()-el.getResizeBorderSouthSize()) && offsetY < el.getAbsoluteHeight()) {
									eventElementResizeDirection = Borders.S;
								}
							}
							if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
								if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
							}
							if (eventElement.getResetKeyboardFocus())
								keyboardElement = null;
						} else if (eventElement.getIsMovable() && eventElementResizeDirection == null) {
							eventElementResizeDirection = null;
							if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
								if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
							}
							if (eventElement.getResetKeyboardFocus())
								keyboardElement = null;
							eventElementOriginXY.set(eventElement.getPosition());
						} else if (eventElement instanceof KeyboardListener) {
							if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
								if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
							}
							if (eventElement.getResetKeyboardFocus())
								keyboardElement = eventElement;
							if (keyboardElement instanceof TextField) {
								((TextField)keyboardElement).setTabFocus();
								if (Screen.isAndroid()) screen.showVirtualKeyboard();
							//	((TextField)keyboardElement).setCaretPositionByX(evt.getX());
							}
							// TODO: Update target element's font shader
						} else {
							eventElementResizeDirection = null;
							if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
								if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
							}
							if (eventElement.getResetKeyboardFocus())
								keyboardElement = null;
						}
						if (eventElement instanceof MouseButtonListener) {
							((MouseButtonListener)eventElement).onMouseLeftPressed(evt);
						}
						if (keyboardElement == null)
							if (Screen.isAndroid()) screen.hideVirtualKeyboard();
						evt.setConsumed();
						oldEvt.setConsumed();
					} else {
						if (keyboardElement == null)
							if (Screen.isAndroid()) screen.hideVirtualKeyboard();
					}
					break;
				case 1:
					mouseRightPressed = true;
				//	eventElement = getEventElement(evt.getX(), evt.getY());
					if (eventElement != null) {
						if (eventElement.getEffectZOrder())
							updateZOrder(eventElement.getAbsoluteParent());
						if (eventElement instanceof MouseButtonListener) {
							((MouseButtonListener)eventElement).onMouseRightPressed(evt);
						}
						evt.setConsumed();
						oldEvt.setConsumed();
					}
					break;
				case 2:
					mouseWheelPressed = true;
				//	eventElement = getEventElement(evt.getX(), evt.getY());
					if (eventElement != null) {
						if (eventElement instanceof MouseWheelListener) {
							((MouseWheelListener)eventElement).onMouseWheelPressed(evt);
						}
						evt.setConsumed();
						oldEvt.setConsumed();
					}
					break;
			}
		} else if (evt.isReleased()) {
			handleMenuState();
			switch (evt.getButtonIndex()) {
				case 0:
					mouseLeftPressed = false;
					eventElementResizeDirection = null;
				//	if (eventElement.getIsDragDropDragElement())
					targetElement = getTargetElement(evt.getX(), evt.getY());
					if (eventElement instanceof MouseButtonListener) {
						((MouseButtonListener)eventElement).onMouseLeftReleased(evt);
					}
					if (eventElement != null) {
						evt.setConsumed();
						oldEvt.setConsumed();
					}
					break;
				case 1:
					mouseRightPressed = false;
					if (eventElement instanceof MouseButtonListener) {
						((MouseButtonListener)eventElement).onMouseRightReleased(evt);
					}
					if (eventElement != null) {
						evt.setConsumed();
						oldEvt.setConsumed();
					}
					break;
				case 2:
					mouseWheelPressed = false;
					if (eventElement instanceof MouseWheelListener) {
						((MouseWheelListener)eventElement).onMouseWheelReleased(evt);
					}
					if (eventElement != null) {
						evt.setConsumed();
						oldEvt.setConsumed();
					}
					break;
			}
			mousePressed = false;
			eventElement = null;
		}
	}
	@Override
	public void onKeyEvent(KeyInputEvent evt) {
		if (evt.getKeyCode() == KeyInput.KEY_LSHIFT || evt.getKeyCode() == KeyInput.KEY_RSHIFT) {
			if (evt.isPressed()) SHIFT = true;
			else SHIFT = false;
		}
		if (evt.getKeyCode() == KeyInput.KEY_LCONTROL || evt.getKeyCode() == KeyInput.KEY_RCONTROL) {
			if (evt.isPressed()) CTRL = true;
			else CTRL = false;
		}
		if (evt.getKeyCode() == KeyInput.KEY_LMENU || evt.getKeyCode() == KeyInput.KEY_RMENU) {
			if (evt.isPressed()) ALT = true;
			else ALT = false;
		}
		if (evt.getKeyCode() == KeyInput.KEY_TAB && evt.isPressed()) {
			if (focusForm != null) {
				if (!SHIFT)	focusForm.tabNext();
				else		focusForm.tabPrev();
			}
		} else {
			if (keyboardElement != null) {
				if (keyboardElement.getParent() != null && keyboardElement.getIsVisible()) {
					if (evt.isPressed()) {
						((KeyboardListener)keyboardElement).onKeyPress(evt);
					} else if (evt.isReleased()) {
						((KeyboardListener)keyboardElement).onKeyRelease(evt);
					}
				}
			}
		}
	}
	public void onTouchEvent(TouchEvent evt) {
	//	if (screen.useMultiTouch) {
			switch (evt.getType()) {
				case DOWN:
					androidTouchDownEvent(evt);
					break;
				case MOVE:
					androidTouchMoveEvent(evt);
					break;
				case UP:
					androidTouchUpEvent(evt);
					break;
			}
	//	}
	}
	
	private void androidTouchDownEvent(TouchEvent evt) {
		mousePressed = true;
		Element contact = getContactElement(evt.getX(), evt.getY());
		Vector2f offset = tempElementOffset.clone();
		Element target = getEventElement(evt.getX(), evt.getY());
		
		Borders dir = null;
		if (target != null) {
			if (target.getResetKeyboardFocus())
				resetTabFocusElement();

			if (target.getEffectZOrder())
				updateZOrder(target.getAbsoluteParent());
			if (target.getResetKeyboardFocus())
				this.setTabFocusElement(target);
			if (target.getIsDragDropDragElement())
				targetElement = null;
			if (target.getIsResizable()) {
				float offsetX = evt.getX();
				float offsetY = evt.getY();
				Element el = target;
				
				if (offsetX > el.getAbsoluteX() && offsetX < el.getAbsoluteX()+el.getResizeBorderWestSize()) {
					if (offsetY > el.getAbsoluteY() && offsetY < el.getAbsoluteY()+el.getResizeBorderNorthSize()) {
						dir = Borders.NW;
					} else if (offsetY > (el.getAbsoluteHeight()-el.getResizeBorderSouthSize()) && offsetY < el.getAbsoluteHeight()) {
						dir = Borders.SW;
					} else {
						dir = Borders.W;
					}
				} else if (offsetX > (el.getAbsoluteWidth()-el.getResizeBorderEastSize()) && offsetX < el.getAbsoluteWidth()) {
					if (offsetY > el.getAbsoluteY() && offsetY < el.getAbsoluteY()+el.getResizeBorderNorthSize()) {
						dir = Borders.NE;
					} else if (offsetY > (el.getAbsoluteHeight()-el.getResizeBorderSouthSize()) && offsetY < el.getAbsoluteHeight()) {
						dir = Borders.SE;
					} else {
						dir = Borders.E;
					}
				} else {
					if (offsetY > el.getAbsoluteY() && offsetY < el.getAbsoluteY()+el.getResizeBorderNorthSize()) {
						dir = Borders.N;
					} else if (offsetY > (el.getAbsoluteHeight()-el.getResizeBorderSouthSize()) && offsetY < el.getAbsoluteHeight()) {
						dir = Borders.S;
					}
				}
				if (keyboardElement != null && target.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (target.getResetKeyboardFocus())
					keyboardElement = null;
			} else if (target.getIsMovable() && dir == null) {
				dir = null;
				if (keyboardElement != null && target.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (target.getResetKeyboardFocus())
					keyboardElement = null;
				eventElementOriginXY.set(target.getPosition());
			} else if (target instanceof KeyboardListener) {
				if (keyboardElement != null && target.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (target.getResetKeyboardFocus())
					keyboardElement = target;
				if (keyboardElement instanceof TextField) {
					((TextField)keyboardElement).setTabFocus();
					screen.showVirtualKeyboard();
				}
			} else {
				dir = null;
				if (keyboardElement != null && target.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (target.getResetKeyboardFocus())
					keyboardElement = null;
			}
			if (target instanceof MouseButtonListener) {
				MouseButtonEvent mbEvt = new MouseButtonEvent(0,true,(int)evt.getX(),(int)evt.getY());
				((MouseButtonListener)target).onMouseLeftPressed(mbEvt);
			}
			if (target instanceof TouchListener) {
				((TouchListener)target).onTouchDown(evt);
			}
			if (keyboardElement == null)
				screen.hideVirtualKeyboard();
			evt.setConsumed();
			contactElements.put(evt.getPointerId(),contact);
			elementOffsets.put(evt.getPointerId(),offset);
			eventElements.put(evt.getPointerId(),target);
			eventElementResizeDirections.put(evt.getPointerId(), dir);
		} else {
			if (keyboardElement == null)
				screen.hideVirtualKeyboard();
			resetTabFocusElement();
		}
	}
	private void androidTouchMoveEvent(TouchEvent evt) {
		for (Integer key : eventElements.keySet()) {
			if (key == evt.getPointerId()) {
				Element target = eventElements.get(key);
				if (target != null) {
					Element contact = contactElements.get(key);
					Vector2f offset = elementOffsets.get(key);
					Borders dir = eventElementResizeDirections.get(key);
					
					boolean movable = contact.getIsMovable();
					if (dir != null) {
						target.resize(evt.getX(), evt.getY(), dir);
					} else if (movable) {
						target.moveTo(evt.getX()-offset.x, evt.getY()-offset.y);
					}

					if (target instanceof MouseMovementListener) {
						MouseMotionEvent mbEvt = new MouseMotionEvent((int)evt.getX(),(int)evt.getY(),(int)evt.getDeltaX(),(int)evt.getDeltaY(),0,0);
						((MouseMovementListener)target).onMouseMove(mbEvt);
					}
					if (target instanceof TouchListener) {
						((TouchListener)target).onTouchMove(evt);
					}
				}
			}
		}
	}
	private void androidTouchUpEvent(TouchEvent evt) {
		Element target = eventElements.get(evt.getPointerId());
		if (target != null) {
			if (target instanceof MouseButtonListener) {
				MouseButtonEvent mbEvt = new MouseButtonEvent(0, true, (int)evt.getX(), (int)evt.getY());
				((MouseButtonListener)target).onMouseLeftReleased(mbEvt);
			}
			if (target instanceof TouchListener) {
				((TouchListener)target).onTouchUp(evt);
			}
			if (!(target.getAbsoluteParent() instanceof AutoHide)) {
				handleAndroidMenuState(target);
			}
			if (target != null)
				evt.setConsumed();
			eventElements.remove(evt.getPointerId());
			contactElements.remove(evt.getPointerId());
			elementOffsets.remove(evt.getPointerId());
			eventElementResizeDirections.remove(evt.getPointerId());
		} else
			handleMenuState();
		mousePressed = false;
	}
	
	@Override
	public CollisionResult getLastCollision() { return screen.getLastCollision(); }
	
	/**
	 * Determines and returns the current mouse focus Element
	 * @param x The current mouse X coord
	 * @param y The current mouse Y coord
	 * @return Element eventElement
	 */
	private Element getEventElement(float x, float y) {
		guiRayOrigin.set(x, y, 0f);
		
		elementZOrderRay.setOrigin(guiRayOrigin);
		CollisionResults results = new CollisionResults();

		subScreenNode.collideWith(elementZOrderRay, results);

		float z = 0;
		Element testEl = null, el = null;
		for (CollisionResult result : results) {
			boolean discard = false;
			if (result.getGeometry().getParent() instanceof Element) {
				testEl = ((Element)(result.getGeometry().getParent()));
				if (testEl.getIgnoreMouse()) {
					discard = true;
				} else if (testEl.getIsClipped()) {
					if (result.getContactPoint().getX() < testEl.getClippingBounds().getX() ||
						result.getContactPoint().getX() > testEl.getClippingBounds().getZ() ||
						result.getContactPoint().getY() < testEl.getClippingBounds().getY() ||
						result.getContactPoint().getY() > testEl.getClippingBounds().getW()) {
						discard = true;
					}
				}
			}
			if (!discard) {
				if (result.getGeometry().getParent() instanceof Element) {
					el = testEl;
				}
			}
		}
		if (el != null) {
			contactElement = el;
			Element parent = null;
			if (el.getEffectParent() && mousePressed) {
				parent = el.getElementParent();
			} else if (el.getEffectAbsoluteParent() && mousePressed) {
				parent = el.getAbsoluteParent();
			}
			if (parent != null) {
				el = parent;
			}
			eventElementOffsetX = x-el.getX();
			eventElementOffsetY = y-el.getY();
			return el;
		} else {
			return null;
		}
	}
	
	private Element getContactElement(float x, float y) {
		guiRayOrigin.set(x, y, 0f);
		
		elementZOrderRay.setOrigin(guiRayOrigin);
		CollisionResults results = new CollisionResults();

		subScreenNode.collideWith(elementZOrderRay, results);

		float z = 0;
		Element testEl = null, el = null;
		for (CollisionResult result : results) {
			boolean discard = false;
			if (result.getGeometry().getParent() instanceof Element) {
				testEl = ((Element)(result.getGeometry().getParent()));
				if (testEl.getIgnoreMouse()) {
					discard = true;
				} else if (testEl.getIsClipped()) {
					if (result.getContactPoint().getX() < testEl.getClippingBounds().getX() ||
						result.getContactPoint().getX() > testEl.getClippingBounds().getZ() ||
						result.getContactPoint().getY() < testEl.getClippingBounds().getY() ||
						result.getContactPoint().getY() > testEl.getClippingBounds().getW()) {
						discard = true;
					}
				}
			}
			if (!discard) {
				if (result.getGeometry().getParent() instanceof Element) {
					el = testEl;
				}
			}
		}
		if (el != null) {
			Element parent = null;
			if (el.getEffectParent() && mousePressed) {
				parent = el.getElementParent();
			} else if (el.getEffectAbsoluteParent() && mousePressed) {
				parent = el.getAbsoluteParent();
			}
			if (parent != null)
				tempElementOffset.set(x-parent.getX(),y-parent.getY());
			else
				tempElementOffset.set(x-el.getX(),y-el.getY());
			return el;
		} else {
			return null;
		}
	}
	
	public void forceEventElement(Element element) {
		float x = element.getAbsoluteX()+1;
		float y = element.getAbsoluteY()+1;
		eventElement = getEventElement(x,y);
		if (eventElement != null) {
			if (eventElement.getEffectZOrder())
				updateZOrder(eventElement.getAbsoluteParent());
			this.setTabFocusElement(eventElement);
			if (eventElement.getIsDragDropDragElement())
				targetElement = null;
			if (eventElement.getIsResizable()) {
				float offsetX = x;
				float offsetY = y;
				Element el = eventElement;
				
				if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (eventElement.getResetKeyboardFocus())
					keyboardElement = null;
			} else if (eventElement.getIsMovable() && eventElementResizeDirection == null) {
				eventElementResizeDirection = null;
				if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (eventElement.getResetKeyboardFocus())
					keyboardElement = null;
				eventElementOriginXY.set(eventElement.getPosition());
			} else if (eventElement instanceof KeyboardListener) {
				if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (eventElement.getResetKeyboardFocus())
					keyboardElement = eventElement;
				if (keyboardElement instanceof TextField) {
					((TextField)keyboardElement).setTabFocus();
				}
				// TODO: Update target element's font shader
			} else {
				eventElementResizeDirection = null;
				if (keyboardElement != null && eventElement.getResetKeyboardFocus()) {
					if (keyboardElement instanceof TextField) ((TextField)keyboardElement).resetTabFocus();
				}
				if (eventElement.getResetKeyboardFocus())
					keyboardElement = null;
			}
		}
	}
	
	/**
	 * Determines and returns the current mouse focus Element
	 * @param x The current mouse X coord
	 * @param y The current mouse Y coord
	 * @return Element eventElement
	 */
	private Element getTargetElement(float x, float y) {
		guiRayOrigin.set(x, y, 0f);
		
		elementZOrderRay.setOrigin(guiRayOrigin);
		CollisionResults results = new CollisionResults();

		subScreenNode.collideWith(elementZOrderRay, results);

		float z = 0;
		Element testEl = null, el = null;
		for (CollisionResult result : results) {
			boolean discard = false;
			if (result.getGeometry().getParent() instanceof Element) {
				testEl = ((Element)(result.getGeometry().getParent()));
				if (testEl.getIgnoreMouse() || !testEl.getIsDragDropDropElement()) {
					discard = true;
				} else if (testEl.getIsClipped()) {
					if (result.getContactPoint().getX() < testEl.getClippingBounds().getX() ||
						result.getContactPoint().getX() > testEl.getClippingBounds().getZ() ||
						result.getContactPoint().getY() < testEl.getClippingBounds().getY() ||
						result.getContactPoint().getY() > testEl.getClippingBounds().getW()) {
						discard = true;
					}
				}
			}
		//	System.out.println(testEl.getUID() + ": " + discard + ": " + testEl.getLocalTranslation().getZ() + ": " + z + ": " + result.getContactPoint().getZ());
			if (!discard) {
				
				if (result.getContactPoint().getZ() > z) {
					z = result.getContactPoint().getZ();
					if (result.getGeometry().getParent() instanceof Element) {
						el = testEl;//((Element)(result.getGeometry().getParent()));
					}
				}
			}
		}
		if (el != null) {
			Element parent = null;
			if (el.getEffectParent() && mousePressed) {
				parent = el.getElementParent();
			} else if (el.getEffectAbsoluteParent() && mousePressed) {
				parent = el.getAbsoluteParent();
			}
			if (parent != null) {
				el = parent;
			}
			targetElementOffsetX = x-el.getX();
			targetElementOffsetY = y-el.getY();
			return el;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the current Drag enabled Element
	 * @return Element
	 */
	public Element getDragElement() {
		return this.eventElement;
	}
	
	/**
	 * Returns the current Drop enabled Element
	 * @return Element
	 */
	@Override
	public Element getDropElement() {
		return this.targetElement;
	}
	
	public void setGlobalUIScale(float widthPercent, float heightPercent) {
		for (Element el : elements.values()) {
			el.setPosition(el.getPosition().x*widthPercent, el.getPosition().y*heightPercent);
			el.setDimensions(el.getDimensions().x*widthPercent, el.getDimensions().y*heightPercent);
			el.setFontSize(el.getFontSize()*heightPercent);
			el.setGlobalUIScale(widthPercent, heightPercent);
		}
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		SubScreen subscreen = new SubScreen(this.screen, this.geom);
		subscreen.elements.putAll(this.elements);
		return subscreen;
	}
	
	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
		if (spatial != null) {
			((Node)spatial).attachChild(subScreenNode);
		}
	}
	
	@Override
	public void write(JmeExporter ex) throws IOException {  }
	
	@Override
	public void read(JmeImporter im) throws IOException {  }
	
	// Menu handling
	private void handleMenuState() {
		if (eventElement == null) {
			for (Element el : elements.values()) {
				if (el instanceof Menu) {
					el.hide();
				}
			}
		} else {
			if (!(eventElement.getAbsoluteParent() instanceof Menu) && !(eventElement.getParent() instanceof ComboBox)) {
				for (Element el : elements.values()) {
					if (el instanceof Menu) {
						el.hide();
					}
				}
			} else if (eventElement.getAbsoluteParent() instanceof Menu) {
				for (Element el : elements.values()) {
					if (el instanceof Menu && el != eventElement.getAbsoluteParent()) {
						el.hide();
					}
				}
			} else if (eventElement.getParent() instanceof ComboBox) {
				for (Element el : elements.values()) {
					if (el instanceof Menu && el != ((ComboBox)eventElement.getParent()).getMenu()) {
						el.hide();
					}
				}
			}
		}
	}
	
	@Override
	public void handleAndroidMenuState(Element target) {
		if (target == null) {
			for (Element el : elements.values()) {
				if (el instanceof Menu) {
					el.hide();
				}
			}
		} else {
			if (!(target.getAbsoluteParent() instanceof Menu) && !(target.getParent() instanceof ComboBox)) {
				for (Element el : elements.values()) {
					if (el instanceof Menu) {
						el.hide();
					}
				}
			} else if (target.getAbsoluteParent() instanceof Menu) {
				for (Element el : elements.values()) {
					if (el instanceof Menu && el != target.getAbsoluteParent()) {
						el.hide();
					}
				}
			} else if (target.getParent() instanceof ComboBox) {
				for (Element el : elements.values()) {
					if (el instanceof Menu && el != ((ComboBox)target.getParent()).getMenu()) {
						el.hide();
					}
				}
			}
		}
	}
	
	// Forms and tab focus
	/**
	 * Method for setting the tab focus element
	 * @param element The Element to set tab focus to
	 */
	@Override
	public void setTabFocusElement(Element element) {
		resetFocusElement();
		focusForm = element.getForm();
		if (element.getResetKeyboardFocus()) {
			if (focusForm != null) {
				tabFocusElement = element;
				focusForm.setSelectedTabIndex(element);
				if (tabFocusElement instanceof TabFocusListener) {
					((TabFocusListener)element).setTabFocus();
				}
			}
		}
	}
	
	/**
	 * Resets the tab focus element to null after calling the TabFocusListener's
	 * resetTabFocus method.
	 */
	@Override
	public void resetTabFocusElement() {
		resetFocusElement();
		this.tabFocusElement = null;
		this.focusForm = null;
	}
	
	/**
	 * Send reset to the current Tab Focus Element
	 */
	private void resetFocusElement() {
		if (tabFocusElement != null) {
			if (tabFocusElement.getResetKeyboardFocus()) {
				if (tabFocusElement instanceof TabFocusListener) {
					((TabFocusListener)tabFocusElement).resetTabFocus();
				}
			}
		}
	}
	
	/**
	 * Sets the current Keyboard focus Element
	 * @param element The Element to set keyboard focus to
	 */
	@Override
	public void setKeyboardElement(Element element) {
		if (element != null) {
			if (element.getResetKeyboardFocus())
				keyboardElement = element;
		} else
			keyboardElement = null;
	}
	
	@Override
	public Style getStyle(String key) {
		return screen.getStyle(key);
	}

	@Override
	public void setClipboardText(String text) {
		screen.setClipboardText(text);
	}

	@Override
	public String getClipboardText() {
		return screen.getClipboardText();
	}

	@Override
	public boolean getUseTextureAtlas() {
		return screen.getUseTextureAtlas();
	}

	@Override
	public float getGlobalAlpha() {
		return screen.getGlobalAlpha();
	}

	@Override
	public EffectManager getEffectManager() {
		return screen.getEffectManager();
	}

	@Override
	public AnimManager getAnimManager() {
		return screen.getAnimManager();
	}

	@Override
	public boolean getUseUIAudio() {
		return screen.getUseUIAudio();
	}

	@Override
	public boolean getUseToolTips() {
		return screen.getUseToolTips();
	}

	@Override
	public void updateToolTipLocation() {
		screen.updateToolTipLocation();
	}
	
	@Override
	public Element getToolTipFocus() {
		return screen.getToolTipFocus();
	}
	
	@Override
	public void hideToolTip() {
		screen.hideToolTip();
	}

	@Override
	public void setCursor(CursorType cursorType) {
		screen.setCursor(cursorType);
	}

	@Override
	public void showVirtualKeyboard() {
		screen.showVirtualKeyboard();
	}

	@Override
	public void hideVirtualKeyboard() {
		screen.hideVirtualKeyboard();
	}

	@Override
	public void setGlobalAlpha(float alpha) {
		screen.setGlobalAlpha(alpha);
	}
	
	@Override
	public BitmapFont getDefaultGUIFont() {
		return screen.getDefaultGUIFont();
	}
	
	@Override
	public ScaleUtil getScaleManager() {
		return screen.getScaleManager();
	}
	
	@Override
	public float scaleFloat(float in) {
		return screen.scaleFloat(in);
	};
	
	@Override
	public Vector2f scaleVector2f(Vector2f in) {
		return screen.scaleVector2f(in);
	};
	
	@Override
	public Vector3f scaleVector3f(Vector3f in) {
		return screen.scaleVector3f(in);
	};
	
	@Override
	public Vector4f scaleVector4f(Vector4f in) {
		return screen.scaleVector4f(in);
	};
	
	@Override
	public float scaleFontSize(float in) {
		return screen.scaleFontSize(in);
	};

	@Override
	public void setUseUIAudio(boolean use) {
		screen.setUseUIAudio(use);
	}

	@Override
	public void setUIAudioVolume(float volume) {
		screen.setUIAudioVolume(volume);
	}

	@Override
	public void setUseToolTips(boolean use) {
		screen.setUseToolTips(use);
	}

	@Override
	public void setUseCustomCursors(boolean use) {
		screen.setUseCustomCursors(use);
	}

	@Override
	public boolean getUseCustomCursors() {
		return screen.getUseCustomCursors();
	}

	@Override
	public void setUseCursorEffects(boolean use) {
		screen.setUseCursorEffects(use);
	}
	
	@Override
	public CursorEffects getCursorEffects() {
		return screen.getCursorEffects();
	}
	
	@Override
	public ModalBackground getModalBackground() {
		return screen.getModalBackground();
	}
	
	@Override
	public void showAsModal(Element el, boolean showWithEffect) {
		screen.showAsModal(el, showWithEffect);
	}
	
	@Override
	public void hideModalBackground() {
		screen.hideModalBackground();
	}
	
	//<editor-fold dewsc="2D Framework">
	public AnimLayer addAnimLayer() {
		return addAnimLayer(UIDUtil.getUID());
	}
	
	@Override
	public AnimLayer addAnimLayer(String UID) {
		if (getAnimLayerById(UID) != null) {
			try {
				throw new ConflictingIDException();
			} catch (ConflictingIDException ex) {
				Logger.getLogger(Element.class.getName()).log(Level.SEVERE, "The child layer '" + UID + "' (Element) conflicts with a previously added child layer in parent Screen.", ex);
				System.exit(0);
			}
			return null;
		} else {
			AnimLayer layer = new AnimLayer(
				this,
				UID
			);
			
			layer.initZOrder(layerZOrderCurrent);
			layerZOrderCurrent += this.getZOrderStepMajor();
			
			layers.put(UID, layer);
			if (!layer.getInitialized()) {
				layer.orgPosition = layer.getPosition().clone();
				layer.setInitialized();
			}
			subScreenNode.attachChild(layer);
			subScreenNode.addControl(layer);
			
			return layer;
		}
	}
	
	@Override
	public void addAnimLayer(String UID, AnimLayer layer) {
		if (getAnimLayerById(UID) != null) {
			try {
				throw new ConflictingIDException();
			} catch (ConflictingIDException ex) {
				Logger.getLogger(Element.class.getName()).log(Level.SEVERE, "The child layer '" + UID + "' (Element) conflicts with a previously added child layer in parent Screen.", ex);
				System.exit(0);
			}
		} else {
			layer.initZOrder(layerZOrderCurrent);
			layerZOrderCurrent += this.getZOrderStepMajor();
			
			layers.put(UID, layer);
			if (!layer.getInitialized()) {
				layer.orgPosition = layer.getPosition().clone();
				layer.setInitialized();
			}
			subScreenNode.attachChild(layer);
			subScreenNode.addControl(layer);
		}
	}
	@Override
	public AnimLayer removeAnimLayer(String UID) {
		AnimLayer animLayer = layers.get(UID);
		if (animLayer != null) {
			removeAnimLayer(animLayer);
			return animLayer;
		} else
			return null;
	}
	
	@Override
	public void removeAnimLayer(AnimLayer animLayer) {
		if (layers.containsValue(animLayer)) {
			subScreenNode.removeControl(animLayer);
			layers.remove(animLayer.getUID());
			float shiftZ = animLayer.getLocalTranslation().getZ();
			for (AnimLayer el : layers.values()) {
				if (el.getLocalTranslation().getZ() > shiftZ) {
					el.move(0,0,-zOrderStepMajor);
				}
			}
			layerZOrderCurrent -= zOrderStepMajor;
			animLayer.removeFromParent();
			animLayer.cleanup();
		}
	}
	
	public AnimLayer getAnimLayerById(String UID) {
		AnimLayer ret = null;
		if (layers.containsKey(UID)) {
			ret = layers.get(UID);
		} else {
			for (AnimLayer el : layers.values()) {
				ret = (AnimLayer)el.getChildElementById(UID);
				if (ret != null) {
					break;
				}
			}
		}
		return ret;
	}
	
	private void setAnimElementZOrder() {
		if (eventAnimElement != null) {
			if (eventAnimElement.getZOrderEffect() == AnimElement.ZOrderEffect.Self ||
				eventAnimElement.getZOrderEffect() == AnimElement.ZOrderEffect.Both)
				if (eventAnimElement.getParentLayer() != null)
					eventAnimElement.getParentLayer().bringAnimElementToFront(eventAnimElement);
			if (eventAnimElement.getZOrderEffect() == AnimElement.ZOrderEffect.Child ||
				eventAnimElement.getZOrderEffect() == AnimElement.ZOrderEffect.Both)
				eventAnimElement.bringQuadToFront(eventQuad);
		}
	}
	//</editor-fold>
}
