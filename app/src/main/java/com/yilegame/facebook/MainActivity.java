package com.yilegame.facebook;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private Button login;
    private CallbackManager callBackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.yilegame.facebook",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        callBackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        LoginManager.getInstance().registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this,"管理器登陆成功,数据token"+loginResult.getAccessToken().getToken(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this,"管理器登陆取消",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,"管理器登录失败",Toast.LENGTH_SHORT).show();

            }
        });
//        loginButton.registerCallback(callBackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(MainActivity.this,"登陆取消",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
//            }
//        });

        login = (Button) findViewById(R.id.login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBackManager.onActivityResult(requestCode,resultCode,data);
    }
    public void login(View view){
        if (view.getId() ==R.id.login){
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken == null || accessToken.isExpired()) {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
            }
        }

    }
}
