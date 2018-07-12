/**
 * 
 */
package com.zn.ffmpeg.exception;

/**
 * 内容类型不符合异常
 * @author zhengnan
 *
 */
public class TypeErrorFfmpegException extends Exception {
	private static final long serialVersionUID = 1L;

	public TypeErrorFfmpegException() {
		super();
	}
	
	public TypeErrorFfmpegException(String msg) {
		super(msg);
	}
	
	public TypeErrorFfmpegException(Throwable e) {
		super(e);
	}
}
