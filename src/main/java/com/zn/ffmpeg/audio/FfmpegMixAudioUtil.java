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
 * 音频混合背景音乐
 * @author zhengnan
 *
 */
public class FfmpegMixAudioUtil extends FfmpegAudioUtil{
	private String frontAudioPath; //前置音频文件路径
	private String backgroundAudioPath; //背景音频文件路径
	private String outAudioPath; //输出音频文件
	
	public FfmpegMixAudioUtil(String frontAudioPath,String backgroundAudioPath,String outAudioPath) {
		this.frontAudioPath = frontAudioPath;
		this.backgroundAudioPath = backgroundAudioPath;
		this.outAudioPath = outAudioPath;
		
	}
	
	/**
	 * 混合前置音频、背景音乐，并输出到目标目录文件，
	 * 音频时长以前置音频为准。背景音乐可以用comcat工具类实现
	 */
	@Override
	public File processCommand() throws FfmpegAudioOperateFailureException {
		//检查内容是否是音频类型
		List<String> command = null;
		try {
			int frontAudioType=checkContentType(frontAudioPath);
			int backgroundAudioType = checkContentType(backgroundAudioPath);
			String outFileExtName= getExtName(outAudioPath);
			
			if(frontAudioType!=1 || backgroundAudioType!=1) {
				throw new TypeErrorFfmpegException("不支持的音频格式");
			}
			
			command = new ArrayList<String>();
			
			command.add(getFfmpegAppDir());
			command.add("-loglevel");
			command.add("quiet");
			command.add("-i");
			command.add(frontAudioPath);
			command.add("-i");
			command.add(backgroundAudioPath);
			command.add("-filter_complex");
			command.add("amix=inputs=2:duration=first:dropout_transition=2");
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
            
			InputStream errorStream = process.getErrorStream();
			InputStream inputStream = process.getInputStream();

//			// 启动线程读取标准错误输出流信息
//			Thread t1 = new Thread() {
//				public void run() {
//					BufferedReader br1 = new BufferedReader(new InputStreamReader(errorStream));
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
//			t1.start();
//
//			// 启动线程读取标准错误输出流信息
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
			   System.out.println("音频混合命令执行出错：");
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
//			t1.interrupt();
//			t2.interrupt();
			
			
            
            return new File(outAudioPath);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		
		throw new FfmpegAudioOperateFailureException("ffmpeg执行失败");
	}
	
}
