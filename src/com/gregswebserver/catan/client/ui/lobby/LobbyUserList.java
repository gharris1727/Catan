package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.lobby.Lobby;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by greg on 1/20/16.
 * List of users in a lobby, with operations to manipulate the lobby
 */
public class LobbyUserList extends UIScreenRegion {

    private static final int spacing = Client.staticConfig.getInt("catan.graphics.interface.inlobby.spacing");
    private static final int elementHeight = Client.staticConfig.getInt("catan.graphics.interface.inlobby.height");
    private static final RenderMask leaderButtonMask =
            new RoundedRectangularMask(Client.staticConfig.getDimension("catan.graphics.interface.inlobby.buttons.leader.size"));
    private static final RenderMask kickButtonMask =
            new RoundedRectangularMask(Client.staticConfig.getDimension("catan.graphics.interface.inlobby.buttons.kick.size"));

    private final Lobby lobby;
    private final Username local;

    private RenderMask listElementMask;

    public LobbyUserList(int priority, Lobby lobby, Username local) {
        super(priority);
        this.lobby = lobby;
        this.local = local;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        listElementMask = new RectangularMask(new Dimension(mask.getWidth(),elementHeight));
    }

    @Override
    protected void renderContents() {
        clear();
        int height = 0;
        for (Username user : lobby) {
            LobbyUserListElement elt = new LobbyUserListElement(1,user,local,lobby);
            add(elt).setPosition(new Point(0, height));
            elt.setStyle(getStyle());
            elt.setMask(listElementMask);
            height += elementHeight;
        }
    }

    public class LobbyUserListElement extends UIScreenRegion {

        private final TiledBackground background;
        private final TextLabel name;
        private final TextLabel owner;
        private final Button makeLeader;
        private final Button kickClient;

        public LobbyUserListElement(int priority, Username username, Username local, Lobby lobby) {
            super(priority);
            background = new TiledBackground(0, UIStyle.BACKGROUND_USERS) {
                @Override
                public String toString() {
                    return "LobbyUserListElementBackground";
                }
            };
            name = new TextLabel(1,UIStyle.FONT_HEADING, username.username) {
                @Override
                public String toString() {
                    return "LobbyUserListElementNameLabel";
                }
            };
            owner = new TextLabel(1, UIStyle.FONT_HEADING, "(Owner)") {
                @Override
                public String toString() {
                    return "LobbyUserListElementOwnerTag";
                }
            };
            makeLeader = new Button(2, "Make Leader") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.Lobby_Make_Leader, username);
                }

                @Override
                public String toString() {
                    return "LobbyUserListElementMakeLeaderButton";
                }
            };
            kickClient = new Button(2, "Kick") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return new UserEvent(this, UserEventType.Lobby_Kick, username);
                }

                @Override
                public String toString() {
                    return "LobbyUserListElementKickClientButton";
                }
            };
            add(background).setClickable(this);
            add(name).setClickable(this);
            if (username.equals(lobby.getOwner()))
                add(owner).setClickable(this);
            if (local.equals(lobby.getOwner())) {
                add(makeLeader);
                add(kickClient);
            }
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
            makeLeader.setMask(leaderButtonMask);
            kickClient.setMask(kickButtonMask);
        }

        @Override
        protected void renderContents() {
            Point namePos = name.getPosition();
            namePos.setLocation(getCenteredPosition(name.getGraphic().getMask()));
            namePos.x = spacing;
            Point kickPos = kickClient.getPosition();
            kickPos.setLocation(getCenteredPosition(kickClient.getMask()));
            kickPos.x = getMask().getWidth() - kickClient.getMask().getWidth() - spacing;
            Point leaderPos = makeLeader.getPosition();
            leaderPos.setLocation(getCenteredPosition(makeLeader.getMask()));
            leaderPos.x = kickPos.x - makeLeader.getMask().getWidth() - spacing;
            Point ownerPos = owner.getPosition();
            ownerPos.setLocation(getCenteredPosition(owner.getGraphic().getMask()));
            ownerPos.x = leaderPos.x - owner.getGraphic().getMask().getWidth() - spacing;
        }

        @Override
        public String toString() {
            return "LobbyUserListElement";
        }
    }

    @Override
    public String toString() {
        return "LobbyUserList";
    }
}
