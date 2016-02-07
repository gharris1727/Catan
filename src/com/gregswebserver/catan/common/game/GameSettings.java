package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.game.board.layout.BoardLayout;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.resources.GameRuleSet;
import com.gregswebserver.catan.common.structure.PlayerPool;

/**
 * Created by greg on 1/24/16.
 * Pack of game settings for creating a CatanGame.
 */
public class GameSettings {

    private final BoardLayout layout;
    private final BoardGenerator generator;
    private final GameRuleSet rules;
    private final PlayerPool teams;

    public GameSettings(BoardLayout layout, BoardGenerator generator, GameRuleSet rules, PlayerPool teams) {
        this.layout = layout;
        this.generator = generator;
        this.rules = rules;
        this.teams = teams;
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
        return "GameSettings(" + layout + "/" + generator + "/" + rules + "/" + teams + ")";
    }
}
