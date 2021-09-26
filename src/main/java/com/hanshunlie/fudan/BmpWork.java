package com.hanshunlie.fudan;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @Author shunlie
 * @Date 2021/9/23 6:48 下午
 * 编写程序实现BMP图像的读取和参数获得。对于给定图像，
 * 编写程序
 * 1、读取BMP图像的像素点灰度值，像素点灰度值：(输出第0行-200行，100-200列的灰度值)
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
public class BmpWork {

    //    public static String path = "/Users/victor/Downloads/fudan/test1/demo.bmp";
    public static String path = "/Users/shunlie/Downloads/fudan/test1/demo.bmp";

    // 文件头
    Filehead filehead = new Filehead();
    byte[] fileheadByte = new byte[14];

    // 信息头
    byte[] mapinfoByte = new byte[40];
    MapInfohead mapInfohead = new MapInfohead();

    // 彩色板
    byte[] colorByte;

    public static void main(String[] args) throws Exception {
        BmpWork bw = new BmpWork();
        bw.readBMPPic(path);
    }


    public void readBMPPic(String src) throws Exception {


        FileInputStream fis = new FileInputStream(src);
        BufferedInputStream bis = new BufferedInputStream(fis);
        //前14位是文件头
        bis.read(fileheadByte, 0, 14);
        //封装成 fileHead对象
        unpackFileHead(fileheadByte);


        //读取40位
        bis.read(mapinfoByte, 0, 40);
        //封装成 mapInfo对象
        unpackMapInfo(mapinfoByte);


        double imageSize = (double) filehead.bfSize / 1024 / 1024;
        Double imageSize2 = Double.valueOf(String.format("%.3f", imageSize));
        System.err.println("图像的大小：" + imageSize2 + " MB");
        System.err.println("像素点个数：" + mapInfohead.biHeight * mapInfohead.biWidth);
        System.err.println("长：" + mapInfohead.biHeight + ", 宽：" + mapInfohead.biWidth);
        System.err.println("色深：" + mapInfohead.biBitCount + "位，所以是" + (int) Math.pow(2, mapInfohead.biBitCount) + "色");


        int colorSize = (int) Math.pow(2, mapInfohead.biBitCount);
        colorByte = new byte[colorSize * 4];
        //彩色表，读取1024位，256色图像为1024字节
        bis.read(colorByte, 0, colorSize * 4);


        System.err.println("-------------------灰度值------------------------");
        //读取灰度值直接用imageIO类, 像素点灰度值：(输出第0行-200行，100-200列的灰度值)
        BufferedImage read = ImageIO.read(new File(path));
        StringBuffer sb =new StringBuffer();
        for (int i = 0; i < 100; i++) {
            sb.append("第" + i + "行：");
            for (int j = 100; j < 200; j++) {
                int rgb = read.getRGB(i, j);
                //三色相等，直接取后八位
                int gray = getGray(rgb);
                sb.append(gray).append(",");
            }
            sb.append("\n");
        }
        System.err.println(sb.toString());

    }

    public int getGray(int rgb) {
        int a = rgb & 0xff000000;//将最高位（24-31）的信息（alpha通道）存储到a变量
        int r = (rgb & 0xff0000 ) >> 16;//取出次高位（16-23）红色分量的信息
        int g = (rgb & 0xff00 ) >> 8 ;//取出中位（8-15）绿色分量的信息
        int b = (rgb & 0xff );//取出低位（0-7）蓝色分量的信息
//        return a | (rgb << 16) | (rgb << 8) | rgb;//将灰度值送入各个颜色分量
        return (r+g+b)/3;//将灰度值送入各个颜色分量
    }

    private Filehead unpackFileHead(byte[] fileheadByte) {
        //2位
        filehead.bfType = load2BytetoInt(fileheadByte, 1);
        //4位 bmp图像文件的大小
        filehead.bfSize = load4BytetoInt(fileheadByte, 5);
        //2位
        filehead.bfReserverd1 = load2BytetoInt(fileheadByte, 7);
        //2位
        filehead.bfReserverd2 = load2BytetoInt(fileheadByte, 9);
        //4位
        filehead.bfOffBits = load4BytetoInt(fileheadByte, 13);

        return filehead;
    }

    private MapInfohead unpackMapInfo(byte[] mapinfoByte) {
        //4位 本结构的大小
        mapInfohead.biSize = load4BytetoInt(mapinfoByte, 3);
        //4位 宽
        mapInfohead.biWidth = load4BytetoInt(mapinfoByte, 7);
        //4位 高
        mapInfohead.biHeight = load4BytetoInt(mapinfoByte, 11);
        //2位
        mapInfohead.biPlanes = load2BytetoInt(mapinfoByte, 13);
        //2位 bmp图像的色深，一个像素用多少位表示,常见有1，4，8，16，24，32，分别对应单色，16色，256色，16位高彩色，24位真彩色，32位增强型彩色
        mapInfohead.biBitCount = load2BytetoInt(mapinfoByte, 15);
        //4位 压缩方式，0表示不压缩
        mapInfohead.biCompression = load4BytetoInt(mapinfoByte, 19);
        //4位 BMP图像数据大小
        mapInfohead.biSizeImage = load4BytetoInt(mapinfoByte, 23);
        //4位 水平分辨率，单位像素/m
        mapInfohead.biXPelsPerMeter = load4BytetoInt(mapinfoByte, 27);
        //4位 垂直分辨率，单位像素/m
        mapInfohead.biYPelsPerMeter = load4BytetoInt(mapinfoByte, 31);
        //4位 bmp图像使用的颜色，0表示使用全部颜色
        mapInfohead.biClrUsed = load4BytetoInt(mapinfoByte, 35);
        //4位 重要的颜色数，0表示所有颜色偶读重要
        mapInfohead.biClrImportant = load4BytetoInt(mapinfoByte, 39);

        return mapInfohead;
    }


    /**
     * 4 字节转int
     *
     * @param head  字节数组
     * @param index 开始的索引
     * @return
     */
    public int load4BytetoInt(byte[] head, int index) {
        // 注意index 是索引位置从0 开始
        return (((head[index] & 0xff) << 24)
                | ((head[index - 1] & 0xff) << 16)
                | ((head[index - 2] & 0xff) << 8)
                | (head[index - 3] & 0xff));
    }

    /**
     * 2 字节转int
     *
     * @param head  字节数组
     * @param index 开始的索引
     * @return
     */
    public int load2BytetoInt(byte[] head, int index) {
        return (((head[index] & 0xff) << 8)
                | (head[index - 1] & 0xff));
    }


    class Filehead {
        /*
            total 14 byte
         */
        public int bfType;
        // size  4 byte
        public int bfSize;
        public int bfReserverd1;
        public int bfReserverd2;
        // size 4 byte
        public int bfOffBits;

    }

    class MapInfohead {
        /*
            total 40 byte
         */
        int biSize;
        int biWidth;
        int biHeight;
        // size 2 byte
        int biPlanes;
        // size 2 byte
        int biBitCount;
        int biCompression;
        int biSizeImage;
        int biXPelsPerMeter;
        int biYPelsPerMeter;
        int biClrUsed;
        int biClrImportant;
    }



}



