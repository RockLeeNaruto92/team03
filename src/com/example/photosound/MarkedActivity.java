/**
 * @author 3C Pham Tran Huynh
 */
package com.example.photosound;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.vn.R;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MarkedActivity extends Activity implements OnItemClickListener {

	List<String> ImageList;
	public Integer[] mThumbd;
	public Integer Image_Count = 0;
	private ImageButton BtnPlay;
	GridView viewImage;

	TextView tvSolo;
	TextView tvSoloMsg;
	MyImageAdapter adapter = null;

	ImageView ivSoloPicture;
	Button btnBack;

	Bundle myBackupBunder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myBackupBunder = savedInstanceState;
		setContentView(R.layout.activity_marked);

		getObjectXml();

		viewImage = (GridView) findViewById(R.id.gridView1);

		adapter = new MyImageAdapter(this, mThumbd);
		adapter.setListImage(ImageList);
		viewImage.setAdapter(adapter);
		viewImage.setOnItemClickListener(this);

	}

	public void getObjectXml() {
		setAllImageList();

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		myAction(arg2);
	}

	public void myAction(int posistion) {
		// BtnPlay = (ImageButton) findViewById(R.id.Play);
		// tvSoloMsg = (TextView) findViewById(R.id.tvSoloMsg);
		// tvSoloMsg.setText("Image name " + getImagename(mThumbd[posistion]));
		// ivSoloPicture = (ImageView) findViewById(R.id.imgSolo);
		//
		final String imgPath = getImagename(mThumbd[posistion]);
		// Options option = new Options();
		// option.inSampleSize = 2;
		// Bitmap bmp = BitmapFactory.decodeFile(Environment
		// .getExternalStorageDirectory().getAbsolutePath()
		// + File.separator
		// + AppConst.IMAGE_FOLDER
		// + File.separator
		// + imgPath, option);
		// ivSoloPicture.setImageBitmap(bmp);
		//
		// btnBack = (Button) findViewById(R.id.btnBack);
		//
		// btnBack.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View arg0) {
		// onCreate(myBackupBunder);
		// }
		// });

		// BtnPlay.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(MarkedActivity.this,
		// PictureActivity.class);
		// intent.putExtra("BitmapImage", Environment
		// .getExternalStorageDirectory().getAbsolutePath()
		// + File.separator
		// + AppConst.IMAGE_FOLDER
		// + File.separator
		// + imgPath);
		// startActivity(intent);
		// }
		// });

		Intent intent = new Intent(MarkedActivity.this, PictureActivity.class);
		intent.putExtra(
				"imgPath",
				AppUtils.getFilePath(AppConst.IMAGE_FOLDER + File.separator
						+ imgPath));
		AppUtils.logString(AppUtils.getFilePath(AppConst.IMAGE_FOLDER + File.separator
						+ imgPath));
		intent.putExtra("fromGallery", false);
		startActivity(intent);
		finish();
	}

	public void setAllImageList() {
		ImageList = getListOfFiles(Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separator
				+ "PhotoSound"
				+ File.separator);
		setMthumbd_Array();
	}

	public void setMthumbd_Array() {
		mThumbd = new Integer[Image_Count];
		for (int i = 0; i < Image_Count; i++) {
			mThumbd[i] = i;
		}

		// Log.d("Array 0", ""+mThumbd.length);
	}

	private List<String> getListOfFiles(String path) {

		File files = new File(path);

		FileFilter filter = new FileFilter() {

			private final List<String> exts = Arrays.asList("jpeg", "jpg",
					"png", "bmp", "gif");

			@Override
			public boolean accept(File pathname) {
				String ext;
				String path = pathname.getPath();
				ext = path.substring(path.lastIndexOf(".") + 1);
				return exts.contains(ext);
			}
		};

		final File[] filesFound = files.listFiles(filter);
		List<String> list = new ArrayList<String>();
		if (filesFound != null && filesFound.length > 0) {
			for (File file : filesFound) {
				list.add(file.getName());
				Image_Count++;
				// Log.d("Fi:",file.getName());
			}
		}

		return list;
	}

	public String getImagename(Integer position) {
		int i = 0;
		for (String image : ImageList) {
			if (i == position) {
				return image;
			}
			i++;
		}
		return null;
	}

}
