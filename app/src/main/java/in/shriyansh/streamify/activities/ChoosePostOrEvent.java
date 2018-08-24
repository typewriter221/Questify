package in.shriyansh.streamify.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import in.shriyansh.streamify.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static in.shriyansh.streamify.network.Urls.EVENT_NOTIFICATION_URL;

public class ChoosePostOrEvent extends AppCompatActivity {

    private Button btn_post;
    private Button btn_notif;

    private static final String TAG = "ChoosePostOrEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_post_or_event);

        btn_post = findViewById(R.id.btn_edit_post);
        btn_notif = findViewById(R.id.btn_edit_notif);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosePostOrEvent.this,PostActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChoosePostOrEvent.this, NotifActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
