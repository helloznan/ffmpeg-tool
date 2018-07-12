/**
 * 
 */
package com.zn.ffmpeg.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.zn.ffmpeg.exception.FfmpegAppCanNotFindException;
import com.zn.ffmpeg.exception.FfmpegAudioOperateFailureException;
import com.zn.ffmpeg.exception.TypeErrorFfmpegException;

/**
 * 音频格式转换
 * @author zhengnan
 *
 */
public class FfmpegAudioTransitFormatUtil extends FfmpegAudioUtil{
	private String inputAudioPath; //音频文件路径
	private String outAudioPath; //输出音频文件
	
	public FfmpegAudioTransitFormatUtil(String inputAudioPath,String outAudioPath) {
		this.inputAudioPath = inputAudioPath;
		this.outAudioPath = outAudioPath;
		
	}
	
	/**
	 * 混合前置音频、背景音乐，并输出到目标目录文件，
	 * 音频时长以前置音频为准。背景音乐可以用comcat工具类实现
	 */
	@Override
	public File processCommand() throws FfmpegAudioOperateFailureException {
		//ffmpeg -i /Users/zhengnan/Desktop/v800.mp3 -f wav /Users/zhengnan/Desktop/v800.wav
		//ffmpeg -i /Users/zhengnan/Desktop/voice.wav -acodec mp3 -f mp3 /Users/zhengnan/Desktop/voice1.mp3
		//检查内容是否是音频类型
		List<String> command = null;
		try {
			int inputAudioType=checkContentType(inputAudioPath);
			int outputAudioType = checkContentType(outAudioPath);
			String outFileExtName= getExtName(outAudioPath);
			
			if(inputAudioType!=1 || outputAudioType!=1) {
				throw new TypeErrorFfmpegException("不支持的音频格式");
			}
			
			command = new ArrayList<String>();
			
			command.add(getFfmpegAppDir());
			command.add("-i");
			command.add(inputAudioPath);
			
			if("mp3".equals(outFileExtName)) {
				command.add("-acodec");
				command.add("mp3");
			}
			
			command.add("-f");
			command.add(outFileExtName);
			command.add(outAudioPath);
		}catch(TypeErrorFfmpegException e) {
			throw new FfmpegAudioOperateFailureException(e);
		} catch (FfmpegAppCanNotFindException e) {
			throw new FfmpegAudioOperateFailureException(e);
		}
		
		
		
		
		Process process=null;
		try {  
			ProcessBuilder builder = new ProcessBuilder(); 
			builder.redirectErrorStream(true);
            builder.command(command);  
            
            checkAndDelFile(outAudioPath);
            
            process = builder.start();
            
			InputStream inputStream = process.getInputStream();

//			// 启动线程读取标准输出流信息
//			Thread t2 = new Thread() {
//				public void run() {
//					BufferedReader br1 = new BufferedReader(new InputStreamReader(inputStream));
//					try {
//						String line1 = null;
//						while ((line1 = br1.readLine()) != null) {
//							if (line1 != null) {
//								System.out.println(line1);
//							}
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					} finally {
//						try {
//							errorStream.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			};
//			t2.start();
            
            //等待进程执行完毕,只有进程结束，才会继续往下执行
			if(process.waitFor() != 0){ //如果进程运行结果不为0,表示进程是错误退出的
			   //获得进程实例的错误输出
			   InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
			   BufferedReader breader = new BufferedReader(reader);
			   String line = null;
			   System.out.println("音频格式转换执行出错：");
			   while((line = breader.readLine())!=null) {
				   System.out.println(line);
			   }
			   
			   try {
				   breader.close();
			   }catch(IOException e) {}
			   
			}else {
				System.out.println("ffmpeg执行成功");
			}
			
			process.destroy();
//			t2.interrupt();
			
            
            return new File(outAudioPath);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		
		throw new FfmpegAudioOperateFailureException("ffmpeg执行失败");
	}
	
}
