package com.landsky.videoimageshot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.arcsoft.face.FaceInfo;
import com.landsky.camera.entity.DistinguishResult;
import com.landsky.camera.service.IBaseLibraryService;
import com.landsky.camera.service.impl.BaseLibraryServiceImpl;
import com.landsky.socket.WebSocketPush;
import com.landsky.videoimageshot.core.JavaImgConverter;
import com.landsky.videoimageshot.utils.CustomThreadFactory;
import com.landsky.videoimageshot.utils.ImgUtils;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

@Component
public class RecordShot extends JPanel {
    private static final ScheduledExecutorService excutor = new ScheduledThreadPoolExecutor(1, new CustomThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
//    private static final ExecutorService pool = Executors.newFixedThreadPool(10);
    @Autowired
     static IBaseLibraryService blsi = new BaseLibraryServiceImpl();
    public RecordShot(IBaseLibraryService blsi) {
        this.blsi = blsi;
    }

    /**
     * 转流器
     * @param inputFile
     * @throws Exception
     * @throws org.bytedeco.javacv.FrameRecorder.Exception
     * @throws InterruptedException
     */
    public static void recordPush(String inputFile) {
        Loader.load(opencv_objdetect.class);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        opencv_core.IplImage grabbedImage =null;
        //设置尺寸
        int width = 640;
        int height = 480;

        grabber.setImageWidth(width);
        grabber.setImageHeight(height);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
//      grabber.restart();
        Frame grabframe = null;

        Integer sum = 1;
        for (; ;){
            try {
                grabframe = grabber.grab();
                grabbedImage = converter.convert(grabframe);
//                opencv_imgcodecs.cvSaveImage("E:\\imgUrl\\hello.jpg", grabbedImage);
//                抓取关键帧
//                grabframe = grabber.grabKeyFrame();
//                frames.offer(grabframe);
                sum++;
//                if (sum % 20 == 0) {
                    Task task = new Task(grabbedImage);
//                    pool.execute(task);
                   excutor.execute(task);
                    sum = 1;
//                }


            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Task implements Runnable{
        private opencv_core.IplImage grabbedImage;
        public Task(opencv_core.IplImage grabbedImage) {
            this.grabbedImage = grabbedImage;
        }

        @Override
        public void run() {
            try {
//                class  CameraLivePanel extends JPanel {
//                    private BufferedImage image ;
//                    public  CameraLivePanel(){
//                        image =  JavaImgConverter.imageToMBufferImage(grabbedImage);
//                    }
//                    @Override
//                    public void paintComponent(Graphics g) {
//                       g.drawImage(JavaImgConverter.imageToMBufferImage(grabbedImage), 0, 0, null);
//                    }
//                }
                String  base64 = JavaImgConverter.bufferedImage2Base64(JavaImgConverter.imageToMBufferImage(grabbedImage), "jpg");
                ImgUtils.generateImage(base64, "pic" + File.separator + UUID.randomUUID() + ".jpg");
                //直接传图片取人脸坐标，没有size为0
                List<DistinguishResult> personList = blsi.contrast(base64);
                List<FaceInfo>  faceCoordinatesList = blsi.getFaceInfo(base64);
//                JSONObject rectJsonObject = new JSONObject();
                System.out.println("------------------------"+faceCoordinatesList.size()+"-------------------");
                if(faceCoordinatesList!=null){
                    if (faceCoordinatesList.size()!=0){
//                        for (DistinguishResult person : personList){
//                            Rect rect =  faceInfo.getRect();
//                            rectJsonObject= (JSONObject) JSONObject.toJSON(rect);
                        System.out.println(JSONArray.parseArray(JSON.toJSONString(personList)).toString());
                            WebSocketPush.sendMessage(JSONArray.parseArray(JSON.toJSONString(personList)).toString());
//                        jPanel.paint(memoryGraphics,rect.getLeft(),rect.getTop(),rect.getRight()-rect.getLeft(),rect.getBottom()-rect.getTop());
//                        }
                    }
                }
                //先判断是否有人脸，然后推到广告屏
//                if (blsi.isHaveFaceFeature(base64)) {
//                    System.err.println("有人脸贴着++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                    blsi.push(base64);
//                    System.out.println("push:"+base64);
//                }else{
//                    System.out.println("没有人脸贴着++++++++++++++++++++++");
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
