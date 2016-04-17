package com.gregswebserver.catan.common.game.board.hexarray;

import java.util.*;

/**
 * Created by Greg on 8/9/2014.
 * Management class for a 2d-hexArray
 */
public class TwoDimensionalArray<T> implements Iterable<T> {

    private final int sizeX, sizeY;
    private final Object[] data;
    private int size;

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
        if (obj != null) size--;
        return obj;
    }

    public T remove(Coordinate c) {
        return remove(c.x, c.y);
    }

    public Map<Coordinate, T> toMap() {
        Map<Coordinate, T> coordinates = new HashMap<>();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                T obj = get(x, y);
                if (obj != null) {
                    coordinates.put(new Coordinate(x, y), obj);
                }
            }
        }
        return coordinates;
    }

    public HashSet<Coordinate> getEmptyCoordinates() {
        HashSet<Coordinate> coordinates = new HashSet<>();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (get(x, y) == null) {
                    coordinates.add(new Coordinate(x, y));
                }
            }
        }
        return coordinates;
    }

    public HashSet<Coordinate> getAllCoordinates() {
        HashSet<Coordinate> coordinates = new HashSet<>();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                coordinates.add(new Coordinate(x, y));
            }
        }
        return coordinates;
    }

    public void rangeCheck(int x, int y) {
        if (x < 0 || x >= sizeX || y < 0 || y >= sizeY) {
            throw new IndexOutOfBoundsException(indexMessage(x, y, sizeX, sizeY));
        }
    }

    public void rangeCheck(Coordinate c) {
        rangeCheck(c.x, c.y);
    }

    private String indexMessage(int x, int y, int limitX, int limitY) {
        return "X: " + x + "/" + limitX + " Y: " + y + "/" + limitY;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int currentX = 0;
            private int currentY = 0;

            @Override
            public boolean hasNext() {
                try {
                    while (get(currentX, currentY) == null) { //Either returns null or throws an exception.
                        increment();
                        rangeCheck(currentX, currentY);
                    }
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    return false;
                }
            }

            @Override
            public T next() {
                if (hasNext()) {
                    T obj = get(currentX, currentY);
                    increment();
                    return obj;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
            }

            private void increment() {
                currentX++;
                if (currentX >= sizeX) {
                    currentX = 0;
                    currentY++;
                }
            }
        };
    }

    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TwoDimensionalArray<?> that = (TwoDimensionalArray<?>) o;

        if (sizeX != that.sizeX) return false;
        if (sizeY != that.sizeY) return false;
        if (size != that.size) return false;
        return Arrays.equals(data, that.data);

    }

    @Override
    public String toString() {
        return "TwoDimensionalArray{" +
                "sizeX=" + sizeX +
                ", sizeY=" + sizeY +
                ", size=" + size +
                '}';
    }
}
