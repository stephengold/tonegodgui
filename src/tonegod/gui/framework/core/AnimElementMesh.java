package tonegod.gui.framework.core;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author t0neg0d
 */
public class AnimElementMesh extends Mesh {
	AnimElement batch;
	private int vCount = 0;
	private int vIndex = 0;
	public boolean init = false;
	private Vector2f dim = new Vector2f(0,0);
	private Vector2f skew = new Vector2f(0,0);
	private Vector2f tempV = new Vector2f(0,0);
	private Vector2f tempV2 = new Vector2f(0,0);
	
	public boolean buildPosition = true;
	public boolean buildTexCoords = true;
	public boolean buildColor = true;
	public boolean buildIndices = true;
	
	private FloatBuffer vb;
	private ShortBuffer ib;
	private FloatBuffer tcb;
	private FloatBuffer cb;
	
	boolean updateCol = false;
	
	public AnimElementMesh(AnimElement batch) {
		this.batch = batch;
	}
	
	public void initialize() {
		vb = BufferUtils.createFloatBuffer(batch.getQuads().size()*3*4);
		cb = BufferUtils.createFloatBuffer(batch.getQuads().size()*4*4);
		tcb = BufferUtils.createFloatBuffer(batch.getQuads().size()*2*4);
		ib = BufferUtils.createShortBuffer(batch.getQuads().size()*6);
		init = true;
	}
	
	public void update(float tpf) {
		if (init) {
			updateCol = false;
			vb.rewind();
			if (buildColor) cb.rewind();
			if (buildTexCoords) tcb.rewind();
			if (buildIndices) ib.rewind();
			updateMeshData(tpf);
		}
	}
	
	private void updateMeshData(float tpf) {
		vIndex = 0;
		vCount = 0;
		
		for (QuadData qd : batch.getQuads().values()) {
			qd.update(tpf);
			addQuad(qd);
			vIndex++;
		}
		
		setBuffers();
	}
	
	private void addQuad(QuadData qd) {
		qd.index = vIndex;
                
		dim.set(qd.getWidth(), qd.getHeight());
		skew.set(qd.getSkew());

		/** VERT 1 **/
		applyTransforms(qd, -skew.x, -skew.y);
		vb	.put(tempV.x)
			.put(tempV.y)
			.put(qd.getPositionZ());

		/** VERT 2 **/
		applyTransforms(qd, dim.x-skew.x, -skew.y);
		vb	.put(tempV.x)
			.put(tempV.y)
			.put(qd.getPositionZ());

		/** VERT 3 **/
		applyTransforms(qd, skew.x, dim.y+skew.y);
		vb	.put(tempV.x)
			.put(tempV.y)
			.put(qd.getPositionZ());

		/** VERT 4 **/
		applyTransforms(qd, dim.x+skew.x, dim.y+skew.y);
		vb	.put(tempV.x)
			.put(tempV.y)
			.put(qd.getPositionZ());

		if (buildColor) {
			for (int i = 0; i < 4; i++) {
				cb	.put(qd.getColorR())
					.put(qd.getColorG())
					.put(qd.getColorB())
					.put(qd.getColorA());
			}
		}

		if (buildTexCoords) {
			tcb	.put(qd.getTextureRegion().getU()+qd.getTCOffsetX())
				.put(qd.getTextureRegion().getV()+qd.getTCOffsetY());
			tcb	.put(qd.getTextureRegion().getU2()+qd.getTCOffsetX())
				.put(qd.getTextureRegion().getV()+qd.getTCOffsetY());
			tcb	.put(qd.getTextureRegion().getU()+qd.getTCOffsetX())
				.put(qd.getTextureRegion().getV2()+qd.getTCOffsetY());
			tcb	.put(qd.getTextureRegion().getU2()+qd.getTCOffsetX())
				.put(qd.getTextureRegion().getV2()+qd.getTCOffsetY());
		}

		if (buildIndices) {
			ib.put((short)(vCount+2));
			ib.put((short)(vCount));
			ib.put((short)(vCount+1));
			ib.put((short)(vCount+1));
			ib.put((short)(vCount+3));
			ib.put((short)(vCount+2));
		}

		vCount += 4;
		/*
		 * // Test for Samsung devices... slower than above
		qd.index = vIndex;
		
		dim.set(qd.getWidth(), qd.getHeight());
		skew.set(qd.getSkew());
		
		int index = qd.index * 12;
		
		applyTransforms(qd, -skew.x, -skew.y);
		vb.put(index,	tempV.x);
		vb.put(index+1,	tempV.y);
		vb.put(index+2,	qd.getPositionZ());

		applyTransforms(qd, dim.x-skew.x, -skew.y);
		vb.put(index+3,	tempV.x);
		vb.put(index+4,	tempV.y);
		vb.put(index+5,	qd.getPositionZ());

		applyTransforms(qd, skew.x, dim.y+skew.y);
		vb.put(index+6,	tempV.x);
		vb.put(index+7,	tempV.y);
		vb.put(index+8,	qd.getPositionZ());

		applyTransforms(qd, dim.x+skew.x, dim.y+skew.y);
		vb.put(index+9,	tempV.x);
		vb.put(index+10,tempV.y);
		vb.put(index+11,qd.getPositionZ());
		
		if (buildColor) {
			index = qd.index*16;
			int indexX = 0;
			for (int i = 0; i < 4; i++) {
				cb.put(index+indexX,	qd.getColorR());
				cb.put(index+indexX+1,	qd.getColorG());
				cb.put(index+indexX+2,	qd.getColorB());
				cb.put(index+indexX+3,	qd.getColorA());
				indexX += 4;
			}
		}
		
		if (buildTexCoords) {
			index = qd.index*8;
			tcb.put(index,	qd.getTextureRegion().getU()+qd.getTCOffsetX());
			tcb.put(index+1,qd.getTextureRegion().getV()+qd.getTCOffsetY());
			tcb.put(index+2,qd.getTextureRegion().getU2()+qd.getTCOffsetX());
			tcb.put(index+3,qd.getTextureRegion().getV()+qd.getTCOffsetY());
			tcb.put(index+4,qd.getTextureRegion().getU()+qd.getTCOffsetX());
			tcb.put(index+5,qd.getTextureRegion().getV2()+qd.getTCOffsetY());
			tcb.put(index+6,qd.getTextureRegion().getU2()+qd.getTCOffsetX());
			tcb.put(index+7,qd.getTextureRegion().getV2()+qd.getTCOffsetY());
		}
		
		if (buildIndices) {
			index = qd.index*6;
			ib.put(index,	(short)(vCount+2));
			ib.put(index+1,	(short)(vCount));
			ib.put(index+2,	(short)(vCount+1));
			ib.put(index+3,	(short)(vCount+1));
			ib.put(index+4,	(short)(vCount+3));
			ib.put(index+5,	(short)(vCount+2));
		}
		
		vCount += 4;
		*/
	}
	
	private void applyTransforms(QuadData qd, float x,  float y) {
		AnimElement a = batch;
		QuadData p = qd.parent;
		tempV.set(x,y);
		tempV.subtractLocal(qd.getOrigin());
		tempV.multLocal(tempV2.set(qd.getScaleX(), qd.getScaleY()));
		tempV.set(rot(tempV, qd.getRotation()));
		tempV.addLocal(qd.getOrigin());
		tempV.addLocal(qd.getPositionX(),qd.getPositionY());
		while (p != null) {
			tempV.subtractLocal(p.getOriginX(), p.getOriginY());
			tempV.multLocal(tempV2.set(p.getScaleX(), p.getScaleY()));
			tempV.set(rot(tempV, p.getRotation()));
			tempV.addLocal(p.getOriginX(), p.getOriginY());
			tempV.addLocal(p.getPositionX(),p.getPositionY());
			p = p.parent;
		}
		while (a != null) {
			tempV.subtractLocal(a.getOriginX(), a.getOriginY());
			tempV.set(rot(tempV, a.getRotation()));
			tempV.multLocal(tempV2.set(a.getScaleX(), a.getScaleY()));
			tempV.addLocal(a.getOriginX(), a.getOriginY());
			tempV.addLocal(a.getPositionX(),a.getPositionY());
			if (a.getParent() instanceof AnimElement)
				a = (AnimElement)a.getParent();
			else
				a = null;
		}
	}
	
	float cos, sin, x, y;
	public Vector2f rot(Vector2f p, float angle) {
		cos = FastMath.cos(angle*FastMath.DEG_TO_RAD);
		sin = FastMath.sin(angle*FastMath.DEG_TO_RAD);
		x = p.x*cos-p.y*sin;
		y = p.x*sin+p.y*cos;
		return p.set(x,y);
	}
	
	private void setBuffers() {
		if (buildPosition) {
			this.clearBuffer(VertexBuffer.Type.Position);
			this.setBuffer(VertexBuffer.Type.Position, 3, vb);
			buildPosition = false;
			updateCol = true;
		}
		
		if (buildColor) {
			this.clearBuffer(VertexBuffer.Type.Color);
			this.setBuffer(VertexBuffer.Type.Color, 4, cb);
			buildColor = false;
			updateCol = true;
		}
		
		if (buildTexCoords) {
			this.clearBuffer(VertexBuffer.Type.TexCoord);
			this.setBuffer(VertexBuffer.Type.TexCoord, 2, tcb);
			buildTexCoords = false;
			updateCol = true;
		}
			
		if (buildIndices) {
			this.clearBuffer(VertexBuffer.Type.Index);
			this.setBuffer(VertexBuffer.Type.Index, 3, ib);
			buildIndices = false;
			updateCol = true;
		}
		
		if (updateCol) {
			createCollisionData();
			updateBound();
		}
	}
	
	protected void deallocateBuffers() {
		BufferUtils.destroyDirectBuffer(vb);
		BufferUtils.destroyDirectBuffer(ib);
		BufferUtils.destroyDirectBuffer(tcb);
		BufferUtils.destroyDirectBuffer(cb);
	}
}
