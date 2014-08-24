package com.gregswebserver.catan.game.board.hexarray;

import java.util.*;

/**
 * Created by Greg on 8/9/2014.
 * Management class for a 2d-hexArray
 */
public class TwoDimensionalArray<T> extends AbstractCollection<T> {

    public static final int MAX_SIZE = Integer.MAX_VALUE - 8;
    public final int sizeX, sizeY;
    private int size;
    private Object[] data;

    public TwoDimensionalArray(int x, int y) {
        if (x < 0 || x >= MAX_SIZE || y < 0 || y >= MAX_SIZE) {
            throw new IllegalArgumentException(indexMessage(x, y, MAX_SIZE, MAX_SIZE));
        }
        sizeX = x;
        sizeY = y;
        data = new Object[x * y];
    }

    private boolean set(int x, int y, T o) {
        rangeCheck(x, y);
        data[x + y * sizeX] = o;
        return true;
    }

    public boolean set(Coordinate c, T o) {
        return set(c.x, c.y, o);
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

    public HashMap<Coordinate, T> toHashMap() {
        HashMap<Coordinate, T> coordinates = new HashMap<>();
        T obj;
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if ((obj = get(x, y)) != null) {
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

    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int currentX = 0;
            private int currentY = 0;

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

            public T next() {
                if (hasNext()) {
                    T obj = get(currentX, currentY);
                    increment();
                    return obj;
                } else {
                    throw new NoSuchElementException();
                }
            }

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
}
