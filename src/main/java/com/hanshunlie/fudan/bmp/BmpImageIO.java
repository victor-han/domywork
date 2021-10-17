package com.hanshunlie.fudan.bmp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @Author shunlie
 * @Date 2021/9/23 6:48 下午
 * 编写程序实现BMP图像的读取和参数获得。对于给定图像，
 * 编写程序
 * 1、读取BMP图像的像素点灰度值，
 * 2、返回该图像的文件大小，
 * 3、像素点的个数，
 * 4、以及长度和高度。
 * <p>
 * 位图结构
 * 1---
 * bit map file header 位图文件头
 * bfType（2byte） 文件类型，具体值为0x4D42 = “BM”
 * bfSize（4byte） 文件大小，此图片大小为0x00000206 = 518（十进制）byte。
 * bfReserved1和bfReserved2（每个2byte） 保留项，必须设置为0。
 * bfOffBits（4byte） 从文件开头到具体图像数据的字节偏移量，0x00000076 = 118（十进制），具体文件头（14byte）+位图信息头（40byte）+调色板（64byte） = 118byte。
 * <p>
 * 2---
 * bit map info header 位图信息头
 * DWORD biSize 4byte
 * LONG biWidth 4byte
 * LONG biHeight 4byte
 * WORD biPlanes 2byte
 * WORD biBitCount 2byte
 * DWORD biCompression 4byte
 * DWORD biSizeImage 4byte
 * LONG biXPelsPerMeter 4byte
 * LONG biYPelsPerMeter 4byte
 * DWORD biClrUsed 4byte
 * DWORD biClrImportant 4byte
 */
public class BmpImageIO {

    //    public static String path = "/Users/victor/Downloads/fudan/test1/demo.bmp";
    public static String path = "/Users/shunlie/Downloads/fudan/test1/demo.bmp";

    public static void main(String[] args) throws Exception {
        BmpImageIO bmpImageIO = new BmpImageIO();
        bmpImageIO.readBMP();
    }

    public void readBMP() throws Exception {
        File file = new File(path);
        BufferedImage read = ImageIO.read(file);
        int width = read.getWidth();
        int height = read.getHeight();
        int minX = read.getMinX();
        int minY = read.getMinY();
        System.err.println("width  : " + width + ", height : " + height);

//        read.getRGB()

        int rgb = read.getRGB(100, 100);
//        int gray = filterRGB(0, 0, rgb);
        int gray = rgb & 0xFF;

        System.err.println(gray);

    }

    public int getGray(int rgb) {
        int a = rgb & 0xff000000;//将最高位（24-31）的信息（alpha通道）存储到a变量
        int r = (rgb & 0xff0000 ) >> 16;//取出次高位（16-23）红色分量的信息
        int g = (rgb & 0xff00 ) >> 8 ;//取出中位（8-15）绿色分量的信息
        int b = (rgb & 0xff );//取出低位（0-7）蓝色分量的信息
//        return a | (rgb << 16) | (rgb << 8) | rgb;//将灰度值送入各个颜色分量
        return (r+g+b)/3;//将灰度值送入各个颜色分量
    }
}





