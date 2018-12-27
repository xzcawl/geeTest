package com.slide;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

//京东
public class jingdongTest2 {

    private WebDriver driver;
    private WebDriver.Navigation navigation;
    private String baseUrl = "https://reg.jd.com/reg/person";
    //private static boolean moveArrayInit = false;
    //private static int[][] moveArray = new int[52][2];

    @Test
    public void testJingdongTest() {
        try {
            //设置firefox浏览器的位置
            System.setProperty("webdriver.firefox.bin", "C:/Program Files (x86)/Mozilla Firefox/firefox.exe");

            //创建WebDriver对象
            driver = new FirefoxDriver();
            navigation = driver.navigate();
            int countNothing = 0;
            int countSucc = 0;
            int countErr = 0;
            int countFobidden = 0;
            for (int i = 0; i < 300; i++) {
                int a = jingdongTest1();
                System.out.println("a = " + a);
                if (a == 0) {
                    countNothing++;
                } else if (a == 1) {
                    countSucc++;
                } else if (a == 2) {
                    countErr++;
                } else if (a == 2) {
                    countFobidden++;
                }
            }
            System.out.println("countNothing = " + countNothing);
            System.out.println("countSucc = " + countSucc);
            System.out.println("countErr = " + countErr);
            System.out.println("countFobidden = " + countFobidden);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int jingdongTest1() throws Exception {
        //加载到指定url
        navigation.to(baseUrl);
        Actions actions = new Actions(driver);
        Thread.sleep(500);

        //File fullFile = new File("D:/jingdongwanzheng.jpg");
        //cutPic(screenShots().toString(),fullFile.toString(),469,427,350,136);

        String xiaotu = driver.findElement(By.xpath("//div[@class='JDJRV-smallimg']/img")).getAttribute("src");
        String quantu = driver.findElement(By.xpath("//div[@class='JDJRV-bigimg']/img")).getAttribute("src");
        //System.out.println("xiaotu = " + xiaotu);
        //System.out.println("quantu = " + quantu);
        generateImage(xiaotu.substring("data:image/png;base64,".length()),"D://jingdongquekou.jpg");
        generateImage(quantu.substring("data:image/png;base64,".length()),"D://jingdongquantu.jpg");
        //grayscaleFilter("D://jingdongquantu.jpg");

        Thread.sleep(500);
        WebElement slider_button = driver.findElement(By.className("JDJRV-slide-btn"));
        actions.clickAndHold(slider_button).perform();

        int moveDistance = getMoveDistance(driver);
        //moveDistance = moveDistance - 3;

        System.out.println("应平移距离：" + moveDistance);
        moveDistance = moveDistance - 4;
        Thread.sleep(200);
        //int moveX = new Random().nextInt(8) - 5;
        //int moveY = 1;

        move(actions,slider_button,moveDistance);

        //printLocation(element);
       /* for (int i = 0; i < moveDistance; ) {
            int index = 10;
            //actions.moveToElement(slider_button, 10, 0).perform();
            if (i < moveDistance - 10) {
                actions.moveToElement(slider_button, index, 0).perform();
            } else {
                int last = moveDistance - i;
                actions.moveToElement(slider_button, last, 0).perform();
            }
            i = i + 10;
            Thread.sleep(1);
        }*/

        //actions.moveToElement(slider_button, moveDistance, 0).perform();
        Thread.sleep(200);
        actions.release().perform();
        Thread.sleep(1000);
/*
        //获取输入框的id,并在输入框中输入用户名
        WebElement loginInput = driver.findElement(By.id("user-input"));
        loginInput.sendKeys("wadeyang");
        //获取输入框的id，并在输入框中输入密码
        WebElement pwdInput = driver.findElement(By.id("qq-input"));
        pwdInput.sendKeys("914645774");
*/

        WebElement flag = driver.findElement(By.xpath("//div[starts-with(@class,'JDJRV-slide-')]"));
        String is = flag.getAttribute("class");
        //System.out.println("is = " + is);
        int a = 0;
        if (is.equals("JDJRV-slide JDJRV-slide-succ")) {
            a = 1;
        } else if (is.equals("JDJRV-slide JDJRV-slide-err")) {
            a = 2;
        } else if (is.equals("JDJRV-slide JDJRV-slide-forbidden")) {
            a = 3;
        }
        //另一中的拖拽防止dragAndDrop不支持有些浏览器
        //action.clickAndHold(elementsource).moveToElement(elementtarget).build().perform();
        return a;

    }

    public static void move(Actions actions, WebElement element, int distance) throws InterruptedException {
        for (int i = 0; i < distance; ) {
            int randomMoveX = new Random().nextInt(30);
            //int randomMoveY = 0;
            int randomMoveY = new Random().nextInt(3);
            if(new Random().nextBoolean()){
                randomMoveY = -new Random().nextInt(3);
            }
            //System.out.println("randomMoveY = " + randomMoveY);
            i = i + randomMoveX;
            if (i >= distance) {
                int lastMoveX = i - distance;
                int random1 = new Random().nextInt(10)+10;
                int random2 = new Random().nextInt(10)+10;
                actions.moveToElement(element, randomMoveX - lastMoveX + random1, randomMoveY).perform();
                Thread.sleep(new Random().nextInt(500)+300);
                int randomMoveY2 = new Random().nextInt(5);
                if(new Random().nextBoolean()){
                    randomMoveY2 = -new Random().nextInt(5);
                }
                actions.moveToElement(element, -(random1+random2), randomMoveY2).perform();
                Thread.sleep(new Random().nextInt(500)+300);
                int randomMoveY3 = new Random().nextInt(5);
                if(new Random().nextBoolean()){
                    randomMoveY3 = -new Random().nextInt(5);
                }
                actions.moveToElement(element, random2, randomMoveY3).perform();
                Thread.sleep(new Random().nextInt(500)+300);
            } else {
                actions.moveToElement(element, randomMoveX, randomMoveY).perform();
            }
            int time = new Random().nextInt(500);
            //printLocation(element);
            Thread.sleep(time);
        }
    }

    /**
     * 计算需要平移的距离
     * 计算平移距离，遍历图片的每一个像素点，当两张图的R、G、B之差的和大于255，说明该点的差异过大，
     * 很有可能就是需要平移到该位置的那个点，代码如下。
     *
     * @param driver
     * @return
     * @throws IOException
     */
    public static int getMoveDistance(WebDriver driver) throws IOException {
        BufferedImage fullBI = ImageIO.read(new File("D://jingdongquantu.jpg"));
        BufferedImage bgBI = ImageIO.read(new File("D://heise.jpg"));
        int indexX = 0;
        int indexY = 0;
        int to = 0;
        ok:
        for (int i = 50; i < fullBI.getWidth()-50; i++) {
            for (int j = 0; j < fullBI.getHeight(); j++) {
                int[] fullRgb = new int[3];
                fullRgb[0] = (fullBI.getRGB(i, j) & 0xff0000) >> 16;
                fullRgb[1] = (fullBI.getRGB(i, j) & 0xff00) >> 8;
                fullRgb[2] = (fullBI.getRGB(i, j) & 0xff);

                int[] bgRgb = new int[3];
                bgRgb[0] = (bgBI.getRGB(0, 0) & 0xff0000) >> 16;
                bgRgb[1] = (bgBI.getRGB(0, 0) & 0xff00) >> 8;
                bgRgb[2] = (bgBI.getRGB(0, 0) & 0xff);
                int count = 0;//计数
                if (difference(fullRgb, bgRgb) < 100) {
                    //System.out.println(indexX + "," + indexY);
                    //System.out.println("i = " + i);
                    for (int k = 0; k < 38; k++) {
                        int[] currRgb = new int[3];
                        currRgb[0] = (fullBI.getRGB(i + k, j) & 0xff0000) >> 16;
                        currRgb[1] = (fullBI.getRGB(i + k, j) & 0xff00) >> 8;
                        currRgb[2] = (fullBI.getRGB(i + k, j) & 0xff);
                        if (difference(fullRgb, currRgb) < 20 ||((difference(fullRgb, currRgb) > 40 && difference(fullRgb, currRgb) <= 80))) {
                            //System.out.println("difference(fullRgb, currRgb)=" + difference(fullRgb, currRgb)+",count="+count+",X="+(i+k)+",Y="+j);
                            count++;
                        }
                    }
                   // System.out.println("count = " + count);
                    if (count > 25) {
                        System.out.println("count = " + count);
                        indexX = i;
                        break ok;
                    }
                }
            }
        }
        System.out.println(indexX + "," + indexY);
        return indexX;
        //throw new RuntimeException("未找到需要平移的位置");
    }

    private static int difference(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2]);
    }

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

