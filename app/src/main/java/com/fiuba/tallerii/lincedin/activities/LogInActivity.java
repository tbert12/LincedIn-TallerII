package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.UserAccount;
import com.fiuba.tallerii.lincedin.model.UserAccountManager;
import com.google.gson.Gson;

import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";

    private static CallbackManager callbackManager = CallbackManager.Factory.create();

    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setFacebookAccessTokenTracker();
        setListeners();
    }

    private void setListeners() {
        setCancelButtonListener();
        setLincedInLogInButtonListener();
        setFacebookLogInButtonListener();
        setCreateAccountTextListener();
    }

    private void setCancelButtonListener() {
        findViewById(R.id.login_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setLincedInLogInButtonListener() {
        // TODO: 30/10/16 Validate email and password and make log in request to app server.
    }

    private void setFacebookLogInButtonListener() {
        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.login_facebook_button);
        if (fbLoginButton == null) {
            return;
        }

        fbLoginButton.setReadPermissions("email", "public_profile", "user_location", "user_birthday");
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook login callback success.");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                if (response.getError() != null) {
                                    Log.d(TAG, "Facebook profile Graph request error!");
                                } else {
                                    Log.d(TAG, "Facebook profile Graph request success.");
                                    String jsonResult = new Gson().toJson(json);
                                    Log.d(TAG, jsonResult);
                                    UserAccountManager.createUserAccountFromFacebookResponse(json);
                                    logInUser();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, first_name, last_name, email, birthday, link, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook login callback cancelled.");
                // TODO: 15/10/16 Decide what to do here.
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Facebook login callback error!");
                exception.printStackTrace();
                // TODO: 15/10/16 Decide what to do here.
            }
        });
    }

    private void setCreateAccountTextListener() {
        findViewById(R.id.login_has_account_textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });
    }

    private void openSignUpActivity() {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    private void logInUser() {
        UserAccountManager.logIn(this);
    }

    private void setFacebookAccessTokenTracker() {
        final UserAccount userAccount = UserAccountManager.getUserAccount();

        // If the access token is available already assign it.
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "Facebook AccessToken: " + (accessToken != null ? accessToken.getToken() : "null"));
        if (accessToken != null) {
            UserAccountManager.updateSessionToken(accessToken.getToken());
        }

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                Log.i(TAG, "Facebook access token has changed.");
                Log.d(TAG, "New Facebook AccessToken: " + (currentAccessToken != null ? currentAccessToken.getToken() : "null"));
                if (currentAccessToken != null) {
                    UserAccountManager.updateSessionToken(currentAccessToken.getToken());
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}