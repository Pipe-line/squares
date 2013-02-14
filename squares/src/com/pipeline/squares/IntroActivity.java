package com.pipeline.squares;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockActivity;

public class IntroActivity extends SherlockActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        /*
         * Action Bar
         */
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("square");

        //Register Button
        Button login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent login = new Intent(IntroActivity.this, LoginActivity.class);
                startActivityForResult(login, 0);

            }
        });

        //Register Button
        Button register = (Button) findViewById(R.id.register_btn);
        register.setOnClickListener(new
        OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent login = new Intent(IntroActivity.this, RegisterActivity.class);
                startActivityForResult(login, 0);

            }
        });

    }
}
