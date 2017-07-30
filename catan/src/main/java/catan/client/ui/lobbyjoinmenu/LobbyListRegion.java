package catan.client.ui.lobbyjoinmenu;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.Button;
import catan.client.graphics.ui.*;
import catan.client.input.UserEvent;
import catan.client.input.UserEventListener;
import catan.client.input.UserEventType;
import catan.common.resources.GraphicSet;
import catan.common.structure.lobby.Lobby;
import catan.common.structure.lobby.LobbyComparator;
import catan.common.structure.lobby.LobbySortOption;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Set;
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

    private final LobbyListHeader header;
    private final LobbyList list;
    private final LobbyListScrollContainer container;
    private final LobbyListFooter footer;

    private final Iterable<Lobby> lobbies;
    private LobbySortOption sortOption;
    private Lobby selected;

    public LobbyListRegion(int priority, String configKey, Iterable<Lobby> lobbies) {
        super("LobbyList", priority, configKey);
        //Store instance information
        this.lobbies = lobbies;
        sortOption = LobbySortOption.Lobby_Name_Asc;
        //Create the child regions
        header = new LobbyListHeader();
        footer = new LobbyListFooter();
        list = new LobbyList();
        container = new LobbyListScrollContainer(list);
        //Add everything to the screen
        add(header);
        add(footer);
        add(container);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
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
    public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
        container.getScroll().scroll(0,event.getScrollAmount());
    }

    public void update() {
        container.update();
    }

    @Override
    protected void renderContents() {
        //Get the new overall size of the window.
        int width = getMask().getWidth();
        int height = getMask().getHeight();
        //Calculate intermediate dimensions
        int windowHeight = (height - headerHeight - footerHeight);
        lobbyNameColumnWidth = width - openSlotsColumnWidth - currentClientsColumnWidth - gameTypeColumnWidth;
        //Set the window locations
        header.setPosition(new Point());
        container.setPosition(new Point(0,headerHeight));
        footer.setPosition(new Point(0,height-footerHeight));
        //Resize the windows
        container.setMask(new RectangularMask(new Dimension(width,windowHeight)));
        header.setMask(new RectangularMask(new Dimension(width,headerHeight)));
        footer.setMask(new RectangularMask(new Dimension(width,footerHeight)));
        list.setElementSize(new RectangularMask(new Dimension(width,lobbyHeight)));
        container.setInsets(new Insets(0,0,0,0));
    }

    private class LobbyListHeader extends ConfigurableScreenRegion {

        private final LobbyListHeaderElement lobbyNameHeader;
        private final LobbyListHeaderElement gameTypeHeader;
        private final LobbyListHeaderElement currentClientsHeader;
        private final LobbyListHeaderElement openSlotsHeader;

        private LobbyListHeader() {
            super("Header", 2, "header");
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

        private class LobbyListHeaderElement extends ConfigurableScreenRegion {

            private final TiledBackground background;
            private final TextLabel labelGraphic;
            private final UpArrow upArrow;
            private final DownArrow downArrow;

            private LobbyListHeaderElement(LobbySortOption ascend, LobbySortOption descend) {
                super("HeaderElement", 0, "element");
                background = new EdgedTiledBackground();
                labelGraphic = new TextLabel("Label", 1, "label", ascend.getTitle());
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
            public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
                container.onMouseScroll(listener, event);
            }

            private void clearArrows() {
                upArrow.index = 0;
                downArrow.index = 1;
            }

            private class DownArrow extends ConfigurableGraphicObject {

                private final LobbySortOption descend;
                private int index;
                private GraphicSet graphics;

                private DownArrow(LobbySortOption descend) {
                    super("DownArrow", 1, "down");
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
                public void onMousePress(UserEventListener listener, MouseEvent event) {
                    index = 3;
                    update();
                }

                @Override
                public void onMouseRelease(UserEventListener listener, MouseEvent event) {
                    lobbyNameHeader.clearArrows();
                    gameTypeHeader.clearArrows();
                    currentClientsHeader.clearArrows();
                    openSlotsHeader.clearArrows();
                    index = 5;
                    sortOption = descend;
                    update();
                }

            }

            private class UpArrow extends ConfigurableGraphicObject {

                private final LobbySortOption ascend;
                private int index;
                private GraphicSet graphics;

                private UpArrow(LobbySortOption ascend) {
                    super("UpArrow", 1, "up");
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
                public void onMousePress(UserEventListener listener, MouseEvent event) {
                    index = 2;
                    update();
                }

                @Override
                public void onMouseRelease(UserEventListener listener, MouseEvent event) {
                    lobbyNameHeader.clearArrows();
                    gameTypeHeader.clearArrows();
                    currentClientsHeader.clearArrows();
                    openSlotsHeader.clearArrows();
                    index = 4;
                    sortOption = ascend;
                    update();
                    container.update();
                }
            }
        }
    }

    private class LobbyListScrollContainer extends ScrollingScreenContainer {

        private final TiledBackground background;

        private LobbyListScrollContainer(ScrollingScreenRegion scroll) {
            super("LobbyList", 1, scroll);
            background = new TiledBackground();
            add(background).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            super.resizeContents(mask);
            background.setMask(mask);
        }
    }

    private class LobbyList extends ScrollingList {

        private LobbyList() {
            super("Scroll", 1, "list");
        }

        @Override
        public void update() {
            forceRender();
        }

        @Override
        protected void renderContents() {
            clear();
            Set<Lobby> sorted = new TreeSet<>(new LobbyComparator(sortOption));
            synchronized (lobbies) {
                for (Lobby lobby : lobbies)
                    sorted.add(lobby);
            }
            for (Lobby lobby : sorted)
                add(new Element(lobby));
            super.renderContents();
        }

        private class Element extends ConfigurableScreenRegion {

            private final TiledBackground background;

            private final TextLabel lobbyNameText;
            private final TextLabel gameTypeText;
            private final TextLabel currentClientsText;
            private final TextLabel openSlotsText;

            private final Lobby lobby;

            private Element(Lobby lobby) {
                super("ListScrollElement", 0, "element");
                this.lobby = lobby;
                background = new TiledBackground();
                lobbyNameText = new TextLabel("LobbyName", 1, "name", lobby.getConfig().getLobbyName());
                gameTypeText = new TextLabel("GameType", 1, "type", lobby.getConfig().getLayoutName());
                currentClientsText = new TextLabel("CurrentClients", 1, "current", String.valueOf(lobby.size()));
                openSlotsText = new TextLabel("OpenSlots", 1, "open", String.valueOf(lobby.getConfig().getMaxPlayers() - lobby.size()));
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
            public void onMouseClick(UserEventListener listener, MouseEvent event) {
                selected = lobby;
            }

            @Override
            public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
                LobbyList.this.onMouseScroll(listener, event);
            }
        }
    }

    private class LobbyListFooter extends ConfigurableScreenRegion {

        private final TiledBackground background;
        private final Button joinButton;

        private LobbyListFooter() {
            super("Footer", 2, "footer");
            background = new EdgedTiledBackground();
            joinButton = new Button("JoinButton", 1, "join", "Join") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    if (selected != null)
                        listener.onUserEvent(new UserEvent(this, UserEventType.Lobby_Join, selected.getPlayer()));
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
        public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
            container.onMouseScroll(listener, event);
        }
    }
}
