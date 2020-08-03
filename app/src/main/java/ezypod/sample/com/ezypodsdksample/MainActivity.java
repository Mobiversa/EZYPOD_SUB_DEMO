package ezypod.sample.com.ezypodsdksample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ezypod.sample.com.ezypodsdksample.CardListPayDelete.CardListActivity;


public class MainActivity extends AppCompatActivity {

    Button button_add_card, button_list;
    String response_from_sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            /*You will get this string whenever you are calling AddCardService (addCardRequest) */
            response_from_sdk = bundle.getString("response");

            Log.e("--ResponseinMainApp--", response_from_sdk);

            /*Save and Parse this response anywhere you want*/
            Toast.makeText(getApplicationContext(), response_from_sdk, Toast.LENGTH_LONG).show();
        }

        button_add_card = (Button) findViewById(R.id.button_add_card);
        button_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddCardActivity.class));
            }
        });

        button_list = (Button) findViewById(R.id.button_list);
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CardListActivity.class));
            }
        });
    }
}
