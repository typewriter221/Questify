package in.shriyansh.streamify.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import in.shriyansh.streamify.R;
import in.shriyansh.streamify.utils.PreferenceUtils;

public class ChooseEvent extends AppCompatActivity {

    private RadioGroup events_radio;
    private String[] events;
    private RadioButton[] event;
    private Button btn_reg_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        events_radio = findViewById(R.id.events_radio);
        events = getResources().getStringArray(R.array.events_array);
        event = new RadioButton[events.length];

        for(int i=0; i<events.length; i++) {
            event[i] = new RadioButton(this);
            event[i].setText(events[i]);
            event[i].setId(i);
            event[i].setTextColor(Color.WHITE);
            events_radio.addView(event[i]);
        }

        btn_reg_event = findViewById(R.id.btn_event_reg);
        btn_reg_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int event_id = events_radio.getCheckedRadioButtonId();
                PreferenceUtils.setIntegerPreference(ChooseEvent.this, PreferenceUtils.PREF_USER_EVENT, event_id);

                Intent intent = new Intent(ChooseEvent.this, ChooseNumberMembers.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
