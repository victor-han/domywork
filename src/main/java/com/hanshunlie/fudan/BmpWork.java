package com.hanshunlie.fudan;

import scala.collection.mutable.StringBuilder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

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
//    MapInfohead mapInfohead = new MapInfohead();

    //图片数据
    Rgb rgb;

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
        //读取1024位，256色图像为1024字节
        bis.read(colorByte, 0, colorSize * 4);

        uppackRgb(bis);
        System.err.println(rgb.print(0,200,100,200));
//        System.err.println(rgb.toString());


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

    public void uppackRgb(BufferedInputStream bis) throws Exception {
        int width = mapInfohead.biWidth;
        int height = mapInfohead.biHeight;
        rgb = new Rgb(width, height);
        //字节填充
        int skip_width = 0;
        //bmp图像区域的大小必须为4的倍数，而256色以1个字节存储一个像素，读的时候应该应该跳过补上的o
        int m = width * 1 % 4;
        if (m != 0) {
            skip_width = 4 - m;
        }
        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {

                rgb.blue[i][j] = bis.read();
                rgb.green[i][j] = bis.read();
                rgb.red[i][j] = bis.read();
                //灰度值计算公式 ： BYTE Gray = (BYTE)(0.3f*R+0.59f*G+0.11f*B);
                rgb.gray[i][j] = (int) (0.11f * rgb.blue[i][j] + 0.59f * rgb.green[i][j] + 0.3f * rgb.red[i][j]);
                if (j == 0) {
                    bis.skip(skip_width);
                }
            }
        }
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

    class Rgb {
        int[][] red;
        int[][] green;
        int[][] blue;
        int[][] gray;
        int width;
        int heigh;

        Rgb(int width, int heigh) {
            this.width = width;
            this.heigh = heigh;
            red = new int[heigh][width];
            green = new int[heigh][width];
            blue = new int[heigh][width];
            gray = new int[heigh][width];
        }

        @Override
        public String toString() {
            // 输出rgb矩阵
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < heigh; i++) {
                for (int j = 0; j < width; j++) {
//                    stringBuilder.append(String.format("(%03d,%03d,%03d)", red[i][j], green[i][j], blue[i][j]));
                    stringBuilder.append(String.format("(%03d)", green[i][j]));
                }
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }

        public String print(int widthBegin, int widthEnd, int heightBegin, int heightEnd) {
            StringBuffer sb = new StringBuffer();
            for (int i = widthBegin; i < widthEnd; i++) {
                for (int j = heightBegin; j < heightEnd; j++) {
                    sb.append(String.format("(%03d)", green[i][j]));
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

}



