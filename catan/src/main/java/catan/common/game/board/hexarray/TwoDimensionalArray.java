package catan.common.game.board.hexarray;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Greg on 8/9/2014.
 * Implementation of a two-dimensional array of elements, indexed by x/y int coordinates and Coordinate objects.
 */
public class TwoDimensionalArray<T> {

    private final int sizeX, sizeY;
    private final Object[] data;

    public TwoDimensionalArray(int x, int y) {
        sizeX = x;
        sizeY = y;
        data = new Object[x * y];
    }

    private void set(int x, int y, T o) {
        rangeCheck(x, y);
        data[x + y * sizeX] = o;
    }

    public void set(Coordinate c, T o) {
        set(c.x, c.y, o);
    }

    @SuppressWarnings("unchecked")
    public T get(int x, int y) {
        rangeCheck(x, y);
        return (T) data[x + y * sizeX];
    }

    public T get(Coordinate c) {
        return get(c.x, c.y);
    }

    public T remove(int x, int y) {
        rangeCheck(x, y);
        T obj = get(x, y);
        set(x, y, null);
        return obj;
    }

    public T remove(Coordinate c) {
        return remove(c.x, c.y);
    }

    public Set<Coordinate> coordinates() {
        Set<Coordinate> coordinates = new HashSet<>();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                T obj = get(x, y);
                if (obj != null) {
                    coordinates.add(new Coordinate(x, y));
                }
            }
        }
        return coordinates;
    }

    private void rangeCheck(int x, int y) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY) {
            throw new IndexOutOfBoundsException("X: " + x + "/" + sizeX + " Y: " + y + "/" + sizeY);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwoDimensionalArray<?> that = (TwoDimensionalArray<?>) o;

        if (sizeX != that.sizeX) return false;
        if (sizeY != that.sizeY) return false;
        return Arrays.equals(data, that.data);

    }

    @Override
    public String toString() {
        return "TwoDimensionalArray(" + sizeX + ',' + sizeY + ')';
    }
}
