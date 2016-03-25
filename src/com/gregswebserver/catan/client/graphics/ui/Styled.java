package com.gregswebserver.catan.client.graphics.ui;

/**
 * Created by greg on 1/15/16.
 * Abstraction for components that have a UIStyle associated with them.
 */
public interface Styled {

    void setStyle(UIStyle style);

    UIStyle getStyle();
}
