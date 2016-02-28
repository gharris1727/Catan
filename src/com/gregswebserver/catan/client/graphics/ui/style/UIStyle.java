package com.gregswebserver.catan.client.graphics.ui.style;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.PropertiesFile;
import com.gregswebserver.catan.common.resources.PropertiesFileInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.awt.*;
import java.util.regex.Pattern;

/**
 * Created by Greg on 1/15/2015.
 * A background used to render a ui background, that is tiled to fit multiple sizes.
 */
public class UIStyle {

    public static final String FONT_HEADING = "heading";
    public static final String FONT_PARAGRAPH = "paragraph";

    public static final String BACKGROUND_WINDOW = "window";
    public static final String BACKGROUND_INTERFACE = "interface";
    public static final String BACKGROUND_BUTTON = "button";
    public static final String BACKGROUND_TEXT = "text";
    public static final String BACKGROUND_LOBBIES = "lobbies";
    public static final String BACKGROUND_USERS = "users";
    public static final String BACKGROUND_GAME = "game"; //TODO: probably find a better place for this

    private static final String fontRootKey = "fonts.";
    private static final String fontNameKey = ".name";
    private static final String fontStyleKey = ".style";
    private static final String fontSizeKey = ".size";
    private static final String fontColorKey = ".color";
    private static final String backgroundRootKey = "background.";
    private static final String iconRootKey = "icons.";

    private final PropertiesFile styleDefinition;

    public UIStyle(String styleName) {
        this.styleDefinition = ResourceLoader.getPropertiesFile(
                new PropertiesFileInfo("config/ui/" + styleName + ".properties", styleName + " Properties"));
    }

    public TextStyle getTextStyle(String styleName) {
        return new TextStyle(fontRootKey + styleName);
    }

    public GraphicSet getBackgroundGraphics(String backgroundStyleName) {
        return new GraphicSet(styleDefinition, backgroundRootKey + backgroundStyleName, RectangularMask.class);
    }

    public GraphicSet getIconGraphics(String styleName, Class<? extends RenderMask> mask) {
        return new GraphicSet(styleDefinition, iconRootKey + styleName, mask);
    }

    /**
     * Created by Greg on 1/17/2015.
     * A style of text, including font, size, italics/bold, and color.
     */
    public class TextStyle {

        private final Font font;
        private final Color color;

        @SuppressWarnings("MagicConstant")
        private TextStyle(String textStyleKey) {
            String fontName = styleDefinition.get(textStyleKey + fontNameKey);
            String fontStyleName = styleDefinition.get(textStyleKey + fontStyleKey).toUpperCase();
            int fontSize = styleDefinition.getInt(textStyleKey + fontSizeKey);
            this.color = styleDefinition.getColor(textStyleKey + fontColorKey);
            this.font = new Font(fontName, getFontStyle(fontStyleName), fontSize);
        }

        private int getFontStyle(String fontStyleName) {
            int style = Font.PLAIN;
            if (Pattern.matches("BOLD",fontStyleName))
                style |= Font.BOLD;
            if (Pattern.matches("ITALIC",fontStyleName))
                style |= Font.ITALIC;
            return style;
        }

        public Font getFont() {
            return font;
        }

        public Color getColor() {
            return color;
        }
    }
}
