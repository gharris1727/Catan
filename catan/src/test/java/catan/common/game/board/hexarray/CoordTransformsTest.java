package catan.common.game.board.hexarray;

import catan.common.util.Direction;
import catan.junit.UnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by greg on 2/26/16.
 * Testbed for the HexagonalArray class.
 */
@Category(UnitTests.class)
public class CoordTransformsTest {

    @Test
    public void testSpaceCoordinateFromSpace() {
        Coordinate odd = new Coordinate(1,1);
        Coordinate even = new Coordinate(2,1);
        try {
            assertEquals(new Coordinate(1,0), CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.up));
            assertEquals(new Coordinate(1,2), CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.down));
            assertEquals(new Coordinate(0,1), CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.upleft));
            assertEquals(new Coordinate(0,2), CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.downleft));
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.upright));
            assertEquals(new Coordinate(2,2), CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromSpace(odd, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,0), CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.up));
            assertEquals(new Coordinate(2,2), CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.down));
            assertEquals(new Coordinate(1,0), CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.upleft));
            assertEquals(new Coordinate(1,1), CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.downleft));
            assertEquals(new Coordinate(3,0), CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.upright));
            assertEquals(new Coordinate(3,1), CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromSpace(even, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testEdgeCoordinateFromEdge() {
        Coordinate zero = new Coordinate(6,1);
        Coordinate one = new Coordinate(7,1);
        Coordinate two = new Coordinate(8,1);
        Coordinate three = new Coordinate(9,1);
        Coordinate four = new Coordinate(10,1);
        Coordinate five = new Coordinate(11,1);
        try {
            assertEquals(new Coordinate(7,0), CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.up));
            assertEquals(new Coordinate(7,1), CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.down));
            assertEquals(new Coordinate(5,1), CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.left));
            assertEquals(new Coordinate(8,1), CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(zero, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,1), CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.up));
            assertEquals(new Coordinate(6,2), CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.down));
            assertEquals(new Coordinate(5,1), CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.left));
            assertEquals(new Coordinate(8,2), CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(7,0), CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.upleft));
            assertEquals(new Coordinate(6,1), CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.downleft));
            assertEquals(new Coordinate(10,0), CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.upright));
            assertEquals(new Coordinate(9,1), CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(10,0), CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.up));
            assertEquals(new Coordinate(10,1), CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.down));
            assertEquals(new Coordinate(8,1), CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.left));
            assertEquals(new Coordinate(11,1), CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(9,1), CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.up));
            assertEquals(new Coordinate(9,2), CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.down));
            assertEquals(new Coordinate(8,2), CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.left));
            assertEquals(new Coordinate(11,1), CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(four, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(9,1), CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.upleft));
            assertEquals(new Coordinate(10,1), CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.downleft));
            assertEquals(new Coordinate(12,1), CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.upright));
            assertEquals(new Coordinate(13,1), CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromEdge(five, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testVertexCoordinateFromVertex() {
        Coordinate zero = new Coordinate(4,1);
        Coordinate one = new Coordinate(5,1);
        Coordinate two = new Coordinate(6,1);
        Coordinate three = new Coordinate(7,1);
        try {
            assertEquals(new Coordinate(3,1), CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.left));
            assertEquals(new Coordinate(5,1), CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.upright));
            assertEquals(new Coordinate(5,2), CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,1), CoordTransforms.getVertexCoordinateFromVertex(one, Direction.right));
            assertEquals(new Coordinate(4,0), CoordTransforms.getVertexCoordinateFromVertex(one, Direction.upleft));
            assertEquals(new Coordinate(4,1), CoordTransforms.getVertexCoordinateFromVertex(one, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromVertex(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(5,1), CoordTransforms.getVertexCoordinateFromVertex(two, Direction.left));
            assertEquals(new Coordinate(7,0), CoordTransforms.getVertexCoordinateFromVertex(two, Direction.upright));
            assertEquals(new Coordinate(7,1), CoordTransforms.getVertexCoordinateFromVertex(two, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromVertex(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), CoordTransforms.getVertexCoordinateFromVertex(three, Direction.right));
            assertEquals(new Coordinate(6,1), CoordTransforms.getVertexCoordinateFromVertex(three, Direction.upleft));
            assertEquals(new Coordinate(6,2), CoordTransforms.getVertexCoordinateFromVertex(three, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromVertex(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromVertex(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testEdgeCoordinateFromSpace() {
        Coordinate odd = new Coordinate(1,1);
        Coordinate even = new Coordinate(2,1);
        try {
            assertEquals(new Coordinate(5,1), CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.up));
            assertEquals(new Coordinate(5,2), CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.down));
            assertEquals(new Coordinate(4,1), CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.upleft));
            assertEquals(new Coordinate(3,2), CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.downleft));
            assertEquals(new Coordinate(7,1), CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.upright));
            assertEquals(new Coordinate(6,2), CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromSpace(odd, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.up));
            assertEquals(new Coordinate(8,2), CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.down));
            assertEquals(new Coordinate(6,1), CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.upleft));
            assertEquals(new Coordinate(7,1), CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.downleft));
            assertEquals(new Coordinate(9,1), CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.upright));
            assertEquals(new Coordinate(10,1), CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromSpace(even, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testVertexCoordinateFromSpace() {
        Coordinate odd = new Coordinate(1,1);
        Coordinate even = new Coordinate(2,1);
        try {
            assertEquals(new Coordinate(2,2), CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.left));
            assertEquals(new Coordinate(5,2), CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.right));
            assertEquals(new Coordinate(3,1), CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.upleft));
            assertEquals(new Coordinate(3,2), CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.downleft));
            assertEquals(new Coordinate(4,1), CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.upright));
            assertEquals(new Coordinate(4,2), CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromSpace(odd, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(4,1), CoordTransforms.getVertexCoordinateFromSpace(even, Direction.left));
            assertEquals(new Coordinate(7,1), CoordTransforms.getVertexCoordinateFromSpace(even, Direction.right));
            assertEquals(new Coordinate(5,1), CoordTransforms.getVertexCoordinateFromSpace(even, Direction.upleft));
            assertEquals(new Coordinate(5,2), CoordTransforms.getVertexCoordinateFromSpace(even, Direction.downleft));
            assertEquals(new Coordinate(6,1), CoordTransforms.getVertexCoordinateFromSpace(even, Direction.upright));
            assertEquals(new Coordinate(6,2), CoordTransforms.getVertexCoordinateFromSpace(even, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromSpace(even, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromSpace(even, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromSpace(even, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testSpaceCoordinateFromEdge() {
        Coordinate zero = new Coordinate(6,1);
        Coordinate one = new Coordinate(7,1);
        Coordinate two = new Coordinate(8,1);
        Coordinate three = new Coordinate(9,1);
        Coordinate four = new Coordinate(10,1);
        Coordinate five = new Coordinate(11,1);
        try {
            assertEquals(new Coordinate(1,0), CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.upleft));
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(zero, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(1,1), CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.downleft));
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,0), CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.up));
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.down));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(two, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.downleft));
            assertEquals(new Coordinate(3,0), CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.upleft));
            assertEquals(new Coordinate(3,1), CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(four, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(3,0), CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.up));
            assertEquals(new Coordinate(3,1), CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.down));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromEdge(five, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testVertexCoordinateFromEdge() {
        Coordinate zero = new Coordinate(6,1);
        Coordinate one = new Coordinate(7,1);
        Coordinate two = new Coordinate(8,1);
        Coordinate three = new Coordinate(9,1);
        Coordinate four = new Coordinate(10,1);
        Coordinate five = new Coordinate(11,1);
        try {
            assertEquals(new Coordinate(4,1), CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.downleft));
            assertEquals(new Coordinate(5,1), CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(zero, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(4,1), CoordTransforms.getVertexCoordinateFromEdge(one, Direction.upleft));
            assertEquals(new Coordinate(5,2), CoordTransforms.getVertexCoordinateFromEdge(one, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromEdge(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(one, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(one, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(5,1), CoordTransforms.getVertexCoordinateFromEdge(two, Direction.left));
            assertEquals(new Coordinate(6,1), CoordTransforms.getVertexCoordinateFromEdge(two, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromEdge(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(two, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(two, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,1), CoordTransforms.getVertexCoordinateFromEdge(three, Direction.upleft));
            assertEquals(new Coordinate(7,1), CoordTransforms.getVertexCoordinateFromEdge(three, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromEdge(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(three, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(three, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,2), CoordTransforms.getVertexCoordinateFromEdge(four, Direction.downleft));
            assertEquals(new Coordinate(7,1), CoordTransforms.getVertexCoordinateFromEdge(four, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromEdge(four, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(four, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(four, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(four, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(four, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(four, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(four, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(7,1), CoordTransforms.getVertexCoordinateFromEdge(five, Direction.left));
            assertEquals(new Coordinate(8,1), CoordTransforms.getVertexCoordinateFromEdge(five, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getVertexCoordinateFromEdge(five, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(five, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(five, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(five, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(five, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(five, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getVertexCoordinateFromEdge(five, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testSpaceCoordinateFromVertex() {
        Coordinate zero = new Coordinate(4,1);
        Coordinate one = new Coordinate(5,1);
        Coordinate two = new Coordinate(6,1);
        Coordinate three = new Coordinate(7,1);
        try {
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.right));
            assertEquals(new Coordinate(1,0), CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.upleft));
            assertEquals(new Coordinate(1,1), CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(zero, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(1,0), CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.left));
            assertEquals(new Coordinate(2,0), CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.upright));
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(one, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(3,0), CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.right));
            assertEquals(new Coordinate(2,0), CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.upleft));
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(two, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,1), CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.left));
            assertEquals(new Coordinate(3,0), CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.upright));
            assertEquals(new Coordinate(3,1), CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getSpaceCoordinateFromVertex(three, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testEdgeCoordinateFromVertex() {
        Coordinate zero = new Coordinate(4,1);
        Coordinate one = new Coordinate(5,1);
        Coordinate two = new Coordinate(6,1);
        Coordinate three = new Coordinate(7,1);
        try {
            assertEquals(new Coordinate(5,1), CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.left));
            assertEquals(new Coordinate(6,1), CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.upright));
            assertEquals(new Coordinate(7,1), CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.right));
            assertEquals(new Coordinate(7,0), CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.upleft));
            assertEquals(new Coordinate(6,1), CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.left));
            assertEquals(new Coordinate(10,0), CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.upright));
            assertEquals(new Coordinate(9,1), CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(11,1), CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.right));
            assertEquals(new Coordinate(9,1), CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.upleft));
            assertEquals(new Coordinate(10,1), CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            CoordTransforms.getEdgeCoordinateFromVertex(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

}
