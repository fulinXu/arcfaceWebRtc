package com.landsky.videoimageshot;


import com.landsky.videoimageshot.ArcRunnable.ReadStreamRunnable;
import com.landsky.videoimageshot.ArcRunnable.SynchronizedStack;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

        /**
         * @param inputFile
         * @throws Exception
         * @throws org.bytedeco.javacv.FrameRecorder.Exception
         * @throws InterruptedException
         */
        public static void recordPush(String inputFile) throws Exception, org.bytedeco.javacv.FrameRecorder.Exception, InterruptedException {
            ExecutorService service = Executors.newCachedThreadPool();
                       SynchronizedStack stack = new SynchronizedStack();
            ReadStreamRunnable readStreamRunnable = new ReadStreamRunnable(inputFile, stack);
//            CatchPicRunnable catchPicRunnable1 = new CatchPicRunnable(stack);
//            Thread readStreamThread = new Thread(readStreamRunnable);
//              Thread catchPicthread = new Thread(catchPicRunnable1);
//              catchPicthread.start();
            service.submit(readStreamRunnable);
//            service.submit(catchPicRunnable1);
//            readStreamThread.start();

            CountDownLatch cdl5 = new CountDownLatch(Integer.MAX_VALUE);
            for (; ; ) {
                cdl5.countDown();
                Thread.sleep(500);
                if (cdl5.getCount() == 0) {
                    cdl5.await();
                    break;
                }
            }


        }
}
