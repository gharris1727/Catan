package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.common.structure.lobby.LobbyConfig;

/**
 * Created by greg on 1/23/16.
 * Panel with editable lobby settings.
 */
public class LobbySettings extends UIScreen {

    private final RenderMask nameSize;
    private final RenderMask typeSize;
    private final RenderMask generatorSize;
    private final RenderMask rulesetSize;
    private final RenderMask clientsSize;
    private final int spacing;

    private final TiledBackground background;
    private final TextBox name;
    private final TextBox type;
    private final TextBox generator;
    private final TextBox ruleset;
    private final TextBox clients;

    private LobbyConfig config;

    public LobbySettings(LobbyScreen parent) {
        super(1, parent, "settings");
        //Load layout information.
        nameSize = new RectangularMask(getLayout().getDimension("name"));
        typeSize = new RectangularMask(getLayout().getDimension("type"));
        generatorSize = new RectangularMask(getLayout().getDimension("generator"));
        rulesetSize = new RectangularMask(getLayout().getDimension("ruleset"));
        clientsSize = new RectangularMask(getLayout().getDimension("clients"));
        spacing = getLayout().getInt("spacing");
        //Create sub-regions
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE);
        name = new TextBox(1, "Lobby Name") {
            @Override
            public UserEvent onAccept() {
                LobbyConfig newConfig = new LobbyConfig(
                        this.getText(),
                        config.getLayoutName(),
                        config.getGeneratorName(),
                        config.getRulesetName(),
                        config.getMaxPlayers());
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
                        config.getLobbyName(),
                        this.getText(),
                        config.getGeneratorName(),
                        config.getRulesetName(),
                        config.getMaxPlayers());
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
                        config.getLobbyName(),
                        config.getLayoutName(),
                        this.getText(),
                        config.getRulesetName(),
                        config.getMaxPlayers());
                return new UserEvent(this, UserEventType.Lobby_Edit, newConfig);
            }

            @Override
            public String toString() {
                return "LobbySettingsGeneratorTextBox";
            }
        };
        ruleset = new TextBox(1, "RuleSet") {
            @Override
            public UserEvent onAccept() {
                LobbyConfig newConfig = new LobbyConfig(
                        config.getLobbyName(),
                        config.getLayoutName(),
                        config.getGeneratorName(),
                        this.getText(),
                        config.getMaxPlayers());
                return new UserEvent(this, UserEventType.Lobby_Edit, newConfig);
            }

            @Override
            public String toString() {
                return "LobbySettingsRuleSetTextBox";
            }
        };
        clients = new TextBox(1, "# Clients") {
            @Override
            public UserEvent onAccept() {
                LobbyConfig newConfig = new LobbyConfig(
                        config.getLobbyName(),
                        config.getLayoutName(),
                        config.getGeneratorName(),
                        config.getRulesetName(),
                        this.getInt());
                return new UserEvent(this, UserEventType.Lobby_Edit, newConfig);
            }

            @Override
            public String toString() {
                return "LobbySettingsClientsTextBox";
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
        name.setMask(nameSize);
        type.setMask(typeSize);
        generator.setMask(generatorSize);
        ruleset.setMask(rulesetSize);
        clients.setMask(clientsSize);
    }

    @Override
    protected void renderContents() {
        center(name).y = spacing;
        center(type).y = name.getPosition().y + nameSize.getHeight() + spacing;
        center(generator).y = type.getPosition().y + typeSize.getHeight() + spacing;
        center(ruleset).y = generator.getPosition().y + generatorSize.getHeight() + spacing;
        center(clients).y = ruleset.getPosition().y + rulesetSize.getHeight() + spacing;
    }

    @Override
    public String toString() {
        return "LobbySettings";
    }
}
