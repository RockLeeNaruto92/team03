/**
 * @author 3C Pham Tran Huynh
 */
package com.example.photosound;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.vn.R;
import android.widget.Button;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;
import android.content.Intent;

public class HomeActivity extends Activity {
	private Button TakePhoto , Gallery;
	private ImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		TakePhoto = (Button) findViewById(R.id.BtnTakePicture);
		Gallery   = (Button) findViewById(R.id.BtnGetImageFrom);
		image     = (ImageView) findViewById(R.id.App_Icon);
		
		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int buttonWidth = Math.round(displayMetric.widthPixels*0.25f);
		int buttonHeight = Math.round(displayMetric.heightPixels*0.1f);
		LayoutParams param  = (LayoutParams)TakePhoto.getLayoutParams();
		param.width = buttonWidth;
		param.height = buttonHeight;
		TakePhoto.setLayoutParams(param);
		Gallery.setLayoutParams(param);
		LayoutParams param1  = (LayoutParams)image.getLayoutParams();
		
		int image_width  = Math.round(displayMetric.widthPixels * 0.55f);
		int image_height = Math.round(displayMetric.heightPixels * 0.4f);  
		param1.width = buttonWidth;
		param1.height = buttonHeight;
		image.setLayoutParams(param1);
		
		TakePhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Press", "take photo");
				Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
				startActivity(intent);
			}
		});
		Gallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Press", "Gallery");
				Intent intent_gallery = new Intent(HomeActivity.this, GetImageFromGallery.class);
				startActivity(intent_gallery);
			}
		});
//		LayoutParams param = TakePhoto.
		
	}

}
