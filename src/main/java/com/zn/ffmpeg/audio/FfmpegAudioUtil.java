/**
 * 
 */
package com.zn.ffmpeg.audio;

import java.io.File;

import com.zn.ffmpeg.exception.FfmpegAppCanNotFindException;
import com.zn.ffmpeg.exception.FfmpegAudioOperateFailureException;
import com.zn.ffmpeg.exception.TypeErrorFfmpegException;

/**
 * ffmpeg音频工具
 * @author zhengnan
 *
 */
public abstract class FfmpegAudioUtil {
	private String ffmpegAppDir; //应用所在系统目录
	
	public void setFfmpegAppDir(String ffmpegAppDir) {
		this.ffmpegAppDir=ffmpegAppDir;
	}
	
	/**
	 * 执行音频处理业务
	 * @return
	 *
	 * @author zhengnan
	 */
	public abstract File processCommand() throws FfmpegAudioOperateFailureException;
	
	/**
	 * 获取不同操作系统下，ffmpeg应用的目录位置
	 * @return
	 * @throws FfmpegAppCanNotFindException
	 *
	 * @author zhengnan
	 */
	String getFfmpegAppDir() throws FfmpegAppCanNotFindException {
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.indexOf("windows")!=-1) {
			
		}else if(osName.indexOf("mac os")!=-1 || osName.indexOf("linux")!=-1) {
			if(ffmpegAppDir==null) {
				ffmpegAppDir="/usr/local/bin/ffmpeg";
			}
		} 
		
		if(ffmpegAppDir==null) {
			throw new FfmpegAppCanNotFindException("未设置ffmpeg应用目录，请先setFfmpegAppDir");
		}
		
		return ffmpegAppDir;
	}
	
	/**
	 * 获取内容类型
	 * @param filePath 文件路径 或 文件名包括扩展名
	 * @return 1=音频
	 *
	 * @author zhengnan
	 * @throws TypeErrorFfmpegException 
	 */
	int checkContentType(String filePath) throws TypeErrorFfmpegException {
		String type = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase();

		if("mp3".equals(type)) {
			return 1;
		}else if("wav".equals(type)) {
			return 1;
		}
		
		throw new TypeErrorFfmpegException("不支持的文件类型");
	}
	
	/**
	 * 获取文件扩展名
	 * @param filePath 文件路径
	 * @return
	 *
	 * @author zhengnan
	 */
	String getExtName(String filePath) {
		return filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length()).toLowerCase();
	}
	
	/**
	 * 检查并删除文件
	 * @param filePath
	 *
	 * @author zhengnan
	 */
	void checkAndDelFile(String filePath) {
		File f = new File(filePath);
		if(f.exists()) {
			f.delete();
		}
	}
}
