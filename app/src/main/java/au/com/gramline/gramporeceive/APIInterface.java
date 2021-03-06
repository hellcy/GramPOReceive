package au.com.gramline.gramporeceive;

import java.util.List;

import au.com.gramline.gramporeceive.pojo.PurchaseOrderList;
import au.com.gramline.gramporeceive.pojo.ReceivedOrderList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    // get the specific job order details
    @GET("/Scanner/api/PurchaseOrder/get?")
    Call<PurchaseOrderList> doGetPurchaseOrderList(@Query("HDR_SEQNO") String HDR_SEQNO);

    // get job orders by account name
    @GET("/Scanner/api/PurchaseOrder/get?")
    Call<PurchaseOrderList> doGetPurchaseOrderListByName(@Query(value="ACCNAME", encoded = true) String ACCNAME);

    // get account names
    @GET("/Scanner/api/PurchaseOrder/get")
    Call<List<String>> doGetAccountNameList();

    // create list of orders
    @POST("/Scanner/api/PurchaseOrder")
    Call<ReceivedOrderList> createReceivedOrderList(@Body ReceivedOrderList receivedOrderList);
}
