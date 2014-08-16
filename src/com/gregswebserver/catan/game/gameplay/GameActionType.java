package com.gregswebserver.catan.game.gameplay;

/**
 * Created by Greg on 8/15/2014.
 * Enum describing any of the many GameActions that can take place.
 */
public enum GameActionType {

    Player_Build_Settlement, //Coordinate
    Player_Build_City, //Coordinate
    Player_Build_Road, //Coordinate
    Player_Move_Robber, //Coordinate
    Player_Roll_Dice, //DiceRoll
    Player_Accept_Trade, //PlayerTrade
    Player_Make_Trade, //ResourceTrade
}
