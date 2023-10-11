/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.style;

import com.jme3.app.Application;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tonegod.gui.core.Screen;
import tonegod.gui.core.utils.XMLHelper;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public class StyleManager {
	public static enum CursorType {
		POINTER,
		HAND,
		MOVE,
		TEXT,
		WAIT,
		RESIZE_CNW,
		RESIZE_CNE,
		RESIZE_NS,
		RESIZE_EW,
		CUSTOM_0,
		CUSTOM_1,
		CUSTOM_2,
		CUSTOM_3,
		CUSTOM_4,
		CUSTOM_5,
		CUSTOM_6,
		CUSTOM_7,
		CUSTOM_8,
		CUSTOM_9
	}
	Screen screen;
	Application app;
	private Map<String, Style> styles = new HashMap<>();
	private Map<CursorType, JmeCursor> cursors = new HashMap<>();
	private Map<String, AudioNode> audioNodes = new HashMap<>();
	private String styleMap;
	
	public StyleManager(Screen screen, String styleMap) {
		this.screen = screen;
		this.app = screen.getApplication();
		this.styleMap = styleMap;
	}
	
	public String getStyleMap() {
		return this.styleMap;
	}
	
	public void parseStyles(String path) {
		List<String> docPaths = new ArrayList<>();
		try {
			// Cursors
			Document doc = app.getAssetManager().loadAsset(new AssetKey<Document>(path));
			if (doc == null) {
				throw new AssetNotFoundException(String.format("Could not find style %s", path));
			}
			NodeList nodeLst = doc.getElementsByTagName("cursors");
			
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(0);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					String cursorDocPath = XMLHelper.getNodeAttributeValue(fstNode, "path");
					docPaths.add(cursorDocPath);
				}
			}
			
			parseCursors(docPaths);
			docPaths.clear();
			
			// Audio nodes
			doc = app.getAssetManager().loadAsset(new AssetKey<Document>(path));
			if (doc == null) {
				throw new AssetNotFoundException(String.format("Could not find style %s", path));
			}
			nodeLst = doc.getElementsByTagName("audio");
			
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(0);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					String audioDocPath = XMLHelper.getNodeAttributeValue(fstNode, "path");
					docPaths.add(audioDocPath);
				}
			}
			
			parseAudios(docPaths);
			docPaths.clear();
			
			// Control style definitions
			doc = app.getAssetManager().loadAsset(new AssetKey<Document>(path));
			if (doc == null) {
				throw new AssetNotFoundException(String.format("Could not find style %s", path));
			}
			nodeLst = doc.getElementsByTagName("style");
			
			for (int s = 0; s < nodeLst.getLength(); s++) {
				Node fstNode = nodeLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
					String styleDocPath = XMLHelper.getNodeAttributeValue(fstNode, "path");
					docPaths.add(styleDocPath);
				}
			}
			
			parseStyleDefs(docPaths);
			docPaths.clear();
		} catch (Exception e) {
			System.err.println("Problem loading style map: " + e);
		}
	}
	
	private void parseCursors(List<String> docPaths) {
		for (String docPath : docPaths) {
			try {
				Document doc = app.getAssetManager().loadAsset(new AssetKey<Document>(docPath));
				if (doc == null) {
					throw new AssetNotFoundException(String.format("Could not find style %s", docPath));
				}
				NodeList nLst = doc.getElementsByTagName("cursor");

				for (int s = 0; s < nLst.getLength(); s++) {
					Node fstNode = nLst.item(s);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						String key = XMLHelper.getNodeAttributeValue(fstNode, "type");
						String curPath = XMLHelper.getNodeAttributeValue(fstNode, "path");
						String imgType = XMLHelper.getNodeAttributeValue(fstNode, "imgType");
						
						JmeCursor jmeCursor = null;
						if (!imgType.equals("cur") && imgType.length() > 0)
						{
							Texture cursorTexture = app.getAssetManager().loadTexture(curPath);		
						    Image image = cursorTexture.getImage();
						    imgByteBuff = (ByteBuffer) image.getData(0).rewind();
						    IntBuffer curIntBuff = BufferUtils.createIntBuffer(image.getHeight() * image.getWidth());
						    while (imgByteBuff.hasRemaining()) {
						        int rgba = imgByteBuff.getInt();
						        int argb = ((rgba & 255) << 24) | (rgba >> 8);
						        curIntBuff.put(argb);
						    }	

						    jmeCursor = new JmeCursor();
						    jmeCursor.setHeight(image.getHeight());
						    jmeCursor.setWidth(image.getWidth());
						    jmeCursor.setNumImages(1);
						    jmeCursor.setImagesData((IntBuffer) curIntBuff.rewind());
						} else {
							jmeCursor = (JmeCursor)app.getAssetManager().loadAsset(curPath);//new JmeCursor();
							
						}						

						try {
							int hsX = Integer.valueOf(XMLHelper.getNodeAttributeValue(fstNode, "x"));
							int hsY = Integer.valueOf(XMLHelper.getNodeAttributeValue(fstNode, "y"));
							jmeCursor.setxHotSpot(hsX);
							jmeCursor.setyHotSpot(hsY);
						} catch (Exception npe) {  }
						
						cursors.put(
							CursorType.valueOf(key), 
							(JmeCursor)app.getAssetManager().loadAsset(curPath)
						);
					}
				}
			} catch (Exception ex) {
				System.err.println("Problem loading cursor definition: " + ex);
			}
		}
	}
	
	private void parseAudios(List<String> docPaths) {
		for (String docPath : docPaths) {
			try {
				Document doc = app.getAssetManager().loadAsset(new AssetKey<Document>(docPath));
				if (doc == null) {
					throw new AssetNotFoundException(String.format("Could not find style %s", docPath));
				}
				NodeList nLst = doc.getElementsByTagName("audiofile");

				for (int s = 0; s < nLst.getLength(); s++) {
					Node fstNode = nLst.item(s);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						String key = XMLHelper.getNodeAttributeValue(fstNode, "key");
						String audioPath = XMLHelper.getNodeAttributeValue(fstNode, "path");

						AudioNode audioNode = new AudioNode(app.getAssetManager(),
                                                        audioPath, AudioData.DataType.Buffer);
						audioNode.setPositional(false);
						audioNode.setReverbEnabled(false);
						audioNodes.put(key, audioNode);
						screen.getGUINode().attachChild(audioNode);
					}
				}
			} catch (Exception ex) {
				System.err.println("Problem loading audio file: " + ex);
			}
		}
	}
	
	private void parseStyleDefs(List<String> docPaths) {
		for (String docPath : docPaths) {
			try {
				Document doc = app.getAssetManager().loadAsset(new AssetKey<Document>(docPath));
				if (doc == null) {
					throw new AssetNotFoundException(String.format("Could not find style %s", docPath));
				}
				NodeList nLst = doc.getElementsByTagName("element");

				for (int s = 0; s < nLst.getLength(); s++) {
					Node fstNode = nLst.item(s);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						Element fstElmnt = (Element) fstNode;
						String key = XMLHelper.getNodeAttributeValue(fstNode, "name");

						Style style = new Style();

						try {
							Node nds = fstElmnt.getElementsByTagName("attributes").item(0);
							Element el = (Element) nds;
							NodeList nodes = el.getElementsByTagName("property");

							for (int n = 0; n < nodes.getLength(); n++) {
								Node nNode = nodes.item(n);
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
									Element nElmnt = (Element) nNode;
									addStyleTag(style, nNode, nElmnt);
								}
							}

							nds = fstElmnt.getElementsByTagName("images").item(0);
							el = (Element) nds;
							nodes = el.getElementsByTagName("property");

							for (int n = 0; n < nodes.getLength(); n++) {
								Node nNode = nodes.item(n);
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
									Element nElmnt = (Element) nNode;
									addStyleTag(style, nNode, nElmnt);
								}
							}

							nds = fstElmnt.getElementsByTagName("font").item(0);
							el = (Element) nds;
							nodes = el.getElementsByTagName("property");

							for (int n = 0; n < nodes.getLength(); n++) {
								Node nNode = nodes.item(n);
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
									Element nElmnt = (Element) nNode;
									addStyleTag(style, nNode, nElmnt);
								}
							}

							nds = fstElmnt.getElementsByTagName("effects").item(0);
							el = (Element) nds;
							nodes = el.getElementsByTagName("property");

							for (int n = 0; n < nodes.getLength(); n++) {
								Node nNode = nodes.item(n);
								if (nNode.getNodeType() == Node.ELEMENT_NODE) {
									Element nElmnt = (Element) nNode;
									addStyleTag(style, nNode, nElmnt);
								}
							}
						} catch (Exception ex) {
							System.err.println("Problem parsing attributes: " + ex);
						}
						styles.put(key, style);
					}
				}
			} catch (Exception ex) {
				System.err.println("Problem loading control definition: " + ex);
			}
		}
	}
	
	private void addStyleTag(Style style, Node nNode, Element nElmnt) {
		String name = XMLHelper.getNodeAttributeValue(nNode, "name");
		String type = XMLHelper.getNodeAttributeValue(nNode, "type");
		if (type.equals("Vector2f")) {
			style.putTag(
				name,
				new Vector2f(
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("x").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("y").item(0), "value"))
				)
			);
		} else if (type.equals("Vector3f")) {
			style.putTag(
				name,
				new Vector3f(
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("x").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("y").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("z").item(0), "value"))
				)
			);
		} else if (type.equals("Vector4f")) {
			style.putTag(
				name,
				new Vector4f(
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("x").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("y").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("z").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("w").item(0), "value"))
				)
			);
		} else if (type.equals("ColorRGBA")) {
			style.putTag(
				name,
				new ColorRGBA(
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("r").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("g").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("b").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("a").item(0), "value"))
				)
			);
		} else if (type.equals("float")) {
			style.putTag(
				name,
				Float.parseFloat(XMLHelper.getNodeAttributeValue(nNode, "value"))
			);
		} else if (type.equals("int")) {
			style.putTag(
				name,
				Integer.parseInt(XMLHelper.getNodeAttributeValue(nNode, "value"))
			);
		} else if (type.equals("boolean")) {
			style.putTag(
				name,
				Boolean.parseBoolean(XMLHelper.getNodeAttributeValue(nNode, "value"))
			);
		} else if (type.equals("String")) {
			style.putTag(
				name,
				XMLHelper.getNodeAttributeValue(nNode, "value")
			);
		} else if (type.equals("Effect")) {
			style.putTag(
				name,
				new Effect(
					Effect.EffectType.valueOf(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("effect").item(0), "value")),
					Effect.EffectEvent.valueOf(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("event").item(0), "value")),
					Float.parseFloat(XMLHelper.getNodeAttributeValue(nElmnt.getElementsByTagName("speed").item(0), "value"))
				)
			);
		}
	}
	
	/**
	 * Returns the Style object associated to the provided key
	 * @param key The String key of the Style
	 * @return Style style
	 */
	public Style getStyle(String key) {
		return this.styles.get(key);
	}
	
	public AudioNode getAudioNode(String key) {
		return this.audioNodes.get(key);
	}
	
	public JmeCursor getCursor(CursorType cursorType) {
		return this.cursors.get(cursorType);
	}
}
