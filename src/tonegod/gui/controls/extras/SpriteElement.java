/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public class SpriteElement extends Element implements Control {

	private boolean isEnabled = true;
	private boolean useInterval = true;
	private float framesPerSecond = 4;
	protected float trackInterval = (1/framesPerSecond), currentTrack = 0;
	private Texture sprite;
	private int spriteCols, spriteRows;
	private float imgWidth, imgHeight, spriteWidth, spriteHeight;
	private int currentIndex = 0;
	private int[] frames;
	private int currentFramesIndex = 0;
	private List<String> sprites = new ArrayList<>();
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 */
	public SpriteElement(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg")
		);
	}
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public SpriteElement(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public SpriteElement(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containing the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public SpriteElement(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions, resizeBorders, defaultImg);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public SpriteElement(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Menu").getVector2f("defaultSize"),
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public SpriteElement(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Menu").getVector4f("resizeBorders"),
			screen.getStyle("Menu").getString("defaultImg")
		);
	}
	
	/**
	 * Creates a new instance of the Menu control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public SpriteElement(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		
	
	}
	
	public void setSprite(String imgPath, int rows, int cols, float framesPerSecond) {
		sprite = screen.getApplication().getAssetManager().loadTexture(imgPath);
		sprite.setMagFilter(Texture.MagFilter.Bilinear);
		sprite.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
		sprite.setWrap(Texture.WrapMode.Repeat);
		
		setSprite(sprite, rows, cols, framesPerSecond);
	}
	
	public void setSprite(Texture sprite, int rows, int cols, float framesPerSecond) {
		this.sprite = sprite;
		
		this.spriteRows = rows;
		this.spriteCols = cols;
		
		Image img = sprite.getImage();
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		
		spriteWidth = imgWidth/cols;
		spriteHeight = imgHeight/rows;
		
		sprites.clear();
		for (int y = rows-1; y > -1; y--) {
			for (int x = 0; x < cols; x++) {
				sprites.add("x=" + (x*spriteWidth) + "|y=" + (y*spriteHeight) + "|w=" + spriteWidth + "|h=" + spriteHeight);
			}
		}
		
		this.setTextureAtlasImage(sprite, sprites.get(currentIndex));
		
		this.useInterval = true;
		this.framesPerSecond = framesPerSecond;
		this.trackInterval = (float)(1/framesPerSecond);
		
		setIsEnabled(true);
	}
	
        @Override
	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		if (isEnabled)	this.addControl(this);
		else			this.removeControl(this);
	}
	
        @Override
	public boolean getIsEnabled() {
		return this.isEnabled;
	}
	
	public int getSpriteRowCount() { return this.spriteRows; }
	
	public int getSpriteColCount() { return this.spriteCols; }
	
	public float getSpriteWidth() { return this.spriteWidth; }
	
	public float getSpriteHeight() { return this.spriteHeight; }
	
	public void setFrames(int[] frames) {
		this.frames = frames;
		if (frames != null)
			updateTextureAtlasImage(sprites.get(frames[currentFramesIndex]));
		else
			updateTextureAtlasImage(sprites.get(currentIndex));
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public void setSpatial(Spatial spatial) {
	}

	@Override
	public void update(float tpf) {
		if (isEnabled) {
			if (useInterval) {
				currentTrack += tpf;
				if (currentTrack >= trackInterval) {
					updateSprite();
					currentTrack -= trackInterval;
				}
			}
		}
	}
	
	private void updateSprite() {
		if (frames == null) {
			currentIndex++;
			if (currentIndex == sprites.size())
				currentIndex = 0;
			updateTextureAtlasImage(sprites.get(currentIndex));
		} else {
			currentFramesIndex++;
			if (currentFramesIndex == frames.length)
				currentFramesIndex = 0;
			updateTextureAtlasImage(sprites.get(frames[currentFramesIndex]));
		}
		updateSpriteHook();
	}
	
	public void updateSpriteHook() {  }
	
	public void setCurrentFrame(int row, int col) {
		int frameIndex = spriteRows*row;
		frameIndex += col;
		setCurrentFrame(frameIndex);
	}
	
	public void setCurrentFrame(int frameIndex) {
		if (frameIndex >= 0 && frameIndex < sprites.size())
			this.setTextureAtlasImage(sprite, sprites.get(frameIndex));
	}
	
	@Override
	public void render(RenderManager rm, ViewPort vp) {
		
	}
	
}
