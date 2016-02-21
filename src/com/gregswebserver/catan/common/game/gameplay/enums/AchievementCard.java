package com.gregswebserver.catan.common.game.gameplay.enums;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different achievement cards.
 */
public enum AchievementCard  {

    //TODO: insert the correct descriptions.
    LongestRoad("Longest Road", "Description for Longest Road"),
    LargestArmy("Largest Army", "Description for Largest Army");

    public final String name;
    public final String description;

    AchievementCard(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
