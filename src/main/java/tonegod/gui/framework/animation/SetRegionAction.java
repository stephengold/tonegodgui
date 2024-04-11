/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.animation;

import tonegod.gui.framework.core.QuadData;
import tonegod.gui.framework.core.TextureRegion;

/**
 *
 * @author t0neg0d
 */
public class SetRegionAction extends TemporalAction {
	TextureRegion tr;
	
	@Override
	protected void begin() {
		((QuadData)quad).setTextureRegion(tr);
		setDuration(0);
	}
	
	@Override
	protected void update(float percent) {  }
	
	@Override
	protected void end() {  }

	@Override
	public void restart() {
		setTime(0);
		setComplete(false);
		setDuration(1);
		reset();
	}
	
	public void setTextureRegion(TextureRegion tr) {
		this.tr = tr;
	}
	
	@Override
	public SetRegionAction clone() {
		SetRegionAction sra = new SetRegionAction();
		sra.setTextureRegion(tr);
		
		return sra;
	}
}