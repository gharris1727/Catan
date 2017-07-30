package catan.common.structure.lobby;

import catan.common.crypto.Username;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by greg on 1/10/16.
 * A set of lobbies, and maintained lists of lobbies by state.
 */
public class LobbyPool implements Serializable {

    private final Map<Username, Lobby> userMap;
    private final LinkedList<Lobby> ingameLobbies;
    private final LinkedList<Lobby> pregameLobbies;

    public LobbyPool() {
        userMap = new HashMap<>();
        pregameLobbies = new LinkedList<>();
        ingameLobbies = new LinkedList<>();
    }

    public void add(Username host, LobbyConfig config) {
        Lobby lobby = new Lobby(config);
        lobby.addPlayer(host);
        lobby.setState(LobbyState.Preparing);
        userMap.put(host, lobby);
        synchronized (pregameLobbies) {
            pregameLobbies.add(lobby);
        }
    }

    public void join(Username user, Username host) {
        Lobby lobby = userMap.get(host);
            lobby.addPlayer(user);
        userMap.put(user, lobby);
    }

    public void leave(Username user) {
        Lobby lobby = userMap.remove(user);
        if (lobby.getState() == LobbyState.Preparing)
            lobby.removePlayer(user);
        if (lobby.size() == 0)
            synchronized (pregameLobbies) {
                pregameLobbies.remove(lobby);
            }
    }

    public void connect(Username user) {
        userMap.get(user).connect(user);
    }

    public void disconnect(Username user) {
        Lobby lobby = userMap.get(user);
        lobby.disconnect(user);
        if (lobby.getConnectedPlayers().isEmpty())
            synchronized (ingameLobbies) {
                ingameLobbies.remove(lobby);
            }
    }

    public boolean isUserInLobby(Username username) {
        return userMap.containsKey(username);
    }

    public Lobby userGetLobby(Username username) {
        return userMap.get(username);
    }

    public void changeConfig(Username username, LobbyConfig config) {
        userMap.get(username).setConfig(config);
    }

    public void start(Username username) {
        Lobby lobby = userGetLobby(username);
        lobby.setState(LobbyState.InGame);
        synchronized (pregameLobbies) {
            pregameLobbies.remove(lobby);
        }
        synchronized (ingameLobbies) {
            ingameLobbies.add(lobby);
        }
    }

    public void finish(Username username) {
        Lobby lobby = userGetLobby(username);
        lobby.setState(LobbyState.Finished);
        synchronized (ingameLobbies) {
            ingameLobbies.remove(lobby);
        }
    }

    public Iterable<Lobby> getIngameLobbies() {
        return ingameLobbies;
    }

    public Iterable<Lobby> getPregameLobbies() {
        return pregameLobbies;
    }
}
