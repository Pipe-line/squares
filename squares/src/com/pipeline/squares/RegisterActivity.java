package com.pipeline.squares;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import com.pipeline.squares.cache.FileCache;
import com.pipeline.squares.cache.Utils;
import com.pipeline.squares.crypto.Base64;
import com.pipeline.squares.servercalls.ServerCall;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private Activity activity = this;
	private Context mContext = this;
	
	private TextView mDateDisplay;
	private TextView mGenderDisplay;
	AlertDialog alert = null;

	static final int DATE_DIALOG_ID = 0;
	private int mYear;
	private int mMonth;
	private int mDay;

	ProgressDialog progressDialog = null;
	String registerResponse = "";	
    public static final String LOGIN_PREFS = "LOGIN_PREFS";
    
	private EditText nameText;
	private EditText snameText;
	private static EditText emailText;	
	private EditText passText;
	private EditText dateText;
	private EditText genderText;
	private EditText positionText;
	private static String imgBase64;
	
	//Image Picker
	private static final int CAMERA_REQUEST = 1888;
	private static final int SELECT_PICTURE = 1;
	private ImageView mImgAvatar = null;
	private String selectedImagePath = null;
	private Uri selectedImageUri = null;
	private String filemanagerstring;
	private ImageView profileImage = null;
	private ImageView profileImageUser = null;
	private static FileCache f = null;
	private static String imageEmailName = "";
	private static String imageName = "";
    private Bitmap AVATAR_IMAGE;

    
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);

		
		
		/********************************
		 *  Date Time Picker
		 *  
		 ********************************/
		mDateDisplay = (TextView) findViewById(R.id.editDate);
		mDateDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});

		// get the current date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		// display the current date
		updateDisplay();
		// EOF - Date Time Picker

		/*******************************
		 *  Gender Edit Text Box
		 *
		 ********************************/
		final CharSequence[] items = { "Male", "Female", "Not sure" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick a color");
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						mGenderDisplay.setText(items[item]);
						alert.dismiss();
					}
				});
		alert = builder.create();

		mGenderDisplay = (TextView) findViewById(R.id.editGender);
		mGenderDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alert.show();
			}
		});
		// EOF - Gender Edit Text Box

		
		/*******************************
		 *  Image picker
		 *
		 ********************************/
		
		final Dialog dialog = new Dialog(this);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.avatar_dialog);
		dialog.setCancelable(true);
		
		profileImage = (ImageView) findViewById(R.id.imgAvatar);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.avatar_frame);
		profileImage.setBackgroundDrawable(drawable);
		profileImageUser = (ImageView) findViewById(R.id.avatarImageUser);
		
		//Onclick on ImageView
		mImgAvatar = (ImageView) findViewById(R.id.imgAvatar);
		mImgAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});

		//Camera 
		Button btnCamera = (Button) dialog.findViewById(R.id.openCamera);
		btnCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
				dialog.dismiss();
			}
		});

		//Gallery
		Button btnGallery = (Button) dialog.findViewById(R.id.openGallery);
		btnGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);
				dialog.dismiss();
			}
		});
		
		
		
		/*******************************
		 *  Register button
		 *
		 ********************************/		
		final Button next = (Button) findViewById(R.id.buttonNext);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//Close Keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(next.getWindowToken(), 0);
				
				nameText = (EditText) findViewById(R.id.editName);
				snameText = (EditText) findViewById(R.id.editSurname);
				emailText = (EditText) findViewById(R.id.editEmail);
				passText = (EditText) findViewById(R.id.editPassword);
				dateText = (EditText) findViewById(R.id.editDate);
				genderText = (EditText) findViewById(R.id.editGender);
				positionText = (EditText) findViewById(R.id.editPosition);
				 
				 
				if (checkEmail(emailText.getText().toString()) &
						nameText.getText().length() != 0  	& 
						snameText.getText().length() != 0 	& 
						passText.getText().length() != 0 	&
						dateText.getText().length() != 0  	& 
						positionText.getText().length() != 0  	& 
						genderText.getText().length() != 0)
				{  
					imageEmailName = emailText.getText().toString();
					progressDialog = ProgressDialog.show(RegisterActivity.this, null, getString(R.string.registrando), true);
					progressDialog.setCancelable(true);

					Thread thread =  new Thread(null, registerAction);
	            	thread.start();
				}
				else
				{
					Context context = getApplicationContext();
				      int duration = Toast.LENGTH_SHORT;
				      Toast toast = Toast.makeText(context, R.string.error_registro, duration);
				      toast.show();

				}
				 
			}
		});
	}

	// Date time picker
	private void updateDisplay() {
		mDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-").append(mDay).append("-")
				.append(mYear).append(" "));
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}
	// EOF - Date Time Picker
	
	
	/**************************
	 * Email Validation
	 * 	
	 ***************************/
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	private boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	/**************************
	 * onActivityResult 
	 * Get and process the image picked by user
	 * 
	 ***************************/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			
			if (requestCode == SELECT_PICTURE) {

				selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 10;
				Bitmap photo = null;
				//Fix to some file explorers like AstroFile 
				if (selectedImagePath != null) {
					photo = BitmapFactory
							.decodeFile(selectedImagePath, options);
				} else {
					photo = BitmapFactory
							.decodeFile(filemanagerstring, options);
				}

				AVATAR_IMAGE = getRoundedCornerBitmap(photo);

				
				mImgAvatar.setImageBitmap(AVATAR_IMAGE);
			}

			if (requestCode == CAMERA_REQUEST) {
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				AVATAR_IMAGE = getRoundedCornerBitmap(photo);
				
				
				mImgAvatar.setImageBitmap(AVATAR_IMAGE);
			}
		} else {

		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		
		int width = 0;
		int height = 0;

		if (bitmap.getWidth() < bitmap.getHeight()) {
			width = bitmap.getWidth();
			height = bitmap.getWidth();
		} else {
			width = bitmap.getHeight();
			height = bitmap.getHeight();
		}

		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);
		final float roundPx = 0;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		output.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		
		byte[] image = baos.toByteArray();
		byte[] encodeString = Base64.encode(image);
		imgBase64 = new String(encodeString);

		try {
			baos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;
	}
		
	/**************************
	 * Register Action
	 * 	Send data to server
	 ***************************/
	Runnable registerAction = new Runnable() {
		@Override
		public void run() {
	        	
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("email", emailText.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("password", passText.getText().toString()));                          
	          nameValuePairs.add(new BasicNameValuePair("name", nameText.getText().toString()));                          
	          nameValuePairs.add(new BasicNameValuePair("surname", snameText.getText().toString()));                          
	          nameValuePairs.add(new BasicNameValuePair("birthday", dateText.getText().toString()));                          
	          nameValuePairs.add(new BasicNameValuePair("gender", genderText.getText().toString()));                          
	          nameValuePairs.add(new BasicNameValuePair("position", positionText.getText().toString()));
			if (imgBase64 != null) {
				nameValuePairs.add(new BasicNameValuePair("image", imgBase64));
			}
	          // En las sentencias anteriores se a�aden las claves/valor de los par�metros que se van a enviar al servidor.

			if(ServerCall.isOnline(activity)){
	          registerResponse = ServerCall.RegisterCall(nameValuePairs);
			}
			else{
				registerResponse = "no_network";
			}
	          //System.out.println(registerResponse);
	          updateGUIHandler.sendEmptyMessage(0);
	       }
	    };

	    private Handler updateGUIHandler = new Handler() {
	       
			public void handleMessage(Message msg) {            
	      
	        	progressDialog.dismiss();
	        	
	        	if(registerResponse.startsWith("no_network")){
	            	Context context = getApplicationContext();
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, "No Internet Connection", duration);
					toast.show();
	        		
	        	}
	        	else if(!registerResponse.startsWith("networkexception")){
					try{
						JSONObject jObject = new JSONObject(registerResponse);
			            String codeValue = jObject.getString("code");	            //System.out.println(codeValue);
		            
			            
			            if(codeValue.equals("0")  ){	
			            	
			            	SharedPreferences prefsPrivate;
			            	String uid 		 = jObject.getString("uid");				//System.out.println(cid);       
							prefsPrivate = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
							Editor prefsPrivateEditor = prefsPrivate.edit();
							prefsPrivateEditor.putString("uid", uid);
							prefsPrivateEditor.putString("ufullname", nameText.getText().toString()+" "+snameText.getText().toString());
							prefsPrivateEditor.putString("uposition", positionText.getText().toString());
							prefsPrivateEditor.putString("uemail", emailText.getText().toString());
							prefsPrivateEditor.putString("ubirthday", dateText.getText().toString());

							prefsPrivateEditor.commit();
							
							setResult(RESULT_OK);
							

							//Save to SD
							f = new FileCache(mContext);
							imageName = Utils.md5(imageEmailName) + ".jpg";
							f.saveFile(imageName, AVATAR_IMAGE);

							finish();
		                	Intent register = new Intent(RegisterActivity.this, MainActivity.class);
		                	startActivityForResult(register, 0);
		                	
			            }
			            else if(codeValue.equals("-5")){
							
			            	Context context = getApplicationContext();
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, "Email duplicado", duration);
							toast.show();
							
			            }
					}
					catch(Exception e){
						
		            	Context context = getApplicationContext();
						int duration = Toast.LENGTH_LONG;
						Toast toast = Toast.makeText(context, R.string.error_servidor, duration);
						toast.show();
					}
				}else{
					
	            	Context context = getApplicationContext();
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, R.string.error_servidor, duration);
					toast.show();
				}
	        }
	    };

}
