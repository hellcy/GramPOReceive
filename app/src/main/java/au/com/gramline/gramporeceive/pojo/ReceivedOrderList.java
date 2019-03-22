package au.com.gramline.gramporeceive.pojo;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ReceivedOrderList {
    @SerializedName("USERNAME")
    public String USERNAME;
    @SerializedName("results")
    public List<ReceivedOrder> results = new ArrayList<>();

    public static class ReceivedOrder {
        @SerializedName("SEQNO")
        public Integer SEQNO;
        @SerializedName("HDR_SEQNO")
        public Integer HDR_SEQNO;
        @SerializedName("ACCNO")
        public Integer ACCNO;
        @SerializedName("STOCKCODE")
        public String STOCKCODE;
        @SerializedName("DESCRIPTION")
        public String DESCRIPTION;
        @SerializedName("ORD_QUANT")
        public Integer ORD_QUANT;
        @SerializedName("SUP_QUANT")
        public Integer SUP_QUANT;
        @SerializedName("QtyReceived")
        public Integer QTYReceived;
        @SerializedName("ORDERDATE")
        public String ORDERDATE;

    }
}
