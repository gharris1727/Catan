package catan.client.graphics.ui;

import catan.common.config.ConfigSource;
import catan.common.resources.GraphicSet;

import java.awt.*;
import java.util.regex.Pattern;

/**
 * Created by Greg on 1/15/2015.
 * Class containing configuration data relevant to designing dynamic user interfaces.
 */
public class UIConfig {


    private static final String fontRootKey = "fonts.";
    private static final String fontNameKey = ".name";
    private static final String fontStyleKey = ".style";
    private static final String fontSizeKey = ".size";
    private static final String fontColorKey = ".color";
    private static final String fontSpacingKey = ".spacing";
    private static final String backgroundRootKey = "background.";
    private static final String iconRootKey = "icons.";

    private final ConfigSource style;
    private final ConfigSource layout;
    private final ConfigSource locale;
    private final ConfigSource teamColors;

    public UIConfig(ConfigSource style, ConfigSource layout, ConfigSource locale, ConfigSource teamColors) {
        this.style = style;
        this.layout = layout;
        this.locale = locale;
        this.teamColors = teamColors;
    }

    public UIConfig(UIConfig config) {
        this.style = config.style;
        this.layout = config.layout;
        this.locale = config.locale;
        this.teamColors = config.teamColors;
    }

    public UIConfig narrow(String prefix) {
        return new UIConfig(style, layout.narrow(prefix), locale, teamColors);
    }

    public ConfigSource getLayout() {
        return layout;
    }

    public ConfigSource getLocale() {
        return locale;
    }

    public ConfigSource getTeamColors() {
        return teamColors;
    }

    public String getLocalization(String key) {
        try {
            return locale.get(key);
        } catch (Exception ignored) {
            return key;
        }
    }

    public TextStyle getTextStyle(String styleName) {
        return new TextStyle(fontRootKey + styleName);
    }

    public GraphicSet getBackgroundGraphics(String backgroundStyleName) {
        return new GraphicSet(style, backgroundRootKey + backgroundStyleName, null);
    }

    public GraphicSet getIconGraphics(String styleName) {
        return new GraphicSet(style, iconRootKey + styleName, null);
    }

    /**
     * Created by Greg on 1/17/2015.
     * A style of text, including font, size, italics/bold, and color.
     */
    public class TextStyle {

        private final Font font;
        private final Color color;
        private final int spacing;

        @SuppressWarnings("MagicConstant")
        private TextStyle(String textStyleKey) {
            String fontName = style.get(textStyleKey + fontNameKey);
            String fontStyleName = style.get(textStyleKey + fontStyleKey).toUpperCase();
            int fontSize = style.getInt(textStyleKey + fontSizeKey);
            this.color = style.getColor(textStyleKey + fontColorKey);
            this.font = new Font(fontName, getFontStyle(fontStyleName), fontSize);
            this.spacing = style.getInt(textStyleKey + fontSpacingKey);
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

        public int getLineSpacing() {
            return spacing;
        }
    }
}
