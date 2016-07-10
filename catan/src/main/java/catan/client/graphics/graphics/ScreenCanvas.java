package catan.client.graphics.graphics;

import catan.common.profiler.TimeSlice;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by Greg on 1/18/2015.
 * A canvas that has an underlying graphic so that we can render to it.
 */
public final class ScreenCanvas extends Canvas {

    private final Dimension size;
    private final TimeSlice canvasRender;
    private final TimeSlice bufferCreate;
    private final TimeSlice screenRender;

    private BufferStrategy frameBuffer;

    public ScreenCanvas(Dimension size) {
        this.size = size;
        canvasRender = new TimeSlice(toString());
        bufferCreate = new TimeSlice("Buffer");
        screenRender = new TimeSlice("Screen");
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    public String toString() {
        return "ScreenCanvas";
    }

    public synchronized void render(Graphic graphic) {
        canvasRender.reset();
        if (isDisplayable()) {
            bufferCreate.reset();
            //Create a framebuffer if we haven't already.
            if (frameBuffer == null) {
                createBufferStrategy(2);
                frameBuffer = getBufferStrategy();
            }
            bufferCreate.mark();
            screenRender.reset();
            graphic.renderTo(frameBuffer.getDrawGraphics(), getWidth(), getHeight());
            frameBuffer.show();
            screenRender.mark();
        }
        canvasRender.mark();
        canvasRender.addChild(bufferCreate);
        canvasRender.addChild(screenRender);
    }

    public TimeSlice getRenderTime() {
        return canvasRender;
    }
}
