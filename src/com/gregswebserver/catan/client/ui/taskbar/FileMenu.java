package com.gregswebserver.catan.client.ui.taskbar;

import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by greg on 4/22/16.
 * The primary menu listed first.
 */
public class FileMenu extends TaskbarMenu {

    public FileMenu() {
        super("FileMenu", 1, "file");
    }

    @Override
    protected TaskbarPopup createPopup() {
        return new FileMenuPopup();
    }

    private class FileMenuPopup extends TaskbarPopup {

        private FileMenuPopup() {
            super("FileMenuPopup", "popup");
            addListItem(new Button("QuitButton", 1, "quit", null) {

                @Override
                public UserEvent onMouseDrag(Point p) {
                    return FileMenuPopup.this.onMouseDrag(p);
                }

                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.Shutdown, null);
                }
            });
        }

        @Override
        public void update() {
        }
    }

}
