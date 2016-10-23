package catan.common.game.board.hexarray;

import catan.common.util.Direction;
import catan.junit.UnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 2/26/16.
 * Testbed for the HexagonalArray class.
 */
@Category(UnitTests.class)
public class CoordTransformsTest {

    private void test(Coordinate[] expected, Map<Direction,Coordinate> map, Function<Direction, Coordinate> get) {
        for (int i = 0; i < Direction.values().length; i++) {
            assertEquals("Get " + Direction.values()[i] + " Incorrect", expected[i], get.apply(Direction.values()[i]));
            assertEquals("Map " + Direction.values()[i] + " Incorrect", expected[i], map.get(Direction.values()[i]));
        }
    }

    @Test
    public void testSpaceCoordinateFromSpace() {
        Coordinate odd = new Coordinate(1,1);
        Coordinate even = new Coordinate(2,1);

        test(new Coordinate[]{null, new Coordinate(1,0), new Coordinate(1,2), null, null,
                new Coordinate(0,1), new Coordinate(0,2), new Coordinate(2,1), new Coordinate(2,2)},
            CoordTransforms.getAdjacentSpacesFromSpace(odd),
            d->CoordTransforms.getSpaceCoordinateFromSpace(odd, d));

        test(new Coordinate[]{null, new Coordinate(2,0), new Coordinate(2,2), null, null,
                new Coordinate(1,0), new Coordinate(1,1), new Coordinate(3,0), new Coordinate(3,1)},
            CoordTransforms.getAdjacentSpacesFromSpace(even),
            d->CoordTransforms.getSpaceCoordinateFromSpace(even, d));

    }

    @Test
    public void testEdgeCoordinateFromEdge() {

        test(new Coordinate[] {null, new Coordinate(7,0), new Coordinate(7,1), new Coordinate(5,1), new Coordinate(8,1), null, null, null, null},
            CoordTransforms.getAdjacentEdgesFromEdge(new Coordinate(6,1)),
            d->CoordTransforms.getEdgeCoordinateFromEdge(new Coordinate(6,1), d));

        test(new Coordinate[] {null, new Coordinate(6,1), new Coordinate(6,2), new Coordinate(5,1), new Coordinate(8,2), null, null, null, null},
            CoordTransforms.getAdjacentEdgesFromEdge(new Coordinate(7,1)),
            d->CoordTransforms.getEdgeCoordinateFromEdge(new Coordinate(7,1), d));

        test(new Coordinate[] {null, null, null, null, null, new Coordinate(7,0), new Coordinate(6,1), new Coordinate(10,0), new Coordinate(9,1)},
            CoordTransforms.getAdjacentEdgesFromEdge(new Coordinate(8,1)),
            d->CoordTransforms.getEdgeCoordinateFromEdge(new Coordinate(8,1), d));

        test(new Coordinate[] {null, new Coordinate(10,0), new Coordinate(10,1), new Coordinate(8,1), new Coordinate(11,1), null, null, null, null},
            CoordTransforms.getAdjacentEdgesFromEdge(new Coordinate(9,1)),
            d->CoordTransforms.getEdgeCoordinateFromEdge(new Coordinate(9,1), d));

        test(new Coordinate[] {null, new Coordinate(9,1), new Coordinate(9,2), new Coordinate(8,2), new Coordinate(11,1), null, null, null, null},
            CoordTransforms.getAdjacentEdgesFromEdge(new Coordinate(10,1)),
            d->CoordTransforms.getEdgeCoordinateFromEdge(new Coordinate(10,1), d));

        test(new Coordinate[] {null, null, null, null, null, new Coordinate(9,1), new Coordinate(10,1), new Coordinate(12,1), new Coordinate(13,1)},
            CoordTransforms.getAdjacentEdgesFromEdge(new Coordinate(11,1)),
            d->CoordTransforms.getEdgeCoordinateFromEdge(new Coordinate(11,1), d));

    }

    @Test
    public void testVertexCoordinateFromVertex() {

        test(new Coordinate[] {null, null, null, new Coordinate(3,1), null, null, null, new Coordinate(5,1), new Coordinate(5,2)},
            CoordTransforms.getAdjacentVerticesFromVertex(new Coordinate(4,1)),
            d->CoordTransforms.getVertexCoordinateFromVertex(new Coordinate(4,1), d));

        test(new Coordinate[] {null, null, null, null, new Coordinate(6,1), new Coordinate(4,0), new Coordinate(4,1), null, null},
            CoordTransforms.getAdjacentVerticesFromVertex(new Coordinate(5,1)),
            d->CoordTransforms.getVertexCoordinateFromVertex(new Coordinate(5,1), d));

        test(new Coordinate[] {null, null, null, new Coordinate(5,1), null, null, null, new Coordinate(7,0), new Coordinate(7,1)},
            CoordTransforms.getAdjacentVerticesFromVertex(new Coordinate(6,1)),
            d->CoordTransforms.getVertexCoordinateFromVertex(new Coordinate(6,1), d));

        test(new Coordinate[] {null, null, null, null, new Coordinate(8,1), new Coordinate(6,1), new Coordinate(6,2), null, null},
            CoordTransforms.getAdjacentVerticesFromVertex(new Coordinate(7,1)),
            d->CoordTransforms.getVertexCoordinateFromVertex(new Coordinate(7,1), d));

    }

    @Test
    public void testEdgeCoordinateFromSpace() {

        test(new Coordinate[] {null, new Coordinate(5,1), new Coordinate(5,2), null, null, new Coordinate(4,1), new Coordinate(3,2), new Coordinate(7,1), new Coordinate(6,2)},
            CoordTransforms.getAdjacentEdgesFromSpace(new Coordinate(1,1)),
            d->CoordTransforms.getEdgeCoordinateFromSpace(new Coordinate(1,1), d));

        test(new Coordinate[] {null, new Coordinate(8,1), new Coordinate(8,2), null, null, new Coordinate(6,1), new Coordinate(7,1), new Coordinate(9,1), new Coordinate(10,1)},
            CoordTransforms.getAdjacentEdgesFromSpace(new Coordinate(2,1)),
            d->CoordTransforms.getEdgeCoordinateFromSpace(new Coordinate(2,1), d));

    }

    @Test
    public void testVertexCoordinateFromSpace() {

        test(new Coordinate[] {null, null, null, new Coordinate(2,2), new Coordinate(5,2), new Coordinate(3,1), new Coordinate(3,2), new Coordinate(4,1), new Coordinate(4,2)},
            CoordTransforms.getAdjacentVerticesFromSpace(new Coordinate(1,1)),
            d->CoordTransforms.getVertexCoordinateFromSpace(new Coordinate(1,1), d));

        test(new Coordinate[] {null, null, null, new Coordinate(4,1), new Coordinate(7,1), new Coordinate(5,1), new Coordinate(5,2), new Coordinate(6,1), new Coordinate(6,2)},
            CoordTransforms.getAdjacentVerticesFromSpace(new Coordinate(2,1)),
            d->CoordTransforms.getVertexCoordinateFromSpace(new Coordinate(2,1), d));

    }

    @Test
    public void testSpaceCoordinateFromEdge() {

        test(new Coordinate[] {null, null, null, null, null, new Coordinate(1,0), null, null, new Coordinate(2,1)},
            CoordTransforms.getAdjacentSpacesFromEdge(new Coordinate(6,1)),
            d->CoordTransforms.getSpaceCoordinateFromEdge(new Coordinate(6,1), d));

        test(new Coordinate[] {null, null, null, null, null, null, new Coordinate(1,1), new Coordinate(2,1), null},
            CoordTransforms.getAdjacentSpacesFromEdge(new Coordinate(7,1)),
            d->CoordTransforms.getSpaceCoordinateFromEdge(new Coordinate(7,1), d));

        test(new Coordinate[] {null, new Coordinate(2,0), new Coordinate(2,1), null, null, null, null, null, null},
            CoordTransforms.getAdjacentSpacesFromEdge(new Coordinate(8,1)),
            d->CoordTransforms.getSpaceCoordinateFromEdge(new Coordinate(8,1), d));

        test(new Coordinate[] {null, null, null, null, null, null, new Coordinate(2,1), new Coordinate(3,0), null},
            CoordTransforms.getAdjacentSpacesFromEdge(new Coordinate(9,1)),
            d->CoordTransforms.getSpaceCoordinateFromEdge(new Coordinate(9,1), d));

        test(new Coordinate[] {null, null, null, null, null, new Coordinate(2,1), null, null, new Coordinate(3,1)},
            CoordTransforms.getAdjacentSpacesFromEdge(new Coordinate(10,1)),
            d->CoordTransforms.getSpaceCoordinateFromEdge(new Coordinate(10,1), d));

        test(new Coordinate[] {null, new Coordinate(3,0), new Coordinate(3,1), null, null, null, null, null, null},
            CoordTransforms.getAdjacentSpacesFromEdge(new Coordinate(11,1)),
            d->CoordTransforms.getSpaceCoordinateFromEdge(new Coordinate(11,1), d));

    }

    @Test
    public void testVertexCoordinateFromEdge() {

        test(new Coordinate[] {null, null, null, null, null, null, new Coordinate(4,1), new Coordinate(5,1), null},
            CoordTransforms.getAdjacentVerticesFromEdge(new Coordinate(6,1)),
            d->CoordTransforms.getVertexCoordinateFromEdge(new Coordinate(6,1), d));

        test(new Coordinate[] {null, null, null, null, null, new Coordinate(4,1), null, null, new Coordinate(5,2)},
            CoordTransforms.getAdjacentVerticesFromEdge(new Coordinate(7,1)),
            d->CoordTransforms.getVertexCoordinateFromEdge(new Coordinate(7,1), d));

        test(new Coordinate[] {null, null, null, new Coordinate(5,1), new Coordinate(6,1), null, null, null, null},
            CoordTransforms.getAdjacentVerticesFromEdge(new Coordinate(8,1)),
            d->CoordTransforms.getVertexCoordinateFromEdge(new Coordinate(8,1), d));

        test(new Coordinate[] {null, null, null, null, null, new Coordinate(6,1), null, null, new Coordinate(7,1)},
            CoordTransforms.getAdjacentVerticesFromEdge(new Coordinate(9,1)),
            d->CoordTransforms.getVertexCoordinateFromEdge(new Coordinate(9,1), d));

        test(new Coordinate[] {null, null, null, null, null, null, new Coordinate(6,2), new Coordinate(7,1), null},
            CoordTransforms.getAdjacentVerticesFromEdge(new Coordinate(10,1)),
            d->CoordTransforms.getVertexCoordinateFromEdge(new Coordinate(10,1), d));

        test(new Coordinate[] {null, null, null, new Coordinate(7,1), new Coordinate(8,1), null, null, null, null},
            CoordTransforms.getAdjacentVerticesFromEdge(new Coordinate(11,1)),
            d->CoordTransforms.getVertexCoordinateFromEdge(new Coordinate(11,1), d));

    }

    @Test
    public void testSpaceCoordinateFromVertex() {

        test(new Coordinate[] {null, null, null, null, new Coordinate(2,1), new Coordinate(1,0), new Coordinate(1,1), null, null},
            CoordTransforms.getAdjacentSpacesFromVertex(new Coordinate(4,1)),
            d->CoordTransforms.getSpaceCoordinateFromVertex(new Coordinate(4,1), d));

        test(new Coordinate[] {null, null, null, new Coordinate(1,0), null, null, null, new Coordinate(2,0), new Coordinate(2,1)},
            CoordTransforms.getAdjacentSpacesFromVertex(new Coordinate(5,1)),
            d->CoordTransforms.getSpaceCoordinateFromVertex(new Coordinate(5,1), d));

        test(new Coordinate[] {null, null, null, null, new Coordinate(3,0), new Coordinate(2,0), new Coordinate(2,1), null, null},
            CoordTransforms.getAdjacentSpacesFromVertex(new Coordinate(6,1)),
            d->CoordTransforms.getSpaceCoordinateFromVertex(new Coordinate(6,1), d));

        test(new Coordinate[] {null, null, null, new Coordinate(2,1), null, null, null, new Coordinate(3,0), new Coordinate(3,1)},
            CoordTransforms.getAdjacentSpacesFromVertex(new Coordinate(7,1)),
            d->CoordTransforms.getSpaceCoordinateFromVertex(new Coordinate(7,1), d));

    }

    @Test
    public void testEdgeCoordinateFromVertex() {

        test(new Coordinate[] {null, null, null, new Coordinate(5,1), null, null, null, new Coordinate(6,1), new Coordinate(7,1)},
            CoordTransforms.getAdjacentEdgesFromVertex(new Coordinate(4,1)),
            d->CoordTransforms.getEdgeCoordinateFromVertex(new Coordinate(4,1), d));

        test(new Coordinate[] {null, null, null, null, new Coordinate(8,1), new Coordinate(7,0), new Coordinate(6,1), null, null},
            CoordTransforms.getAdjacentEdgesFromVertex(new Coordinate(5,1)),
            d->CoordTransforms.getEdgeCoordinateFromVertex(new Coordinate(5,1), d));

        test(new Coordinate[] {null, null, null, new Coordinate(8,1), null, null, null, new Coordinate(10,0), new Coordinate(9,1)},
            CoordTransforms.getAdjacentEdgesFromVertex(new Coordinate(6,1)),
            d->CoordTransforms.getEdgeCoordinateFromVertex(new Coordinate(6,1), d));

        test(new Coordinate[] {null, null, null, null, new Coordinate(11,1), new Coordinate(9,1), new Coordinate(10,1), null, null},
            CoordTransforms.getAdjacentEdgesFromVertex(new Coordinate(7,1)),
            d->CoordTransforms.getEdgeCoordinateFromVertex(new Coordinate(7,1), d));

    }

}
