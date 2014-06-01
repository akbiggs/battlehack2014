package apps.digicity;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class TextStylizer {
	public static void makeCursive(Activity activity, int id) {
		TextView text = (TextView) activity.findViewById(id);
		text.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/cursive.ttf"));
	}
	
	public static void applyAnimation(Activity activity, int textId, int animationId) {
		View v = activity.findViewById(textId);
		v.setAnimation(AnimationUtils.loadAnimation(activity.getApplicationContext(), animationId));
	}
}
