package com.cjh.ttt.base.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;

/**
 * 图片工具类
 */
@Slf4j
public class ImgUtil {

    /**
     * 将图片转换成base64格式进行存储
     */
    public static String img2base64(String url) {
        String type = StringUtils.substring(url, url.lastIndexOf(".") + 1);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new URL(url));
        } catch (Exception e) {
            log.info("url: {}", url);
            log.error("img2base64转换失败: {}", e.getMessage());
        }
        String imageString = "data:image/jpeg;base64,";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            imageString += encoder.encode(imageBytes);
            bos.close();
        } catch (IOException e) {
            log.error("img2base64转换失败: {}", e.getMessage());
        }
        return imageString;
    }

    /**
     * 将图片inputStream转换成服务器文件
     */
    public static String inputStream2file(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            String fileName = System.currentTimeMillis() + ".png";
            getResultPath(fileName);
            ImageIO.write(image, "png", new File(fileName));
            return getResultPath(fileName);
        } catch (IOException e) {
            log.error("将图片inputStream转换成服务器文件链接: {}", e.getMessage());
        }
        return null;
    }

    public static void createDir(String filePath) {
        File file = new File(filePath);
        File dir = new File(file.getParent());
        //如果文件夹不存在则创建
        if (!dir.exists() && !dir.isDirectory()) {
            createDir(dir.getPath());
            boolean mkdir = dir.mkdir();
            log.info("{} 不存在，创建: {}", dir.getPath(), mkdir);
        }
    }

    /**
     * 判断是否windows系统
     */
    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toLowerCase().contains("windows");
    }

    public static String getRealPath(String fileName) {
        String rootPath = "/root/images";
        if (ImgUtil.isWindows()) {
            rootPath = "C:" + rootPath;
        }
        String filePath = "/ttt/";
        String realPath = rootPath + filePath + fileName;
        createDir(realPath);
        return realPath;
    }

    public static String getResultPath(String fileName) {
        String doMain = "http://images.springeasy.cn";
        if (ImgUtil.isWindows()) {
            doMain = "http://192.168.1.76";
        }
        String filePath = "/ttt/";
        return doMain + filePath + fileName;
    }
}
