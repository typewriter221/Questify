package in.shriyansh.streamify.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.ui.LabelledSpinner;

public class RegisterTeam extends AppCompatActivity {

    private String[] events_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        events_array = getResources().getStringArray(R.array.events_array);

        LabelledSpinner branch_spinner = findViewById(R.id.events_spinner);
        ArrayAdapter<String> branch_adapter =
                new ArrayAdapter<>(RegisterTeam.this,
                        android.R.layout.simple_dropdown_item_1line, events_array);
        branch_spinner.setCustomAdapter(branch_adapter);
    }
}
