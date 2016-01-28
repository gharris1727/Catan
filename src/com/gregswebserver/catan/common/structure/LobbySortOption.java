package com.gregswebserver.catan.common.structure;

/**
 * Created by greg on 1/12/16.
 * Different options for sorting the list of lobbies.
 */
public enum LobbySortOption {
    Lobby_Name_Asc("Lobby"),
    Lobby_Name_Desc("Lobby"),
    Game_Type_Asc("Game"),
    Game_Type_Desc("Game"),
    Num_Clients_Asc("# Clients"),
    Num_Clients_Desc("# Clients"),
    Open_Spaces_Asc("# Open"),
    Open_Spaces_Desc("# Open"),;

    private final String title;

    LobbySortOption(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
