package au.com.gramline.gramporeceive;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

import au.com.gramline.gramporeceive.pojo.PurchaseOrderList;
import au.com.gramline.gramporeceive.pojo.ReceivedOrderList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayPurchaseOrdersActivity extends AppCompatActivity {

    private Context context = null;
    APIInterface apiInterface;
    int size; // used to track the number of row under one order
    ObjectMapper mapper = new ObjectMapper();
    ReceivedOrderList savedOrder = new ReceivedOrderList();
    // Get TableLayout object in layout xml.
    TableLayout tableLayout = null;

    // Get the Intent that started this activity and extract the string
    Intent intent = null;
    String message, resume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_purchase_orders);

        tableLayout = (TableLayout)findViewById(R.id.table_layout_table);
        Button saveOrderButton = findViewById(R.id.saveOrderButton);
        Button uploadOrderButton = findViewById(R.id.uploadOrderButton);

        size = 0;
        intent = getIntent();
        message = intent.getStringExtra(EnterPurchaseOrderNumberActivity.EXTRA_MESSAGE_THREE);
        resume = intent.getStringExtra(EnterPurchaseOrderNumberActivity.EXTRA_MESSAGE_TWO);
        context = getApplicationContext();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        // Capture the layout's TextView and set the string as its text
        TextView orderNumber = findViewById(R.id.OrderNumberView);
        orderNumber.setText("Order Number: " + message);

        if (resume != null)
        {
            savedOrder = getOrderFromFile(savedOrder);
        }
        else
        {
            getOrderFromDatabase();
        }

        /* When save order button clicked. */
        saveOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < size; i++)
                {
                    EditText qtyReceived = tableLayout.findViewWithTag("qtyReceived_" + i);
                    if (qtyReceived.length() != 0)
                    {
                        String qtyReceivedValue = qtyReceived.getText().toString();
                        savedOrder.results.get(i).QTYReceived = Integer.parseInt(qtyReceivedValue);
                    }

                }
                Toast.makeText(getApplicationContext(), "Data Saved \n", Toast.LENGTH_SHORT).show();
                writeFileExternalStorage(savedOrder);
            }

        });
        /* When upload order button is clicked. */
        uploadOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ReceivedOrderList> call1 = apiInterface.createReceivedOrderList(savedOrder);
                // call and response type have to be the same to get a successful callback
                call1.enqueue(new Callback<ReceivedOrderList>() {
                    @Override
                    public void onResponse(Call<ReceivedOrderList> call, Response<ReceivedOrderList> response) {
                        Toast.makeText(getApplicationContext(), "File Uploaded \n", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ReceivedOrderList> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "File failed to upload \n", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }
        });
    }

    public void writeFileExternalStorage(ReceivedOrderList receivedOrderList) {

        //Text of the Document
        String DirName = "orderInfo.txt";

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            //If it isn't mounted - we can't write into it.
            return;
        }

        //Create a new file that points to the root directory, with the given name:
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GramPOReceive");
        if (!path.mkdirs())
        {
            path.mkdirs();
        }
        File file = new File(path, DirName);
        //File file = mapper.writeValue(new File("result.json"), collectedOrder);//Plain JSON
        /**
         * Write object to file
         */
        try {
            //mapper.writeValue(file, collectedOrderList);//Plain JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, receivedOrderList);//Prettified JSON
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReceivedOrderList getOrderFromFile(ReceivedOrderList receivedOrderList)
    {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GramPOReceive", "orderInfo.txt");
        try {
            receivedOrderList = mapper.readValue(file, ReceivedOrderList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ReceivedOrderList.ReceivedOrder receivedOrder : receivedOrderList.results) {
            // Create a new table row.
            TableRow tableRow = new TableRow(context);
            // Set new table row layout parameters.
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);

            // Add a TextView in the first column.
            TextView seqNo = new TextView(context);
            seqNo.setText(String.valueOf(receivedOrder.SEQNO));
            tableRow.addView(seqNo, 0,layoutParams);

            // Add a TextView in the second column.
            TextView stockCode = new TextView(context);
            stockCode.setText(String.valueOf(receivedOrder.STOCKCODE));
            tableRow.addView(stockCode, 1,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2));

            // Add a TextView in the third column.
            TextView description = new TextView(context);
            description.setText(receivedOrder.DESCRIPTION);
            tableRow.addView(description, 2, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4));

            // Add a TextView in the fourth column.
            TextView qtyReqd = new TextView(context);
            qtyReqd.setText(String.valueOf(receivedOrder.ORD_QUANT));
            tableRow.addView(qtyReqd, 3, layoutParams);

            // Add a EditText in the fifth column.
            EditText qtyCollected = new EditText(context);
            if (receivedOrder.QTYReceived != null)
            {
                qtyCollected.setText(String.valueOf(receivedOrder.QTYReceived));
            }
            tableRow.addView(qtyCollected, 4, layoutParams);
            qtyCollected.setTag("qtyReceived_" + size);
            qtyCollected.setInputType(InputType.TYPE_CLASS_NUMBER);

            size++;
            tableLayout.addView(tableRow);
        }
        return receivedOrderList;
    }

    public void getOrderFromDatabase()
    {
        /**
         * GET List Resources
         **/
        Call<PurchaseOrderList> call = apiInterface.doGetPurchaseOrderList(message);
        call.enqueue(new Callback<PurchaseOrderList>()
        {
            @Override
            public void onResponse (Call < PurchaseOrderList > call, Response< PurchaseOrderList > response){

                Log.d("TAG", response.code() + ""); // success code 200

                PurchaseOrderList resource = response.body();
                List<PurchaseOrderList.PurchaseOrder> dataList = resource.results;

                for (PurchaseOrderList.PurchaseOrder purchaseOrder : dataList) {
                    // Create a new table row.
                    TableRow tableRow = new TableRow(context);
                    ReceivedOrderList.ReceivedOrder orderItem = new ReceivedOrderList.ReceivedOrder();
                    // Set new table row layout parameters.
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);

                    // Add a TextView in the first column.
                    TextView seqNo = new TextView(context);
                    seqNo.setText(String.valueOf(purchaseOrder.SEQNO));
                    tableRow.addView(seqNo, 0,layoutParams);
                    orderItem.SEQNO = purchaseOrder.SEQNO;

                    // Add a TextView in the second column.
                    TextView stockCode = new TextView(context);
                    stockCode.setText(String.valueOf(purchaseOrder.STOCKCODE));
                    tableRow.addView(stockCode, 1,new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2));
                    orderItem.STOCKCODE = purchaseOrder.STOCKCODE;

                    // Add a TextView in the third column.
                    TextView description = new TextView(context);
                    description.setText(purchaseOrder.DESCRIPTION);
                    tableRow.addView(description, 2, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 4));
                    orderItem.DESCRIPTION = purchaseOrder.DESCRIPTION;

                    // Add a TextView in the fourth column.
                    TextView qtyReqd = new TextView(context);
                    qtyReqd.setText(String.valueOf(purchaseOrder.ORD_QUANT));
                    tableRow.addView(qtyReqd, 3, layoutParams);
                    orderItem.ORD_QUANT = purchaseOrder.ORD_QUANT.intValue();

                    // Add a EditText in the fifth column.
                    EditText qtyCollected = new EditText(context);
                    tableRow.addView(qtyCollected, 4, layoutParams);
                    qtyCollected.setTag("qtyReceived_" + size);
                    qtyCollected.setInputType(InputType.TYPE_CLASS_NUMBER);

                    size++;
                    savedOrder.results.add(orderItem);
                    tableLayout.addView(tableRow);
                }
            }
            @Override
            public void onFailure (Call < PurchaseOrderList > call, Throwable t){
                call.cancel();
            }
        });
    }
}