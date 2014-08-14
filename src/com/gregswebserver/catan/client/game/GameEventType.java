package com.gregswebserver.catan.client.game;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the GameEvent to indicate it's purpose.
 * Stores any of the game actions that can happen in a Catan game.
 * All enums have the type of object they expect at the other end.
 */
public enum GameEventType {

    Game_Create, //GameType
    Player_Join, //Identity
    Player_Leave, //Identity
    Player_Build_Settlement, //Coordinate
    Player_Build_City, //Coordinate
    Player_Build_Road, //Coordinate
    Player_Move_Robber, //Coordinate
    Player_Roll_Dice, //DiceRoll
    Player_Offer_Trade, //PlayerTrade
    Player_Accept_Trade, //PlayerTrade
    Player_Make_Trade, //ResourceTrade

}
