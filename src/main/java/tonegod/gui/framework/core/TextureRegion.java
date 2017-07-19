package tonegod.gui.framework.core;

import com.jme3.texture.Texture;

/**
 * Borrowed from LibGdx
 * @author Nathan Sweet
 */
public class TextureRegion {
	Texture texture;
	float u, v;
	float u2, v2;
	int regionWidth, regionHeight;

	public TextureRegion () {  }

	public TextureRegion (Texture texture) {
		if (texture == null) throw new IllegalArgumentException("texture cannot be null.");
		this.texture = texture;
		setRegion(0, 0, texture.getImage().getWidth(), texture.getImage().getHeight());
	}

	public TextureRegion (Texture texture, int width, int height) {
		this.texture = texture;
		setRegion(0, 0, width, height);
	}

	public TextureRegion (Texture texture, int x, int y, int width, int height) {
		this.texture = texture;
		setRegion(x, y, width, height);
	}

	public TextureRegion (Texture texture, float u, float v, float u2, float v2) {
		this.texture = texture;
		setRegion(u, v, u2, v2);
	}

	public TextureRegion (TextureRegion region) {
		setRegion(region);
	}

	public TextureRegion (TextureRegion region, int x, int y, int width, int height) {
		setRegion(region, x, y, width, height);
	}

	public void setRegion (Texture texture) {
		this.texture = texture;
		setRegion(0, 0, texture.getImage().getWidth(), texture.getImage().getHeight());
	}

	public void setRegion (int x, int y, int width, int height) {
		float invTexWidth = 1f / texture.getImage().getWidth();
		float invTexHeight = 1f / texture.getImage().getHeight();
		setRegion(x * invTexWidth, y * invTexHeight, (x + width) * invTexWidth, (y + height) * invTexHeight);
		regionWidth = Math.abs(width);
		regionHeight = Math.abs(height);
	}

	public void setRegion (float u, float v, float u2, float v2) {
		this.u = u;
		this.v = v;
		this.u2 = u2;
		this.v2 = v2;
		regionWidth = Math.round(Math.abs(u2 - u) * texture.getImage().getWidth());
		regionHeight = Math.round(Math.abs(v2 - v) * texture.getImage().getHeight());
	}

	public void setRegion (TextureRegion region) {
		texture = region.texture;
		setRegion(region.u, region.v, region.u2, region.v2);
	}

	public void setRegion (TextureRegion region, int x, int y, int width, int height) {
		texture = region.texture;
		setRegion(region.getRegionX() + x, region.getRegionY() + y, width, height);
	}

	public Texture getTexture () {
		return texture;
	}

	public void setTexture (Texture texture) {
		this.texture = texture;
	}

	public float getU () {
		return u;
	}

	public void setU (float u) {
		this.u = u;
		regionWidth = Math.round(Math.abs(u2 - u) * texture.getImage().getWidth());
	}

	public float getV () {
		return v;
	}

	public void setV (float v) {
		this.v = v;
		regionHeight = Math.round(Math.abs(v2 - v) * texture.getImage().getHeight());
	}

	public float getU2 () {
		return u2;
	}

	public void setU2 (float u2) {
		this.u2 = u2;
		regionWidth = Math.round(Math.abs(u2 - u) * texture.getImage().getWidth());
	}

	public float getV2 () {
		return v2;
	}

	public void setV2 (float v2) {
		this.v2 = v2;
		regionHeight = Math.round(Math.abs(v2 - v) * texture.getImage().getHeight());
	}

	public int getRegionX () {
		return Math.round(u * texture.getImage().getWidth());
	}

	public void setRegionX (int x) {
		setU(x / (float)texture.getImage().getWidth());
	}

	public int getRegionY () {
		return Math.round(v * texture.getImage().getHeight());
	}

	public void setRegionY (int y) {
		setV(y / (float)texture.getImage().getHeight());
	}

	/** 
         * Read the region's width.
         * 
         * @return width
         */
	public int getRegionWidth () {
		return regionWidth;
	}

	public void setRegionWidth (int width) {
		setU2(u + width / (float)texture.getImage().getWidth());
	}

	public int getRegionHeight () {
		return regionHeight;
	}

	public void setRegionHeight (int height) {
		setV2(v + height / (float)texture.getImage().getHeight());
	}

	public void flip (boolean x, boolean y) {
		if (x) {
			float temp = u;
			u = u2;
			u2 = temp;
		}
		if (y) {
			float temp = v;
			v = v2;
			v2 = temp;
		}
	}

	public boolean isFlipX () {
		return u > u2;
	}

	public boolean isFlipY () {
		return v > v2;
	}

	public void scroll (float xAmount, float yAmount) {
		if (xAmount != 0) {
			float width = (u2 - u) * texture.getImage().getWidth();
			u = (u + xAmount) % 1;
			u2 = u + width / texture.getImage().getWidth();
		}
		if (yAmount != 0) {
			float height = (v2 - v) * texture.getImage().getHeight();
			v = (v + yAmount) % 1;
			v2 = v + height / texture.getImage().getHeight();
		}
	}

	public TextureRegion[][] split (int tileWidth, int tileHeight) {
		int x = getRegionX();
		int y = getRegionY();
		int width = regionWidth;
		int height = regionHeight;

		int rows = height / tileHeight;
		int cols = width / tileWidth;

		int startX = x;
		TextureRegion[][] tiles = new TextureRegion[rows][cols];
		for (int row = 0; row < rows; row++, y += tileHeight) {
			x = startX;
			for (int col = 0; col < cols; col++, x += tileWidth) {
				tiles[row][col] = new TextureRegion(texture, x, y, tileWidth, tileHeight);
			}
		}

		return tiles;
	}

	public static TextureRegion[][] split (Texture texture, int tileWidth, int tileHeight) {
		TextureRegion region = new TextureRegion(texture);
		return region.split(tileWidth, tileHeight);
	}
}
