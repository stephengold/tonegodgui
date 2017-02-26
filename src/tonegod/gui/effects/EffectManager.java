/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.effects;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class EffectManager implements Control {
	Screen screen;
	Spatial spatial;
//	private Map<String, Effect> currentEffects = new HashMap();
	private List<Effect> currentEffects = new ArrayList<>();
	private List<EffectQueue> currentEffectQueues = new ArrayList<>();
	private List<BatchEffect> currentBatchEffects = new ArrayList<>();
	
	public EffectManager(Screen screen) {
		this.screen = screen;
	}
	
	public void applyEffect(Effect effect) {
		if (effect != null) {
			for (Effect ef : currentEffects) {
				if (effect.getElement().getUID().equals(ef.getElement().getUID())) {
					if (ef.getEffectType() == Effect.EffectType.Pulse || ef.getEffectType() == Effect.EffectType.PulseColor)
						ef.setIsActive(false);
				}
			}
			currentEffects.add(effect);
			if (effect.getAudioFile() != null) {
				if (screen.getUseUIAudio())
					screen.playAudioNode(effect.getAudioFile(), effect.getAudioVolume());
			}
		}
	}

	public void applyEffectQueue(EffectQueue queue) {
		queue.setEffectManager(this);
		currentEffectQueues.add(queue);
	}
	
	public void applyBatchEffect(BatchEffect batch) {
		batch.setEffectManager(this);
		currentBatchEffects.add(batch);
		batch.startBatch();
	}
	
	@Override
	public void update(float tpf) {
		try {
			for (Effect effect : currentEffects) {
				if (effect.getIsActive())
					effect.update(tpf);
				else {
					currentEffects.remove(effect);
					break;
				}
			}
		} catch (Exception ex) {
			// Temporary error consumption for menu rebuild checkbox issue
		}
		for (EffectQueue queue : currentEffectQueues) {
			if (queue.getIsActive())
				queue.update(tpf);
			else {
				currentEffectQueues.remove(queue);
				break;
			}
		}
		for (BatchEffect batch : currentBatchEffects) {
			if (batch.getIsActive())
				batch.update(tpf);
			else {
				currentBatchEffects.remove(batch);
				break;
			}
		}
	}
	
	@Override
	public Control cloneForSpatial(Spatial spatial) {
		return this;
	}

	@Override
	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	@Override
	public void render(RenderManager rm, ViewPort vp) {  }
	@Override
	public void write(JmeExporter ex) throws IOException {  }
	@Override
	public void read(JmeImporter im) throws IOException {  }
}
