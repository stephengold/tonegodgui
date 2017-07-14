/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras;

import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.awt.Color;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.Dial;
import tonegod.gui.controls.lists.Slider;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class ColorWheel extends Window {
	Element content;
	Dial primarySelector;
	Element colorSwatch;
	Slider secondarySelector, sR, sG, sB, sH, sL, sS, sA;
	TextField tfR, tfG, tfB, tfA;
	TextField tfHex;
	ButtonAdapter bFinish;
	Element blackToColor, colorToWhite;
	ColorRGBA finalColor = new ColorRGBA(1,0,0,1);
	
	int R = 255, G = 0, B = 0, A = 100, H = 100, S = 100, L = 100;
	float red = 1.0f, green = 0.0f, blue = 0.0f, alpha = 1.0f, hue = 1.0f, saturation = 1.0f, light = 1.0f;
	float finalRed = 1.0f, finalGreen = 0.0f, finalBlue = 0.0f, finalAlpha = 1.0f, finalHue = 1.0f, finalSaturation = 1.0f, finalLight = 1.0f;
	
	
	Vector2f sliderDim = new Vector2f(150, 15);
	Vector2f textDim = new Vector2f(60, 15);
	Vector2f labelDim = new Vector2f(30, 15);
	
	/**
	 * Creates a new instance of the ColorWheel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ColorWheel(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("ColorWheel").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ColorWheel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ColorWheel(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("ColorWheel").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ColorWheel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Element
	 */
	public ColorWheel(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg);
	}
	
	/**
	 * Creates a new instance of the ColorWheel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ColorWheel(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("ColorWheel").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ColorWheel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ColorWheel(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("ColorWheel").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the ColorWheel control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Element
	 */
	public ColorWheel(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		setScaleEW(false);
		setScaleNS(false);
		setIsResizable(false);
		
		float contentPadding = screen.getStyle("ColorWheel").getFloat("contentPadding");
		float labelYOffset = screen.getStyle("ColorWheel").getFloat("labelYOffset");
		
		float psSize = 200;
		float sX = 210;
		float tfX = 390;
		float lX = 360;
		float yInc = 20;
		float tfY = labelYOffset;
		float lY = 0;
		float sY = labelYOffset;
		float hexX = 242;
		float hexY = getHeight()-34;
		
		content = new Element(screen, UID + ":Content", Vector2f.ZERO, dimensions, Vector4f.ZERO, null);
		content.setAsContainerOnly();
		content.setIsMovable(false);
		content.setIsResizable(false);
		content.setIgnoreMouse(true);
		content.setDocking(Docking.NW);
		
		primarySelector = new Dial(screen, UID + ":primarySelector", new Vector2f(0,0), new Vector2f(psSize,psSize), Vector4f.ZERO, screen.getStyle("ColorWheel").getString("colorWheelImgTC")) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				setHFromWheel();
				sH.setSelectedIndex(100-H);
				getRGBFromHue();
				factor();
				sR.setSelectedIndex((int)(red*100));
				sG.setSelectedIndex((int)(green*100));
				sB.setSelectedIndex((int)(blue*100));
				RGBToHSL();
				sS.setSelectedIndex(S);
				sL.setSelectedIndex(L);
				displayFactoredColor();
			}
		};
		if (screen.getUseTextureAtlas()) {
			primarySelector.setTextureAtlasImage(
				screen.createNewTexture(
					screen.getStyle("ColorWheel").getString("colorWheelImg")
				), screen.getStyle("ColorWheel").getString("colorWheelImgTC")
			);
		}
		if (screen.getUseTextureAtlas())
			primarySelector.setDialImageIndicator(screen.getStyle("ColorWheel").getString("colorWheelSelectorImgTC"));
		else
			primarySelector.setDialImageIndicator(screen.getStyle("ColorWheel").getString("colorWheelSelectorImg"));
			
		if (screen.getUseTextureAtlas()) {
			primarySelector.getDialCenter().setTextureAtlasImage(
				screen.createNewTexture(
					screen.getStyle("ColorWheel").getString("colorWheelSelectorImg")
				), screen.getStyle("ColorWheel").getString("colorWheelSelectorImgTC")
			);
			primarySelector.getDialIndicator().setColorMap(screen.getStyle("Common").getString("blankImg"));
		}
		primarySelector.setIsMovable(false);
		primarySelector.setIsResizable(false);
		primarySelector.setDocking(Docking.NW);
		content.addChild(primarySelector);
		
		float csSize = 76;
		float csPos = (primarySelector.getWidth()/2)-(csSize/2);
		
		colorSwatch = new Element(screen, UID + ":colorSwatch", new Vector2f(csPos, csPos), new Vector2f(csSize,csSize), Vector4f.ZERO, null);
		colorSwatch.getElementMaterial().setColor("Color", new ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f));
		colorSwatch.setScaleEW(false);
		colorSwatch.setScaleNS(false);
		colorSwatch.setIsMovable(false);
		colorSwatch.setIsResizable(false);
		content.addChild(colorSwatch);
		
		// Add textfields
		tfR = getTextField("tfR", tfX, tfY);
		content.addChild(tfR);
		tfY += yInc;
		tfG = getTextField("tfG", tfX, tfY);
		content.addChild(tfG);
		tfY += yInc;
		tfB = getTextField("tfB", tfX, tfY);
		content.addChild(tfB);
		tfY += yInc*4;
		tfA = getTextField("tfA", tfX, tfY);
		content.addChild(tfA);
		
		// Add labels
		content.addChild(getLabel("lR", lX, lY, "  R  "));
		lY += yInc;
		content.addChild(getLabel("lG", lX, lY, "  G  "));
		lY += yInc;
		content.addChild(getLabel("lB", lX, lY, "  B  "));
		lY += yInc;
		content.addChild(getLabel("lH", lX, lY, "  H  "));
		lY += yInc;
		content.addChild(getLabel("lS", lX, lY, "  S  "));
		lY += yInc;
		content.addChild(getLabel("lL", lX, lY, "  L  "));
		lY += yInc;
		content.addChild(getLabel("lA", lX, lY, "  A  "));
		
		Label lHex = new Label(screen, UID + ":lHex", new Vector2f(sX, hexY-labelYOffset), textDim);
		lHex.setFontSize(screen.getStyle("ColorWheel").getFloat("labelFontSize"));
		lHex.setTextVAlign(BitmapFont.VAlign.Center);
		lHex.setText("HEX: #");
		lHex.setScaleEW(false);
		lHex.setScaleNS(false);
		content.addChild(lHex);
		
		tfHex = getTextField("tfHex", sX+lHex.getWidth(), hexY);
		tfHex.setType(TextField.Type.ALPHANUMERIC_NOSPACE);
		content.addChild(tfHex);
		
		// Add sliders
		sR = new Slider(screen, UID + ":sR", new Vector2f(sX, sY), new Vector2f(150, 15), Vector4f.ZERO, null, Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				R = (int)(selectedIndex*2.55f);
				RGBToHSL();
				sH.setSelectedIndex(100-H);
				int hIndex = H+51;
				if (hIndex > 100) hIndex -= 101;
				primarySelector.setSelectedIndex(100-hIndex);
				sS.setSelectedIndex(S);
				sL.setSelectedIndex(L);
				factor();
				displayFactoredColor();
			}
		};
		sR.getElementMaterial().setColor("Color", new ColorRGBA(1.0f, 0.0f, 0.0f, 1.0f));
		content.addChild(sR);
		sY += yInc;
		
		sG = new Slider(screen, UID + ":sG", new Vector2f(sX, sY), new Vector2f(150, 15), Vector4f.ZERO, null, Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				G = (int)(selectedIndex*2.55f);
				RGBToHSL();
				sH.setSelectedIndex(100-H);
				int hIndex = H+51;
				if (hIndex > 100) hIndex -= 101;
				primarySelector.setSelectedIndex(100-hIndex);
				sS.setSelectedIndex(S);
				sL.setSelectedIndex(L);
				factor();
				displayFactoredColor();
			}
		};
		sG.getElementMaterial().setColor("Color", new ColorRGBA(0.0f, 1.0f, 0.0f, 1.0f));
		content.addChild(sG);
		sY += yInc;
		
		sB = new Slider(screen, UID + ":sB", new Vector2f(sX, sY), new Vector2f(150, 15), Vector4f.ZERO, null, Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				B = (int)(selectedIndex*2.55f);
				RGBToHSL();
				sH.setSelectedIndex(100-H);
				int hIndex = H+51;
				if (hIndex > 100) hIndex -= 101;
				primarySelector.setSelectedIndex(100-hIndex);
				sS.setSelectedIndex(S);
				sL.setSelectedIndex(L);
				factor();
				displayFactoredColor();
			}
		};
		sB.getElementMaterial().setColor("Color", new ColorRGBA(0.0f, 0.0f, 1.0f, 1.0f));
		content.addChild(sB);
		sY += yInc;
		
		addHueSliderBG(sX, sY, 150, 15);
		sH = new Slider(screen, UID + ":sH", new Vector2f(sX, sY), new Vector2f(150,15), Vector4f.ZERO, null, Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				setHFromSlider();
				int hIndex = H+51;
				if (hIndex > 100) hIndex -= 101;
				primarySelector.setSelectedIndex(100-hIndex);
				getRGBFromHue();
				factor();
				sR.setSelectedIndex((int)(red*100));
				sG.setSelectedIndex((int)(green*100));
				sB.setSelectedIndex((int)(blue*100));
				sS.setSelectedIndex(S);
				sL.setSelectedIndex(L);
				displayFactoredColor();
			}
		};
		content.addChild(sH);
		sY += yInc;
		
		sS = new Slider(screen, UID + ":sS", new Vector2f(sX, sY), new Vector2f(150,15), Vector4f.ZERO, screen.getStyle("ColorWheel").getString("colorSImg"), Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				S = selectedIndex;
				HSLToRGB();
				factor();
				sR.setSelectedIndex((int)(red*100));
				sG.setSelectedIndex((int)(green*100));
				sB.setSelectedIndex((int)(blue*100));
				displayFactoredColor();
			}
		};
		sS.getElementMaterial().setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		sS.getElementMaterial().setBoolean("VertexColor", true);
		sS.getModel().setGradientFillHorizontal(ColorRGBA.Gray, new ColorRGBA(red, green, blue, 1.0f));
		content.addChild(sS);
		sY += yInc;
		
		sL = new Slider(screen, UID + ":sL", new Vector2f(sX, sY), new Vector2f(150,15), Vector4f.ZERO, screen.getStyle("ColorWheel").getString("colorLImg"), Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				L = selectedIndex;
				HSLToRGB();
				factor();
				sR.setSelectedIndex((int)(red*100));
				sG.setSelectedIndex((int)(green*100));
				sB.setSelectedIndex((int)(blue*100));
				displayFactoredColor();
			}
		};
		sL.getElementMaterial().setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		sL.getElementMaterial().setBoolean("VertexColor", true);
		sL.getModel().setGradientFillHorizontal(ColorRGBA.Black, new ColorRGBA(red, green, blue, 1.0f));
		content.addChild(sL);
		sY += yInc;
		
		sA = new Slider(screen, UID + ":sA", new Vector2f(sX, sY), new Vector2f(150,15), Vector4f.ZERO, screen.getStyle("ColorWheel").getString("colorLImg"), Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				A = selectedIndex;
				factorAlpha();
				displayFactoredColor();
			}
		};
		sA.getElementMaterial().setColor("Color", new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		sA.getElementMaterial().setBoolean("VertexColor", true);
		sA.getModel().setGradientFillHorizontal(new ColorRGBA(finalRed, finalGreen, finalBlue, 0.0f), new ColorRGBA(finalRed, finalGreen, finalBlue, 1.0f));
		content.addChild(sA);
		
		bFinish = new ButtonAdapter(screen, UID + ":bFiniah", new Vector2f(getWidth()-contentPadding-screen.getStyle("Button").getVector2f("defaultSize").x, getHeight()-contentPadding-screen.getStyle("Button").getVector2f("defaultSize").y)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				onComplete(new ColorRGBA(finalRed, finalGreen, finalBlue, finalAlpha));
				hideWindow();
			}
		};
		bFinish.setText("Done");
		bFinish.setDocking(Docking.SE);
		addChild(bFinish);
		
		sR.setSelectedIndex(100);
		sH.setSelectedIndex(100);
		sS.setSelectedIndex(100);
		sL.setSelectedIndex(100);
		sA.setSelectedIndex(100);
		
		content.sizeToContent();
		content.setPosition(contentPadding, contentPadding+getDragBarHeight());
		addChild(content);
		
		resize(getX()+content.getWidth()+(contentPadding*2),getY()+content.getHeight()+(contentPadding*2)+getDragBarHeight(), Borders.SE);
		setWindowTitle("Color Selector");
	}
	
	private TextField getTextField(String id, float x, float y) {
		TextField ret = new TextField(screen, getUID() + ":" + id, new Vector2f(x, y), textDim);
		ret.setType(TextField.Type.NUMERIC);
		ret.setMaxLength(5);
		ret.setFontSize(screen.getStyle("ColorWheel").getFloat("fontSize"));
		ret.setIsEnabled(false);
		ret.setScaleEW(false);
		ret.setScaleNS(false);
		ret.setDocking(Docking.NW);
		return ret;
	}
	
	private Label getLabel(String id, float x, float y, String text) {
		Label ret = new Label(screen, getUID() + ":" + id, new Vector2f(x, y), labelDim);
		ret.setFontSize(screen.getStyle("ColorWheel").getFloat("labelFontSize"));
		ret.setTextVAlign(BitmapFont.VAlign.Center);
		ret.setText(text);
		ret.setScaleEW(false);
		ret.setScaleNS(false);
		ret.setDocking(Docking.NW);
		return ret;
	}
	
	private void addHueSliderBG(float x, float y ,float w, float h) {
		Element bg1 = new Element(screen, getUID()+":HSBG1", new Vector2f(x,y), new Vector2f(w/6,h), Vector4f.ZERO,null);
		bg1.getModel().setGradientFillHorizontal(new ColorRGBA(1,0,0,1), new ColorRGBA(1,0,1,1));
		bg1.getElementMaterial().setColor("Color", ColorRGBA.White);
		bg1.getElementMaterial().setBoolean("VertexColor", true);
		bg1.setScaleEW(false);
		bg1.setScaleNS(false);
		content.addChild(bg1);
		
		Element bg2 = new Element(screen, ":HSBG2", new Vector2f(x+(w/6),y), new Vector2f(w/6,h), Vector4f.ZERO,null);
		bg2.getModel().setGradientFillHorizontal(new ColorRGBA(1,0,1,1), new ColorRGBA(0,0,1,1));
		bg2.getElementMaterial().setColor("Color", ColorRGBA.White);
		bg2.getElementMaterial().setBoolean("VertexColor", true);
		bg2.setScaleEW(false);
		bg2.setScaleNS(false);
		content.addChild(bg2);
		
		Element bg3 = new Element(screen, ":HSBG3", new Vector2f(x+(w/6*2),y), new Vector2f(w/6,h), Vector4f.ZERO,null);
		bg3.getModel().setGradientFillHorizontal(new ColorRGBA(0,0,1,1), new ColorRGBA(0,1,1,1));
		bg3.getElementMaterial().setColor("Color", ColorRGBA.White);
		bg3.getElementMaterial().setBoolean("VertexColor", true);
		bg3.setScaleEW(false);
		bg3.setScaleNS(false);
		content.addChild(bg3);
		
		Element bg4 = new Element(screen, ":HSBG4", new Vector2f(x+(w/6*3),y), new Vector2f(w/6,h), Vector4f.ZERO,null);
		bg4.getModel().setGradientFillHorizontal(new ColorRGBA(0,1,1,1), new ColorRGBA(0,1,0,1));
		bg4.getElementMaterial().setColor("Color", ColorRGBA.White);
		bg4.getElementMaterial().setBoolean("VertexColor", true);
		bg4.setScaleEW(false);
		bg4.setScaleNS(false);
		content.addChild(bg4);
		
		Element bg5 = new Element(screen, ":HSBG5", new Vector2f(x+(w/6*4),y), new Vector2f(w/6,h), Vector4f.ZERO,null);
		bg5.getModel().setGradientFillHorizontal(new ColorRGBA(0,1,0,1), new ColorRGBA(1,1,0,1));
		bg5.getElementMaterial().setColor("Color", ColorRGBA.White);
		bg5.getElementMaterial().setBoolean("VertexColor", true);
		bg5.setScaleEW(false);
		bg5.setScaleNS(false);
		content.addChild(bg5);
		
		Element bg6 = new Element(screen, ":HSBG6", new Vector2f(x+(w/6*5),y), new Vector2f(w/6,h), Vector4f.ZERO,null);
		bg6.getModel().setGradientFillHorizontal(new ColorRGBA(1,1,0,1), new ColorRGBA(1,0,0,1));
		bg6.getElementMaterial().setColor("Color", ColorRGBA.White);
		bg6.getElementMaterial().setBoolean("VertexColor", true);
		bg6.setScaleEW(false);
		bg6.setScaleNS(false);
		content.addChild(bg6);
	}
	
	private void addLightSliderBG(float x, float y ,float w, float h) {
		blackToColor = new Element(screen, getUID()+":blackToColor", new Vector2f(x,y), new Vector2f(w/2,h), Vector4f.ZERO, null);
		blackToColor.getModel().setGradientFillHorizontal(new ColorRGBA(0,0,0,1), new ColorRGBA(1,0,0,1));
		blackToColor.getElementMaterial().setColor("Color", ColorRGBA.White);
		blackToColor.getElementMaterial().setBoolean("VertexColor", true);
		blackToColor.setScaleEW(false);
		blackToColor.setScaleNS(false);
		content.addChild(blackToColor);
		
		colorToWhite = new Element(screen, ":colorToWhite", new Vector2f(x+(w/2),y), new Vector2f(w/2,h), Vector4f.ZERO, null);
		colorToWhite.getModel().setGradientFillHorizontal(new ColorRGBA(1,0,0,1), new ColorRGBA(1,1,1,1));
		colorToWhite.getElementMaterial().setColor("Color", ColorRGBA.White);
		colorToWhite.getElementMaterial().setBoolean("VertexColor", true);
		colorToWhite.setScaleEW(false);
		colorToWhite.setScaleNS(false);
		content.addChild(colorToWhite);
	}
	
	private void setHFromWheel() {
		int hIndex = primarySelector.getSelectedIndex();
		hIndex -= 51;
		if (hIndex < 0) hIndex += 101;
		H = 100-hIndex;
	}
	
	private void setHFromSlider() {
		H = 100-sH.getSelectedIndex();
	}
	
	private void factorRed() {
		red = R/255f;
		finalRed = red;
	}
	
	private void factorGreen() {
		green = G/255f;
		finalGreen = green;
	}
	
	private void factorBlue() {
		blue = B/255f;
		finalBlue = blue;
	}
	
	private void RGBToHSL() {
		float[] hsv = new float[3];
		hsv = Color.RGBtoHSB(R,G,B,hsv);
		H = (int)(hsv[0]*100);
		S = (int)(hsv[1]*100);
		L = (int)(hsv[2]*100);
	}
	
	private void HSLToRGB() {
		int rgb = Color.HSBtoRGB(H*0.01f, S*0.01f, L*0.01f);
		R = (rgb >> 16) & 0xff;
		G = (rgb >> 8) & 0xff;
		B = (rgb >> 0) & 0xff;
	}
	
	private void factorHue() {
		hue = H*0.01f;
		finalHue = hue;
	}
	
	private void getRGBFromHue() {
		HSLToRGB();
	}
	
	private void factorSaturation() {
		saturation = S*0.01f;
		finalSaturation = saturation;
	}
	
	private float average() {
		red = R/255f;
		green = G/255f;
		blue = B/255f;
		float sum = red+green+blue;
		return sum/3;
	}
	
	private void factorLight() {
		light = L*0.01f;
		finalLight = light;
	}
	
	private void factorAlpha() {
		alpha = A*0.01f;
		finalAlpha = alpha;
	}
	
	private void factor() {
		factorRed();
		factorGreen();
		factorBlue();
		factorHue();
		factorSaturation();
		factorLight();
		factorAlpha();
	}
	
	private void displayFactoredColor() {
		String strR = String.valueOf(finalRed);
		if (strR.length() > 5) strR = strR.substring(0,5);
		tfR.setText(strR);
		String strG = String.valueOf(finalGreen);
		if (strG.length() > 5) strG = strG.substring(0,5);
		tfG.setText(strG);
		String strB = String.valueOf(finalBlue);
		if (strB.length() > 5) strB = strB.substring(0,5);
		tfB.setText(strB);
		String strA = String.valueOf(finalAlpha);
		if (strA.length() > 5) strA = strA.substring(0,5);
		tfA.setText(strA);
		String hex = String.format("%02x%02x%02x", (int)(finalRed*255), (int)(finalGreen*255), (int)(finalBlue*255));
		tfHex.setText(hex);
		colorSwatch.getElementMaterial().setColor("Color", new ColorRGBA(finalRed, finalGreen, finalBlue, finalAlpha));
		float av = average();
		sS.getModel().setGradientFillHorizontal(new ColorRGBA(av, av, av, 1.0f), new ColorRGBA(red, green, blue, 1.0f));
		sL.getModel().setGradientFillHorizontal(ColorRGBA.Black, new ColorRGBA(red, green, blue, 1.0f));
		sA.getModel().setGradientFillHorizontal(new ColorRGBA(finalRed, finalGreen, finalBlue, 0.0f), new ColorRGBA(finalRed, finalGreen, finalBlue, 1.0f));
		finalColor.set(finalRed, finalGreen, finalBlue, finalAlpha);
		onChange(finalColor);
	}
	
	public void setColor(ColorRGBA color) {
		R = (int)(color.r*255);
		if (R < 0) R = 0;
		else if (R > 255) R = 255;
		sR.setSelectedIndex((int)(color.r*100));
		G = (int)(color.g*255);
		if (G < 0) G = 0;
		else if (G > 255) G = 255;
		sG.setSelectedIndex((int)(color.g*100));
		B = (int)(color.b*255);
		if (B < 0) B = 0;
		else if (B > 255) B = 255;
		sB.setSelectedIndex((int)(color.b*100));
		A = (int)(color.a*100);
		if (A < 0) A = 0;
		else if (A > 100) A = 100;
		sA.setSelectedIndex(A);
		
		setDisplay();
	}
	
	public void setColor(float red, float green, float blue) {
		if (red < 0) red = 0;
		else if (red > 1) red = 1;
		R = (int)(red*255);
		sR.setSelectedIndex((int)(red*100));
		if (green < 0) green = 0;
		else if (green > 1) green = 1;
		G = (int)(green*255);
		sG.setSelectedIndex((int)(green*100));
		if (blue < 0) blue = 0;
		else if (blue > 1) blue = 1;
		B = (int)(blue*255);
		sB.setSelectedIndex((int)(blue*100));
		
		setDisplay();
	}
	
	public void setColor(float red, float green, float blue, float alpha) {
		if (red < 0) red = 0;
		else if (red > 1) red = 1;
		R = (int)(red*255);
		sR.setSelectedIndex((int)(red*100));
		if (green < 0) green = 0;
		else if (green > 1) green = 1;
		G = (int)(green*255);
		sG.setSelectedIndex((int)(green*100));
		if (blue < 0) blue = 0;
		else if (blue > 1) blue = 1;
		B = (int)(blue*255);
		sB.setSelectedIndex((int)(blue*100));
		if (alpha < 0) alpha = 0;
		else if (alpha > 1) alpha = 1;
		A = (int)(alpha*100);
		sA.setSelectedIndex(A);
		
		setDisplay();
	}
	
	public void setColor(int red, int green, int blue) {
		if (red < 0) red = 0;
		else if (red > 255) red = 255;
		R = red;
		if (green < 0) green = 0;
		else if (green > 255) green = 255;
		G = green;
		if (blue < 0) blue = 0;
		else if (blue > 255) blue = 255;
		B = blue;
		
		setDisplay();
	}
	
	public void setRed(int red) {
		if (red < 0) red = 0;
		else if (red > 255) red = 255;
		R = red;
	}
	
	public void setRed(float red) {
		if (red < 0) red = 0;
		else if (red > 1) red = 1;
		R = (int)(red*255);
		sR.setSelectedIndex((int)(red*100));
		
		setDisplay();
	}
	
	public void setGreen(int green) {
		if (green < 0) green = 0;
		else if (green > 255) green = 255;
		G = green;
	}
	
	public void setGreen(float green) {
		if (green < 0) green = 0;
		else if (green > 1) green = 1;
		G = (int)(green*255);
		sG.setSelectedIndex((int)(green*100));
		
		setDisplay();
	}
	
	public void setBlue(int blue) {
		if (blue < 0) blue = 0;
		else if (blue > 255) blue = 255;
		B = blue;
	}
	
	public void setBlue(float blue) {
		if (blue < 0) blue = 0;
		else if (blue > 1) blue = 1;
		B = (int)(blue*255);
		sB.setSelectedIndex((int)(blue*100));
		
		setDisplay();
	}
	
	public void setAlpha(float alpha) {
		if (alpha < 0) alpha = 0;
		else if (alpha > 1) alpha = 1;
		A = (int)(alpha*100);
		sA.setSelectedIndex(A);
		
		setDisplay();
	}
	
	public void setHue(float hue) {
		if (hue < 0) hue = 0;
		else if (hue > 1) hue = 1;
		H = (int)(hue*100);
		sH.setSelectedIndex(H);
	}
	
	public void setSaturation(float saturation) {
		if (saturation < 0) saturation = 0;
		else if (saturation > 1) saturation = 1;
		S = (int)(saturation*100);
		sS.setSelectedIndex(S);
	}
	
	public void setLight(float light) {
		if (light < 0) light = 0;
		else if (light > 1) light = 1;
		L = (int)(light*100);
		sL.setSelectedIndex(L);
	}
	
	public ColorRGBA getColor() {
		return new ColorRGBA(finalRed, finalGreen, finalBlue, finalAlpha);
	}
	
	public float getRed() {
		return finalRed;
	}
	
	public float getGreen() {
		return finalGreen;
	}
	
	public float getBlue() {
		return finalGreen;
	}
	
	public float getAlpha() {
		return finalAlpha;
	}
	
	public float getHue() {
		return finalHue;
	}
	
	public float getSaturation() {
		return finalSaturation;
	}
	
	public float getLight() {
		return finalLight;
	}
	
	private void setDisplay() {
		RGBToHSL();
		sH.setSelectedIndex(100-H);
		int hIndex = H+51;
		if (hIndex > 100) hIndex -= 101;
		primarySelector.setSelectedIndex(100-hIndex);
		sS.setSelectedIndex(S);
		sL.setSelectedIndex(L);
		factor();
		displayFactoredColor();
	}
	
	public abstract void onChange(ColorRGBA color);
	
	public abstract void onComplete(ColorRGBA color);
}
