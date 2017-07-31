package catan.client.ui.taskbar;

import catan.client.graphics.ui.Button;
import catan.client.input.UserEvent;
import catan.client.input.UserEventType;

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
            super("FileMenuPopup");
            addListItem(new Button("QuitButton", 1, "quit", null,
                    (listener) -> listener.onUserEvent(new UserEvent(this, UserEventType.Shutdown, null))));
        }

        @Override
        public void update() {

        }
    }

}
