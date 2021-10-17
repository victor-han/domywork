package com.hanshunlie.fudan.fft;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FFT {

    public static Complex[] dft(Complex[] x) {
        int n = x.length;

        // exp(-2i*n*PI)=cos(-2*n*PI)+i*sin(-2*n*PI)=1
        if (n == 1)
            return new Complex[]{x[0]};

        Complex[] result = new Complex[n];
        for (int i = 0; i < n; i++) {
            result[i] = new Complex(0, 0);
            for (int k = 0; k < n; k++) {
                //使用欧拉公式e^(-i*2pi*k/N) = cos(-2pi*k/N) + i*sin(-2pi*k/N)
                double p = -2 * i * k * Math.PI / n;
                Complex m = new Complex(Math.cos(p), Math.sin(p));
                result[i] = result[i].plus(x[k].multiple(m));
            }
        }
        return result;
    }

    public static Complex[] fft(Complex[] x) {
        int n = x.length;

        // 因为exp(-2i*n*PI)=1，n=1时递归原点
        if (n == 1) {
            return new Complex[]{x[0]};
        }

        // 如果信号数为奇数，使用dft计算
        if (n % 2 != 0) {
            return dft(x);
        }

        // 提取下标为偶数的原始信号值进行递归fft计算
        Complex[] even = new Complex[n / 2];
        for (int k = 0; k < n / 2; k++) {
            even[k] = x[2 * k];
        }
        Complex[] evenValue = fft(even);

        // 提取下标为奇数的原始信号值进行fft计算
        // 节约内存
        Complex[] odd = even;
        for (int k = 0; k < n / 2; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] oddValue = fft(odd);

        // 偶数+奇数
        Complex[] result = new Complex[n];
        for (int k = 0; k < n / 2; k++) {
            // 使用欧拉公式e^(-i*2pi*k/N) = cos(-2pi*k/N) + i*sin(-2pi*k/N)
            double p = -2 * k * Math.PI / n;
            Complex m = new Complex(Math.cos(p), Math.sin(p));
            result[k] = evenValue[k].plus(m.multiple(oddValue[k]));
            // exp(-2*(k+n/2)*PI/n) 相当于 -exp(-2*k*PI/n)，其中exp(-n*PI)=-1(欧拉公式);
            result[k + n / 2] = evenValue[k].minus(m.multiple(oddValue[k]));
        }
        return result;
    }


    //读取text的数据，读到double数组里面
    public static double[] readArray(String path) {
        String[] s1;
        double[] result = null;
        try {
            File file = new File(path);
            Long length = file.length();
            byte[] bytes = new byte[length.intValue()];
            FileInputStream fileInputStream = new FileInputStream(file);

            fileInputStream.read(bytes);
            fileInputStream.close();

            String s = new String(bytes);

            s = s.replaceAll("\n", "");

            s1 = s.split(" ");
            List<String> list = new ArrayList<>();
            for (String s2 : s1) {
                if (!StringUtils.isBlank(s2))
                    list.add(s2);
            }
            Object[] objects = list.toArray();

            result = new double[objects.length];
            for (int i = 0; i < objects.length; i++) {
                result[i] = Double.parseDouble((String) objects[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    //double数组转换为 复数数组
    public static Complex[] convert(double[] source) {
        Complex[] result = new Complex[source.length];
        for (int i = 0; i < source.length; i++) {
            Complex complex = new Complex(source[i], 0.0);
            result[i] = complex;
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            String path = "/Users/victor/Downloads/研究生/数字图像处理/作业/test2/demo.txt";
            //未过滤空格的数组
            double[] doubles = readArray(path);

            //过滤空格的数组
            double[] d1 = new double[150];
            for (int i = 0; i < doubles.length; i++) {
                d1[i] = doubles[i];
            }


            //转化为复数
            Complex[] convert = convert(doubles);

            //fft
            Complex[] fft = fft(convert);

            //fft的结果abs,y轴的数据
            double[] result = new double[fft.length];
            for (int i = 0; i < fft.length; i++) {
                double s = fft[i].abs();
                System.err.println(s);
//                result[i] = s;
            }
//            double[] x = new double[fft.length];
//            for (int i = 0; i < x.length; i++) {
//                x[i] = i;
//            }
//
//            Grap.createLineChart(x, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
