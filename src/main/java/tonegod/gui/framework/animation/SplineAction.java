/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.animation;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author t0neg0d
 */
public class SplineAction extends TemporalAction {
	List<Vector2f> path;
	Vector2f tempV = new Vector2f(0,0);
	
	public SplineAction() {  }
	
	public void setPath(List<Vector2f> path) {
		this.path = path;
	}
	
	@Override
	protected void update(float percent) {
		tempV.set(0,0);
		P(percent,path);
		if (!forceJmeTransform) {
			quad.setPositionX(tempV.x);
			quad.setPositionY(tempV.y);
		} else {
			if (quad instanceof Node) {
				((Spatial)quad).setLocalTranslation(
					tempV.x, 
					tempV.y, 
					quad.getPositionZ());
			} else {
				quad.setPositionX(tempV.x);
				quad.setPositionY(tempV.y);
			}
		}
	}
	
	private float fact (float k) {
	    if ( k==0 || k==1 )	return 1;
	    else				return k * fact(k-1);
	}
	
	private float B (float i, float n, float t) {
	    return (float) (fact(n) / (fact(i) * fact(n-i)) * Math.pow(t, i) * Math.pow(1-t, n-i));
	}
	
	private void P (float t, List<Vector2f> path) {
		for(int i = 0; i < path.size(); i++) {
			tempV.addLocal(
				path.get(i).x * B(i, path.size()-1, t),
				path.get(i).y * B(i, path.size()-1, t)
			);
		}
	}
	
	@Override
	public SplineAction clone() {
		SplineAction sa = new SplineAction();
		sa.setPath(path);
		sa.setDuration(getDuration());
		sa.setInterpolation(getInterpolation());
		sa.setAutoRestart(getAutoRestart());
		sa.setForceJmeTransform(forceJmeTransform);
		return sa;
	}
}