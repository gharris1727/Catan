package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.resources.GameRuleSet;
import com.gregswebserver.catan.common.structure.PlayerPool;

/**
 * Created by greg on 1/24/16.
 * Pack of game settings for creating a CatanGame.
 */
public class GameSettings extends EventPayload {

    private final long seed;
    private final BoardLayout layout;
    private final BoardGenerator generator;
    private final GameRuleSet rules;
    private final PlayerPool teams;

    public GameSettings(long seed, BoardLayout layout, BoardGenerator generator, GameRuleSet rules, PlayerPool teams) {
        this.seed = seed;
        this.layout = layout;
        this.generator = generator;
        this.rules = rules;
        this.teams = teams;
    }

    public long getSeed(){
        return seed;
    }

    public BoardLayout getLayout() {
        return layout;
    }

    public BoardGenerator getGenerator() {
        return generator;
    }

    public GameRuleSet getRules() {
        return rules;
    }

    public PlayerPool getTeams() {
        return teams;
    }

    @Override
    public String toString() {
        return "GameSettings(" + seed + "/" + layout + "/" + generator + "/" + rules + "/" + teams + ")";
    }

    public GameBoard generate() {
        return generator.generate(layout, seed);
    }
}
