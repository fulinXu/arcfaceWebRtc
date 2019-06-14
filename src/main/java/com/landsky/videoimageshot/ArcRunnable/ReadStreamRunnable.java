package com.landsky.videoimageshot.ArcRunnable;


import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadStreamRunnable implements  Runnable{
    private  SynchronizedStack stack;
    private final String inputFile;

    public ReadStreamRunnable(String inputFile,SynchronizedStack s) {
        this.inputFile = inputFile;
        this.stack = s;
    }

    @Override
    public void run() {
        System.out.println("开始抓图");
        while (true){
        System.out.println("*******************************");
        Loader.load(opencv_objdetect.class);
        FrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
            try {
                grabber.start();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
//      grabber.restart();
        Frame grabframe = null;
            try {
                grabframe = grabber.grab();
                System.out.println("抓到图了哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println(df.format(new Date()));
                com.landsky.videoimageshot.ArcRunnable.SynchronizedStack.frames.offer(grabframe);
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }

    }
}
