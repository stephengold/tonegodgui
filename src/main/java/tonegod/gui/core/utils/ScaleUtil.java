package tonegod.gui.core.utils;

import com.jme3.font.BitmapFont;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.text.TextElement;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class ScaleUtil {
	private float REF_WIDTH = 720;
	private float REF_HEIGHT = 480;
	
	private Screen screen;
	
	private float baseFontSize = 30;
	private float gameScale = 1;
	private float fontScale = 1;
	
	public ScaleUtil(Screen screen) {
		this.screen = screen;
	}
	
	public void setReferenceScreenDimensions(float REF_WIDTH, float REF_HEIGHT) {
		this.REF_WIDTH = REF_WIDTH;
		this.REF_HEIGHT = REF_HEIGHT;
		initialize();
	}
	
	public void initialize() {
		gameScale = screen.getWidth()/REF_WIDTH;
		fontScale = getFontScale(baseFontSize)/baseFontSize;
	}
	
	public float getGameScale() { return this.gameScale; }
	public float getFontScale() { return this.fontScale; }
	
	private float getFontScale(float startSize) {
		Vector2f dim1 = new Vector2f(REF_WIDTH, REF_HEIGHT);
		TextElement refString = getTestLabel(UIDUtil.getUID(),"Testing", dim1);
		refString.setFontSize(startSize);
		float refScale = refString.getAnimText().getLineWidth()/dim1.x;
		
		Vector2f dim2 = new Vector2f(screen.getWidth(), REF_HEIGHT);
		TextElement testString = getTestLabel(UIDUtil.getUID(),"Testing", dim2);
		startSize = 5;
		testString.setFontSize(startSize);
		float testScale = testString.getAnimText().getLineWidth()/dim2.x;
		
		while (testScale < refScale) {
			startSize++;
			testString.setFontSize(startSize);
			testScale = testString.getAnimText().getLineWidth()/dim2.x;
		}
		
		return startSize;
	}
	
	private TextElement getTestLabel(String UID, String text, Vector2f dim) {
		TextElement el = new TextElement(screen, UID, Vector2f.ZERO, new Vector2f(dim), null) {
			@Override
			public void onUpdate(float tpf) {  }
			@Override
			public void onEffectStart() {  }
			@Override
			public void onEffectStop() {  }
		};
		el.setIsResizable(false);
		el.setIsMovable(false);
		el.setUseTextClipping(false);
		el.setTextWrap(LineWrapMode.NoWrap);
		el.setTextVAlign(BitmapFont.VAlign.Center);
		el.setTextAlign(BitmapFont.Align.Center);
		el.setFont(screen.getDefaultGUIFont());
		el.setFontColor(ColorRGBA.White);
		el.setFontSize(baseFontSize);
		el.setText(text);
		el.setIgnoreMouse(true);
		el.getAnimText().setIgnoreMouse(true);
		return el;
	}
}