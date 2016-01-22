package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenRegion;
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

    private static final GraphicSet arrows = new GraphicSet("catan.ui.blue.icons.arrows", RectangularMask.class);

    private static final int lobbyHeight = 2*arrows.getMask().getHeight();
    private static final int footerHeight = Main.staticConfig.getInt("catan.graphics.interface.lobbylist.footer.height");

    private static final int arrowWidth = arrows.getMask().getWidth();
    private static final int openSlotsLabelSize = Main.staticConfig.getInt("catan.graphics.interface.lobbylist.header.open.width");
    private static final int currentClientsLabelSize = Main.staticConfig.getInt("catan.graphics.interface.lobbylist.header.current.width");
    private static final int gameTypeLabelSize = Main.staticConfig.getInt("catan.graphics.interface.lobbylist.header.type.width");
    private static final int lobbyOwnerLabelSize = Main.staticConfig.getInt("catan.graphics.interface.lobbylist.header.owner.width");

    private RenderMask lobbySize;

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

    private final TiledBackground background;
    private final LobbyListHeader header;
    private final LobbyListScroll scroll;
    private final LobbyListFooter footer;

    private final LobbyPool lobbies;
    private LobbySortOption sortOption;
    private Lobby selected;

    public LobbyListRegion(int priority,  LobbyPool lobbies) {
        super(priority);
        this.lobbies = lobbies;
        //Create the child region
        header = new LobbyListHeader(2);
        background = new TiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            public String toString() {
                return "LobbyListBackground";
            }
        };
        footer = new LobbyListFooter(2);
        scroll = new LobbyListScroll(1);
        add(header);
        add(background);
        add(footer);
        add(scroll);
    }

    @Override
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
        scroll.setPosition(new Point(0,lobbyHeight));
        //Create the new render masks.
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
        //Tell the scrolling window about our new size.
        scroll.setHostMask(mask);
    }

    @Override
    public UserEvent onMouseScroll(int rot) {
        scroll.scroll(0,rot);
        return null;
    }

    @Override
    public String toString() {
        return "LobbyListRegion";
    }

    public void update() {
        scroll.forceRender();
    }

    private class LobbyListHeader extends UIScreenRegion {

        private final LobbyListHeaderElement lobbyNameHeader;
        private final LobbyListHeaderElement lobbyOwnerHeader;
        private final LobbyListHeaderElement gameTypeHeader;
        private final LobbyListHeaderElement currentClientsHeader;
        private final LobbyListHeaderElement openSlotsHeader;

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
            lobbyNameHeader.setMask(lobbyNameSize);
            lobbyNameHeader.setPosition(lobbyNamePosition);
            lobbyOwnerHeader.setMask(lobbyOwnerSize);
            lobbyOwnerHeader.setPosition(lobbyOwnerPosition);
            gameTypeHeader.setMask(gameTypeSize);
            gameTypeHeader.setPosition(gameTypePosition);
            currentClientsHeader.setMask(currentClientsSize);
            currentClientsHeader.setPosition(currentClientsPosition);
            openSlotsHeader.setMask(openSlotsSize);
            openSlotsHeader.setPosition(openSlotsPosition);
        }

        @Override
        public String toString() {
            return "LobbyListHeader";
        }

        private class LobbyListHeaderElement extends UIScreenRegion {

            private final TiledBackground background;
            private final TextLabel labelGraphic;
            private final GraphicObject upArrow;
            private final GraphicObject downArrow;

            public LobbyListHeaderElement(int priority, LobbySortOption ascend, LobbySortOption descend) {
                super(priority);
                background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderElementBackground";
                    }
                };
                labelGraphic = new TextLabel(1,UIStyle.FONT_PARAGRAPH, ascend.getTitle()) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderElementLabelGraphic " + ascend.getTitle();
                    }
                };
                upArrow = new GraphicObject(1, arrows.getGraphic(0)) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderElementUpArrow " + ascend.getTitle();
                    }

                    @Override
                    public UserEvent onMouseClick(MouseEvent event) {
                        sortOption = ascend;
                        return new UserEvent(this, UserEventType.Lobby_Sort, ascend);
                    }
                };
                downArrow = new GraphicObject(1, arrows.getGraphic(1)) {
                    @Override
                    public String toString() {
                        return "LobbyListHeaderDownArrow " + descend.getTitle();
                    }

                    @Override
                    public UserEvent onMouseClick(MouseEvent event) {
                        sortOption = descend;
                        return new UserEvent(this,UserEventType.Lobby_Sort, descend);
                    }
                };
                //Add the objects to the screen.
                add(background).setClickable(this);
                add(labelGraphic).setClickable(this);
                add(upArrow);
                add(downArrow);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
                upArrow.setPosition(new Point(mask.getWidth()-arrowWidth, 0));
                downArrow.setPosition(new Point(mask.getWidth()-arrowWidth, lobbyHeight/2));
            }

            @Override
            protected void renderContents() {
                Point centered = getCenteredPosition(labelGraphic.getGraphic().getMask());
                labelGraphic.setPosition(new Point(0,centered.y));
            }

            @Override
            public UserEvent onMouseScroll(int rot) {
                return scroll.onMouseScroll(rot);
            }

            @Override
            public String toString() {
                return "LobbyListHeaderElement";
            }
        }
    }

    private class LobbyListScroll extends ScrollingScreenRegion {

        public LobbyListScroll(int priority) {
            super(priority);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
        }

        @Override
        protected void renderContents() {
            clear();
            int height = 0;
            synchronized (lobbies) {
                for (Lobby lobby : lobbies) {
                    LobbyListScrollElement elt = new LobbyListScrollElement(0, lobby);
                    elt.setStyle(getStyle());
                    add(elt).setPosition(new Point(0,height));
                    height += lobbyHeight;
                }
            }
            setMask(new RectangularMask(new Dimension(LobbyListRegion.this.getMask().getWidth(),height)));
        }

        private class LobbyListScrollElement extends UIScreenRegion {

            private final TiledBackground background;

            private final TextLabel lobbyNameText;
            private final TextLabel lobbyOwnerText;
            private final TextLabel gameTypeText;
            private final TextLabel currentClientsText;
            private final TextLabel openSlotsText;

            private final Lobby lobby;

            public LobbyListScrollElement(int priority, Lobby lobby) {
                super(priority);
                this.lobby = lobby;
                background = new TiledBackground(0, UIStyle.BACKGROUND_LOBBIES) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementBackground";
                    }
                };
                lobbyNameText = new TextLabel(1, UIStyle.FONT_PARAGRAPH, lobby.getConfig().getLobbyName()) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementLobbyName";
                    }
                };
                lobbyOwnerText = new TextLabel(1, UIStyle.FONT_PARAGRAPH, lobby.getOwner().username) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementOwnerName";
                    }
                };
                gameTypeText = new TextLabel(1, UIStyle.FONT_PARAGRAPH, lobby.getConfig().getMapGenerator()) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementGameType";
                    }
                };
                currentClientsText = new TextLabel(1, UIStyle.FONT_PARAGRAPH, "" + lobby.size()) {
                    @Override
                    public String toString() {
                        return "LobbyListScrollElementCurrentClients";
                    }
                };
                openSlotsText = new TextLabel(1, UIStyle.FONT_PARAGRAPH, "" + (lobby.getConfig().getMaxPlayers() - lobby.size())) {
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
                //Set the size computed from above.
                setMask(lobbySize);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
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
            public UserEvent onMouseScroll(int rot) {
                return LobbyListScroll.this.onMouseScroll(rot);
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

    private class LobbyListFooter extends UIScreenRegion {

        private final TiledBackground background;
        private final Button joinButton;
        private final Button createButton;

        public LobbyListFooter(int priority) {
            super(priority);
            background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
                public String toString() {
                    return "ServerListItem Background";
                }
            };
            joinButton = new Button(1, "Join") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return (selected == null) ? null : new UserEvent(this, UserEventType.Lobby_Join, selected.getOwner());
                }
                public String toString() {
                    return "LobbyListJoinButton";
                }
            };
            createButton = new Button(1, "Create new") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.Lobby_Create, null);
                }
                @Override
                public String toString() {
                    return "LobbyListCreateButton";
                }
            };
            //Add objects to the screen.
            add(background);
            add(joinButton);
            add(createButton);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
            joinButton.setPosition(new Point(16, 16));
            joinButton.setMask(new RoundedRectangularMask(new Dimension(128, 32)));
            createButton.setPosition(new Point(200, 16));
            createButton.setMask(new RoundedRectangularMask(new Dimension(128, 32)));
        }

        @Override
        public UserEvent onMouseScroll(int rot) {
            return scroll.onMouseScroll(rot);
        }

        @Override
        public String toString() {
            return "LobbyListFooter";
        }
    }
}
