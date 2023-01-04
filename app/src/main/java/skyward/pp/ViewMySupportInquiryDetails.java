package skyward.pp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewMySupportInquiryDetails extends AppCompatActivity {

    TextView txt_name,txt_mobileno,txt_emailid, txt_service;
    TextView txt_inquirydetails,txt_inquirycode;
    int orderID;
    String inquiryid;
    String name, mobileno, emailid, remarks, productname, inquirycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_supportinquirydetails);

        setTitle("Inquiry Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Inquiry Details");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ViewMySupportInquiryDetails.this, CustomerOrderInquiryListActivity.class);
                startActivity(i);
                finish();
            }
        });


        txt_name= (TextView) findViewById(R.id.sidetails_name);
        txt_mobileno= (TextView) findViewById(R.id.sidetails_mobileno);
        txt_emailid= (TextView) findViewById(R.id.sidetails_emailid);
        txt_service= (TextView) findViewById(R.id.sidetails_service);
        txt_inquirydetails= (TextView) findViewById(R.id.sidetails_supportinquirydetails);
        txt_inquirycode= (TextView) findViewById(R.id.sidetails_inquirycode);

        Intent i = getIntent();
        orderID = Integer.valueOf(i.getStringExtra("ID"));

        txt_inquirycode.setText(i.getStringExtra("inquirycode"));
        txt_service.setText(i.getStringExtra("servicetype"));
        txt_name.setText(i.getStringExtra("name"));
        txt_mobileno.setText(i.getStringExtra("phoneno"));
        txt_emailid.setText(i.getStringExtra("emailid"));
        txt_inquirydetails.setText(i.getStringExtra("remarks"));

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ViewMySupportInquiryDetails.this, CustomerOrderInquiryListActivity.class);
        i.putExtra("fromsupport","fromsupport");
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
