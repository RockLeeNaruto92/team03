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
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.vn.R;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class PictureActivity extends Activity {
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
	private boolean isRecording = false;
	private boolean isPlaying = false;
	private boolean fromGallery;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_picture);

		getView();
		getImagePath();

		setBitmapToImageView(image, imagePath);

		/*
		 * File file = new File(imagePath); if (file.exists()) Log.d("APP",
		 * "file exist"); else Log.d("App", "!file exist");
		 */
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

	/**
	 * @author 3A Bui Minh Thu
	 */
	private void getImagePath() {
		// TODO Auto-generated method stub
		// imagePath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/vd.jpg";
		Intent iin = getIntent();
		Bundle b = iin.getExtras();
		if (b != null) {
			imagePath = (String) b.get("BitmapImage");
			AppUtils.logString("Image: " + imagePath);
			fromGallery = b.getBoolean("fromGallery");
		}

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
		playBtn = (Button) findViewById(R.id.playBtn);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		chooseSoundBtn = (Button) findViewById(R.id.chooseSoundBtn);
		chronometer = (Chronometer) findViewById(R.id.chronometer1);
		seekBar = (SeekBar) findViewById(R.id.SeekBar01);

		image = (ImageView) findViewById(R.id.imageView);
		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int imgWidth = Math.round(displayMetric.widthPixels * 0.9f);
		int imgHeight = Math.round(displayMetric.heightPixels * 0.5f);
		// Layou param = (LayoutParams)imageView.getLayoutParams();
		android.widget.RelativeLayout.LayoutParams param = (android.widget.RelativeLayout.LayoutParams) image
				.getLayoutParams();
		param.width = imgWidth;
		param.height = imgHeight;
		image.setLayoutParams(param);
		
		setOnButtonClick();
	}

	/**
	 * @author 3A Bui Minh Thu
	 */
	private void setOnButtonClick() {
		recordBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onRecordBtnClick(recordBtn);
			}
		});

		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPlayBtnClick(playBtn);
			}
		});

		saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSaveBtnClick(saveBtn);
			}
		});
		
		chooseSoundBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onChooseSoundBtnClick(chooseSoundBtn);
			}
		});

	}

	/**********************************************************/
	/**** ON BUTTON CLICKED *****/
	public void onChooseSoundBtnClick(Button chooseSoundBtn) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
		startActivity(i);
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
			recorder.setOutputFile(AppUtils.getTempFile());

			if (playBtn.getVisibility() == View.VISIBLE) {
				seekBar.setVisibility(View.GONE);
				playBtn.setVisibility(View.GONE);
				saveBtn.setVisibility(View.GONE);

			}
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
			playBtn.setVisibility(View.VISIBLE);
			saveBtn.setVisibility(View.VISIBLE);
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
	 * @author 3A Bui Minh Thu
	 * @param view
	 */
	public void onCancelBtnClick(View view) {
		AppUtils.logString("Cancel click");
		// only delete temp file
		AppUtils.deleteTempFile();
	}

	/**
	 * @author 3B Dang Dinh Dien
	 * @param view
	 */
	public void onPlayBtnClick(View view) {

		AppUtils.logString("Play click");
		if (isPlaying == false) {
			isPlaying = true;
			playBtn.setBackgroundResource(R.drawable.pausebtn);
			if (seekBar.getVisibility() == View.GONE) {
				seekBar.setVisibility(View.VISIBLE);
				File file=new File(AppUtils.getTempFile());
				Uri uri = Uri.fromFile(file);
				mp = MediaPlayer.create(this, uri);
				AppUtils.playSong(mp, AppUtils.getTempFile());
				seekBar.setMax(mp.getDuration());
				seekBar.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						seekChange(v);
						return false;
					}
				});
			}
			chronometer.setVisibility(View.GONE);
			recordBtn.setVisibility(View.GONE);
			saveBtn.setVisibility(View.GONE);
			Log.d("maxsecond", Integer.toString(mp.getDuration()));
			try {
				mp.seekTo(seekBar.getProgress());
				mp.start();
				startPlayProgressUpdater();
			} catch (IllegalStateException e) {
				mp.pause();
			}
		} else {
			isPlaying = false;
			playBtn.setBackgroundResource(R.drawable.playbtn3);
			saveBtn.setVisibility(View.VISIBLE);
			recordBtn.setVisibility(View.VISIBLE);
			mp.pause();
		}
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
			mp.pause();
			playBtn.setBackgroundResource(R.drawable.playbtn3);
			seekBar.setProgress(0);
			saveBtn.setVisibility(View.VISIBLE);
			recordBtn.setVisibility(View.VISIBLE);
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

	/**
	 * @author 3A Bui Minh Thu
	 */
	public void playSound() {
		Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();

		if (!mp.isPlaying()) {
			if (mp != null) {
				
				//File file=new File(AppUtils.getTempFile());
				//Uri uri = Uri.fromFile(file);
				//mp = MediaPlayer.create(this, uri);
				AppUtils.playSong(mp, AppUtils.getTempFile());
			}
		}
	}

}
