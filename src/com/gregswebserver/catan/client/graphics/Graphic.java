package com.gregswebserver.catan.client.graphics;


import com.gregswebserver.catan.client.renderer.Renderable;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Greg on 8/15/2014.
 * Utility class to provide pixel-moving functions to the other image handlers.
 */
public class Graphic implements Renderable {

    public static int transColor = 0xffff00ff;
    private int[] pixels, hitboxMask;
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
        hitboxMask = new int[arrayLen];
        this.mask = mask;
        renderFrom(source, start);
        for (int i = 0; i < arrayLen; i++) {
            hitboxMask[i] = hitboxColor;
        }
    }

    public Graphic(int[] pixels, RenderMask mask) {
        this.pixels = pixels;
        this.hitboxMask = new int[pixels.length];
        this.mask = mask;
        for (int i = 0; i < pixels.length; i++) {
            hitboxMask[i] = 0;
        }
    }

    private static void render(Graphic to, Point toStart, Graphic from, Point fromStart) {
        //Check for lower bounds.
        if (toStart.x < 0) toStart.x = 0;
        if (toStart.y < 0) toStart.y = 0;
        if (fromStart.x < 0) fromStart.x = 0;
        if (fromStart.y < 0) fromStart.y = 0;
        //Check for upper bounds
        //Note: if any of these are out of spec, nothing will get copied anyway.
        if (toStart.x >= to.mask.getWidth()) return;
        if (toStart.y >= to.mask.getHeight()) return;
        if (fromStart.x >= from.mask.getWidth()) return;
        if (fromStart.y >= from.mask.getHeight()) return;
        //Pull in the masks
        ArrayList<Integer> toPadding = to.mask.getLeftPadding();
        ArrayList<Integer> toLength = to.mask.getLineWidth();
        ArrayList<Integer> fromPadding = from.mask.getLeftPadding();
        ArrayList<Integer> fromLength = from.mask.getLineWidth();
        //Find how many rows we need to iterate.
        int numRows = to.mask.getHeight() - toStart.y;
        if (numRows > (from.mask.getHeight() - fromStart.y)) numRows = (from.mask.getHeight() - fromStart.y);
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
            pixelCopy(from.hitboxMask, from.mask.getIndex(fromX, fromY), 1, to.hitboxMask, to.mask.getIndex(toX, toY), 1, length);
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

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = transColor;
        }
    }

    public void setMask(RenderMask mask) {
        this.mask = mask;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public void renderTo(Graphic to, Point toPos) {
        //Renders this Image onto another image, with this image's top corner located at tPos on the destination image.
        render(to, toPos, this, new Point());
    }

    public void renderFrom(Graphic from, Point fromPos) {
        //Renders this Image from another, with the fromPos pixel being at this image's top corner.
        render(this, new Point(), from, fromPos);
    }
}
