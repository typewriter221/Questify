package in.shriyansh.streamify.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import in.shriyansh.streamify.utils.PreferenceUtils;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import in.shriyansh.streamify.R;

public class GetUserDetails extends AppCompatActivity implements OnItemSelectedListener {

    private String branch;
    private String year_join;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner branch_spinner = (Spinner) findViewById(R.id.branch_spinner);
        ArrayAdapter<CharSequence> branch_adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_array, android.R.layout.simple_spinner_item);
        branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch_spinner.setAdapter(branch_adapter);

        Spinner year_spinner = (Spinner) findViewById(R.id.year_spinner);
        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_adapter);

        Button submit_button = (Button) findViewById(R.id.btn_submit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        GetUserDetails.this,MainActivity.class);
                startActivity(intent);
                finish();
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(GetUserDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(GetUserDetails.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner branch_spinner = (Spinner) findViewById(R.id.branch_spinner);
        Spinner year_spinner = (Spinner) findViewById(R.id.year_spinner);

        if(Objects.equals(id, "branch_spinner")){
            branch = branch_spinner.getSelectedItem().toString();
        }
        else{
            year_join = year_spinner.getSelectedItem().toString();
        }

        PreferenceUtils.setStringPreference(GetUserDetails.this,
                PreferenceUtils.PREF_USER_BRANCH, branch);
        PreferenceUtils.setStringPreference(GetUserDetails.this,
                PreferenceUtils.PREF_USER_YEAR_JOIN, year_join);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
