/**
 * @author Pham Tran Huynh
 */
package com.example.photosound;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.vn.R;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class CameraActivity extends Activity {
   
	ImageView imageView;
	private  ImageButton Record ;
	private  Button      Picture;
	
	public final String TAG = "Camera";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		
		Record  = (ImageButton)findViewById(R.id.BtnCreateMark);
		Picture = (Button)findViewById(R.id.BtnTakePicture); 
		
		
		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int imgWidth = Math.round(displayMetric.widthPixels*0.9f);
		int imgHeight = Math.round(displayMetric.heightPixels*0.7f);
		//Layou param  = (LayoutParams)imageView.getLayoutParams();
		android.widget.RelativeLayout.LayoutParams param = (android.widget.RelativeLayout.LayoutParams)imageView.getLayoutParams();
		param.width = imgWidth;
		param.height = imgHeight;
		imageView.setLayoutParams(param);
		/*
		int buttonWidth = Math.round(displayMetric.widthPixels*0.2f);
		int buttonHeight = Math.round(displayMetric.heightPixels*0.1f);
		LayoutParams param1  = (LayoutParams)Picture.getLayoutParams();
		param.width = buttonWidth;
		param.height = buttonHeight;
		//Record.setLayoutParams(param1);
		Picture.setLayoutParams(param1);
*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onTakePhotoClick(View v){
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		deletePhoto();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoUri());
		startActivityForResult(intent, 0);
		Record.setVisibility(View.VISIBLE);
	}
	
	public Uri getPhotoUri(){
		File rootFolder = Environment.getExternalStorageDirectory();
		File tempPhoto = new File(rootFolder.getAbsolutePath()+File.separator + "tmp.jpg");
		try{
			if(!tempPhoto.exists()){
				tempPhoto.createNewFile();
			}
		
			Uri temPhotoUri = Uri.fromFile(tempPhoto);
			return temPhotoUri;
		}
		catch (IOException e){
		e.printStackTrace();
		return Uri.EMPTY;
		}
	}
	
	public void deletePhoto(){
		File rootFolder = Environment.getExternalStorageDirectory();
		File tempPhoto = new File(rootFolder.getAbsolutePath()+File.separator + "tmp.jpg");
		if(tempPhoto.exists()){
			tempPhoto.delete();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	      if(resultCode == RESULT_CANCELED) return;
	   
	      String imgPath = Environment.getExternalStorageDirectory() + File.separator + "tmp.jpg";
	      Log.v(TAG, imgPath);
//	      Bitmap bmp = BitmapFactory.decodeFile(imgPath);
	      Bitmap bmp = rotateBitmap(imgPath);
	      imageView.setImageBitmap(bmp);
	      
	   }
	
	public  Bitmap rotateBitmap(String fileName) {

		Bitmap bitmap = null;
		Bitmap rotatedBitmap = null;

		try {
			bitmap = BitmapFactory.decodeFile(fileName);
			if (bitmap == null) {
				return null;
			}

			ExifInterface ex = new ExifInterface(fileName);
			int orientation = ex.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);

			Uri fileUri = Uri.fromFile(new File(fileName));
			if (orientation == ExifInterface.ORIENTATION_UNDEFINED) {
				Cursor cursor = this
						.getContentResolver()
						.query(fileUri,
								new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
								null, null, null);

				try {
					if (cursor.moveToFirst()) {
						int deg = cursor
								.getInt(cursor
										.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION));
						if (deg == 90) {
							orientation = ExifInterface.ORIENTATION_ROTATE_90;
						} else if (deg == 180) {
							orientation = ExifInterface.ORIENTATION_ROTATE_180;
						} else if (deg == 270) {
							orientation = ExifInterface.ORIENTATION_ROTATE_270;
						}
					}

					cursor.close();
				} catch (Exception e) {

				}
			}

			int degree = 0;
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree += 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree += 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree += 90;
				break;
			case ExifInterface.ORIENTATION_TRANSPOSE:
				degree += 45;
				break;
			case ExifInterface.ORIENTATION_UNDEFINED:
				degree += 360;
				break;
			default:
				break;
			}

			if (degree > 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(degree);
				rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			} else {
				rotatedBitmap = bitmap;
			}

		} catch (Exception e) {
			// handle the exception(s)
		}

		return rotatedBitmap;
	}
	
}
