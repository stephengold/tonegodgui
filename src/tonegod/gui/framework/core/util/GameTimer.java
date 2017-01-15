/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.core.util;

import tonegod.gui.framework.animation.Interpolation;

/**
 *
 * @author t0neg0d
 */
public abstract class GameTimer {
	private float time = 0;
	private float duration = 1;
	private boolean active = false;
	private boolean complete = false;
	private long runCount = 0;
	private boolean autoRestart = false;
	private boolean isManaged = false;
	private Interpolation interpolation = Interpolation.linear;
	
	private boolean updateDurationOnNextRestart = false;
	private float nextDuration = 1;
	/**
	 * Creates a new instance of the GameTimer class with a default duration of 1 second
	 */
	public GameTimer() {
		this(1,false);
	}
	
	/**
	 * Creates a new instance of the GameTimer class
	 * @param duration The duration the timer should run in seconds
	 */
	public GameTimer(float duration) {
		this(duration,false);
	}
	
	/**
	 * Creates a new instance of the GameTimer class
	 * @param duration The duration the timer should run in seconds
	 * @param autoStart Sets the GameTimer to active if true
	 */
	public GameTimer(float duration, boolean autoStart) {
		this.duration = duration;
		this.active = autoStart;
	}
	
	/**
	 * Sets the amount of time in seconds that should elapse before the
	 * GameTimer is complete.
	 * @param duration The duration of the timer
	 */
	public void setDuration(float duration) {
		this.duration = duration;
	}
	
	/**
	 * Sets the duration to a new value on the next call to reset.
	 * Allows for updating the duration properly from onComplete method
	 * @param duration The duration to use on the next timer run
	 */
	public void setNextDuration(float duration) {
		this.updateDurationOnNextRestart = true;
		this.nextDuration = duration;
	}
	
	/**
	 * Return the amount of time in seconds the GameTimer should run for
	 * before completing.
	 * @return targetTime
	 */
	public float getDuration() {
		return this.duration;
	}
	
	/**
	 * Resets the timer for another use.
	 * @param resetFromLastEndTime GameTimer should subtract the targetTime from the
	 * previously elapsed time of the timer.
	 */
	public void reset(boolean resetFromLastEndTime) {
		if (resetFromLastEndTime)
			time -= duration;
		else
			time = 0;
		
		if (this.updateDurationOnNextRestart) {
			this.duration = nextDuration;
			this.updateDurationOnNextRestart = false;
			nextDuration = 1;
		}
		
		active = false;
		complete = false;
	}
	
	/**
	 * Call to reset the number of times the timer has run to 0
	 */
	public void resetRunCount() {
		this.runCount = 0;
	}
	
	/**
	 * Sets the GameTimer to active
	 */
	public void startGameTimer() {
		this.active = true;
	}
	
	/**
	 * Forces a stop on the game timer.
	 */
	public void endGameTimer() {
		this.complete = true;
	}
	
	/**
	 * Returns if the GameTimer has completed running
	 * @return 
	 */
	public boolean isComplete() {
		return this.complete;
	}
	
	/**
	 * Sets the interpolation to apply to the percent complete returned by getPercentComplete()
	 * @param interpolation 
	 */
	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}
	
	/**
	 * Returns the interpolation set to be used by getPercentComplete()
	 * @return 
	 */
	public Interpolation getInterpolation() {
		return this.interpolation;
	}
	
	/**
	 * return a float between 0.0 and 1.0 representing the percent complete
	 * @return 
	 */
	public float getPercentComplete() {
		return interpolation.apply(time/duration);
	}
	
	/**
	 * Returns if the GameTimer is running
	 * @return 
	 */
	public boolean isActive() {
		return this.active;
	}
	
	/**
	 * Returns the number of times startGameTimer has been called on this timer
	 * @return 
	 */
	public long getRunCount() {
		return this.runCount;
	}
	
	/**
	 * For use with or without managed GameTimers.
	 * Enables auto restart of the timer after calling onComplete.
	 * @param autoRestart 
	 */
	public void setAutoRestart(boolean autoRestart) {
		this.autoRestart = autoRestart;
	}
	
	/**
	 * Returns if the GameTimer will automatically reset and restart after calling onComplete.
	 * @return 
	 */
	public boolean getAutoRestart() {
		return this.autoRestart;
	}
	
	/**
	 * FOR INTERNAL USE ONLY. Do not call this method directly.
	 */
	public void setIsManaged(boolean isManaged) {
		this.isManaged = isManaged;
	}
	
	public boolean getIsManaged() { return this.isManaged; }
	
	/**
	 * Should be called each game loop
	 * @param tpf 
	 */
	public void update(float tpf) {
		if (active && !complete) {
			time += tpf;
			timerUpdateHook(tpf);
			if (time >= duration) {
				complete = true;
				active = false;
				validateRestart(time);
			}
		}
	}
	
	public void timerUpdateHook(float tpf) {  }
	
	private void validateRestart(float time) {
		runCount++;
		onComplete(time);
		if (autoRestart) {
			reset(true);
			startGameTimer();
		}
	}
	
	public abstract void onComplete(float time);
}
