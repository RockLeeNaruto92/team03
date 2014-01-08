/**
 * @author 3B Dang Dinh Dien
 */
package com.example.photosound;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.vn.R;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

public class PictureActivity extends Activity {
	/*
	 * @author: 3B Dang Dinh Dien
	 */
	private ImageView image;
	private ImageButton recordBtn;
	private ImageButton playBtn;
	private ImageButton saveBtn;

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
	 * @author 3A Bui Minh Thu
	 */
	private void getView() {
		recordBtn = (ImageButton) findViewById(R.id.recordBtn);
		playBtn = (ImageButton) findViewById(R.id.playBtn);
		saveBtn = (ImageButton) findViewById(R.id.saveBtn);
		chronometer = (Chronometer) findViewById(R.id.chronometer1);
		seekBar = (SeekBar) findViewById(R.id.SeekBar01);

		image = (ImageView) findViewById(R.id.imageView);
		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int imgWidth = Math.round(displayMetric.widthPixels * 0.9f);
		int imgHeight = Math.round(displayMetric.heightPixels * 0.7f);
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

	}

	/**********************************************************/
	/**** ON BUTTON CLICKED *****/

	/**
	 * @author 3A Bui Minh Thu
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
			recordBtn.setImageResource(R.drawable.stop_recordbtn);
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
			recordBtn.setImageResource(R.drawable.microphone1);
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
	 * @author 3A Bui Minh Thu
	 * @param view
	 */
	public void onPlayBtnClick(View view) {

		AppUtils.logString("Play click");
		if (isPlaying == false) {
			isPlaying = true;
			playBtn.setImageResource(R.drawable.pause01);

			seekBar.setVisibility(View.VISIBLE);
			chronometer.setVisibility(View.GONE);
			recordBtn.setEnabled(false);
			saveBtn.setEnabled(false);

			playSound();
		} else {
			isPlaying = false;
			playBtn.setImageResource(R.drawable.playicon1);
			saveBtn.setEnabled(true);
			recordBtn.setEnabled(true);
		}
	}

	/**
	 * @author 3A Bui Minh Thu
	 */
	public void playSound() {
		Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();

		if (!mp.isPlaying()) {
			if (mp != null) {
				AppUtils.playSong(mp, AppUtils.getTempFile());
			}
		}
	}

}
