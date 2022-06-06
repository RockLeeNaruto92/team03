/**
 * @author 3C Pham Tran Huynh
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
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.vn.R;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class HomeActivity extends Activity {
	private Button TakePhoto, Gallery;
	private ImageView image;
	private final int SELECT_PICTURE = 1;
	private int TAKE_PICTURE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		TakePhoto = (Button) findViewById(R.id.BtnTakePicture);
		Gallery = (Button) findViewById(R.id.BtnGetImageFrom);
		image = (ImageView) findViewById(R.id.App_Icon);

		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int buttonWidth = Math.round(displayMetric.widthPixels * 0.4f);
		int buttonHeight = Math.round(displayMetric.heightPixels * 0.1f);
		LayoutParams param = (LayoutParams) TakePhoto.getLayoutParams();
		param.width = buttonWidth;
		param.height = buttonHeight;
		TakePhoto.setLayoutParams(param);
		Gallery.setLayoutParams(param);
		LayoutParams param1 = (LayoutParams) image.getLayoutParams();

		int image_width = Math.round(displayMetric.widthPixels * 0.5f);
		int image_height = Math.round(displayMetric.heightPixels * 0.4f);
		param1.width = image_width;
		param1.height = image_height;
		image.setLayoutParams(param1);

		TakePhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deletePhoto();
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoUri());
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});
		Gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Gallery.setBackgroundResource(R.drawable.button7_hover);
				// Gallery.setBackgroundResource(R.drawable.button7);
				// Gallery.setBackgroundResource(R.drawable.button7_hover);
				// Gallery.setBackgroundResource(R.drawable.button7);
				Log.d("Press", "Gallery");
				// Intent intent_gallery = new Intent(HomeActivity.this,
				// GetImageFromGallery.class);
				// startActivity(intent_gallery);
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
			}
		});
		// LayoutParams param = TakePhoto.

	}

	/**
	 * @athor huynh
	 * @return Uri
	 */
	public Uri getPhotoUri() {
		File rootFolder = Environment.getExternalStorageDirectory();
		File tempPhoto = new File(rootFolder.getAbsolutePath() + File.separator
				+ "tmp.jpg");
		try {
			if (!tempPhoto.exists()) {
				tempPhoto.createNewFile();
				Log.d("TAGANH", "ok");
			}

			Uri temPhotoUri = Uri.fromFile(tempPhoto);
			return temPhotoUri;
		} catch (IOException e) {
			e.printStackTrace();
			return Uri.EMPTY;
		}
	}

	/**
	 * @author huynh
	 */
	public void deletePhoto() {
		File rootFolder = Environment.getExternalStorageDirectory();
		File tempPhoto = new File(rootFolder.getAbsolutePath() + File.separator
				+ "tmp.jpg");
		if (tempPhoto.exists()) {
			tempPhoto.delete();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == SELECT_PICTURE) {
			Uri selectedImageUri = data.getData();
            String selectedImagePath = getPath(selectedImageUri);
            
			data.setClass(HomeActivity.this, PictureActivity.class);
			data.putExtra("imgPath", selectedImagePath);
			data.putExtra("fromGallery", true);
			startActivity(data);
		} else if ( requestCode == TAKE_PICTURE){
			Uri selectedImageUri = data.getData();
            String selectedImagePath = getPath(selectedImageUri);
            
           // Uri fileName = data.getData();

    		Bitmap bmp = null;
    		try {
    			InputStream is = getContentResolver().openInputStream(selectedImageUri);
    			Options option = new Options();
    			option.inSampleSize = 2;
    			bmp = BitmapFactory.decodeStream(is, null, option);
    			is.close();

    			if (bmp != null) {
    				bmp = rotateBitmap(bmp,selectedImageUri);
    				//imageView.setImageBitmap(bmp);
    			}
    		} catch (Exception e) {
    			Log.e("decode", "" + e.getMessage());
    		}

    		
    		String path = Environment.getExternalStorageDirectory().toString();
    		OutputStream fOutputStream = null;
    		selectedImagePath = Environment.getExternalStorageDirectory()
    				.getAbsoluteFile() + File.separator + "rotate.jpg";
    		File file = new File(selectedImagePath);
    		if (file.exists()) {
    			file.delete();
    		}
    		try {
    			file.createNewFile();
    			fOutputStream = new FileOutputStream(file);

    			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

    			fOutputStream.flush();
    			fOutputStream.close();

    			MediaStore.Images.Media.insertImage(getContentResolver(),
    					file.getAbsolutePath(), file.getName(), file.getName());
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    			Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
    			return;
    		} catch (IOException e) {
    			e.printStackTrace();
    			Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
    			return;
    		}
            
			data.setClass(HomeActivity.this, PictureActivity.class);
			data.putExtra("imgPath", selectedImagePath);
			data.putExtra("fromCamera", true);
			startActivity(data);
		}
	}

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    private Bitmap rotateBitmap(Bitmap inputBmp, Uri fileName) {

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
				rotatedBitmap = Bitmap
						.createBitmap(inputBmp, 0, 0, inputBmp.getWidth(),
								inputBmp.getHeight(), matrix, true);
			} else {
				rotatedBitmap = inputBmp;
			}

		} catch (Exception e) {
			// handle the exception(s)
		}

		return rotatedBitmap;
	}
}
