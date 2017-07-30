package catan.client.graphics.graphics;

import catan.client.graphics.masks.RenderMask;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by Greg on 8/15/2014.
 * Utility class to provide pixel-moving functions to the other image handlers.
 */
public class Graphic {

    private static final int TRANSPARENCY = 0xff00ff; //Transparency color (pink)
    protected int[] pixels; //Pixel array for rasterization.
    protected RenderMask mask; //Details about the image size and shape.
    protected BufferedImage buffer; //Hardware accelerated form of the graphic.
    private boolean transparency; //Flag on whether to use transparency.

    //Constructor to allow subclasses to do their own instantiation.
    protected Graphic() {
    }

    //Resource constructor, for cutting graphics from larger graphics.
    public Graphic(Graphic source, RenderMask mask, Point start, boolean transparency)  {
        this(mask, transparency);
        if (source.getMask().hasContent() && mask.hasContent()) {
            BufferedImage part = source.buffer.getSubimage(start.x, start.y, mask.getWidth(), mask.getHeight());
            buffer.getGraphics().drawImage(part, 0, 0, mask.getWidth(), mask.getHeight(), null);
        }
    }

    //Primary constructor for a blank Graphic object.
    public Graphic(RenderMask mask, boolean transparency) {
        init(mask, transparency);
        clear();
    }

    //Initialize the graphic with the specified mask and transparency.
    protected final void init(RenderMask mask, boolean transparency) {
        this.mask = mask;
        buffer = mask.hasContent() ? new BufferedImage(mask.getWidth(), mask.getHeight(), TYPE_INT_RGB) : null;
        this.transparency = transparency;
    }

    //Rasterize the image to the pixel buffer for manual rendering. De-accelerates the graphic.
    protected void loadRaster() {
        if (pixels == null) {
            pixels = buffer != null ? ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData() : new int[0];
        }
    }

    //Find out whether the graphic is hardware accelerated or not.
    private boolean isAccelerated() {
        return (pixels == null) && !transparency && mask.isAccelerable();
    }

    //Render one graphic to another using their render masks.
    private void maskRender(Graphic to, Point toStart, Graphic from, Point fromStart, int color) {
        RenderMask toMask = to.mask;
        RenderMask fromMask = from.mask;
        //Check for upper bounds, if any of these are out of spec, nothing will get copied anyway.
        if (toStart.x >= toMask.getWidth()) return;
        if (toStart.y >= toMask.getHeight()) return;
        if (fromStart.x >= fromMask.getWidth()) return;
        if (fromStart.y >= fromMask.getHeight()) return;
        //These are the differences between the from coordinates and the toCoordinates.
        int diffX = toStart.x - fromStart.x;
        int diffY = toStart.y - fromStart.y;

        //Start at 0, and limit inward.
        int startX = 0;
        if (startX < fromStart.x) startX = fromStart.x;
        if ((startX + diffX) < 0) startX = -diffX;

        //Start at 0, and limit inward.
        int startY = 0;
        if (startY < fromStart.y) startY = fromStart.y;
        if ((startY + diffY) < 0) startY = -diffY;

        //Start at one height, and limit inward.
        int endY = fromMask.getHeight();
        if ((endY + diffY) > toMask.getHeight()) endY = toMask.getHeight() - diffY;

        for (int currY = startY; currY < endY; currY++) {
            int fromPad = fromMask.getLinePadding(currY);
            int fromLen = fromMask.getLineWidth(currY);
            int toPad = toMask.getLinePadding(currY + diffY);
            int toLen = toMask.getLineWidth(currY + diffY);
            //Start at one extreme and work inward.
            int currX = startX;
            if (currX < fromPad) currX = fromPad;
            if ((currX + diffX) < toPad) currX = toPad - diffX;
            //Start at
            int endX = fromPad + fromLen;
            if ((endX + diffX) > (toPad + toLen)) endX = (toPad + toLen) - diffX;
            //Find the length
            int length = endX - currX;
            if (length < 1) continue;
            //Copy
            pixelCopy(from.pixels, from.mask.getIndex(currX, currY), 1, to.pixels, to.mask.getIndex(currX + diffX, currY + diffY), 1, length, color, from.transparency || to.transparency);
        }
    }

    //Copy pixels and clickable pixels, using faster method if possible.
    protected void pixelCopy(int[] src, int srcPos, int srcStep, int[] dst, int dstPos, int dstStep, int length, int color, boolean trans) {
        if (trans) {
            if ((srcStep == 1) && (dstStep == 1)) {
                for (int i = 0; i < length; i++) {
                    int pixel = src[i + srcPos];
                    if (pixel != TRANSPARENCY)
                        dst[i + dstPos] = pixel;
                }
            } else {
                for (int i = 0; i < length; i++) {
                    int srcCurr = (i * srcStep) + srcPos;
                    int dstCurr = (i * dstStep) + dstPos;
                    int pixel = src[srcCurr];
                    if (pixel != TRANSPARENCY)
                        dst[dstCurr] = pixel;
                }
            }
        } else {
            if ((srcStep == 1) && (dstStep == 1)) {
                System.arraycopy(src, srcPos, dst, dstPos, length);
            } else {
                for (int i = 0; i < length; i++) {
                    int srcCurr = (i * srcStep) + srcPos;
                    int dstCurr = (i * dstStep) + dstPos;
                    dst[dstCurr] = src[srcCurr];
                }
            }
        }
    }

    public RenderMask getMask() {
        return mask;
    }

    //Render from another graphic onto this graphic.
    public void renderFrom(Graphic from, Point fromPos, int color) {
        if (from.mask.hasContent() && mask.hasContent()) {
            if (isAccelerated() && from.isAccelerated()) {
                //If we are isAccelerated then do the hardware acceleration render.
                buffer.getGraphics().drawImage(from.buffer, fromPos.x, fromPos.y, from.mask.getWidth(), from.mask.getHeight(), null);
            } else {
                //Otherwise we need to do the traditional render.
                loadRaster();
                from.loadRaster();
                maskRender(this, fromPos, from, new Point(), color);
            }
        }
    }

    //Render to a graphics, filling the space.
    public void renderTo(Graphics graphics, int width, int height) {
        if (mask.hasContent() && (width > 0) && (height > 0))
            graphics.drawImage(buffer, 0, 0, width, height, null);
    }

    public void applySwaps(int[] swaps) {
        if (swaps != null && (swaps.length % 2) == 0) {
            for (int i = 0; i < swaps.length; i += 2) {
                swap(swaps[i], swaps[i + 1]);
            }
        }
    }

    private void swap(int from, int to) {
        loadRaster();
        int length = pixels.length;
        for (int i = 0; i < length; i++) {
            if (pixels[i] == from)
                pixels[i] = to;
        }
    }

    //Clear the image, trying to preserve acceleration.
    public final void clear() {
        if (mask.hasContent()) {
            if (isAccelerated()) {
                Graphics graphics = buffer.getGraphics();
                graphics.setColor(Color.black);
                graphics.drawRect(0, 0, mask.getWidth(), mask.getHeight());
            } else {
                loadRaster();
                Arrays.fill(pixels, TRANSPARENCY);
            }
        }
    }

    public String toString() {
        return "Graphic(" + mask.getWidth() + "/" + mask.getHeight() + ")";
    }
}
