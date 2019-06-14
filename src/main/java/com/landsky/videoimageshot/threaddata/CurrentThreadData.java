package com.landsky.videoimageshot.threaddata;


import com.landsky.videoimageshot.core.Base64Plus;
import com.landsky.videoimageshot.core.ByteArrayOutputStreamPlus;

/**
 * 当前线程共享数据
 * @author eguid
 *
 */
public class CurrentThreadData {

	
	public final static String DETAULT_FORMAT = "jpg";
//
//	public final static ThreadLocal<FFmpegVideoImageGrabber> grabber = new ThreadLocal<FFmpegVideoImageGrabber>() {
//		@Override
//		protected FFmpegVideoImageGrabber initialValue() {
//			return new FFmpegVideoImageGrabber() {
//				@Override
//				protected ByteBuffer saveFrame(avutil.AVFrame pFrameRGB, Integer width, Integer height) {
//					return null;
//				}
//
//				@Override
//				protected ByteBuffer saveFrame(avutil.AVFrame pFrameRGB, Integer width, Integer height) {
//					return null;
//				}
//			};
//		}
//	};
	
	public static final ThreadLocal<Base64Plus.Encoder> localEncoder=new ThreadLocal<Base64Plus.Encoder>() {
		protected Base64Plus.Encoder initialValue() {
			return Base64Plus.getEncoder();
		}; 
	};
	
	public static final ThreadLocal<ByteArrayOutputStreamPlus> localbaos=new ThreadLocal<ByteArrayOutputStreamPlus>() {
		protected ByteArrayOutputStreamPlus initialValue() {
			ByteArrayOutputStreamPlus baos=new ByteArrayOutputStreamPlus(1280*720*3);
			return baos;
		}; 
		@Override
		public ByteArrayOutputStreamPlus get() {
			ByteArrayOutputStreamPlus baos=super.get();
			baos.reset();
			return baos;
		}
	};
	
}
