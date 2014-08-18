package com.gregswebserver.catan.client.graphics;


import com.gregswebserver.catan.client.hitbox.HitboxColor;
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

    public static int transColor = 0xffff00ff;
    private int[] pixels, hitbox;
    private RenderMask mask;

    protected Graphic() {
        //Constructor in place for calling from subclasses.
        //Used primarily in the Screen class.
    }

    public Graphic(int width, int height) {
        this(new int[width * height], new RectangularMask(width, height));
    }

    public Graphic(GraphicSource source, RenderMask mask, Point start, int hitboxColor) {
        //Create an image from an ImageSource following the position and mask information.
        int arrayLen = mask.getPixelCount();
        pixels = new int[arrayLen];
        hitbox = new int[arrayLen];
        this.mask = mask;
        renderFrom(source, null, start, hitboxColor);
    }

    public Graphic(int[] pixels, RenderMask mask) {
        this.pixels = pixels;
        this.hitbox = new int[pixels.length];
        this.mask = mask;
        for (int i = 0; i < pixels.length; i++) {
            hitbox[i] = 0;
        }
    }

    private static void render(Graphic to, RenderMask toMask, Point toStart, Graphic from, RenderMask fromMask, Point fromStart, int color) {
        //If color is anything but black (0x0) that will be copied instead of the pre-existing hitbox.
        if (toMask == null)
            toMask = to.mask;
        if (fromMask == null)
            fromMask = from.mask;
        //Check for lower bounds.
        if (toStart.x < 0) toStart.x = 0;
        if (toStart.y < 0) toStart.y = 0;
        if (fromStart.x < 0) fromStart.x = 0;
        if (fromStart.y < 0) fromStart.y = 0;
        //Check for upper bounds
        //Note: if any of these are out of spec, nothing will get copied anyway.
        if (toStart.x >= toMask.getWidth()) return;
        if (toStart.y >= toMask.getHeight()) return;
        if (fromStart.x >= fromMask.getWidth()) return;
        if (fromStart.y >= fromMask.getHeight()) return;
        //Pull in the masks
        ArrayList<Integer> toPadding = toMask.getLeftPadding();
        ArrayList<Integer> toLength = toMask.getLineWidth();
        ArrayList<Integer> fromPadding = fromMask.getLeftPadding();
        ArrayList<Integer> fromLength = fromMask.getLineWidth();
        //Find how many rows we need to iterate.
        int numRows = toMask.getHeight() - toStart.y;
        if (numRows > (fromMask.getHeight() - fromStart.y)) numRows = (fromMask.getHeight() - fromStart.y);
        //Iterate over the rows going downward.
        for (int row = 0; row < numRows; row++) {
            //Find the row numbers that we are working with
            int toY = row + toStart.y;
            int fromY = row + fromStart.y;
            //Pull in the lengths
            int toLen = toLength.get(toY);
            int fromLen = fromLength.get(fromY);
            //Find where to start copying on each
            int toX = toPadding.get(toY);
            if (toX < toStart.x) { //If the padding is less than the start
                toLen -= (toStart.x - toX); //Cut that length off
                toX = toStart.x; //Start at the start
            } //Otherwise just use the padding.
            int fromX = fromPadding.get(fromY);
            if (fromX < fromStart.x) {
                fromLen -= (fromStart.x - fromX);
                fromX = fromStart.x;
            }
            //Find the length needed to copy
            int length = toLen;
            if (length > fromLen) length = fromLen;
            pixelCopy(from.pixels, from.mask.getIndex(fromX, fromY), 1, to.pixels, to.mask.getIndex(toX, toY), 1, length);
            if (color > 0)
                setMask(to.hitbox, to.mask.getIndex(toX, toY), 1, color, length);
            else
                pixelCopy(from.hitbox, from.mask.getIndex(fromX, fromY), 1, to.hitbox, to.mask.getIndex(toX, toY), 1, length);
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

    private static void setMask(int[] dst, int dstPos, int dstStep, int color, int length) {
        for (int i = 0; i < length; i++) {
            int dstCurr = i * dstStep + dstPos;
            dst[dstCurr] = color;
        }
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = transColor;
        }
    }

    public RenderMask getMask() {
        return mask;
    }

    public void setMask(RenderMask mask) {
        this.mask = mask;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public HitboxColor getHitboxColor(Point p) {
        return HitboxColor.get(hitbox[mask.getIndex(p.x, p.y)]);
    }

    public void renderTo(Graphic to, RenderMask toMask, Point toPos, int color) {
        //Renders this Image onto another image, with this image's top corner located at tPos on the destination image.
        render(to, toMask, toPos, this, null, new Point(), color);
    }

    public void renderFrom(Graphic from, RenderMask fromMask, Point fromPos, int color) {
        //Renders this Image from another, with the fromPos pixel being at this image's top corner.
        render(this, null, new Point(), from, fromMask, fromPos, color);
    }
}
