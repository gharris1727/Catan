package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.client.graphics.masks.Maskable;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.util.Animated;
import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;
import com.gregswebserver.catan.common.profiler.TimeSlice;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 1/1/2015.
 * A ScreenObject that contains other ScreenObjects.
 */
public abstract class ScreenRegion extends ScreenObject implements Renderable, Iterable<ScreenObject>, Graphical, Animated, Maskable {

    private RenderMask mask;
    private Graphic graphic;
    private boolean needsRendering;
    private Map<Integer, List<ScreenObject>> priorityMap;
    private Map<Integer, ScreenObject> clickableColorMap;
    private TimeSlice timeSlice;

    //The constructor of a screen region is meant to store relevant references,
    //and create any permanent ScreenObjects needed in the render.
    public ScreenRegion(int priority) {
        super(priority);
        clear();
    }

    @Override
    public final RenderMask getMask() {
        return mask;
    }

    //Sets the display size for this region, and notifies it's contents about the resizing.
    @Override
    public final void setMask(RenderMask mask) {
        this.mask = mask;
        graphic = null;
        if (mask != null) {
            graphic = new Graphic(mask);
            forceRender();
            resizeContents(mask);
            limitScroll();
        }
    }

    //Force this region to re-render even if none of it's children need it.
    @Override
    public final void forceRender() {
        needsRendering = true;
    }

    //Clear the screen of any objects. A render after this will not have anything visible.
    protected void clear() {
        priorityMap = new TreeMap<>();
        clickableColorMap = new HashMap<>();
    }

    //Gets the clickable object under a certain point in the screen.
    @Override
    public Clickable getClickable(Point p) {
        //Find out what the redirect would have been.
        Clickable result = super.getClickable(p);
        if (result == this && graphic != null) {
            //There was no redirect object specified, and we are already rendered.
            int color = graphic.getClickableColor(p);
            ScreenObject object = clickableColorMap.get(color);
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
            clickableColorMap.put(object.getClickableColor(), object);
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
            clickableColorMap.remove(object.getClickableColor());
            List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
            if (objects != null) {
                objects.remove(object);
                forceRender();
            }
        }
        return object;
    }

    public final Point center(ScreenObject other) {
        if (other != null && other.isGraphical()) {
            if (other instanceof Renderable)
                ((Renderable) other).assertRenderable();
            RenderMask mask = ((Graphical) other).getGraphic().getMask();
            int x = (this.mask.getWidth() - mask.getWidth()) / 2;
            int y = (this.mask.getHeight() - mask.getHeight()) / 2;
            other.setPosition(new Point(x, y));
            return other.getPosition();
        }
        return null;
    }

    //Iterator over every object in the screen in order of priority.
    @Override
    public final Iterator<ScreenObject> iterator() {
        return new Iterator<ScreenObject>() {

            private final Iterator<List<ScreenObject>> treeIterator = priorityMap.values().iterator();
            private Iterator<ScreenObject> listIterator;

            @Override
            public boolean hasNext() {
                return treeIterator.hasNext() || (listIterator != null && listIterator.hasNext());
            }

            @Override
            public ScreenObject next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                if (listIterator == null || !listIterator.hasNext())
                    listIterator = treeIterator.next().iterator();
                try {
                    return listIterator.next();
                } catch (ConcurrentModificationException e) {
                    System.out.println("CME in " + ScreenRegion.this);
                    throw e;
                }
            }
        };
    }

    @Override
    public final boolean isAnimated() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                return true;
        return false;
    }

    @Override
    public final void step() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).step();
    }

    @Override
    public final void reset() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).reset();
    }

    @Override
    public boolean isRenderable() {
        return mask != null && graphic != null;
    }

    @Override
    public void assertRenderable() {
        if (mask == null)
            throw new NotYetRenderableException(this + " has no mask");
        if (graphic == null)
            throw new NotYetRenderableException(this + " has no graphic");
    }

    @Override
    public final boolean needsRender() {
        if (limitScroll() || needsRendering) return true;
        for (ScreenObject object : this)
            if (object.needsRender())
                return true;
        return false;
    }

    @Override
    public final boolean isGraphical() {
        return true;
    }

    @Override
    public Graphic getGraphic() {
        timeSlice = new TimeSlice(this.toString());
        if (needsRender()) {
            renderContents();
            assertRenderable();
            graphic.clear();
            for (ScreenObject object : this)
                if (object.isGraphical()) {
                    Graphic g = ((Graphical) object).getGraphic();
                    g.renderTo(graphic, object.getPosition(), object.getClickableColor());
                    if (object instanceof Renderable)
                        timeSlice.addChild(((Renderable) object).getRenderTime());
                }
            needsRendering = false;
        }
        assertRenderable();
        timeSlice.markTime();
        return graphic;
    }

    @Override
    public TimeSlice getRenderTime() {
        return timeSlice;
    }

    //Notify all children of this region's size, and take the necessary steps to resize them.
    protected abstract void resizeContents(RenderMask mask);

    //Take the necessary steps to render the contents, such as adding or removing elements from the screen.
    protected void renderContents() { }

    protected boolean limitScroll() {
        return false;
    }
}
