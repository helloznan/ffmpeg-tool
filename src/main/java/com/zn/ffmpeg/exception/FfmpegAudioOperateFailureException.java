/**
 * 
 */
package com.zn.ffmpeg.exception;

/**
 * @author zhengnan
 *
 */
public class FfmpegAudioOperateFailureException extends Exception{
	private static final long serialVersionUID = 1L;

	public FfmpegAudioOperateFailureException() {
		super();
	}
	
	public FfmpegAudioOperateFailureException(String msg) {
		super(msg);
	}
	
	public FfmpegAudioOperateFailureException(Throwable throwable) {
		super(throwable);
	}
}
