package com.landsky.camera.service.impl;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.ImageFormat;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.landsky.camera.common.utils.ReadFile;
import com.landsky.camera.entity.BaseLibrary;
import com.landsky.camera.entity.DistinguishResult;
import com.landsky.camera.entity.Person;
import com.landsky.camera.mapper.BaseLibraryMapper;
import com.landsky.camera.service.IBaseLibraryService;
import com.landsky.videoimageshot.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Musk
 * @since 2019-05-08
 */
@Service
@Transactional
public class BaseLibraryServiceImpl extends ServiceImpl<BaseLibraryMapper, BaseLibrary> implements IBaseLibraryService {
    public static volatile List<BaseLibrary> faceList = null;

    @Autowired
    static FaceEngine faceEngine = null;


    /**
     * 往广告屏推送
     * @param base64img
     */
    @Override
    public void push(String base64img) {
        System.out.println("*******************************************");
        BufferedImage bufferedImage = ImageUtil.getBufferedImage(base64img);
        ImageInfo imageInfo = getRGBData(bufferedImage);
        List<BaseLibrary> list = getFaceList();
        if (getFaceList() == null) {
            list = this.list();
            setFaceList(list);
        }

        FaceFeature faceFeature = getFaceFeature(imageInfo);

        list.forEach(li -> {
            FaceFeature sourceFaceFeature = new FaceFeature();
            sourceFaceFeature.setFeatureData(li.getData());
            FaceSimilar faceSimilar = new FaceSimilar();
            faceEngine.compareFaceFeature(faceFeature, sourceFaceFeature, faceSimilar);
            if (faceSimilar.getScore() > 0.4f) {

                System.out.println("找到啦： " + li.toString());
//                ImageUtil.generateImage(new String(li.getImg()), "pic" + File.separator + li.getName() + ".jpg");
                return;
            }
        });

    }


    public List<DistinguishResult> contrast(String base64img) {
        DistinguishResult distinguishResult = new DistinguishResult();
        BufferedImage bufferedImage = ImageUtil.getBufferedImage(base64img);
        ImageInfo imageInfo = getRGBData(bufferedImage);
        List<BaseLibrary> list = getFaceList();
        List<DistinguishResult> featureList = new ArrayList<>();

        if (getFaceList() == null) {
            list = this.list();
            setFaceList(list);
        }
        List<Person> personInfoList = getPersonInfo(base64img);
        List<FaceFeature> faceFeatureList = getFaceFeatureList(imageInfo);
        for(int i = 0 ; i < personInfoList.size() ; i++){
            for(BaseLibrary li : list){
                for(FaceFeature faceFeature : faceFeatureList){
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(li.getData());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(faceFeature, sourceFaceFeature, faceSimilar);
                    if (faceSimilar.getScore() > 0.8f) {
                        distinguishResult.setBaseLibrary(li);
                    }
                    else {
                        continue;
                    }
                }
            }
            distinguishResult.setPerson(personInfoList.get(i));
            featureList.add(distinguishResult);
        }

        return  featureList;
    }



    public void faceEngineTest() throws IOException {


        List<String> base_pic = ReadFile.readfile("pic");
        for (String pic : base_pic) {
            try {

                String base64img = ImageUtil.encodeToString(pic);
                BufferedImage bufferedImage = ImageUtil.getBufferedImage(base64img);
                ImageInfo imageInfo = getRGBData(bufferedImage);


                BaseLibrary record = extrac(pic, imageInfo, bufferedImage);
                if (record == null) {
                    System.out.println(pic + " 检测失败");
                    continue;
                } else if (this.save(record)) {
                    System.out.println(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void extrac(ImageInfo imageInfo) {
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
    }

    private FaceFeature getFaceFeature(ImageInfo imageInfo) {
        //人脸检测
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);

        //提取人脸特征
        FaceFeature faceFeature = new FaceFeature();
        faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);

        return faceFeature;

    }

    //图片上的多张人脸和底库进行对比
    private List<FaceFeature> getFaceFeatureList(ImageInfo imageInfo) {
        //人脸检测
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);

        //提取人脸特征
        List<FaceFeature> faceFeatureList = new ArrayList<>();


        for (int i =0;i<faceInfoList.size();i++){
            FaceFeature faceFeature = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(i), faceFeature);
            faceFeatureList.add(faceFeature);
        }

        return faceFeatureList;

    }


    /**
     * 是否有人脸特征
     * @param base64img
     * @return
     */
    @Override
    public boolean isHaveFaceFeature(String base64img) {
        System.out.println("开始检测人脸-*-----------------------------------------------------------------------");
        try {
            BufferedImage bufferedImage = ImageUtil.getBufferedImage(base64img);


            ImageInfo imageInfo = getRGBData(bufferedImage);
            //人脸检测
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            if (faceInfoList.size() <= 0) {
                return false;
            }

            //提取人脸特征
            FaceFeature faceFeature = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
            if (faceFeature == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("检测到人脸信息");
        return true;

    }

    @Override
    public List<FaceInfo> getFaceInfo(String base64) {
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        try {
            BufferedImage bufferedImage = ImageUtil.getBufferedImage(base64);
            ImageInfo imageInfo = getRGBData(bufferedImage);
            //人脸检测
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            return faceInfoList;

        } catch (Exception e) {
            e.printStackTrace();
            return faceInfoList;
        }

    }

    public List<Person> getPersonInfo(String base64) {
        List<Person> personList = new ArrayList<>();
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        try {
            BufferedImage bufferedImage = ImageUtil.getBufferedImage(base64);
            ImageInfo imageInfo = getRGBData(bufferedImage);
            //人脸检测
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            //性别提取
            List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
            faceEngine.getGender(genderInfoList);
            //年龄提取
            List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
            faceEngine.getAge(ageInfoList);
            if(faceInfoList.size()>0){
                for (int i = 0; i < faceInfoList.size(); i++) {
                    Person person = new Person();
                    person.setFaceInfo(faceInfoList.get(i));
                    if(ageInfoList.size()>0) {
                        person.setAgeInfo(ageInfoList.get(i).getAge());
                    }
                    if (genderInfoList.size()>0){
                        person.setGenderInfo(genderInfoList.get(i).getGender());
                    }
                    personList.add(person);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personList;
    }

    private BaseLibrary extrac(String pic, ImageInfo imageInfo, BufferedImage bufferedImage) throws IOException {
        //人脸检测
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        if (faceInfoList.size() <= 0) {
            return null;
        }

        //提取人脸特征
        FaceFeature faceFeature = new FaceFeature();
        faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);

        if (faceFeature == null) {
            return null;
        }
//
//        //人脸对比
//        FaceFeature targetFaceFeature = new FaceFeature();
//        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());

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


        BaseLibrary record = new BaseLibrary();
        record.setWidth(bufferedImage.getWidth());
        record.setHeight(bufferedImage.getHeight());

        String substring = pic.substring(pic.lastIndexOf('\\') + 1, pic.lastIndexOf('.'));
        String[] split = substring.split("-");

        if (split.length >= 2) {
            record.setName(split[0]);
            record.setCareer(split[1]);
        } else {
            record.setName(substring);
            record.setCareer("");
        }
        record.setAge(ageInfoList.get(0).getAge());
        record.setGender(genderInfoList.get(0).getGender());
        record.setData(faceFeature.getFeatureData());
        record.setImg(ImageUtil.encodeToString(pic).getBytes());
        record.setYaw(face3DAngleList.get(0).getYaw());
        record.setRoll(face3DAngleList.get(0).getRoll());
        record.setPitch(face3DAngleList.get(0).getPitch());
        record.setStatus(face3DAngleList.get(0).getStatus());
        record.setLiveness(livenessInfoList.get(0).getLiveness());
        return record;
    }

    public ImageInfo getRGBData(File file) {
        if (file == null) {
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

    public ImageInfo getRGBData(BufferedImage image) {
        if (image == null) {
            return null;
        }
        ImageInfo imageInfo;
        try {
            //将图片文件加载到内存缓冲区
//            BufferedImage image = ImageIO.read(file);
            imageInfo = bufferedImage2ImageInfo(image);
        } catch (Exception e) {
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


//    @PostConstruct
     static  {
        String appId = "3ubW4hVCc165ycSHkjiAdMsSzjDYEGHHzTEBXQAkBMgL";
        String sdkKey = "yT3m1BMRDdnH4x5nB1BufKUrkr3vy8A2QC2J52vLTET";

        faceEngine = new FaceEngine();
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
    }

//    @PreDestroy
//    public void destroy() {
//        faceEngine.unInit();
//    }


    public static List<BaseLibrary> getFaceList() {
        return faceList;
    }

    public static void setFaceList(List<BaseLibrary> faceList) {
        BaseLibraryServiceImpl.faceList = faceList;
    }
}
