package catan.client.ui.taskbar;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.*;
import catan.client.input.UserEventListener;
import catan.client.ui.PopupWindow;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 4/22/16.
 * A menu object that is displayed in the TaskbarRegion.
 */
public abstract class TaskbarMenu extends ConfigurableScreenRegion implements Comparable<TaskbarMenu> {

    private final TiledBackground background;
    private TaskbarPopup popup;

    protected TaskbarMenu(String name, int priority, String configKey) {
        super(name, priority, configKey);
        background = new EdgedTiledBackground();
        popup = null;
    }

    @Override
    public void loadConfig(UIConfig config) {
        TextLabel label = new TextLabel(toString() + "Label", 1, "label", null);
        background.setConfig(config);
        label.setConfig(config);
        setMask(config.getLayout().getRenderMask("mask"));
        clear();
        center(label);
        add(background).setClickable(this);
        add(label).setClickable(this);
    }

    @Override
    public void onMouseClick(UserEventListener listener, MouseEvent event) {
        if (popup == null) {
            popup = createPopup();
            popup.display();
        } else {
            popup.expire();
        }
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    protected abstract TaskbarPopup createPopup();

    protected abstract class TaskbarPopup extends PopupWindow {

        private final List<DefaultConfigurableScreenRegion> items;

        protected TaskbarPopup(String name) {
            super(name, TaskbarMenu.this.getConfigKey(), TaskbarMenu.this);
            items = new ArrayList<>();
        }

        protected void addListItem(DefaultConfigurableScreenRegion item) {
            items.add(item);
            forceRender();
        }

        @Override
        public void expire() {
            popup = null;
            super.expire();
        }

        @Override
        protected void renderContents() {
            clear();
            int height = 0;
            int width = 0;
            for (DefaultConfigurableScreenRegion item : items) {
                item.setConfig(getConfig());
                RenderMask itemSize = item.getMask();
                height += itemSize.getHeight();
                if (width < itemSize.getWidth())
                    width = itemSize.getWidth();
                add(item);
            }
            setMask(new RectangularMask(new Dimension(width, height)));
        }
    }

    @Override
    public int compareTo(TaskbarMenu t) {
        return getRenderPriority() - t.getRenderPriority();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskbarMenu that = (TaskbarMenu) o;

        if (!background.equals(that.background)) return false;
        return popup.equals(that.popup);
    }

    @Override
    public int hashCode() {
        int result = background.hashCode();
        result = 31 * result + popup.hashCode();
        return result;
    }
}
