package com.hanshunlie.fudan.bmp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author shunlie
 * @Date 2021/9/23 6:48 下午
 * 编写程序实现BMP图像的读取和参数获得。对于给定图像，
 * 编写程序
 * 1、读取BMP图像的像素点灰度值，
 * 2、返回该图像的文件大小，
 * 3、像素点的个数，
 * 4、以及长度和高度。
 *
 * 位图结构
 * 1---
 * bit map file header 位图文件头
 * bfType（2byte） 文件类型，具体值为0x4D42 = “BM”
 * bfSize（4byte） 文件大小，此图片大小为0x00000206 = 518（十进制）byte。
 * bfReserved1和bfReserved2（每个2byte） 保留项，必须设置为0。
 * bfOffBits（4byte） 从文件开头到具体图像数据的字节偏移量，0x00000076 = 118（十进制），具体文件头（14byte）+位图信息头（40byte）+调色板（64byte） = 118byte。
 *
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
 *
 * 3---
 * 调色板
 */
public class Bmp {
    public static int[][] readBMPPic(String src) throws Exception{
        FileInputStream fis = new FileInputStream(src);
        BufferedInputStream bis = new BufferedInputStream(fis);


        byte[] bType = new byte[2];
        bis.read(bType);
        int type = byte2Int(bType);

        byte[] bSize = new byte[4];
        bis.read(bSize);
        int size = byte2Int(bSize);

        //丢掉文件头信息
        bis.skip(18);
        //获取长度和宽度
        //18-21是宽度
        byte[] b1 = new byte[4];
        bis.read(b1);
        int width = byte2Int(b1);

        //22-25是高度
        byte[] b2 = new byte[4];
        bis.read(b2);
        int height = byte2Int(b2);

        System.out.println("Hight:"+width+" Width:"+height);

        //bmp位图的读取顺序为横向扫描，所以应该把数组定义为int[height][width]
        int[][] data = new int[height][width];
        int skipnum =0;

        //bmp图像区域的大小必须为4的倍数，而他以三个字节存储一个像素，读的时候应该应该跳过补上的o
        if (width*3%4!=0){
            skipnum = 4-width*3%4;
        }
        System.err.println(skipnum);

        bis.skip(28);
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                int red = bis.read();
                int green = bis.read();
                int blue = bis.read();
            }
            if (skipnum !=0){
                bis.skip(skipnum);
            }
        }
        bis.close();
        return data;

    }

    private static int byte2Int(byte[] b) throws IOException {
        int num=(b[3]&0xff<<24)|(b[2]&0xff)<<16|(b[1]&0xff)<<8|b[0]&0xff;
        return num;
    }
    public static void main(String[] args) throws Exception {
        String path = "/Users/shunlie/Downloads/fudan/test1/demo.bmp";
        readBMPPic(path);
    }
}
