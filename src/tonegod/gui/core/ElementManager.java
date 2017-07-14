/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tonegod.gui.core;

import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import tonegod.gui.controls.util.ModalBackground;
import tonegod.gui.core.utils.ScaleUtil;
import tonegod.gui.effects.EffectManager;
import tonegod.gui.effects.cursor.CursorEffects;
import tonegod.gui.framework.core.AnimLayer;
import tonegod.gui.framework.core.AnimManager;
import tonegod.gui.style.Style;
import tonegod.gui.style.StyleManager.CursorType;

/**
 *
 * @author t0neg0d
 */
public interface ElementManager {
    Application getApplication();

    float getWidth();
    float getHeight();
    Vector2f getMouseXY();
    Vector2f getTouchXY();
    CollisionResult getLastCollision();
    Node getGUINode();

    void addElement(Element element);
    void addElement(Element element, boolean hide);
    void removeElement(Element element);
    Element getElementById(String UID);
    void setKeyboardElement(Element element);
    void setTabFocusElement(Element element);
    void resetTabFocusElement();
    Element getDropElement();

    float getZOrderStepMinor();
    float getZOrderStepMajor();
    void updateZOrder(Element element);

    Style getStyle(String key);
    void setClipboardText(String text);
    String getClipboardText();

    boolean getUseTextureAtlas();
    float[] parseAtlasCoords(String coords);
    Texture getAtlasTexture();
    Texture createNewTexture(String texturePath);
    void setGlobalAlpha(float alpha);
    float getGlobalAlpha();

    ScaleUtil getScaleManager();
    float scaleFloat(float in);
    Vector2f scaleVector2f(Vector2f in);
    Vector3f scaleVector3f(Vector3f in);
    Vector4f scaleVector4f(Vector4f in);
    float scaleFontSize(float in);

    BitmapFont getDefaultGUIFont();
    EffectManager getEffectManager();
    AnimManager getAnimManager();

    boolean getUseUIAudio();
    void setUseUIAudio(boolean use);
    void setUIAudioVolume(float volume);

    boolean getUseToolTips();
    void setUseToolTips(boolean use);
    void updateToolTipLocation();
    Element getToolTipFocus();
    void hideToolTip();

    void setUseCustomCursors(boolean use);
    boolean getUseCustomCursors();
    void setCursor(CursorType cursorType);
    void setUseCursorEffects(boolean use);
    CursorEffects getCursorEffects();
//  boolean getUseCursorEffects();

    void onKeyEvent(KeyInputEvent evt);

    ModalBackground getModalBackground();
    void showAsModal(Element el, boolean showWithEffect);
    void hideModalBackground();

    void showVirtualKeyboard();
    void hideVirtualKeyboard();
    void handleAndroidMenuState(Element element);

    // 2D Framework
    AnimLayer addAnimLayer(String UID);
    void addAnimLayer(String UID, AnimLayer layer);
    AnimLayer removeAnimLayer(String UID);
    void removeAnimLayer(AnimLayer animLayer);
}
