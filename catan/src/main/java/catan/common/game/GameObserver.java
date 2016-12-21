package catan.common.game;

import catan.common.crypto.Username;
import catan.common.game.event.GameHistory;
import catan.common.game.gamestate.DiceRoll;
import catan.common.game.scoring.reporting.ScoreException;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.game.scoring.reporting.team.NullTeamScore;
import catan.common.game.scoring.reporting.team.TeamScoreReport;
import catan.common.game.teams.TeamColor;

import java.util.function.Consumer;

/**
 * Created by greg on 12/20/16.
 * Object handling observation requests to a CatanGame object.
 * Intended as a thread-safe way to access data within a catan game.
 */
public class GameObserver {

    private final CatanGame game;

    GameObserver(CatanGame game) {
        this.game = game;
    }

    public PlayerObserver getPlayerObserver(Username username) {
        synchronized (game) {
            return (game.players.getPlayer(username) == null) ? null : new PlayerObserver(game, username);
        }
    }

    public BoardObserver getBoardObserver() {
        return new BoardObserver(game);
    }

    public void readHistory(Consumer<GameHistory> action) {
        synchronized (game) {
            game.history.forEach(action);
        }
    }

    public boolean mustDiscard(Username username) {
        synchronized (game) {
            return game.state.getDiceRoll() == DiceRoll.Seven
                && game.players.getPlayer(username).getDiscardCount() > 0
                && !game.players.hasDiscarded(username);
        }
    }

    public TeamScoreReport getScore() {
        synchronized (game) {
            try {
                return game.scoring.score(game.rules);
            } catch (ScoreException e) {
                return NullTeamScore.INSTANCE;
            }
        }
    }

    public TeamColor getWinner() {
        synchronized (game) {
            TeamColor winner = TeamColor.None;
            int points = 0;
            TeamScoreReport teamScoreReport = getScore();
            for (TeamColor color : teamScoreReport) {
                ScoreReport report = teamScoreReport.getScoreReport(color);
                if (points < report.getPoints()) {
                    points = report.getPoints();
                    winner = color;
                }
            }
            if (points < game.rules.getMinimumPoints())
                return TeamColor.None;
            return winner;
        }
    }

    public boolean isFinished() {
        synchronized (game) {
            return getWinner() != TeamColor.None;
        }
    }
}
