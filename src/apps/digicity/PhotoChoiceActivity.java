package apps.digicity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoChoiceActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		this.setContentView(R.layout.activity_photo_choice);
		
		TextStylizer.makeCursive(this, R.id.questHeaderText);
		TextStylizer.makeCursive(this, R.id.questDescriptionText);
		
		ImageView photoView = (ImageView) this.findViewById(R.id.photoToTake);
		TextView exhaustedView = (TextView) this.findViewById(R.id.exhaustedText);
		TextView recreateView = (TextView) this.findViewById(R.id.questDescriptionText);
		if (PhotoInfo.nextPhotoIndex >= PhotoInfo.details.size()) {
			exhaustedView.setVisibility(View.VISIBLE);
			photoView.setVisibility(View.GONE);
			recreateView.setVisibility(View.GONE);
		} else {
			exhaustedView.setVisibility(View.GONE);
			photoView.setVisibility(View.VISIBLE);
			recreateView.setVisibility(View.VISIBLE);
			
			photoView.setImageBitmap(PhotoInfo.details.get(PhotoInfo.nextPhotoIndex).image);
		}
		
		ImageView cameraView = (ImageView) this.findViewById(R.id.camera);
		cameraView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoChoiceActivity.this.takePhoto();
			}
		});
	}
	
	public void takePhoto() {
		Intent switchActivity = new Intent(PhotoChoiceActivity.this, CoolActivity.class);
		PhotoChoiceActivity.this.startActivity(switchActivity);
		PhotoChoiceActivity.this.finish();
		
		overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
	}
}
