/**
 * @author buiminhthuk55
 */

package com.example.photosound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

public class AppUtils {
	/**
	 * 
	 * @param msg
	 */
	public static void logString(String msg){
		Log.d(AppConst.TAG, msg);
	}
	
	/**
	 * @author buiminhthuk55
	 * @return
	 */
	public static String getTempFile() {
		String str = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		return str + "/" + AppConst.MP3_TEMP_FILE;
	}

	/**
	 * @author buiminhthuk55
	 */
	public static void deleteTempFile() {
		String filePath = getTempFile();
		File file = new File(filePath);

		if (file.exists()) {
			file.delete();
		}
	}
	
	/**
	 * 
	 * @param mp
	 * @param filePath
	 * @author buiminhthuk55
	 */
	public static void playSong(MediaPlayer mp, String filePath) {
		AppUtils.logString("playSong: " + filePath);
		try {
			mp.reset();
			mp.setDataSource(filePath);
			mp.prepare();
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			AppUtils.logString("illegal argument exeption" + filePath);
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			AppUtils.logString("security exeption" + filePath);
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			AppUtils.logString("illegal state exeption" + filePath);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppUtils.logString("IO exeption" + filePath);
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param str
	 * @param array
	 * @return
	 * @author buiminhthuk55
	 */
	public static boolean compareBetweenStringAndByteArray(String str,
			byte[] array) {
		byte[] strByte = new byte[str.length()];

		strByte = str.getBytes();
		for (int i = 0; i < array.length; i++)
			if (array[i] != strByte[i])
				return false;

		return true;
	}
	
	/**
	 * 
	 * @param str
	 * @param file
	 * @return
	 * @throws IOException
	 * @author buiminhthuk55
	 */

	public static long getPositionOfStringInFile(String str,
			RandomAccessFile file) throws IOException {
		long position = file.length()
				- AppConst.SEPERATOR_OF_IMG_AND_SOUND.length();
		byte[] data = new byte[AppConst.SEPERATOR_OF_IMG_AND_SOUND.length()];
		
		while (position >= 0) {
			file.seek(position);
			if ((char)file.readByte() == str.charAt(0)) {
				file.seek(position);
				file.read(data, 0, AppConst.SEPERATOR_OF_IMG_AND_SOUND.length());
				if (AppUtils.compareBetweenStringAndByteArray(
						AppConst.SEPERATOR_OF_IMG_AND_SOUND, data))
					return position;
			}
			position--;
		}

		return position;
	}
	
	/**
	 * 
	 * @param imgFile
	 * @param position
	 * @throws IOException
	 * @author buiminhthuk55
	 */
	public static void createMp3TempFileFromImgFile(RandomAccessFile imgFile, long position) throws IOException{
		File file = new File(AppUtils.getTempFile());
		if (file.exists()) file.delete();
		
		RandomAccessFile tempFile = new RandomAccessFile(AppUtils.getTempFile(), "rw");
		byte[] data = new byte[AppConst.DATA_LENGTH];
		int byteRead;
		
		imgFile.seek(position);
		while ((byteRead = imgFile.read(data)) != -1){
			tempFile.write(data, 0, byteRead);
		}
		
		tempFile.close();
	}
	
	/**
	 * 
	 * @param imgPath
	 * @param mp3Path
	 * @author buiminhthuk55
	 */
	public static void writeMp3ToEndOfImage(String imgPath, String mp3Path) {
		RandomAccessFile imgFile, mp3File;

		byte[] data = new byte[4096];
		int rbyte;
		long byteread = 0;

		try {
			imgFile = new RandomAccessFile(imgPath, "rw");
			mp3File = new RandomAccessFile(mp3Path, "r");

			imgFile.seek(imgFile.length() - 1);

			imgFile.writeBytes(AppConst.SEPERATOR_OF_IMG_AND_SOUND);

			while ((rbyte = mp3File.read(data)) != -1) {
				byteread += rbyte;
				imgFile.write(data, 0, rbyte);
			}

			imgFile.close();
			Log.d(AppConst.TAG, "imgFile .close()");
			mp3File.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.d(AppConst.TAG, "add mp3 to jpg complete");
	}

	public static void createImageFolderIfNotExist(){
		String rootPath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(rootPath, AppConst.IMAGE_FOLDER);

		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
