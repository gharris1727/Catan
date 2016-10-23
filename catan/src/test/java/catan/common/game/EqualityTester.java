package catan.common.game;

/**
 * Created by greg on 7/9/16.
 * Test framework to assert the equality of two objects.
 */
public interface EqualityTester<T> {

    void assertEquals(T expected, T actual);

}
