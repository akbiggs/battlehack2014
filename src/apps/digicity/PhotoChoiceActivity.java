package apps.digicity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

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
		TextStylizer.applyAnimation(this, R.id.questDescriptionText, R.anim.splashtextfade);
	}
}
