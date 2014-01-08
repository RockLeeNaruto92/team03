/**
 * @author Pham Tran Huynh
 */
package com.example.photosound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import android.widget.Toast;

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
		
		Record.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CameraActivity.this, DienClass.class);
				intent.putExtra("imgPath", getPhotoUri());
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onTakePhotoClick(View v){
		deletePhoto();
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoUri());
		Log.d(TAG, "Create anh");
		startActivityForResult(intent, 0);
		Log.d(TAG, "Tra anh ve");
	}
	
	public Uri getPhotoUri(){
		File rootFolder = Environment.getExternalStorageDirectory();
		File tempPhoto = new File(rootFolder.getAbsolutePath()+File.separator + "tmp.jpg");
		try{
			if(!tempPhoto.exists()){
				tempPhoto.createNewFile();
				Log.d("TAGANH", "ok");
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
		Record.setVisibility(View.VISIBLE);

	      Log.v(TAG, "Result  :"+resultCode);

	      if(resultCode == RESULT_CANCELED) return;
	      
	     Uri fileName = data.getData();
	     
	     Bitmap bmp = null;
	     try{
	    	 InputStream is = getContentResolver().openInputStream(fileName);
	    	 bmp = BitmapFactory.decodeStream(is);
	    	 is.close();
	    	 
	    	 if(bmp != null){
	    		  bmp = rotateBitmap(bmp,fileName);
	    		 imageView.setImageBitmap(bmp);
	    	 }
	     }catch(Exception e){
	    	 Log.e("decode","" + e.getMessage());
	     }
	     
	   
	      //String imgPath = Environment.getExternalStorageDirectory() + File.separator + "tmp.jpg";
	     // Log.v(TAG, "Duong dan anh :"+imgPath);
//	      Bitmap bmp = BitmapFactory.decodeFile(imgPath);
	     // echo 
	      //Bitmap bmp = rotateBitmap(imgPath);
	     // imageView.setImageBitmap(bmp);

	    //  deletePhoto();
	 /*     String path = Environment.getExternalStorageDirectory().toString();
	        OutputStream fOutputStream = null;
	        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator+"tmp.jpg");
	        try {
	        	file.createNewFile();
	            fOutputStream = new FileOutputStream(file);

				bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

	            fOutputStream.flush();
	            fOutputStream.close();

	            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
	            return;
	        } catch (IOException e) {
	            e.printStackTrace();
	            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
	            return;
	        }*/
	      
	   }
	

	
	private  Bitmap rotateBitmap(Bitmap inputBmp,Uri fileName) {

		Bitmap rotatedBitmap = null;

		try {

			ExifInterface ex = new ExifInterface(fileName.getPath());
			int orientation = ex.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_UNDEFINED);

			if (orientation == ExifInterface.ORIENTATION_UNDEFINED) {
				Cursor cursor = this
						.getContentResolver()
						.query(fileName,
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
				rotatedBitmap = Bitmap.createBitmap(inputBmp, 0, 0,
						inputBmp.getWidth(), inputBmp.getHeight(), matrix, true);
			} else {
				rotatedBitmap = inputBmp;
			}

		} catch (Exception e) {
			// handle the exception(s)
		}

		return rotatedBitmap;
	}
	
}
