package tonegod.gui.style;

import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.texture.Texture;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.buttons.RadioButton;
import tonegod.gui.controls.extras.ChatBox;
import tonegod.gui.controls.extras.ChatBoxExt;
import tonegod.gui.controls.extras.ColorWheel;
import tonegod.gui.controls.extras.Indicator;
import tonegod.gui.controls.extras.OSRViewPort;
import tonegod.gui.controls.lists.ComboBox;
import tonegod.gui.controls.lists.Dial;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.lists.SelectList;
import tonegod.gui.controls.lists.SlideTray;
import tonegod.gui.controls.lists.Slider;
import tonegod.gui.controls.lists.Spinner;
import tonegod.gui.controls.menuing.Menu;
import tonegod.gui.controls.scrolling.ScrollArea;
import tonegod.gui.controls.scrolling.ScrollAreaAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.Password;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.AlertBox;
import tonegod.gui.controls.windows.DialogBox;
import tonegod.gui.controls.windows.LoginBox;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.TabControl;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Element.Orientation;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.core.utils.XMLHelper;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public class LayoutParser {
        Screen screen;
        AbstractAppState state;
        
        List<Class> controls = new ArrayList<>();
        
        public LayoutParser(Screen screen) {
                this.screen = screen;
                
                
                controls.add(OSRViewPort.class);
                controls.add(Indicator.class);
                controls.add(Slider.class);
                controls.add(Dial.class);
                controls.add(CheckBox.class);
                controls.add(RadioButton.class);
                controls.add(Button.class);
                controls.add(ChatBox.class);
                controls.add(ChatBoxExt.class);
                controls.add(Panel.class);
                controls.add(ColorWheel.class);
                controls.add(LoginBox.class);
                controls.add(AlertBox.class);
                controls.add(DialogBox.class);
                controls.add(Window.class);
                controls.add(TabControl.class);
                controls.add(SelectBox.class);
                controls.add(ComboBox.class);
                controls.add(Menu.class);
                controls.add(SelectList.class);
                controls.add(ScrollArea.class);
                controls.add(SlideTray.class);
                controls.add(Spinner.class);
                controls.add(Label.class);
                controls.add(Password.class);
                controls.add(TextField.class);
        }
        
        public void parseLayout(String filePath, AbstractAppState state) {
                this.state = state;
                try {
                        // Get Cursors
						/*
                        InputStream file = Screen.class.getClassLoader().getResourceAsStream(
                                filePath
                        );
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(file);
                        doc.getDocumentElement().normalize();
						*/
						Document doc = screen.getApplication().getAssetManager().loadAsset(new AssetKey<Document>(filePath));
						if (doc == null) {
							throw new AssetNotFoundException(String.format("Could not find style %s", filePath));
						}
                        NodeList screenNodeLst = doc.getElementsByTagName("screen");
                        org.w3c.dom.Node screenNode = screenNodeLst.item(0);
                        NodeList nodeLst = screenNode.getChildNodes();
                        
                        parseNodeList(nodeLst, null);
                        
                        doc = null;
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        
        private void parseNodeList(NodeList nodeLst, Element el) {
                for (int s = 0; s < nodeLst.getLength(); s++) {
                        org.w3c.dom.Node fstNode = nodeLst.item(s);
                        if (fstNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                if (fstNode.getNodeName().equals("component")) {
                                        String type, id, pos, dim, rsb, img, ori, flag;
                                        type = XMLHelper.getNodeAttributeValue(fstNode, "type");
                                        try { id = XMLHelper.getNodeAttributeValue(fstNode, "id"); } catch (NoSuchElementException ex) { id = UIDUtil.getUID(); }
                                        try { pos = XMLHelper.getNodeAttributeValue(fstNode, "position"); } catch (NoSuchElementException ex) { pos = null; }
                                        try { dim = XMLHelper.getNodeAttributeValue(fstNode, "dimensions"); } catch (NoSuchElementException ex) { dim = null; }
                                        try { rsb = XMLHelper.getNodeAttributeValue(fstNode, "resizeBorders"); } catch (NoSuchElementException ex) {  rsb = null; }
                                        try { img = XMLHelper.getNodeAttributeValue(fstNode, "defaultImg"); } catch (NoSuchElementException ex) { img = null; }
                                        try { ori = XMLHelper.getNodeAttributeValue(fstNode, "orientation"); } catch (NoSuchElementException ex) { ori = null; }
                                        try { flag = XMLHelper.getNodeAttributeValue(fstNode, "tracSurroundsThumb"); } catch (NoSuchElementException ex) { flag = null; }
                                        if (flag == null) {
                                                try { flag = XMLHelper.getNodeAttributeValue(fstNode, "isScrollable"); } catch (NoSuchElementException ex) { flag = null; }
                                        }
                                        if (flag == null) {
                                                try { flag = XMLHelper.getNodeAttributeValue(fstNode, "cycle"); } catch (NoSuchElementException ex) { flag = null; }
                                        }
                                        
                                        NodeList childNodes = fstNode.getChildNodes();
                                        Element element = null;
                                        Class cl = null;
                                        
                                        for (Class c : controls) {
                                                if (c.getName().substring(c.getName().lastIndexOf(".")+1).equals(type)) {
                                                        element = createElement(el, c, id, pos, dim, rsb, img, ori, flag, childNodes);
                                                        cl = c;
                                                        break;
                                                }
                                        }
                                        
                                        parseMethods(childNodes, element, cl);
                                        parseNodeList(childNodes, element);
                                        parseEffects(childNodes, element);
                                        
                                        if (el == null) screen.addElement(element);
                                        else                    el.addChild(element);
                                }
                        }
                }
        }
        
        private void parseMethods(NodeList childNodes, Element el, Class c) {
                for (int n = 0; n < childNodes.getLength(); n++) {
                        org.w3c.dom.Node childNode = childNodes.item(n);
                        if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                if (childNode.getNodeName().equals("method")) {
                                        String name = XMLHelper.getNodeAttributeValue(childNode, "name");
                                        parseParamters(el,c,name,childNode);
                                }
                        }
                }
        }
        
        private void parseParamters(Element el, Class c, String name, org.w3c.dom.Node childNode) {
                try {
                        Method method = null;
                        Class<?> clazz = c;
                        while (method == null) {
                                Method[] methods = clazz.getDeclaredMethods();
                                for (Method m : methods) {
                                        if (m.getName().equals(name)) {
                                                method = m;
                                                break;
                                        }
                                }
                                clazz = clazz.getSuperclass();
                                if (clazz == Object.class)
                                        break;
                        }
                        if (method != null) {
                                method.setAccessible(true);
                                Class<?>[] params = method.getParameterTypes();
                                Object[] finalParams = new Object[params.length];
                                int index = 0;
                                for (Class paramClass : params) {
                                        if (paramClass == Integer.class || paramClass == Integer.TYPE) {
                                                finalParams[index] = Integer.parseInt(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == Float.class || paramClass == Float.TYPE) {
                                                finalParams[index] = Float.parseFloat(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == String.class) {
                                                finalParams[index] = XMLHelper.getNodeAttributeValue(childNode, "param" + index);
                                        } else if (paramClass == Object.class) {
                                                finalParams[index] = XMLHelper.getNodeAttributeValue(childNode, "param" + index);
                                        } else if (paramClass == Boolean.class || paramClass == Boolean.TYPE) {
                                                finalParams[index] = Boolean.parseBoolean(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == Menu.class) {
                                                finalParams[index] = screen.getElementById(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == BitmapFont.Align.class) {
                                                finalParams[index] = BitmapFont.Align.valueOf(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == BitmapFont.VAlign.class) {
                                                finalParams[index] = BitmapFont.VAlign.valueOf(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == LineWrapMode.class) {
                                                finalParams[index] = LineWrapMode.valueOf(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == TextField.Type.class) {
                                                finalParams[index] = TextField.Type.valueOf(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == ColorRGBA.class) {
                                                finalParams[index] = parseColorRGBA(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == Vector2f.class) {
                                                finalParams[index] = parseVector2f(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == Vector3f.class) {
                                                finalParams[index] = parseVector3f(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        } else if (paramClass == Vector4f.class) {
                                                finalParams[index] = parseVector4f(XMLHelper.getNodeAttributeValue(childNode, "param" + index));
                                        }
                                        index++;
                                }
                                method.invoke(c.cast(el), finalParams);
                        }
                } catch (Exception ex) { ex.printStackTrace(); }
        }
        
        public Method getMethod(Class<?> clazz, String name) {  
                System.out.println(name + " : " + clazz.getName());
                Method m = null;
                try {
                        m = clazz.getDeclaredMethod(name);
                        System.out.println(m);
                //      System.out.println("Hello:");
                //      if (m == null)
                //              m = getMethod(clazz.getSuperclass(), name);
                //      else {
                //              if (clazz == Object.class) {  
                //                      return null;
                //              } else {
                //                      return getMethod(clazz.getSuperclass(), name);  
                //              }
                //      }
                } catch (Exception e) { 
                        try {
                                m = getMethod(clazz.getSuperclass(), name);
                        } catch (Exception ex) {  }
                //      if (clazz != Object.class)
                //              return getMethod(clazz.getSuperclass(), name);
                }
                return m;
        //      Class<?> c = clazz.getSuperclass();
        //      System.out.println(c);
        //      return m;  
    }
        
        private Element createElement(Element el, Class c, String id, String pos, String dim, String rsb, String img, String ori, String flag, final NodeList childNodes) {
                Vector2f position, dimensions;
                Vector4f resizeBorders;
                String styleName = "";
                String className = c.getName().substring(c.getName().lastIndexOf(".")+1);
                styleName = className;
                if (className.equals("ComboBox")) styleName = "TextField";
                if (className.equals("TabControl") ||
                        className.equals("LoginBox") ||
                        className.equals("DialogBox") ||
                        className.equals("AlertBox") ||
                        className.equals("ChatBox") ||
                        className.equals("ChatBoxExt") ||
                        className.equals("OSRViewPort")) styleName = "Window";
                position = parsePositionalVector2f(pos, el);
                if (dim == null || dim.equals(""))      dimensions = screen.getStyle(styleName).getVector2f("defaultSize");
                else                                                            dimensions = parsePositionalVector2f(dim, el);
                if (className.equals("ColorWheel")) styleName = "Window";
                if (rsb == null || rsb.equals(""))      try { resizeBorders = screen.getStyle(styleName).getVector4f("resizeBorders"); } catch (Exception e) { resizeBorders = Vector4f.ZERO; }
                else                                                            resizeBorders = parseVector4f(rsb);
                if (img == null || img.equals(""))      try { img = screen.getStyle(styleName).getString("defaultImg"); } catch (Exception e) { img = null; }
                
                if (className.equals("Label")) {
                        return new Label(screen, id, position, dimensions, resizeBorders, img);
                } else if (className.equals("OSRViewPort")) {
                        return new OSRViewPort(screen, id, position, dimensions, resizeBorders, img);
                } else if (className.equals("Spinner")) {
                        Spinner.Orientation orientation = Spinner.Orientation.valueOf(ori);
                        Boolean bool = Boolean.parseBoolean(flag);
                        return new Spinner(screen, id, position, dimensions, resizeBorders, img, orientation, bool) {
                                @Override
                                public void onChange(int selectedIndex, String value, ChangeType type) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), selectedIndex, value);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("Slider")) {
                        Slider.Orientation orientation = Slider.Orientation.valueOf(ori);
                        Boolean bool = Boolean.parseBoolean(flag);
                        return new Slider(screen, id, position, dimensions, resizeBorders, img, orientation, bool) {
                                @Override
                                public void onChange(int selectedIndex, Object value) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), selectedIndex, value);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("Dial")) {
                        return new Dial(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onChange(int selectedIndex, Object value) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), selectedIndex, value);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("TextField")) {
                        return new TextField(screen, id, position, dimensions, resizeBorders, img);
                } else if (className.equals("Password")) {
                        return new Password(screen, id, position, dimensions, resizeBorders, img);
                } else if (className.equals("ColorWheel")) {
                        return new ColorWheel(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onChange(ColorRGBA color) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), color);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onComplete(ColorRGBA color) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onComplete")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), color);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("Indicator")) {
                        Indicator.Orientation orientation = Indicator.Orientation.valueOf(ori);
                        return new Indicator(screen, id, position, dimensions, resizeBorders, img, orientation, true) {
                                @Override
                                public void onChange(float currentValue, float currentPercentage) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), currentValue, currentPercentage);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("Window")) {
                        return new Window(screen, id, position, dimensions, resizeBorders, img);
                } else if (className.equals("SlideTray")) {
                        SlideTray.Orientation orientation = SlideTray.Orientation.valueOf(ori);
                        return new SlideTray(screen, id, position, dimensions, resizeBorders, img, orientation);
                } else if (className.equals("Panel")) {
                        return new Panel(screen, id, position, dimensions, resizeBorders, img);
                } else if (className.equals("TabControl")) {
                        return new TabControl(screen, id, position, dimensions, resizeBorders, img, Orientation.HORIZONTAL) {
						@Override
						public void onTabSelect(int index) {
							for (int n = 0; n < childNodes.getLength(); n++) {
								org.w3c.dom.Node childNode = childNodes.item(n);
								if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
									if (childNode.getNodeName().equals("eventMethod")) {
										if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onTabSelect")) {
											try {
												for (Method method : state.getClass().getDeclaredMethods()) {
													if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
														method.invoke(state.getClass().cast(state), index);
												}
											} catch (Exception ex) { ex.printStackTrace(); }
										}
									}
								}
							}
						}
					};
                } else if (className.equals("ChatBox")) {
					return new ChatBox(screen, id, position, dimensions, resizeBorders, img) {
						@Override
						public void onSendMsg(String msg) {
							for (int n = 0; n < childNodes.getLength(); n++) {
								org.w3c.dom.Node childNode = childNodes.item(n);
								if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
									if (childNode.getNodeName().equals("eventMethod")) {
										if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onSendMsg")) {
											try {
												for (Method method : state.getClass().getDeclaredMethods()) {
													if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
														method.invoke(state.getClass().cast(state), msg);
												}
											} catch (Exception ex) { ex.printStackTrace(); }
										}
									}
								}
							}
						}
					};
                } else if (className.equals("ChatBoxExt")) {
                        return new ChatBoxExt(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onSendMsg(Object command, String msg) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onSendMsg")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), command, msg);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("LoginBox")) {
                        return new LoginBox(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onButtonCancelPressed(MouseButtonEvent evt, boolean toggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonCancelPressed")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, toggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonLoginPressed(MouseButtonEvent evt, boolean toggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonLoginPressed")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, toggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("AlertBox")) {
                        return new AlertBox(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonOkPressed")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, toggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("DialogBox")) {
                        return new DialogBox(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onButtonCancelPressed(MouseButtonEvent evt, boolean toggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonCancelPressed")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, toggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonOkPressed(MouseButtonEvent evt, boolean toggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonOkPressed")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, toggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("SelectBox")) {
                        return new SelectBox(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onChange(int selectedIndex, Object value) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), selectedIndex, value);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("ComboBox")) {
                        return new ComboBox(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onChange(int selectedIndex, Object value) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), selectedIndex, value);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("SelectList")) {
                        return new SelectList(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onChange() {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onChange")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state));
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("Menu")) {
                        Boolean bool = Boolean.parseBoolean(flag);
                        return new Menu(screen, id, position, dimensions, resizeBorders, img, bool) {
                                @Override
                                public void onMenuItemClicked(int index, Object value, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onMenuItemClicked")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), index, value, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("Button")) {
                        return new ButtonAdapter(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseLeftDown")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseLeftUp")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseRightDown(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseRightDown")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseRightUp(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseRightUp")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonFocus(MouseMotionEvent evt) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonFocus")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonLostFocus(MouseMotionEvent evt) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonLostFocus")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("CheckBox")) {
                        return new CheckBox(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseLeftDown")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseLeftUp")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseRightDown(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseRightDown")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseRightUp(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseRightUp")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonFocus(MouseMotionEvent evt) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonFocus")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonLostFocus(MouseMotionEvent evt) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonLostFocus")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("RadioButton")) {
                        return new RadioButton(screen, id, position, dimensions, resizeBorders, img) {
                                @Override
                                public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseLeftDown")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseLeftUp")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseRightDown(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseRightDown")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonMouseRightUp(MouseButtonEvent evt, boolean isToggled) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonMouseRightUp")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt, isToggled);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonFocus(MouseMotionEvent evt) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonFocus")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                                @Override
                                public void onButtonLostFocus(MouseMotionEvent evt) {
                                        for (int n = 0; n < childNodes.getLength(); n++) {
                                                org.w3c.dom.Node childNode = childNodes.item(n);
                                                if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                                        if (childNode.getNodeName().equals("eventMethod")) {
                                                                if (XMLHelper.getNodeAttributeValue(childNode, "name").equals("onButtonLostFocus")) {
                                                                        try {
                                                                                for (Method method : state.getClass().getDeclaredMethods()) {
                                                                                        if (method.getName().equals(XMLHelper.getNodeAttributeValue(childNode, "stateMethodName")))
                                                                                                method.invoke(state.getClass().cast(state), evt);
                                                                                }
                                                                        } catch (Exception ex) { ex.printStackTrace(); }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        };
                } else if (className.equals("ScrollArea")) {
                        return new ScrollAreaAdapter(screen, id, position, dimensions, resizeBorders, img);
                } else
                        return new Element(screen, id, position, dimensions, resizeBorders, img);
        }
        
        private void parseEffects(NodeList childNodes, Element el) {
                for (int n = 0; n < childNodes.getLength(); n++) {
                        org.w3c.dom.Node childNode = childNodes.item(n);
                        if (childNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                if (childNode.getNodeName().equals("effect")) {
                                        Effect.EffectType type = Effect.EffectType.valueOf(XMLHelper.getNodeAttributeValue(childNode, "type"));
                                        Effect.EffectEvent event = Effect.EffectEvent.valueOf(XMLHelper.getNodeAttributeValue(childNode, "event"));
                                        float duration = Float.parseFloat(XMLHelper.getNodeAttributeValue(childNode, "duration"));
                                        Effect effect = new Effect(type, event, duration);
                                        switch(type) {
                                                case ImageSwap:
                                                case Pulse:
                                                        String imagePath = XMLHelper.getNodeAttributeValue(childNode, "blendImage");
                                                        Texture tex = screen.getApplication().getAssetManager().loadTexture(imagePath);
                                                        tex.setMagFilter(Texture.MagFilter.Bilinear);
                                                        tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
                                                        tex.setWrap(Texture.WrapMode.Repeat);
                                                        effect.setBlendImage(tex);
                                                        break;
                                                case ColorSwap:
                                                case PulseColor:
                                                        effect.setColor(parseColorRGBA(XMLHelper.getNodeAttributeValue(childNode, "color")));
                                                        break;
                                                case SlideIn:
                                                case SlideOut:
                                                        effect.setEffectDirection(Effect.EffectDirection.valueOf(XMLHelper.getNodeAttributeValue(childNode, "direction")));
                                                        break;
                                                case SlideTo:
                                                        effect.setEffectDestination(parseVector2f(XMLHelper.getNodeAttributeValue(childNode, "destination")));
                                                        break;
                                        }
                                        String audioFile = XMLHelper.getNodeAttributeValue(childNode, "audioFile");
                                        if (!audioFile.equals("")) effect.setAudioFile(audioFile);
                                        float audioVolume = 1;
                                        try { audioVolume = Float.parseFloat(XMLHelper.getNodeAttributeValue(childNode, "volume")); } catch (Exception exa) { audioVolume = 1; }
                                        effect.setAudioVolume(audioVolume);
                                        
                                        el.addEffect(effect);
                                }
                        }
                }
        }
        
        private Vector2f parsePositionalVector2f(String str, Element el) {
                StringTokenizer st;
                float x, y, z, w;

                st = new StringTokenizer(str,",");
                String xComp = st.nextToken();
                String yComp = st.nextToken();
                if (el == null) {
                        if (xComp.indexOf("%") != -1)   x = screen.getWidth()*(Float.parseFloat(xComp.substring(0,xComp.indexOf("%")))*0.01f);
                        else                                                    x = Float.parseFloat(xComp);
                        if (yComp.indexOf("%") != -1)   y = screen.getHeight()*(Float.parseFloat(yComp.substring(0,yComp.indexOf("%")))*0.01f);
                        else                                                    y = Float.parseFloat(yComp);
                } else {
                        if (xComp.indexOf("%") != -1)   x = el.getWidth()*(Float.parseFloat(xComp.substring(0,xComp.indexOf("%")))*0.01f);
                        else                                                    x = Float.parseFloat(xComp);
                        if (yComp.indexOf("%") != -1)   y = el.getHeight()*(Float.parseFloat(yComp.substring(0,yComp.indexOf("%")))*0.01f);
                        else                                                    y = Float.parseFloat(yComp);
                }
                return new Vector2f(x,y);
        }
        
        private Vector2f parseVector2f(String str) {
                StringTokenizer st;
                float x, y, z, w;

                st = new StringTokenizer(str,",");
                String xComp = st.nextToken();
                String yComp = st.nextToken();
                x = Float.parseFloat(xComp);
                y = Float.parseFloat(yComp);
                return new Vector2f(x,y);
        }
        
        private Vector3f parseVector3f(String str) {
                StringTokenizer st;
                float x, y, z;

                st = new StringTokenizer(str,",");
                String xComp = st.nextToken();
                String yComp = st.nextToken();
                String zComp = st.nextToken();
                x = Float.parseFloat(xComp);
                y = Float.parseFloat(yComp);
                z = Float.parseFloat(zComp);
                return new Vector3f(x,y,z);
        }
        
        private Vector4f parseVector4f(String str) {
                StringTokenizer st;
                float x, y, z, w;

                st = new StringTokenizer(str,",");
                String xComp = st.nextToken();
                String yComp = st.nextToken();
                String zComp = st.nextToken();
                String wComp = st.nextToken();
                x = Float.parseFloat(xComp);
                y = Float.parseFloat(yComp);
                z = Float.parseFloat(zComp);
                w = Float.parseFloat(wComp);
                return new Vector4f(x,y,z,w);
        }
        
        private ColorRGBA parseColorRGBA(String str) {
                StringTokenizer st;
                float x, y, z, w;

                st = new StringTokenizer(str,",");
                String xComp = st.nextToken();
                String yComp = st.nextToken();
                String zComp = st.nextToken();
                String wComp = st.nextToken();
                x = Float.parseFloat(xComp);
                y = Float.parseFloat(yComp);
                z = Float.parseFloat(zComp);
                w = Float.parseFloat(wComp);
                return new ColorRGBA(x,y,z,w);
        }
}