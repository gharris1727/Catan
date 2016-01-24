package com.gregswebserver.catan.client.graphics.ui.style;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.resources.GraphicSet;

import java.awt.*;
import java.util.regex.Pattern;

/**
 * Created by Greg on 1/15/2015.
 * A background used to render a ui background, that is tiled to fit multiple sizes.
 */
public enum UIStyle {

    Blue("blue");

    public static final String FONT_HEADING = "heading";
    public static final String FONT_PARAGRAPH = "paragraph";

    public static final String BACKGROUND_WINDOW = "window";
    public static final String BACKGROUND_INTERFACE = "interface";
    public static final String BACKGROUND_BUTTON = "button";
    public static final String BACKGROUND_TEXT = "text";
    public static final String BACKGROUND_LOBBIES = "lobbies";
    public static final String BACKGROUND_USERS = "users";
    public static final String BACKGROUND_GAME = "game"; //TODO: probably find a better place for this

    private static final String styleRootKey = "catan.ui.";
    private static final String fontRootKey = ".fonts.";
    private static final String fontNameKey = ".name";
    private static final String fontStyleKey = ".style";
    private static final String fontSizeKey = ".size";
    private static final String fontColorKey = ".color";
    private static final String backgroundRootKey = ".background.";

    private final String uiStyleKey;

    UIStyle(String uiStyleName) {
        this.uiStyleKey = styleRootKey + uiStyleName;
    }

    public TextStyle getTextStyle(String textStyleName) {
        String textStyleKey = uiStyleKey + fontRootKey + textStyleName;
        return new TextStyle(textStyleKey);
    }

    public BackgroundStyle getBackgroundStyle(String backgroundStyleName) {
        String backgroundStyleKey = uiStyleKey + backgroundRootKey + backgroundStyleName;
        return new BackgroundStyle(backgroundStyleKey);
    }

    /**
     * Created by Greg on 1/17/2015.
     * A style of text, including font, size, italics/bold, and color.
     */
    public static class TextStyle {

        private final String textStyleKey;
        private final Font font;
        private final Color color;

        public TextStyle(String textStyleKey) {
            this.textStyleKey = textStyleKey;
            String fontName = Client.staticConfig.get(textStyleKey + fontNameKey);
            String fontStyleName = Client.staticConfig.get(textStyleKey + fontStyleKey).toUpperCase();
            int fontSize = Client.staticConfig.getInt(textStyleKey + fontSizeKey);
            this.color = Client.staticConfig.getColor(textStyleKey + fontColorKey);
            this.font = new Font(fontName, getFontStyle(fontStyleName), fontSize);
        }

        private static int getFontStyle(String fontStyleName) {
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

    public class BackgroundStyle {

        private final GraphicSet graphicSet;

        public BackgroundStyle(String backgroundStyleKey) {
            graphicSet = new GraphicSet(backgroundStyleKey, RectangularMask.class);
        }

        public GraphicSet getGraphicSet() {
            return graphicSet;
        }
    }
}
