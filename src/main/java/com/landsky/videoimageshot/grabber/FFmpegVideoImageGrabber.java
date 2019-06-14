package com.landsky.videoimageshot.grabber;


import com.landsky.videoimageshot.core.JavaImgConverter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_BGR24;


/**
 * 视频帧抓取
 *
 * @author eguid
 *
 */
public abstract class FFmpegVideoImageGrabber extends GrabberTmplate implements com.landsky.videoimageshot.grabber.BufferGrabber, com.landsky.videoimageshot.grabber.BufferedImageGrabber {

	public FFmpegVideoImageGrabber setWidth(Integer width) {
		this.width = width;
		return this;
	}

	public FFmpegVideoImageGrabber setHeight(Integer height) {
		this.height = height;
		return this;
	}

//	protected ByteBuffer saveFrame(avutil.AVFrame pFrame, int width, int height){
//		BytePointer data = pFrame.data(0);
//		int size = width * height * 3;
//		ByteBuffer buf = data.position(0).limit(size).asBuffer();
//		return buf;
//	}

	@Override
	public ByteBuffer grabBuffer() throws IOException {
		return grabBuffer(url);
	}

	@Override
	public ByteBuffer grabBuffer(String url) throws IOException {
		return grabBuffer(url,null);
	}

	@Override
	public ByteBuffer grabBuffer(String url,Integer fmt) throws IOException {
		ByteBuffer buf=null;
		if(validateAndInit(url,fmt)) {
			buf= grabVideoFrame(url,this.fmt);
		}
		return buf;
	}

	/*
	 * 验证并初始化
	 * @param url
	 * @param fmt
	 * @return
	 */
	private boolean validateAndInit(String url,Integer fmt) {
		if (url == null) {
			throw new IllegalArgumentException("Didn't open video file");
		}
		if(fmt == null) {
			this.fmt=AV_PIX_FMT_BGR24;
		}
		return true;
	}

	@Override
	public BufferedImage grabBufferImage() throws IOException {
		return grabBufferImage(url,null);
	}

	@Override
	public BufferedImage grabBufferImage(String url) throws IOException {
		return grabBufferImage(url,null);
	}

	@Override
	public BufferedImage grabBufferImage(String url, Integer fmt) throws IOException {
		BufferedImage image=null;
		long now =System.currentTimeMillis();
		ByteBuffer buf=grabBuffer(url,fmt);
		long cu=System.currentTimeMillis();
		System.err.println("截图耗时："+(cu-now));
		image= JavaImgConverter.BGR2BufferedImage(buf,width,height);
		return image;
	}

	private String url;//视频地址
	private Integer fmt;//图像数据结构

	public FFmpegVideoImageGrabber() {

	}

	public FFmpegVideoImageGrabber(String url) {
		this.url=url;
	}

	public FFmpegVideoImageGrabber(String url, Integer fmt) {
		super();
		this.url = url;
		this.fmt = fmt;
	}

	public FFmpegVideoImageGrabber(String url, Integer fmt,Integer width,Integer height) {
		super(width,height);
		this.url = url;
		this.fmt = fmt;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public int getFmt() {
		return fmt;
	}

	public void setFmt(int fmt) {
		this.fmt = fmt;
	}
}
