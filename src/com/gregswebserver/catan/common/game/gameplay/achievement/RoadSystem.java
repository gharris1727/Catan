package com.gregswebserver.catan.common.game.gameplay.achievement;

import com.gregswebserver.catan.common.game.board.hexarray.CoordTransforms;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.*;

/**
 * Created by greg on 4/15/16.
 * A path in the GameBoard that represents multiple paths that are adjacent and connected to one another.
 */
public class RoadSystem implements Comparable<RoadSystem>, Iterable<Path> {

    private final Set<Path> paths;
    private final List<Path> longest;

    public RoadSystem(HexagonalArray hexArray, Coordinate start) {
        //Create a set of all paths considered part of this RoadSystem.
        this.paths = new HashSet<>();
        //Create a queue of paths to expand while searching.
        Queue<Path> search = new LinkedList<>();
        //Get the origin path from its coordinate.
        Path origin = hexArray.getPath(start);
        //Add the origin path to the list of found edges, and to the beginning of the queue.
        paths.add(origin);
        search.offer(origin);
        //While we still have unproccessed edges
        while (!search.isEmpty()) {
            //Grab the next edge to process
            Path active = search.poll();
            //Go to each of its neighbors and discover it.
            for (Coordinate neighbor : getValidAdjacentEdges(hexArray, active)) {
                Path next = hexArray.getPath(neighbor);
                if (!paths.contains(next)) {
                    paths.add(next);
                    search.offer(next);
                }
            }
        }
        //Keep track of the best path we have found so far.
        List<Path> maximum = new ArrayList<>();
        //Check only paths starting from terminal edges.
        for (Path p : paths) {
            if (getValidAdjacentEdges(hexArray, p).size() == 1) {
                List<Path> candidate = searchLongest(hexArray, p);
                if (maximum.size() < candidate.size())
                    maximum = candidate;
            }
        }
        //If that fails, we have only cycles, so check starting everywhere.
        //TODO: try to cull some obviously bad paths, checking everything is slow.
        if (maximum.size() == 0) {
            for (Path p : paths) {
                List<Path> candidate = searchLongest(hexArray, p);
                if (maximum.size() < candidate.size())
                    maximum = candidate;
            }
        }
        longest = maximum;
    }

    private List<Coordinate> getValidAdjacentEdges(HexagonalArray hexArray, Path path) {
        List<Coordinate> out = new LinkedList<>();
        for (Coordinate vertex : CoordTransforms.getAdjacentVerticesFromEdge(path.getPosition()).values()) {
            Town town = hexArray.getTown(vertex);
            if (town != null && (town.getTeam() == TeamColor.None || town.getTeam() == path.getTeam())) {
                for (Coordinate edge : CoordTransforms.getAdjacentEdgesFromVertex(vertex).values()) {
                    Path neighbor = hexArray.getPath(edge);
                    if (neighbor != null && neighbor != path && neighbor.getTeam() == path.getTeam())
                        out.add(edge);
                }
            }
        }
        return out;
    }

    private List<Path> searchLongest(HexagonalArray hexArray, Path origin) {
        //Keep track of the best path we have found so far.
        List<Path> maximum = new ArrayList<>();
        //Depth-first search for the longest path, keeping state with two stacks.
        Stack<Path> search = new Stack<>();
        Stack<List<Coordinate>> searchNeighbors = new Stack<>();
        //Start the search off at the origin we identified.
        search.push(origin);
        searchNeighbors.push(new LinkedList<>(getValidAdjacentEdges(hexArray, origin)));
        //If there is anything still in the stack, then we are not done with the search
        while (!search.isEmpty()) {
            //Get the list of neighbors we have yet to deal with.
            List<Coordinate> neighbors = searchNeighbors.peek();
            //If we exhausted our list of neighbors, then we are at the end of a path.
            if (neighbors.isEmpty()) {
                //Check if we have a new maximum, and save the max in a new list.
                if (maximum.size() < search.size())
                    maximum = new ArrayList<>(search);
                //Backtrack up the stack.
                search.pop();
                searchNeighbors.pop();
            } else {
                //Otherwise there is a neighbor we need to traverse towards.
                Path neighbor = hexArray.getPath(neighbors.remove(0));
                //Get the list of neighbors of this neighbor.
                List<Coordinate> neighborSearchState = getValidAdjacentEdges(hexArray, neighbor);
                //Remove any nodes we have already seen from the search.
                for (Path existing : search)
                    neighborSearchState.remove(existing.getPosition());
                //Push this neighbor onto the stack.
                search.push(neighbor);
                //Push this list onto the state stack
                searchNeighbors.push(neighborSearchState);
            }
        }
        return maximum;
    }

    @Override
    public int compareTo(RoadSystem paths) {
        return longest.size() - paths.longest.size();
    }

    @Override
    public Iterator<Path> iterator() {
        return longest.iterator();
    }

    public int getLength() {
        return longest.size();
    }

    public Set<Path> getPaths() {
        return paths;
    }
}
