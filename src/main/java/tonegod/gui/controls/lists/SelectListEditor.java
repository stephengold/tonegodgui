package tonegod.gui.controls.lists;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class SelectListEditor extends Element {
	public SelectList items;
	private ButtonAdapter editItem, removeItem, moveUp, moveDown;
	
	public SelectListEditor(ElementManager screen) {
		this(screen, UIDUtil.getUID(), Vector2f.ZERO,
			new Vector2f(100,100),
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			new Vector2f(100,100),
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg);
	}
	
	public SelectListEditor(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			new Vector2f(100,100),
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		this.setAsContainerOnly();
		
		float size = screen.getStyle("Common").getFloat("defaultControlSize");
		
		items = new SelectList(screen, UID + "items", Vector2f.ZERO, new Vector2f(dimensions.x,dimensions.y)) {
			@Override
			public void onChange() {
				
			}
		};
		this.addChild(items);
		
		moveUp = new ButtonAdapter(screen, UID + "moveUp",
			new Vector2f(items.getWidth()+size,0),
			new Vector2f(size,items.getHeight()*0.5f),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty()) {
					int index = items.getSelectedIndex();
					if (index > 0) {
						SelectList.ListItem item = items.getListItem(index);
						items.removeListItem(index);
						items.insertListItem(index-1, item.getCaption(), item.getValue());
						items.setSelectedIndex(index-1);
						scrollToSelected();
						onSelectListUpdate(items.getListItems());
					}
				}
			}
		};
		moveUp.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowUp"));
		moveUp.setScaleEW(false);
		moveUp.setScaleNS(false);
		moveUp.setDocking(Element.Docking.NE);
		this.addChild(moveUp);
		
		moveDown = new ButtonAdapter(screen, UID + "moveDown",
			new Vector2f(items.getWidth()+size,items.getHeight()*0.5f),
			new Vector2f(size,items.getHeight()*0.5f),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty()) {
					int index = items.getSelectedIndex();
					if (index < items.getListItems().size()-2) {
						SelectList.ListItem item = items.getListItem(index);
						items.removeListItem(index);
						items.insertListItem(index+1, item.getCaption(), item.getValue());
						items.setSelectedIndex(index+1);
						scrollToSelected();
						onSelectListUpdate(items.getListItems());
					} else if (index < items.getListItems().size()-1) {
						SelectList.ListItem item = items.getListItem(index);
						items.removeListItem(index);
						items.addListItem(item.getCaption(), item.getValue());
						items.setSelectedIndex(index+1);
						scrollToSelected();
						onSelectListUpdate(items.getListItems());
					}
				}
			}
		};
		moveDown.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowDown"));
		moveDown.setScaleEW(false);
		moveDown.setScaleNS(false);
		moveDown.setDocking(Element.Docking.NE);
		this.addChild(moveDown);
		
		editItem = new ButtonAdapter(screen, UID + "editItem",
			new Vector2f(0,items.getHeight()),
			new Vector2f(moveUp.getX()/2,size),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty())
					onEditSelectedItem(items.getSelectedIndex(), items.getSelectedListItems().get(0));
			}
		};
		editItem.setText("Edit Selected");
		editItem.setScaleEW(false);
		editItem.setScaleNS(false);
		editItem.setDocking(Element.Docking.SW);
		this.addChild(editItem);
		
		removeItem = new ButtonAdapter(screen, UID + "removeItem",
			new Vector2f(moveUp.getX()/2,items.getHeight()),
			new Vector2f(moveUp.getX()/2,size),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty()) {
					SelectList.ListItem ret = items.getSelectedListItems().get(0);
					int index = items.removeListItem(ret.getCaption());
					if (index != -1) {
						if (index < items.getListItems().size())
							items.setSelectedIndex(index);
						else
							items.setSelectedIndex(index-1);
						scrollToSelected();
					}
					onRemoveSelectedItem(items.getSelectedIndex(), ret);
				}
			}
		};
		removeItem.setText("Remove Selected");
		removeItem.setScaleEW(false);
		removeItem.setScaleNS(false);
		removeItem.setDocking(Element.Docking.SW);
		this.addChild(removeItem);
		
		
		
		this.sizeToContent();
	}
	
	public SelectList getSelectList() {
		return this.items;
	}
	
	public void removeListItem(int indexd) {
		this.removeListItem(indexd);
	}
	
	public int removeListItem(String caption) {
		return items.removeListItem(caption);
	}
	
	public int removeListItem(Object value) {
		return items.removeListItem(value);
	}
	
	public int removeFirstListItem() {
		return items.removeFirstListItem();
	}
	
	public int removeLastListItem() {
		return items.removeLastListItem();
	}
	
	public void removeAllListItems() {
		items.removeAllListItems();
	}
	
	public int addListItem(String caption, Object value) {
		return items.addListItem(caption, value);
	}
	
	public void insertListItem(int index, String caption, Object value) {
		items.insertListItem(index, caption, value);
	}
	
	public void updateListItems(int index, String caption, Object value) {
		items.updateListItem(index, caption, value);
	}
	
	public int getSelectedIndex() {
		return this.items.getSelectedIndex();
	}
	
	public SelectList.ListItem getSelectedListItem() {
		List<SelectList.ListItem> list = this.items.getSelectedListItems();
		if (!list.isEmpty())
			return list.get(0);
		else
			return null;
	}
	
	public void scrollToSelected() {
		items.scrollToSelected();
	}
	
	public void hideEditButton() {
		this.editItem.hide();
	}
	
	public void hideRemoveButton() {
		this.removeItem.hide();
	}
	
	public abstract void onEditSelectedItem(int index, SelectList.ListItem updated);
	public abstract void onRemoveSelectedItem(int index, SelectList.ListItem removed);
	public abstract void onSelectListUpdate(List<SelectList.ListItem> items);
}