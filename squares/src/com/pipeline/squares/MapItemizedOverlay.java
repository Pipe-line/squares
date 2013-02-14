package com.pipeline.squares;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapItemizedOverlay extends ItemizedOverlay<OverlayItem>
{
     private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
     private Context mContext;
    
     private JSONArray pointsArray = null;
     private JSONObject json = null;
     private String latitude = "";
     private String longitude = "";
    
     public MapItemizedOverlay(Drawable defaultMarker) {
          super(boundCenterBottom(defaultMarker));
     }
    
     public MapItemizedOverlay(Drawable defaultMarker, Context context) {
          super(boundCenterBottom(defaultMarker));
          mContext = context;
     }
     public void addOverlay(OverlayItem overlay) {
          mOverlays.add(overlay);
          populate();
     }
    
     @Override
     protected OverlayItem createItem(int i) {
          return mOverlays.get(i);
     }
     @Override
     public int size() {
          return mOverlays.size();
     }
     @Override
     protected boolean onTap(int index) {
          OverlayItem item = mOverlays.get(index);
         
          System.out.println(index + "***");
         
          try {
              
               json = pointsArray.getJSONObject(index);
               latitude = json.getString("lat");
               longitude = json.getString("lon");
          }
          catch(Exception e){
               System.out.println(e + "Exception");
          }
         
          AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
          dialog.setTitle(item.getTitle());
          dialog.setMessage(item.getSnippet());
          dialog.setPositiveButton("Ver", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                          Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0?q=" + (latitude + "," + longitude)));
                                mContext.startActivity(intent);
                    
                }
            });
         dialog.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                     dialog.cancel();
                }
            });
        /* dialog.setNeutralButton("Llegar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                          Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                       Uri.parse("geo:0,0?q=" + (latitude +","+ longitude)));
                       mContext.startActivity(intent);
                }
            });*/
        
        
          dialog.show();
          return true;
     }
    
    
     public void setJsonArray(JSONArray jArray){
          this.pointsArray = jArray;
          populate();
     }
    
}