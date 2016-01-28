package com.gregswebserver.catan.common.structure;

import com.gregswebserver.catan.common.crypto.Username;

import java.io.Serializable;
import java.util.*;

/**
 * Created by greg on 1/10/16.
 * List of lobby objects that are sortable by multiple different
 */
public class LobbyPool implements Iterable<Lobby>, Serializable{

    private final Map<Username, Lobby> userMap;
    private final List<Lobby> list;
    private LobbySortOption sortOption;

    public LobbyPool() {
        userMap = new HashMap<>();
        list = new LinkedList<>();
        sortOption = LobbySortOption.Lobby_Name_Asc;
    }

    public void add(Username host, LobbyConfig config) {
        Lobby lobby = new Lobby(config);
        lobby.add(host);
        userMap.put(host, lobby);
        //Concurrent write, but inserting on the head of the list should be safe.
        list.add(0, lobby);
    }

    public void join(Username user, Username host) {
        Lobby lobby = userMap.get(host);
        lobby.add(user);
        userMap.put(user, lobby);
    }

    public void leave(Username user) {
        Lobby lobby = userMap.remove(user);
        lobby.remove(user);
        //Concurrent write, removing may cause issues.
        if (lobby.size() == 0) {
            list.remove(lobby);
        }
    }

    public boolean userInLobby(Username username) {
        return userMap.containsKey(username);
    }

    public Lobby userGetLobby(Username username) {
        return userMap.get(username);
    }

    public void changeConfig(Username username, LobbyConfig config) {
        userMap.get(username).setConfig(config);
    }

    private class LobbyComparator implements Comparator<Lobby> {

        private final LobbySortOption sortOption;

        private LobbyComparator(LobbySortOption sortOption) {
            this.sortOption = sortOption;
        }

        @Override
        public int compare(Lobby f, Lobby s) {
            switch (sortOption) {
                case Lobby_Name_Asc:
                    return f.getConfig().getLobbyName().compareTo(s.getConfig().getLobbyName());
                case Lobby_Name_Desc:
                    return -f.getConfig().getLobbyName().compareTo(s.getConfig().getLobbyName());
                case Game_Type_Asc:
                    return f.getConfig().getLayoutName().compareTo(s.getConfig().getLayoutName());
                case Game_Type_Desc:
                    return -f.getConfig().getLayoutName().compareTo(s.getConfig().getLayoutName());
                case Num_Clients_Asc:
                    return f.size() - s.size();
                case Num_Clients_Desc:
                    return s.size() - f.size();
                case Open_Spaces_Asc:
                    return f.getConfig().getMaxPlayers() - f.size() - s.getConfig().getMaxPlayers() + s.size();
                case Open_Spaces_Desc:
                    return s.getConfig().getMaxPlayers() - s.size() - f.getConfig().getMaxPlayers() + f.size();
                default:
                    return 0;
            }
        }

    }

    public void sort(LobbySortOption sortOption) {
        this.sortOption = sortOption;
        sort();
    }

    public void sort() {
        list.sort(new LobbyComparator(sortOption));
    }

    public LobbySortOption getSortOption() {
        return sortOption;
    }

    @Override
    public Iterator<Lobby> iterator() {
        return list.iterator();
    }

    public int size() {
        return list.size();
    }
}
