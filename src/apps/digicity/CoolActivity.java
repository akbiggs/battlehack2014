package apps.digicity;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;

import android.R.string;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class CoolActivity extends Activity implements CvCameraViewListener2 {
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("TAG", "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
    }
    
    
	private CameraBridgeViewBase mOpenCvCameraView;
	
	private boolean scheduleSave;
	
	private Mat savedImage;
	private MatOfKeyPoint savedKeypoints;
	private Mat savedDescriptors;
	private Mat matchImage;
	
	Bitmap newBitmap;
	ImageView imageView;
	TextView textView;
	String debugText = "";

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	     Log.i("TAG", "called onCreate");
	     super.onCreate(savedInstanceState);
	     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	     this.setContentView(R.layout.activity_cool);
	     mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
	     mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
	     mOpenCvCameraView.setCvCameraViewListener(this);
	     
	     imageView = (ImageView) this.findViewById(R.id.ImageView);
	     imageView.setImageResource(R.drawable.ic_launcher);
	     imageView.bringToFront();
	     textView = (TextView) this.findViewById(R.id.text);
	 }

	 @Override
	 public void onPause()
	 {
	     super.onPause();
	     if (mOpenCvCameraView != null)
	         mOpenCvCameraView.disableView();
	 }

	 public void onDestroy() {
	     super.onDestroy();
	     if (mOpenCvCameraView != null)
	         mOpenCvCameraView.disableView();
	 }
	 
	 @Override
     public boolean onTouchEvent(MotionEvent event) {
		 this.scheduleSave = true;
		 return super.onTouchEvent(event);
     }

	 public void onCameraViewStarted(int width, int height) {
	 }

	 public void onCameraViewStopped() {
	 }

	 public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		
		 org.opencv.core.Size size = new org.opencv.core.Size(200, 200);
		 org.opencv.core.Size kernel = new org.opencv.core.Size(2, 2);
		 Mat image = inputFrame.gray();
		 
		 try {
			 Imgproc.resize(image, image, size);
		 	 Imgproc.blur(image, image, kernel);
		 }
		 catch (CvException e) {Log.d("Exception",e.getMessage());}
		 
		 if (this.scheduleSave) {
			 this.scheduleSave = false;
			 
			 FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
			 MatOfKeyPoint keypoints = new MatOfKeyPoint();
			 detector.detect(image, keypoints);
			 
			 DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
			 Mat descriptors = new Mat();
			 extractor.compute(image, keypoints, descriptors);
			 
			 this.savedImage = image;
			 this.savedKeypoints = keypoints;
			 this.savedDescriptors = descriptors;
		 }
		 
		 if (this.savedImage != null) {

			 FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
			 MatOfKeyPoint keypoints = new MatOfKeyPoint();
			 detector.detect(image, keypoints);
			 
			 DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
			 Mat descriptors = new Mat();
			 extractor.compute(image, keypoints, descriptors);
			 
			 if (keypoints.empty())
				 return inputFrame.rgba();
			 
			 DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
			 MatOfDMatch matches = new MatOfDMatch();
			 matcher.match(savedDescriptors, descriptors, matches);
			 
			 Mat matchImage = new Mat();
			 Features2d.drawMatches(this.savedImage, this.savedKeypoints, image, keypoints, matches, matchImage);
			 
			 int goodMatches = 0;
			 for( DMatch match : matches.toList()) {
			 	 if (match.distance < 200)
			 		 goodMatches++;
			 }

			 newBitmap = null;
			 Mat tmp = new Mat (matchImage.height(), matchImage.width(), CvType.CV_8U, new Scalar(4));
			 
			 // Build a bitmap and update the UI
			 try {
			     Imgproc.cvtColor(matchImage, tmp, Imgproc.COLOR_RGB2BGRA);
			     newBitmap = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
			     Utils.matToBitmap(tmp, newBitmap);
			     
			     debugText = " " +goodMatches+ " / " + matches.total();
			     //debugText = "total Distance: " + totalDistance;

			     this.runOnUiThread(new Runnable() {
			         @Override
			         public void run() {
					     imageView.setImageBitmap(newBitmap);
					     imageView.bringToFront();
					     textView.setText(debugText);
			        }
			     });
			 }
			 catch (CvException e) {Log.d("Exception",e.getMessage());}
			 
			 
			 /*double[] white = {255, 255, 0, 255};
			 List<KeyPoint> _list = keypoints.toList();
			 for (int i = 0; i < _list.size(); i++) {
				 KeyPoint point = _list.get(i);
				 image.put((int)point.pt.y, (int)point.pt.x, white ); 
			 }*/
			 
			 return inputFrame.rgba();
		 } else {
			 
			 FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
			 MatOfKeyPoint keypoints = new MatOfKeyPoint();
			 detector.detect(image, keypoints);
			 
			 double[] white = {255, 255, 0, 255};
			 List<KeyPoint> _list = keypoints.toList();
			 for (int i = 0; i < _list.size(); i++) {
				 KeyPoint point = _list.get(i);
				 image.put((int)point.pt.y, (int)point.pt.x, white ); 
			 }
			 
			 return inputFrame.rgba();
		 }
		 
	     // return inputFrame.rgba();
	 }
	 
}
