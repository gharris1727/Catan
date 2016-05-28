package com.gregswebserver.catan.common.structure.game;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.Map;

/**
 * Created by greg on 1/24/16.
 * Pack of game settings for creating a CatanGame.
 */
public class GameSettings extends EventPayload {

    public final long seed;
    public final BoardLayout boardLayout;
    public final BoardGenerator boardGenerator;
    public final GameRules rules;
    public final Map<Username, TeamColor> playerTeams;

    public GameSettings(long seed, BoardLayout boardLayout, BoardGenerator boardGenerator, GameRules rules, Map<Username, TeamColor> playerTeams) {
        this.seed = seed;
        this.boardLayout = boardLayout;
        this.boardGenerator = boardGenerator;
        this.rules = rules;
        this.playerTeams = playerTeams;
    }

    @Override
    public String toString() {
        return "GameSettings(" + seed + "/" + boardLayout + "/" + boardGenerator + "/" + rules + "/" + playerTeams + ")";
    }

}
