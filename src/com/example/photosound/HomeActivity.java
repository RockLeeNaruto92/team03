/**
 * @author Pham Tran Huynh
 */
package com.example.photosound;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.vn.R;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;

public class HomeActivity extends Activity {
	private ImageButton TakePhoto , Gallery;
	private ImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		TakePhoto = (ImageButton) findViewById(R.id.BtnGetImageFrom);
		Gallery   = (ImageButton) findViewById(R.id.BtnTakePicture);
		image     = (ImageView) findViewById(R.id.App_Icon);
		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int buttonWidth = Math.round(displayMetric.widthPixels*0.4f);
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
		
//		LayoutParams param = TakePhoto.
		
	}

}
