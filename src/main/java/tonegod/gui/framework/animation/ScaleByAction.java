/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.animation;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


/**
 *
 * @author t0neg0d
 */
public class ScaleByAction extends TemporalAction {

	private float initX = -1, initY = -1;
	private float amountX, amountY;
	private float initDuration = -1;
	private float nextPercent = 0;
	private float lastPercent = 0;
	private boolean autoReverse = false;
	private boolean initAutoReverse = false;
	private int cycles = 0;
	
	@Override
	protected void begin() {
		lastPercent = 0;
		nextPercent = 0;
		if (autoReverse) {
			if (!forceJmeTransform)			reverseQuad();
			else {
				if (quad instanceof Node)	reverseTransform();
				else						reverseQuad();
			}
			if (initDuration == -1)	{
				initDuration = getDuration();
				setDuration(initDuration*.5f);
			}
		}
	}
	
	private void reverseQuad() {
		if (initX == -1) initX = quad.getScaleX();
		if (initY == -1) initY = quad.getScaleY();
	}
	
	private void reverseTransform() {
		if (initX == -1) initX = ((Spatial)quad).getLocalScale().getX();
		if (initY == -1) initY = ((Spatial)quad).getLocalScale().getY();
	}
	
	@Override
	protected void update(float percent) {
		nextPercent = percent - lastPercent;
		lastPercent = percent;
		
		if (!forceJmeTransform)			updateQuad();
		else {
			if (quad instanceof Node)	updateTransform();
			else						updateQuad();
		}
	}
	
	private void updateQuad() {
		quad.setScaleX(quad.getScaleX()+(amountX * nextPercent));
		quad.setScaleY(quad.getScaleY()+(amountY * nextPercent));
	}
	
	private void updateTransform() {
		((Spatial)quad).setLocalScale(
			((Spatial)quad).getLocalScale().getX()+(amountX * nextPercent),
			((Spatial)quad).getLocalScale().getY()+(amountY * nextPercent),
			((Spatial)quad).getLocalScale().getZ()
		);
	}
	
	@Override
	protected void end() {
		if (autoReverse && cycles == 0) {
			restart();
			autoReverse = false;
			amountX = -amountX;
			amountY = -amountY;
			cycles = 1;
		} else if (cycles == 1) {
			if (!forceJmeTransform) {
				quad.setScaleX(initX);
				quad.setScaleY(initY);
			} else {
				if (quad instanceof Node) {
					((Spatial)quad).setLocalScale(initX,initY,((Spatial)quad).getLocalScale().z);
				} else {
					quad.setScaleX(initX);
					quad.setScaleY(initY);
				}
			}
			amountX = -amountX;
			amountY = -amountY;
			autoReverse = initAutoReverse;
			cycles = 0;
		}
	}

	public void setAmount (float x, float y) {
		amountX = x;
		amountY = y;
	}

	public void setAmount (float scale) {
		amountX = scale;
		amountY = scale;
	}

	public float getAmountX () {
		return amountX;
	}

	public void setAmountX (float x) {
		this.amountX = x;
	}

	public float getAmountY () {
		return amountY;
	}

	public void setAmountY (float y) {
		this.amountY = y;
	}
	
	public void setAutoReverse(boolean autoReverse) {
		this.autoReverse = autoReverse;
		initAutoReverse = autoReverse;
	}
	
	@Override
	public ScaleByAction clone() {
		ScaleByAction sba = new ScaleByAction();
		sba.setAmount(amountX, amountY);
		sba.setDuration(getDuration());
		sba.setAutoRestart(getAutoRestart());
		sba.setAutoReverse(autoReverse);
		sba.setInterpolation(getInterpolation());
		sba.setForceJmeTransform(forceJmeTransform);
		return sba;
	}
}
