package com.gregswebserver.catan.client.hitbox;

/**
 * Created by Greg on 8/17/2014.
 * Not to be confused with a ColorHitbox, which uses this class.
 */
public enum HitboxColor {

    HexA(0xff0000),
    HexB(0xff0101),
    HexC(0xff0202),
    HexD(0xff0303),
    EdgeA(0x00ff00),
    EdgeB(0x01ff01),
    EdgeC(0x02ff02),
    EdgeD(0x03ff03),
    EdgeE(0x04ff04),
    EdgeF(0x05ff05),
    VertA(0x0000ff),
    VertB(0x0101ff),
    VertC(0x0202ff),
    VertD(0x0303ff),
    Foreground(0x101010),
    Default(0);

    public final int color;

    HitboxColor(int color) {
        this.color = color;
    }

    public static HitboxColor get(int color) {
        for (HitboxColor hc : values()) {
            if (hc.color == color)
                return hc;
        }
        return HitboxColor.Default;
    }
}
