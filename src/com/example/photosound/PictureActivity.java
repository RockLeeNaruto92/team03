/*
 * @author Dien
 */
package com.example.photosound;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.vn.R;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PictureActivity extends Activity{
	/*
	 * Dang Dinh Dien
	 */
	ImageView image; 
	private int image_id;
	ImageButton stopRecordBtn;
	ImageButton recordBtn;
	ImageButton playBtn;
	ImageButton saveBtn;
	ImageButton cancelBtn;
	int recordBtnStatus;//check status of recordBtn variables. 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		//Bundle extra = getIntent().getExtras();
		//this.image_id = extra.getInt("image");
		image = (ImageView) findViewById(R.id.imageView);
		//image.setBackgroundResource(this.image_id);
		Bitmap bitmap;
		Intent intent = getIntent();
//	    bitmap= (Bitmap) intent.getParcelableExtra("BitmapImage");
	    Bundle b = intent.getExtras();
		//bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aodai);
		//this.image_id = extra.getInt("image_bitmap");		
	    
	    String filename = (String)b.get("BitmapImage");
	    Bitmap bmp = BitmapFactory.decodeFile(filename);
		image.setImageBitmap(bmp);
		recordBtn=(ImageButton) findViewById(R.id.recordBtn);
		playBtn=(ImageButton) findViewById(R.id.playBtn);
		saveBtn=(ImageButton) findViewById(R.id.saveBtn);
		cancelBtn=(ImageButton) findViewById(R.id.cancelBtn);
		recordBtnStatus=0;
		recordBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(recordBtnStatus==0){
					if (playBtn.getVisibility() == View.VISIBLE) {
						playBtn.setVisibility(View.INVISIBLE);
						saveBtn.setVisibility(View.INVISIBLE);
						cancelBtn.setVisibility(View.INVISIBLE);
					}
					recordBtn.setImageResource(R.drawable.stop_recordbtn);
					recordBtnStatus=1;
				}
				else {					
					recordBtn.setImageResource(R.drawable.microphone1);
					playBtn.setVisibility(View.VISIBLE);
					saveBtn.setVisibility(View.VISIBLE);
					cancelBtn.setVisibility(View.VISIBLE);
					recordBtnStatus=0;
				}
					
			}
			
		});
		
		playBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PictureActivity.this, PictureSoundActivity.class);							
				startActivity(intent);
				finish();
			}
			
		});
		
	}
}
