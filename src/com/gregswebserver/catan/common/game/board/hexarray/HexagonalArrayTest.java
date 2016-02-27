package com.gregswebserver.catan.common.game.board.hexarray;

import com.gregswebserver.catan.common.util.Direction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * Created by greg on 2/26/16.
 * Testbed for the HexagonalArray class.
 */
public class HexagonalArrayTest {

    private static HexagonalArray array;

    @Before
    public void setup() {
        array = new HexagonalArray(5,5);
    }

    @Test
    public void testSpaceCoordinateFromSpace() {
        Coordinate odd = new Coordinate(1,1);
        Coordinate even = new Coordinate(2,1);
        try {
            assertEquals(new Coordinate(1,0), array.getSpaceCoordinateFromSpace(odd, Direction.up));
            assertEquals(new Coordinate(1,2), array.getSpaceCoordinateFromSpace(odd, Direction.down));
            assertEquals(new Coordinate(0,1), array.getSpaceCoordinateFromSpace(odd, Direction.upleft));
            assertEquals(new Coordinate(0,2), array.getSpaceCoordinateFromSpace(odd, Direction.downleft));
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromSpace(odd, Direction.upright));
            assertEquals(new Coordinate(2,2), array.getSpaceCoordinateFromSpace(odd, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromSpace(odd, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromSpace(odd, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromSpace(odd, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,0), array.getSpaceCoordinateFromSpace(even, Direction.up));
            assertEquals(new Coordinate(2,2), array.getSpaceCoordinateFromSpace(even, Direction.down));
            assertEquals(new Coordinate(1,0), array.getSpaceCoordinateFromSpace(even, Direction.upleft));
            assertEquals(new Coordinate(1,1), array.getSpaceCoordinateFromSpace(even, Direction.downleft));
            assertEquals(new Coordinate(3,0), array.getSpaceCoordinateFromSpace(even, Direction.upright));
            assertEquals(new Coordinate(3,1), array.getSpaceCoordinateFromSpace(even, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromSpace(even, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromSpace(even, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromSpace(even, Direction.right);
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
            assertEquals(new Coordinate(7,0), array.getEdgeCoordinateFromEdge(zero, Direction.up));
            assertEquals(new Coordinate(7,1), array.getEdgeCoordinateFromEdge(zero, Direction.down));
            assertEquals(new Coordinate(5,1), array.getEdgeCoordinateFromEdge(zero, Direction.left));
            assertEquals(new Coordinate(8,1), array.getEdgeCoordinateFromEdge(zero, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromEdge(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(zero, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(zero, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,1), array.getEdgeCoordinateFromEdge(one, Direction.up));
            assertEquals(new Coordinate(6,2), array.getEdgeCoordinateFromEdge(one, Direction.down));
            assertEquals(new Coordinate(5,1), array.getEdgeCoordinateFromEdge(one, Direction.left));
            assertEquals(new Coordinate(8,2), array.getEdgeCoordinateFromEdge(one, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromEdge(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(one, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(one, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(7,0), array.getEdgeCoordinateFromEdge(two, Direction.upleft));
            assertEquals(new Coordinate(6,1), array.getEdgeCoordinateFromEdge(two, Direction.downleft));
            assertEquals(new Coordinate(10,0), array.getEdgeCoordinateFromEdge(two, Direction.upright));
            assertEquals(new Coordinate(9,1), array.getEdgeCoordinateFromEdge(two, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromEdge(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(two, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(10,0), array.getEdgeCoordinateFromEdge(three, Direction.up));
            assertEquals(new Coordinate(10,1), array.getEdgeCoordinateFromEdge(three, Direction.down));
            assertEquals(new Coordinate(8,1), array.getEdgeCoordinateFromEdge(three, Direction.left));
            assertEquals(new Coordinate(11,1), array.getEdgeCoordinateFromEdge(three, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromEdge(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(three, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(three, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(9,1), array.getEdgeCoordinateFromEdge(four, Direction.up));
            assertEquals(new Coordinate(9,2), array.getEdgeCoordinateFromEdge(four, Direction.down));
            assertEquals(new Coordinate(8,2), array.getEdgeCoordinateFromEdge(four, Direction.left));
            assertEquals(new Coordinate(11,1), array.getEdgeCoordinateFromEdge(four, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromEdge(four, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(four, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(four, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(four, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(four, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(9,1), array.getEdgeCoordinateFromEdge(five, Direction.upleft));
            assertEquals(new Coordinate(10,1), array.getEdgeCoordinateFromEdge(five, Direction.downleft));
            assertEquals(new Coordinate(12,1), array.getEdgeCoordinateFromEdge(five, Direction.upright));
            assertEquals(new Coordinate(13,1), array.getEdgeCoordinateFromEdge(five, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromEdge(five, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(five, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(five, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(five, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromEdge(five, Direction.right);
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
            assertEquals(new Coordinate(3,1), array.getVertexCoordinateFromVertex(zero, Direction.left));
            assertEquals(new Coordinate(5,1), array.getVertexCoordinateFromVertex(zero, Direction.upright));
            assertEquals(new Coordinate(5,2), array.getVertexCoordinateFromVertex(zero, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromVertex(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,1), array.getVertexCoordinateFromVertex(one, Direction.right));
            assertEquals(new Coordinate(4,0), array.getVertexCoordinateFromVertex(one, Direction.upleft));
            assertEquals(new Coordinate(4,1), array.getVertexCoordinateFromVertex(one, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromVertex(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(5,1), array.getVertexCoordinateFromVertex(two, Direction.left));
            assertEquals(new Coordinate(7,0), array.getVertexCoordinateFromVertex(two, Direction.upright));
            assertEquals(new Coordinate(7,1), array.getVertexCoordinateFromVertex(two, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromVertex(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), array.getVertexCoordinateFromVertex(three, Direction.right));
            assertEquals(new Coordinate(6,1), array.getVertexCoordinateFromVertex(three, Direction.upleft));
            assertEquals(new Coordinate(6,2), array.getVertexCoordinateFromVertex(three, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromVertex(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromVertex(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testEdgeCoordinateFromSpace() {
        Coordinate odd = new Coordinate(1,1);
        Coordinate even = new Coordinate(2,1);
        try {
            assertEquals(new Coordinate(5,1), array.getEdgeCoordinateFromSpace(odd, Direction.up));
            assertEquals(new Coordinate(5,2), array.getEdgeCoordinateFromSpace(odd, Direction.down));
            assertEquals(new Coordinate(4,1), array.getEdgeCoordinateFromSpace(odd, Direction.upleft));
            assertEquals(new Coordinate(3,2), array.getEdgeCoordinateFromSpace(odd, Direction.downleft));
            assertEquals(new Coordinate(7,1), array.getEdgeCoordinateFromSpace(odd, Direction.upright));
            assertEquals(new Coordinate(6,2), array.getEdgeCoordinateFromSpace(odd, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromSpace(odd, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromSpace(odd, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromSpace(odd, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), array.getEdgeCoordinateFromSpace(even, Direction.up));
            assertEquals(new Coordinate(8,2), array.getEdgeCoordinateFromSpace(even, Direction.down));
            assertEquals(new Coordinate(6,1), array.getEdgeCoordinateFromSpace(even, Direction.upleft));
            assertEquals(new Coordinate(7,1), array.getEdgeCoordinateFromSpace(even, Direction.downleft));
            assertEquals(new Coordinate(9,1), array.getEdgeCoordinateFromSpace(even, Direction.upright));
            assertEquals(new Coordinate(10,1), array.getEdgeCoordinateFromSpace(even, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromSpace(even, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromSpace(even, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromSpace(even, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

    @Test
    public void testVertexCoordinateFromSpace() {
        Coordinate odd = new Coordinate(1,1);
        Coordinate even = new Coordinate(2,1);
        try {
            assertEquals(new Coordinate(2,2), array.getVertexCoordinateFromSpace(odd, Direction.left));
            assertEquals(new Coordinate(5,2), array.getVertexCoordinateFromSpace(odd, Direction.right));
            assertEquals(new Coordinate(3,1), array.getVertexCoordinateFromSpace(odd, Direction.upleft));
            assertEquals(new Coordinate(3,2), array.getVertexCoordinateFromSpace(odd, Direction.downleft));
            assertEquals(new Coordinate(4,1), array.getVertexCoordinateFromSpace(odd, Direction.upright));
            assertEquals(new Coordinate(4,2), array.getVertexCoordinateFromSpace(odd, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromSpace(odd, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromSpace(odd, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromSpace(odd, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(4,1), array.getVertexCoordinateFromSpace(even, Direction.left));
            assertEquals(new Coordinate(7,1), array.getVertexCoordinateFromSpace(even, Direction.right));
            assertEquals(new Coordinate(5,1), array.getVertexCoordinateFromSpace(even, Direction.upleft));
            assertEquals(new Coordinate(5,2), array.getVertexCoordinateFromSpace(even, Direction.downleft));
            assertEquals(new Coordinate(6,1), array.getVertexCoordinateFromSpace(even, Direction.upright));
            assertEquals(new Coordinate(6,2), array.getVertexCoordinateFromSpace(even, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromSpace(even, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromSpace(even, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromSpace(even, Direction.down);
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
            assertEquals(new Coordinate(1,0), array.getSpaceCoordinateFromEdge(zero, Direction.upleft));
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromEdge(zero, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromEdge(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(zero, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(zero, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(1,1), array.getSpaceCoordinateFromEdge(one, Direction.downleft));
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromEdge(one, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromEdge(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(one, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(one, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,0), array.getSpaceCoordinateFromEdge(two, Direction.up));
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromEdge(two, Direction.down));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromEdge(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(two, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(two, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(two, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromEdge(three, Direction.downleft));
            assertEquals(new Coordinate(3,0), array.getSpaceCoordinateFromEdge(three, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromEdge(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(three, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(three, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromEdge(four, Direction.upleft));
            assertEquals(new Coordinate(3,1), array.getSpaceCoordinateFromEdge(four, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromEdge(four, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(four, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(four, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(four, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(four, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(four, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(four, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(3,0), array.getSpaceCoordinateFromEdge(five, Direction.up));
            assertEquals(new Coordinate(3,1), array.getSpaceCoordinateFromEdge(five, Direction.down));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromEdge(five, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(five, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(five, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(five, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(five, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(five, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromEdge(five, Direction.downright);
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
            assertEquals(new Coordinate(4,1), array.getVertexCoordinateFromEdge(zero, Direction.downleft));
            assertEquals(new Coordinate(5,1), array.getVertexCoordinateFromEdge(zero, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromEdge(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(zero, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(zero, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(4,1), array.getVertexCoordinateFromEdge(one, Direction.upleft));
            assertEquals(new Coordinate(5,2), array.getVertexCoordinateFromEdge(one, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromEdge(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(one, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(one, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(5,1), array.getVertexCoordinateFromEdge(two, Direction.left));
            assertEquals(new Coordinate(6,1), array.getVertexCoordinateFromEdge(two, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromEdge(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(two, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(two, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,1), array.getVertexCoordinateFromEdge(three, Direction.upleft));
            assertEquals(new Coordinate(7,1), array.getVertexCoordinateFromEdge(three, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromEdge(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(three, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(three, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(6,2), array.getVertexCoordinateFromEdge(four, Direction.downleft));
            assertEquals(new Coordinate(7,1), array.getVertexCoordinateFromEdge(four, Direction.upright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromEdge(four, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(four, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(four, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(four, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(four, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(four, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(four, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(7,1), array.getVertexCoordinateFromEdge(five, Direction.left));
            assertEquals(new Coordinate(8,1), array.getVertexCoordinateFromEdge(five, Direction.right));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getVertexCoordinateFromEdge(five, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(five, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(five, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(five, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(five, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(five, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getVertexCoordinateFromEdge(five, Direction.downright);
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
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromVertex(zero, Direction.right));
            assertEquals(new Coordinate(1,0), array.getSpaceCoordinateFromVertex(zero, Direction.upleft));
            assertEquals(new Coordinate(1,1), array.getSpaceCoordinateFromVertex(zero, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromVertex(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(zero, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(zero, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(zero, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(1,0), array.getSpaceCoordinateFromVertex(one, Direction.left));
            assertEquals(new Coordinate(2,0), array.getSpaceCoordinateFromVertex(one, Direction.upright));
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromVertex(one, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromVertex(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(one, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(one, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(one, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(3,0), array.getSpaceCoordinateFromVertex(two, Direction.right));
            assertEquals(new Coordinate(2,0), array.getSpaceCoordinateFromVertex(two, Direction.upleft));
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromVertex(two, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromVertex(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(two, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(two, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(two, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(2,1), array.getSpaceCoordinateFromVertex(three, Direction.left));
            assertEquals(new Coordinate(3,0), array.getSpaceCoordinateFromVertex(three, Direction.upright));
            assertEquals(new Coordinate(3,1), array.getSpaceCoordinateFromVertex(three, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getSpaceCoordinateFromVertex(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(three, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(three, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getSpaceCoordinateFromVertex(three, Direction.downleft);
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
            assertEquals(new Coordinate(5,1), array.getEdgeCoordinateFromVertex(zero, Direction.left));
            assertEquals(new Coordinate(6,1), array.getEdgeCoordinateFromVertex(zero, Direction.upright));
            assertEquals(new Coordinate(7,1), array.getEdgeCoordinateFromVertex(zero, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromVertex(zero, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(zero, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(zero, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(zero, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(zero, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(zero, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), array.getEdgeCoordinateFromVertex(one, Direction.right));
            assertEquals(new Coordinate(7,0), array.getEdgeCoordinateFromVertex(one, Direction.upleft));
            assertEquals(new Coordinate(6,1), array.getEdgeCoordinateFromVertex(one, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromVertex(one, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(one, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(one, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(one, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(one, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(one, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(8,1), array.getEdgeCoordinateFromVertex(two, Direction.left));
            assertEquals(new Coordinate(10,0), array.getEdgeCoordinateFromVertex(two, Direction.upright));
            assertEquals(new Coordinate(9,1), array.getEdgeCoordinateFromVertex(two, Direction.downright));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromVertex(two, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(two, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(two, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(two, Direction.right);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(two, Direction.upleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(two, Direction.downleft);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            assertEquals(new Coordinate(11,1), array.getEdgeCoordinateFromVertex(three, Direction.right));
            assertEquals(new Coordinate(9,1), array.getEdgeCoordinateFromVertex(three, Direction.upleft));
            assertEquals(new Coordinate(10,1), array.getEdgeCoordinateFromVertex(three, Direction.downleft));
        } catch (IllegalDirectionException e) {
            e.printStackTrace();
            fail();
        }
        try {
            array.getEdgeCoordinateFromVertex(three, Direction.center);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(three, Direction.up);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(three, Direction.down);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(three, Direction.left);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(three, Direction.upright);
            fail();
        } catch (IllegalDirectionException ignored) {}
        try {
            array.getEdgeCoordinateFromVertex(three, Direction.downright);
            fail();
        } catch (IllegalDirectionException ignored) {}
    }

}
