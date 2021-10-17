package com.hanshunlie.fudan.fft;

import javafx.scene.chart.LineChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Grap extends JFrame {

    // 保存为图像文件
    public static void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);
            // 保存为PNG
            ChartUtilities.writeChartAsPNG(out, chart, weight, height);
            //保存为JPEG
            //ChartUtilities.writeChartAsJPEG(out, chart, weight, height);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    public static XYSeriesCollection createDataSet(double[] x, double[] y) {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("画图");

        for(int i=0; i < x.length; i++){
            series1.add(x[i], y[i]);
        }
        seriesCollection.addSeries(series1);
        return seriesCollection;
    }

    public static void createLineChart(double[] x,double[] y) {

        JFreeChart chart = ChartFactory.createXYLineChart("快速傅里叶变换后的结果",           //图表标题
                "HZ",                   //X轴标题
                "",                //Y轴标题
                createDataSet(x,y),     //绘图数据集
                PlotOrientation.VERTICAL,    //绘制方向
                true,                        //显示图例
                true,                        //采用标准生成器
                false);                      //是否生成超链接

        //ChartFrame frame=new ChartFrame("接口响应时间统计",chart);

        //设置标题字体
        chart.getTitle().setFont(new Font("宋体", Font.BOLD, 15));
        chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15)); // 设置图例类别字体
        //获取图表区域对象
        XYPlot plot = (XYPlot) chart.getPlot();

        ValueAxis domainAxis=plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("黑体", Font.BOLD, 15));
        /*------设置X轴坐标上的文字-----------*/
        domainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 11));
        /*------设置X轴的标题文字------------*/
        domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));

        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
        /*------设置Y轴坐标上的文字-----------*/
        numberaxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 12));
        /*------设置Y轴的标题文字------------*/
        numberaxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));


        //终端显示
        //frame.pack();
        //frame.setVisible(true);


        System.out.println("23456789098765445678");

        //保存图像的大小
        int weight = 800;
        int height = 600;
        //存放图像的目录
        String outputPath = "/Users/victor/Downloads/研究生/数字图像处理/作业/test2/123.png";
        //String outputPath = "d:\\interface\\imgs\\" + interfaceName + ".png";
        saveAsFile(chart, outputPath, weight, height);
    }


    public static void main(String[] args) {
        double[] x = {1,2,3.4};
        double[] y = {1,2,3.4};
        createLineChart(x,y);
    }
}
