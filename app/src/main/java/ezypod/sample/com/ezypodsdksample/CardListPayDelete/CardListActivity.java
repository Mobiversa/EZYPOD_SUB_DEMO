package ezypod.sample.com.ezypodsdksample.CardListPayDelete;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mobiversa.ezypod_sdk.Service.CardService;
import com.mobiversa.ezypod_sdk.Service.VolleyUtils.NetworkErrorListener;
import com.mobiversa.ezypod_sdk.Service.VolleyUtils.NetworkSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ezypod.sample.com.ezypodsdksample.Constants;
import ezypod.sample.com.ezypodsdksample.R;

public class CardListActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    CardService cardListService;
    ListView card_listview;
    public static CardListAdapter cardListAdapter;
    ArrayList<CardData> cardDataArrayList = new ArrayList<CardData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list_layout);

        mProgressDialog = new ProgressDialog(CardListActivity.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);

        cardListService = new CardService(getApplicationContext());
        cardListAdapter = new CardListAdapter(this, R.layout.card_list_row, cardDataArrayList);
        card_listview = (ListView) findViewById(R.id.card_listview);

        try {
            mProgressDialog.show();
            JSONObject requestVal = new JSONObject();
            requestVal.put("mobileNo", "+601112212345");
            requestVal.put("masterMid", Constants.masterMid);

            cardListService.getSavedCardList(new NetworkSuccessListener<JSONObject>() {
                @Override
                public void onResponse(JSONObject responseObj) {
                    mProgressDialog.dismiss();
                    try {
                        Log.e("responseObj", responseObj.toString());
                        if (responseObj.getString("responseCode").equalsIgnoreCase("0000")) {

                            JSONObject responseData = responseObj.getJSONObject("responseData");
                            Log.v("--responseData--", responseData.toString());

                            JSONArray cardArray = responseData.getJSONArray("cardWalletList");
                            Log.v("--cardArray--", cardArray.toString());
                            for (int i = 0; i < cardArray.length(); i++) {
                                JSONObject cardArrayJSONObject = cardArray.getJSONObject(i);
                                CardData cardData = new CardData();
                                cardData.setMaskedPan(cardArrayJSONObject.getString("maskedPan"));
                                cardData.setCardHolderName(cardArrayJSONObject.getString("cardHolderName"));
                                cardData.setWalletId(cardArrayJSONObject.getString("walletId"));
                                cardData.setCardBrand(cardArrayJSONObject.getString("cardBrand"));
                                cardData.setCardScheme(cardArrayJSONObject.getString("cardScheme"));
                                cardData.setExpDate(cardArrayJSONObject.getString("expDate"));
                                cardDataArrayList.add(cardData);
                            }
                            card_listview.setAdapter(cardListAdapter);
                            cardListAdapter.notifyDataSetChanged();
                            Toast.makeText(CardListActivity.this, responseObj.toString(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CardListActivity.this, responseObj.toString(), Toast.LENGTH_SHORT).show();
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
}
