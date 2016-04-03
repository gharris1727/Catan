package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.structure.lobby.Lobby;
import com.gregswebserver.catan.common.structure.lobby.LobbyComparator;
import com.gregswebserver.catan.common.structure.lobby.LobbySortOption;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.TreeSet;

/**
 * Created by greg on 1/10/16.
 * List of lobbies that the client can join, or create one themselves.
 */
public class LobbyListRegion extends ConfigurableScreenRegion {

    private int headerHeight;
    private int lobbyHeight;
    private int footerHeight;
    private int spacing;

    private int gameTypeColumnWidth;
    private int currentClientsColumnWidth;
    private int openSlotsColumnWidth;
    private int arrowWidth;

    private int lobbyNameColumnWidth;
    private RenderMask lobbySize;

    private final LobbyListHeader header;
    private final LobbyListScrollContainer container;
    private final LobbyListFooter footer;

    private final Iterable<Lobby> lobbies;
    private LobbySortOption sortOption;
    private Lobby selected;

    public LobbyListRegion(int priority, String configKey, Iterable<Lobby> lobbies) {
        super(priority, configKey);
        //Store instance information
        this.lobbies = lobbies;
        sortOption = LobbySortOption.Lobby_Name_Asc;
        //Create the child regions
        header = new LobbyListHeader();
        footer = new LobbyListFooter();
        container = new LobbyListScrollContainer(new LobbyListScroll());
        //Add everything to the screen
        add(header);
        add(footer);
        add(container);
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
        container.setPosition(new Point(0,headerHeight));
        footer.setPosition(new Point(0,height-footerHeight));
        //Resize the windows
        container.setMask(new RectangularMask(new Dimension(width,windowHeight)));
        header.setMask(new RectangularMask(new Dimension(width,headerHeight)));
        footer.setMask(new RectangularMask(new Dimension(width,footerHeight)));
        lobbySize = new RectangularMask(new Dimension(width,lobbyHeight));
        container.setInsets(new Insets(0,0,0,0));
    }

    @Override
    public void loadConfig(UIConfig config) {
        headerHeight = config.getLayout().getInt("header.height");
        lobbyHeight = config.getLayout().getInt("lobby.height");
        footerHeight = config.getLayout().getInt("footer.height");
        spacing = config.getLayout().getInt("spacing");
        gameTypeColumnWidth = config.getLayout().getInt("header.type.width");
        currentClientsColumnWidth = config.getLayout().getInt("header.current.width");
        openSlotsColumnWidth = config.getLayout().getInt("header.open.width");
        arrowWidth = config.getLayout().getInt("arrows.width");
    }

    @Override
    public UserEvent onMouseScroll(MouseWheelEvent event) {
        container.getScroll().scroll(0,event.getScrollAmount());
        return null;
    }

    @Override
    public String toString() {
        return "LobbyListRegion";
    }

    public void update() {
        container.update();
    }

    private class LobbyListHeader extends ConfigurableScreenRegion {

        private final LobbyListHeaderElement lobbyNameHeader;
        private final LobbyListHeaderElement gameTypeHeader;
        private final LobbyListHeaderElement currentClientsHeader;
        private final LobbyListHeaderElement openSlotsHeader;

        private LobbyListHeader() {
            super(2, "header");
            lobbyNameHeader = new LobbyListHeaderElement(
                    LobbySortOption.Lobby_Name_Asc, LobbySortOption.Lobby_Name_Desc);
            gameTypeHeader = new LobbyListHeaderElement(
                    LobbySortOption.Game_Type_Asc, LobbySortOption.Game_Type_Desc);
            currentClientsHeader = new LobbyListHeaderElement(
                    LobbySortOption.Num_Clients_Asc, LobbySortOption.Num_Clients_Desc);
            openSlotsHeader = new LobbyListHeaderElement(
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

        private class LobbyListHeaderElement extends ConfigurableScreenRegion {

            private final TiledBackground background;
            private final TextLabel labelGraphic;
            private final UpArrow upArrow;
            private final DownArrow downArrow;

            private LobbyListHeaderElement(LobbySortOption ascend, LobbySortOption descend) {
                super(0, "element");
                background = new EdgedTiledBackground(0, "background");
                labelGraphic = new TextLabel(1, "label", ascend.getTitle());
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
                return container.onMouseScroll(event);
            }

            private void clearArrows() {
                upArrow.index = 0;
                downArrow.index = 1;
            }

            @Override
            public String toString() {
                return "LobbyListHeaderElement";
            }

            private class DownArrow extends ConfigurableGraphicObject {

                private final LobbySortOption descend;
                private int index;
                private GraphicSet graphics;

                private DownArrow(LobbySortOption descend) {
                    super(1, "down");
                    this.descend = descend;
                    index = 1;
                }

                @Override
                public void loadConfig(UIConfig config) {
                    graphics = config.getIconGraphics("arrows");
                    update();
                }

                public void update() {
                    setGraphic(graphics.getGraphic(index));
                }

                @Override
                public String toString() {
                    return "LobbyListHeaderDownArrow " + descend.getTitle();
                }

                @Override
                public UserEvent onMousePress(MouseEvent event) {
                    index = 3;
                    update();
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
                    update();
                    return null;
                }

            }

            private class UpArrow extends ConfigurableGraphicObject {

                private final LobbySortOption ascend;
                private int index;
                private GraphicSet graphics;

                private UpArrow(LobbySortOption ascend) {
                    super(1, "up");
                    this.ascend = ascend;
                    index = 0;
                }

                @Override
                public void loadConfig(UIConfig config) {
                    graphics = config.getIconGraphics("arrows");
                    update();
                }

                public void update() {
                    setGraphic(graphics.getGraphic(index));
                }

                @Override
                public String toString() {
                    return "LobbyListHeaderElementUpArrow " + ascend.getTitle();
                }

                @Override
                public UserEvent onMousePress(MouseEvent event) {
                    index = 2;
                    update();
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
                    update();
                    container.update();
                    return null;
                }
            }
        }
    }

    private class LobbyListScrollContainer extends ScrollingScreenContainer {

        private final TiledBackground background;

        private LobbyListScrollContainer(ScrollingScreenRegion scroll) {
            super(1, "container", scroll);
            background = new TiledBackground(0, "background");
            add(background).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            super.resizeContents(mask);
            background.setMask(mask);
        }

        @Override
        public String toString() {
            return "LobbyListScrollContainer";
        }
    }

    private class LobbyListScroll extends ScrollingScreenRegion {

        private LobbyListScroll() {
            super(1, "scroll");
        }

        @Override
        protected void renderContents() {
            clear();
            TreeSet<Lobby> sorted = new TreeSet<>(new LobbyComparator(sortOption));
            synchronized (lobbies) {
                for (Lobby lobby : lobbies)
                    sorted.add(lobby);
            }
            int height = 0;
            for (Lobby lobby : sorted) {
                LobbyListScrollElement elt = new LobbyListScrollElement(lobby);
                elt.setConfig(getConfig());
                elt.setMask(lobbySize);
                add(elt).setPosition(new Point(0, height));
                height += lobbyHeight;
            }
            setMask(new RectangularMask(new Dimension(LobbyListRegion.this.getMask().getWidth(),height)));
        }

        private class LobbyListScrollElement extends ConfigurableScreenRegion {

            private final TiledBackground background;

            private final TextLabel lobbyNameText;
            private final TextLabel gameTypeText;
            private final TextLabel currentClientsText;
            private final TextLabel openSlotsText;

            private final Lobby lobby;

            private LobbyListScrollElement(Lobby lobby) {
                super(0, "element");
                this.lobby = lobby;
                background = new TiledBackground(0, "background");
                lobbyNameText = new TextLabel(1, "name", lobby.getConfig().getLobbyName());
                gameTypeText = new TextLabel(1, "type", lobby.getConfig().getLayoutName());
                currentClientsText = new TextLabel(1, "current", "" + lobby.size());
                openSlotsText = new TextLabel(1, "open", "" + (lobby.getConfig().getMaxPlayers() - lobby.size()));
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

    private class LobbyListFooter extends ConfigurableScreenRegion {

        private final TiledBackground background;
        private final Button joinButton;

        private LobbyListFooter() {
            super(2, "footer");
            background = new EdgedTiledBackground(0, "background");
            joinButton = new Button(1, "join", "Join") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return (selected == null) ? null : new UserEvent(this, UserEventType.Lobby_Join, selected.getPlayer());
                }
                public String toString() {
                    return "LobbyListJoinButton";
                }
            };
            //Add objects to the screen.
            add(background);
            add(joinButton);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        @Override
        public UserEvent onMouseScroll(MouseWheelEvent event) {
            return container.onMouseScroll(event);
        }

        @Override
        public String toString() {
            return "LobbyListFooter";
        }
    }
}
