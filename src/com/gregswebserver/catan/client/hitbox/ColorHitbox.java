package com.gregswebserver.catan.client.hitbox;

import com.gregswebserver.catan.client.graphics.Graphic;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Greg on 8/14/2014.
 * Hitbox for translating hitbox colors into objects.
 */
public class ColorHitbox implements Hitbox {

    private Graphic graphic;
    private HashMap<HitboxColor, Object> map;

    public ColorHitbox(Graphic graphic) {
        this.graphic = graphic;
        map = new HashMap<>();
    }

    public void setObject(HitboxColor c, Object o) {
        map.put(c, o);
    }

    public Object getObject(Point p) {
        HitboxColor c = graphic.getHitboxColor(p);
        Object o = map.get(c);
        if (o instanceof Hitbox) return ((Hitbox) o).getObject(p); //Send the point through, because this isn't a grid.
        return o;
    }
}
