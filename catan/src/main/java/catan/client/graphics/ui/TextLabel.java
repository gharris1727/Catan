package catan.client.graphics.ui;

import catan.client.graphics.graphics.Graphic;
import catan.client.graphics.graphics.TextGraphic;
import catan.client.graphics.masks.RectangularMask;
import catan.common.config.ConfigurationException;

import java.awt.*;

/**
 * Created by greg on 1/12/16.
 * A graphical text label
 */
public class TextLabel extends ConfigurableGraphicObject {

    private final String text;

    public TextLabel(String name, int priority, String configKey, String text) {
        super(name, priority, configKey);
        this.text = text;
    }

    @Override
    public void loadConfig(UIConfig config) {
        setText(text);
    }

    public void setText(String text) {
        if (text == null) {
            try {
                String key = getConfig().getLayout().get("text");
                text = getConfig().getLocalization(key);
            } catch (ConfigurationException ignored) {
                text = "!!";
            }
        }
        if (isRenderable() && (text != null) && !text.isEmpty())
            setGraphic(new TextGraphic(getConfig(), getConfig().getLayout().get("style"), text));
        else
            setGraphic(new Graphic(new RectangularMask(new Dimension(1, 1)), true));
    }
}
