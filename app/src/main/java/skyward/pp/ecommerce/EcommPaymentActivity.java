package skyward.pp.ecommerce;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;

import skyward.pp.R;

public class EcommPaymentActivity extends NavigationHeader {

    RadioGroup payment_rg;
    RadioButton payment_cc,payment_dc,payment_nb,payment_cod;
    EditText payment_choosebank, payment_name, payment_cardnumber, payment_expiremonth,payment_expireyear,payment_cvvnumber;
    Button payment_btn_pay;
    LinearLayout ll_card, ll_netbanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_ecommpayment, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Payment");
        ab.setHomeButtonEnabled(true);

        payment_rg = (RadioGroup) findViewById(R.id.payment_rg);
        payment_cc = (RadioButton) findViewById(R.id.payment_cc);
        payment_dc = (RadioButton) findViewById(R.id.payment_dc);
        payment_nb = (RadioButton) findViewById(R.id.payment_nb);
        payment_cod = (RadioButton) findViewById(R.id.payment_cod);
        payment_choosebank =(EditText) findViewById(R.id.payment_choosebank);
        payment_name =(EditText) findViewById(R.id.payment_name);
        payment_cardnumber =(EditText) findViewById(R.id.payment_cardnumber);
        payment_expiremonth =(EditText) findViewById(R.id.payment_expiremonth);
        payment_expireyear =(EditText) findViewById(R.id.payment_expireyear);
        payment_cvvnumber =(EditText) findViewById(R.id.payment_cvvnumber);
        payment_btn_pay = (Button) findViewById(R.id.payment_btn_pay);
        ll_card = (LinearLayout) findViewById(R.id.ll_card);

    }
}
