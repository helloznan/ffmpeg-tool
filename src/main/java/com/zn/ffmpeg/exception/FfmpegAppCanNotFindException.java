/**
 * 
 */
package com.zn.ffmpeg.exception;

/**
 * 无法找到ffmpeg应用，未设置应用目录
 * @author zhengnan
 *
 */
public class FfmpegAppCanNotFindException extends Throwable {
	private static final long serialVersionUID = 1L;
	

	public FfmpegAppCanNotFindException() {
		super();
	}
	
	public FfmpegAppCanNotFindException(Throwable throwable) {
		super(throwable);
	}
	
	public FfmpegAppCanNotFindException(String msg) {
		super(msg);
	}
}
