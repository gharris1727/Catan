package catan.common.game.gamestate;

import catan.common.game.scoring.rules.GameRules;
import catan.common.util.ReversibleIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by greg on 2/27/16.
 * A random generator of development cards.
 */
public class DevelopmentDeckState implements ReversibleIterator<DevelopmentCard> {

    private final List<DevelopmentCard> deck;
    private int index;

    public DevelopmentDeckState(GameRules rules, long seed) {
        int total = 0;
        for (DevelopmentCard card : DevelopmentCard.values())
            total += rules.getDevelopmentCardCount(card);
        deck = new ArrayList<>(total);
        for (DevelopmentCard card : DevelopmentCard.values())
            for (int i = 0; i < rules.getDevelopmentCardCount(card); i++)
                deck.add(card);
        Collections.shuffle(deck, new Random(seed));
    }

    @Override
    public boolean hasNext() {
        return index < deck.size();
    }

    @Override
    public boolean hasPrev() {
        return index > 0;
    }

    @Override
    public DevelopmentCard next() {
        return deck.get(index++);
    }

    @Override
    public DevelopmentCard prev() {
        return deck.get(--index);
    }

    @Override
    public DevelopmentCard get() {
        return deck.get(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DevelopmentDeckState that = (DevelopmentDeckState) o;

        if (index != that.index) return false;
        return deck.equals(that.deck);
    }

    @Override
    public String toString() {
        return "DevelopmentDeckState{" +
                "index=" + index +
                ", deck=" + deck +
                '}';
    }
}
