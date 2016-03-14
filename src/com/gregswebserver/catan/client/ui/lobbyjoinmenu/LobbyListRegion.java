package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.StyledGraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.structure.lobby.Lobby;
import com.gregswebserver.catan.common.structure.lobby.LobbyPool;
import com.gregswebserver.catan.common.structure.lobby.LobbySortOption;
import com.gregswebserver.catan.common.structure.lobby.LobbyState;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by greg on 1/10/16.
 * List of lobbies that the client can join, or create one themselves.
 */
public class LobbyListRegion extends UIScreenRegion {

    private static final int headerHeight = Client.graphicsConfig.getInt("interface.lobbylist.header.height");
    private static final int lobbyHeight = Client.graphicsConfig.getInt("interface.lobbylist.lobby.height");
    private static final int footerHeight = Client.graphicsConfig.getInt("interface.lobbylist.footer.height");
    private static final int spacing = Client.graphicsConfig.getInt("interface.lobbylist.spacing");

    private static final int gameTypeColumnWidth = Client.graphicsConfig.getInt("interface.lobbylist.header.type.width");
    private static final int currentClientsColumnWidth = Client.graphicsConfig.getInt("interface.lobbylist.header.current.width");
    private static final int openSlotsColumnWidth = Client.graphicsConfig.getInt("interface.lobbylist.header.open.width");
    private static final int arrowWidth = Client.graphicsConfig.getInt("interface.lobbylist.arrows.width");

    private int lobbyNameColumnWidth;
    private RenderMask lobbySize;

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
        int windowHeight = (height - headerHeight - footerHeight);
        lobbyNameColumnWidth = mask.getWidth() - openSlotsColumnWidth - currentClientsColumnWidth - gameTypeColumnWidth;
        //Set the window locations
        header.setPosition(new Point());
        background.setPosition(new Point(0,headerHeight));
        scroll.setPosition(background.getPosition());
        footer.setPosition(new Point(0,height-footerHeight));
        //Resize the windows
        background.setMask(new RectangularMask(new Dimension(width,windowHeight)));
        scroll.setHostView(background.getMask(),new Insets(0,0,0,0));
        header.setMask(new RectangularMask(new Dimension(width,headerHeight)));
        footer.setMask(new RectangularMask(new Dimension(width,footerHeight)));
        lobbySize = new RectangularMask(new Dimension(width,lobbyHeight));
    }

    @Override
    public UserEvent onMouseScroll(MouseWheelEvent event) {
        scroll.scroll(0,event.getScrollAmount());
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
        private final LobbyListHeaderElement gameTypeHeader;
        private final LobbyListHeaderElement currentClientsHeader;
        private final LobbyListHeaderElement openSlotsHeader;

        private LobbyListHeader(int priority) {
            super(priority);
            lobbyNameHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Lobby_Name_Asc, LobbySortOption.Lobby_Name_Desc);
            gameTypeHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Game_Type_Asc, LobbySortOption.Game_Type_Desc);
            currentClientsHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Num_Clients_Asc, LobbySortOption.Num_Clients_Desc);
            openSlotsHeader = new LobbyListHeaderElement(0,
                    LobbySortOption.Open_Spaces_Asc, LobbySortOption.Open_Spaces_Desc);
            add(lobbyNameHeader);
            add(gameTypeHeader);
            add(currentClientsHeader);
            add(openSlotsHeader);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            //Resized contents is already available from the outer class
            lobbyNameHeader.setMask(new RectangularMask(new Dimension(lobbyNameColumnWidth, mask.getHeight())));
            lobbyNameHeader.setPosition(new Point());
            gameTypeHeader.setMask(new RectangularMask(new Dimension(gameTypeColumnWidth, mask.getHeight())));
            gameTypeHeader.setPosition(new Point(lobbyNameColumnWidth, 0));
            currentClientsHeader.setMask(new RectangularMask(new Dimension(currentClientsColumnWidth, mask.getHeight())));
            currentClientsHeader.setPosition(new Point(lobbyNameColumnWidth + gameTypeColumnWidth, 0));
            openSlotsHeader.setMask(new RectangularMask(new Dimension(openSlotsColumnWidth, mask.getHeight())));
            openSlotsHeader.setPosition(new Point(lobbyNameColumnWidth + gameTypeColumnWidth + currentClientsColumnWidth, 0));
        }

        @Override
        public String toString() {
            return "LobbyListHeader";
        }

        private class LobbyListHeaderElement extends UIScreenRegion {

            private final TiledBackground background;
            private final TextLabel labelGraphic;
            private final UpArrow upArrow;
            private final DownArrow downArrow;

            public LobbyListHeaderElement(int priority, LobbySortOption ascend, LobbySortOption descend) {
                super(priority);
                background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_LOBBIES) {
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
                upArrow = new UpArrow(ascend);
                downArrow = new DownArrow(descend);
                //Add the objects to the screen.
                add(background).setClickable(this);
                add(labelGraphic).setClickable(this);
                add(upArrow);
                add(downArrow);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(new RectangularMask(new Dimension(mask.getWidth() - arrowWidth, mask.getHeight())));
            }

            @Override
            protected void renderContents() {
                upArrow.setPosition(new Point(getMask().getWidth()-arrowWidth, 0));
                downArrow.setPosition(new Point(getMask().getWidth()-arrowWidth, getMask().getHeight()/2));
                center(labelGraphic).x = spacing;
            }

            @Override
            public UserEvent onMouseScroll(MouseWheelEvent event) {
                return scroll.onMouseScroll(event);
            }

            public void clearArrows() {
                upArrow.index = 0;
                downArrow.index = 1;
            }

            @Override
            public String toString() {
                return "LobbyListHeaderElement";
            }

            private class DownArrow extends StyledGraphicObject {

                private final LobbySortOption descend;
                int index;

                public DownArrow(LobbySortOption descend) {
                    super(1);
                    this.descend = descend;
                    index = 1;
                }

                @Override
                public Graphic getGraphic() {
                    return getStyle().getIconGraphics("arrows", RectangularMask.class).getGraphic(index);
                }

                @Override
                public String toString() {
                    return "LobbyListHeaderDownArrow " + descend.getTitle();
                }

                @Override
                public UserEvent onMousePress(MouseEvent event) {
                    index = 3;
                    forceRender();
                    return null;
                }

                @Override
                public UserEvent onMouseRelease(MouseEvent event) {
                    lobbyNameHeader.clearArrows();
                    gameTypeHeader.clearArrows();
                    currentClientsHeader.clearArrows();
                    openSlotsHeader.clearArrows();
                    index = 5;
                    sortOption = descend;
                    forceRender();
                    return new UserEvent(this, UserEventType.Lobby_Sort, descend);
                }

            }

            private class UpArrow extends StyledGraphicObject {

                private final LobbySortOption ascend;
                private int index;

                public UpArrow(LobbySortOption ascend) {
                    super(1);
                    this.ascend = ascend;
                    index = 0;
                }

                @Override
                public Graphic getGraphic() {
                    return getStyle().getIconGraphics("arrows", RectangularMask.class).getGraphic(index);
                }

                @Override
                public String toString() {
                    return "LobbyListHeaderElementUpArrow " + ascend.getTitle();
                }

                @Override
                public UserEvent onMousePress(MouseEvent event) {
                    index = 2;
                    forceRender();
                    return null;
                }

                @Override
                public UserEvent onMouseRelease(MouseEvent event) {
                    lobbyNameHeader.clearArrows();
                    gameTypeHeader.clearArrows();
                    currentClientsHeader.clearArrows();
                    openSlotsHeader.clearArrows();
                    index = 4;
                    sortOption = ascend;
                    forceRender();
                    return new UserEvent(this, UserEventType.Lobby_Sort, ascend);
                }
            }
        }
    }

    private class LobbyListScroll extends ScrollingScreenRegion {

        private LobbyListScroll(int priority) {
            super(priority);
            setTransparency(true);
        }

        @Override
        protected void renderContents() {
            clear();
            int height = 1;
            for (Lobby lobby : lobbies) {
                if (lobby.getState() == LobbyState.Preparing) {
                    LobbyListScrollElement elt = new LobbyListScrollElement(0, lobby);
                    elt.setStyle(getStyle());
                    elt.setMask(lobbySize);
                    add(elt).setPosition(new Point(0, height));
                    height += lobbyHeight;
                }
            }
            setMask(new RectangularMask(new Dimension(LobbyListRegion.this.getMask().getWidth(),height)));
        }

        private class LobbyListScrollElement extends UIScreenRegion {

            private final TiledBackground background;

            private final TextLabel lobbyNameText;
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
                gameTypeText = new TextLabel(1, UIStyle.FONT_PARAGRAPH, lobby.getConfig().getLayoutName()) {
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
                add(gameTypeText).setClickable(this);
                add(currentClientsText).setClickable(this);
                add(openSlotsText).setClickable(this);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
            }

            @Override
            protected void renderContents() {
                // Calculate the positions of the tiles
                center(lobbyNameText).x = spacing;
                center(gameTypeText).x = lobbyNameText.getPosition().x + lobbyNameColumnWidth;
                center(currentClientsText).x = gameTypeText.getPosition().x + gameTypeColumnWidth;
                center(openSlotsText).x = currentClientsText.getPosition().x + currentClientsColumnWidth;
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                selected = this.lobby;
                return null;
            }

            @Override
            public UserEvent onMouseScroll(MouseWheelEvent event) {
                return LobbyListScroll.this.onMouseScroll(event);
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

        private LobbyListFooter(int priority) {
            super(priority);
            background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
                public String toString() {
                    return "ServerListItem Background";
                }
            };
            joinButton = new Button(1, "Join") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return (selected == null) ? null : new UserEvent(this, UserEventType.Lobby_Join, selected.getUsers().iterator().next());
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
        public UserEvent onMouseScroll(MouseWheelEvent event) {
            return scroll.onMouseScroll(event);
        }

        @Override
        public String toString() {
            return "LobbyListFooter";
        }
    }
}
