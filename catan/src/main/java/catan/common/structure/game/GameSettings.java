package catan.common.structure.game;

import catan.common.event.EventPayload;
import catan.common.game.gameplay.allocator.TeamAllocator;
import catan.common.game.gameplay.generator.BoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.scoring.rules.GameRules;

/**
 * Created by greg on 1/24/16.
 * Pack of game settings for creating a CatanGame.
 */
public class GameSettings extends EventPayload {

    public final long seed;
    public final BoardLayout boardLayout;
    public final BoardGenerator boardGenerator;
    public final GameRules rules;
    public final TeamAllocator playerTeams;

    public GameSettings(long seed, BoardLayout boardLayout, BoardGenerator boardGenerator, GameRules rules, TeamAllocator playerTeams) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        GameSettings other = (GameSettings) o;

        if (seed != other.seed) return false;
        if (!boardLayout.equals(other.boardLayout)) return false;
        if (!boardGenerator.equals(other.boardGenerator)) return false;
        if (!rules.equals(other.rules)) return false;
        return playerTeams.equals(other.playerTeams);
    }

    @Override
    public int hashCode() {
        int result = (int) (seed ^ (seed >>> 32));
        result = (31 * result) + boardLayout.hashCode();
        result = (31 * result) + boardGenerator.hashCode();
        result = (31 * result) + rules.hashCode();
        result = (31 * result) + playerTeams.hashCode();
        return result;
    }
}
