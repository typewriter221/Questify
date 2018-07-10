package in.shriyansh.streamify.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import in.shriyansh.streamify.ui.LabelledSpinner;
import in.shriyansh.streamify.utils.PreferenceUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import in.shriyansh.streamify.R;

public class GetUserDetails extends AppCompatActivity {

    private String branch;
    private String year_join;
    public static final int PICK_IMAGE = 1;
    private EditText rolledit;
    private String[] branch_array;
    private String[] year_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        branch_array = getResources().getStringArray(R.array.branch_array);
        year_array = getResources().getStringArray(R.array.year_array);

        LabelledSpinner branch_spinner = findViewById(R.id.branch_spinner);
        ArrayAdapter<String> branch_adapter =
                new ArrayAdapter<>(GetUserDetails.this,
                        android.R.layout.simple_dropdown_item_1line, branch_array);
        branch_spinner.setCustomAdapter(branch_adapter);

        LabelledSpinner year_spinner = findViewById(R.id.year_spinner);
        ArrayAdapter<String> year_adapter =
                new ArrayAdapter<>(GetUserDetails.this,
                        android.R.layout.simple_dropdown_item_1line, year_array);
        year_spinner.setCustomAdapter(year_adapter);

        Button submit_button = (Button) findViewById(R.id.btn_submit);
        rolledit = findViewById(R.id.roll_edittext);

        PreferenceUtils.setBooleanPreference(GetUserDetails.this,
                PreferenceUtils.PREF_IS_DETAILS_REGISTERED,false);

        setListeners();

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PreferenceUtils.getBooleanPreference(GetUserDetails.this, PreferenceUtils.PREF_IS_DETAILS_REGISTERED)) {
                    Intent intent = new Intent(
                            GetUserDetails.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    checkCreds();
                }
            }
        });

        Button upload_btn = findViewById(R.id.pick_photo);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });


    }

    private void checkCreds() {

        boolean cancel = false;
        View focusView = null;

        if (rolledit.getText().toString().length()==0) {
            rolledit.setError(Html.fromHtml(
                    "<font color='#ffffff'>Roll No. cannot be empty !</font>"));
            focusView = rolledit;
            cancel = true;
        }


        if (cancel) {
            focusView.requestFocus();
        }
        else {
            PreferenceUtils.setBooleanPreference(GetUserDetails.this,
                    PreferenceUtils.PREF_IS_DETAILS_REGISTERED,true);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView user_img = findViewById(R.id.user_photo);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    user_img.setImageBitmap(selectedImage);

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    // Create imageDir
                    File mypath=new File(directory,"profile.jpg");

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mypath);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //return directory.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(GetUserDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(GetUserDetails.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setListeners(){

        LabelledSpinner branch_spinner = findViewById(R.id.branch_spinner);
        LabelledSpinner year_spinner = findViewById(R.id.year_spinner);

        branch_array = getResources().getStringArray(R.array.branch_array);
        year_array = getResources().getStringArray(R.array.year_array);


        branch_spinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                branch = branch_array[position];

                PreferenceUtils.setStringPreference(GetUserDetails.this,
                        PreferenceUtils.PREF_USER_BRANCH, branch);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
                branch = branch_array[0];
            }
        });


        year_spinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                year_join = year_array[position];

                PreferenceUtils.setStringPreference(GetUserDetails.this,
                        PreferenceUtils.PREF_USER_YEAR_JOIN, year_join);
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
                year_join = year_array[0];
            }
        });
    }


}
