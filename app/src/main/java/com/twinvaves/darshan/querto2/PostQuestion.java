package com.twinvaves.darshan.querto2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.richedit.ColorPicker;
import com.commonsware.cwac.richedit.ColorPickerOperation;
import com.commonsware.cwac.richedit.Effect;
import com.commonsware.cwac.richedit.RichEditText;
import com.commonsware.cwac.richtextutils.SpannedXhtmlGenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class PostQuestion extends AppCompatActivity implements ColorPicker, View.OnClickListener {

    ImageView bold, italic, underline, pic;
//    boolean boldBoolean = false, italicBoolean = false, underlineBoolean = false;
    RichEditText richEditText;
    FloatingActionButton fab;
    private static final int RESULT_LOAD_IMAGE = 1234;
    private static final int REQUEST_IMAGE_CAPTURE = 0000;
    private static final int SPEECH_REQUEST_CODE = 2222;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);
        init();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable e = richEditText.getText();
                String s2 = Html.toHtml(e);
                Log.d("darshan", s2);
                Spanned s3 = Html.fromHtml(s2);
                displaySpeechRecognizer();
            }
        });
        bold.setOnClickListener(this);
        italic.setOnClickListener(this);
        underline.setOnClickListener(this);
        pic.setOnClickListener(this);

    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"This feature is not compitable with your device",Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {

        bold = (ImageView) findViewById(R.id.bold);
        italic = (ImageView) findViewById(R.id.italic);
        underline = (ImageView) findViewById(R.id.underline);
        richEditText = (RichEditText) findViewById(R.id.questionTv);
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        pic = (ImageView) findViewById(R.id.picture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean pick(ColorPickerOperation colorPickerOperation) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bold:
                richEditText.toggleEffect(RichEditText.BOLD);

            case R.id.italic:
               richEditText.toggleEffect(RichEditText.ITALIC);

            case R.id.underline:
                richEditText.toggleEffect(RichEditText.UNDERLINE);
                break;
            case R.id.picture:
                callCamera();
                break;
        }
    }

    private void callCamera() {
        String[] items = {"Camera","Gallery"};
        new AlertDialog.Builder(PostQuestion.this).setTitle("Select ").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        selectPicture();
                        break;
                }
            }
        }).show();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("Exception",ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void selectPicture() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {



        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            //add function to display and send the data
            /*load_image("file://" + picturePath, imageView);
            file = saveBitmap(bm);*/
        }
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            String present = Html.toHtml(richEditText.getText());
            richEditText.setText(Html.fromHtml(present)+spokenText);
        }
    }

}
