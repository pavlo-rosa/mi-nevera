package com.minevera.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.minevera.Controller;
import com.minevera.R;
//ok
public class SendInvitationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = SendInvitationActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;

    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invitation);
        getSupportActionBar().setTitle("Invitar a..");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create an auto-managed GoogleApiClient with acccess to App Invites.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .build();

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                // Because autoLaunchDeepLink = true we don't have to do anything
                                // here, but we could set that to false and manually choose
                                // an Activity to launch to handle the deep link here.
                            }
                        });
    }
    // [END on_create]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        showMessage(getString(R.string.google_play_services_error));
    }

    @Override
    protected void onStart() {
        super.onStart();
        onInviteClicked();
    }

    /**
     * User has clicked the 'Invite' button, launch the invitation UI with the proper
     * <a href="http://es.tinypic.com?ref=2md0fk" target="_blank">
     * title, message, and deep link
     */
    // [START on_invite_clicked]
    private void onInviteClicked() {
       String message = "<br><br>ID Grupo de Hogar: "+ Controller.gh.getId()+" <br><br>Contraseña: "+ Controller.gh.getPassword()+
               "<br><br><a href=\"https://play.google.com/store/apps?hl=es\" target=\"_blank\"><img src=\"http://i67.tinypic.com/2ins77.png\" border=\"0\" alt=\"Image Mi Nevera\"></a> " +
               "<br><a href=\"https://play.google.com/store/apps?hl=es\"><br>Descargar Mi Nevera</a>";
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message1))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setEmailSubject("Mi Nevera")
                .setEmailHtmlContent(getString(R.string.invitation_message2)+message)

                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    // [END on_invite_clicked]  .setCallToActionText(getString(R.string.invitation_cta))
// .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))href="https://play.google.com/store/apps?hl=es"
    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and log a message
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
                finish();
            } else {
                // Sending failed or it was canceled, show failure message to the user
                //showMessage(getString(R.string.send_failed));
                Intent dataP = new Intent();
                dataP.setData(Uri.parse("RESULT_FAILED"));
                setResult(RESULT_CANCELED,dataP);
                finish();
            }
        }
    }

    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.;
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
