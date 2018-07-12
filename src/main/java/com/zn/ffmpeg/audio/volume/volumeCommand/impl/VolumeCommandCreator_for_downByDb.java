/**
 * 
 */
package com.zn.ffmpeg.audio.volume.volumeCommand.impl;

import java.util.ArrayList;
import java.util.List;

import com.zn.ffmpeg.audio.volume.volumeCommand.VolumeCommandCreator;

/**
 * 以分贝方式音量调小的生成器
 * @author zhengnan
 *
 */
public class VolumeCommandCreator_for_downByDb implements VolumeCommandCreator {
	@Override
	public List<String> compCommand(int volumeUpdateNumber,String inputAudioPath,String outputAudioPath,String ffmepegAppBaseDir) {
//		ffmpeg -i /Users/zhengnan/Desktop/c.wav -af volume=-6dB /Users/zhengnan/Desktop/v800v1.wav

		List<String> command = null;

		command = new ArrayList<String>();
		command.add(ffmepegAppBaseDir);
		command.add("-i");
		command.add(inputAudioPath);
		command.add("-af");
		command.add("volume=-"+volumeUpdateNumber+"dB"); 
		command.add(outputAudioPath);

		return command;
	}

}
