/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core;

import com.jme3.input.ChaseCamera;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 *
 * @author t0neg0d
 */
public class OSRBridge extends AbstractControl {
	private RenderManager rm;
	private ChaseCamera chaseCam;
	private Camera cam;
	private ViewPort vp;
	private Node root;
	private Texture2D tex;
	private float tpf = 0.01f;
	
	public OSRBridge(RenderManager rm, int width, int height, Node root) {
		this.rm = rm;
		this.root = root;

		cam = new Camera(width, height);
		
		vp = rm.createPreView("Offscreen View", cam);
		if (!Screen.isAndroid())	vp.setClearFlags(true, true, true);
		else						vp.setClearFlags(true, false, false);
		
		FrameBuffer offBuffer = new FrameBuffer(width, height, 1);
		
		tex = new Texture2D(width, height, Image.Format.RGBA8);
		tex.setMinFilter(Texture.MinFilter.BilinearNoMipMaps);
		tex.setMagFilter(Texture.MagFilter.Bilinear);

		if (!Screen.isAndroid())
			offBuffer.setDepthBuffer(Image.Format.Depth);
		
		offBuffer.setColorTexture(tex);

		vp.setOutputFrameBuffer(offBuffer);
		
		setSpatial(root);
		vp.attachScene(root);
		
		chaseCam = new ChaseCamera(cam, root) {
			@Override
			 public void setDragToRotate(boolean dragToRotate) {
				this.dragToRotate = dragToRotate;
				this.canRotate = !dragToRotate;
			}
		};
		chaseCam.setDefaultDistance(5f);
		chaseCam.setMaxDistance(340f);
		chaseCam.setDefaultHorizontalRotation(90*FastMath.DEG_TO_RAD);
		chaseCam.setDefaultVerticalRotation(0f);
		cam.setFrustumFar(36000f);
		float aspect = cam.getWidth() / (float)cam.getHeight();
		cam.setFrustumPerspective( 45f, aspect, 0.1f, cam.getFrustumFar() );
		chaseCam.setUpVector(Vector3f.UNIT_Y);
	}
	
	public Texture2D getTexture() {
		return this.tex;
	}
	
	public ViewPort getViewPort() {
		return this.vp;
	}
	
	public Camera getCamera() {
		return this.cam;
	}
	
	public ChaseCamera getChaseCamera() {
		return this.chaseCam;
	}
	
	public RenderManager getRenderManager() {
		return this.rm;
	}
	
	public Node getRootNode() {
		return this.root;
	}
	
	public void setViewPortColor(ColorRGBA color) {
		vp.setBackgroundColor(color);
	}
	
	public float getCurrentTPF() {
		return tpf;
	}
	
	@Override
	public final void setSpatial(Spatial spatial) {
		this.spatial = spatial;
		if (spatial != null)
			this.setEnabled(true);
		else
			this.setEnabled(false);
	}
	
	@Override
	protected void controlUpdate(float tpf) {
		if (enabled) {
			root.updateLogicalState(tpf);
			root.updateGeometricState();
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		
	}
	
}
