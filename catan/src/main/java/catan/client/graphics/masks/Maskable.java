package catan.client.graphics.masks;

/**
 * Created by greg on 1/15/16.
 * Abstraction of objects that have an inherent RenderMask.
 */
public interface Maskable {

    void setMask(RenderMask mask);

    RenderMask getMask();
}
