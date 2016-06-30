package com.gregswebserver.catan.test.common.game;

import com.gregswebserver.catan.common.crypto.Username;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by greg on 6/29/16.
 * A set of utility functions for testing elements of gameplay.
 */
public class GameTestUtils {

    public static Set<Username> generatePlayerUsernames(int count) {
        Random random = new Random();
        Set<Username> users = new HashSet<>();
        int length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            length = random.nextInt(10)+4;
            builder.setLength(0);
            builder.ensureCapacity(length);
            for (int j = 0; j < length; j++)
                builder.append((char) (random.nextInt('z'-'a') + 'a'));
            users.add(new Username(builder.toString()));
        }
        return Collections.unmodifiableSet(users);
    }

}
