/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.controls.extras.emitter;

import tonegod.gui.controls.extras.emitter.ElementEmitter.ElementParticle;

/**
 *
 * @author t0neg0d
 */
public interface Influencer {
    void update(ElementParticle particle, float tpf);
    void initialize(ElementParticle particle);
    void setIsEnabled(boolean isEnabled);
    boolean getIsEnabled();
}
