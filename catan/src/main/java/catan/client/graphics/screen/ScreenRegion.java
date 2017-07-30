package catan.client.graphics.screen;

import catan.client.graphics.graphics.Graphic;
import catan.client.graphics.graphics.HitboxGraphic;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.Resizable;
import catan.client.input.Clickable;
import catan.client.renderer.NotYetRenderableException;
import catan.client.renderer.RenderThread;
import catan.common.profiler.TimeSlice;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 1/1/2015.
 * A ScreenObject that contains other ScreenObjects.
 */
public abstract class ScreenRegion extends ScreenObject implements Iterable<ScreenObject>, Animated, Resizable {

    private final TimeSlice timeSlice;
    private RenderMask mask;
    private Graphic graphic;
    private HitboxGraphic pixelHitbox;
    private boolean transparency;
    private boolean needsRendering;
    private Map<Integer, List<ScreenObject>> priorityMap;
    private Map<Integer, ScreenObject> clickableColorMap;

    //The constructor of a screen region is meant to store relevant references,
    //and create any permanent ScreenObjects needed in the render.
    protected ScreenRegion(String name, int priority) {
        super(name, priority);
        timeSlice = new TimeSlice(toString());
        clear();
    }

    @Override
    public void setRenderer(RenderThread renderer) {
        super.setRenderer(renderer);
        for (ScreenObject object : this)
            object.setRenderer(renderer);
    }

    public final void enableTransparency() {
        transparency = true;
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
        pixelHitbox = null;
        if (mask != null) {
            graphic = new Graphic(mask, transparency);
            pixelHitbox = new HitboxGraphic(mask);
            forceRender();
            resizeContents(mask);
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
        //TODO: if possible, use rectangular collision boxes rather than pixel based collisions.
        //Find out what the redirect would have been.
        Clickable result = super.getClickable(p);
        if ((result == this) && (pixelHitbox != null) && mask.containsPoint(p)) {
            //There was no redirect object specified, and we are already rendered.
            int color = pixelHitbox.getClickableColor(p);
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
    protected ScreenObject add(ScreenObject object) {
        if (object != null) {
            clickableColorMap.put(object.getClickableColor(), object);
            List<ScreenObject> objects = priorityMap.computeIfAbsent(object.getRenderPriority(), k -> new LinkedList<>());
            objects.add(object);
            forceRender();
        }
        return object;
    }

    //Remove a specific object from the screen.
    //This operation can be inefficient if there are many objects at the same render priority.
    protected void remove(ScreenObject object) {
        if (object != null) {
            clickableColorMap.remove(object.getClickableColor());
            List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
            if (objects != null) {
                objects.remove(object);
                forceRender();
            }
        }
    }

    public final Point center(ScreenObject object) {
        if (object != null) {
            RenderMask mask = object.getGraphic().getMask();
            int x = (this.mask.getWidth() - mask.getWidth()) / 2;
            int y = (this.mask.getHeight() - mask.getHeight()) / 2;
            object.setPosition(new Point(x, y));
            return object.getPosition();
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
                return treeIterator.hasNext() || ((listIterator != null) && listIterator.hasNext());
            }

            @Override
            public ScreenObject next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                if ((listIterator == null) || !listIterator.hasNext())
                    listIterator = treeIterator.next().iterator();
                return listIterator.next();
            }
        };
    }

    @Override
    public final void step() {
        for (ScreenObject object : this)
            if (object instanceof Animated)
                ((Animated) object).step();
    }

    @Override
    public final void reset() {
        for (ScreenObject object : this)
            if (object instanceof Animated)
                ((Animated) object).reset();
    }

    @Override
    public boolean isRenderable() {
        return mask != null;
    }

    @Override
    public void assertRenderable() {
        if (mask == null)
            throw new NotYetRenderableException(this + " has no mask");
    }

    @Override
    public final boolean needsRender() {
        if (needsRendering) return true;
        for (ScreenObject object : this)
            if (object.needsRender())
                return true;
        return false;
    }

    @Override
    public Graphic getGraphic() {
        timeSlice.reset();
        if (needsRender()) {
            renderContents();
            assertRenderable();
            graphic.clear();
            pixelHitbox.clear();
            for (ScreenObject object : this)
                try {
                    Graphic g = object.getGraphic();
                    graphic.renderFrom(g, object.getPosition(), object.getClickableColor());
                    pixelHitbox.renderFrom(g, object.getPosition(), object.getClickableColor());
                    timeSlice.addChild(object.getRenderTime());
                } catch (Exception e) {
                    throw new NotYetRenderableException("Unable to render " + object, e);
                }
            needsRendering = false;
        }
        assertRenderable();
        timeSlice.mark();
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
}
