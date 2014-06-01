package apps.digicity;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotoDetails {
	public String path;
	public Bitmap image;
	public Bitmap successImage;
	public float longitude;
	public float latitude;
	
	public PhotoDetails(AssetManager assets, String text) {
		String[] details = text.split(",");
		
		this.path = details[0];
		this.image = this.getBitmapFromAsset(assets, this.path + ".jpg");
		this.successImage = this.getBitmapFromAsset(assets, this.path + "_success.jpg");
		
		this.longitude = Float.parseFloat(details[1]);
		this.latitude = Float.parseFloat(details[2]);
	}
	
	private Bitmap getBitmapFromAsset(AssetManager assetManager, String strName)
    {
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
}
