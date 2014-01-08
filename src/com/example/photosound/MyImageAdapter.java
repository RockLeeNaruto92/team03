/*
 * @author huynh
 */
package com.example.photosound;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	public String getImagename(Integer position) {
		int i = 0;
		for (String image : ImageList) {
			if (i == position) {
				return image;
			}
		}
		return null;
	}

	/**
	 * Cần override lại hàm này để hiển thị hình ảnh
	 */
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ImageView imgView;
		if(convertView==null){
			imgView=new ImageView(mContext);
			//can chỉnh lại hình cho đẹp
			imgView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imgView.setPadding(8, 8, 8, 8);
		}else{
			imgView=(ImageView) convertView;
		}
		//lấy đúng vị trí hình ảnh được chọn
		//gán lại ImageResource
//		imgView.setImageResource(mThumbIds[arg0]);
		String imgPath = getImagename(mThumbIds[arg0]);
		Bitmap bmp = BitmapFactory.decodeFile(imgPath);
		imgView.setImageBitmap(bmp);
		return imgView;
	}
}