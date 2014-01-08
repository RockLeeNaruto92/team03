/*
 * @author 3C Pham Tran Huynh
 */
package com.example.photosound;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Class dùng để hiển thị từng hình ảnh riêng lẻ
 * 
 * @author drthanh
 * 
 */
public class MyImageAdapter extends BaseAdapter {
	private Context mContext;
	private Integer[] mThumbIds;
	List<String> ImageList;

	public MyImageAdapter(Context c) {
		mContext = c;
	}

	public MyImageAdapter(Context c, Integer[] arrIds) {
		mContext = c;
		mThumbIds = arrIds;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void setListImage(List<String> list) {
		this.ImageList = list;
	}

	public void viewList() {
		for (String l : ImageList) {
			Log.d("name :", l);
		}
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

	/**
	 * Cần override lại hàm này để hiển thị hình ảnh
	 */

	public View getView(int arg0, View convertView, ViewGroup arg2) {
		viewList();
		ImageView imgView;
		if (convertView == null) {
			imgView = new ImageView(mContext);
			// can chỉnh lại hình cho đẹp
			imgView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imgView.setPadding(8, 8, 8, 8);
		} else {
			imgView = (ImageView) convertView;
		}

		// gán lại ImageResource
		// imgView.setImageResource(mThumbIds[arg0]);
		String imgPath = getImagename(mThumbIds[arg0]);
		Options option = new Options();
		option.inSampleSize = 2;
		Bitmap bmp = BitmapFactory.decodeFile(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + AppConst.IMAGE_FOLDER + File.separator + imgPath,
				option);
		Log.d("File name", imgPath);
		imgView.setImageBitmap(bmp);
		return imgView;
	}
}