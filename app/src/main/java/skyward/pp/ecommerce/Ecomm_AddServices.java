package skyward.pp.ecommerce;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import skyward.pp.R;

public class Ecomm_AddServices extends NavigationHeader {
    LinearLayout add_ll5star,add_ll3star, add_ll1star;
    TextView add_servicedetails;
    TextView add_termsconditions, add_price;
    Button addservices_btn_buy;
    CheckBox tc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecomm_addservices, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Add Services");
        ab.setHomeButtonEnabled(true);
        addservices_btn_buy = (Button) findViewById(R.id.btn_as_buy);
        add_price = (TextView) findViewById(R.id.txt_as_price);
        add_servicedetails = (TextView) findViewById(R.id.txt_as__servicedetails);
        add_ll1star = (LinearLayout) findViewById(R.id.ll_as_1star);
        add_ll3star = (LinearLayout) findViewById(R.id.ll_as_3star);
        add_ll5star = (LinearLayout) findViewById(R.id.ll_as_5star);
        tc = (CheckBox) findViewById(R.id.cb_as_tc);

    }


    }
