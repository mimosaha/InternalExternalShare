package com.example.internalexternalshare;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button sendText, sendMedia, sendMultipleMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendText = findViewById(R.id.send_text);
        sendMedia = findViewById(R.id.send_media);
        sendMultipleMedia = findViewById(R.id.send_multipart_media);

        sendText.setOnClickListener(this);
        sendMedia.setOnClickListener(this);
        sendMultipleMedia.setOnClickListener(this);

        Intent intent = getIntent();

        retrieveIntentTask(intent);
    }

    private void retrieveIntentTask(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    handleReceiveText(intent); // Handle text being sent
                } else {
                    handleReceiveImage(intent); // Handle single image being sent
                }
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                handleReceiveMultipleImages(intent); // Handle multiple images being sent
            } else {
                //TODO
                // Handle other intents, such as being started from the home screen
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        retrieveIntentTask(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.send_text:
                handleSendText();
                break;

            case R.id.send_media:
                handleSendMedia();
                break;

            case R.id.send_multipart_media:
                handleSendMultipleMedia();
                break;
        }

    }

    private void handleSendText() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    private void handleSendMedia() {
        String imageUri = "file:///storage/emulated/0/Pictures/SquareCamera/IMG_20181221_185143.jpg";
        Uri uri = Uri.parse(imageUri);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    private void handleSendMultipleMedia() {

        String imageUri = "file:///storage/emulated/0/Pictures/SquareCamera/IMG_20181221_185143.jpg";
        Uri uri = Uri.parse(imageUri);

        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        imageUris.add(uri); // Add your image URIs here

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("*/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }

    void handleReceiveText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Log.v("MIMO_SAHA: ", "SharedText: " + sharedText);
            // Update UI to reflect text being shared
        }
    }

    void handleReceiveImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.v("MIMO_SAHA: ", "ImageUri: " + imageUri);
            // Update UI to reflect image being shared
        }
    }

    void handleReceiveMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            Log.v("MIMO_SAHA: ", "ImageUri Set: " + imageUris.size());
            // Update UI to reflect multiple images being shared
        }
    }


}
