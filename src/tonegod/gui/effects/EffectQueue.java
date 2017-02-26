/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.effects;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author t0neg0d
 */
public class EffectQueue {
	EffectManager effectManager;
	List<EffectQueueItem> queue = new LinkedList<>();
	EffectQueueItem currentEffectItem = null;
	float currentDelay;
	float updateTime = 0;
	float targetTime = 0;
	boolean effectSet = false;
	boolean effectStarted = false;
	boolean isActive = true;
	
	public EffectQueue() {
		
	}
	
	public void addEffect(Effect effect, float delayTime) {
		if (effect.getEffectType() != Effect.EffectType.Pulse && effect.getEffectType() != Effect.EffectType.PulseColor) {
			EffectQueueItem item = new EffectQueueItem(effect, delayTime);
			queue.add(item);
		}
	}
	
	public void addBatchEffect(BatchEffect batchEffect, float delayTime) {
		EffectQueueItem item = new EffectQueueItem(batchEffect, delayTime);
		queue.add(item);
	}
	
	public boolean getIsActive() {
		return this.isActive;
	}
	
	public void update(float tpf) {
		if (isActive) {
			if (!effectSet) {
				currentEffectItem = queue.remove(0);
				targetTime = currentEffectItem.getDelay();
				updateTime  = 0;
				effectSet = true;
				effectStarted = false;
			} else {
				if (!effectStarted) {
					if (updateTime < targetTime) {
						updateTime += tpf/targetTime;
					} else {
						if (currentEffectItem.getEffect() != null) {
						//	currentEffectItem.getEffect().getElement().getScreen().updateZOrder(currentEffectItem.getEffect().getElement());
							effectManager.applyEffect(currentEffectItem.getEffect());
						} else {
						//	currentEffectItem.getBatchEffect().getScreen().updateZOrder(currentEffectItem.getEffect().getElement());
							effectManager.applyBatchEffect(currentEffectItem.getBatchEffect());
						}
						effectStarted = true;
					}
				} else {
					if (currentEffectItem.getEffect() != null) {
						if (!currentEffectItem.getEffect().getIsActive()) {
							effectSet = false;
							effectStarted = false;
							if (queue.isEmpty()) {
								isActive = false;
							//	effectManager.removeEffectQueue(this);
							}
						}
					} else {
						if (!currentEffectItem.getBatchEffect().getIsActive()) {
							effectSet = false;
							effectStarted = false;
							if (queue.isEmpty()) {
								isActive = false;
							//	effectManager.removeEffectQueue(this);
							}
						}
					}
				}
			}
		}
	}
	
	public class EffectQueueItem {
		private Effect effect = null;
		private BatchEffect batchEffect = null;
		private float delay;
		
		public EffectQueueItem(Effect effect, float delay) {
			this.effect = effect;
			this.delay = delay;
		}
		
		public EffectQueueItem(BatchEffect batchEffect, float delay) {
			this.batchEffect = batchEffect;
			this.delay = delay;
		}
		
		public Effect getEffect() {
			return this.effect;
		}
		
		public BatchEffect getBatchEffect() {
			return this.batchEffect;
		}
		
		public float getDelay() {
			return this.delay;
		}
	}
	
	public void setEffectManager(EffectManager effectManager) {
		this.effectManager = effectManager;
	}
}
