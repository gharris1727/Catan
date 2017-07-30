package catan.common.game.gamestate;

import catan.common.game.scoring.rules.GameRules;
import catan.common.util.ReversibleIterator;

import java.util.*;

/**
 * Created by greg on 2/27/16.
 * A random generator of development cards.
 */
public class DevelopmentDeckState implements ReversibleIterator<DevelopmentCard> {

    private final List<DevelopmentCard> deck;
    private int index;

    public DevelopmentDeckState(GameRules rules, long seed) {
        int total = Arrays.stream(DevelopmentCard.values()).mapToInt(rules::getDevelopmentCardCount).sum();
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
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
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
        if ((o == null) || (getClass() != o.getClass())) return false;

        DevelopmentDeckState other = (DevelopmentDeckState) o;

        if (index != other.index) return false;
        return deck.equals(other.deck);
    }

    @Override
    public int hashCode() {
        int result = deck.hashCode();
        result = 31 * result + index;
        return result;
    }

    @Override
    public String toString() {
        return "DevelopmentDeckState{" +
                "index=" + index +
                ", deck=" + deck +
                '}';
    }
}
