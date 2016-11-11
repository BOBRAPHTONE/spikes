package com.novoda.tpbot.landing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.novoda.tpbot.R;
import com.novoda.tpbot.human.HumanActivity;
import com.novoda.tpbot.bot.BotActivity;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tp_bot);

        View humanSelection = findViewById(R.id.human_selection);
        View botSelection = findViewById(R.id.bot_selection);

        humanSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, HumanActivity.class);
                startActivity(intent);
            }
        });

        botSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, BotActivity.class);
                startActivity(intent);
            }
        });
    }

}
