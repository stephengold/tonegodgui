/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

/**
 *
 * @author t0neg0d
 */
public abstract class InfluencerBase implements Influencer {
	public ElementEmitter emitter;
	
	public InfluencerBase(ElementEmitter emitter) {
		this.emitter = emitter;
	}
}
