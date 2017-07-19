/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.core;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.core.Screen;
import tonegod.gui.framework.animation.TemporalAction;
import tonegod.gui.framework.core.util.GameTimer;

/**
 *
 * @author t0neg0d
 */
public class AnimManager extends AbstractControl {
	private Screen screen;
	private List<ActionItem> queue = new ArrayList<>();
	private List<ActionItem> remove = new ArrayList<>();
	private List<ActionItem> active = new ArrayList<>();
	
	private List<GameTimer> timers = new ArrayList<>();
	private List<GameTimer> removeTimers = new ArrayList<>();
	
	float time;
	
	public AnimManager(Screen screen) {
		this.screen = screen;
		time = this.screen.getApplication().getTimer().getTimeInSeconds();
	}
	
	public void addQueuedAction(TemporalAction action, Transformable item, float startTime) {
		ActionItem act = new ActionItem(action,item,time+startTime);
		queue.add(act);
	}
	
	public void addGameTimer(GameTimer timer) {
		timer.setIsManaged(true);
		timers.add(timer);
		timer.startGameTimer();
	}
	
	public void removeGameTimer(GameTimer timer) {
		timers.remove(timer);
		timer.setIsManaged(false);
		if (removeTimers.contains(timer))
			removeTimers.remove(timer);
	}
	
	public boolean hasGameTimer(GameTimer timer) {
		return this.timers.contains(timer);
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		time += tpf;
		for (ActionItem item : queue) {
			if (time >= item.startTime) {
				if (item.item != null) {
					item.item.addAction(item.action);
					if (item.item instanceof QuadData)
						((QuadData)item.item).show();
				}
				active.add(item);
				remove.add(item);
			}
		}
		if (!remove.isEmpty()) {
			queue.removeAll(remove);
			remove.clear();
		}
		if (!active.isEmpty()) {
			for (ActionItem item : active) {
				if (!item.item.getContainsAction(item.action)) {
					remove.add(item);
				}
			}
		}
		if (!remove.isEmpty()) {
			active.removeAll(remove);
			remove.clear();
		}
		// GameTimers
		for(GameTimer timer : timers) {
			if (timer.isActive()) {
				timer.update(tpf);
			}
			if (!timer.getAutoRestart() && timer.isComplete()) {
				removeTimers.add(timer);
				timer.setIsManaged(false);
			}
		}
		if (!removeTimers.isEmpty()) {
			timers.removeAll(removeTimers);
			removeTimers.clear();
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {  }
	
	public int getActiveTimerCount() {
		return timers.size();
	}
	
	public int getQueueCount() {
		return this.queue.size();
	}
	
	public boolean getIsQueueIdle() {
		return this.queue.isEmpty();
	}
	
	public class ActionItem {
		TemporalAction action;
		Transformable item;
		float startTime;
		
		private ActionItem(TemporalAction action, Transformable item, float startTime) {
			this.action = action;
			this.item = item;
			this.startTime = startTime;
		}
	}
}
