package com.gregswebserver.catan.client.renderer.secondary.connected;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.lobby.Lobby;
import com.gregswebserver.catan.common.lobby.LobbyPool;
import com.gregswebserver.catan.common.lobby.LobbySortOption;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by greg on 1/10/16.
 * List of lobbies that the client can join, or create one themselves.
 */
public class LobbyListRegion extends UIScreenRegion {

    private static final int lobbyHeight = 100;
    private static final int footerHeight = 100;

    private static final int arrowWidth = 16;
    private static final int openSlotsLabelSize = 50;
    private static final int currentClientsLabelSize = 50;
    private static final int gameTypeLabelSize = 100;
    private static final int lobbyOwnerLabelSize = 100;

    private RenderMask lobbySize;

    private RenderMask sortArrowsSize;

    private RenderMask lobbyNameSize;
    private RenderMask lobbyOwnerSize;
    private RenderMask gameTypeSize;
    private RenderMask currentClientsSize;
    private RenderMask openSlotsSize;

    private Point lobbyNamePosition;
    private Point lobbyOwnerPosition;
    private Point gameTypePosition;
    private Point currentClientsPosition;
    private Point openSlotsPosition;

    private ScreenRegion header;
    private ScreenRegion background;
    private ScreenRegion scroll;
    private ScreenRegion footer;

    private final LobbyPool lobbies;
    private Lobby selected;

    public LobbyListRegion(int priority, UIStyle style, LobbyPool lobbies) {
        super(priority, style);
        this.lobbies = lobbies;
        background = new TiledBackground(0, getStyle().getBackgroundStyle()) {
            public String toString() {
                return "LobbyListBackground";
            }
        };
        header = new LobbyListHeader(2);
        footer = new LobbyListFooter(2);
        add(header).setClickable(this);
        add(background).setClickable(this);
        add(footer).setClickable(this);
    }

    protected void resizeContents(RenderMask mask) {
        //Get the new overall size of the window.
        int width = mask.getWidth();
        int height = mask.getHeight();
        //Calculate intermediate dimensions
        int windowHeight = (height - lobbyHeight - footerHeight);
        int lobbyNameLabelSize = width - arrowWidth
                - (arrowWidth + openSlotsLabelSize)
                - (arrowWidth + currentClientsLabelSize)
                - (arrowWidth + gameTypeLabelSize)
                - (arrowWidth + lobbyOwnerLabelSize);
        //Set the window locations
        header.setPosition(new Point());
        background.setPosition(new Point(0,lobbyHeight));
        footer.setPosition(new Point(0,height-footerHeight));
        //Create the new render masks.
        sortArrowsSize = new RectangularMask(new Dimension(arrowWidth, lobbyHeight));
        lobbyNameSize = new RectangularMask(new Dimension(arrowWidth + lobbyNameLabelSize, lobbyHeight));
        lobbyOwnerSize = new RectangularMask(new Dimension(arrowWidth + lobbyOwnerLabelSize, lobbyHeight));
        gameTypeSize = new RectangularMask(new Dimension(arrowWidth + gameTypeLabelSize, lobbyHeight));
        currentClientsSize = new RectangularMask(new Dimension(arrowWidth + currentClientsLabelSize, lobbyHeight));
        openSlotsSize = new RectangularMask(new Dimension(arrowWidth + openSlotsLabelSize, lobbyHeight));
        // Calculate the positions of the tiles
        lobbyNamePosition = new Point();
        lobbyOwnerPosition = new Point(lobbyNameLabelSize + arrowWidth, 0);
        gameTypePosition = new Point(lobbyOwnerPosition.x+ lobbyOwnerLabelSize + arrowWidth, 0);
        currentClientsPosition = new Point(gameTypePosition.x + gameTypeLabelSize + arrowWidth, 0);
        openSlotsPosition = new Point(currentClientsPosition.x + currentClientsLabelSize + arrowWidth, 0);
        //Resize the windows
        header.setMask(new RectangularMask(new Dimension(width,lobbyHeight)));
        background.setMask(new RectangularMask(new Dimension(width,windowHeight)));
        footer.setMask(new RectangularMask(new Dimension(width,footerHeight)));
        lobbySize = new RectangularMask(new Dimension(width,lobbyHeight));
    }

    @Override
    protected void renderContents() {
        remove(scroll);
        scroll = new LobbyListScroll(1);
        add(scroll).setClickable(this).setPosition(new Point(0,lobbyHeight));
    }

    @Override
    public UserEvent onMouseScroll(int rot) {
        scroll.getPosition().translate(0,rot);
        return null;
    }

    @Override
    public String toString() {
        return "LobbyListRegion";
    }

    private class LobbyListHeader extends ScreenRegion {

        private ScreenRegion lobbyNameHeader;
        private ScreenRegion lobbyOwnerHeader;
        private ScreenRegion gameTypeHeader;
        private ScreenRegion currentClientsHeader;
        private ScreenRegion openSlotsHeader;

        public LobbyListHeader(int priority) {
            super(priority);
            lobbyNameHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Lobby_Name_Asc, LobbySortOption.Lobby_Name_Desc);
            lobbyOwnerHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Host_Name_Asc, LobbySortOption.Host_Name_Desc);
            gameTypeHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Game_Type_Asc, LobbySortOption.Game_Type_Desc);
            currentClientsHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Num_Clients_Asc, LobbySortOption.Num_Clients_Desc);
            openSlotsHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Open_Spaces_Asc, LobbySortOption.Open_Spaces_Desc);
            add(lobbyNameHeader);
            add(lobbyOwnerHeader);
            add(gameTypeHeader);
            add(currentClientsHeader);
            add(openSlotsHeader);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            //Resized contents is already available from the outer class
            lobbyNameHeader.setMask(lobbyNameSize).setPosition(lobbyNamePosition);
            lobbyOwnerHeader.setMask(lobbyOwnerSize).setPosition(lobbyOwnerPosition);
            gameTypeHeader.setMask(gameTypeSize).setPosition(gameTypePosition);
            currentClientsHeader.setMask(currentClientsSize).setPosition(currentClientsPosition);
            openSlotsHeader.setMask(openSlotsSize).setPosition(openSlotsPosition);
        }

        @Override
        public String toString() {
            return "LobbyListHeader";
        }

        private class LobbyListHeaderElement extends ScreenRegion {

            private ScreenRegion background;
            private TextLabel labelGraphic;
            private StaticObject upArrow;
            private StaticObject downArrow;

            public LobbyListHeaderElement(int priority, LobbySortOption ascend, LobbySortOption descend) {
                super(priority);
                background = new TiledBackground(0, getStyle().getBackgroundStyle()) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderElementBackground";
                    }
                };
                labelGraphic = new TextLabel(1, getStyle().getSmallTextStyle(), ascend.getTitle()) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderElementLabelGraphic " + ascend.getTitle();
                    }
                };
                upArrow = new StaticObject(1, GraphicSet.UIBlueLobbyIcons.getGraphic(0)) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderElementUpArrow " + ascend.getTitle();
                    }

                    @Override
                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Lobby_Sort, ascend);
                    }
                };
                downArrow = new StaticObject(1, GraphicSet.UIBlueLobbyIcons.getGraphic(1)) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderDownArrow " + descend.getTitle();
                    }

                    @Override
                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this,UserEventType.Lobby_Sort, descend);
                    }
                };
                //Add the objects to the screen.
                add(background).setClickable(this);
                add(labelGraphic).setClickable(this);
                add(upArrow);
                add(downArrow);
                setClickable(labelGraphic);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
                Point centered = getCenteredPosition(labelGraphic.getGraphic().getMask());
                labelGraphic.setPosition(new Point(0,centered.y));
                //TODO: Fix these positions
                upArrow.setPosition(new Point(mask.getWidth()-arrowWidth, 0));
                downArrow.setPosition(new Point(mask.getWidth()-arrowWidth, lobbyHeight/2));
            }

            @Override
            public String toString() {
                return "LobbyListHeaderElement";
            }
        }
    }

    private class LobbyListScroll extends ScreenRegion {

        public LobbyListScroll(int priority) {
            super(priority);
            int height = lobbyHeight; //TODO: this over-counts the height, fix having zero height problems.
            synchronized (lobbies) {
                for (Lobby lobby : lobbies) {
                    add(new LobbyListScrollElement(0, lobby)).setPosition(new Point(0,height));
                    height += lobbyHeight;
                }
            }
            setMask(new RectangularMask(new Dimension(LobbyListRegion.this.getMask().getWidth(),height)));
        }

        @Override
        protected void resizeContents(RenderMask mask) {
        }

        @Override
        protected void renderContents() {
        }

        private class LobbyListScrollElement extends ScreenRegion {

            private ScreenRegion background;

            private ScreenObject lobbyNameText;
            private ScreenObject lobbyOwnerText;
            private ScreenObject gameTypeText;
            private ScreenObject currentClientsText;
            private ScreenObject openSlotsText;

            private final Lobby lobby;

            public LobbyListScrollElement(int priority, Lobby lobby) {
                super(priority);
                this.lobby = lobby;
                background = new TiledBackground(0, getStyle().getBackgroundStyle()) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementBackground";
                    }
                };
                lobbyNameText = new TextLabel(1, getStyle().getDarkTextStyle(), lobby.getConfig().getLobbyName()) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementLobbyName";
                    }
                };
                lobbyOwnerText = new TextLabel(1, getStyle().getDarkTextStyle(), lobby.getOwner().username) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementOwnerName";
                    }
                };
                gameTypeText = new TextLabel(1, getStyle().getDarkTextStyle(), lobby.getConfig().getMapGenerator()) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementGameType";
                    }
                };
                currentClientsText = new TextLabel(1, getStyle().getDarkTextStyle(), "" + lobby.size()) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementCurrentClients";
                    }
                };
                openSlotsText = new TextLabel(1, getStyle().getDarkTextStyle(), "" + (lobby.getConfig().getMaxPlayers() - lobby.size())) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementOpenSlots";
                    }
                };
                add(background).setClickable(this);
                add(lobbyNameText).setClickable(this);
                add(lobbyOwnerText).setClickable(this);
                add(gameTypeText).setClickable(this);
                add(currentClientsText).setClickable(this);
                add(openSlotsText).setClickable(this);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                lobbyNameText.setPosition(lobbyNamePosition);
                lobbyOwnerText.setPosition(lobbyOwnerPosition);
                gameTypeText.setPosition(gameTypePosition);
                currentClientsText.setPosition(currentClientsPosition);
                openSlotsText.setPosition(openSlotsPosition);
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                selected = this.lobby;
                return null;
            }

            @Override
            public String toString() {
                return "LobbyListScrollElement "+ lobby;
            }
        }

        public String toString() {
            return "LobbyListScroll";
        }
    }

    private class LobbyListFooter extends ScreenRegion {

        private ScreenRegion background;
        private ScreenRegion joinButton;

        public LobbyListFooter(int priority) {
            super(priority);
            background = new EdgedTiledBackground(0, getStyle().getInterfaceStyle()) {
                public String toString() {
                    return "ServerListItem Background";
                }
            };
            joinButton = new com.gregswebserver.catan.client.graphics.ui.text.Button(1, getStyle(), "Join") {
                public UserEvent onMouseClick(MouseEvent event) {
                    return (selected == null) ? null : new UserEvent(this, UserEventType.Lobby_Join, selected);
                }

                public String toString() {
                    return "LobbyListJoinButton";
                }
            };
            //Add objects to the screen.
            add(background).setClickable(this);
            add(joinButton);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
            joinButton.setPosition(new Point(16, 16));
            joinButton.setMask(new RoundedRectangularMask(new Dimension(128, 32)));
        }

        @Override
        public String toString() {
            return "LobbyListFooter";
        }
    }
}
