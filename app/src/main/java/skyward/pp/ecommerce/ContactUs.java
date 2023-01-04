package skyward.pp.ecommerce;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.widget.TextView;

import skyward.pp.R;

public class ContactUs extends NavigationHeader {

    TextView txt_ho,txt_distmeast,txt_distoman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_contact_us);

        getLayoutInflater().inflate(R.layout.activity_contact_us, frameLayout);
        mDrawerList.setItemChecked(position, true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Contact Us");
        ab.setHomeButtonEnabled(true);

        txt_ho = (TextView) findViewById(R.id.txt_headoffice);
        txt_distmeast = (TextView) findViewById(R.id.txt_dist);
        txt_distoman = (TextView) findViewById(R.id.txt_distoman);


        txt_ho.setText("PRAKASH PUMP" +
                "\nShed No. C-5/6 Mangal Estate, " +
                "\nNr. Jayhind Metal Bus stand, Naroda," +
                "\nG.I.D.C.," +
                "\nAhmedabad, 380025, Gujarat (INDIA) " +
                "\nPhone : 079-22840004 " +
                "\ne-mail : sale@prakashpump.com");

        txt_distmeast.setText("PRAKASH PUMP " +
                "\n P.O. Box : 20973 Industrial area Al Ain, " +
                "\n(United Arab Emirates)" +
                "\nT : 00971 3 7222733 " +
                "\nF : 00971 3 7222744 " +
                "\nM : 00971 50 6731283" +
                "\ne-mail : sale@prakashpump.com");

        txt_distoman.setText("GLOBAL EQUIPMENT MATERIAL CO.LLC " +
                "\n\nP. O. Box. 187, Postal Code: 320 Nakhal Road," +
                "\n Barka (Sultanate of Oman) " +
                "\nT : 00968 26883982, " +
                "\nF : 00968 26883982 " +
                "\nM : 00968 95690021 " +
                "\nE-mail : oman@prakashpump.com");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), EcommDashboardActivity.class);
        startActivity(i);
        finish();
    }
}
