package ezypod.sample.com.ezypodsdksample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobiversa.ezypod_sdk.Service.AddCardService;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCardActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_submit;
    private ProgressDialog mProgressDialog;
    private EditText edit_latitude, edit_longitude, edit_mobileNo,
            edit_nameOnCard, edit_cardNumber, edit_cardCVV, edit_cardExpirymonth, edit_cardExpiryYear, edit_email;

    String mobiApiKey = "132fe8ed2715bc0fb4fe16c55acbd6d4";
    String username = "Mobiversa";
    String masterMid = "000777700007777"; // for demo
//    String mobiApiKey = "2e4b4ed48dcfb55ac0bb62185721dd30";
//    String username = "SACK40059";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_layout);

        mProgressDialog = new ProgressDialog(AddCardActivity.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);

        edit_latitude = (EditText) findViewById(R.id.edit_latitude);
        edit_longitude = (EditText) findViewById(R.id.edit_longitude);
        edit_mobileNo = (EditText) findViewById(R.id.edit_mobileNo);
        edit_nameOnCard = (EditText) findViewById(R.id.edit_nameOnCard);
        edit_cardNumber = (EditText) findViewById(R.id.edit_cardNumber);
        edit_cardCVV = (EditText) findViewById(R.id.edit_cardCVV);
        edit_cardExpirymonth = (EditText) findViewById(R.id.edit_cardExpirymonth);
        edit_cardExpiryYear = (EditText) findViewById(R.id.edit_cardExpiryYear);
        edit_email = (EditText) findViewById(R.id.edit_email);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        edit_latitude.setText("");
        edit_longitude.setText("");
        edit_mobileNo.setText("+601112212345");
        edit_nameOnCard.setText("Mobiversa");
        edit_cardNumber.setText("4918914107195005");
        edit_cardCVV.setText("123");
        edit_cardExpirymonth.setText("03");
        edit_cardExpiryYear.setText("23");
        edit_email.setText("premkumar@mobiversa.com");
    }
    @Override
    public void onClick(View v) {
        if (v == btn_submit) {
            try {
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
                mProgressDialog.setMessage("Please wait...");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JSONObject requestJSON = null;
            try {
                //Form the request as JSONObject
                requestJSON = new JSONObject();
                /*String amount = "1.00";
                String amount_to_send = String.format("%012d", Integer.parseInt(amount.replace(".", "")));
                requestJSON.put("amount", amount_to_send);*/
                requestJSON.put("latitude", edit_latitude.getText().toString());
                requestJSON.put("longitude", edit_longitude.getText().toString());
                requestJSON.put("mobileNo", edit_mobileNo.getText().toString());
                requestJSON.put("nameOnCard", edit_nameOnCard.getText().toString());
                requestJSON.put("email", edit_email.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String CardNumber = edit_cardNumber.getText().toString();
            String CVVNumber = edit_cardCVV.getText().toString();
            String ExpiryMonth = edit_cardExpirymonth.getText().toString();
            String ExpiryYear = edit_cardExpiryYear.getText().toString();
            //Year should be last 2 digit of the year e.g., if the year is 2019 You need to send it as 19

            mProgressDialog.dismiss();
            //Finally you pass all the parameters in the below function and this data will be sent to SDK and the SDK will process your request.
            AddCardService loginService1 = new AddCardService(AddCardActivity.this);
            loginService1.addCardRequest(requestJSON, mobiApiKey,masterMid, username, CardNumber, CVVNumber, ExpiryMonth, ExpiryYear);
        }
    }
}
