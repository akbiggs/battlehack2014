package apps.digicity;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private static final ScheduledExecutorService worker = 
			  Executors.newSingleThreadScheduledExecutor();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.setContentView(R.layout.activity_splash);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		TextStylizer.makeCursive(this, R.id.title_text);
		TextStylizer.applyAnimation(this, R.id.title_text, R.anim.splashtextfade);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				SplashActivity.this.loadSomeStuff();
				Intent switchActivity = new Intent(SplashActivity.this, PhotoChoiceActivity.class);
				SplashActivity.this.startActivity(switchActivity);
				SplashActivity.this.finish();
				
				overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			}
		};
		
		worker.schedule(task, 650, TimeUnit.MILLISECONDS);
	}

	private void loadSomeStuff() {
		PhotoInfo.initialize(getAssets(), this.getApplicationContext());
	}
}
