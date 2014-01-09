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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;

public class HomeActivity extends Activity {
	private Button TakePhoto, Gallery;
	private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		TakePhoto = (Button) findViewById(R.id.BtnTakePicture);
		Gallery = (Button) findViewById(R.id.BtnGetImageFrom);
		image = (ImageView) findViewById(R.id.App_Icon);

		DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int buttonWidth = Math.round(displayMetric.widthPixels * 0.25f);
		int buttonHeight = Math.round(displayMetric.heightPixels * 0.1f);
		LayoutParams param = (LayoutParams) TakePhoto.getLayoutParams();
		param.width = buttonWidth;
		param.height = buttonHeight;
		TakePhoto.setLayoutParams(param);
		Gallery.setLayoutParams(param);
		LayoutParams param1 = (LayoutParams) image.getLayoutParams();

		int image_width = Math.round(displayMetric.widthPixels * 0.55f);
		int image_height = Math.round(displayMetric.heightPixels * 0.4f);
		param1.width = buttonWidth;
		param1.height = buttonHeight;
		image.setLayoutParams(param1);

		TakePhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deletePhoto();
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoUri());
				startActivityForResult(intent, 0);
			}
		});
		Gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Press", "Gallery");
				Intent intent_gallery = new Intent(HomeActivity.this,
						GetImageFromGallery.class);
				startActivity(intent_gallery);
			}
		});
		// LayoutParams param = TakePhoto.

	}

	public void deletePhoto() {
		File rootFolder = Environment.getExternalStorageDirectory();
		File tempPhoto = new File(rootFolder.getAbsolutePath() + File.separator
				+ "tmp.jpg");
		if (tempPhoto.exists()) {
			tempPhoto.delete();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_CANCELED)
			return;

		Uri fileName = data.getData();

		Bitmap bmp = null;
		try {
			InputStream is = getContentResolver().openInputStream(fileName);
			Options option = new Options();
			option.inSampleSize = 8;
			bmp = BitmapFactory.decodeStream(is, null, option);
			is.close();

			if (bmp != null) {
				bmp = rotateBitmap(bmp, fileName);
			}
		} catch (Exception e) {
			Log.e("decode", "" + e.getMessage());
		}

		String path = Environment.getExternalStorageDirectory().toString();
		OutputStream fOutputStream = null;
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsoluteFile() + File.separator + "rotate.jpg");
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

		Intent intent = new Intent(HomeActivity.this, PictureActivity.class);
		intent.putExtra("BitmapImage", Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "rotate.jpg");
		intent.putExtra("fromGallery", false);
		AppUtils.logString("putextras");
		startActivity(intent);
		finish();
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
