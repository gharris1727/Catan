package com.gregswebserver.catan.client.ui.taskbar;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;

import java.awt.*;
import java.util.TreeSet;

/**
 * Created by greg on 4/3/16.
 * Taskbar at the top of the client screen.
 */
public class TaskbarRegion extends ConfigurableScreenRegion {

    private final TiledBackground background;

    private final TreeSet<TaskbarMenu> menus;

    public TaskbarRegion() {
        super("Taskbar", 1, "taskbar");
        menus = new TreeSet<>();
        background = new TiledBackground();
        add(background).setClickable(this);
    }

    public void addMenu(TaskbarMenu menu) {
        menu.setConfig(getConfig());
        menus.add(menu);
        add(menu);
    }

    public void removeMenu(TaskbarMenu menu) {
        menus.remove(menu);
        remove(menu);
    }

    @Override
    protected void renderContents() {
        int xPosition = 0;
        for (TaskbarMenu menu : menus) {
            menu.setPosition(new Point(xPosition, 0));
            xPosition += menu.getMask().getWidth();
        }
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }
}
