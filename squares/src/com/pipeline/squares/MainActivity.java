package com.pipeline.squares;

import android.app.Activity;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.maps.*;
import com.pipeline.squares.cache.FileCache;
import com.pipeline.squares.cache.Utils;
import com.pipeline.squares.gps.UserLocation;
import com.pipeline.squares.gps.UserLocation.LocationResult;
import com.pipeline.squares.servercalls.ServerCall;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MapActivity {

	// Acvitity
	private Context context = this;
	private Activity activity = this;
	// List View
	private ArrayList<String> mItems;
	private ListView mListView;

	// Map view
	private MapView mapView;
	List<Overlay> mapOverlays;
	MapItemizedOverlay itemizedOverlay;
	private MyLocationOverlay myLocationOverlay;
	private OverlayItem overlayitem = null;
	private GeoPoint[] items;
	private String jResponse = "";
	private static double latitude;
	private static double longitude;

	private Context mContext = this;
	UserLocation myLocation = new UserLocation();

	// Avatar image
	private FileCache f = null;
	private String imagePath = "";
	private ImageView profileImage = null;
	private ImageView profileImageUser = null;
	private String PATH_AVATAR = "http://app.alvaroabella.com/img/";

	// User Info
	SharedPreferences prefsPrivate;
	public static final String LOGIN_PREFS = "LOGIN_PREFS";
	public static final String UID = "uid";
	public static final String FULLNAME = "ufullname";
	public static final String EMAIL = "uemail";
	public static final String POSITION = "uposition";

	private String mUid = "";
	private String mfullName = "";
	private String mEmail = "";
	private String mPosition = "";
	
	//Checkins on memory
	public static final String CHECKIN_MEM = "CHECKIN_MEM";
	public static final String LAST_CHECKIN = "last_checkin";

	private String mLastCheckin = "";

	//Views
	TextView mTvFullName;
	TextView mTvposition;
	TextView mTvCheckin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Close Activity on Log Out
		/*IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("onReceive", "Logout in progress");
				// start the login activity and finish
				// this one
			    
				Intent login = new Intent(MainActivity.this, LoginActivity.class);
				startActivityForResult(login, 0);
				finish();
			}
		}, intentFilter);   */
		
		mTvFullName = (TextView) findViewById(R.id.txtUserName);
		mTvposition = (TextView) findViewById(R.id.position);
		mTvCheckin  = (TextView) findViewById(R.id.last_checkin);
		
		profileImageUser = (ImageView)findViewById(R.id.avatarImageUser);

		/* Get user data from memory */
		prefsPrivate = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);

		mUid = prefsPrivate.getString(UID, "");
		mfullName = prefsPrivate.getString(FULLNAME, "");
		mEmail = prefsPrivate.getString(EMAIL, "");
		mPosition = prefsPrivate.getString(POSITION, "");
		
		prefsPrivate = getSharedPreferences(CHECKIN_MEM, Context.MODE_PRIVATE);
		mLastCheckin = prefsPrivate.getString(LAST_CHECKIN, "");

		if (mfullName != null) {
			mTvFullName.setText(mfullName);
			mTvposition.setText(mPosition);
			mTvCheckin.setText(mLastCheckin);

		} else {
			
			mTvFullName.setText("User Name");
			mTvposition.setText("Not Available");
			mTvCheckin.setText("No checkins");

		}

		/* Get avatar image */
		f = new FileCache(context);
		imagePath = Utils.md5(mEmail) + ".jpg";

		if (!f.exists(imagePath)) {
			new getThumbTask().execute();

		} else {
			System.out.println("como existe...");
			Bitmap bmp = f.getBitmap(imagePath);
			if(bmp != null){
			System.out.println(bmp);
			profileImageUser.setVisibility(View.VISIBLE);
			profileImageUser.setImageBitmap(bmp);
			}
			else{
				//profileImageUser.setVisibility(View.VISIBLE);
				profileImageUser.setImageResource(R.id.avatarImageUser);
			}

		}
		
		final ImageView avatar = (ImageView) findViewById(R.id.avatar);
		avatar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//Intent login = new Intent(MainActivity.this,
				//		ProfileActivity.class);
				//startActivityForResult(login, 0);
			}
		});
		
		if (ServerCall.isOnline(activity)) {

			locationClick();

			/*final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
			// actionBar.setHomeAction(new IntentAction(this,
			// createIntent(this), R.drawable.ic_title_home_demo));
			actionBar.setTitle("Nearness");

			// Notification icon
			final Action shareAction = new IntentAction(this, new Intent(this,
					InboxActivity.class), R.drawable.ic_title_home_default);
			actionBar.addAction(shareAction);*/

			// Map View
			mapView = (MapView) findViewById(R.id.mapview);
			// mapView.setBuiltInZoomControls(true);

			/******************/
			myLocationOverlay = new MyLocationOverlay(this, mapView);
			myLocationOverlay.enableMyLocation();

			mapOverlays = mapView.getOverlays();
			Drawable drawable = this.getResources().getDrawable(
					R.drawable.ic_title_home_default);
			itemizedOverlay = new MapItemizedOverlay(drawable, this);

			mItems = new ArrayList<String>();
			mItems.add("Fernando Fern�ndez Hidalgo");
			mItems.add("�lvaro Abella Gonz�lez");
			mItems.add("Eric Smith");
			mItems.add("Steve Jobs");
			mItems.add("Bill Gates");
			mItems.add("Stephanie Marvell");
			mItems.add("Fernando Fern�ndez Hidalgo");
			mItems.add("�lvaro Abella Gonz�lez");
			mItems.add("Bill Gates");
			mItems.add("Stephanie Marvell");
			mItems.add("Fernando Fern�ndez Hidalgo");

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, mItems);

			mListView = (ListView) findViewById(R.id.lv_users);
			mListView.setAdapter(adapter);



			final ImageView checkin = (ImageView) findViewById(R.id.checkin_iv);
			checkin.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					Intent next = new Intent();
					//next.setClass(mContext, FindPlacesActivity.class);
					next.putExtra("latitude", latitude);
					next.putExtra("longitude", longitude);
					startActivity(next);

				}
			});
		} else {
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, "Sin conexi�n", duration);
			toast.show();
		}
	}
	
	
	public void onResume() {
		super.onResume();

		prefsPrivate = getSharedPreferences(CHECKIN_MEM, Context.MODE_PRIVATE);
		mLastCheckin = prefsPrivate.getString(LAST_CHECKIN, "");
		System.out.println(mLastCheckin + " *************");
		System.out.println(mLastCheckin + " *************");
		System.out.println(mLastCheckin + " *************");
		System.out.println(mLastCheckin + " *************");
		System.out.println(mLastCheckin + " *************");
		if (mLastCheckin != "") {
			mTvCheckin.setText(mLastCheckin);
		}

	}

	private void locationClick() {
		myLocation.getLocation(this, locationResult);
	}

	public LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(final Location location) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			// Load points on map
			Thread thread = new Thread(null, loadPointsAction);
			thread.start();
		}
	};


	private class getThumbTask extends AsyncTask<Void, Void, Boolean> {

		private Bitmap bmp = null;
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				//Bitmap bmp = ServerCall.LoadProfileThumbnail(PATH_AVATAR + imagePath);
				bmp = BitmapFactory.decodeStream((InputStream) new URL(PATH_AVATAR + imagePath).getContent());

				if (bmp != null) {


				}
			} catch (Exception e) {
				return true;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				ImageView profileImageUser = (ImageView) findViewById(R.id.avatarImageUser);

				profileImageUser.setVisibility(View.VISIBLE);
				profileImageUser.setImageBitmap(bmp);

				f.saveFile(imagePath, bmp);
			} catch (Exception e) {
				// do nothing
			}
			super.onPostExecute(result);
		}
	}

	// Maps Thread
	Runnable loadPointsAction = new Runnable() {
		@Override
		public void run() {

			setPoints();
			updateGUIHandler.sendEmptyMessage(0);

		}
	};

	private Handler updateGUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (jResponse.startsWith("networkexception")) {
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, "Error en la conexi�n",
                        duration);
				toast.show();
			}

			mapView.invalidate();
			// buttonClick = true;
		}
	};

	private void setPoints() {
		// TODO Auto-generated method stub
		GeoPoint point;
		try {

			System.out.println("GEO: " + latitude + " - " + longitude);
			jResponse = ServerCall.MapsCall(latitude, longitude);

			JSONObject jObject = new JSONObject(jResponse);
			JSONArray jCoords = jObject.getJSONArray("results");

			int JSONLength = jCoords.length();
			items = new GeoPoint[jCoords.length()];

			for (int i = 0; i < JSONLength; i++) {

				JSONObject e = jCoords.getJSONObject(i);
				JSONObject jsonLocation = e.getJSONObject("geometry")
						.getJSONObject("location");
				// System.out.println(jsonLocation + "jsonlocation");

				double lat = Double.parseDouble(jsonLocation.getString("lat"));
				double lng = Double.parseDouble(jsonLocation.getString("lng"));
				point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
				items[i] = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

				overlayitem = new OverlayItem(point, e.getString("name"),
						e.getString("vicinity"));

				itemizedOverlay.addOverlay(overlayitem);
				itemizedOverlay.setJsonArray(jCoords);
			}
		} catch (Exception e) {
			System.out.println(e + " exception ");
		}

		MapController mc = mapView.getController();

		int minLatitude = Integer.MAX_VALUE;
		int maxLatitude = Integer.MIN_VALUE;
		int minLongitude = Integer.MAX_VALUE;
		int maxLongitude = Integer.MIN_VALUE;

		// Find the boundaries of the item set
		for (GeoPoint item : items) { // item Contain list of Geopints
			int lat = item.getLatitudeE6();
			int lon = item.getLongitudeE6();

			maxLatitude = Math.max(lat, maxLatitude);
			minLatitude = Math.min(lat, minLatitude);
			maxLongitude = Math.max(lon, maxLongitude);
			minLongitude = Math.min(lon, minLongitude);
		}

		int centerLatitude = (int) (maxLatitude - (maxLatitude - minLatitude) * 0.3);
		int centerLongitude = (int) (maxLongitude - (maxLongitude - minLongitude) * 0.6);

		int zoomLat = (int) (Math.abs(maxLatitude - minLatitude) * 1.1);
		int zoomLong = (int) (Math.abs(maxLongitude - minLongitude) * 1.4);
		mc.zoomToSpan(zoomLat, zoomLong);
		mc.animateTo(new GeoPoint((centerLatitude), (centerLongitude)));

		mapOverlays.add(itemizedOverlay);
		mapView.getOverlays().add(myLocationOverlay);

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
