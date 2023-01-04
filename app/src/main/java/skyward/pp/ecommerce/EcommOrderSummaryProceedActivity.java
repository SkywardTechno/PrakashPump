package skyward.pp.ecommerce;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import skyward.pp.R;

public class EcommOrderSummaryProceedActivity extends NavigationHeader {

    Button osp_btn_proceed;
    TextView osp_shippingname, osp_total, osp_shippingcharge,osp_grandtotal,osp_estimateddelivery;
    ListView listordersummary;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_ordersummary_proceed, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Order Summary");
        ab.setHomeButtonEnabled(true);

        osp_btn_proceed = (Button) findViewById(R.id.btn_osp_proceed);
        osp_total = (TextView) findViewById(R.id.txtx_osp_total);
        osp_shippingcharge = (TextView) findViewById(R.id.txt_osp_shippingcharges);
        osp_shippingname = (TextView) findViewById(R.id.txt_osp_shippingname);
        osp_grandtotal = (TextView) findViewById(R.id.txt_osp_grandtotal);
        osp_estimateddelivery = (TextView) findViewById(R.id.txt_osp_estimateddelivery);
        address = (TextView) findViewById(R.id.txt_osp_address);

        listordersummary = (ListView) findViewById(R.id.listordersummaryproceed);
    }
}