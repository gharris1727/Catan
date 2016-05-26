package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;
import com.gregswebserver.catan.common.util.ReversibleIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by greg on 2/27/16.
 * A random generator of development cards.
 */
public class DevelopmentDeckState implements ReversibleIterator<DevelopmentCard> {

    private int index;
    private final List<DevelopmentCard> deck;

    public DevelopmentDeckState(GameRules rules, long seed) {
        deck = new ArrayList<>();
        for (int i = 0; i < rules.getSoldierCount(); i++)
            deck.add(DevelopmentCard.Knight);
        for (int i = 0; i < rules.getMarketCount(); i++)
            deck.add(DevelopmentCard.Market);
        for (int i = 0; i < rules.getParliamentCount(); i++)
            deck.add(DevelopmentCard.Parliament);
        for (int i = 0; i < rules.getUniversityCount(); i++)
            deck.add(DevelopmentCard.University);
        for (int i = 0; i < rules.getChapelCount(); i++)
            deck.add(DevelopmentCard.Chapel);
        for (int i = 0; i < rules.getLibraryCount(); i++)
            deck.add(DevelopmentCard.Library);
        for (int i = 0; i < rules.getMonopolyCount(); i++)
            deck.add(DevelopmentCard.Monopoly);
        for (int i = 0; i < rules.getRoadbuildingCount(); i++)
            deck.add(DevelopmentCard.RoadBuilding);
        for (int i = 0; i < rules.getPlentyCount(); i++)
            deck.add(DevelopmentCard.YearOfPlenty);
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
