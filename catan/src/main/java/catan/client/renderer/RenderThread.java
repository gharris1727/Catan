package catan.client.renderer;

import catan.client.graphics.graphics.ScreenCanvas;
import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.ui.UIConfig;
import catan.client.ui.ClientScreen;
import catan.client.ui.PopupWindow;
import catan.client.ui.taskbar.TaskbarMenu;
import catan.common.event.QueuedInputThread;
import catan.common.profiler.TimeSlice;

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

    public RenderThread(BaseRegion base) {
        this.base = base;
        root = new TimeSlice("root");
        events = new TimeSlice("Events");
    }

    @Override
    protected void execute() throws ThreadStopException {
        //Block when waiting for a new canvas.
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
            }
            events.mark();
            root.addChild(events);
        } else if ((canvas != null) && base.isRenderable()) {
            base.setRenderer(this);
            canvas.render(base.getGraphic());
            root.addChild(base.getRenderTime());
            root.addChild(canvas.getRenderTime());
            root.waitUntil(33*TimeSlice.MILLION);
        } else {
            root.waitUntil(100*TimeSlice.MILLION);
        }
        root.mark();
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
