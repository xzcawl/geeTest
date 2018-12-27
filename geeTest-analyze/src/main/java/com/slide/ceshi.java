package com.slide;

import com.jhlabs.image.*;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.batik.ext.awt.image.renderable.Filter;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ceshi {

    public static void main(String[] args) throws Exception {
        BufferedImage fullBI = ImageIO.read(new File("D:/work/测试用例/图片识别/chuli.jpg"));
        BufferedImage bgBI = ImageIO.read(new File("D:/work/测试用例/图片识别/q.png"));

        //BufferedImage fullBI = ImageIO.read(new File("D://tencentquantu.jpg"));
        //BufferedImage bgBI = ImageIO.read(new File("D://tencentheise.jpg"));
        int indexX = 0;
        int indexY = 0;
        int to = 0;


        int[] fullRgb = new int[3];
        fullRgb[0] = (fullBI.getRGB(50, 21) & 0xff0000) >> 16;
        fullRgb[1] = (fullBI.getRGB(50, 21) & 0xff00) >> 8;
        fullRgb[2] = (fullBI.getRGB(50, 21) & 0xff);

        int[] bgRgb = new int[3];
        bgRgb[0] = (bgBI.getRGB(0, 0) & 0xff0000) >> 16;
        bgRgb[1] = (bgBI.getRGB(0, 0) & 0xff00) >> 8;
        bgRgb[2] = (bgBI.getRGB(0, 0) & 0xff);
        int count = 0;//计数
        int difference = tencentTest.difference(fullRgb, bgRgb);
        System.out.println("difference = " + difference);


        //InputStream  inputstream  = new  FileInputStream("D://wangyi.jpg");
        //File file = new File("D://jingdongquantu.jpg");
        /*BufferedImage image = ImageIO.read(new File("D://jingdongquantu.jpg"));
        int width = image.getWidth();
        int height = image.getHeight();
        //byte[] bytes = ImageUtils.transferAlpha(image, inputstream, 255);
        //byte2image(bytes,"D://wangyi2.jpg");
        //File file2 = new File("D://wangyi2.jpg");
        //BufferedImage image2 = ImageIO.read(new FileInputStream(file2));
        GrayscaleFilter filter = new GrayscaleFilter();
        BufferedImage toImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                // 过滤
                int grayValue = filter.filterRGB(i, j, rgb);
                toImage.setRGB(i, j, grayValue);
            }
        }
        BufferedImage grayValue = filter.filter(image,toImage);
        Image scaledImage = grayValue.getScaledInstance(width, height,Image.SCALE_DEFAULT);
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        output.createGraphics().drawImage(scaledImage, 0, 0, null); //画图
        ImageIO.write(output, "jpg", new File("D://jingdongquantu22.jpg"));*/

/*        ImageIO.write(toImage, "jpg", new File("D://tengxun2.jpg"));
        BufferedImage fullBI = ImageIO.read(new File("D://wangyiquekou.jpg"));
        BufferedImage bgBI = ImageIO.read(new File("D://aaa.jpg"));
        int indexX = 0;
        int indexY = 0;
        int to = 0;
        ok:for (int i = 0; i < fullBI.getWidth(); i++){
            for (int j = 0; j < fullBI.getHeight(); j++) {
                int[] fullRgb = new int[3];
                fullRgb[0] = (fullBI.getRGB(i, j)  & 0xff0000) >> 16;
                fullRgb[1] = (fullBI.getRGB(i, j)  & 0xff00) >> 8;
                fullRgb[2] = (fullBI.getRGB(i, j)  & 0xff);

                int[] bgRgb = new int[3];
                bgRgb[0] = (bgBI.getRGB(10,10)  & 0xff0000) >> 16;
                bgRgb[1] = (bgBI.getRGB(10,10)  & 0xff00) >> 8;
                bgRgb[2] = (bgBI.getRGB(10,10)  & 0xff);
                if(difference(fullRgb, bgRgb) < 100){
                    indexX = i;
                    indexY = j;
                    break ok;
                }
            }
        }
        System.out.println(indexX+","+indexY);
        fullBI = ImageIO.read(new File("D://wangyiquantu.jpg"));
        bgBI = ImageIO.read(new File("D://wangyiquekou.jpg"));
        ok2:for (int i = 55; i < fullBI.getWidth()-55; i++){
            int[] fullRgb = new int[3];
            fullRgb[0] = (fullBI.getRGB(i, indexY+39)  & 0xff0000) >> 16;
            fullRgb[1] = (fullBI.getRGB(i, indexY+39)  & 0xff00) >> 8;
            fullRgb[2] = (fullBI.getRGB(i, indexY+39)  & 0xff);

            int[] bgRgb = new int[3];
            bgRgb[0] = (bgBI.getRGB(indexX,indexY+39)  & 0xff0000) >> 16;
            bgRgb[1] = (bgBI.getRGB(indexX,indexY+39)  & 0xff00) >> 8;
            bgRgb[2] = (bgBI.getRGB(indexX,indexY+39)  & 0xff);
            int count = 0;//计数
            if(difference(fullRgb, bgRgb) < 100){
                System.out.println("i = " + i);
                for(int k = 0; k < 40; k++){
                    int[] currRgb = new int[3];
                    currRgb[0] = (fullBI.getRGB(i+k,indexY+40)  & 0xff0000) >> 16;
                    currRgb[1] = (fullBI.getRGB(i+k,indexY+40)  & 0xff00) >> 8;
                    currRgb[2] = (fullBI.getRGB(i+k,indexY+40)  & 0xff);
                    if(difference(fullRgb, currRgb) < 40){
                        count++;
                    }
                }
                System.out.println("count = " + count);
                if(count >= 15) {
                    to = count;
                    indexX = i;
                    break ok2;
                }
                //indexY = j;
            }

        }
        System.out.println("to = " + to);
        System.out.println(indexX+","+indexY);


    }

    private static int difference(int[] a, int[] b){
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2]);
    }

    //byte数组到图片
    public static void byte2image(byte[] data,String path){
        if(data.length<3||path.equals("")) return;
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }*/
    }
}
