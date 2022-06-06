/**
 * @author 3B Dang Dinh Dien
 */
package com.example.photosound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.vn.R;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;

public class CopyOfPictureActivity extends Activity {
	/*
	 * @author: 3B Dang Dinh Dien
	 */
	private ImageView image;
	private Button recordBtn;
	private Button playBtn;
	private Button saveBtn;
	private Button chooseSoundBtn;
	private final Handler handler = new Handler();
	private Chronometer chronometer;
	private SeekBar seekBar;
	private MediaPlayer mp;
	private MediaRecorder recorder;

	private String imagePath;
	private String soundPath;
	private boolean isRecording = false;
	private boolean isPlaying = false;
	private boolean hadSound = false;
	private boolean isFirstPlayClick = true;
	private long position;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_picture);
		AppUtils.logString("set content view");
		getView();
		AppUtils.logString("getView");
		getImagePath();

		setBitmapToImageView(image, imagePath);
		
		AppUtils.logString("imgPath: " + imagePath);

//		checkHasSound(imagePath);
//		if (hadSound) {
//			playBtn.setEnabled(true);
//			playBtn.setBackgroundResource(R.drawable.playbtn3);
//		}

		/*
		 * File file = new File(imagePath); if (file.exists()) Log.d("APP",
		 * "file exist"); else Log.d("App", "!file exist");
		 */
	}

	private void checkHasSound(String imagePath) {
		// TODO Auto-generated method stub
		try {
			RandomAccessFile file = new RandomAccessFile(imagePath, "r");
			
			position = AppUtils.getPositionOfStringInFile(
					AppConst.SEPERATOR_OF_IMG_AND_SOUND, file); 
			if ( position != -1) {
				AppUtils.logString("exists sound in image");
				hadSound = true;
			} else {
				AppUtils.logString("not exist sound");
				hadSound = false;
			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (recorder != null) {
			if (isPlaying)
				recorder.stop();
			recorder.release();
			recorder = null;
		}

		if (mp != null) {
			if (mp.isPlaying())
				mp.stop();
			mp.release();
			mp = null;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mp == null) {
			mp = new MediaPlayer();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AppConst.CHOOSE_SOUND_REQUEST) {
			if (resultCode == RESULT_OK) {
				soundPath = data.getExtras().getString("soundPath");
				AppUtils.logString("sound Path: " + soundPath);
				setEnableButton(true, true, true, true);

				if (!hadSound)
					playBtn.setBackgroundResource(R.drawable.playbtn3);
				saveBtn.setBackgroundResource(R.drawable.save2);

				isPlaying = false;
			}
		}
	}

	/**
	 * @author 3A Bui Minh Thu
	 */
	private void getImagePath() {
		// TODO Auto-generated method stub
		// imagePath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/vd.jpg";
		AppUtils.logString("getImagePath");
		Intent iin = getIntent();
		Bundle b = iin.getExtras();

		if (b != null) {
			AppUtils.logString("intent not null");
			imagePath = (String) b.get("BitmapImage");
			AppUtils.logString("Image: "  + imagePath);
		}else AppUtils.logString("intent null");
	}

	/**
	 * @author 3A Bui Minh Thu
	 * @param imagePath
	 */
	private void setBitmapToImageView(ImageView image, String imagePath) {
		// TODO Auto-generated method stub
		Bitmap bitmap;
		bitmap = BitmapFactory.decodeFile(imagePath);
		image.setImageBitmap(bitmap);
	}

	/**
	 * @author 3B Dang Dinh Dien
	 */
	private void getView() {
		recordBtn = (Button) findViewById(R.id.recordBtn);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		chooseSoundBtn = (Button) findViewById(R.id.chooseSoundBtn);
		chronometer = (Chronometer) findViewById(R.id.chronometer1);
		seekBar = (SeekBar) findViewById(R.id.SeekBar01);
		AppUtils.logString("seekbar");
		
		image = (ImageView) findViewById(R.id.imageView);
		AppUtils.logString("image");
		
		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int imgWidth = Math.round(displayMetric.widthPixels * 0.9f);
		int imgHeight = Math.round(displayMetric.heightPixels * 0.5f);
		
		// Layou param = (LayoutParams)imageView.getLayoutParams();
		android.widget.RelativeLayout.LayoutParams param = (android.widget.RelativeLayout.LayoutParams) image
				.getLayoutParams();
		param.width = imgWidth;
		param.height = imgHeight;
		AppUtils.logString("set param");
		image.setLayoutParams(param);
		AppUtils.logString("setLauout");
		setOnButtonClick();
		AppUtils.logString("buttonclick");
	}

	/**
	 * @author 3A Bui Minh Thu
	 */
	private void setOnButtonClick() {
		AppUtils.logString("on button click");
		recordBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onRecordBtnClick(recordBtn);
			}
		});
		
		AppUtils.logString("record btn");

		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPlayBtnClick(playBtn);
			}
		});
		
		AppUtils.logString("play btn");

		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSaveBtnClick(saveBtn);
			}
		});
		AppUtils.logString("save btn");

		chooseSoundBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onChooseSoundBtnClick(chooseSoundBtn);
			}
		});
		
		AppUtils.logString("choose btn");
	}

	/**********************************************************/
	/**** ON BUTTON CLICKED *****/
	public void onChooseSoundBtnClick(Button chooseSoundBtn) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
		startActivityForResult(i, AppConst.CHOOSE_SOUND_REQUEST);
	}

	/**
	 * @author 3A Bui Minh Thu
	 * @author 3B Dang Dinh Dien
	 * @param view
	 */
	public void onRecordBtnClick(View view) {
		if (!isRecording) {
			AppUtils.logString("start record");
			AppUtils.deleteTempFile();

			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			AppUtils.logString("tempFile: " + AppUtils.getTempFile());
			soundPath = AppUtils.getTempFile();
			recorder.setOutputFile(soundPath);

			// if (playBtn.getVisibility() == View.VISIBLE) {
			seekBar.setVisibility(View.GONE);
			setEnableButton(true, false, false, false);
			playBtn.setBackgroundResource(R.drawable.playbtn3_disable);
			chooseSoundBtn.setBackgroundResource(R.drawable.folder_sound2_disable);
			saveBtn.setBackgroundResource(R.drawable.save_disable);
			// }
			recordBtn.setBackgroundResource(R.drawable.stopbtn);
			chronometer.setVisibility(View.VISIBLE);
			chronometer.setBase(SystemClock.elapsedRealtime());
			chronometer.start();

			try {
				recorder.prepare();
				recorder.start();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				AppUtils.logString("illegal state exception");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				AppUtils.logString("IO exception");
				e.printStackTrace();
			}

		} else {
			recordBtn.setBackgroundResource(R.drawable.microphone1);
			setEnableButton(true, true, true, true);
			playBtn.setBackgroundResource(R.drawable.playbtn3);
			chooseSoundBtn.setBackgroundResource(R.drawable.folder_sound2);
			saveBtn.setBackgroundResource(R.drawable.save2);
			
			chronometer.stop();
			// Stop and free recorder
			recorder.stop();
			recorder.release();
			recorder = null;

		}
		isRecording = !isRecording;
	}

	/**
	 * @author buiminthuk55
	 * @param view
	 */
	public void onSaveBtnClick(View view) {
		AppUtils.logString("Save click");
		String filename = AppUtils.getDefaultFileName();
		// Copy file to photosound folder
		AppUtils.copyFile(imagePath, filename);

		// write temp mp3 sound data to end of image
		AppUtils.writeMp3ToEndOfImage(filename, AppUtils.getTempFile());
		AppUtils.logString("filename: " + filename);
		// delete temp mp3 file
		AppUtils.deleteTempFile();
		AppUtils.logString("deleted mp3 tempFile");

		Intent i = new Intent(getApplicationContext(), MarkedActivity.class);
		startActivity(i);
		finish();
	}

	/**
	 * @author 3B Dang Dinh Dien
	 * @param view
	 */
	public void onPlayBtnClick(View view) {

		AppUtils.logString("Play click " + isPlaying);
		if (isPlaying == false) {
			isPlaying = true;
			playBtn.setBackgroundResource(R.drawable.pausebtn);
			if (seekBar.getVisibility() == View.GONE) {
				seekBar.setVisibility(View.VISIBLE);

				File file = new File(soundPath);
				Uri uri = Uri.fromFile(file);

				mp = MediaPlayer.create(this, uri);

				AppUtils.logString("file sound path: " + soundPath);
				seekBar.setMax(mp.getDuration());
				seekBar.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						seekChange(v);
						return false;
					}
				});
			}

			setEnableButton(false, true, false, false);
			
			
			AppUtils.playSong(mp, soundPath);
			startPlayProgressUpdater();

		} else {
			isPlaying = false;
			playBtn.setBackgroundResource(R.drawable.playbtn3);
			recordBtn.setBackgroundResource(R.drawable.microphone1);
			chooseSoundBtn.setBackgroundResource(R.drawable.folder_sound2);
			saveBtn.setBackgroundResource(R.drawable.save2);
			setEnableButton(true, true, true, true);
			mp.pause();
		}
	}

	/**
	 * 
	 * @param recordStatus
	 * @param playStatus
	 * @param chooseStatus
	 * @param saveStatus
	 */
	private void setEnableButton(boolean recordStatus, boolean playStatus,
			boolean chooseStatus, boolean saveStatus) {
		recordBtn.setEnabled(recordStatus);
		playBtn.setEnabled(playStatus);
		chooseSoundBtn.setEnabled(chooseStatus);
		saveBtn.setEnabled(saveStatus);
	}

	/**
	 * @author 3B Dang Dinh Dien
	 */
	public void startPlayProgressUpdater() {
		seekBar.setProgress(mp.getCurrentPosition());

		if (mp.isPlaying()) {
			Runnable notification = new Runnable() {
				public void run() {
					startPlayProgressUpdater();
				}
			};
			handler.postDelayed(notification, 1000);
		} else {
			mp.stop();
			// mp.release();
			// mp = null;
			playBtn.setBackgroundResource(R.drawable.playbtn3);
			seekBar.setProgress(0);
			setEnableButton(true, true, true, true);
			playBtn.setBackgroundResource(R.drawable.playbtn3);
			recordBtn.setBackgroundResource(R.drawable.microphone1);
			chooseSoundBtn.setBackgroundResource(R.drawable.folder_sound2);
			saveBtn.setBackgroundResource(R.drawable.save2);
		}
	}

	/**
	 * @author 3B Dang Dinh Dien
	 */
	// This is event handler thumb moving event
	private void seekChange(View v) {
		if (mp.isPlaying()) {
			SeekBar sb = (SeekBar) v;
			mp.seekTo(sb.getProgress());
		}
	}
}
