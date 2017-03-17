/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.android;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;
import tonegod.gui.effects.Effect;

/**
 *
 * @author t0neg0d
 */
public abstract class Joystick extends Element implements Control {
	private ButtonAdapter thumb;
	private Vector2f origin = new Vector2f();
	private Vector2f centerVec = new Vector2f();
	private float maxDistance;
	private float deltaX, deltaY;
	private Vector2f tempV2 = new Vector2f();
	
	public Joystick(ElementManager screen, Vector2f position, int size) {
		super(screen, UIDUtil.getUID(),
			position, new Vector2f(size, size), new Vector4f(0,0,0,0),
			screen.getStyle("Common").getString("blankImg")
		);
		setIsMovable(false);
		setIsResizable(false);
		setScaleEW(false);
		setScaleNS(false);
		setDocking(Docking.SW);
		
		maxDistance = getDimensions().x/2;
		
		Texture texBG = screen.createNewTexture("tonegod/gui/style/atlasdef/android/joystick_bg.png");
		setTextureAtlasImage(texBG, "x=0|y=0|w=128|h=128");
		
		tempV2.set(getWidth()/2,getHeight()/2);
		
		thumb = new ButtonAdapter(screen, UIDUtil.getUID(),
			new Vector2f(getWidth()/2-(tempV2.x/2), getHeight()/2-(tempV2.y/2)),
			tempV2,
			new Vector4f(0,0,0,0),
			screen.getStyle("Common").getString("blankImg")
		) {
			@Override
			public void controlMoveHook() {
				if (getPosition().distance(origin) > maxDistance)
					setPosition(getPosition().subtract(centerVec).normalize().mult(maxDistance).add(centerVec));
				deltaX = (getPosition().x-centerVec.x);
				deltaX /= maxDistance;
				deltaY = (getPosition().y-centerVec.x);
				deltaY /= maxDistance;
			}
			
			@Override
			public void onButtonMouseLeftDown(MouseButtonEvent evt, boolean isToggled) {
				
			}
			
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				setPosition(origin);
				deltaX = (getPosition().x-centerVec.x);
				deltaX /= maxDistance;
				deltaY = (getPosition().y-centerVec.x);
				deltaY /= maxDistance;
			}
		};
		thumb.setDocking(Docking.SW);
		thumb.setIsMovable(true);
		thumb.clearAltImages();
		thumb.removeEffect(Effect.EffectEvent.Hover);
		thumb.removeEffect(Effect.EffectEvent.Press);
		thumb.removeEffect(Effect.EffectEvent.GetFocus);
		thumb.removeEffect(Effect.EffectEvent.LoseFocus);
		
		origin.set(thumb.getPosition());
		
		Texture texThumb = screen.createNewTexture("tonegod/gui/style/atlasdef/android/joystick_thumb.png");
		thumb.setTextureAtlasImage(texThumb, "x=0|y=0|w=32|h=32");
		
		addChild(thumb);
		
		float dist = (size/2);
		dist -= tempV2.x/2;
		centerVec.set(dist,dist);
		
		addControl(this);
	}

	public ButtonAdapter getThumb() { return this.thumb; }
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return this;
	}

	@Override
	public void update(float tpf) {
		onUpdate(tpf, deltaX, deltaY);
	}
	
	public abstract void onUpdate(float tpf, float deltaX, float deltaY);
	
	@Override
	public void render(RenderManager rm, ViewPort vp) {  }
}
