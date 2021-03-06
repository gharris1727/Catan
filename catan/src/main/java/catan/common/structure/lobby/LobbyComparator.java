package catan.common.structure.lobby;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by greg on 4/2/16.
 * A comparator that sorts lobbies by their different qualities.
 */
public class LobbyComparator implements Comparator<Lobby>, Serializable {

    private final LobbySortOption sortOption;

    public LobbyComparator(LobbySortOption sortOption) {
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
                return Integer.compare(f.size(), s.size());
            case Num_Clients_Desc:
                return Integer.compare(s.size(), f.size());
            case Open_Spaces_Asc:
                return Integer.compare(f.getConfig().getMaxPlayers() - f.size(), s.getConfig().getMaxPlayers() - s.size());
            case Open_Spaces_Desc:
                return Integer.compare(s.getConfig().getMaxPlayers() - s.size(), f.getConfig().getMaxPlayers() - f.size());
        }
        return 0;
    }

}
