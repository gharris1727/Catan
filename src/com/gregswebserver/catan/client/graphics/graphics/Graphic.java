package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.Maskable;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
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

    private static final int transColor = 0xff00ff; //Transparency color (pink)
    protected int[] pixels = null; //Pixel array for rasterization.
    private int[] clickable = null; //Clickable pixel array.
    private RenderMask mask; //Details about the image size and shape.
    protected BufferedImage buffer; //Hardware accelerated form of the graphic.
    private boolean transparency; //Flag on whether to use transparency.

    //Constructor to allow subclasses to do their own instantiation.
    protected Graphic() {
    }

    //Resource constructor, for cutting graphics from larger graphics.
    public Graphic(Graphic source, RenderMask mask, Point start, boolean transparency)  {
        this(mask, transparency);
        BufferedImage part = source.buffer.getSubimage(start.x, start.y, mask.getWidth(), mask.getHeight());
        buffer.getGraphics().drawImage(part, 0, 0, mask.getWidth(), mask.getHeight(), null);
    }

    //Primary constructor for a blank Graphic object.
    public Graphic(RenderMask mask, boolean transparency) {
        init(mask, transparency);
        clear();
    }

    //Initialize the graphic with the specified mask and transparency.
    protected void init(RenderMask mask, boolean transparency) {
        this.mask = mask;
        this.buffer = new BufferedImage(mask.getWidth(), mask.getHeight(), TYPE_INT_RGB);
        this.clickable = new int[mask.getWidth() * mask.getHeight()];
        this.transparency = transparency;
    }

    //Rasterize the image to the pixel buffer for manual rendering. De-accelerates the graphic.
    protected void loadRaster() {
        if (pixels == null)
            pixels = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
    }

    //Find out whether the graphic is hardware accelerated or not.
    private boolean accelerated() {
        return pixels == null && !transparency && mask instanceof RectangularMask;
    }

    //Render one graphic to another using their render masks.
    private static void maskRender(Graphic to, Point toStart, Graphic from, Point fromStart, int color, boolean trans) {
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
            fillColor(to.clickable, to.mask.getIndex(currX + diffX, currY + diffY), 1, color, length);
        }
    }

    //Copy pixels and clickable pixels, using faster method if possible.
    private static void pixelCopy(int[] src, int srcPos, int srcStep, int[] dst, int dstPos, int dstStep, int length, boolean trans) {
        if (trans) {
            if (srcStep == 1 && dstStep == 1) {
                for (int i = 0; i < length; i++) {
                    int color = src[i + srcPos];
                    if (color != transColor)
                        dst[i + dstPos] = color;
                }
            } else {
                for (int i = 0; i < length; i++) {
                    int srcCurr = i * srcStep + srcPos;
                    int dstCurr = i * dstStep + dstPos;
                    int color = src[srcCurr];
                    if (color != transColor)
                        dst[dstCurr] = color;
                }
            }
        } else {
            if (srcStep == 1 && dstStep == 1) {
                System.arraycopy(src, srcPos, dst, dstPos, length);
            } else {
                for (int i = 0; i < length; i++) {
                    int srcCurr = i * srcStep + srcPos;
                    int dstCurr = i * dstStep + dstPos;
                    dst[dstCurr] = src[srcCurr];
                }
            }
        }
    }

    //Fill a line with a solid color.
    private static void fillColor(int[] dst, int dstPos, int dstStep, int color, int length) {
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

    //Get the color of the secondary array at a specified point.
    public int getClickableColor(Point p) {
        return clickable[mask.getIndex(p)];
    }

    //Renders this Image onto another image, with this image's top corner located at tPos on the destination image.
    public void renderTo(Graphic to, Point toPos, int color) {
        if (accelerated()) {
            //If we are accelerated then do the hardware acceleration render.
            to.buffer.getGraphics().drawImage(buffer, toPos.x, toPos.y, mask.getWidth(), mask.getHeight(), null);
            for (int i = 0; i < mask.getHeight(); i++)
                fillColor(to.clickable, to.getMask().getIndex(toPos.x, toPos.y + i), 1, color, mask.getWidth());
        } else {
            //Otherwise we need to do the traditional render.
            loadRaster();
            to.loadRaster();
            maskRender(to, toPos, this, new Point(), color, transparency);
        }
    }

    //Render to a graphics, filling the space.
    public void renderTo(Graphics graphics, int width, int height) {
        graphics.drawImage(buffer, 0, 0, width, height, null);
    }

    public void swap(int from, int to ) {
        loadRaster();
        int length = pixels.length;
        for (int i = 0; i < length; i++) {
            if (pixels[i] == from)
                pixels[i] = to;
        }
    }

    //Clear the image, trying to preserve acceleration.
    public void clear() {
        if (accelerated()) {
            Graphics graphics = buffer.getGraphics();
            graphics.setColor(Color.black);
            graphics.drawRect(0, 0, mask.getWidth(), mask.getHeight());
        } else {
            loadRaster();
            Arrays.fill(pixels, transColor);
        }
        Arrays.fill(clickable, 0);
    }

    public String toString() {
        return "Graphic(" + mask.getWidth() + "/" + mask.getHeight() + ")";
    }
}
