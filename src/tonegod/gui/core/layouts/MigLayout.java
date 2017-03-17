package tonegod.gui.core.layouts;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.StringTokenizer;
import tonegod.gui.core.Element;
import tonegod.gui.core.Element.Borders;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.layouts.LayoutHint.Align;
import tonegod.gui.core.layouts.LayoutHint.SizeUnit;
import tonegod.gui.core.layouts.LayoutHint.VAlign;

/**
 *
 * @author t0neg0d
 */
public class MigLayout extends AbstractLayout {
	private String cols, rows;
	private Cell[][] oldcells;
	private Cell[][] cells;
	private StringTokenizer[] st = new StringTokenizer[2];
	private float[] widths, heights;
	private SizeUnit hUnit, wUnit;
	
	/**
	 * Creates a new instance of MigLayout
	 * 
	 * @param screen manager for this layout
	 * @param cols number of columns of cells, encoded in a String
	 * @param rows number of rows of cells, encoded in a String
	 * @param constraints constraints of this layout
	 */
	public MigLayout(ElementManager screen, String cols, String rows, String... constraints) {
		super(screen, constraints);
		this.cols = cols;
		this.rows = rows;
	}
	
	private int parseCount(int index, String id, String str) {
		String token;
		if (str.indexOf("]") != -1) {
			token = "]";
			if (str.indexOf(id) != -1)
				str = str.substring(str.indexOf(" ")+1);
		} else if (str.indexOf(",") != -1) {
			token = ",";
			if (str.indexOf(id) != -1)
				str = str.substring(str.indexOf(" ")+1);
		} else {
			token = " ";
			if (str.indexOf(id) != -1)
				str = str.substring(str.indexOf(" ")+1);
		}
		st[index] = new StringTokenizer(str, token);
		return st[index].countTokens();
	}
	
	private void layoutCells() {
		getCellWidths();
		getCellHeights();
		
		float x = 0, y = 0;
		for (int r = 0; r < cells.length; r++) {
			int index = 0;
			for (int c = 0; c < cells[0].length; c++) {
				Cell cell = new Cell(r,c);
				cell.setSize(
					widths[index],
					heights[r]
				);
				if (oldcells != null) {
					cell.setInitSize(oldcells[r][c].initsize.x,oldcells[r][c].initsize.y);
				} else
					cell.setInitSize(cell.size.x,cell.size.y);
				cell.setPos(x+margins.x, y+margins.z);
				cell.setSizeUnits(wUnit, hUnit);
				x += widths[index];
				cells[r][c] = cell;
				index++;
			}
			y += heights[r];
			x = 0;
		}
	}
	
	private void getCellWidths() {
		float w;
		widths = new float[cells[0].length];
		
		for (int c = 0; c < cells[0].length; c++) {
			wUnit = SizeUnit.absolute;
			String cStr = st[1].nextToken();
			if (cStr.indexOf("[") != -1)
				cStr = cStr.substring(cStr.indexOf("[")+1);
			if (cStr.equals("")) {
				w = 0;
				wUnit = SizeUnit.percent;
			} else {
				if (cStr.indexOf("%") != -1) {
					wUnit = SizeUnit.percent;
					cStr = cStr.substring(0,cStr.length()-1);
				}
				w = Float.parseFloat(cStr);
				if (w > 1 && wUnit == SizeUnit.percent)
					w /= 100;
				else if (w < 1)
					wUnit = SizeUnit.percent;
				if (wUnit == SizeUnit.percent)
					w *= owner.getWidth()-(margins.x+margins.y);
				else
					w -= (margins.x*2);
			}
			widths[c] = w;
		}
		
		int cZeroCount = 0;
		float remWidth = owner.getWidth()-(margins.x+margins.y);
		for (float nW : widths) {
			if (nW != 0.0f)
				remWidth -= nW;
			else
				cZeroCount++;
		}
		if (cZeroCount > 0) {
			float sizeW = remWidth/cZeroCount;
			int index = 0;
			for (float nW : widths) {
				if (nW == 0.0f)
					widths[index] = sizeW;
				index++;
			}
		}
	}
	
	private void getCellHeights() {
		float h;
		heights = new float[cells.length];
		
		for (int r = 0; r < cells.length; r++) {
			hUnit = SizeUnit.absolute;
			String rStr = st[0].nextToken();
			if (rStr.indexOf("[") != -1)
				rStr = rStr.substring(rStr.indexOf("[")+1);
			if (rStr.equals("")) {
				h = 0;
				hUnit = SizeUnit.percent;
			} else {
				if (rStr.indexOf("%") != -1) {
					hUnit = SizeUnit.percent;
					rStr = rStr.substring(0,rStr.length()-1);
				}
				h = Float.parseFloat(rStr);
				if (h > 1 && hUnit == SizeUnit.percent)
					h /= 100f;
				if (hUnit == SizeUnit.percent)
					h *= owner.getHeight()-(margins.z+margins.w);
				else
					h -= (margins.y*2);
			}
			heights[r] = h;
		}
		
		int rZeroCount = 0;
		float remHeight = owner.getHeight()-(margins.z+margins.w);
		for (float nH : heights) {
			if (nH != 0.0f)
				remHeight -= nH;
			else
				rZeroCount++;
		}
		if (rZeroCount > 0) {
			float sizeH = remHeight/rZeroCount;
			int index = 0;
			for (float nH : heights) {
				if (nH == 0.0f)
					heights[index] = sizeH;
				index++;
			}
		}
	}
	
	public class Cell {
		int col, row;
		Vector2f pos = new Vector2f();
		Vector2f initsize = new Vector2f(0,0);
		Vector2f size = new Vector2f();
		SizeUnit wUnit, hUnit;
		
		public Cell(int row, int col) {
		}
		
		public void setPos(float x, float y) {
			pos.set(x,y);
		}
		
		public void setSize(float w, float h) {
			size.set(w,h);
		}
		
		public void setSizeUnits(SizeUnit wUnit, SizeUnit hUnit) {
		}
		
		public void setInitSize(float w, float h) {
			initsize.set(w,h);
		}
	}
	
	@Override
	public void resize() {
		oldcells = cells;
		cells = new Cell[parseCount(0,"row",rows)][parseCount(1,"col",cols)];
		layoutCells();
		layoutChildren();
	}
	
	@Override
	public void setOwner(Element el) {
		this.owner = el;
		cells = new Cell[parseCount(0,"row",rows)][parseCount(1,"col",cols)];
		layoutCells();
	}
	
	@Override
	public void layoutChildren() {
		float x, y, w, h;
		int r = 0, c = 0, sr = 0, sc = 0;
		for (Element el : owner.getElements()) {
			convertElementProperties(el);
			for (LayoutHint hint : el.getLayoutHints().params.values()) {
				switch (hint.getType()) {
					case cell:
						r = (Integer)hint.values.get("row").getValue();
						c = (Integer)hint.values.get("col").getValue();
						break;
					case span:
						sc = (Integer)hint.values.get("x").getValue();
						sr = (Integer)hint.values.get("y").getValue();
						break;
				}
			}
			w = 0;
			LayoutHint grow = el.getLayoutHints().get("grow");
			boolean growx = (grow == null) ? true : (Boolean)grow.getValues().get("x").getValue();
			boolean growy = (grow == null) ? true : (Boolean)grow.getValues().get("y").getValue();
			
			LayoutHint fill = el.getLayoutHints().get("fill");
			boolean fillx = (fill == null) ? true : (Boolean)fill.getValues().get("x").getValue();
			boolean filly = (fill == null) ? true : (Boolean)fill.getValues().get("y").getValue();
			
			float totalW = 0, totalH = 0;
			for (int t = c; t < c+sc; t++) {
				totalW += cells[0][t].size.x;
				if (growx)
					w += cells[0][t].size.x;
				else {
					if (fillx)	w += cells[0][t].initsize.x;
					else		w = el.getOrgDimensions().x;
				}
			}
			h = 0;
			for (int t = r; t < r+sr; t++) {
				totalH += cells[t][0].size.y;
				if (growy)
					h += cells[t][0].size.y;
				else {
					if (filly)	h += cells[t][0].initsize.y;
					else		h = el.getOrgDimensions().y;
				}
			}
			float padLeft = 0, padTop = 0, padRight = 0, padBottom = 0;
			LayoutHint pad = el.getLayoutHints().get("pad");
			if (pad != null) {
				padLeft		= (Float)pad.getValues().get("left").getValue();
				padRight	= (Float)pad.getValues().get("right").getValue();
				padTop		= (Float)pad.getValues().get("top").getValue();
				padBottom	= (Float)pad.getValues().get("bottom").getValue();
			}
			LayoutHint dock = el.getLayoutHints().get("dock");
			Align align = Align.left;
			VAlign valign = VAlign.top;
			if (dock != null) {
				align = (Align)dock.getValues().get("align").getValue();
				valign = (VAlign)dock.getValues().get("valign").getValue();
			}
			el.setX(cells[r][c].pos.x);
			el.setY(cells[r][c].pos.y);
			float minx = 0, miny = 0;
			
			el.resize((int)(el.getAbsoluteX()+(w-(padLeft+padRight))),(int)(el.getAbsoluteY()+(h-(padTop+padBottom))), Borders.SE);
			
			if (!growx) {
				float newX = 0;
				switch (align) {
					case left:
						newX = el.getX()+padLeft;
						break;
					case center:
						newX = cells[r][c].pos.x+((totalW/2)-(el.getWidth()/2));
						break;
					case right:
						newX = (cells[r][c].pos.x+totalW)-(el.getWidth()+padRight);
						break;
				}
				el.setX(newX);
			} else {
				el.setX(el.getX()+padLeft);
			}
			if (!growy) {
				float newY = 0;
				switch (valign) {
					case top:
						newY = el.getY()+padTop;
						break;
					case center:
						newY = cells[r][c].pos.y+((totalH/2)-(el.getHeight()/2));
						break;
					case bottom:
						newY = (cells[r][c].pos.y+totalH)-(el.getHeight()+padBottom);
						break;
				}
				el.setY(newY);
			} else {
				el.setY(el.getY()+padTop);
			}
			
			el.setY(owner.getHeight()-el.getY()-el.getHeight());
			
			if (clip) {
				if (el.getClippingDefine(owner) == null) {
					el.addClippingLayer(owner,Vector4f.ZERO);
				}
				tempV4.set(
					cells[r][c].pos.x-owner.getClipPaddingVec().x,
					owner.getHeight()-(cells[r][c].pos.y+totalH)-owner.getClipPaddingVec().y,
					(cells[r][c].pos.x+totalW)+owner.getClipPaddingVec().z,
					owner.getHeight()-cells[r][c].pos.y+owner.getClipPaddingVec().w
				);
				el.updateClippingLayer(owner, tempV4);
			}
		}
		props = true;
	}
}
