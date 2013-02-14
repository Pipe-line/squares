package com.pipeline.squares.cache;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context){
        //Find the directory to save cached images
    	//Save cache at "clic2c" directory on SD.
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"cool");
        else{
            cacheDir=context.getCacheDir();
        }
        if(!cacheDir.exists())
            cacheDir.mkdirs();
        
    }
    
    public File getFile(String id){
        //I identify images by ID.
        String filename=String.valueOf(id);
        System.out.println("Where is cach�? " + cacheDir + " - - - " + filename);
        File f = new File(cacheDir, filename);
        return f;
    }
    
    public Bitmap getBitmap(String id){
        //I identify images by ID.
    	System.out.println("Path imagen " + cacheDir + " - " + id );
        
        Bitmap bitmap = BitmapFactory.decodeFile(cacheDir + "/" +id);
        return bitmap;
    }

	public void saveFile(String id, Bitmap bmp) {
		FileOutputStream out;
		try {
			String filePath = cacheDir + "/" + id;
			out = new FileOutputStream(filePath);
			bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e + " Error ");
			e.printStackTrace();
		}
	    
	}
	
	public Boolean exists(String image){
		System.out.println(cacheDir + "/" + image);
		File f = new File (cacheDir + "/" + image);
		return f.exists();
		
	}
    

}