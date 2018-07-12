/**
 * 
 */
package com.zn.ffmpeg.audio.volume.volumeCommand.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.zn.ffmpeg.audio.volume.volumeCommand.VolumeCommandCreator;
import com.zn.ffmpeg.exception.FfmpegCommandCreateFailureException;

/**
 * 以倍数方式音量调小的生成器
 * @author zhengnan
 *
 */
public class VolumeCommandCreator_for_downByDevide implements VolumeCommandCreator {
	@Override
	public List<String> compCommand(int volumeUpdateNumber,String inputAudioPath,String outputAudioPath,String ffmepegAppBaseDir)throws FfmpegCommandCreateFailureException {
//		ffmpeg -i /Users/zhengnan/Desktop/c.wav -af 'volume=0.5' /Users/zhengnan/Desktop/c1.wav

		List<String> command = null;

		try {
			command = new ArrayList<String>();
			command.add(ffmepegAppBaseDir);
			command.add("-i");
			command.add(inputAudioPath);
			command.add("-af");
			
			BigDecimal bigDecimal = new BigDecimal(1.0/volumeUpdateNumber);
			bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP);
			float val = bigDecimal.floatValue();
			
			command.add("volume="+val); 
			command.add(outputAudioPath);
		}catch(Exception e){
			throw new FfmpegCommandCreateFailureException("ffmpeg命令生成失败");
		}
		

		return command;
	}

}
