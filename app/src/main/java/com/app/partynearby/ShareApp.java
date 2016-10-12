package com.app.partynearby;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.utility.AppLog;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by ram on 01/06/16.
 */
public class ShareApp extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageButton whatsapp_rl, facebook_rl, email_rl, message_rl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_app);

        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_share));
        }

        initWidget();

    }


    private void initWidget() {
        whatsapp_rl = (ImageButton) findViewById(R.id.whatsapp_rl);
        facebook_rl = (ImageButton) findViewById(R.id.facebook_rl);
        email_rl = (ImageButton) findViewById(R.id.email_rl);
        message_rl = (ImageButton) findViewById(R.id.message_rl);

        whatsapp_rl.setOnClickListener(this);
        facebook_rl.setOnClickListener(this);
        email_rl.setOnClickListener(this);
        message_rl.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);

        MenuItem item3;
        item3 = menu.findItem(R.id.write_review);
        item3.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        Intent sendIntent = null;
        switch (v.getId()) {

            case R.id.whatsapp_rl:

                sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Pubs Nearby.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;
            case R.id.facebook_rl:
                setupFacebookShareIntent();
                break;

            case R.id.email_rl:
                sendEmail();
                break;
            case R.id.message_rl:
                sendIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + "" ) );
                sendIntent.putExtra( "sms_body", "Test text..." );
                startActivity( sendIntent );
                break;
            default:
                break;
        }
    }

    public void setupFacebookShareIntent() {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Title")
                .setContentDescription(
                        "\"Pubs Nearby app\"")
                .setContentUrl(Uri.parse("http://someurl.com/here"))
                .build();

        shareDialog.show(linkContent);
    }

    protected void sendEmail() {
        AppLog.Log("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Pubs Nearby");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            AppLog.Log("Finished sending email...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
