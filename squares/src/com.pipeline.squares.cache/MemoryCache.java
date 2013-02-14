package com.pipeline.squares.cache;


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;

public class MemoryCache {
    private HashMap<String, SoftReference<Bitmap>> cache=new HashMap<String, SoftReference<Bitmap>>();
    private HashMap<String,String> descriptionCache = new HashMap<String,String>();
    private HashMap<String, SoftReference<Bitmap>> cameraThumb=new HashMap<String, SoftReference<Bitmap>>();

    

    public Bitmap getBitmap(String id){ 	
        if(!cache.containsKey(id)){
            return null;
        }
        SoftReference<Bitmap> ref=cache.get(id);
        System.out.println("GetBitmap: " + ref.get());
        System.out.println(" _ _ _ _ _ _ _ ");
        return ref.get();
    }
    
    public String getDesc(String id){
    	if(!descriptionCache.containsKey(id + "_desc")){
            return null;
        }
        String ref=descriptionCache.get(id + "_desc");
        return ref;
    }

    /*
     * Share Activity
     */
    public void put(String id, Bitmap bitmap, String desc){
    	//System.out.println("Metiendo description "+ id);
        cache.put(id, new SoftReference<Bitmap>(bitmap));
        descriptionCache.put(id + "_desc", desc);
    }
    
 
    /*
     * Camera Activity
     */
    public void putImage(String id, Bitmap bitmap){ 	
    	cache.put(id, new SoftReference<Bitmap>(bitmap));
    }
    
    
    public void clear() {
        cache.clear();
    }
}