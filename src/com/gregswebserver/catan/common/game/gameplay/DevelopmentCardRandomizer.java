package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.gameplay.enums.DevelopmentCard;
import com.gregswebserver.catan.common.game.gameplay.rules.GameRules;

import java.util.*;

/**
 * Created by greg on 2/27/16.
 * A random generator of development cards.
 */
public class DevelopmentCardRandomizer implements Iterator<DevelopmentCard> {

    private final List<DevelopmentCard> deck;
    private final Iterator<DevelopmentCard> iterator;

    public DevelopmentCardRandomizer(GameRules rules, long seed) {
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
        iterator = deck.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public DevelopmentCard next() {
        return iterator.next();
    }
}
