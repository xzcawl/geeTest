package com.slide;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtils {
    /**
     * 图片去白色的背景，并裁切
     *
     * @param image 图片
     * @param range 范围 1-255 越大 容错越高 去掉的背景越多
     * @return 图片
     * @throws Exception 异常
     */
    public static byte[] transferAlpha(Image image, InputStream in, int range) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon
                    .getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon
                    .getImageObserver());
            int alpha = 0;
            int minX = bufferedImage.getWidth();
            int minY = bufferedImage.getHeight();
            int maxX = 0;
            int maxY = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage
                    .getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage
                        .getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < range) && ((255 - G) < range) && ((255 - B) < range)) { //去除白色背景；
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    } else {
                        minX = minX <= j2 ? minX : j2;
                        minY = minY <= j1 ? minY : j1;
                        maxX = maxX >= j2 ? maxX : j2;
                        maxY = maxY >= j1 ? maxY : j1;
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            int width = maxX - minX;
            int height = maxY - minY;
            BufferedImage sub = bufferedImage.getSubimage(minX, minY, width, height);
            int degree = getDegree(in);
            sub = rotateImage(sub, degree);
            ImageIO.write(sub, "png", byteArrayOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 图片旋转
     *
     * @param bufferedimage bufferedimage
     * @param degree        旋转的角度
     * @return BufferedImage
     */
    public static BufferedImage rotateImage(final BufferedImage bufferedimage,
                                            final int degree) {
        int w = bufferedimage.getWidth();
        int h = bufferedimage.getHeight();
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
                w, h)), degree);
        int type = bufferedimage.getColorModel().getTransparency();
        BufferedImage img;
        Graphics2D graphics2d;
        (graphics2d = (img = new BufferedImage(rect_des.width, rect_des.height, type))
                .createGraphics()).setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.translate((rect_des.width - w) / 2,
                (rect_des.height - h) / 2);
        graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
        graphics2d.drawImage(bufferedimage, 0, 0, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 计算旋转后图像的大小
     *
     * @param src    Rectangle
     * @param degree 旋转的角度
     * @return Rectangle
     */
    public static Rectangle CalcRotatedSize(Rectangle src, int degree) {
        if (degree >= 90) {
            if (degree / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            degree = degree % 90;
        }
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(degree) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(degree)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    /**
     * byte[] ------>BufferedImage
     *
     * @param byteImage byteImage
     * @return return
     * @throws IOException IOException
     */
    public static BufferedImage ByteToBufferedImage(byte[] byteImage) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(byteImage);
        return ImageIO.read(in);
    }

    /**
     * 获取照片信息的旋转角度
     *
     * @param inputStream 照片的路径
     * @return 角度
     */
    public static int getDegree(InputStream inputStream) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(inputStream), 1);
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if ("Orientation".equals(tag.getTagName())) {
                        return turn(getOrientation(tag.getDescription()));
                    }
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    /**
     * 获取旋转的角度
     *
     * @param orientation orientation
     * @return 旋转的角度
     */
    public static int turn(int orientation) {
        Integer turn = 360;
        if (orientation == 0 || orientation == 1) {
            turn = 360;
        } else if (orientation == 3) {
            turn = 180;
        } else if (orientation == 6) {
            turn = 90;
        } else if (orientation == 8) {
            turn = 270;
        }
        return turn;
    }

    /**
     * 根据图片自带的旋转的信息 获取 orientation
     *
     * @param orientation orientation
     * @return orientation
     */
    public static int getOrientation(String orientation) {
        int tag = 0;
        if ("Top, left side (Horizontal / normal)".equalsIgnoreCase(orientation)) {
            tag = 1;
        } else if ("Top, right side (Mirror horizontal)".equalsIgnoreCase(orientation)) {
            tag = 2;
        } else if ("Bottom, right side (Rotate 180)".equalsIgnoreCase(orientation)) {
            tag = 3;
        } else if ("Bottom, left side (Mirror vertical)".equalsIgnoreCase(orientation)) {
            tag = 4;
        } else if ("Left side, top (Mirror horizontal and rotate 270 CW)".equalsIgnoreCase(orientation)) {
            tag = 5;
        } else if ("Right side, top (Rotate 90 CW)".equalsIgnoreCase(orientation)) {
            tag = 6;
        } else if ("Right side, bottom (Mirror horizontal and rotate 90 CW)".equalsIgnoreCase(orientation)) {
            tag = 7;
        } else if ("Left side, bottom (Rotate 270 CW)".equalsIgnoreCase(orientation)) {
            tag = 8;
        }
        return tag;
    }
}