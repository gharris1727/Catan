package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventListener;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.common.structure.lobby.LobbyConfig;

/**
 * Created by greg on 1/23/16.
 * Panel with editable lobby settings.
 */
public class LobbySettings extends ConfigurableScreenRegion {

    private int spacing;

    private final TiledBackground background;
    private final TextBox name;
    private final TextBox type;
    private final TextBox generator;
    private final TextBox ruleset;
    private final TextBox clients;

    private LobbyConfig config;

    public LobbySettings() {
        super("LobbySettings", 1, "settings");
        //Create sub-regions
        background = new EdgedTiledBackground();
        name = new TextBox("LobbyNameBox", 1, "name", "Lobby Name", false) {
            @Override
            public void onAccept(UserEventListener listener) {
                LobbyConfig newConfig = new LobbyConfig(
                        this.getText(),
                        config.getLayoutName(),
                        config.getGeneratorName(),
                        config.getRulesetName(),
                        config.getMaxPlayers());
                listener.onUserEvent(new UserEvent(this, UserEventType.Lobby_Edit, newConfig));
            }
        };
        type = new TextBox("GameTypeBox", 1, "type", "Game Type", false) {
            @Override
            public void onAccept(UserEventListener listener) {
                LobbyConfig newConfig = new LobbyConfig(
                        config.getLobbyName(),
                        this.getText(),
                        config.getGeneratorName(),
                        config.getRulesetName(),
                        config.getMaxPlayers());
                listener.onUserEvent(new UserEvent(this, UserEventType.Lobby_Edit, newConfig));
            }
        };
        generator = new TextBox("MapGeneratorBox", 1, "generator", "Generator", false) {
            @Override
            public void onAccept(UserEventListener listener) {
                LobbyConfig newConfig = new LobbyConfig(
                        config.getLobbyName(),
                        config.getLayoutName(),
                        this.getText(),
                        config.getRulesetName(),
                        config.getMaxPlayers());
                listener.onUserEvent(new UserEvent(this, UserEventType.Lobby_Edit, newConfig));
            }
        };
        ruleset = new TextBox("RulsesetBox", 1, "ruleset", "RuleSet", false) {
            @Override
            public void onAccept(UserEventListener listener) {
                LobbyConfig newConfig = new LobbyConfig(
                        config.getLobbyName(),
                        config.getLayoutName(),
                        config.getGeneratorName(),
                        this.getText(),
                        config.getMaxPlayers());
                listener.onUserEvent(new UserEvent(this, UserEventType.Lobby_Edit, newConfig));
            }
        };
        clients = new TextBox("ClientLimitBox", 1, "clients", "# Clients", false) {
            @Override
            public void onAccept(UserEventListener listener) {
                LobbyConfig newConfig = new LobbyConfig(
                        config.getLobbyName(),
                        config.getLayoutName(),
                        config.getGeneratorName(),
                        config.getRulesetName(),
                        this.getInt());
                listener.onUserEvent(new UserEvent(this, UserEventType.Lobby_Edit, newConfig));
            }
        };
        //Add everything to the screen.
        add(background).setClickable(this);
        add(name);
        add(type);
        add(generator);
        add(ruleset);
        add(clients);
    }

    @Override
    public void loadConfig(UIConfig config) {
        spacing = config.getLayout().getInt("spacing");
    }

    public void setLobbyConfig(LobbyConfig config) {
        this.config = config;
        name.setText(config.getLobbyName());
        type.setText(config.getLayoutName());
        generator.setText(config.getGeneratorName());
        ruleset.setText(config.getRulesetName());
        clients.setText(""+config.getMaxPlayers());
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        center(name).y = spacing;
        center(type).y = name.getPosition().y + name.getMask().getHeight() + spacing;
        center(generator).y = type.getPosition().y + type.getMask().getHeight() + spacing;
        center(ruleset).y = generator.getPosition().y + generator.getMask().getHeight() + spacing;
        center(clients).y = ruleset.getPosition().y + ruleset.getMask().getHeight() + spacing;
    }
}
