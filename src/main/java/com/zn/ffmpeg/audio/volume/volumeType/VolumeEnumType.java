/**
 * 
 */
package com.zn.ffmpeg.audio.volume.volumeType;

/**
 * 音量调节类型
 * @author zhengnan
 *
 */
public enum VolumeEnumType {
	/**
	 * 以分贝的方式提高音量
	 */
	up_voice_by_db, 
	
	/**
	 * 以分贝的方式降低音量
	 */
	down_voice_by_db,
	
	/**
	 * 以翻倍的方式提高音量
	 */
	up_voice_by_multiple,
	
	/**
	 * 以分数的方式降低音量
	 */
	down_voice_by_divide;
}
