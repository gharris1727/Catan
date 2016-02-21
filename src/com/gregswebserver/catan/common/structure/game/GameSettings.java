package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;

/**
 * Created by greg on 1/24/16.
 * Pack of game settings for creating a CatanGame.
 */
public class GameSettings extends EventPayload {

    public final long seed;
    public final BoardLayout boardLayout;
    public final BoardGenerator boardGenerator;
    public final GameRules gameRules;
    public final PlayerPool playerPool;

    public GameSettings(long seed, BoardLayout boardLayout, BoardGenerator boardGenerator, GameRules gameRules, PlayerPool playerPool) {
        this.seed = seed;
        this.boardLayout = boardLayout;
        this.boardGenerator = boardGenerator;
        this.gameRules = gameRules;
        this.playerPool = playerPool;
    }

    @Override
    public String toString() {
        return "GameSettings(" + seed + "/" + boardLayout + "/" + boardGenerator + "/" + gameRules + "/" + playerPool + ")";
    }

    public GameBoard generate() {
        return boardGenerator.generate(boardLayout, seed);
    }
}
