package apps.digicity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class EvaluationActivity extends Activity {
	private static final ScheduledExecutorService worker = 
			  Executors.newSingleThreadScheduledExecutor();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PhotoInfo.photoSuccessful = true;
		
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.setContentView(R.layout.activity_evaluation);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		TextStylizer.makeCursive(this, R.id.evaluationHeaderSuccess);
		TextStylizer.makeCursive(this, R.id.evaluationHeaderFailure);
		
		TextView successHeaderView = (TextView) this.findViewById(R.id.evaluationHeaderSuccess);
		final ImageView successImage = (ImageView) this.findViewById(R.id.successImageUnrevealed);
		final ImageView successImageRevealed = (ImageView) this.findViewById(R.id.successImageRevealed);
		
		successImage.setImageBitmap(PhotoInfo.details.get(PhotoInfo.nextPhotoIndex).successImage);
		TextView failureHeaderView = (TextView) this.findViewById(R.id.evaluationHeaderFailure);
		TextView failureText = (TextView) this.findViewById(R.id.failureText);
		
		if (PhotoInfo.photoSuccessful) {
			successHeaderView.setVisibility(View.VISIBLE);
			successImage.setVisibility(View.VISIBLE);
			
			Runnable task = new Runnable() {
				
				@Override
				public void run() {
//					successImageRevealed.setVisibility(View.VISIBLE);
//					successImage.setVisibility(View.GONE);
//					TextStylizer.applyAnimation(EvaluationActivity.this, R.id.successImageRevealed, R.anim.mainfadein);
//					TextStylizer.applyAnimation(EvaluationActivity.this, R.id.successImageUnrevealed, R.anim.splashfadeout);
				}
			};
			
			worker.schedule(task, 750, TimeUnit.MILLISECONDS);
			
			failureHeaderView.setVisibility(View.GONE);
			failureText.setVisibility(View.GONE);
		} else {
			successHeaderView.setVisibility(View.GONE);
			successImage.setVisibility(View.GONE);
			successImageRevealed.setVisibility(View.GONE);
			
			failureHeaderView.setVisibility(View.VISIBLE);
			failureText.setVisibility(View.VISIBLE);
			TextStylizer.makeCursive(this, R.id.failureText);
		}
	}
}
