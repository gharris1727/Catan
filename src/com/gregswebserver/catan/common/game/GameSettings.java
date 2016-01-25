package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.game.board.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.GameRules;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;

/**
 * Created by greg on 1/24/16.
 * Pack of game settings for creating a CatanGame.
 */
public class GameSettings {

    private final BoardLayout layout;
    private final BoardGenerator generator;
    private final GameRules rules;

    public GameSettings(BoardLayout layout, BoardGenerator generator, GameRules rules) {
        this.layout = layout;
        this.generator = generator;
        this.rules = rules;
    }

    public BoardLayout getLayout() {
        return layout;
    }

    public BoardGenerator getGenerator() {
        return generator;
    }

    public GameRules getRules() {
        return rules;
    }
}
