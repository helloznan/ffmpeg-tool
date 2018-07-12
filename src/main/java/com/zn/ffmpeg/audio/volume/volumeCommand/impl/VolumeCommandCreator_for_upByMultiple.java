/**
 * 
 */
package com.zn.ffmpeg.audio.volume.volumeCommand.impl;

import java.util.ArrayList;
import java.util.List;

import com.zn.ffmpeg.audio.volume.volumeCommand.VolumeCommandCreator;

/**
 * 以倍数方式音量调大的生成器
 * @author zhengnan
 *
 */
public class VolumeCommandCreator_for_upByMultiple implements VolumeCommandCreator {
	@Override
	public List<String> compCommand(int volumeUpdateNumber,String inputAudioPath,String outputAudioPath,String ffmepegAppBaseDir) {
//		ffmpeg -i /Users/zhengnan/Desktop/c.wav -af 'volume=2' /Users/zhengnan/Desktop/c1.wav

		List<String> command = null;

		command = new ArrayList<String>();
		command.add(ffmepegAppBaseDir);
		command.add("-i");
		command.add(inputAudioPath);
		command.add("-af");
		command.add("volume="+volumeUpdateNumber); 
		command.add(outputAudioPath);

		return command;
	}

}
