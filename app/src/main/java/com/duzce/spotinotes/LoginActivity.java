package com.duzce.spotinotes;

import static com.duzce.spotinotes.db.token.TokenManager.getAccessToken;
import static com.duzce.spotinotes.db.token.TokenManager.saveAccessToken;
import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "cb7e5dfd73d74ef4bfae3d2a42c5b6c5";

    private static final String REDIRECT_URI = "com.duzce.spotinotes://callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getAccessToken(this) != null) {
            RedirectToApp();
        } else {
            Button btn = findViewById(R.id.LoginButton);
            btn.setOnClickListener(v -> {
                AuthorizationRequest.Builder builder =
                        new AuthorizationRequest.Builder(
                                CLIENT_ID,
                                AuthorizationResponse.Type.TOKEN,
                                REDIRECT_URI);
                builder.setScopes(new String[]{
                        "streaming",
                        "user-read-playback-state",
                        "user-modify-playback-state",
                        "user-read-currently-playing",
                        "app-remote-control",
                        "user-library-modify",
                        "user-library-read",
                        "user-read-email",
                        "user-read-private"});
                AuthorizationRequest request = builder.build();
                AuthorizationClient.openLoginActivity(LoginActivity.this, REQUEST_CODE, request);
            });
        }
    }

    // Handle redirection and get access token etc
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                case TOKEN:
                    saveAccessToken(
                            this,
                            response.getAccessToken(),
                            System.currentTimeMillis() + (response.getExpiresIn() * 1000L)
                    );
                    RedirectToApp();
                    break;
                case ERROR:
                    // TODO
                    break;
                default:
            }
        }
    }

    private void RedirectToApp() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}