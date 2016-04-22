package com.gregswebserver.catan.client.ui.taskbar;

import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 4/22/16.
 * The primary menu listed first.
 */
public class FileMenu extends TaskbarMenu {

    public FileMenu() {
        super(1, "file");
    }

    @Override
    protected TaskbarPopup createPopup() {
        return new FileMenuPopup();
    }

    @Override
    public String toString() {
        return "FileMenu";
    }

    private class FileMenuPopup extends TaskbarPopup {

        private FileMenuPopup() {
            super("popup");
            //TODO: add a text label to the quit button
            addListItem(new TaskbarMenuItem(1, "quit") {
                @Override
                public String toString() {
                    return "QuitMenuItem";
                }

                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.Shutdown, null);
                }
            });
        }

        @Override
        public String toString() {
            return "FileMenuPopup";
        }
    }
}
