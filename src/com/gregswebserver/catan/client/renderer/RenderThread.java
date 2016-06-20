package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.client.ui.PopupWindow;
import com.gregswebserver.catan.client.ui.taskbar.TaskbarMenu;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.profiler.TimeSlice;

/**
 * Created by Greg on 8/13/2014.
 * Render engine with a event queue on the front for data input.
 * Stores the clickable information for the InputListener to use.
 */
public class RenderThread extends QueuedInputThread<RenderEvent> {

    private final BaseRegion base;
    private final TimeSlice root;
    private ScreenCanvas canvas;
    private final TimeSlice events;

    public RenderThread(Logger logger, BaseRegion base) {
        super(logger);
        this.base = base;
        root = new TimeSlice("root");
        events = new TimeSlice("Events");
    }

    @Override
    protected void execute() throws ThreadStop {
        //Process the event queue, blocking for every event. Only re-renders what needs to be re-rendered.
        RenderEvent event = getEvent(false);
        root.reset();
        if (event != null) {
            events.reset();
            switch (event.getType()) {
                case Canvas_Update:
                    canvas = (ScreenCanvas) event.getPayload();
                    base.setMask(new RectangularMask(canvas.getSize()));
                    break;
                case Set_Configuration:
                    base.setConfig((UIConfig) event.getPayload());
                    break;
                case Screen_Update:
                    base.displayScreen((ClientScreen) event.getPayload());
                    break;
                case Screen_Refresh:
                    base.update();
                    break;
                case Taskbar_Add:
                    base.addMenu((TaskbarMenu) event.getPayload());
                    break;
                case Taskbar_Remove:
                    base.removeMenu((TaskbarMenu) event.getPayload());
                    break;
                case Popup_Show:
                    base.displayPopup((PopupWindow) event.getPayload());
                    break;
                case Popup_Remove:
                    base.removePopup((PopupWindow) event.getPayload());
                    break;
                case Animation_Step:
                    base.step();
                    break;
            }
            events.mark();
            root.addChild(events);
        } else { //No event to be processed this round.
            if (canvas != null && base.isRenderable()) {
                base.setRenderer(this);
                canvas.render(base.getGraphic());
                root.addChild(base.getRenderTime());
                root.addChild(canvas.getRenderTime());
            } else {
                //This may be super spammy.
                logger.log("Unable to render " + base + " to " + canvas, LogLevel.DEBUG);
            }
            root.waitUntil(33*TimeSlice.MILLION);
        }
        root.mark();
        /*
        if (root.getTime() > 100*TimeSlice.MILLION)
            logger.log("Slow Render!\n" + root.print(1, 0), LogLevel.WARN);
        */
    }

    @Override
    protected void onException(Throwable t) {
        super.onException(t);
        stop();
    }

    public String toString() {
        return "RenderThread";
    }

}
