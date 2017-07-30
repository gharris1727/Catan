package catan.common.game.scoring.achievement;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.board.GameBoard;
import catan.common.game.board.hexarray.CoordTransforms;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.paths.Path;
import catan.common.game.scoring.ScoreEvent;
import catan.common.game.scoring.ScoreKeeper;
import catan.common.game.scoring.reporting.scores.NullScoreReport;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.game.scoring.rules.GameRules;
import catan.common.game.teams.TeamColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


/**
 * Created by greg on 5/26/16.
 * A leaderboard keeping track of active RoadSystems.
 */
public class LongestRoadScoreKeeper implements ScoreKeeper {

    private final GameBoard board;
    private final Map<Coordinate, RoadSystem> roadSystems;

    private final Stack<ScoreEvent> history;

    public LongestRoadScoreKeeper(GameBoard board) {
        this.board = board;
        roadSystems = new HashMap<>();
        history = new Stack<>();
    }

    private void updateVertex(Coordinate vertex) {
        //Update the adjacent edges in case they were separated by the settlement building.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromVertex(vertex).values())
            discoverRoadSystem(adjacent);
    }

    private void updateEdge(Coordinate edge) {
        //Get the original path that we need to break up.
        RoadSystem path = roadSystems.get(edge);
        //Look at each of the neighbors of the edge being deleted.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromEdge(edge).values()) {
            //If the RoadSystem for the neighbor has not been updated, we need to update it.
            if (roadSystems.get(adjacent).equals(path))
                discoverRoadSystem(adjacent);
        }
    }

    private void discoverRoadSystem(Coordinate origin) {
        //Get the path at the origin coordinate
        Path originPath = board.getPath(origin);
        //If there is no edge, or it is a non-teamColor path then we shouldn't process anything.
        if ((originPath != null) && (originPath.getTeam() != TeamColor.None)) {
            //Create the new path
            RoadSystem roadSystem = new RoadSystem(board, origin);
            //Add this path to the overall list of roadSystems.
            //For every path that is a member, we need to update their path pointers.
            for (Path p : roadSystem.getPaths()) {
                //Get the path that is associated with that edge.
                RoadSystem existing = roadSystems.get(p.getPosition());
                //If there was an existing path, it is now invalid so remove it from the priority queue.
                if (existing != null)
                //Now map this edge to the new path.
                    roadSystems.put(p.getPosition(), roadSystem);
            }
        }
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        ScoreEvent event = history.pop();
        switch (event.getType()) {
            case Build_Settlement:
                updateVertex((Coordinate) event.getPayload());
                break;
            case Build_Road:
                updateEdge((Coordinate) event.getPayload());
                break;
            default:
                throw new EventConsumerException("Inconsistent ScoreEvent Undo");
        }
    }

    @Override
    public EventConsumerProblem test(ScoreEvent event) {
        if (event == null)
            return new EventConsumerProblem("No event");
        switch (event.getType()){
            case Build_Settlement:
            case Build_Road:
                break;
            default:
                return new EventConsumerProblem("Uninterested");
        }
        return null;
    }

    @Override
    public void execute(ScoreEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null) {
            throw new EventConsumerException(event, problem);
        }
        history.push(event);
        switch (event.getType()){
            case Build_Settlement:
                updateVertex((Coordinate) event.getPayload());
                break;
            case Build_Road:
                discoverRoadSystem((Coordinate) event.getPayload());
                break;
            default:
                throw new EventConsumerException("Inconsistent ScoreEvent Exec");
        }
    }

    @Override
    public ScoreReport score(GameRules rules) {
        //TODO: implement score counting of longest road.
        return NullScoreReport.INSTANCE;
    }
}
