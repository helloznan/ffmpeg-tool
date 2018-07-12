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
 * 拼接多段音频
 * 
 * @author zhengnan
 *
 */
public class FfmpegConcatAudioUtil extends FfmpegAudioUtil {
	private String[] audioPaths; // 待拼接的音频路径，按顺序拼接
	private String outputAudioPath; // 输出音频路径

	public FfmpegConcatAudioUtil(String[] audioPaths, String outputAudioPath) {
		this.audioPaths = audioPaths;
		this.outputAudioPath = outputAudioPath;
	}

	@Override
	public File processCommand() throws FfmpegAudioOperateFailureException {
		// ffmpeg -i /Users/zhengnan/Desktop/introduce.wav -i
		// /Users/zhengnan/Desktop/introduce.wav -i
		// /Users/zhengnan/Desktop/introduce.wav -i
		// /Users/zhengnan/Desktop/introduce.wav -i
		// /Users/zhengnan/Desktop/introduce.wav
		// -filter_complex 'concat=n=5:v=0:a=1 [a1]' -map '[a1]'
		// /Users/zhengnan/Desktop/c.wav

		if (audioPaths == null || audioPaths.length <= 1) {
			throw new FfmpegAudioOperateFailureException("音频源数量必须大于等于2");
		}

		List<String> command = null;

		try {
			// 检查输入流和输出流是否是音频
			for (String path : audioPaths) {
				int type = checkContentType(path);
				if (type != 1) {
					throw new TypeErrorFfmpegException("不支持的音频格式");
				}
			}
			int type = checkContentType(outputAudioPath);
			if (type != 1) {
				throw new TypeErrorFfmpegException("不支持的音频格式");
			}

			command = new ArrayList<String>();
			command.add(getFfmpegAppDir());

			for (int i = 0; i < audioPaths.length; i++) {
				command.add("-i");
				command.add(audioPaths[i]);
			}

			command.add("-filter_complex");
			command.add("concat=n=" + audioPaths.length + ":v=0:a=1 [a1]"); // n=输入流个数，v=输出视频数，a=输出音频数,[a1]=输出音频的别名
			command.add("-map");
			command.add("[a1]");
			command.add(outputAudioPath);
		} catch (TypeErrorFfmpegException e) {
			throw new FfmpegAudioOperateFailureException(e);
		} catch (FfmpegAppCanNotFindException e) {
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
			   System.out.println("音频拼接命令执行出错：");
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
			
            
            return new File(outputAudioPath);
		} catch (IOException e) {
			throw new FfmpegAudioOperateFailureException(e);
		} catch(InterruptedException e) {
			throw new FfmpegAudioOperateFailureException(e);
		}
	}

}
