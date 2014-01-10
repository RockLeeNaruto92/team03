/**
 * @author 3B Dang Dinh Dien
 */
package com.example.photosound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
	private MediaPlayer mp = null;
	private MediaRecorder recorder = null;

	private String imagePath;
	private String soundPath;
	private boolean isRecording = false;
	private boolean isPlaying = false;
	private boolean hadSound;
	private int processValue;
	private int count;

	private long position;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_picture);

		getView();

		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			return;
		}

		boolean fromGallery = bundle.getBoolean("fromGallery", false);
		boolean fromCamera = bundle.getBoolean("fromCamera", false);
		Options option = new Options();
		option.inSampleSize = 4;

		imagePath = bundle.getString("imgPath");
		AppUtils.logString("imgPath: " + imagePath);
		if (fromGallery) {
			Uri imgUri = getIntent().getData();
			try {

				InputStream is = getContentResolver().openInputStream(imgUri);
				Bitmap bmp = BitmapFactory.decodeStream(is, null, option);
				is.close();
				if (bmp != null) {
					image.setImageBitmap(bmp);
				}
			} catch (Exception e) {
				Log.e("Load Image", "" + e.getMessage());
				return;
			}

		} else {
			Bitmap bmp = BitmapFactory.decodeFile(imagePath, option);
			if (bmp != null) {
				image.setImageBitmap(bmp);
			}
		}

		// getView();
		// getImagePath();

		// setBitmapToImageView(image, imagePath);
		if (fromCamera)
			return;

		RandomAccessFile file;
		try {
			file = new RandomAccessFile(imagePath, "rw");
			position = AppUtils.getPositionOfStringInFile(
					AppConst.SEPERATOR_OF_IMG_AND_SOUND, file);

			if (position == -1) {
				hadSound = false;
			} else {
				hadSound = true;
				playBtn.setEnabled(true);
				playBtn.setBackgroundResource(R.drawable.playbtn3);

				AppUtils.createMp3TempFileFromImgFile(file, position
						+ AppConst.SEPERATOR_OF_IMG_AND_SOUND.length());

				soundPath = AppUtils.getFilePath(AppConst.MP3_TEMP_FILE);

			}
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		}

	}

	/**
	 * @author 3A Bui Minh Thu
	 * @param imagePath
	 */
	private void setBitmapToImageView(ImageView image, String imagePath) {
		// TODO Auto-generated method stub
		Bitmap bitmap;
		Options option = new Options();
		option.inSampleSize = 2;
		bitmap = BitmapFactory.decodeFile(imagePath, option);

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
		int imgWidth = Math.round(displayMetric.widthPixels * 1.0f);
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
			soundPath = AppUtils.getTempFile();
			recorder.setOutputFile(soundPath);

			if (chronometer.getVisibility() != View.VISIBLE) {
				seekBar.setVisibility(View.GONE);
				chronometer.setVisibility(View.VISIBLE);
			}

			playBtn.setBackgroundResource(R.drawable.play_button_disable1);
			playBtn.setEnabled(false);

			saveBtn.setBackgroundResource(R.drawable.save_disable);
			saveBtn.setEnabled(false);

			chooseSoundBtn
					.setBackgroundResource(R.drawable.choose_from_folder_disable1);
			chooseSoundBtn.setEnabled(false);

			recordBtn.setBackgroundResource(R.drawable.stop_button2);
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
			recordBtn.setBackgroundResource(R.drawable.record_button3);
			count = 0;
			processValue = 0;
			playBtn.setEnabled(true);
			playBtn.setBackgroundResource(R.drawable.playbtn3);

			chooseSoundBtn.setEnabled(true);
			chooseSoundBtn
					.setBackgroundResource(R.drawable.choose_from_folder1);

			saveBtn.setEnabled(true);
			saveBtn.setBackgroundResource(R.drawable.save_button1);

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
		if (hadSound) {
			AppUtils.writeMp3ToEndOfImage(filename, soundPath, true);
		} else {
			AppUtils.writeMp3ToEndOfImage(filename, soundPath, false);
		}
		AppUtils.logString("filename: " + filename);
		// delete temp mp3 file
		AppUtils.deleteTempFile();
		AppUtils.logString("deleted mp3 tempFile");

		Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG)
				.show();

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
			playBtn.setBackgroundResource(R.drawable.pause_button1);
			if (seekBar.getVisibility() == View.GONE) {
				seekBar.setVisibility(View.VISIBLE);
				chronometer.setVisibility(View.GONE);
			}
			Log.d("count", count + "");
			if (count == 0) {
				count++;
				File file = new File(soundPath);
				Uri uri = Uri.fromFile(file);
				mp = MediaPlayer.create(this, uri);
				seekBar.setMax(mp.getDuration());
				seekBar.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						seekChange(v);
						return false;
					}
				});
			}

			// mp.seekTo(seekBar.getProgress());
			mp.start();
			// AppUtils.playSong(mp, soundPath);
			startPlayProgressUpdater();

			recordBtn.setEnabled(false);
			recordBtn.setBackgroundResource(R.drawable.record_button_disable1);

			chooseSoundBtn.setEnabled(false);
			chooseSoundBtn
					.setBackgroundResource(R.drawable.choose_from_folder_disable1);

			saveBtn.setEnabled(false);
			saveBtn.setBackgroundResource(R.drawable.save_disable);

		} else {
			seekBar.setProgress(mp.getCurrentPosition());
			isPlaying = false;
			processValue = seekBar.getProgress();
			playBtn.setBackgroundResource(R.drawable.playbtn3);
			saveBtn.setEnabled(true);
			chooseSoundBtn.setEnabled(true);
			recordBtn.setEnabled(true);

			saveBtn.setBackgroundResource(R.drawable.save_button1);
			chooseSoundBtn
					.setBackgroundResource(R.drawable.choose_from_folder1);
			recordBtn.setBackgroundResource(R.drawable.record_button3);
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
			playBtn.setBackgroundResource(R.drawable.play_button1);
			seekBar.setProgress(0);

			saveBtn.setEnabled(true);
			chooseSoundBtn.setEnabled(true);
			recordBtn.setEnabled(true);

			saveBtn.setBackgroundResource(R.drawable.save_button1);
			chooseSoundBtn
					.setBackgroundResource(R.drawable.choose_from_folder1);
			recordBtn.setBackgroundResource(R.drawable.record_button3);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == AppConst.CHOOSE_SOUND_REQUEST) {
			if (resultCode == RESULT_OK) {
				soundPath = data.getExtras().getString("soundPath");
				AppUtils.logString("sound path:" + soundPath);
				count = 0;
				processValue = 0;
			}
		}
	}

	public void share(View v) {
		Intent share = new Intent(Intent.ACTION_SEND);

		share.setType("image/jpg");

		// String imagePath = Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
		// + "/result.png";

		File imageFileToShare = new File(imagePath);

		Uri uri = Uri.fromFile(imageFileToShare);
		share.putExtra(Intent.EXTRA_STREAM, uri);

		startActivity(Intent.createChooser(share, "Share Image!"));
	}
	
	

}
