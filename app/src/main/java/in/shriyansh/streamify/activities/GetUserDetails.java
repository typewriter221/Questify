package in.shriyansh.streamify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import in.shriyansh.streamify.utils.PreferenceUtils;


import java.util.Objects;

import in.shriyansh.streamify.R;

public class GetUserDetails extends AppCompatActivity implements OnItemSelectedListener {

    private String branch;
    private String year_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner branch_spinner = (Spinner) findViewById(R.id.branch_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> branch_adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        branch_spinner.setAdapter(branch_adapter);

        Spinner year_spinner = (Spinner) findViewById(R.id.year_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
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
