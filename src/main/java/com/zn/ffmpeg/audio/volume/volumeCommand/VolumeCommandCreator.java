/**
 * 
 */
package com.zn.ffmpeg.audio.volume.volumeCommand;

import java.util.List;

import com.zn.ffmpeg.exception.FfmpegCommandCreateFailureException;

/**
 * 音量调节命令生成器
 * @author zhengnan
 *
 */
public abstract interface VolumeCommandCreator {
	/**
	 * 组装命令
	 * @param volumeUpdateNumber 音量修改大小（分贝值：如3=增加或减小3分贝，翻倍值：如2=音量增强1倍，缩倍值：如2=音量减小1半）
	 * @param inputAudioPath 输入流音频路径
	 * @param outputAudioPath 输出流音频路径
	 * @param ffmepegAppBaseDir ffmpeg应用所在目录
	 * @return ffmpeg命令结果
	 *
	 * @author zhengnan
	 */
	public abstract List<String> compCommand(int volumeUpdateNumber,String inputAudioPath,String outputAudioPath,String ffmepegAppBaseDir)throws FfmpegCommandCreateFailureException;
}
