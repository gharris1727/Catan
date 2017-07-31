package catan.common.structure.lobby;

import catan.common.crypto.Username;
import catan.common.game.gameplay.allocator.PreferenceTeamAllocator;
import catan.common.game.gameplay.generator.BoardGenerator;
import catan.common.game.gameplay.generator.CopyGenerator;
import catan.common.game.gameplay.generator.RandomBoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.scoring.rules.GameRules;
import catan.common.resources.BoardLayoutInfo;
import catan.common.resources.GameRulesInfo;
import catan.common.resources.ResourceLoadException;
import catan.common.resources.ResourceLoader;
import catan.common.structure.game.GameSettings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Greg on 8/22/2014.
 * A lobby containing information relevant to organizing groups of users setting up for a game.
 */
public class Lobby implements Serializable {

    private final Map<Username, LobbyUser> users;
    private final Set<Username> connected;
    private int gameID;
    private LobbyState state;
    private LobbyConfig config;

    public Lobby(LobbyConfig config) {
        users = new HashMap<>(config.getMaxPlayers());
        connected = new HashSet<>();
        setConfig(config);
    }

    public void addPlayer(Username username) {
        users.put(username, new LobbyUser());
    }

    public void removePlayer(Username username) {
        users.remove(username);
    }

    public void connect(Username username) {
        connected.add(username);
    }

    public void disconnect(Username username) {
        connected.remove(username);
    }

    public Set<Username> getPlayers() {
        return users.keySet();
    }

    public LobbyConfig getConfig() {
        return config;
    }

    public final void setConfig(LobbyConfig config) {
        this.config = config;
    }

    public int size() {
        return users.size();
    }

    public GameSettings getGameSettings() {
        //TODO: error handling and reporting errors to the user.

        //We need to construct the parts of a GameSettings object.
        BoardLayout boardLayout;
        long seed = System.nanoTime();
        try {
            BoardLayoutInfo info;
            //TODO: put the regex stuff somewhere so that we can use it to validate when saving the game.
            //Static map file name, maybe having .properties appended (ignored).
            Matcher staticConfigName = Pattern.compile("^(?<name>[a-zA-Z]+)(?:[.]properties)??$").matcher(config.getLayoutName());
            // Each of these options are mutually exclusive, and can fail for whatever reason.
            if (staticConfigName.find())
                info = new BoardLayoutInfo(staticConfigName.group("name"));
            else
                info = new BoardLayoutInfo("default");
            boardLayout = ResourceLoader.getBoardLayout(info);
        } catch (ResourceLoadException ignored) {
            // Fall back to the text seeded map.
            boardLayout = ResourceLoader.getBoardLayout(new BoardLayoutInfo("default"));
        }

        //We really don't have an alternative than to spit the error back up.
        GameRules rules = ResourceLoader.getGameRuleSet(new GameRulesInfo(config.getRulesetName()));

        //Keyword 'copy' for selecting the copy generator
        Matcher copyGenerator = Pattern.compile("^[Cc]opy$").matcher(config.getGeneratorName());
        //Choose the generator to use.
        BoardGenerator boardGenerator = copyGenerator.find() ? new CopyGenerator() : new RandomBoardGenerator();

        PreferenceTeamAllocator players = new PreferenceTeamAllocator();

        for (Entry<Username, LobbyUser> user : users.entrySet())
            players.addPreference(user.getKey(), user.getValue().getTeamColor());

        return new GameSettings(seed, boardLayout, boardGenerator, rules, players);
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public String toString() {
        return "Lobby " + config;
    }

    public void setState(LobbyState state) {
        this.state = state;
    }

    public LobbyState getState() {
        return state;
    }

    public Set<Username> getConnectedPlayers() {
        return connected;
    }

    public Username getPlayer() {
        return users.keySet().iterator().next();
    }
}
