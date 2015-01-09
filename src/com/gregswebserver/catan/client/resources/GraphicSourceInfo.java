package com.gregswebserver.catan.client.resources;

/**
 * Created by Greg on 1/6/2015.
 * A list of GraphicSource resources that can be loaded.
 */
public enum GraphicSourceInfo {

    Tiles("tiles"),
    Ocean("ocean"),
    Beach("beach"),
    Blank("blank"),
    UI("ui"),
    Empty("empty"),
    Red("redTeam"),
    Orange("orangeTeam"),
    Blue("blueTeam"),
    White("whiteTeam"),
    Resource("resources"),
    Development("development"),
    Achievement("achievement"),
    Dice("tokens"),
    Icon("icons"),
    Dialog("dialogs"),
    Interface("interface"),
    Trade("trade");

    private final String name;

    GraphicSourceInfo(String name) {
        this.name = name;
    }

    public String getPath() {
        return "/graphics/" + name + ".png";
    }

}
