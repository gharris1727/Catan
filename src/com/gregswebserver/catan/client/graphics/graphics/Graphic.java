package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.Maskable;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by Greg on 8/15/2014.
 * Utility class to provide pixel-moving functions to the other image handlers.
 */
public class Graphic implements Maskable {

    public static final int transColor = 0xff00ff;
    protected int[] pixels = null, clickable = null;
    private RenderMask mask;
    private BufferedImage buffer;

    protected Graphic() {
    }

    public Graphic(Graphic graphic) {
        this(graphic, graphic.getMask(), new Point());
        this.clickable = graphic.clickable;
    }

    //For creating Graphics from other Graphics (usually StaticGraphics).
    public Graphic(Graphic source, RenderMask mask, Point start)  {
        this(mask);
        BufferedImage part = source.buffer.getSubimage(start.x, start.y, mask.getWidth(), mask.getHeight());
        buffer.getGraphics().drawImage(part, 0, 0, mask.getWidth(), mask.getHeight(), null);
    }

    //Primary constructor for a blank Graphic object.
    public Graphic(RenderMask mask) {
        this(mask, new BufferedImage(mask.getWidth(), mask.getHeight(), TYPE_INT_RGB));
    }

    public Graphic(RenderMask mask, BufferedImage buffer) {
        init(mask, buffer);
    }

    protected void init(RenderMask mask, BufferedImage buffer) {
        this.mask = mask;
        this.buffer = buffer;
    }

    protected void loadRaster() {
        if (pixels == null)
            pixels = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
        if (clickable == null)
            clickable = new int[pixels.length];
    }

    private static void render(Graphic to, Point toStart, Graphic from, Point fromStart, int color, boolean trans) {
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
            pixelCopy(from.pixels, from.mask.getIndex(currX, currY), 1, to.pixels, to.mask.getIndex(currX + diffX, currY + diffY), 1, length, trans);
            colorCopy(to.clickable, to.mask.getIndex(currX + diffX, currY + diffY), 1, color, length);
        }
    }

    private static void pixelCopy(int[] src, int srcPos, int srcStep, int[] dst, int dstPos, int dstStep, int length, boolean trans) {
        if (!trans && srcStep == 1 && dstStep == 1)
            System.arraycopy(src, srcPos, dst, dstPos, length);
        else {
            for (int i = 0; i < length; i++) {
                int srcCurr = i * srcStep + srcPos;
                int dstCurr = i * dstStep + dstPos;
                int color = src[srcCurr];
                if (trans && color != transColor)
                    dst[dstCurr] = color;
            }
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

    @Override
    public RenderMask getMask() {
        return mask;
    }

    @Override
    public void setMask(RenderMask mask) {
        throw new RuntimeException("You cant resize a graphic dummy.");
    }

    public int getClickableColor(Point p) {
        if (clickable == null)
            return 0;
        return clickable[mask.getIndex(p)];
    }

    public void renderTo(Graphic to, Point toPos, int color) {
        //to.buffer.getGraphics().drawImage(buffer,toPos.x,toPos.y,mask.getWidth(),mask.getHeight(),null);
        //Renders this Image onto another image, with this image's top corner located at tPos on the destination image.
        loadRaster();
        render(to, toPos, this, new Point(), color, true);
    }

    public void clear() {
        loadRaster();
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = transColor;
            clickable[i] = 0;
        }
    }

    public String toString() {
        return "Graphic(" + mask.getWidth() + "/" + mask.getHeight() + ")";
    }

    public void displayClickableColor() {
        loadRaster();
        pixelCopy(clickable, 0, 1, pixels, 0, 1, clickable.length, false);
    }

    public Image getBuffer() {
        return buffer;
    }
}
