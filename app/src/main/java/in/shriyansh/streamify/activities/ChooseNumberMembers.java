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

public class ChooseNumberMembers extends AppCompatActivity {

    private Button btn_choose_mem_num;
    private RadioGroup mem_num_opts;
    private RadioButton[] mem_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_number_members);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mem_num_opts = findViewById(R.id.mem_num_opts);
        mem_num = new RadioButton[5];

        for (int i=0; i<5; i++) {
            mem_num[i] = new RadioButton(this);
            mem_num[i].setText(Integer.toString(i+1));
            mem_num[i].setId(i+1);
            mem_num[i].setTextColor(Color.WHITE);
            mem_num[i].setTextSize(getResources().getDimension(R.dimen.radioTextSize));
            mem_num_opts.addView(mem_num[i]);
        }

        btn_choose_mem_num = findViewById(R.id.btn_choose_mem_num);
        btn_choose_mem_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mem_num = mem_num_opts.getCheckedRadioButtonId();
                PreferenceUtils.setIntegerPreference(ChooseNumberMembers.this, PreferenceUtils.PREF_MEM_NUM, mem_num);

                Intent intent = new Intent(ChooseNumberMembers.this, RegisterTeam.class);
                startActivity(intent);
                finish();

            }
        });

    }

}
