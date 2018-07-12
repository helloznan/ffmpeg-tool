import com.zn.ffmpeg.audio.FfmpegAudioTransitFormatUtil;
import com.zn.ffmpeg.audio.FfmpegAudioUtil;
import com.zn.ffmpeg.audio.FfmpegConcatAudioUtil;
import com.zn.ffmpeg.audio.FfmpegMixAudioUtil;
import com.zn.ffmpeg.audio.FfmpegVolumeAudioUtil;
import com.zn.ffmpeg.audio.volume.volumeType.VolumeEnumType;
import com.zn.ffmpeg.exception.FfmpegAudioOperateFailureException;


/**
 * 
 */

/**
 * @author zhengnan
 *
 */
public class Test {
	public static void main(String[] args) {
		testMixAudio();
		testConcatAudio();
		testVolumeUpdateAudio();
		testAudioFormatTransit();
	}
	
	/**
	 * 测试混合音频
	 * 
	 *
	 * @author zhengnan
	 */
	private static void testMixAudio() {
		try {
			FfmpegMixAudioUtil util = new FfmpegMixAudioUtil("/Users/zhengnan/Desktop/voice.mp3","/Users/zhengnan/Desktop/introduce.wav", "/Users/zhengnan/Desktop/mix_1.wav");
			util.setFfmpegAppDir("/usr/local/bin/ffmpeg");
			util.processCommand();
		} catch (FfmpegAudioOperateFailureException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试拼接音频
	 * 
	 *
	 * @author zhengnan
	 */
	private static void testConcatAudio() {
		String[] inputAudioPath = {"/Users/zhengnan/Desktop/introduce.wav","/Users/zhengnan/Desktop/introduce2.wav"};
		FfmpegAudioUtil util = new FfmpegConcatAudioUtil(inputAudioPath, "/Users/zhengnan/Desktop/c_2.wav");
		try {
			util.processCommand();
		} catch (FfmpegAudioOperateFailureException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试音量大小
	 * 
	 *
	 * @author zhengnan
	 */
	private static void testVolumeUpdateAudio() {
		FfmpegAudioUtil util = new FfmpegVolumeAudioUtil(2,"/Users/zhengnan/Desktop/v800_10.wav", "/Users/zhengnan/Desktop/v800_1.wav", VolumeEnumType.down_voice_by_divide);
		try {
			util.processCommand();
		} catch (FfmpegAudioOperateFailureException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试音频格式转换
	 * 
	 *
	 * @author zhengnan
	 */
	private static void testAudioFormatTransit() {
//		FfmpegAudioUtil util = new FfmpegAudioTransitFormatUtil("/Users/zhengnan/Desktop/v800.wav", "/Users/zhengnan/Desktop/v800.mp3");
		FfmpegAudioUtil util = new FfmpegAudioTransitFormatUtil("/Users/zhengnan/Desktop/voice.mp3", "/Users/zhengnan/Desktop/voice1.wav");
		
		try {
			util.processCommand();
		} catch (FfmpegAudioOperateFailureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
