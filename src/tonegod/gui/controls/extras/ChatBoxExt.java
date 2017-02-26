package tonegod.gui.controls.extras;

import com.jme3.font.LineWrapMode;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.form.Form;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.scrolling.ScrollArea;
import tonegod.gui.controls.scrolling.ScrollPanel;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.layouts.FlowLayout;
import tonegod.gui.core.utils.BitmapTextUtil;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class ChatBoxExt extends Panel {
	private ScrollPanel saChatArea;
	private TextField tfChatInput;
	private ButtonAdapter btnChatSendMsg;
	private ButtonAdapter btnChatFilter;
	private float btnChatFilterHeight = 20;
	private SelectBox sbDefaultChannel;
	private float saContentPadding;
	private boolean showSendButton = true;
	private boolean showFilterButton = true;
	private boolean showChannelLabels = true;
	private Form chatForm;

	private Window filters = null;
	private ScrollArea filtersScrollArea = null;
	float filterLineHeight;

	float controlSpacing, controlSize, buttonWidth, scrollSize;
	Vector4f indents;

	private int sendKey;
	private int chatHistorySize = 30;
	protected List<ChatMessage> chatMessages = new ArrayList<>();

	protected List<ChatChannel> channels = new ArrayList<>();
	private String defaultCommand;

	List<Label> displayMessages = new ArrayList<>();

	/**
	 * Creates a new instance of the ChatBoxExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ChatBoxExt(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}

	/**
	 * Creates a new instance of the ChatBoxExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ChatBoxExt(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}

	/**
	 * Creates a new instance of the ChatBoxExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public ChatBoxExt(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg);
	}

	/**
	 * Creates a new instance of the ChatBoxExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 */
	public ChatBoxExt(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			screen.getStyle("Window").getVector2f("defaultSize"),
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}

	/**
	 * Creates a new instance of the ChatBoxExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 */
	public ChatBoxExt(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		);
	}

	/**
	 * Creates a new instance of the ChatBoxExt control
	 * 
	 * @param screen The screen control the Element is to be added to
	 * @param UID A unique String identifier for the Element
	 * @param position A Vector2f containing the x/y position of the Element
	 * @param dimensions A Vector2f containing the width/height dimensions of the Element
	 * @param resizeBorders A Vector4f containg the border information used when resizing the default image (x = N, y = W, z = E, w = S)
	 * @param defaultImg The default image to use for the Slider's track
	 */
	public ChatBoxExt(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);

		this.setIsMovable(true);
		this.setIsResizable(true);
		this.setScaleNS(false);
		this.setScaleEW(false);

		chatForm = new Form(screen);
		saContentPadding = screen.getStyle("ChatBox").getFloat("contentPadding");

		indents = screen.getStyle("Window").getVector4f("contentIndents");
		controlSpacing = screen.getStyle("Common").getFloat("defaultControlSpacing");
		controlSize = screen.getStyle("Common").getFloat("defaultControlSize");
		buttonWidth = screen.getStyle("Button").getVector2f("defaultSize").x;
		scrollSize = screen.getStyle("ScrollArea#VScrollBar").getFloat("defaultControlSize");

		saChatArea = new ScrollPanel(screen, UID + ":ChatArea",
			new Vector2f(
				indents.y,
				indents.x
			),
			new Vector2f(
				getWidth()-indents.y-indents.z,
				getHeight()-controlSize-(controlSpacing*2)-indents.x-indents.w
			)
		) {
			@Override
			public void controlResizeHook() {
				for (Label l : displayMessages) {
					l.setWidth(getScrollBoundsWidth()-(textPadding.x+textPadding.y));
					l.setHeight(l.getTextElement().getHeight());
				}
				getScrollableArea().layoutChildren();
				reshape();
				updateForResize();
				if (getVerticalScrollDistance() > 0)
					scrollToBottom();
			}
		};
		saChatArea.setIsResizable(false);
		saChatArea.setScaleEW(true);
		saChatArea.setScaleNS(true);
		saChatArea.getScrollableArea().setDocking(Docking.SW);
		saChatArea.getScrollableArea().setTextPaddingByKey("ScrollArea", "textPadding");
		saChatArea.setUseVerticalWrap(true);
		saChatArea.getScrollableArea().setLayout(
			new FlowLayout(screen, "margins 8 8 8 8", "padding 0 0 0 0")
		);
		
		addChild(saChatArea);


		btnChatFilter = new ButtonAdapter(
			screen,
			UID + ":ChatFilter",
			new Vector2f(indents.y,getHeight()-controlSize-indents.w),
			new Vector2f(controlSize, controlSize)
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (filters == null) {
					filters = new Window(
						screen,
						getElementParent().getUID()+":FilterWindow",
						new Vector2f(screen.getWidth()/2-225,screen.getHeight()/2-175),
						new Vector2f(450,350)
					);
					filters.setWindowTitle("Chat Filters");
					filters.setIsResizable(false);

					filtersScrollArea = new ScrollArea(
						screen,
						filters.getUID() + ":ScrollArea",
						new Vector2f(
							indents.y,
							indents.x+filters.getDragBarHeight()+controlSpacing
						),
						new Vector2f(
							filters.getWidth()-indents.y-indents.z,
							filters.getHeight()-indents.x-indents.w-filters.getDragBarHeight()-screen.getStyle("Window").getFloat("buttonAreaHeight")-(controlSpacing*2)
						),
						false
					);
					filtersScrollArea.getScrollableArea().setIgnoreMouse(true);
					filters.addChild(filtersScrollArea);

					ButtonAdapter btnFiltersClose = new ButtonAdapter(
						screen,
						filters.getUID() + ":btnClose",
						new Vector2f(filters.getWidth()-buttonWidth-indents.z,filters.getHeight()-controlSize-controlSpacing-indents.w)
					) {
						@Override
						public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
							filters.hideWindow();
						}
					};
					btnFiltersClose.setText("Close");
					btnFiltersClose.setDocking(Docking.SE);
					filters.addChild(btnFiltersClose);

					screen.addElement(filters);
				}
				showFiltersWindow();
			}
		};
		btnChatFilter.setDocking(Docking.SW);
		btnChatFilter.setScaleEW(false);
		btnChatFilter.setScaleNS(false);
		btnChatFilter.setText("F");

		chatForm.addFormElement(btnChatFilter);
		addChild(btnChatFilter);

		sbDefaultChannel = new SelectBox(
			screen,
			UID + ":DefaultChannel",
			new Vector2f(indents.y+controlSize, getHeight()-controlSize-indents.w),
			new Vector2f(120, controlSize)
		) {
			@Override
			public void onChange(int selectedIndex, Object value) {  }
		};
		sbDefaultChannel.setDocking(Docking.SW);
		sbDefaultChannel.setScaleEW(false);
		sbDefaultChannel.setScaleNS(false);

		chatForm.addFormElement(sbDefaultChannel);
		addChild(sbDefaultChannel);

		tfChatInput = new TextField(
			screen,
			UID + ":ChatInput",
			new Vector2f(indents.y+sbDefaultChannel.getWidth()+(controlSize*2), getHeight()-controlSize-indents.w),
			new Vector2f(getWidth()-sbDefaultChannel.getWidth()-(controlSize*2)-indents.y-indents.z-buttonWidth, controlSize)
		) {
			@Override
			public void controlKeyPressHook(KeyInputEvent evt, String text) {
				if (evt.getKeyCode() == sendKey) {
					if (tfChatInput.getText().length() > 0) {
						sendMsg();
					}
				}
			}
		};
		tfChatInput.setScaleEW(true);
		tfChatInput.setScaleNS(false);
		tfChatInput.setDocking(Docking.SW);

		btnChatSendMsg = new ButtonAdapter(
			screen,
			UID + ":ChatSendMsg",
			new Vector2f(getWidth()-indents.z-buttonWidth, getHeight()-controlSize-indents.w),
			new Vector2f(buttonWidth,controlSize)
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				sendMsg();
			}
		};
		btnChatSendMsg.setScaleEW(false);
		btnChatSendMsg.setScaleNS(false);
		btnChatSendMsg.setDocking(Docking.SE);
		btnChatSendMsg.setText("Send");


		chatForm.addFormElement(btnChatSendMsg);
		addChild(btnChatSendMsg);
		chatForm.addFormElement(tfChatInput);
		addChild(tfChatInput);

		addClippingLayer(this);
		
		populateEffects("Window");
	}

	/**
	 * Returns the chat display area (ScrollArea)
	 * @return 
	 */
	public ScrollPanel getChatArea() {
		return saChatArea;
	}

	/**
	 * Returns the chat input TextField
	 * @return 
	 */
	public TextField getChatInput() {
		return this.tfChatInput;
	}
	
	/**
	 * Sets the default validation type for the chat input TextField
	 * @param type 
	 */
	public void setValidationType(TextField.Type type) {
		this.tfChatInput.setType(type);
	}
	
	/**
	 * Sets the validation string for the chat input TextField
	 * @param grabBag 
	 */
	public void setCustomValidation(String grabBag) {
		this.tfChatInput.setCustomValidation(grabBag);
	}
	
	private void sendMsg() {
		if (tfChatInput.getText().length() > 0) {
			if (!tfChatInput.getText().equals("")) {
				String command = (String)sbDefaultChannel.getSelectedListItem().getValue();
				onSendMsg(command, tfChatInput.getText());
				tfChatInput.setText("");
			}
		}
	}

	/**
	 * Call this method to display a message
	 * @param command The object associated with the appropriate ChatChannel
	 * @param msg The String message to display
	 */
	public void receiveMsg(Object command, String msg) {
		ChatChannel channel = null;
		if (command instanceof String)
			channel = getChannelByStringCommand((String)command);
		else
			channel = getChannelByCommand(command);
		chatMessages.add(new ChatMessage(channel, msg));
		updateChatHistory();
	}

	private void updateChatHistory() {
		if (chatMessages.size() > chatHistorySize) {
			chatMessages.remove(0);
		}
		rebuildChat();
	}

	private void rebuildChat() {
		int index = 0;
		for (Label l : displayMessages) {
			saChatArea.removeScrollableContent(l);
		}
		displayMessages.clear();
		
		for (ChatMessage cm : chatMessages) {
			if (!cm.getChannel().getIsFiltered()) {
				Label l = createMessageLabel(index, cm);
				l.getLayoutHints().define("wrap");
				displayMessages.add(l);
				saChatArea.addScrollableContent(l,true);
				l.setWidth(saChatArea.getScrollBoundsWidth()-(textPadding.x+textPadding.y));
				l.setHeight(l.getTextElement().getHeight());
				index++;
			}
		}
		saChatArea.getScrollableArea().layoutChildren();
		saChatArea.reshape();
		saChatArea.updateForResize();
		saChatArea.controlResizeHook();
		saChatArea.reshape();
		if (saChatArea.getVerticalScrollDistance() > 0)
			saChatArea.scrollToBottom();
	}

	private Label createMessageLabel(int index, ChatMessage cm) {
		String s = cm.getMsg();
		Label l = new Label(
			screen,
			getUID() + ":Label" + index,
			new Vector2f(0, 0),
			new Vector2f(saChatArea.getScrollBoundsWidth()-(textPadding.x+textPadding.y),25)
		);			
		l.setTextWrap(LineWrapMode.Word);
		l.setScaleEW(true);
		l.setScaleNS(false);
		l.setDocking(Docking.NW);
		l.setIsResizable(false);
		l.setIsMovable(false);
		l.setIgnoreMouse(true);
	//	l.setClippingLayer(saChatArea);
		l.addClippingLayer(saChatArea);
		l.setClipPadding(saContentPadding);
		l.setFontColor(cm.getChannel().getColor());
		l.setFontSize(saChatArea.getFontSize());
		String channelLabel = "";
		if (showChannelLabels) channelLabel = "[" + cm.getChannel().getName() + "] ";
		l.setText(channelLabel + s);
		l.setHeight(l.getTextElement().getHeight());
		l.setIgnoreMouse(true);

		return l;
	}

	/**
	 * Sets the keyboard key code to send messages (in place of the send button)
	 * @param sendKey 
	 */
	public void setSendKey(int sendKey) {
		this.sendKey = sendKey;
	}

	/**
	 * Abstract event method called when the user sends a message
	 * @param command The Object associated with the appropriate ChatChannel for the message
	 * @param msg The String message to display
	 */
	public abstract void onSendMsg(Object command, String msg);

	/**
	 * Adds a ChatChannel that messages are display under and are filtered by
	 * @param UID The unique string identifier of the ChatChannel
	 * @param name The ChatChannel display name
	 * @param command The command associated with the ChatChannel (e.g. /group /say /ooc etc)
	 * @param filterDisplayText The text to display for this ChatChannel in the Chat Filters window
	 * @param color The ColorRGBA to use when displaying messages associated with the ChatChannel
	 * @param visibleToUser 
	 */
	public final void addChatChannel(String UID, String name, Object command, String filterDisplayText, ColorRGBA color, boolean visibleToUser) {
		channels.add(new ChatChannel(UID, name, command, filterDisplayText, color, visibleToUser));
		if (visibleToUser) {
			this.sbDefaultChannel.addListItem(name, command);
			this.sbDefaultChannel.pack();
		}
	}

	public void removeChatChannel(String name) {
		ChatChannel channel = getChannelByName(name);
		if (channel != null) {
			channels.remove(channel);
			this.sbDefaultChannel.removeListItem(name);
			this.sbDefaultChannel.pack();
		}
	}

	private ChatChannel getChannelByCommand(Object command) {
		ChatChannel c = null;
		for (ChatChannel channel : channels) {
			if (channel.getCommand() == command) {
				c = channel;
				break;
			}
		}
		return c;
	}

	private ChatChannel getChannelByStringCommand(String command) {
		ChatChannel c = null;
		for (ChatChannel channel : channels) {
			if (((String)channel.getCommand()).equals(command)) {
				c = channel;
				break;
			}
		}
		return c;
	}

	private ChatChannel getChannelByName(String name) {
		ChatChannel c = null;
		for (ChatChannel channel : channels) {
			if (channel.getName().equals(name)) {
				c = channel;
				break;
			}
		}
		return c;
	}

	/**
	 * Hides/Shows the Filter Window button
	 * @param showFilterButton 
	 */
	public void showFilterButton(boolean showFilterButton) {
		if (showFilterButton) {
			if (btnChatFilter.getParent() == null) {
				this.attachChild(btnChatFilter);
				chatForm.addFormElement(btnChatFilter);
				sbDefaultChannel.setX(indents.y+controlSize);
				tfChatInput.setX(indents.y+sbDefaultChannel.getWidth()+(controlSize*2));
				if (showSendButton)
					tfChatInput.setWidth(getWidth()-sbDefaultChannel.getWidth()-(controlSize*2)-indents.y-indents.z-buttonWidth);
				else
					tfChatInput.setWidth(getWidth()-sbDefaultChannel.getWidth()-(controlSize*2)-indents.y-indents.z);
			}
		} else {
			if (btnChatFilter.getParent() != null) {
				btnChatFilter.removeFromParent();
				chatForm.removeFormElement(btnChatFilter);
				sbDefaultChannel.setX(indents.y);
				tfChatInput.setX(indents.y+sbDefaultChannel.getWidth()+controlSize);
				if (showSendButton)
					tfChatInput.setWidth(getWidth()-sbDefaultChannel.getWidth()-controlSize-indents.y-indents.z-buttonWidth);
				else
					tfChatInput.setWidth(getWidth()-sbDefaultChannel.getWidth()-controlSize-indents.y-indents.z);
			}
		}
		this.showFilterButton = showFilterButton;
	}

	/**
	 * Hides/Shows the Send Button
	 * @param showSendButton 
	 */
	public void showSendButton(boolean showSendButton) {
		if (showSendButton) {
			if (btnChatSendMsg.getParent() == null) {
				this.attachChild(btnChatSendMsg);
				chatForm.addFormElement(btnChatSendMsg);
				tfChatInput.setWidth(tfChatInput.getWidth()-btnChatSendMsg.getWidth());
			}
		} else {
			if (btnChatSendMsg.getParent() != null) {
				btnChatSendMsg.removeFromParent();
				chatForm.removeFormElement(btnChatSendMsg);
				tfChatInput.setWidth(tfChatInput.getWidth()+btnChatSendMsg.getWidth());
			}
		}
		this.showSendButton = showSendButton;
	}

	public void setShowChannelLabels(boolean showChannelLabels) {
		this.showChannelLabels = showChannelLabels;
	}

	public class ChatMessage {
		private ChatChannel channel;
		private String msg;
		public ChatMessage(ChatChannel channel, String msg) {
			this.channel = channel;
			this.msg = msg;
		}

		public ChatChannel getChannel() {
			return channel;
		}
		public String getMsg() {
			return this.msg;
		}
	}

	public class ChatChannel {
		private String UID;
		private String name;
		private String filterDisplayText;
		private Object command;
		private ColorRGBA color;
		private boolean visibleToUser;
		private boolean isFiltered = false;

		public ChatChannel(String UID, String name, Object command, String filterDisplayText, ColorRGBA color, boolean visibleToUser) {
			this.UID = UID;
			this.name = name;
			this.command = command;
			this.filterDisplayText = filterDisplayText;
			this.color = color;
			this.visibleToUser = visibleToUser;
		}

		public String getUID() { return this.UID; }
		public String getName() {
			return this.name;
		}
		public Object getCommand() {
			return this.command;
		}
		public ColorRGBA getColor() {
			return this.color;
		}
		public boolean getVisibleToUser() { return visibleToUser; }
		public void setIsFiltered(boolean isFiltered) { this.isFiltered = isFiltered; }
		public boolean getIsFiltered() { return this.isFiltered; }
		public String getFilterDisplayText() { return filterDisplayText; }
	}

	/**
	 * Called by the Chat Filter Window.
	 * @param channel
	 * @param filter 
	 */
	public void setChannelFiltered(ChatChannel channel, boolean filter) {
		channel.setIsFiltered(filter);
		rebuildChat();
	}

	protected void showFiltersWindow() {
		Element scrollableArea = filtersScrollArea.getScrollableArea();
		filtersScrollArea.setPadding(2);

		scrollableArea.removeAllChildren();
		scrollableArea.setY(filtersScrollArea.getHeight());
		scrollableArea.setHeight(0);

		boolean init = true;
		String finalString = "";
		float currentHeight = 0;
		int index = 0;

		filterLineHeight = BitmapTextUtil.getTextLineHeight(scrollableArea, "Xg");

		for (ChatChannel channel : channels) {
			if (!channel.getFilterDisplayText().equals("")) {
				if (init) {
					finalString = "        " + channel.getFilterDisplayText() + "  ";
					init = false;
				} else {
					finalString += "\n        " + channel.getFilterDisplayText() + "  ";
				}
				currentHeight += filterLineHeight;
			}
		}

		currentHeight += scrollableArea.getTextPadding()*2;
		scrollableArea.setHeight(currentHeight);
		scrollableArea.setWidth(getWidth());
		scrollableArea.setText(finalString);

		index = 0;
		for (ChatChannel channel : channels) {
				this.addCheckBox(index, channel);
				index++;
		}

		filtersScrollArea.scrollToTop();
		filters.showWindow();
	}

	private void addCheckBox(int index, ChatChannel channel) {
		CheckBox checkbox = new CheckBox(screen, filtersScrollArea.getUID() + ":CheckBox:" + index,
			new Vector2f(8,filtersScrollArea.getTextPadding()+(index*filterLineHeight))
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				((ChatChannel)getElementUserData()).setIsFiltered(!isToggled);
				rebuildChat();
			}
		};
		checkbox.setElementUserData(channel);
		checkbox.setScaleEW(false);
		checkbox.setScaleNS(false);
		checkbox.setDocking(Docking.SW);
		checkbox.setIsResizable(false);
		checkbox.setIsMovable(false);
		checkbox.setIgnoreMouse(false);
	//	checkbox.setClippingLayer(filtersScrollArea);
		checkbox.addClippingLayer(filtersScrollArea);
		checkbox.setClipPadding(filtersScrollArea.getScrollableArea().getTextPadding());
		if (!channel.getIsFiltered())
			checkbox.setIsChecked(true);
		filtersScrollArea.addScrollableChild(checkbox);
	}

	/**
	 * Sets the ToolTip text to display for mouse focus of the TextField input
	 * @param tip 
	 */
	public void setToolTipTextInput(String tip) {
		this.tfChatInput.setToolTipText(tip);
	}

	/**
	 * Sets the ToolTip text to display for mouse focus of the Send button
	 * @param tip 
	 */
	public void setToolTipSendButton(String tip) {
		this.btnChatSendMsg.setToolTipText(tip);
	}
}
