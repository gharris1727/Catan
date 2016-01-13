package com.gregswebserver.catan.client.resources;

/**
 * Created by Greg on 1/6/2015.
 * A list of GraphicSource resources that can be loaded.
 */
public enum GraphicSourceInfo {

    Land("tiles/land"),
    Ocean("tiles/ocean"),
    BeachSingle("tiles/singleBeach"),
    BeachDouble("tiles/doubleBeach"),

    BlueUIBackground("ui/blue/background"),
    BlueUIWindow("ui/blue/window"),
    BlueUIText("ui/blue/text"),
    BlueUIButton("ui/blue/button"),

    BlueUIArrows("ui/blue/arrows"),

    EmptyTeam("teams/empty"),
    RedTeam("teams/red"),
    OrangeTeam("teams/orange"),
    BlueTeam("teams/blue"),
    WhiteTeam("teams/white"),

    TradeBridges("trade/bridges"),
    TradeRatios("trade/ratios"),
    TradeIcons("trade/icons"),

    Dice("game/dice"),
    ResourceCard("game/resource"),
    Development("game/development"),
    Achievement("game/achievement");

    private final String name;

    GraphicSourceInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "/graphics/" + name + ".png";
    }

}
