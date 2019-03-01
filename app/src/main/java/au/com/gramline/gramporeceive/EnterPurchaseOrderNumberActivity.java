package au.com.gramline.gramporeceive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class EnterPurchaseOrderNumberActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_THREE = "com.example.GramDispatch.MESSAGE3";
    public static final String EXTRA_MESSAGE_TWO = "com.example.GramDispatch.MESSAGE2";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_purchase_order_number);

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
        EditText editText = (EditText) findViewById(R.id.enterOrderNumberText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_THREE, message);
        startActivity(intent);
    }

    /** Called when the user taps the continue order button */
    public void continueOrder(View view) {
        Intent intent = new Intent(this, DisplayPurchaseOrdersActivity.class);
        EditText editText = (EditText) findViewById(R.id.enterOrderNumberText);
        String message = editText.getText().toString();
        String resume = "resume";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GramPOReceive", "order " + message + ".txt");
        if (!file.exists())
        {
            Toast.makeText(getApplicationContext(), "File doesn't exist! \n", Toast.LENGTH_SHORT).show();
        }
        else
        {
            intent.putExtra(EXTRA_MESSAGE_THREE, message);
            intent.putExtra(EXTRA_MESSAGE_TWO, resume);
            startActivity(intent);
        }
    }
}
