//package videoimageshot.utils;
//
//import javax.swing.*;
//import java.awt.event.MouseEvent;
//
//public class JFrameNature {
//
//
//
//    //鼠标拖拽时不断设置frame的新位置
//    public void mouseDragged(MouseEvent  e){
//        int dragx=e.getX();
//        int dragy=e.getY();
//
//
//    }
//    public  void mousePull(JFrame frame, MouseEvent e){
//        //鼠标press时的座标
//        int pressX  = e.getX();
//        int pressY  = e.getY();
//        int framex=frame.getX();
//        int framey=frame.getY();
//        frame.addMouseListener();
//        frame.addMouseMotionListener();
//        int frameNowX=framex+dragx-pressX;
//        int frameNowY=framey+dragy-pressY;
//        frame.setLocation(frameNowX,frameNowY);
//    }
//
//}
