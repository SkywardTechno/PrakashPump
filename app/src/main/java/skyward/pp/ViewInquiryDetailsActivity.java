package skyward.pp;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewInquiryDetailsActivity extends AppCompatActivity {

    String inquiryid;
    String name, mobileno, emailid, remarks, productname, inquirycode;
    TextView txt_name, txt_mobileno, txt_emailid, txt_inquirycode, txt_productname, txt_remarks;

    int orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewinquiry_details);

        setTitle("Inquiry Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Inquiry Details");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        txt_name = (TextView) findViewById(R.id.vi_name);
        txt_mobileno = (TextView) findViewById(R.id.vi_mobileno);
        txt_emailid = (TextView) findViewById(R.id.vi_emailid);
        txt_inquirycode = (TextView) findViewById(R.id.vi_inquirycode);
        txt_productname = (TextView) findViewById(R.id.vi_productname);
        txt_remarks = (TextView) findViewById(R.id.vi_inquirydetails);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ViewInquiryDetailsActivity.this, CustomerOrderInquiryListActivity.class);
                startActivity(i);
                finish();
            }
        });

        Intent i = getIntent();
        orderID = Integer.valueOf(i.getStringExtra("ID"));


        txt_inquirycode.setText(i.getStringExtra("inquirycode"));
        txt_productname.setText(i.getStringExtra("productname"));
        txt_name.setText(i.getStringExtra("name"));
        txt_mobileno.setText(i.getStringExtra("phoneno"));
        txt_emailid.setText(i.getStringExtra("emailid"));
        txt_remarks.setText(i.getStringExtra("remarks"));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ViewInquiryDetailsActivity.this, CustomerOrderInquiryListActivity.class);
        i.putExtra("fromorder","fromorder");
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater menuInflater = getSupportMenuInflater();
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // return true;
        // MenuItemCompat.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_home){
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
