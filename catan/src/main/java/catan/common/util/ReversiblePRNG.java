package catan.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by greg on 3/12/16.
 * A Pseudo-random number generator that is reversible.
 * Based on the MessageDigest's SHA implementation.
 */
public class ReversiblePRNG {

    private long seed;
    private final MessageDigest hash;

    public ReversiblePRNG(long seed) {
        this.seed = seed;
        try {
            hash = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
    }

    public void next() {
        seed++;
    }

    public void prev() {
        seed--;
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
            out = out << 8;
            out |= bytes[i];
        }
        return out & 0x7fffffff;
    }

    public int getInt(int limit) {
        return getInt() % limit;
    }

    public int nextInt(int limit) {
        int next = getInt(limit);
        next();
        return next;
    }

    public int prevInt(int limit) {
        prev();
        return getInt(limit);
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
