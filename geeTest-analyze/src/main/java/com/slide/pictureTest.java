package com.slide;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.sanselan.ImageReadException;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;

/*
* 极验攻击
*/
public class pictureTest {
    private WebDriver driver;
    private WebDriver.Navigation navigation;
    private String baseUrl = "http://www.geetest.com/demo/";
    private static boolean moveArrayInit = false;
    private static int[][] moveArray = new int[52][2];

    @Test
    public void testPictureTest(){
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
            for (int i = 0;i < 300;i++) {
                int a = geeTest1();
                System.out.println("a = " + a);
                if (a == 0){
                    countNothing++;
                }else if(a == 1){
                    countSucc++;
                }else if(a == 2){
                    countErr++;
                }else if(a == 2){
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


    public int geeTest1() throws InterruptedException, IOException, ImageReadException, TesseractException {
        //加载到指定url
        navigation.to(baseUrl);
        Actions actions = new Actions(driver);
        Thread.sleep(500);
        WebElement element = driver.findElement(By.xpath("//*[text()='大图模式-custom']"));
        element.click();
        Thread.sleep(1000);
        WebElement gameBtn = driver.findElement(By.className("geetest_radar_tip_content"));
        actions.clickAndHold(gameBtn).perform();
        Thread.sleep(500);
        actions.release().perform();
        //File fullFile = new File("D:/fullFile.jpg");
        //Thread.sleep(1000);
        //cutPic(screenShots().toString(),fullFile.toString(),520,420,260,160);
        //WebElement picture = driver.findElement(By.className("geetest_canvas_fullbg"));

        //WebElement slider_button = driver.findElement(By.className("geetest_slider_button"));
        //actions.clickAndHold(slider_button).perform();
        //screenShots().toString();
        File outFile = new File("D:/outFile.jpg");
        Thread.sleep(2000);
        cutPic(screenShots().toString(),outFile.toString(),520,420,247,247);


        //Tesseract instance = new Tesseract();
        //将验证码图片的内容识别为字符串
        //String result = instance.doOCR(outFile);

        BufferedImage textImage = null;
        BufferedImage image = ImageIO.read(new FileInputStream(outFile));
        // 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分
        textImage = ImageHelper.convertImageToGrayscale(ImageHelper.getSubImage(image, 0, 0, image.getWidth(), image.getHeight()));  //对图片进行处理
        textImage = ImageHelper.getScaledInstance(image, image.getWidth() * 5, image.getHeight() * 5);  //将图片扩大5倍
        //将其保存在C:/imageSort/targetPIC/下
        ImageIO.write(textImage, "jpg", new File("D://text.jpg"));

        ITesseract instance = Tesseract.getInstance();  // JNA Interface Mapping
        //instance.setDatapath("D://Program Files (x86)/Tesseract-OCR");

        URL url = ClassLoader.getSystemResource("tessdata");
        String path = url.getPath().substring(1);
        instance.setDatapath(path);


        instance.setLanguage("chi_sim+equ+eng");//chi_sim是中文识别包，equ是数学公式包，eng是英文包
        String result = instance.doOCR(new File("D://ceshi.png"));
        System.out.println("result = " + result);

        //int moveX = new Random().nextInt(8) - 5;
        //int moveY = 1;

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

        WebElement flag = driver.findElement(By.xpath("//div[starts-with(@class,'geetest_slider geetest_')]"));
        String is = flag.getAttribute("class");
        //System.out.println("is = " + is);
        int a = 0;
        if (is.equals("geetest_slider geetest_success")) {
            a = 1;
        } else if (is.equals("geetest_slider geetest_fail")) {
            a = 2;
        } else if (is.equals("geetest_slider geetest_forbidden")) {
            a = 3;
        }
        //另一中的拖拽防止dragAndDrop不支持有些浏览器
        //action.clickAndHold(elementsource).moveToElement(elementtarget).build().perform();
        return a;

    }


    public static File screenShots(){
        Robot robot;
        File file = new File("D:/" + System.currentTimeMillis() + ".jpg");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            robot = new Robot();
            BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(d.width, d.height));
            ImageIO.write(bufferedImage, "jpg", file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("ok");
        return file;
    }


    /**
     * 计算需要平移的距离
     *  计算平移距离，遍历图片的每一个像素点，当两张图的R、G、B之差的和大于255，说明该点的差异过大，
     *  很有可能就是需要平移到该位置的那个点，代码如下。
     * @param driver
     * @return
     * @throws IOException
     */
    public static int getMoveDistance(WebDriver driver) throws IOException {
        BufferedImage fullBI = ImageIO.read(new File("D:/fullFile.jpg"));
        BufferedImage bgBI = ImageIO.read(new File("D:/outFile.jpg"));

        for (int i = 58; i < bgBI.getWidth(); i++){
            for (int j = 0; j < bgBI.getHeight(); j++) {
                int[] fullRgb = new int[3];
                fullRgb[0] = (fullBI.getRGB(i, j)  & 0xff0000) >> 16;
                fullRgb[1] = (fullBI.getRGB(i, j)  & 0xff00) >> 8;
                fullRgb[2] = (fullBI.getRGB(i, j)  & 0xff);

                int[] bgRgb = new int[3];
                bgRgb[0] = (bgBI.getRGB(i, j)  & 0xff0000) >> 16;
                bgRgb[1] = (bgBI.getRGB(i, j)  & 0xff00) >> 8;
                bgRgb[2] = (bgBI.getRGB(i, j)  & 0xff);
                if(difference(fullRgb, bgRgb) > 300){
                    return i;
                }
            }
        }
        return 0;
        //throw new RuntimeException("未找到需要平移的位置");
    }

    public static void move(WebDriver driver, WebElement element, int distance) throws InterruptedException {
        int xDis = distance;
        System.out.println("应平移距离：" + xDis);
        int moveX = new Random().nextInt(8) - 5;
        int moveY = 1;
        Actions actions = new Actions(driver);
        new Actions(driver).clickAndHold(element).perform();
        Thread.sleep(200);
        printLocation(element);
        actions.moveToElement(element, moveX, moveY).perform();
        System.out.println(moveX + "--" + moveY);
        printLocation(element);
        for (int i = 0; i < 22; i++){
            int s = 10;
            if (i % 2 == 0){
                s = -10;
            }
            actions.moveToElement(element, s, 1).perform();
            printLocation(element);
            Thread.sleep(new Random().nextInt(100) + 150);
        }

        System.out.println(xDis + "--" + 1);
        actions.moveByOffset(xDis, 1).perform();
        printLocation(element);
        Thread.sleep(200);
        actions.release(element).perform();
    }


    public static boolean cutPic(String srcFile, String outFile, int x, int y,
                                 int width, int height) {
        FileInputStream is = null;
        ImageInputStream iis = null;
        try {
            if (!new File(srcFile).exists()) {
                return false;
            }
            is = new FileInputStream(srcFile);
            String ext = srcFile.substring(srcFile.lastIndexOf(".") + 1);
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(ext);
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            File tempOutFile = new File(outFile);
            if (!tempOutFile.exists()) {
                tempOutFile.mkdirs();
            }
            ImageIO.write(bi, ext, new File(outFile));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (iis != null) {
                    iis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private static int difference(int[] a, int[] b){
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2]);
    }

    private static void printLocation(WebElement element) {
        Point point = element.getLocation();
        System.out.println(point.toString());
    }
}
