/**
 * @author 3B Dang Dinh Dien
 */
package com.example.photosound;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.vn.R;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;

public class PictureSoundActivity extends Activity {
	/*
	 * @author: 3B Dang Dinh Dien
	 */
	private ImageView image;
	
	private Button playBtn;	
	private final Handler handler = new Handler();
	private Chronometer chronometer;
	private SeekBar seekBar;
	private MediaPlayer mp;

	private String imagePath;
	
	private boolean isPlaying = false;
	

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

	

	/**
	 * @author 3B Dang Dinh Dien
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
	 * @author 3B Dang Dinh Dien
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
		playBtn = (Button) findViewById(R.id.playBtn);		
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
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onPlayBtnClick(playBtn);
			}
		});

	}

	/**********************************************************/
	/**** ON BUTTON CLICKED *****/
	
	

	/**
	 * @author 3B Dang Dinh Dien
	 * @param view
	 */
	public void onPlayBtnClick(View view) {

		AppUtils.logString("Play click");
		if (isPlaying == false) {
			isPlaying = true;
			playBtn.setBackgroundResource(R.drawable.pause01);
			AppUtils.playSong(mp, AppUtils.getTempFile());
			seekBar.setMax(mp.getDuration());
			seekBar.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					seekChange(v);
					return false;
				}
			});
			seekBar.setVisibility(View.VISIBLE);
			chronometer.setVisibility(View.GONE);			
			
			Log.d("maxsecond", Integer.toString(mp.getDuration()));			
			try {
				mp.start();
				startPlayProgressUpdater();
			} catch (IllegalStateException e) {
				mp.pause();
			}
		} else {
			isPlaying = false;
			playBtn.setBackgroundResource(R.drawable.playicon1);			
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
			playBtn.setBackgroundResource(R.drawable.playicon1);
			seekBar.setProgress(0);			
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
