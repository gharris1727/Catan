package com.gregswebserver.catan.common.game.board.layout;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.resources.ConfigFile;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Greg on 8/9/2014.
 * Data about a static board layout of tiles and trading posts.
 */
public class StaticBoardLayout implements BoardLayout {

    private final String name;
    private final Dimension size;
    private final Coordinate robber;
    private final int tileCount;
    private final int desertCount;
    private final int portCount;
    private final LinkedList<Coordinate> tiles;
    private final LinkedList<Coordinate> ports;
    private final LinkedList<Terrain> terrain;
    private final LinkedList<DiceRoll> rolls;
    private final LinkedList<TradingPostType> posts;

    public StaticBoardLayout(String path) throws IOException {
        ConfigFile file = new ConfigFile(path,"Board layout information");
        file.open();
        name = parseName(file);
        size = parseSize(file);
        tiles = parseTiles(file);
        ports = parsePorts(file);
        terrain = parseTerrain(file);
        rolls = parseRolls(file);
        posts = parsePosts(file);
        robber = parseRobber(file);
        tileCount = tiles.size();
        desertCount = parseDesertCount(file);
        portCount = ports.size();
    }

    private String parseName(ConfigFile file) {
        return file.get("name");
    }

    @Override
    public String getName() {
        return name;
    }

    private Dimension parseSize(ConfigFile file) {
        return file.getDimension("size");
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    //Depends on parseTerrain(), parseTiles()
    private Coordinate parseRobber(ConfigFile file) {
        Iterator<Coordinate> tiles = getTiles();
        Iterator<Terrain> terrain = getTerrain();
        //Look through all of the terrain tiles
        while (tiles.hasNext() && terrain.hasNext()) {
            Coordinate c = tiles.next();
            if (terrain.next().equals(Terrain.Desert))
                return c;
        }
        //We looked through all of the tiles and didn't find a desert. No robber in file.
        return null;
    }

    @Override
    public Coordinate getRobber() {
        return robber;
    }

    @Override
    public int getTileCount() {
        return tileCount;
    }

    //Depends on parseTerrain()
    private int parseDesertCount(ConfigFile file) {
        int count = 0;
        try {
            //Check for explicit desert count.
            count = file.getInt("deserts");
        } catch (Exception e) {
            //If the user didn't specify the number of deserts, it must be implicit
            for (Terrain t : terrain)
                if (t.equals(Terrain.Desert))
                    count++;
        }
        return count;
    }

    @Override
    public int getDesertCount() {
        return desertCount;
    }

    @Override
    public int getPortCount() {
        return portCount;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private LinkedList<Coordinate> parseTiles(ConfigFile file) {
        LinkedList<Coordinate> list = new LinkedList<>();
        try {
            for (int i = 0; true; i++)
                list.add(file.getCoord("resource." + i));
        } catch (Exception e) {
            //The above loop runs until the list of tiles bottoms out and excepts.
        }
        return list;
    }

    @Override
    public Iterator<Coordinate> getTiles() {
        return tiles.iterator();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private LinkedList<Coordinate> parsePorts(ConfigFile file) {
        LinkedList<Coordinate> list = new LinkedList<>();
        try {
            for (int i = 0; true; i++)
                list.add(file.getCoord("trade." + i));
        } catch (Exception e) {
            //The above loop runs until the list of tiles bottoms out and excepts.
        }
        return list;
    }
    @Override
    public Iterator<Coordinate> getPorts() {
        return ports.iterator();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private LinkedList<Terrain> parseTerrain(ConfigFile file) {
        LinkedList<Terrain> list = new LinkedList<>();
        try {
            for (int i = 0; true; i++) {
                Terrain found;
                switch (file.get("resource." + i + ".type").toUpperCase()) {
                    case "SHEEP":
                        found = Terrain.Pasture;
                        break;
                    case "CLAY":
                        found = Terrain.Hill;
                        break;
                    case "STONE":
                        found = Terrain.Mountain;
                        break;
                    case "WHEAT":
                        found = Terrain.Field;
                        break;
                    case "WOOD":
                        found = Terrain.Forest;
                        break;
                    case "DESERT":
                        found = Terrain.Desert;
                        break;
                    default:
                        throw new Exception("NO TERRAIN FOUND");
                }
                list.add(found);
            }
        } catch (Exception e) {
            //The above loop runs until the list bottoms out and excepts.
        }
        return list;
    }

    @Override
    public Iterator<Terrain> getTerrain() {
        return terrain.iterator();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private LinkedList<DiceRoll> parseRolls(ConfigFile file) {
        LinkedList<DiceRoll> list = new LinkedList<>();
        try {
            for (int i = 0; true; i++)
                list.add(DiceRoll.get(file.getInt("resource." + i + ".roll")));
        } catch (Exception e) {
            //The above loop runs until the list of tiles bottoms out and excepts.
        }
        return list;
    }

    @Override
    public Iterator<DiceRoll> getRolls() {
        return rolls.iterator();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private LinkedList<TradingPostType> parsePosts(ConfigFile file) {
        LinkedList<TradingPostType> list = new LinkedList<>();
        try {
            for (int i = 0; true; i++) {
                TradingPostType found;
                switch (file.get("trade." + i + ".type").toUpperCase()) {
                    case "SHEEP":
                        found = TradingPostType.Wool;
                        break;
                    case "CLAY":
                        found = TradingPostType.Brick;
                        break;
                    case "STONE":
                        found = TradingPostType.Ore;
                        break;
                    case "WHEAT":
                        found = TradingPostType.Grain;
                        break;
                    case "WOOD":
                        found = TradingPostType.Lumber;
                        break;
                    case "ANY":
                        found = TradingPostType.Wildcard;
                        break;
                    default:
                        throw new Exception("NO RESOURCE FOUND");
                }
                posts.add(found);
            }
        } catch (Exception e) {
            //The above loop runs until the list of tiles bottoms out and excepts.
        }
        return list;
    }

    @Override
    public Iterator<TradingPostType> getPosts() {
        return posts.iterator();
    }

    public String toString() {
        return "StaticBoardLayout " + name;
    }
}
