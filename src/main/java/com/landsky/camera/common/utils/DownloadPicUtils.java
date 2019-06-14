package com.landsky.camera.common.utils;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DownloadPicUtils {

    public static Map<String,Object> downloadPicture(String picUrl) {

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        BufferedImage bufferedImage = null;
        InputStream is = null;
        try {
            // 创建URL
            URL url = new URL(picUrl);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            is = conn.getInputStream();

            bufferedImage = ImageIO.read(is);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64Img = encoder.encode(outputStream.toByteArray());
            Map<String,Object> map = new HashMap<>();
            map.put("width",width);
            map.put("height",height);
            map.put("base64Img",base64Img.replaceAll("\r\n",""));
            is.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
