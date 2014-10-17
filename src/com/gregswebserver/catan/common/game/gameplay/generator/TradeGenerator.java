package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.gameplay.enums.TradingPost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Greg on 8/13/2014.
 * Helper class to generate TradingPosts.
 */
public class TradeGenerator implements FeatureGenerator<TradingPost> {

    private ArrayList<TradingPost> posts;

    public TradeGenerator(int numTradingPosts) {
        posts = new ArrayList<>(numTradingPosts);
        int specPosts;
        switch (numTradingPosts) {
            case 0:
                return;
            case 1:
            case 2:
            case 3:
            case 4:
                specPosts = 0;
                break;
            case 5:
            case 6:
            case 7:
                specPosts = 1;
                break;
            default:
                specPosts = numTradingPosts / 7; //Integer division intentional.
                break;
        }
        int randPosts = numTradingPosts - specPosts * 5;
        for (int i = 0; i < specPosts * 5; i++) {
            posts.add(TradingPost.values()[i % 5]);
        }
        for (int i = 0; i < randPosts; i++) {
            posts.add(TradingPost.Wildcard);
        }
    }

    public void randomize() {
        Collections.shuffle(posts);
    }

    public Iterator<TradingPost> iterator() {
        return posts.iterator();
    }
}
