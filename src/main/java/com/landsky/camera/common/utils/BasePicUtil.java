package com.landsky.camera.common.utils;

/*import cn.com.landsky.camera.CatchPic.videoimageshot.utils.ImageUtil;
import cn.com.landsky.camera.common.utils.baseutil.ReadFile;
import cn.com.landsky.camera.entity.BaseLibrary;*/

import com.arcsoft.face.*;
import com.arcsoft.face.enums.ImageFormat;
import com.landsky.camera.entity.BaseLibrary;
import com.landsky.videoimageshot.utils.ImageUtil;
import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BasePicUtil {

    public static void main(String[] args) throws IOException {
        new BasePicUtil().faceEngineTest();
    }


    public void faceEngineTest() throws IOException {
        String appId = "3ubW4hVCc165ycSHkjiAdMsSzjDYEGHHzTEBXQAkBMgL";
        String sdkKey = "yT3m1BMRDdnH4x5nB1BufKUrkr3vy8A2QC2J52vLTET";

        FaceEngine faceEngine = new FaceEngine();
        //激活引擎
        faceEngine.active(appId, sdkKey);
        EngineConfiguration engineConfiguration = EngineConfiguration.builder().functionConfiguration(
                FunctionConfiguration.builder()
                        .supportAge(true)
                        .supportFace3dAngle(true)
                        .supportFaceDetect(true)
                        .supportFaceRecognition(true)
                        .supportGender(true)
                        .supportLiveness(true)
                        .build()).build();
        //初始化引擎
        faceEngine.init(engineConfiguration);

        List<String> base_pic = ReadFile.readfile("base_pic");
        for (String pic : base_pic) {
            try {
                BaseLibrary record = new BaseLibrary();
                ImageInfo imageInfo = getRGBData(new File(pic));

                //人脸检测
                List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
                faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);

                //提取人脸特征
                FaceFeature faceFeature = new FaceFeature();
                faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);

                //人脸对比
                FaceFeature targetFaceFeature = new FaceFeature();
                targetFaceFeature.setFeatureData(faceFeature.getFeatureData());

                int processResult = faceEngine.process(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, FunctionConfiguration.builder().supportAge(true).supportFace3dAngle(true).supportGender(true).supportLiveness(true).build());

                //性别提取
                List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
                int genderCode = faceEngine.getGender(genderInfoList);
                System.out.println(genderInfoList.get(0).getGender());

                //年龄提取
                List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
                int ageCode = faceEngine.getAge(ageInfoList);
                System.out.println(ageInfoList.get(0).getAge());

                //3D信息提取
                List<Face3DAngle> face3DAngleList = new ArrayList<Face3DAngle>();
                int face3dCode = faceEngine.getFace3DAngle(face3DAngleList);
                System.out.println(face3DAngleList.get(0));

                //活体信息
                List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
                int livenessCode = faceEngine.getLiveness(livenessInfoList);
                System.out.println(livenessInfoList.get(0).getLiveness());
                System.out.println();

                record.setName(pic.substring(pic.lastIndexOf('\\')+1,pic.lastIndexOf('.')));
                record.setAge(ageInfoList.get(0).getAge());
                record.setGender(genderInfoList.get(0).getGender());
                record.setData(faceFeature.getFeatureData());
                record.setImg(ImageUtil.encodeToString(pic).getBytes());
                record.setYaw(face3DAngleList.get(0).getYaw());
                record.setRoll(face3DAngleList.get(0).getRoll());
                record.setPitch(face3DAngleList.get(0).getPitch());
                record.setStatus(face3DAngleList.get(0).getStatus());
                record.setLiveness(livenessInfoList.get(0).getLiveness());
                System.out.println(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public ImageInfo getRGBData(File file) {
        if (file == null){
            return null;
        }
        ImageInfo imageInfo;
        try {
            //将图片文件加载到内存缓冲区
            BufferedImage image = ImageIO.read(file);
            imageInfo = bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageInfo;
    }

    private ImageInfo bufferedImage2ImageInfo(BufferedImage image) {
        ImageInfo imageInfo = new ImageInfo();
        int width = image.getWidth();
        int height = image.getHeight();
        // 使图片居中
        width = width & (~3);
        height = height & (~3);
        imageInfo.setWidth(width);
        imageInfo.setHeight(height);
        //根据原图片信息新建一个图片缓冲区
        BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        //得到原图的rgb像素矩阵
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        //将像素矩阵 绘制到新的图片缓冲区中
        resultImage.setRGB(0, 0, width, height, rgb, 0, width);
        //进行数据格式化为可用数据
        BufferedImage dstImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        if (resultImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
            ColorConvertOp colorConvertOp = new ColorConvertOp(cs, dstImage.createGraphics().getRenderingHints());
            colorConvertOp.filter(resultImage, dstImage);
        } else {
            dstImage = resultImage;
        }
        //获取rgb数据
        imageInfo.setRgbData(((DataBufferByte) (dstImage.getRaster().getDataBuffer())).getData());
        return imageInfo;
    }


    class ImageInfo {
        public byte[] rgbData;
        public int width;
        public int height;

        public byte[] getRgbData() {
            return rgbData;
        }

        public void setRgbData(byte[] rgbData) {
            this.rgbData = rgbData;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
