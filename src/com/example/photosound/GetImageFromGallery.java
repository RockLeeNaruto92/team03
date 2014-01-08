/*
 * @author Pham Tran Huynh
 */
package com.example.photosound;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.vn.R;
import android.widget.ImageButton;
import android.widget.ImageView;

public class GetImageFromGallery extends Activity {

    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private ImageView img;
    private ImageButton Other , Record;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
   
        img = (ImageView)findViewById(R.id.imageView2);
        Other = (ImageButton) findViewById(R.id.BtnGetOther);
        Record= (ImageButton )findViewById(R.id.BtnRecord);
     
        DisplayMetrics displayMetric = getResources().getDisplayMetrics();
		int imgWidth = Math.round(displayMetric.widthPixels*0.9f);
		int imgHeight = Math.round(displayMetric.heightPixels*0.7f);
        android.widget.RelativeLayout.LayoutParams param = (android.widget.RelativeLayout.LayoutParams)img.getLayoutParams();
		param.width = imgWidth;
		param.height = imgHeight;
        img.setLayoutParams(param);
		
		
        Other.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GetImageFromGallery();
				Record.setVisibility(View.VISIBLE);
			}
		});
       
        Record.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GetImageFromGallery.this, DienClass.class);
				intent.putExtra("imgPath", selectedImagePath);
				startActivity(intent);
			}
		});
        
        GetImageFromGallery();
                
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public void GetImageFromGallery(){
    	
    	Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
    
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                img.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
