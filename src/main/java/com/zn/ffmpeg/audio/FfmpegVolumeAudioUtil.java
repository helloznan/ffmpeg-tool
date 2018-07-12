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

import com.zn.ffmpeg.audio.volume.volumeCommand.VolumeCommandCreator;
import com.zn.ffmpeg.audio.volume.volumeCommand.impl.VolumeCommandCreator_for_downByDb;
import com.zn.ffmpeg.audio.volume.volumeCommand.impl.VolumeCommandCreator_for_downByDevide;
import com.zn.ffmpeg.audio.volume.volumeCommand.impl.VolumeCommandCreator_for_upByDb;
import com.zn.ffmpeg.audio.volume.volumeCommand.impl.VolumeCommandCreator_for_upByMultiple;
import com.zn.ffmpeg.audio.volume.volumeType.VolumeEnumType;
import com.zn.ffmpeg.exception.FfmpegAppCanNotFindException;
import com.zn.ffmpeg.exception.FfmpegAudioOperateFailureException;
import com.zn.ffmpeg.exception.FfmpegCommandCreateFailureException;
import com.zn.ffmpeg.exception.TypeErrorFfmpegException;

/**
 * 音频声音调节
 * 
 * @author zhengnan
 *
 */
public class FfmpegVolumeAudioUtil extends FfmpegAudioUtil {
	private String inputAudioPath; //待处理音频路径
	private String outputAudioPath; // 输出音频路径
	private Integer volumeUpdateNumber; //音量修改大小（分贝值：如3=增加或减小3分贝，翻倍值：如2=音量增强1倍，缩倍值：如2=音量减小1半）
	
	private VolumeCommandCreator volumeCommandCreator=null; //命令生成器
	

	/**
	 * 
	 * @param inputAudioPath 音频输入流
	 * @param outputAudioPath 音频输出流
	 * @param volumeEnumType 声音调节方式
	 */
	public FfmpegVolumeAudioUtil(Integer volumeUpdateNumber,String inputAudioPath, String outputAudioPath,VolumeEnumType volumeEnumType) {
		this.inputAudioPath = inputAudioPath;
		this.outputAudioPath = outputAudioPath;
		this.volumeUpdateNumber = volumeUpdateNumber;
		
		switch(volumeEnumType) {
			case up_voice_by_db: 
				volumeCommandCreator = new VolumeCommandCreator_for_upByDb();
				break;
			case down_voice_by_db: 
				volumeCommandCreator = new VolumeCommandCreator_for_downByDb();
				break;
			case up_voice_by_multiple : 
				volumeCommandCreator = new VolumeCommandCreator_for_upByMultiple();
				break;
			case down_voice_by_divide : 
				volumeCommandCreator = new VolumeCommandCreator_for_downByDevide();
				break;
		}
			
			
	}

	@Override
	public File processCommand() throws FfmpegAudioOperateFailureException {
		List<String> command = null;

		try {
			// 检查输入流和输出流是否是音频
			int type = checkContentType(inputAudioPath);
			if (type != 1) {
				throw new TypeErrorFfmpegException("不支持的音频格式");
			}
			
			type = checkContentType(outputAudioPath);
			if (type != 1) {
				throw new TypeErrorFfmpegException("不支持的音频格式");
			}

			if(volumeCommandCreator==null) {
				throw new FfmpegAudioOperateFailureException("音量调整命令生成失败");
			}
			
			command = volumeCommandCreator.compCommand(volumeUpdateNumber, inputAudioPath, outputAudioPath, getFfmpegAppDir());
			
		} catch (TypeErrorFfmpegException e) {
			throw new FfmpegAudioOperateFailureException(e);
		} catch (FfmpegAppCanNotFindException e) {
			throw new FfmpegAudioOperateFailureException(e);
		} catch (FfmpegCommandCreateFailureException e) {
			throw new FfmpegAudioOperateFailureException(e);
		}

		// 执行操作
		Process process = null;

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			processBuilder.redirectErrorStream(true);
			processBuilder.command(command);
			
			checkAndDelFile(outputAudioPath);
			
			process = processBuilder.start();

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
			   System.out.println("音频音量调整命令执行出错：");
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
			
			System.out.println("音量调整成功");
			
            
            return new File(outputAudioPath);
		} catch (IOException e) {
			throw new FfmpegAudioOperateFailureException(e);
		} catch(InterruptedException e) {
			throw new FfmpegAudioOperateFailureException(e);
		}
	}

}
