package com.landsky.videoimageshot.ArcRunnable;


import org.bytedeco.javacv.Frame;

import java.util.concurrent.ConcurrentLinkedQueue;

public  class SynchronizedStack {
    public  static  ConcurrentLinkedQueue<Frame> frames = new ConcurrentLinkedQueue<>();
    /**
     * 生产数据
     */
    public synchronized void push(Frame grabframe){
            frames.offer(grabframe);
        }
    /**
     * 消费数据
     *
     * @return
     */
    public synchronized Frame pop() throws InterruptedException {
       if(frames.isEmpty()){
           System.out.println("等待取帧");
           return  null;
       }else {
            return frames.poll();
       }
    }
}
