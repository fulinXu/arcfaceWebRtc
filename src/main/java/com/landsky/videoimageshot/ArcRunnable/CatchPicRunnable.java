package com.landsky.videoimageshot.ArcRunnable;

import com.landsky.camera.service.impl.BaseLibraryServiceImpl;
import com.landsky.videoimageshot.core.JavaImgConverter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CatchPicRunnable implements Runnable {
    private SynchronizedStack stack;

    public CatchPicRunnable(SynchronizedStack s) {
        this.stack = s;
    }

    @Override
    public void run() {

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        BaseLibraryServiceImpl blsi = new BaseLibraryServiceImpl();
        String base64 = null;
        Frame grabframe = null;
        while (true) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("开始取图" + df.format(new Date()));
                grabframe = com.landsky.videoimageshot.ArcRunnable.SynchronizedStack.frames.poll();
                if (grabframe != null) {
                    System.out.println("取到图了" + df.format(new Date()));
                    base64 = JavaImgConverter.bufferedImage2Base64(JavaImgConverter.imageToMat(grabframe), "jpg");
                    System.out.println(base64);
                    if (blsi.isHaveFaceFeature(base64)) {
                        blsi.push(base64);
                        System.out.println("push:"+base64);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
