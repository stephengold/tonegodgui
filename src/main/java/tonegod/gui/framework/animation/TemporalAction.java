package tonegod.gui.framework.animation;

import tonegod.gui.framework.core.Transformable;

/**
 * Based on LibGdx TemporalAction.  This has been altered to work with QuadData representing
 * a single quad from a mesh of x number of quads.
 * @author t0neg0d
 */
public abstract class TemporalAction implements Cloneable {
	private float duration, time = 0;
	private Interpolation interpolation;
	private boolean reverse, complete;
//	protected AnimElement batch;
	protected Transformable quad;
	protected boolean forceJmeTransform = false;
	protected boolean autoRestart = false;
	private int runCount = 0;
	
	public TemporalAction () {
	}

	public TemporalAction (float duration) {
		this.duration = duration;
	}

	public TemporalAction (float duration, Interpolation interpolation) {
		this.duration = duration;
		this.interpolation = interpolation;
	}
	
	public void setTransformable(Transformable quad) { this.quad = quad; }
	public Transformable getTransformable() { return this.quad; }
	
	public boolean act (float delta) {
		if (complete) return true;
		if (time == 0) begin();
		time += delta;
		complete = time >= duration;
		float percent;
		if (complete)
			percent = 1;
		else {
			percent = time / duration;
			if (interpolation != null) percent = interpolation.apply(percent);
		}
		update(reverse ? 1 - percent : percent);
		if (complete) end();
		return complete;
	}

	public void setAutoRestart(boolean autoRestart) {
		this.autoRestart = autoRestart;
	}
	
	public boolean getAutoRestart() {
		return this.autoRestart;
	}
	
	public int getRunCount() { return this.runCount; }
	
	protected void begin () {  }
	protected void end () {  }
	abstract protected void update (float percent);
	
	public void finish () { time = duration; }
	
	public void restart () {
		if (!reverse) runCount++;
		time = 0;
		complete = false;
	}
	
	public void reset () {
		reverse = false;
		runCount = 0;
	//	interpolation = null;
	}
	
	protected void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	/** 
         * Read the transition time. 
         * @return transition time so far
         */
	public float getTime () { return time; }

	/** 
         * Alter the transition time.
         * @param time desired value
         */
	public void setTime (float time) {
		this.time = time;
	}

	public float getDuration () {
		return duration;
	}

	/** 
         * Alter the transition time. 
         * @param duration desired transition time (in seconds)
         */
	public void setDuration (float duration) {
		this.duration = duration;
	}

	public Interpolation getInterpolation () {
		return interpolation;
	}

	public void setInterpolation (Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	public boolean isReverse () {
		return reverse;
	}

	/** 
         * Alter the direction of this action's progress.
         * @param reverse true &rarr; progress will go from 100% to 0%, false &rarr; from 0% to 100%.
         */
	public void setReverse (boolean reverse) {
		this.reverse = reverse;
	}
	
	public void setForceJmeTransform(boolean forceJmeTransform) {
		this.forceJmeTransform = forceJmeTransform;
	}
}
