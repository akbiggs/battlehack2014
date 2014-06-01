package apps.digicity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.provider.OpenableColumns;

public class PhotoInfo {
	public static int nextPhotoIndex = 0;
	public static List<PhotoDetails> details;
	public static boolean photoSuccessful = false;
	private static BufferedReader fileReader = null;
	
	public static void initialize(AssetManager assets, Context context) {
		try {
			try {
				BufferedReader stream = new BufferedReader(new InputStreamReader(context.openFileInput("photos_completed")));
				nextPhotoIndex = Integer.parseInt(stream.readLine());
				stream.close();
			} catch (FileNotFoundException e) {
				changePhotoIndex(0, context);
			}
			
			details = new ArrayList<PhotoDetails>();
			fileReader = new BufferedReader(new InputStreamReader(assets.open("photo_infos")));
			String line = fileReader.readLine();
			while (line != null) {
				details.add(new PhotoDetails(assets, line));
				line = fileReader.readLine();
			}
			fileReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void changePhotoIndex(int newIndex, Context context) throws IOException {
		nextPhotoIndex = newIndex;
		FileOutputStream stream = context.openFileOutput("photos_completed", 0);
		stream.write(String.valueOf(nextPhotoIndex).getBytes());
		stream.close();
	}
}
