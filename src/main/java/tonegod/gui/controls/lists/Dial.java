/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.lists;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.ElementQuadGrid;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public abstract class Dial extends ButtonAdapter {
	Element elCenter, elPosition, elIndicator;
	protected List<Object> stepValues = new ArrayList<>();
	
	int selectedIndex = 0;
	boolean isStepped = false;
	float stepSize = 1;
	float currentAngle = 0;
	float startGap, endGap, totalGap, totalSize;
	
	float minDegrees = 0;
	float maxDegrees = 359;
	
	Texture indicatorTex;
	
	/**
	 * Creates a new instance of the Dial control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public Dial(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Dial").getVector2f("defaultSize"),
			screen.getStyle("Dial").getVector4f("resizeBorders"),
			screen.getStyle("Dial").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Dial control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public Dial(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Dial").getVector2f("defaultSize"),
			screen.getStyle("Dial").getVector4f("resizeBorders"),
			screen.getStyle("Dial").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Dial control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Dial(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Dial").getVector4f("resizeBorders"),
			screen.getStyle("Dial").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Dial control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Dial's background image
	 */
	public Dial(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the Dial control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public Dial(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Dial").getVector2f("defaultSize"),
			screen.getStyle("Dial").getVector4f("resizeBorders"),
			screen.getStyle("Dial").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Dial control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public Dial(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Dial").getVector4f("resizeBorders"),
			screen.getStyle("Dial").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Dial control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Dial's background image
	 */
	public Dial(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
		removeEffect(Effect.EffectEvent.Hover);
		removeEffect(Effect.EffectEvent.Press);
		removeEffect(Effect.EffectEvent.LoseFocus);
		
		startGap = minDegrees;
		endGap = 359-maxDegrees;
		totalGap = startGap + endGap;
		totalSize = 359-totalGap;
		
		elCenter = new Element(
			screen,
			UID + ":Center",
			new Vector2f((getWidth()/2), -(getHeight()/2)),
			new Vector2f(dimensions.x, dimensions.y),
			new Vector4f(0,0,0,0),
			screen.getStyle("Common").getString("blankImg")
		);
		elCenter.getChild(0).center();
		elCenter.setScaleNS(false);
		elCenter.setScaleEW(false);
		elCenter.setDocking(Docking.SW);
		elCenter.setIgnoreMouse(true);
		addChild(elCenter);
		
		elIndicator = new Element(
			screen,
			UID + ":Indicator",
			new Vector2f(-1,getHeight()/2),
			new Vector2f(5, 5),
			new Vector4f(0,0,0,0),
			screen.getStyle("Dial").getString("radialImg")
		);
		elIndicator.getChild(0).center();
		elIndicator.setScaleNS(false);
		elIndicator.setScaleEW(false);
		elIndicator.setDocking(Docking.SW);
		elIndicator.setIgnoreMouse(true);
		elCenter.addChild(elIndicator);
		
		setStepSize();
		setInterval(100);
	}
	
	public Element getDialCenter() {
		return this.elCenter;
	}
	
	public Element getDialIndicator() {
		return this.elIndicator;
	}
	
	/**
	 * Sets the angle at which the minimum rotation of the dial will start (0 being the bottom)
	 * @param angle float
	 */
	public void setGapStartAngle(int angle) {
		if (angle < 0) angle = 0;
		minDegrees = angle;
		startGap = minDegrees;
		endGap = 359-maxDegrees;
		totalGap = startGap + endGap;
		totalSize = 359-totalGap;
		setStepSize();
	}
	
	/**
	 * Sets the angle at which the maximum rotation of the dial will stop (359 being the bottom)
	 * @param angle float
	 */
	public void setGapEndAngle(int angle) {
		if (angle > 359) angle = 359;
		maxDegrees = angle;
		startGap = minDegrees;
		endGap = 359-maxDegrees;
		totalGap = startGap + endGap;
		totalSize = 359-totalGap;
		setStepSize();
	}

	/**
	 * Adds a step value to the Slider.  When 2 or more step values are associated with
	 * a Dial, the rotation becomes stepped and advances to the next/previous slot
	 * position as the mouse is moved.  Each slot added has an associated value that is
	 * returned via the onChange event or getSelectedValue() method.
	 * 
	 * @param value The string value to add for the next step.
	 */
	public void addStepValue(Object value) {
		stepValues.add(value);
		if (stepValues.size() >= 2) {
			isStepped = true;
			setStepSize();
		}
	}
	
	/**
	 * Removes a step value by the value originally added.
	 * 
	 * @param value The string value of the step to be removed.
	 */
	public void removeStepValue(Object value) {
		stepValues.remove(value);
		if (stepValues.size() < 2) {
			isStepped = false;
			setStepSize();
		}
	}
	
	/**
	 * Sets the Dial's rotating indicator image
	 * @param imgPath String Path to image
	 */
	public void setDialImageIndicator(String imgPath) {
		if (screen.getUseTextureAtlas()) {
			indicatorTex = screen.getAtlasTexture();
			float[] coords = screen.parseAtlasCoords(imgPath);
			float textureAtlasX = coords[0];
			float textureAtlasY = coords[1];
			float textureAtlasW = coords[2];
			float textureAtlasH = coords[3];

			float imgWidth = indicatorTex.getImage().getWidth();
			float imgHeight = indicatorTex.getImage().getHeight();
			float pixelWidth = 1f/imgWidth;
			float pixelHeight = 1f/imgHeight;

			textureAtlasY = imgHeight-textureAtlasY-textureAtlasH;
			
			Mesh model = new ElementQuadGrid(this.getDimensions(), borders, imgWidth, imgHeight, pixelWidth, pixelHeight, textureAtlasX, textureAtlasY, textureAtlasW, textureAtlasH);
			
			elCenter.getGeometry().setMesh(model);
			elCenter.getGeometry().center();
		} else {
			indicatorTex = screen.getApplication().getAssetManager().loadTexture(imgPath);
			indicatorTex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
			indicatorTex.setMagFilter(Texture.MagFilter.Bilinear);
			indicatorTex.setWrap(Texture.WrapMode.Repeat);
		}
		elCenter.getElementMaterial().setTexture("ColorMap", indicatorTex);
	}
	
	/**
	 * Sets the Dial's background image
	 * @param imgPath String Path to image
	 */
	public void setDialImageBackground(String imgPath) {
		Texture tex = screen.getApplication().getAssetManager().loadTexture(imgPath);
		tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
		tex.setMagFilter(Texture.MagFilter.Bilinear);
		tex.setWrap(Texture.WrapMode.Repeat);
		
		getElementMaterial().setTexture("ColorMap", tex);
	}
	
	private void setStepSize() {
		if (stepValues.size() >= 2)
			stepSize = totalSize/(stepValues.size()-1);
		else
			stepSize = totalSize/100;
	}
	
	private float getStepAngle(float angle) {
		angle += 180-startGap;
		
		int nIndex;
		if (stepValues.size() >= 2) {
			nIndex = Math.round(angle/stepSize);
			if (nIndex >= 0 && nIndex < stepValues.size() && nIndex != this.selectedIndex)
				setInternalIndex(nIndex);
		} else {
			nIndex = Math.round(angle/stepSize);
			int finIndex = Math.round(nIndex);
			if (finIndex != this.selectedIndex)
				setInternalIndex(finIndex);
		}

		return (nIndex*stepSize)-180+startGap;
	}
	
	/**
	 * Sets the selected index for both free-floating and stepped Dials
	 * @param index float
	 */
	public void setSelectedIndex(int index) {
		float angle;
		if (isStepped) {
			if (index < 0) index = 0;
			else if (index > stepValues.size()-1) index = stepValues.size()-1;
			selectedIndex = index;
			angle = index*stepSize-180+startGap;
		} else {
			if (index < 0) index = 0;
			else if (index > 100) index = 100;
			selectedIndex = index;
			angle = index*stepSize-180+startGap;
		}
		elCenter.setLocalRotation(elCenter.getLocalRotation().fromAngleAxis(-(angle*FastMath.DEG_TO_RAD), Vector3f.UNIT_Z));
	}
	
	/**
	 * Sets the selected index for both free-floating and stepped Dials
	 * @param index float
	 */
	public void setSelectedIndexWithCallback(int index) {
		float angle;
		if (isStepped) {
			if (index < 0) index = 0;
			else if (index > stepValues.size()-1) index = stepValues.size()-1;
			setInternalIndex(index);
			angle = index*stepSize-180+startGap;
		} else {
			if (index < 0) index = 0;
			else if (index > 100) index = 100;
			setInternalIndex(index);
			angle = index*stepSize-180+startGap;
		}
		elCenter.setLocalRotation(elCenter.getLocalRotation().fromAngleAxis(-(angle*FastMath.DEG_TO_RAD), Vector3f.UNIT_Z));
	}
	
	/**
	 * For use with free-floating Dials - Sets the selected index of the Dial
	 * @param index float A range from 0.0 to 100.0 for a more accurate representation of the angle desired
	 */
	public void setSelectedIndex(float index) {
		float angle;
		int index1 = Math.round(index);
		if (isStepped) {
			if (index1 < 0) index1 = 0;
			else if (index1 > stepValues.size()-1) index1 = stepValues.size()-1;
			this.selectedIndex = index1;
			angle = index1*stepSize-180+startGap;
		} else {
			if (index1 < 0) index1 = 0;
			else if (index1 > 100) index1 = 100;
			this.selectedIndex = index1;
			angle = index*stepSize-180+startGap;
		}
		elCenter.setLocalRotation(elCenter.getLocalRotation().fromAngleAxis(-(angle*FastMath.DEG_TO_RAD), Vector3f.UNIT_Z));
	}
	
	/**
	 * For use with free-floating Dials - Sets the selected index of the Dial
	 * @param index float A range from 0.0 to 100.0 for a more accurate representation of the angle desired
	 */
	public void setSelectedIndexWithCallback(float index) {
		float angle;
		int index1 = Math.round(index);
		if (isStepped) {
			if (index1 < 0) index1 = 0;
			else if (index1 > stepValues.size()-1) index1 = stepValues.size()-1;
			setInternalIndex(index1);
			angle = index1*stepSize-180+startGap;
		} else {
			if (index1 < 0) index1 = 0;
			else if (index1 > 100) index1 = 100;
			setInternalIndex(index1);
			angle = index*stepSize-180+startGap;
		}
		elCenter.setLocalRotation(elCenter.getLocalRotation().fromAngleAxis(-(angle*FastMath.DEG_TO_RAD), Vector3f.UNIT_Z));
	}
	
	/**
	 * Returns the text value of the current selected step.
	 * 
	 * @return String stepValue
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * Sets the Dial's selected index to the selected step index specified and
	 * rotates the Dial to appropriate angle to reflect this change.
	 * 
	 * @param selectedIndex The index to set the Dial's selectedIndex to.
	 */
	private void setInternalIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		if(isStepped)	onChange(selectedIndex, stepValues.get(selectedIndex));
		else			onChange(selectedIndex, selectedIndex);
	}
	
	/**
	 * Event called when the Slider's selectedIndex changes.
	 * 
	 * @param selectedIndex The Slider's current selectedIndex
	 * @param value The string value associated with this index
	 */
	public abstract void onChange(int selectedIndex, Object value);
	
	@Override
	public void onButtonStillPressedInterval() {
		currentAngle = (float)Math.atan2(screen.getMouseXY().x-elCenter.getAbsoluteX(), screen.getMouseXY().y-elCenter.getAbsoluteY())*FastMath.RAD_TO_DEG;
		
		if (currentAngle < -(180-startGap))
			currentAngle = -(180-startGap);
		else if (currentAngle > (180-endGap))
			currentAngle = (180-endGap);
		
		float angle = Float.valueOf(currentAngle);
		
		angle = getStepAngle(angle);
		
		elCenter.setLocalRotation(elCenter.getLocalRotation().fromAngleAxis(-(angle*FastMath.DEG_TO_RAD), Vector3f.UNIT_Z));
	}
}
