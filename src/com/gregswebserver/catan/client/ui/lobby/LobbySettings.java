package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextBox;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.lobby.LobbyConfig;

/**
 * Created by greg on 1/23/16.
 * Panel with editable lobby settings.
 */
class LobbySettings extends UIScreenRegion {

    private static final RenderMask nameSize = new RectangularMask(
            Client.staticConfig.getDimension("catan.graphics.interface.inlobby.settings.name"));
    private static final RenderMask typeSize = new RectangularMask(
            Client.staticConfig.getDimension("catan.graphics.interface.inlobby.settings.type"));
    private static final RenderMask generatorSize = new RectangularMask(
            Client.staticConfig.getDimension("catan.graphics.interface.inlobby.settings.generator"));
    private static final RenderMask clientsSize = new RectangularMask(
            Client.staticConfig.getDimension("catan.graphics.interface.inlobby.settings.clients"));
    private static final int spacing = Client.staticConfig.getInt("catan.graphics.interface.inlobby.settings.spacing");

    private final TiledBackground background;
    private final TextBox name;
    private final TextBox type;
    private final TextBox generator;
    private final TextBox clients;

    private LobbyConfig config;

    public LobbySettings(int priority) {
        super(priority);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
            @Override
            public String toString() {
                return "LobbySidebarBackground";
            }
        };
        name = new TextBox(1, "Lobby Name") {
            @Override
            public UserEvent onAccept() {
                LobbyConfig newConfig = new LobbyConfig(
                        this.getText(),
                        LobbySettings.this.config.getLayoutName(),
                        LobbySettings.this.config.getGeneratorName(),
                        LobbySettings.this.config.getMaxPlayers());
                return new UserEvent(this, UserEventType.Lobby_Edit, newConfig);
            }

            @Override
            public String toString() {
                return "LobbySettingsNameTextBox";
            }
        };
        type = new TextBox(1, "Game Type") {
            @Override
            public UserEvent onAccept() {
                LobbyConfig newConfig = new LobbyConfig(
                        LobbySettings.this.config.getLobbyName(),
                        this.getText(),
                        LobbySettings.this.config.getGeneratorName(),
                        LobbySettings.this.config.getMaxPlayers());
                return new UserEvent(this, UserEventType.Lobby_Edit, newConfig);
            }

            @Override
            public String toString() {
                return "LobbySettingsGameTypeTextBox";
            }
        };
        generator = new TextBox(1, "Generator") {
            @Override
            public UserEvent onAccept() {
                LobbyConfig newConfig = new LobbyConfig(
                        LobbySettings.this.config.getLobbyName(),
                        LobbySettings.this.config.getLayoutName(),
                        this.getText(),
                        LobbySettings.this.config.getMaxPlayers());
                return new UserEvent(this, UserEventType.Lobby_Edit, newConfig);
            }

            @Override
            public String toString() {
                return "LobbySettingsGeneratorTextBox";
            }
        };
        clients = new TextBox(1, "# Clients") {
            @Override
            public UserEvent onAccept() {
                LobbyConfig newConfig = new LobbyConfig(
                        LobbySettings.this.config.getLobbyName(),
                        LobbySettings.this.config.getLayoutName(),
                        LobbySettings.this.config.getGeneratorName(),
                        this.getInt());
                return new UserEvent(this, UserEventType.Lobby_Edit, newConfig);
            }

            @Override
            public String toString() {
                return "LobbySettingsClientsTextBox";
            }
        };
        add(background).setClickable(this);
        add(name);
        add(type);
        add(generator);
        add(clients);
    }

    public void setLobbyConfig(LobbyConfig config) {
        this.config = config;
        name.setText(config.getLobbyName());
        type.setText(config.getLayoutName());
        generator.setText(config.getGeneratorName());
        clients.setText(""+config.getMaxPlayers());
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        name.setMask(nameSize);
        type.setMask(typeSize);
        generator.setMask(generatorSize);
        clients.setMask(clientsSize);
    }

    @Override
    protected void renderContents() {
        center(name).y = spacing;
        center(type).y = name.getPosition().y + nameSize.getHeight() + spacing;
        center(generator).y = type.getPosition().y + typeSize.getHeight() + spacing;
        center(clients).y = generator.getPosition().y + generatorSize.getHeight() + spacing;
    }

    @Override
    public String toString() {
        return "LobbySettings";
    }
}
