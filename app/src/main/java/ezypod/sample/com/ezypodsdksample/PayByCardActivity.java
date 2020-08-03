package ezypod.sample.com.ezypodsdksample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mobiversa.ezypod_sdk.Service.CardService;
import com.mobiversa.ezypod_sdk.Service.VolleyUtils.NetworkErrorListener;
import com.mobiversa.ezypod_sdk.Service.VolleyUtils.NetworkSuccessListener;

import org.json.JSONObject;

public class PayByCardActivity extends AppCompatActivity {

    EditText edit_amount, edit_mobileNo, edit_invoiceId;
    CardService cardListService;
    Button button_pay;
    String from_Cardadapterclass;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);

        mProgressDialog = new ProgressDialog(PayByCardActivity.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);

        button_pay = (Button) findViewById(R.id.button_pay);
        edit_amount = (EditText) findViewById(R.id.edit_amount);
        edit_mobileNo = (EditText) findViewById(R.id.edit_mobileNo);
        edit_invoiceId = (EditText) findViewById(R.id.edit_invoiceId);

        cardListService = new CardService(getApplicationContext());

        edit_mobileNo.setText("+601112212345");
        edit_amount.setText("2.00");
        edit_invoiceId.setText("test");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            from_Cardadapterclass = bundle.getString("walletId");

            Log.e("--ResponseinMainApp--", from_Cardadapterclass);

            /*Save and Parse this response anywhere you want*/
            Toast.makeText(getApplicationContext(), "walletID from Adapter Class:   "+ from_Cardadapterclass, Toast.LENGTH_LONG).show();
        }

        button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mProgressDialog.show();
                    JSONObject requestVal = new JSONObject();
                    requestVal.put("masterMid",  Constants.masterMid );
                    requestVal.put("mobileNo", edit_mobileNo.getText().toString());
                    requestVal.put("cardToken", from_Cardadapterclass);
                    String amount_to_send = String.format("%012d", Integer.parseInt(edit_amount.getText().toString().replace(".", "")));
                    requestVal.put("amount", amount_to_send);
                    requestVal.put("invoiceId", edit_invoiceId.getText().toString());

                    cardListService.payByCard(new NetworkSuccessListener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseObj) {
                            mProgressDialog.dismiss();
                            try {
                                Log.e("SaleresponseObj", responseObj.toString());
                                if (responseObj.getString("responseCode").equalsIgnoreCase("0000")) {

                                    JSONObject responseData = responseObj.getJSONObject("responseData");
                                    Log.v("--SaleResponseData--", responseData.toString());
                                    Toast.makeText(PayByCardActivity.this, responseObj.toString(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(PayByCardActivity.this, responseObj.toString(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new NetworkErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            mProgressDialog.dismiss();
                        }
                    }, requestVal, Constants.mobiApiKey, Constants.username);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
