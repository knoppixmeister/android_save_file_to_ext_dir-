package lv.bizapps.extfilesaveexample;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button b = (Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				b.setEnabled(false);

				new Thread(new Runnable() {
					public void run() {
						Log.e("AAA", "IS WRITEABLE: "+isExternalStorageWritable());

						File extDir = getAlbumStorageDir(getApplicationContext(), "IMAGES");
						Log.e("AAA", "EXT DIR: "+extDir);

						try {
							URL imageUrl = new URL("http://online.igk-group.ru/assets/themes/multistat/images/logo_igk.jpg");
							Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());

							File imageFile = new File(extDir, "image_file.jpg");

							FileOutputStream out = new FileOutputStream(imageFile);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
							out.flush();
							out.close();
						}
						catch(Exception e) {
							e.printStackTrace();
						}

						runOnUiThread(new Runnable() {
							public void run() {
								b.setEnabled(true);
							}
						});
					}
				}).start();
			}
		});
	}

	public File getAlbumStorageDir(Context context, String albumName) {
		File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
		if(!file.mkdirs()) Log.e("AAA", "Directory not created");

		return file;
	}

	boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if(	Environment.MEDIA_MOUNTED.equals(state) &&
	    	!Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
	    {
	        return true;
	    }

	    return false;
	}
}