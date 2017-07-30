package catan.common.game.players;

import catan.common.game.util.EnumAccumulator;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Created by Greg on 8/13/2014.
 * Enum of purchase-able items for use in the trading framework.
 */
public enum Purchase implements EnumCounter<GameResource> {

    Road(new GameResource[]{GameResource.Brick, GameResource.Lumber}),
    Settlement(new GameResource[]{GameResource.Brick, GameResource.Lumber, GameResource.Grain, GameResource.Wool}),
    City(new GameResource[]{GameResource.Grain, GameResource.Grain, GameResource.Ore, GameResource.Ore, GameResource.Ore}),
    DevelopmentCard(new GameResource[]{GameResource.Wool, GameResource.Grain, GameResource.Ore});

    private final EnumCounter<GameResource> cost;

    Purchase(GameResource[] resources) {
        cost = new EnumAccumulator<>(GameResource.class, resources);
    }

    @Override
    public int get(GameResource e) {
        return cost.get(e);
    }

    @Override
    public Iterator<GameResource> iterator() {
        return new Iterator<GameResource>() {
            private int index;
            @Override
            public boolean hasNext() {
                return index < GameResource.values().length;
            }
            @Override
            public GameResource next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return GameResource.values()[index++];
            }
        };
    }

    @Override
    public void forEach(Consumer<? super GameResource> consumer) {
        cost.forEach(consumer);
    }
}
