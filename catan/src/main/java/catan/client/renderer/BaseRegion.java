package catan.client.renderer;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.ScreenRegion;
import catan.client.graphics.ui.Configurable;
import catan.client.graphics.ui.UIConfig;
import catan.client.graphics.ui.Updatable;
import catan.client.ui.ClientScreen;
import catan.client.ui.PopupWindow;
import catan.client.ui.taskbar.TaskbarMenu;
import catan.client.ui.taskbar.TaskbarRegion;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by greg on 4/22/16.
 * Top-level screen region to composite all of the window functionality together.
 */
public class BaseRegion extends ScreenRegion implements Configurable, Updatable {

    private UIConfig config;
    private int taskbarHeight;
    private RenderMask liveMask;
    private Point livePosition;
    private Insets popupInsets;

    private final TaskbarRegion taskbar;
    private final List<PopupWindow> popups;
    private ClientScreen live;

    public BaseRegion() {
        super("Base Region", 0);
        taskbar = new TaskbarRegion();
        live = null;
        popups = new LinkedList<>();
    }

    public void displayPopup(PopupWindow popup) {
        if (popup != null) {
            popup.setHost(this);
            popup.setInsets(popupInsets);
            if (getConfig() != null)
                popup.setConfig(config.narrow("popup"));
            center(popup); //TODO: provide a way to position popups relative to their source.
            popups.add(popup);
            forceRender();
        }
    }

    public void removePopup(PopupWindow popup) {
        popups.remove(popup);
        forceRender();
    }

    public void addMenu(TaskbarMenu menu) {
        taskbar.addMenu(menu);
    }

    public void removeMenu(TaskbarMenu menu) {
        taskbar.removeMenu(menu);
    }

    public void displayScreen(ClientScreen region) {
        if (region != null) {
            if (getConfig() != null)
                region.setConfig(getConfig());
            if (liveMask != null) {
                region.setMask(liveMask);
                region.setPosition(livePosition);
            }
        }
        live = region;
        forceRender();
    }

    @Override
    public void update() {
        if (live != null)
            live.update();
    }

    @Override
    protected void renderContents() {
        clear();
        add(taskbar);
        add(live);
        for (PopupWindow popup : popups)
            add(popup);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        taskbar.setMask(new RectangularMask(new Dimension(mask.getWidth(), taskbarHeight)));
        liveMask = new RectangularMask(new Dimension(mask.getWidth(), mask.getHeight() - taskbarHeight));
        livePosition = new Point(0, taskbarHeight);
        popupInsets = new Insets(taskbarHeight, 0, 0, 0);
        if (live != null) {
            live.setMask(liveMask);
            live.setPosition(livePosition);
        }
        for (PopupWindow popup : popups) {
            popup.setInsets(popupInsets);
        }
    }

    @Override
    public void setConfig(UIConfig config) {
        this.config = config;
        taskbarHeight = config.getLayout().getInt("taskbar.height");
        taskbar.setConfig(config);
        if (live != null)
            live.setConfig(config);
    }

    @Override
    public UIConfig getConfig() {
        return config;
    }
}
