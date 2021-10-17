package com.hanshunlie.fudan.bmp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BmpDemo {

    String address = "";


    public void getImage() throws Exception{
        BufferedImage img = ImageIO.read(new File(address));
        int imageType = img.getType();
        int width = img.getWidth();
        int height = img.getHeight();
        int startX =0;
        int startY =0;
        int offset =0;
        int scansize = width;
        //RGB 数组
        int[] rgbArray = new int[offset + (height - startY) * scansize + (width-startX)];
        int[] rgb = img.getRGB(startX, startY, width, height, rgbArray, offset, scansize);
        

    }

}
