/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.framework.core;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapCharacter;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import static com.jme3.font.BitmapFont.Align.Center;
import static com.jme3.font.BitmapFont.Align.Left;
import static com.jme3.font.BitmapFont.Align.Right;
import com.jme3.font.BitmapFont.VAlign;
import com.jme3.font.LineWrapMode;
import static com.jme3.font.LineWrapMode.Character;
import static com.jme3.font.LineWrapMode.Clip;
import static com.jme3.font.LineWrapMode.NoWrap;
import static com.jme3.font.LineWrapMode.Word;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.texture.Texture;
import java.util.LinkedList;

/**
 *
 * @author t0neg0d
 */
public class AnimText extends AnimElement {
	public static enum TagType {
		Paragraph,
		NewLine,
		Underline,
		StrikeThrough,
		Italic,
		Bold
	}
	private LinkedList<Tag> tags = new LinkedList<>();
	protected BitmapFont font;
	private String text;
	private int imgHeight;
	private boolean fadeIn = false;
	private boolean fadeOut = false;
	private char[] characters;
	private Character c;
	private QuadData[] letters;
	private QuadData[] lines;
	private QuadData qd;
	private QuadData line;
	private TextureRegion tr;
	private Vector2f bounds = new Vector2f();
	
	private LineWrapMode textWrap = LineWrapMode.NoWrap;
	private Align textAlign = Align.Left;
	private VAlign textVAlign = VAlign.Top;
	private Align currentAlign = textAlign;
	private float size = 30;
	private ColorRGBA fontColor = new ColorRGBA(1f,1f,1f,1f);
	
	private int lineCount = 0;
	private float	fadeDuration = 0,
					fadeCounter = 0,
					alpha = 1,
					lineWidth = 0,
					lineHeight = 0;
	
	BitmapCharacter bc, bcSpc;
	int wordSIndex = 0, wordEIndex = 0;
	int lineSIndex = 0;
	int italicSIndex = 0, italicEIndex = 0;
	float x = 0, y = 0, lnWidth = 0, wordWidth = 0;
	int lIndex = 0;
	boolean placeWord = false;
	
	// Formatting
	boolean hasLines = false;
	AnimElement lineDisplay;
	boolean ul = false;
	float lineOffset = 3;
	float lineSize = 2;
	float skewSize = 3;
	
	// Temp vars
	private Vector2f align = new Vector2f();
	private Vector2f pos = new Vector2f();
	
	public AnimText(AssetManager assetManager, BitmapFont font) {
		this(assetManager, font, (Texture)font.getPage(0).getParam("ColorMap").getValue());
	}
	
	public AnimText(AssetManager assetManager, BitmapFont font, Texture bfTexture) {
		super(assetManager);
		this.font = font;
		this.text = "";
		this.setScale(1,1);
		this.setPosition(0,0);
		this.setOrigin(0,0);
	//	nl = System.getProperty("line.separator").toCharArray();
		this.size = 30/font.getPreferredSize();
		
		Texture tex = assetManager.loadTexture("tonegod/gui/style/def/TextField/text_field_x.png");
		
		lineDisplay = new AnimElement(assetManager) {
			@Override
			public void animElementUpdate(float tpf) {  }
		};
		lineDisplay.setTexture(tex);
		lineDisplay.addTextureRegion("trLine", 4, 4, 6, 6);
		
		setTexture(bfTexture);
		imgHeight = bfTexture.getImage().getHeight();
		
		setText(text);
		initialize();
		
		setBounds(getWidth(),getHeight());
	//	lineDisplay.setDimensions(bounds);
	}
	
	public AnimElement getLineDisplay() {
		return this.lineDisplay;
	}
	
	public final void setText(String text) {
		lineCount = 0;
		hasLines = false;
		lineDisplay.quads.clear();
		lineDisplay.detachAllChildren();
		
		this.uvs.clear();
		this.quads.clear();
		bcSpc = font.getCharSet().getCharacter('i');
		lIndex = 0;
		lineWidth = 0;
		int textIndex = 0;
		text = stripTags(text);
		
		lineHeight = font.getCharSet().getLineHeight()*size;
		
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			bc = font.getCharSet().getCharacter(c.charValue());
			
			if (bc != null) {
				if (c != ' ') {
					tr = addTextureRegion(String.valueOf(c.hashCode()), bc.getX(), imgHeight-bc.getY()-bc.getHeight(), bc.getWidth(), bc.getHeight());
					tr.flip(false, true);
					align.set(bc.getWidth()*size/2, bc.getHeight()*size/2);
					pos.set(lineWidth,font.getCharSet().getBase()-bc.getHeight()-bc.getYOffset()*size);
					qd = addQuad(String.valueOf(lIndex), String.valueOf(c.hashCode()), pos, align);
					qd.setDimensions(qd.getTextureRegion().getRegionWidth()*size,qd.getTextureRegion().getRegionHeight()*size);
					qd.setColor(fontColor);
					qd.userIndex = lIndex;//textIndex;
					if (hasLines) {
						line = lineDisplay.addQuad(String.valueOf(lIndex), "trLine", pos, align);
						line.setDimensions(bc.getXAdvance()*size,(lineSize*size < 1) ? 1 : lineSize*size);
						line.setColor(fontColor);
					}
					lineWidth += bc.getXAdvance()*size;
					lIndex++;
				} else {
					lineWidth += bcSpc.getXAdvance()*size;
				}
			}
			textIndex++;
		}
		
		setOrigin(getWidth()/2,getHeight()/2);
		mesh.initialize();
		mesh.update(0);
		
		mesh.updateBound();
		
		characters = text.toCharArray();
		
		letters = quads.values().toArray(new QuadData[0]);
		
		this.text = text;
		
		if (hasLines) {
			lineDisplay.initialize();
			if (getMaterial().getParam("Clipping") != null) {
				lineDisplay.getMaterial().setVector4("Clipping", (Vector4f)getMaterial().getParam("Clipping").getValue());
				lineDisplay.getMaterial().setBoolean("UseClipping", (Boolean)getMaterial().getParam("UseClipping").getValue());
			}
			lineDisplay.update(0);
			if (lineDisplay.getParent() == null)
				attachChild(lineDisplay);
		} else {
			lineDisplay.removeFromParent();
		}
		
		lines = lineDisplay.quads.values().toArray(new QuadData[0]);
	}
	
	private String stripTags(String text) {
		tags.clear();
		int sIndex = text.indexOf('<');
		while (sIndex > -1) {
			int eIndex = text.indexOf('>');
			if (eIndex > -1) {
				String tagName = text.substring(sIndex, eIndex+1);
				TagType type = getTagType(tagName);
				Tag tag = new Tag(sIndex,type);
				if (tagName.contains("</"))
					tag.close = true;
				switch(type) {
					case Paragraph:
						if (tagName.contains("align")) {
							tag.align = Align.valueOf(tagName.substring(tagName.indexOf('=')+1,tagName.indexOf('>')));
						}
						break;
					case Underline:
						hasLines = true;
						break;
				}
				
				tags.add(tag);
				text = text.replaceFirst(tagName, "");
			} else {
				break;
			}
			sIndex = text.indexOf('<');
		}
		return text;
	}
	
	private TagType getTagType(String tagName) {
		if (tagName.equals("<u>") || tagName.equals("</u>"))
			return TagType.Underline;
		else if (tagName.equals("<i>") || tagName.equals("</i>"))
			return TagType.Italic;
		else if (tagName.equals("<strike>") || tagName.equals("</strike>"))
			return TagType.StrikeThrough;
		else if (tagName.contains("<b>") || tagName.contains("</b>"))
			return TagType.Bold;
		else if (tagName.contains("</br>") || tagName.contains("<br>") || tagName.contains("<br/>"))
			return TagType.NewLine;
		else if (tagName.contains("<p") || tagName.contains("</p>"))
			return TagType.Paragraph;
		else return null;
	}
	
	public void setMaxAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public void setAlpha(float alpha) {
		for (QuadData quad : quads.values())
			quad.setColorA(alpha);
	}
	
	public void wrapTextNoWrap() {
		int i = 0;
		x = 0; y = -(font.getCharSet().getBase()/2)*size;
		lnWidth = 0;
		lIndex = 0;
		bcSpc = font.getCharSet().getCharacter('i');
		for (char c : characters) {
			bc = font.getCharSet().getCharacter(c);
			
			if (bc != null) {
				for (Tag t : tags) {
					if (t.index == i) {
						switch (t.type) {
							case Italic:
								if (t.close) {
									italicEIndex = lIndex;
									formatItalic();
								} else {
									italicSIndex = lIndex;
								}
								break;
							case Underline:
								if (t.close) {
									ul = false;
								} else {
									ul = true;
								}
								break;
						}
					}
				}
				if (c != ' ') {
					QuadData quad = letters[lIndex];
					float offset = font.getCharSet().getBase()*size;
					offset -= (bc.getHeight()*size);
					offset -= (bc.getYOffset()*size);
					quad.setPosition(x,offset+y);
					quad.setDimensions(quad.getTextureRegion().getRegionWidth()*size,quad.getTextureRegion().getRegionHeight()*size);
					
					if (hasLines) {
						line = lines[lIndex];
						line.setPosition(x,y-(lineOffset*size));
						line.setDimensions(quad.getWidth(),(lineSize*size < 1) ? 1 : lineSize*size);
						if (!ul) line.setWidth(0);
						else {
							if (lIndex-1 > -1) {
								QuadData lastLine = lines[lIndex-1];
								if (lastLine.getWidth() != 0) {
									if (line.getPositionX() > lastLine.getPositionX()) {
										lastLine.setWidth(line.getPositionX()-lastLine.getPositionX());
									}
								}
							}
						}
					}
					
					
					x += bc.getXAdvance()*size;
					lnWidth += bc.getXAdvance()*size;
					lIndex++;
				} else {
					x += bcSpc.getXAdvance()*size;
					lnWidth += bcSpc.getXAdvance()*size;
					if (hasLines && ul) line.setWidth(line.getWidth()+(bcSpc.getXAdvance()*size));
				}
			}
			i++;
		}
		
		lineWidth = lnWidth;
		lineHeight = font.getCharSet().getLineHeight()*size;
		
		updateLineForAlignment(0,lIndex,lnWidth);
		
	//	lineCount = 1;
		updateForAlign();
		setOrigin(getWidth()/2,getHeight()/2);
		mesh.update(0);
		mesh.updateBound();
		lineDisplay.update(0);
		alignToBoundsV();
	}
	public void wrapTextToCharacter(float width) {
		float scaled = width*getScale().x;
		float diff = scaled-width;
		width -= diff;
		int i = 0, lineIndex = 0;
		float x = 0, y = -(font.getCharSet().getBase()/2);
		BitmapCharacter bc, bcSpc = font.getCharSet().getCharacter('i');
		float lnWidth = 0;
		boolean newLine = false;
		lineCount = 1;
		int lIndex = 0;
		int newLineCount = 0;
		
		for (char c : characters) {
			c = text.charAt(i);
			bc = font.getCharSet().getCharacter(c);
			
			if (bc != null) {
				if (c == ' ' && ! newLine) {
					lnWidth += bc.getXAdvance();
					x += bcSpc.getXAdvance();
				}
				else {
					if (x+bc.getXAdvance() > width || newLine) {
						if (i > 0) {
							int tIndex = i;
						//	tIndex;
							while (characters[tIndex] == ' ') {
								x -= bcSpc.getXAdvance();
								lnWidth -= bcSpc.getXAdvance();
								tIndex--;
							}
						}
						updateLineForAlignment(lineIndex, lIndex, lnWidth);
						lnWidth = 0;
						x = 0;
						if (newLine)	y -= font.getCharSet().getBase()*newLineCount;
						else			y -= font.getCharSet().getBase();
						lineIndex = lIndex;
						newLine = false;
						newLineCount = 0;
					}
					qd = letters[lIndex];
					qd.setPositionX(x);
					qd.setPositionY(font.getCharSet().getBase()-bc.getHeight()-bc.getYOffset()+y);
					lnWidth += bc.getXAdvance();
					x += bc.getXAdvance();
					lIndex++;
				}
			} else {
				newLine = true;
				newLineCount++;
			}
			i++;
		}
		
		updateLineForAlignment(lineIndex, lIndex, lnWidth);
		
		updateForAlign();
		setOrigin(getWidth()/2,getHeight()/2);
		mesh.update(0);
		mesh.updateBound();
		alignToBoundsV();
	}
	public void wrapTextToWord(float width) {
		bcSpc = font.getCharSet().getCharacter('i');
		wordSIndex = 0; wordEIndex = 0;
		lineSIndex = 0;
		x = 0; y = -(font.getCharSet().getBase()/2)*size;
		lnWidth = 0; wordWidth = 0;
		lIndex = 0;
		placeWord = false;
		int i = 0;
		
		for (char c : characters) {
			bc = font.getCharSet().getCharacter(c);
			
			if (bc != null) {
				wordEIndex = lIndex;
				for (Tag t : tags) {
					if (t.index == i) {
						switch (t.type) {
							case NewLine:
								formatNewLine(width);
								break;
							case Paragraph:
								formatParagraph(t,width);
								break;
							case Italic:
								if (t.close) {
									italicEIndex = lIndex;
									formatItalic();
								} else {
									italicSIndex = lIndex;
								}
								break;
							case Underline:
								if (t.close) {
									ul = false;
								} else {
									ul = true;
								}
								break;
						}
					}
				}
				if (c == ' ') {
					lnWidth += bcSpc.getXAdvance()*size;
					wordSIndex = lIndex;
					if (hasLines && ul) line.setWidth(line.getWidth()+(bcSpc.getXAdvance()+4*size));
				} else {
					QuadData quad = letters[lIndex];
					float offset = font.getCharSet().getBase()*size;
					offset -= (bc.getHeight()*size);
					offset -= (bc.getYOffset()*size);
					quad.setPosition(x,offset+y);
					quad.setDimensions(quad.getTextureRegion().getRegionWidth()*size,quad.getTextureRegion().getRegionHeight()*size);
					quad.setOrigin(quad.getWidth()/2,quad.getHeight()/2);
					
					if (hasLines) {
						line = lines[lIndex];
						line.setPosition(x,y-(lineOffset*size));
						line.setDimensions(quad.getWidth(),(lineSize*size < 1) ? 1 : lineSize*size);
						if (!ul) line.setWidth(0);
					}
					
					x += bc.getXAdvance()*size;
					wordWidth += bc.getXAdvance()*size;
					
					if (i+1 < text.length()) {
						char ch = text.charAt(i+1);
						if (ch == ' ')
							placeWord = true;
					} else if (i+1 >= text.length()) {
						placeWord = true;
					}
					if (placeWord) {
						placeWord(width);
					}
					
					if (hasLines) {
						if (lIndex-1 > -1) {
							QuadData lastLine = lines[lIndex-1];
							if (lastLine.getWidth() != 0) {
								if (line.getPositionX() > lastLine.getPositionX()) {
									lastLine.setWidth(line.getPositionX()-lastLine.getPositionX());
								}
							}
						}
					}
					
					lIndex++;
				}
			}
			i++;
		}
		wordEIndex = letters.length-1;
		for (Tag t : tags) {
			if (t.index == i) {
				switch (t.type) {
					case NewLine:
						formatNewLine(width);
						break;
					case Paragraph:
						formatParagraph(t,width);
						break;
					case Italic:
						if (t.close) {
							italicEIndex = lIndex;
							formatItalic();
						} else {
							italicSIndex = lIndex;
						}
						break;
				}
			}
		}
		setOrigin(getWidth()/2,getHeight()/2);
		alignToBoundsV();
		mesh.update(0);
		mesh.updateBound();
		lineDisplay.update(0);
	}
	
	private void placeWord(float width) {
		if (lnWidth+wordWidth < width) {
			for (int w = wordSIndex; w <= wordEIndex; w++) {
				QuadData quad = letters[w];
				quad.setPositionX(quad.getPositionX()+lnWidth);
				if (hasLines)
					lines[w].setPositionX(quad.getPositionX());
			}
			lnWidth += wordWidth;
		} else {
			updateLineForAlignment(currentAlign, lineSIndex, wordSIndex, width, lnWidth);
			
			y -= font.getCharSet().getBase()*size;
			
			for (int w = wordSIndex; w <= wordEIndex; w++) {
				QuadData quad = letters[w];
				quad.setPositionY(
					quad.getPositionY()-(font.getCharSet().getBase()*size)
				);
				if (hasLines)
					lines[w].setPositionY(y-(lineOffset*size));
			}
			lineSIndex = wordSIndex;
			
			lnWidth = wordWidth;
			lineCount++;
		}
		x = 0;
		wordWidth = 0;
		wordSIndex = lIndex;
		placeWord = false;
	}
	
	private void formatNewLine(float width) {
		placeWord(width);
		updateLineForAlignment(currentAlign, lineSIndex, wordEIndex, width, lnWidth);
		lineSIndex = wordEIndex;
		lnWidth = wordWidth;
		x = 0;
		wordWidth = 0;
		y -= font.getCharSet().getBase()*size;
	}
	private void formatParagraph(Tag t, float width) {
		placeWord(width);
		updateLineForAlignment(currentAlign, lineSIndex, wordEIndex, width, lnWidth);
		lineSIndex = wordEIndex;
		x = 0;
		lnWidth = wordWidth;
		wordWidth = 0;
		y -= font.getCharSet().getBase()*size*2;
		if (!t.close) {
			currentAlign = t.align;
		} else {
			currentAlign = textAlign;
		}
		lineCount++;
	}
	private void formatItalic() {
		for (int xi = italicSIndex; xi < italicEIndex; xi++) {
			letters[xi].setSkew(skewSize*size,0);
		}
	}
	
	private void updateLineForAlignment(int head, int tail, float width) {
		switch (textAlign) {
			case Right:
				for (int xi = head; xi < tail; xi++) {
					letters[xi].setPositionX(letters[xi].getPositionX()-width);
					if (hasLines) lines[xi].setPositionX(lines[xi].getPositionX()-width);
				}
				break;
			case Center:
				for (int xi = head; xi < tail; xi++) {
					letters[xi].setPositionX(letters[xi].getPositionX()-(width/2));
					if (hasLines) lines[xi].setPositionX(lines[xi].getPositionX()-(width/2));
				}
				break;
		}
	}
	private void updateLineForAlignment(Align textAlign, int head, int tail, float width, float lnWidth) {
		if (tail == letters.length-1) tail = letters.length;
		switch (textAlign) {
			case Right:
				for (int xi = head; xi < tail; xi++) {
					letters[xi].setPositionX(letters[xi].getPositionX()+(width-lnWidth));
					if (hasLines) lines[xi].setPositionX(lines[xi].getPositionX()+(width-lnWidth));
				}
				break;
			case Center:
				for (int xi = head; xi < tail; xi++) {
					letters[xi].setPositionX(letters[xi].getPositionX()+((width/2)-(lnWidth/2)));
					if (hasLines) lines[xi].setPositionX(lines[xi].getPositionX()+((width/2)-(lnWidth/2)));
				}
				break;
		}
	}
	
	@Override
	public void animElementUpdate(float tpf) {
		if (fadeIn) {
			fadeCounter += tpf;
			float percent = fadeCounter/fadeDuration;
			if (percent >= 1) {
				percent = 1;
				for (QuadData quad : quads.values())
					quad.setColorA(percent*alpha);
				fadeCounter = 0;
				fadeIn = false;
			} else {
				for (QuadData quad : quads.values()) {
					quad.setColorA(percent*alpha);
					if (percent*alpha <= 0f)
						quad.setColorA(0.01f);
				}
			}
		} else if (fadeOut) {
			fadeCounter += tpf;
			float percent = 1-(fadeCounter/fadeDuration);
			if (percent <= 0) {
				percent = 0.01f;
				for (QuadData quad : quads.values())
					quad.setColorA(percent*alpha);
				fadeCounter = 0;
				fadeOut = false;
			} else {
				for (QuadData quad : quads.values())
					quad.setColorA(percent*alpha);
			}
		}
	}
	
	public void setFontSize(float size) {
		this.size = size/font.getPreferredSize();
	//	float tempScale = size/font.getPreferredSize();
	//	setScale(
	//		tempScale,
	//		tempScale
	//	);
	}
	
	public float getLineWidth() {
		return lineWidth;
	}
	
	public float getLineHeight() {
		return lineHeight;
	}
	
	public float getTotalWidth() {
		return bounds.x;
	}
	
	public float getTotalHeight() {
		return lineHeight*(lineCount+1);
	}
	
	public void fadeTextIn(float duration) {
		fadeIn = true;
		fadeDuration = duration;
	}
	
	public void fadeTextOut(float duration) {
		fadeOut = true;
		fadeDuration = duration;
	}
	
	public void resetFade(float finalAlpha) {
		fadeIn = false;
		fadeOut = false;
		for (QuadData quad : quads.values())
			quad.setColorA(finalAlpha);
	}
	
	public boolean isAnimating() {
		return fadeIn || fadeOut;
	}
	
	public void setFontColor(ColorRGBA color) {
		for (QuadData quad : quads.values()) {
			quad.setColor(color);
		}
		fontColor.set(color);
		alpha = color.a;
	}
	
	public QuadData getQuadDataAt(int index) {
		return (QuadData)quads.values().toArray()[index];
	}
	
	public int length() {
		return text.length();
	}
	
	public String getText() {
		return this.text;
	}
	
	public void setBounds(Vector2f dimensions) {
		this.bounds.set(dimensions);
	}
	
	public final void setBounds(float x, float y) {
		this.bounds.set(x,y);
	}
	
	public Vector2f getBounds() {
		return this.bounds;
	}
	
	public float getBoundsX() {
		return this.bounds.x;
	}
	
	public float getBoundsY() {
		return this.bounds.y;
	}
	
	private void alignToBoundsV() {
		float height = bounds.y;
		float innerHeight = ((BoundingBox)this.mesh.getBound()).getYExtent()*2;
		float lnHeight = font.getCharSet().getCharacter('X').getYOffset()*size;//*getScale().y;
		innerHeight -= lnHeight;
		
		switch (textVAlign) {
			case Top:
				setPositionY(height-lnHeight);
				if (hasLines) lineDisplay.setOriginY(0);
				break;
			case Center:
				switch (textWrap) {
					case NoWrap:
					case Clip:
						setPositionY((height/2)+(lnHeight/2));
						if (hasLines) lineDisplay.setOriginY(0);
						break;
					default:
						setPositionY((height/2)+(innerHeight/2));
						if (hasLines) lineDisplay.setOriginY(0);
						break;
				}
				break;
			case Bottom:
				switch (textWrap) {
					case NoWrap:
					case Clip:
						setPositionY((lnHeight*2));
						if (hasLines) lineDisplay.setOriginY(0);
						break;
					default:
						setPositionY(innerHeight+lnHeight);
						if (hasLines) lineDisplay.setOriginY(0);
						break;
				}
				break;
		}
		mesh.update(0);
		mesh.updateBound();
		lineDisplay.update(0);
	}
	
	public void setTextWrap(LineWrapMode textWrap) {
		this.textWrap = textWrap;
	}
	
	public LineWrapMode getTextWrap() { return this.textWrap; }
	
	private void updateForAlign() {
		switch (textAlign) {
			case Left:
				setPositionX(0);
				break;
			case Center:
				setPositionX(bounds.x/2);
				break;
			case Right:
				setPositionX(bounds.x);
				break;
		}
	}
	
	public void setTextAlign(Align textAlign) {
		this.textAlign = textAlign;
		currentAlign = textAlign;
		switch (textWrap) {
			case Character:
				wrapTextToCharacter(bounds.x);
				break;
			case Word:
				wrapTextToWord(bounds.x);
				break;
			case NoWrap:
				wrapTextNoWrap();
				break;
			case Clip:
				wrapTextNoWrap();
				break;
		}
	}
	
	public Align getTextAlign() { return this.textAlign; }
	
	public void setTextVAlign(VAlign textVAlign) {
		this.textVAlign = textVAlign;
		alignToBoundsV();
	}
	
	public VAlign getTextVAlign() { return this.textVAlign; }
	
	public void setFont(BitmapFont font) {
		this.font = font;
		
		Texture bfTexture = (Texture)font.getPage(0).getParam("ColorMap").getValue();
		setTexture(bfTexture);
		imgHeight = bfTexture.getImage().getHeight();
		
		setText(".");
		initialize();
	}
	
	public BitmapFont getFont() { return this.font; }
	
	public void setSubStringColor(String subString, ColorRGBA color, boolean allInstances, int... whichInstances) {
		String temp = text;
		int lastIndex = 0;
		int sIndex = 1;
		int count = 1;
		while (sIndex != -1) {
			sIndex = temp.indexOf(subString);
			if (sIndex != -1) {
				int spcCount = 0;
				for (int i = 0; i < subString.length(); i++) {
					if (subString.charAt(i) == ' ')
						spcCount++;
				}
				int eIndex = sIndex+subString.length()-spcCount;
				boolean valid = false;
				if (!allInstances) {
					for (int c : whichInstances) {
						if (c == count) {
							valid = true;
							break;
						}
					}
					if (valid) {
						spcCount = 0;
						for (int i = 0; i < sIndex+lastIndex; i++) {
							if (text.charAt(i) == ' ')
								spcCount++;
						}
						for (int i = sIndex+lastIndex-spcCount; i < eIndex+lastIndex-spcCount; i++) {
							letters[i].setColor(color);
						}
					}
					count++;
				} else {
					spcCount = 0;
					for (int i = 0; i < sIndex+lastIndex; i++) {
						if (text.charAt(i) == ' ')
							spcCount++;
					}
					for (int i = sIndex+lastIndex-spcCount; i < eIndex+lastIndex-spcCount; i++) {
						letters[i].setColor(color);
					}
				}
				lastIndex += eIndex;
				temp = temp.substring(eIndex, temp.length());
			}
		}
	}
	
	public class Tag {
		public TagType type;
		public int index = 0;
		public Align align = null;
		public boolean close = false;
		
		public Tag(int index, TagType type) {
			this.index = index;
			this.type = type;
		}
	}
}
