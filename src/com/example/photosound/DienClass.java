package com.example.photosound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.vn.R;
import android.widget.TextView;

public class DienClass extends Activity{
	private TextView Textv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dien_xml);
	    Textv = (TextView)findViewById(R.id.textView1);
	    Intent iin= getIntent();
	    Bundle b = iin.getExtras();
	    if(b!=null)
	    {
	        String j =(String) b.get("imgPath");
	        Textv.setText(j);
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
