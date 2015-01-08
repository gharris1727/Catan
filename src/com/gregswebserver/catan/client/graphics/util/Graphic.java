package com.gregswebserver.catan.client.graphics.util;


import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Greg on 8/15/2014.
 * Utility class to provide pixel-moving functions to the other image handlers.
 */
public class Graphic {

    public static final int transColor = 0xffff00ff;
    protected String name;
    protected int[] pixels, hitbox;
    private RenderMask mask;

    protected Graphic() {
    }

    //For use in the ObjectArea class to create a new, blank graphic.
    public Graphic(Dimension size) {
        this(new RectangularMask(size));
        clear();
    }

    //For use in the Statics class to create graphics from source files.
    public Graphic(Graphic source, RenderMask mask, Point start) {
        this(mask);
        render(this, new Point(), source, start, 0);
    }

    //For use in the BoardObject subclasses for complex
    public Graphic(RenderMask mask) {
        this(new int[mask.getPixelCount()], new int[mask.getPixelCount()], mask);
    }

    //Underlying constructor
    protected Graphic(int[] pixels, int[] hitbox, RenderMask mask) {
        this.pixels = pixels;
        this.hitbox = hitbox;
        this.mask = mask;
        this.name = "Graphic " + mask + " Pixels: " + mask.getPixelCount();
    }

    private static void render(Graphic to, Point toStart, Graphic from, Point fromStart, int color) {
        RenderMask toMask = to.mask;
        RenderMask fromMask = from.mask;
        //Check for upper bounds, if any of these are out of spec, nothing will get copied anyway.
        if (toStart.x >= toMask.getWidth()) return;
        if (toStart.y >= toMask.getHeight()) return;
        if (fromStart.x >= fromMask.getWidth()) return;
        if (fromStart.y >= fromMask.getHeight()) return;
        //Pull in the masks
        int[] toPadding = toMask.getPadding();
        int[] toLength = toMask.getWidths();
        int[] fromPadding = fromMask.getPadding();
        int[] fromLength = fromMask.getWidths();
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
            int fromPad = fromPadding[currY];
            int fromLen = fromLength[currY];
            int toPad = toPadding[currY + diffY];
            int toLen = toLength[currY + diffY];
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
            try {
                pixelCopy(from.pixels, from.mask.getIndex(currX, currY), 1, to.pixels, to.mask.getIndex(currX + diffX, currY + diffY), 1, length);
                if (color > 0)
                    colorCopy(to.hitbox, to.mask.getIndex(currX + diffX, currY + diffY), 1, color, length);
                else
                    pixelCopy(from.hitbox, from.mask.getIndex(currX, currY), 1, to.hitbox, to.mask.getIndex(currX + diffX, currY + diffY), 1, length);
            } catch (Exception e) {
                Main.logger.debug(null, e.toString());
                Main.logger.debug(null, "X/" + startX + "/" + currX + "/" + endX + " Y/" + startY + "/" + currY + "/" + endY + " L/" + length);
            }
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
        if (dstStep == 1)
            Arrays.fill(dst, dstPos, dstPos + length, color);
        else {
            for (int i = 0; i < length; i++) {
                int dstCurr = i * dstStep + dstPos;
                dst[dstCurr] = color;
            }
        }
    }

    public RenderMask getMask() {
        return mask;
    }

    protected void setMask(RenderMask mask) {
        this.mask = mask;
    }

    protected void setPixels(int[] pixels) {
        this.pixels = pixels;
        hitbox = new int[pixels.length];
    }

    public int getHitboxColor(Point p) {
        return hitbox[mask.getIndex(p.x, p.y)];
    }

    public void renderTo(Graphic to, Point toPos, int color) {
        //Renders this Image onto another image, with this image's top corner located at tPos on the destination image.
        render(to, toPos, this, new Point(), color);
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = transColor;
            hitbox[i] = 0;
        }
    }

    public String toString() {
        return name;
    }

    public void displayHitbox() {
        pixels = hitbox;
    }
}
