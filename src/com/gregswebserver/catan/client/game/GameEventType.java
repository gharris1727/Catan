package com.gregswebserver.catan.client.game;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the GameEvent to indicate it's purpose.
 * Stores data related to managing a game
 * All enums have the type of object they expect at the other end.
 */
public enum GameEventType {

    Game_Create, //Identity owner
    Game_Join, //Identity player
    Game_Leave, //Identity player
    Game_Start, //Identity owner
    Game_End, //Identity winner

}
