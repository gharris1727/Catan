package com.gregswebserver.catan.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by greg on 3/12/16.
 * A Pseudo-random number generator that is reversible.
 * Based on the MessageDigest's SHA implementation.
 */
public class ReversiblePRNG {

    private long seed;
    private static MessageDigest hash;

    static {
        try {
            hash = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            hash = null;
        }
    }

    public ReversiblePRNG() {
        this(System.nanoTime());
    }

    public ReversiblePRNG(long seed) {
        this.seed = seed;
    }

    private byte[] getBytes() {
        byte[] arr = new byte[8];
        for (int i = 0; i < 8; i++)
            arr[i] = (byte) (seed >> 8*i);
        hash.reset();
        return hash.digest(arr);
    }

    private int getInt() {
        byte[] bytes = getBytes();
        int out = 0;
        for (int i = 0; i < 4; i++) {
            out = out << (i * 8);
            out += bytes[i];
        }
        return out & 0x7fffffff;
    }

    public int nextInt(int limit) {
        seed++;
        return getInt() % limit;
    }

    public int prevInt(int limit) {
        int prev = getInt() % limit;
        seed--;
        return prev;
    }

    private long getLong() {
        byte[] bytes = getBytes();
        long out = 0L;
        for (int i = 0; i < 8; i++) {
            out = out << (i * 8);
            out += bytes[i];
        }
        return out & 0x7fffffffffffffffL;
    }

    public long nextLong(long limit) {
        seed++;
        return getLong() % limit;
    }

    public long prevLong(long limit) {
        long prev = getLong() % limit;
        seed--;
        return prev;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReversiblePRNG)) return false;

        ReversiblePRNG that = (ReversiblePRNG) o;

        return seed == that.seed;
    }

    @Override
    public int hashCode() {
        return (int) (seed ^ (seed >>> 32));
    }

    @Override
    public String toString() {
        return "ReversiblePRNG(" + seed + ")";
    }
}
