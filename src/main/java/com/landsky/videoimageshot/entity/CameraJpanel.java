package com.landsky.videoimageshot.entity;

import javax.swing.*;
import java.awt.*;

public class CameraJpanel extends JPanel {
    // JPanel 里面有一个方法(paint)继承了之后需要重写
    // Graphics是一个画笔 是绘图类的一个重要类
    // 这个方法不需要显式的去调用,运行时候系统会自动调用
    // 以下情况也会调用paint
    // 最大化和最小化窗口时
    // 改变窗体大小时
    // repaint()方法被调用时

    public void paint(Graphics graphics){
        super.paint(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.drawRect(50,50,50,50);
        }
        // 简单的画一个圆圈 使用该方法drawOval 参数为 x 坐标 y 坐标 宽度 高度 单位都是像素
        // x 坐标和 y 坐标 为距离我们GUI界面左上角的位置的像素
        // graphics.draw3DRect(50, 50, 50, 50, true);
}


