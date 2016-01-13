package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.util.Animated;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 1/1/2015.
 * A ScreenObject that contains other ScreenObjects.
 */
public abstract class ScreenRegion extends ScreenObject implements Iterable<ScreenObject>, Graphical, Animated {

    private RenderMask mask;
    private Graphic graphic;
    private boolean needsRendering;
    private Map<Integer, List<ScreenObject>> priorityMap;
    private Map<Integer, ScreenObject> hitboxMap;

    //The constructor of a screen region is meant to store relevant references,
    //and create any permanent ScreenObjects needed in the render.
    public ScreenRegion(int priority) {
        super(new Point(), priority);
        clear();
    }

    public final RenderMask getMask() {
        return mask;
    }

    //Sets the display size for this region, and notifies it's contents about the resizing.
    public final ScreenRegion setMask(RenderMask mask) {
        this.mask = mask;
        graphic = new Graphic(mask);
        forceRender();
        resizeContents(mask);
        limitScroll();
        return this;
    }

    //Force this region to re-render even if none of it's children need it.
    public final void forceRender() {
        needsRendering = true;
    }

    //Clear the screen of any objects. A render after this will not have anything visible.
    protected void clear() {
        priorityMap = new TreeMap<>();
        hitboxMap = new HashMap<>();
    }

    //Gets the clickable object under a certain point in the screen.
    public Clickable getClickable(Point p) {
        //Find out what the redirect would have been.
        Clickable result = super.getClickable(p);
        if (result == this) { //There was no redirect object specified.
            int color = getGraphic().getHitboxColor(p);
            ScreenObject object = hitboxMap.get(color);
            if (object != null) { //There was something clicked on
                Point position = object.getPosition();
                Point subPosition = new Point(p.x - position.x, p.y - position.y);
                result = object.getClickable(subPosition); //Go get the clickable from it.
            }
        }
        return result;
    }

    //Add an object to be rendered on the next render pass.
    public final ScreenObject add(ScreenObject object) {
        if (object != null) {
            hitboxMap.put(object.getHitboxColor(), object);
            List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
            if (objects == null) {
                objects = new LinkedList<>();
                priorityMap.put(object.getRenderPriority(), objects);
            }
            objects.add(object);
            forceRender();
        }
        return object;
    }

    //Remove a specific object from the screen.
    //This operation can be inefficient if there are many objects at the same render priority.
    public final ScreenObject remove(ScreenObject object) {
        if (object != null) {
            hitboxMap.remove(object.getHitboxColor());
            List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
            if (objects != null) {
                objects.remove(object);
                forceRender();
            }
        }
        return object;
    }

    // Get the coordinate that a child needs to be at to be centered in this region.
    public final Point getCenteredPosition(RenderMask other) {
        int x = (mask.getWidth() - other.getWidth()) / 2;
        int y = (mask.getHeight() - other.getHeight()) / 2;
        return new Point(x, y);
    }

    //Iterator over every object in the screen in order of priority.
    public final Iterator<ScreenObject> iterator() {
        return new Iterator<ScreenObject>() {

            private Iterator<List<ScreenObject>> treeIterator = priorityMap.values().iterator();
            private Iterator<ScreenObject> listIterator;

            public boolean hasNext() {
                return treeIterator.hasNext() || (listIterator != null && listIterator.hasNext());
            }

            public ScreenObject next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                if (listIterator == null || !listIterator.hasNext())
                    listIterator = treeIterator.next().iterator();
                return listIterator.next();
            }
        };
    }

    public final boolean isAnimated() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                return true;
        return false;
    }

    public final void step() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).step();
    }

    public final void reset() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).reset();
    }

    public final boolean needsRender() {
        if (limitScroll() || needsRendering) return true;
        for (ScreenObject object : this)
            if (object.needsRender())
                return true;
        return false;
    }

    public final boolean isGraphical() {
        return true;
    }

    public Graphic getGraphic() {
        if (needsRender()) {
            renderContents();
            if (graphic == null)
                throw new IllegalStateException("Screen Region " + this + " has no size.");
            graphic.clear();
            for (ScreenObject object : this)
                if (object.isGraphical())
                    ((Graphical) object).getGraphic().renderTo(graphic, object.getPosition(), object.getHitboxColor());
            needsRendering = false;
        }
        return graphic;
    }

    //Notify all children of this region's size, and take the necessary steps to resize them.
    protected abstract void resizeContents(RenderMask mask);

    //Take the necessary steps to render the contents, such as adding or removing elements from the screen.
    protected void renderContents() { };

    protected boolean limitScroll() {
        return false;
    }

}
