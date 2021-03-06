package catan.common.game.gameplay.generator;

import catan.common.game.gameplay.trade.TradingPostType;

import java.util.*;

/**
 * Created by Greg on 8/13/2014.
 * Helper class to generate TradingPosts.
 */
public class RandomTradeGenerator implements FeatureGenerator<TradingPostType> {

    private final List<TradingPostType> posts;

    public RandomTradeGenerator(int numTradingPosts) {
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
        int randPosts = numTradingPosts - (specPosts * 5);
        for (int i = 0; i < (specPosts * 5); i++) {
            posts.add(TradingPostType.values()[i % 5]);
        }
        for (int i = 0; i < randPosts; i++) {
            posts.add(TradingPostType.Wildcard);
        }
    }

    @Override
    public void randomize(Random random) {
        Collections.shuffle(posts, random);
    }

    @Override
    public Iterator<TradingPostType> iterator() {
        return posts.iterator();
    }
}
