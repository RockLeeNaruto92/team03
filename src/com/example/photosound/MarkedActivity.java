/**
 * @author Pham Tran Huynh
 */
package com.example.photosound;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.vn.R;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
public class MarkedActivity extends Activity implements OnClickListener{
	
	List<String> ImageList;
	public Integer[]mThumbd;
	public Integer Image_Count = 0;
	TextView tvMsg;
	GridView viewImage;
	
	TextView tvSolo;
	TextView tvSoloMsg;
	MyImageAdapter adapter= null;
	
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
    	
    	adapter = new  MyImageAdapter(this, mThumbd);
    	adapter.setListImage(ImageList);
		viewImage.setAdapter(adapter);
		//viewImage.setOnClickListener(this);
		//viewImage.setOnItemClickListener(this); 
	}
	
    public void getObjectXml(){
    	tvMsg = (TextView)findViewById(R.id.tvMsg);
    	setAllImageList();
    	
    }
    
    public void onItemClick(AdapterView<?> arg0,
			View arg1, int arg2, long arg3) {
    		myAction(arg2);
	}	
    
    	public void myAction(int posistion)
    	{
    		setContentView(R.layout.solo_picture);
    		
    		tvSoloMsg=(TextView) findViewById(R.id.tvSoloMsg);
    		tvSoloMsg.setText("Image at "+posistion);
    		ivSoloPicture=(ImageView) findViewById(R.id.imgSolo);

    		String imgPath = getImagename(mThumbd[posistion]);
    		Bitmap bmp = BitmapFactory.decodeFile(imgPath);
    		ivSoloPicture.setImageBitmap(bmp);
    		
    		btnBack=(Button) findViewById(R.id.btnBack);
    		
    		btnBack.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View arg0) {
    				onCreate(myBackupBunder);
    			}
    		});
    	}
    
    
	public void setAllImageList(){
		ImageList = getListOfFiles(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"PhotoSound"+File.separator );
		setMthumbd_Array();
	}
	
	public void setMthumbd_Array(){
		mThumbd = new Integer[Image_Count];
		for( int i = 0 ; i < 1 ; i++ ){
			mThumbd[i] = i; 
		}
		Log.d("Array 0", ""+mThumbd.length);
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
				Log.d("File name :",file.getName());
			}
		}
		
		return list;
	}
	
	public String getImagename(Integer position){
		int i = 0;
		for(String image : ImageList ){
			if(i == position){
				return image;
			}
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
