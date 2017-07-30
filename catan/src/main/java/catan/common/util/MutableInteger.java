package catan.common.util;

/**
 * Created by greg on 12/20/16.
 * Class of a mutable integer wrapping object.
 */
public class MutableInteger {

    private int value;

    public void set(int value) {
        this.value = value;
    }

    public void inc(int value) {
        this.value += value;
    }

    public int get() {
        return value;
    }
}
