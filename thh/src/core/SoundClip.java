package core;

import javax.sound.sampled.*; //javazoom mp3spi�Ǐ�������Ƥ��ޤ�
import javax.swing.*;
import java.io.*;

/**
	����ե�����:wav,au,mp3
*/
public class SoundClip{

	private final Clip clip;
	private final FloatControl control;
	SoundClip(String url){
		Clip clip = null;
		try{
			final AudioInputStream in = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(url));
			clip = AudioSystem.getClip();
			if(url.endsWith(".mp3")){
				final AudioFormat baseFormat = in.getFormat();
				clip.open(AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
																							baseFormat.getSampleRate(),
																							16,
																							baseFormat.getChannels(),
																							baseFormat.getChannels() * 2,
																							baseFormat.getSampleRate(),
																							false),in));
			}else
				clip.open(in);
			in.close();
		}catch(NullPointerException | IOException e){
			JOptionPane.showMessageDialog(null,"�����ե�����\"" + url + "\"��Ҋ�Ĥ���ޤ���Ǥ���������BGM������ޤ���","�i���z�ߥ���`",JOptionPane.WARNING_MESSAGE);
		}catch(UnsupportedAudioFileException e){
			JOptionPane.showMessageDialog(null,"�����ե�����\"" + url + "\"�Υ�ǥ�����ʽ�ˤό���Ǥ��ޤ���","�i���z�ߥ���`",JOptionPane.WARNING_MESSAGE);
		}catch(LineUnavailableException e){
			JOptionPane.showMessageDialog(null,"�����ե�����\"" + url + "\"���_���ޤ���Ǥ������ۤ��Υ��ץꥱ�`�����ʹ���Фο����Ԥ�����ޤ���","�i���z�ߥ���`",JOptionPane.WARNING_MESSAGE);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,e.toString(),"�i���z�ߥ���`",JOptionPane.WARNING_MESSAGE);
		}
		this.clip = clip;
		FloatControl control = null;
		try{
			control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		}catch(Exception e){}
		this.control = control;
	}
	SoundClip(){
		clip = null;
		control = null;
	}
	final void play(){ //����
		if(clip != null)
			clip.start();
	}
	final void pause(){ //һ�rֹͣ
		if(clip != null)
			clip.stop();
	}
	final void stop(){ //ֹͣ
		if(clip != null){
			clip.stop();
			clip.setFramePosition(0);
		}
	}
	final void loop(int count){ //��`������
		if(clip != null)
			clip.loop(count);
	}
	final void loop(){ //�o�ޥ�`������
		if(clip != null)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	final void setVolume(double volume){ //setVolume(0.5);  50%����������������
		if(control != null)
			control.setValue((float)Math.log10(volume) * 20);
	}
	final boolean isRunning(){ //�������Ƥ��뤫
		if(clip != null)
			return clip.isRunning();
		else
			return false;
	}
}