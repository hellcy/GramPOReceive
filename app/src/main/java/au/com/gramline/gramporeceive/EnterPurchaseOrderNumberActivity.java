package au.com.gramline.gramporeceive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterPurchaseOrderNumberActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_THREE = "com.example.GramDispatch.MESSAGE3";
    public static final String RESUME_FLAG = "com.example.GramDispatch.MESSAGE2";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    APIInterface apiInterface;
    ArrayList<String> nameStringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AutoCompleteTextView accountNameText;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_purchase_order_number);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        // get account names from the database
        Call<List<String>> call = apiInterface.doGetAccountNameList();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                Log.d("TAG", response.code() + ""); // success code 200

                List<String> resource = response.body();

                for (String accountName : resource) {
                    nameStringList.add(accountName);
                }
            }
            @Override
            public void onFailure (Call < List<String> > call, Throwable t){
                call.cancel();
            }
        });
        accountNameText = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        ArrayAdapter adapter = new
                ArrayAdapter(this,android.R.layout.simple_list_item_1,nameStringList);

        accountNameText.setAdapter(adapter);
        accountNameText.setThreshold(1);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.usernameView);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString("username", null);
        textView.setText("You are logged in as: ");
        TextView username2 = findViewById(R.id.usernameView2);
        username2.setText(username);
    }

    /** Called when the user taps the get job order button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayPurchaseOrdersActivity.class);
        EditText purchaseNumberText = (EditText) findViewById(R.id.enterOrderNumberText);
        AutoCompleteTextView accountNameText = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        if (TextUtils.isEmpty(purchaseNumberText.getText()) && TextUtils.isEmpty(accountNameText.getText())) {
            Toast.makeText(getApplicationContext(), "Please enter purchase number or account name \n", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(purchaseNumberText.getText()) == false)
        {
            String message = purchaseNumberText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE_THREE, message);
            startActivity(intent);
        }
        else
        {
            String message = accountNameText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE_THREE, message);
            startActivity(intent);
        }
    }

    /** Called when the user taps the continue order button */
    public void continueOrder(View view) {
        Intent intent = new Intent(this, DisplayPurchaseOrdersActivity.class);
        EditText purchaseNumberText = (EditText) findViewById(R.id.enterOrderNumberText);
        AutoCompleteTextView accountNameText = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        String message = purchaseNumberText.getText().toString();
        if (TextUtils.isEmpty(purchaseNumberText.getText()) && TextUtils.isEmpty(accountNameText.getText()) == false)
        {
            message = accountNameText.getText().toString();
        }

        String resume = "resume";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GramPOReceive", "order " + message + ".txt");
        if (!file.exists())
        {
            Toast.makeText(getApplicationContext(), "File doesn't exist! \n", Toast.LENGTH_SHORT).show();
        }
        else
        {
            intent.putExtra(EXTRA_MESSAGE_THREE, message);
            intent.putExtra(RESUME_FLAG, resume);
            startActivity(intent);
        }
    }
}
