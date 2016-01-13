package com.gregswebserver.catan.common.lobby;

/**
 * Created by greg on 1/12/16.
 * Different options for sorting the list of lobbies.
 */
public enum LobbySortOption {
    Lobby_Name_Asc("Lobby Name"),
    Lobby_Name_Desc("Lobby Name"),
    Host_Name_Asc("Host Name"),
    Host_Name_Desc("Host Name"),
    Game_Type_Asc("Game Type"),
    Game_Type_Desc("Game Type"),
    Num_Clients_Asc("Clients Connected"),
    Num_Clients_Desc("Clients Connected"),
    Open_Spaces_Asc("Open Spaces"),
    Open_Spaces_Desc("Open Spaces"),;

    private final String title;

    LobbySortOption(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
