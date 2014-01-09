/*
 * @author 3C Pham Tran Huynh
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
    private ImageButton Other ;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
   
       
       
       
        
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
                
                Intent intent = new Intent(GetImageFromGallery.this, PictureActivity.class);
				intent.putExtra("BitmapImage", selectedImagePath);
				intent.putExtra("fromGallery", true);
				startActivity(intent);
				finish();
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
