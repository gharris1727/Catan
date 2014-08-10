package com.gregswebserver.catan.game.board.hexarray;

import java.util.*;

/**
 * Created by Greg on 8/9/2014.
 * Management class for a 2d-array
 */
public class TwoDimensionalArray<T> extends AbstractCollection<T> {

    private final int MAX_SIZE = Integer.MAX_VALUE - 8;
    private int sizeX, sizeY;
    private Object[] data;

    public TwoDimensionalArray(int x, int y) {
        if (x < 0 || x >= MAX_SIZE || y < 0 || y >= MAX_SIZE) {
            throw new IllegalArgumentException(indexMessage(x, y, MAX_SIZE, MAX_SIZE));
        }
        sizeX = x;
        sizeY = y;
        data = new Object[x * y];
    }

    @SuppressWarnings("unchecked")
    public T get(int x, int y) {
        rangeCheck(x, y);
        return (T) data[x + y * sizeX];
    }

    public T get(Coordinate c) {
        return get(c.getX(), c.getY());
    }

    public T remove(int x, int y) {
        rangeCheck(x, y);
        T obj = get(x, y);
        data[x + y * sizeX] = null;
        return obj;
    }

    public T remove(Coordinate c) {
        return remove(c.getX(), c.getY());
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

    public ArrayList<Coordinate> getEmptyCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (get(x, y) == null) {
                    coordinates.add(new Coordinate(x, y));
                }
            }
        }
        return coordinates;
    }

    public ArrayList<Coordinate> getAllCoordinates() {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
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
        rangeCheck(c.getX(), c.getY());
    }

    private String indexMessage(int x, int y, int limitX, int limitY) {
        return "X: " + x + "/" + limitX + " Y: " + y + "/" + limitY;
    }

    @Override
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
        return sizeX * sizeY;
    }
}
