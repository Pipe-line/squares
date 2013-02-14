package com.pipeline.squares.servercalls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;



public class ServerCall {
	private static String mapsURL= "https://maps.googleapis.com/maps/api/place/search/json?radius=100&sensor=false&key=AIzaSyD9qXRH4wm2c3oiSpv6JN5qnnZk4sJStlA&location=";
	private static String searchURL = "https://maps.googleapis.com/maps/api/place/search/json?sensor=true&key=AIzaSyD9qXRH4wm2c3oiSpv6JN5qnnZk4sJStlA&location=";
	
	private static String loginURL= "http://app.alvaroabella.com/login.php";
	private static String registerURL = "http://app.alvaroabella.com/register.php";
	
	private static String checkinURL = "http://app.alvaroabella.com/checkin.php";
	
	public ServerCall(){}


	/***************
	 * Maps Call
	 ***************/
	public static String MapsCall(double lat, double lng){

		String final_mapsURL = mapsURL + lat +","+lng ;
    	HttpParams httpParameters = new BasicHttpParams();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 15000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient client = new DefaultHttpClient(httpParameters);
    	
	    HttpResponse response;
	    JSONObject json = new JSONObject();
	    
	    String jString = "";
	    try {
	    	
	        
	    	HttpPost post = new HttpPost(final_mapsURL);
	        post.setHeader("json", json.toString());
	        StringEntity se = new StringEntity(json.toString());
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
	        post.setEntity(se);
	        
	        //Add user name and password.
		    //List<NameValuePair> nameValuePairs = params;
		    //post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
   		        
	        response = client.execute(post);
	        
	        /* Checking response */
	        if (response != null) {
	            InputStream in = response.getEntity().getContent(); 
	            jString = convertStreamToString(in);
	            
	            //System.out.println(jString);
	            return jString;

	        }
	    } catch (ClientProtocolException e1) {
	    	   //System.out.println("ERROR ClientProtocolException" + e1);
	    	   jString = "networkexception";
	    	   e1.printStackTrace();
	    } catch (IOException e1) {
	    	   //System.out.println("ERROR IOException" + e1);
	    	   jString = "networkexception";
	    	   e1.printStackTrace();
	    } catch (Exception e) {
	    	   e.printStackTrace();
	    	   jString = "networkexception";
	    } 
	    
	    return jString;
	 }

	
	/**SEARCH**/
	public static String SearchPlaces(String lat, String lng, String query){
		
		String final_mapsURL = searchURL + lat +","+lng  + "&name=" + query + "&radius=20000";
		
		System.out.println(final_mapsURL);
		HttpPost post = new HttpPost(final_mapsURL);
    	HttpParams httpParameters = new BasicHttpParams();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 15000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient client = new DefaultHttpClient(httpParameters);
 
    	HttpResponse response;

    	
	    JSONObject json = new JSONObject();
	    
	    String jString = "";
	    try {
	    	
	        post.setHeader("json", json.toString());
	        StringEntity se = new StringEntity(json.toString());
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
	        post.setEntity(se);
	        
	        //Add user name and password.
		    //List<NameValuePair> nameValuePairs = params;
		    //post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
   		    System.out.println(post + " POST ");
	        response = client.execute(post);
	        
	        /* Checking response */
	        if (response != null) {
	            InputStream in = response.getEntity().getContent(); 
	            jString = convertStreamToString(in);
	            
	            //System.out.println(jString);
	            return jString;

	        }
	    } catch (ClientProtocolException e1) {
	    	   //System.out.println("ERROR ClientProtocolException" + e1);
	    	   jString = "networkexception";
	    	   e1.printStackTrace();
	    } catch (IOException e1) {
	    	   //System.out.println("ERROR IOException" + e1);
	    	   jString = "networkexception";
	    	   e1.printStackTrace();
	    } catch (Exception e) {
	    	   e.printStackTrace();
	    	   jString = "networkexception";
	    } 
	    
	    return jString;
	 }
	
	
	/***************
	 * Checkin
	 ***************/
	public static String CheckIn(List<NameValuePair> params){

		String final_mapsURL = checkinURL;
    	HttpParams httpParameters = new BasicHttpParams();
    	int timeoutConnection = 10000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 15000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient client = new DefaultHttpClient(httpParameters);
    	
	    HttpResponse response;
	    JSONObject json = new JSONObject();
	    
	    String jString = "";
	    try {
	    	
	        
	    	HttpPost post = new HttpPost(final_mapsURL);
	        post.setHeader("json", json.toString());
	        StringEntity se = new StringEntity(json.toString());
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
	        post.setEntity(se);
	        
	        //Add user name and password.
		    List<NameValuePair> nameValuePairs = params;
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
   		        
	        response = client.execute(post);
	        
	        /* Checking response */
	        if (response != null) {
	            InputStream in = response.getEntity().getContent(); 
	            jString = convertStreamToString(in);
	            
	            //System.out.println(jString);
	            return jString;

	        }
	    } catch (ClientProtocolException e1) {
	    	   //System.out.println("ERROR ClientProtocolException" + e1);
	    	   jString = "networkexception";
	    	   e1.printStackTrace();
	    } catch (IOException e1) {
	    	   //System.out.println("ERROR IOException" + e1);
	    	   jString = "networkexception";
	    	   e1.printStackTrace();
	    } catch (Exception e) {
	    	   e.printStackTrace();
	    	   jString = "networkexception";
	    } 
	    
	    return jString;
	 }
	
	/***************
	 * Login Call
	 ***************/
	public static String LoginCall(List<NameValuePair> params){
		
		
    	HttpParams httpParameters = new BasicHttpParams();
    	int timeoutConnection = 6000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 7000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient client = new DefaultHttpClient(httpParameters);
    	
	    HttpResponse response = null;
	    JSONObject json = new JSONObject();
	    String codeValue = "-1";
	    String jString = "";
	     try {
	    	HttpPost post = new HttpPost(loginURL);
	        post.setHeader("json", json.toString());
	        StringEntity se = new StringEntity(json.toString());
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
	        post.setEntity(se);
	        
	        //Add user name and password.
		    List<NameValuePair> nameValuePairs = params;
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
   		        
	   
			response = client.execute(post);
	        if (response != null) {
	            InputStream in = response.getEntity().getContent(); 
	            jString = convertStreamToString(in);
	           
	            System.out.println(jString);
	        }
	        
			} catch (ClientProtocolException e1) {
				
				jString = "networkexception";
				e1.printStackTrace();
			} catch (IOException e1) {
				
				jString = "networkexception";
				e1.printStackTrace();
			} catch (Exception e) {
				
				e.printStackTrace();
				jString = "networkexception";
			} 
	    
	    return jString;
	}
	
	/***************
	 * Register Call
	 ***************/
	public static String RegisterCall( List<NameValuePair> params) {
		
    	HttpParams httpParameters = new BasicHttpParams();
    	int timeoutConnection = 6000;
    	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    	int timeoutSocket = 7000;
    	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    	HttpClient client = new DefaultHttpClient(httpParameters);
	    
	    HttpResponse response;
	    JSONObject json = new JSONObject();
	    String codeValue = "-1";
	    String jString = "";
	    try {
	    	
	    	HttpPost post = new HttpPost(registerURL);

	        post.setHeader("json", json.toString());
	        StringEntity se = new StringEntity(json.toString());
	        se.setContentType("application/json; charset=utf-8");
	        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;  charset=utf-8"));
	        post.setEntity(se);
	        
	        //Add user name and password.
		    List<NameValuePair> nameValuePairs = params;
		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			
	        response = client.execute(post);

	        /* Checking response */
	        if (response != null) {
	            InputStream in = response.getEntity().getContent(); 
	            jString = convertStreamToString(in);
	            System.out.println("REQUEST JSON");
	            System.out.println(jString);
	            
	        }
	    } catch (ClientProtocolException e1) {
			
			jString = "networkexception";
			e1.printStackTrace();
		} catch (IOException e1) {
			
			jString = "networkexception";
			e1.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
			jString = "networkexception";
		} 
		
	    return jString;
	}

	

	/****************************
	 * Load profile image from URL
	 * sUrl: url profile image
	 *****************************/
	public static Bitmap LoadProfileThumbnail(String sUrl) {
		Bitmap loadedImage;
		try {
			URL url = new URL(sUrl);
			
			/* Open a connection to that URL. */
			HttpURLConnection ucon = (HttpURLConnection) url.openConnection();

			ucon.setConnectTimeout(3000);
			ucon.setReadTimeout(3000);
			ucon.connect();

			//Bitmap loadedImage = BitmapFactory.decodeStream(ucon
			//		.getInputStream());
			InputStream is = ucon.getInputStream();
            
             loadedImage = BitmapFactory.decodeStream(is);
			ucon.disconnect();
			ucon.disconnect();

			// System.out.println("Valor de Drawable " + d + " Valor");
			// System.out.println("creo el drawable y retorno...");

			return loadedImage;
		} catch (Exception e) {

			return null;
		}
	}
	
	
	public static boolean isOnline(Activity activity) {
		
	    ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	   
	    //mobile
	    State mobile = cm.getNetworkInfo(0).getState();
	    //wifi
	    State wifi = cm.getNetworkInfo(1).getState();

	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
	        //mobile
	    	return true;
	    } else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
	        //wifi
	    	return true;
	    }

	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	


	
}
	
	
	
	
	
