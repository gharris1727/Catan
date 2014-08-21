package com.gregswebserver.catan.client.graphics;


import com.gregswebserver.catan.client.masks.RectangularMask;
import com.gregswebserver.catan.client.masks.RenderMask;
import com.gregswebserver.catan.client.renderer.Renderable;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Greg on 8/15/2014.
 * Utility class to provide pixel-moving functions to the other image handlers.
 */
public class Graphic implements Renderable {

    public static final int transColor = 0xffff00ff;
    protected int[] pixels, hitbox;
    private RenderMask mask;

    protected Graphic() {
    }

    public Graphic(Dimension d) {
        this(d.width, d.height);
    }

    public Graphic(int width, int height) {
        this(new int[width * height], new int[width * height], new RectangularMask(width, height));
        clear();
    }

    public Graphic(GraphicSource source, RenderMask mask, Point start, int hitboxColor) {
        this(new int[mask.getPixelCount()], new int[mask.getPixelCount()], mask);
        renderFrom(source, null, start, hitboxColor);
    }

    public Graphic(int[] pixels, int[] hitbox, RenderMask mask) {
        this.pixels = pixels;
        this.hitbox = hitbox;
        this.mask = mask;
    }

    private static void render(Graphic to, RenderMask toMask, Point toStart, Graphic from, RenderMask fromMask, Point fromStart, int color) {
        if (toMask == null)
            toMask = to.mask;
        if (fromMask == null)
            fromMask = from.mask;
        //Check for upper bounds, if any of these are out of spec, nothing will get copied anyway.
        if (toStart.x >= toMask.getWidth()) return;
        if (toStart.y >= toMask.getHeight()) return;
        if (fromStart.x >= fromMask.getWidth()) return;
        if (fromStart.y >= fromMask.getHeight()) return;
        //Pull in the masks
        ArrayList<Integer> toPadding = toMask.getLeftPadding();
        ArrayList<Integer> toLength = toMask.getLineWidth();
        ArrayList<Integer> fromPadding = fromMask.getLeftPadding();
        ArrayList<Integer> fromLength = fromMask.getLineWidth();
        //These are the differences between the from coordinates and the toCoordinates.
        //These must stay the same throughout execution.
        final int diffX = toStart.x - fromStart.x;
        final int diffY = toStart.y - fromStart.y;

        //Start at 0, and limit inward.
        int startX = 0;
        if (startX < fromStart.x) startX = fromStart.x;
        if (startX + diffX < 0) startX = -diffX;

        //Start at 0, and limit inward.
        int startY = 0;
        if (startY < fromStart.y) startY = fromStart.y;
        if (startY + diffY < 0) startY = -diffY;

        //Start at one height, and limit inward.
        int endY = fromMask.getHeight();
        if (endY + diffY > toMask.getHeight()) endY = toMask.getHeight() - diffY;

        for (int currY = startY; currY < endY; currY++) {
            int fromPad = fromPadding.get(currY);
            int fromLen = fromLength.get(currY);
            int toPad = toPadding.get(currY + diffY);
            int toLen = toLength.get(currY + diffY);
            //Start at one extreme and work inward.
            int currX = startX;
            if (currX < fromPad) currX = fromPad;
            if (currX + diffX < toPad) currX = toPad - diffX;
            //Start at
            int endX = fromPad + fromLen;
            if (endX + diffX > toPad + toLen) endX = toPad + toLen - diffX;
            //Find the length
            int length = endX - currX;
            if (length < 1) continue;
            //Copy
            pixelCopy(from.pixels, from.mask.getIndex(currX, currY), 1, to.pixels, to.mask.getIndex(currX + diffX, currY + diffY), 1, length);
            if (color > 0)
                colorCopy(to.hitbox, to.mask.getIndex(currX + diffX, currY + diffY), 1, color, length);
            else
                pixelCopy(from.hitbox, from.mask.getIndex(currX, currY), 1, to.hitbox, to.mask.getIndex(currX + diffX, currY + diffY), 1, length);
        }
    }

    private static void pixelCopy(int[] src, int srcPos, int srcStep, int[] dst, int dstPos, int dstStep, int length) {
        for (int i = 0; i < length; i++) {
            int srcCurr = i * srcStep + srcPos;
            int dstCurr = i * dstStep + dstPos;
            int color = src[srcCurr];
            if (color != transColor)
                dst[dstCurr] = color;
        }
    }

    private static void colorCopy(int[] dst, int dstPos, int dstStep, int color, int length) {
        for (int i = 0; i < length; i++) {
            int dstCurr = i * dstStep + dstPos;
            dst[dstCurr] = color;
        }
    }

    public void setMask(RenderMask mask) {
        this.mask = mask;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
        hitbox = new int[pixels.length];
    }

    public int getHitboxColor(Point p) {
        return hitbox[mask.getIndex(p.x, p.y)];
    }

    public void renderTo(Graphic to, RenderMask toMask, Point toPos, int color) {
        //Renders this Image onto another image, with this image's top corner located at tPos on the destination image.
        render(to, toMask, toPos, this, null, new Point(), color);
    }

    public void renderFrom(Graphic from, RenderMask fromMask, Point fromPos, int color) {
        //Renders this Image from another, with the fromPos pixel being at this image's top corner.
        render(this, null, new Point(), from, fromMask, fromPos, color);
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = transColor;
            hitbox[i] = 0;
        }
    }
}
