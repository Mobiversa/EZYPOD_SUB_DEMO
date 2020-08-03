package ezypod.sample.com.ezypodsdksample.CardListPayDelete;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mobiversa.ezypod_sdk.Service.CardService;
import com.mobiversa.ezypod_sdk.Service.VolleyUtils.NetworkErrorListener;
import com.mobiversa.ezypod_sdk.Service.VolleyUtils.NetworkSuccessListener;

import org.json.JSONObject;

import java.util.List;

import ezypod.sample.com.ezypodsdksample.Constants;
import ezypod.sample.com.ezypodsdksample.PayByCardActivity;
import ezypod.sample.com.ezypodsdksample.R;

public class CardListAdapter extends ArrayAdapter<CardData> {

    List<CardData> cardData;

    Context context;

    int res;

    private ProgressDialog mProgressDialog;

    CardService cardListService;

    public CardListAdapter(Context context, int resource, List<CardData> cardData) {
        super(context, resource, cardData);
        this.context = context;
        this.res = resource;
        this.cardData = cardData;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view;

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /* We inflate the xml which gives us a view */
        view = inflater.inflate(R.layout.card_list_row, parent, false);

        /* Get the item in the adapter */
        final CardData myObject = getItem(position);

        /* Get the widget with id name which is defined in the xml of the row */
        TextView walletId = (TextView) view.findViewById(R.id.text_walletId);
        TextView maskedPan = (TextView) view.findViewById(R.id.text_maskedPan);
        TextView expDate = (TextView) view.findViewById(R.id.text_expDate);
        Button button_pay = (Button) view.findViewById(R.id.button_pay);
        Button button_delete = (Button) view.findViewById(R.id.button_delete);

        /* Populate the row's xml with info from the item */
        walletId.setText("Wallet ID:   " + myObject.getWalletId());
        expDate.setText("Exp Date:   " + myObject.getExpDate());
        maskedPan.setText("MaskedPan:   " + myObject.getMaskedPan());

        cardListService = new CardService(context);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setCancelable(false);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("--walletIdPosition--", myObject.getWalletId());
                removeCard(myObject.getWalletId());
            }
        });

        button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PayByCardActivity.class)
                        .putExtra("walletId", myObject.getWalletId()));
            }
        });
        /* Return the generated view */
        return view;
    }

    //this method will remove the item from the list
    private void removeCard(final String walletId) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this card?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    mProgressDialog.show();
                    JSONObject requestVal = new JSONObject();
                    requestVal.put("mobileNo", "+601112212345");
                    requestVal.put("cardToken", walletId);
                    requestVal.put("masterMid", Constants.masterMid);

                    cardListService.deleteCard(new NetworkSuccessListener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseObj) {
                            mProgressDialog.dismiss();
                            try {
                                mProgressDialog.dismiss();
                                Log.e("--cardDelete--", responseObj.toString());
                                if (responseObj.getString("responseCode").equalsIgnoreCase("0000")) {
                                    mProgressDialog.dismiss();
                                    context.startActivity(new Intent(context, CardListActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    JSONObject responseData = responseObj.getJSONObject("responseData");
                                    Log.v("--cardDelete--", responseData.toString());
                                    Toast.makeText(context, responseObj.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(context, responseObj.toString(), Toast.LENGTH_SHORT).show();
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
                //reloading the list
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
