package au.com.gramline.gramporeceive.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderList {
    @SerializedName("results")
    public List<PurchaseOrder> results = new ArrayList<>();

    public class PurchaseOrder {
        @SerializedName("ORDERDATE")
        public String ORDERDATE;
        @SerializedName("ACCNO")
        public Integer ACCNO;
        @SerializedName("NAME")
        public String NAME;
        @SerializedName("HDR_SEQNO")
        public Integer HDR_SEQNO;
        @SerializedName("STOCKCODE")
        public String STOCKCODE;
        @SerializedName("DESCRIPTION")
        public String DESCRIPTION;
        @SerializedName("ORD_QUANT")
        public Double ORD_QUANT;
        @SerializedName("SUP_QUANT")
        public Double SUP_QUANT;
        @SerializedName("SEQNO")
        public Integer SEQNO;

    }
}
