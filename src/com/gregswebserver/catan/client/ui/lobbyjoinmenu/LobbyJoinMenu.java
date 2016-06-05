package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.common.structure.lobby.MatchmakingPool;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/17/2015.
 * The list of lobbies hosted on the current server.
 */
public class LobbyJoinMenu extends ClientScreen {

    private int tabHeight;

    private final LobbyTabRegion lobbyTabs;
    private final LobbyListRegion pregameLobbies;
    private final LobbyListRegion ingameLobbies;
    private final LobbyCreateRegion createLobby;
    private final UserListRegion userList;

    public LobbyJoinMenu(MatchmakingPool matchmakingPool) {
        super("LobbyJoinMenu", "lobbylist");
        lobbyTabs = new LobbyTabRegion();
        pregameLobbies = new LobbyListRegion(3, "pregame", matchmakingPool.getLobbyList().getPregameLobbies());
        ingameLobbies = new LobbyListRegion(2, "ingame", matchmakingPool.getLobbyList().getIngameLobbies());
        createLobby = new LobbyCreateRegion();
        userList = new UserListRegion(matchmakingPool.getClientList());
        //Add the screen contents
        add(lobbyTabs);
        add(pregameLobbies);
        add(ingameLobbies);
        add(createLobby);
        add(userList);
    }

    @Override
    public void loadConfig(UIConfig config) {
        tabHeight = config.getLayout().getInt("tabs.height");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        //Get the new overall size of the window.
        int width = mask.getWidth();
        int height = mask.getHeight();
        //Calculate intermediate dimensions
        int mainWidth = (width*3)/4;
        int mainHeight = height - tabHeight;
        int userListWidth = width - mainWidth;
        RectangularMask mainMask = new RectangularMask(new Dimension(mainWidth, mainHeight));
        //Create new positions
        pregameLobbies.setPosition(new Point(0, tabHeight));
        ingameLobbies.setPosition(new Point(0, tabHeight));
        createLobby.setPosition(new Point(0, tabHeight));
        userList.setPosition(new Point(mainWidth, 0));
        //Create new render masks.
        lobbyTabs.setMask(new RectangularMask(new Dimension(mainWidth, tabHeight)));
        pregameLobbies.setMask(mainMask);
        ingameLobbies.setMask(mainMask);
        createLobby.setMask(mainMask);
        userList.setMask(new RectangularMask(new Dimension(userListWidth,height)));
    }

    @Override
    public void update() {
        pregameLobbies.update();
        ingameLobbies.update();
        userList.update();
    }

    private class LobbyTabRegion extends ConfigurableScreenRegion {

        private final Button pregame;
        private final Button ingame;
        private final Button create;

        private LobbyTabRegion() {
            super("LobbyTabs", 0, "tabs");
            pregame = new Button("PregameButton", 0, "pregame", "Join") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    LobbyJoinMenu.this.clear();
                    LobbyJoinMenu.this.add(lobbyTabs);
                    LobbyJoinMenu.this.add(pregameLobbies);
                    LobbyJoinMenu.this.add(userList);
                    update();
                    return null;
                }
            };
            ingame = new Button("SpectateButton", 0, "ingame", "Spectate") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    LobbyJoinMenu.this.clear();
                    LobbyJoinMenu.this.add(lobbyTabs);
                    LobbyJoinMenu.this.add(ingameLobbies);
                    LobbyJoinMenu.this.add(userList);
                    update();
                    return null;
                }
            };
            create = new Button("CreateNew", 0, "create", "Create Game") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    LobbyJoinMenu.this.clear();
                    LobbyJoinMenu.this.add(lobbyTabs);
                    LobbyJoinMenu.this.add(createLobby);
                    LobbyJoinMenu.this.add(userList);
                    update();
                    return null;
                }
            };
            add(pregame);
            add(ingame);
            add(create);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            int thirdWidth = mask.getWidth()/3;
            ingame.setPosition(new Point(thirdWidth, 0));
            create.setPosition(new Point(2*thirdWidth, 0));
            RectangularMask thirdMask = new RectangularMask(new Dimension(thirdWidth, mask.getHeight()));
            pregame.setMask(thirdMask);
            ingame.setMask(thirdMask);
            create.setMask(new RectangularMask(new Dimension(mask.getWidth()-2*thirdWidth, mask.getHeight())));
        }
    }
}
