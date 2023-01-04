package skyward.pp.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import skyward.pp.R;
import skyward.pp.util.Utility;

public class Ecomm_ConfirmationActivity extends NavigationHeader {

     Button confirm_btn_ok;
    TextView confirm_shippingaddress,confirm_estimatedtime,confirm_shippingname,confirm_grandtotal, confirm_successmessage;
    ListView listconfirmitem;
    TextView confirm_orderid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_confirmation, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Confirmation");
        ab.setHomeButtonEnabled(true);


        confirm_btn_ok = (Button) findViewById(R.id.btn_confirm_ok);
        confirm_shippingaddress = (TextView) findViewById(R.id.txt_confirm_shippingaddress);
        confirm_orderid= (TextView) findViewById(R.id.txt_confirm_orderid);
        confirm_grandtotal = (TextView) findViewById(R.id.txt_confirm_grandtotal);
        confirm_successmessage = (TextView) findViewById(R.id.txt_confirm_successmessage);

        listconfirmitem = (ListView) findViewById(R.id.listconfirmitem);
        String total= Utility.gettotal(getApplicationContext());
        String orderid=Utility.getorderidorder(getApplicationContext());
        String address= Utility.getshippingaddress(getApplicationContext());
        confirm_shippingaddress.setText(address);
        confirm_grandtotal.setText(total +" "+ Utility.getordercurrency(getApplicationContext()));
        confirm_orderid.setText(orderid);
        confirm_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EcommDashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),EcommDashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
