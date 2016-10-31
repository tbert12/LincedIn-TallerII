package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.DatePickedEvent;
import com.fiuba.tallerii.lincedin.fragments.DatePickerDialogFragment;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;
import com.fiuba.tallerii.lincedin.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EventBus.getDefault().register(this);

        setToolbar();
        setListeners();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_signup);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setListeners() {
        setBirthdayFieldListener();
        setSubmitButtonListener();
    }

    private void setBirthdayFieldListener() {
        findViewById(R.id.signup_date_of_birth_edittext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthdayDatePickerDialog();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatePicked(DatePickedEvent event) {
        EditText birthdayTextView = (EditText) findViewById(R.id.signup_date_of_birth_edittext);
        if (birthdayTextView != null) {
            String dateOfBirth = DateUtils.parseToLocalDate(this, event.day, event.month, event.year);
            birthdayTextView.setText(dateOfBirth);
            //birthdayTextView.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void openBirthdayDatePickerDialog() {
        DialogFragment httpDialog = new DatePickerDialogFragment();
        httpDialog.show(getSupportFragmentManager(), "DatePickerDialogFragment");
    }

    private void setSubmitButtonListener() {
        findViewById(R.id.signup_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserInput();
                createUserAccount();
            }
        });
    }

    private void validateUserInput() {
        EditText firstNameEditText = (EditText) findViewById(R.id.signup_first_name_edittext);
        EditText lastNameEditText = (EditText) findViewById(R.id.signup_last_name_edittext);
        EditText birthdayTextView = (EditText) findViewById(R.id.signup_date_of_birth_edittext);
        EditText emailEditText = (EditText) findViewById(R.id.signup_email_edittext);
        EditText passwordEditText = (EditText) findViewById(R.id.signup_password_edittext);
        EditText repeatPasswordEditText = (EditText) findViewById(R.id.signup_repeat_password_edittext);


    }

    private void createUserAccount() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
