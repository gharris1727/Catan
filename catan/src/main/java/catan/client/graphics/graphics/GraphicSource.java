package catan.client.graphics.graphics;

import catan.ExternalResource;
import catan.client.graphics.masks.RectangularMask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Greg on 8/14/2014.
 * Class for importing images to be used for sprites.
 */
public class GraphicSource extends Graphic {

    public GraphicSource(String path) throws IOException {
        //Load the external image.
        BufferedImage image = ImageIO.read(ExternalResource.getStaticResource(path));
        //Calculate the dimensions we need to save.
        int width = image.getWidth();
        int height = image.getHeight();
        //Create a buffer to import the RGBA pixels.
        int pixelsRGBA[] = new int[width * height];
        //Pull the image data into the rgba buffer.
        image.getRGB(0, 0, width, height, pixelsRGBA, 0, width);
        //Initialize this object's storage
        init(new RectangularMask(new Dimension(width,height)), true);
        loadRaster();
        //Copy the pixels, changing pixel format RGBA -> RGB
        for (int i = 0; i < (width * height); i++)
            pixels[i] = pixelsRGBA[i] & 0xffffff;
    }
}
