package com.gregswebserver.catan.common.structure;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.game.GameSettings;
import com.gregswebserver.catan.common.game.board.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.GameRules;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.resources.BoardLayoutInfo;
import com.gregswebserver.catan.common.resources.ResourceLoadException;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Greg on 12/29/2014.
 * A set of settings that are configurable on a per-lobby
 */
public class LobbyConfig extends EventPayload {

    private final String lobbyName;
    private final String layoutName;
    private final String generatorName;
    private final int maxPlayers;

    public LobbyConfig(String lobbyName, String layoutName, String generatorName, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.layoutName = layoutName;
        this.generatorName = generatorName;
        this.maxPlayers = maxPlayers;
    }

    public LobbyConfig(Username username) {
        lobbyName = username.username + "'s Game";
        layoutName = "base";
        generatorName = "random";
        maxPlayers = 4;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameSettings getGameSettings() {
        //We need to construct the parts of a GameSettings object.
        BoardLayout layout;
        BoardGenerator generator = null;
        GameRules rules = null;
        try {
            BoardLayoutInfo info = null;
            //Static map file name, maybe having .properties appended (ignored).
            Matcher staticConfigName = Pattern.compile("^(?<name>[a-zA-Z]+)(?:\\.properties)??").matcher(layoutName);
            //Dynamic numeric seed that consists of "dynamic.<number>" where the number is the actual seed.
            Matcher dynamicNumericSeed = Pattern.compile("^\\d+$").matcher(layoutName);
            // Each of these options are mutually exclusive, and can fail for whatever reason.
            if (staticConfigName.find())
                info = new BoardLayoutInfo(staticConfigName.group("name"));
            else if (dynamicNumericSeed.find()) // Number specifies a manual seed.
                info = new BoardLayoutInfo(Long.parseLong(layoutName));
            else if ("".equals(layoutName)) // Empty string triggers random seed.
                info = new BoardLayoutInfo(System.nanoTime());
            layout = ResourceLoader.getBoardLayout(info);
        } catch (ResourceLoadException e) {
            // Fall back to the text seeded map.
            layout = ResourceLoader.getBoardLayout(new BoardLayoutInfo(layoutName.hashCode()));
        }

        return new GameSettings(layout, generator, rules);
    }
}
