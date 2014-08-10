package com.gregswebserver.catan.game.gameplay;

import com.gregswebserver.catan.game.board.hexarray.HexagonalArray;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Greg on 8/9/2014.
 * Static data in order to create a game of Catan.
 */
public class GameType {

    GameType(String fileName) {
        try {
            BufferedInputStream file = new BufferedInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
        }
    }

    public void save(String fileName) {
        //TODO: save custom game modes to file
    }

    public void init(HexagonalArray board) {

    }
}
