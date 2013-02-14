package com.pipeline.squares;

import java.util.ArrayList;
import java.util.List;


import com.pipeline.squares.servercalls.ServerCall;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {

	private Activity activity = this;
	
	private Button btnLogin;
	private Button btnCancel;
	ProgressDialog progressDialog = null;
	String loginResponse = "";	
    
    public static final String LOGIN_PREFS = "LOGIN_PREFS";
    public static final String IS_LOGGED = "IS_LOGGED";
    private SharedPreferences prefsPrivate = null;
    public static final String UID = "uid";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
		
		
		/*prefsPrivate = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
		String logged = prefsPrivate.getString(UID, "");
		System.out.println("log " + logged);
		if (logged != "") {
			finish();
			Intent login = new Intent(LoginActivity.this, MainActivity.class);
			startActivityForResult(login, 0);

		} else {
			System.out.println(logged);
		}      */
        

		 // Login button
        btnLogin = (Button)findViewById(R.id.buttonLogIn);
        btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//Close Keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(btnLogin.getWindowToken(), 0);
				
				EditText userText = (EditText)findViewById(R.id.txtUserName);
				EditText passText = (EditText)findViewById(R.id.txtPassword);
				if( userText.getText().length() != 0 && passText.getText().length() != 0){

					progressDialog = ProgressDialog.show(LoginActivity.this, null , "Iniciando", true);
					progressDialog.setCancelable(true);
					Thread thread =  new Thread(null, loginAction);
	            	thread.start();  
				}
				else{
					Context context = getApplicationContext();
				      int duration = Toast.LENGTH_SHORT;
				      Toast toast = Toast.makeText(context, "Error Login", duration);
				      toast.show();
				}

			}
		});
		
		// Login Button
		final Button register = (Button) findViewById(R.id.buttonRegister);
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent login = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivityForResult(login, 0);
			}
		});

	}
	
	
    Runnable loginAction = new Runnable(){
        @Override
        public void run() {
          EditText userText = (EditText)findViewById(R.id.txtUserName);
		  EditText passText = (EditText)findViewById(R.id.txtPassword);
          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
          nameValuePairs.add(new BasicNameValuePair("email", userText.getText().toString()));
          nameValuePairs.add(new BasicNameValuePair("password", passText.getText().toString()));                          
          // En las sentencias anteriores se a�aden las claves/valor de los par�metros que se van a enviar al servidor.  
          System.out.println("*******");

          if(ServerCall.isOnline(activity)){
          loginResponse = ServerCall.LoginCall(nameValuePairs);
          }
			else{
				loginResponse = "no_network";
			}
          
          //loginResponse = ServerCall.call("http://www.alvaroabella.com/TestJSON.php", nameValuePairs);
          System.out.println(loginResponse);
          updateGUIHandler.sendEmptyMessage(0);
       }
    };

    
    
	   private Handler updateGUIHandler = new Handler() {
	       
			public void handleMessage(Message msg) {            
	      
	        	progressDialog.dismiss();
	        	EditText userText = (EditText)findViewById(R.id.txtUserName);
				EditText passText = (EditText)findViewById(R.id.txtPassword);
	        	String user = userText.getText().toString();
				String pass = passText.getText().toString();
				
				if(loginResponse.startsWith("no_network")){
	            	Context context = getApplicationContext();
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, "No Internet Connection", duration);
					toast.show();
	        		
	        	}
	        	else if(!loginResponse.startsWith("networkexception")){
					try{
						JSONObject jObject = new JSONObject(loginResponse);
			            String codeValue = jObject.getString("code");	           
			             
			            System.out.println("codeValue " + codeValue);
			            System.out.println("codeValue " + codeValue);

			            System.out.println("codeValue " + codeValue);
			            System.out.println("codeValue " + codeValue);
			            System.out.println("codeValue " + codeValue);
			            System.out.println("codeValue " + codeValue);

			            //System.out.println(jObject.getString("code").equalsIgnoreCase("1"));
			            /* Values recieved by JSON
			             *	code: 0 - user accepted
			             *	      -1 - user not found
			             *		  -2 - error data
			             *	cid: user ID
			             */
						if (jObject.getString("code").equalsIgnoreCase("-1") || jObject.getString("code").equalsIgnoreCase("-2")) {
							
							Context context = getApplicationContext();
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context,"error login", duration);
							toast.show();
						} 
						else  if (codeValue.equals("0")) {

				            	String uid 		 	= jObject.getString("uid");	
				            	String uemail 		= jObject.getString("uemail");
				            	String uname 		= jObject.getString("uname");					
				            	String usurname 	= jObject.getString("usurname");					
				            	String ubirthday 	= jObject.getString("ubirthday");					
				            	String uposition 	= jObject.getString("uposition");					

								// Save login status on memory
								SharedPreferences prefsPrivate;

								prefsPrivate = getSharedPreferences(LOGIN_PREFS,Context.MODE_PRIVATE);
								Editor prefsPrivateEditor = prefsPrivate.edit();
								prefsPrivateEditor.putString("uid", uid);
								prefsPrivateEditor.putString("uemail", uemail);
								prefsPrivateEditor.putString("ufullname", uname + " " + usurname);
								prefsPrivateEditor.putString("ubirthday", ubirthday);
								prefsPrivateEditor.putString("uposition", uposition);

						       
								prefsPrivateEditor.commit();
								setResult(RESULT_OK);

								finish();
								Intent login = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivityForResult(login, 0);

							
						} else{
			            	//System.out.println("Error en el servidor");
			            	Context context = getApplicationContext();
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, "error_servidor" , duration);
							toast.show();
			            }
					}
					catch(Exception e)
					{
		            	Context context = getApplicationContext();
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, "error_servidor", duration);
						toast.show();
						//System.out.println(e);
						//System.out.println("JSON Exception");
					}
				}
				else{
	            	//System.out.println("No internet connection");
	            	Context context = getApplicationContext();
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, "error_servidor", duration);
					toast.show();
				}
	                  
	        }
	    };
}
