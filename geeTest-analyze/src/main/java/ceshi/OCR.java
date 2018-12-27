package ceshi;

import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by WSK on 2017/1/6.
 */
public class OCR {
    /**
     *
     * @param srImage 图片路径
     * @param ENG 是否使用中文训练库,true-是
     * @return 识别结果
     */
    public static String FindOCR(String srImage, boolean ENG) {
        try {
            System.out.println("start");
            double start=System.currentTimeMillis();
            File imageFile = new File(srImage);
            if (!imageFile.exists()) {
                return "图片不存在";
            }
            BufferedImage textImage = ImageIO.read(imageFile);
            Tesseract instance=Tesseract.getInstance();
            instance.setDatapath("D:\\Program Files (x86)\\Tesseract-OCR\\tessdata");//设置训练库
            if (ENG)
                instance.setLanguage("eng");//中文识别
            String result = null;
            result = instance.doOCR(textImage);
            double end=System.currentTimeMillis();
            System.out.println("耗时"+(end-start)/1000+" s");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "发生未知错误";
        }
    }
    public static void main(String[] args) throws Exception {
        String result=FindOCR("D:/work/测试用例/图片识别/ceshi.jpg",true);
        System.out.println(result);
    }
}
