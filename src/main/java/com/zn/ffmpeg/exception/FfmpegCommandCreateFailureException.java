/**
 * 
 */
package com.zn.ffmpeg.exception;

/**
 * ffmpeg命令组装错误
 * @author zhengnan
 *
 */
public class FfmpegCommandCreateFailureException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public FfmpegCommandCreateFailureException() {
		super();
	}
	
	public FfmpegCommandCreateFailureException(Throwable throwable) {
		super(throwable);
	}
	
	public FfmpegCommandCreateFailureException(String msg) {
		super(msg);
	}
}
