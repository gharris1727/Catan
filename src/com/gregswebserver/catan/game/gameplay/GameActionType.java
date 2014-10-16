package com.gregswebserver.catan.game.gameplay;

/**
 * Created by Greg on 8/15/2014.
 * Enum describing any of the many GameActions that can take place.
 */
public enum GameActionType {

    Build_Settlement, //Coordinate
    Build_City, //Coordinate
    Build_Road, //Coordinate
    Trade_Offer, //PlayerTrade
    Trade_Accept, //PlayerTrade
    Trade_Bank, //ResourceTrade
    Player_Select_Location, //Coordinate
    Player_Move_Robber, //Coordinate
    Player_Roll_Dice, //DiceRoll
}
