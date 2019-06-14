package com.landsky.videoimageshot;

import com.landsky.camera.service.impl.BaseLibraryServiceImpl;

public class ArcRun {

    static
    BaseLibraryServiceImpl iBaseLibraryService = new BaseLibraryServiceImpl();
    public static void main(String[] args) {
        String rtsp2 = "rtsp://admin:qwer1234@192.168.30.205:554/h264/ch1/sub/av_stream";
        String rtsp = "rtsp://admin:qwer1234@192.168.30.250:554/MPEG-4/ch1/sub/av_stream";
        String newHeiguangRysp = "rtsp://admin:qwer1234@192.168.30.175:554/MPEG-4/ch1/sub/av_stream";
        String rtsp3 = "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";
        String whiteRtsp = "rtsp://admin:qwer1234@192.168.30.251:554/h264/ch1/sub/av_stream";
//      RecordShot.recordPush(rtsp);
//        cn.com.landsky.camera.CatchPic.videoimageshot.Test.recordPush(whiteRtsp);
        RecordShot recordShot = new RecordShot(iBaseLibraryService);
        recordShot.recordPush(rtsp2);
    }
}
