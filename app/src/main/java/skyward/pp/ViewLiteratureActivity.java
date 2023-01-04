package skyward.pp;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import skyward.pp.util.Utility;

public class ViewLiteratureActivity extends AppCompatActivity {

    WebView viewliterature;
    int back,LID;
    String navigatevalue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewliterature);

        setTitle("Literature");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Literature");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().getCustomView();

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navigatevalue.equalsIgnoreCase("fromadmin")){
                    Intent i = new Intent(ViewLiteratureActivity.this, AdminMultipleLiteratureList.class);
                    i.putExtra("LID",String.valueOf(LID));
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(ViewLiteratureActivity.this, LiteratureActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        viewliterature = (WebView) findViewById(R.id.viewliterature);

        Intent i = getIntent();

        if(i.hasExtra("fromadmin")){
            navigatevalue ="fromadmin";
        }else {
            navigatevalue ="fromcustomer";
        }

        if(i.hasExtra("FileName") && i.hasExtra("FilePath") && i.hasExtra("LiteratureName") && i.hasExtra("Value") && i.hasExtra("fromadmin"))
        {
            back = 1;
            LID = Integer.parseInt(i.getStringExtra("Value"));
            String myPdfUrl = Utility.URLFORIMAGE + i.getStringExtra("FilePath");
            //String doc="<iframe src='http://docs.google.com/viewer?url="+myPdfUrl+"&embedded=true' width='100%' height='100%' style='border: none;'></iframe>";
           // String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
            String googleDocs = "https://docs.google.com/viewer?url=";
            //    Log.i(, "Opening PDF: " + url);
            viewliterature.getSettings().setJavaScriptEnabled(true);
           // viewliterature.getSettings().setAllowFileAccess(true);
            viewliterature.loadUrl(googleDocs+myPdfUrl);
            //viewliterature.loadData( doc , "text/html",  "UTF-8");
        }

        else
        {
            back =2;
            String myPdfUrl = Utility.URLFORIMAGE + i.getStringExtra("Filepath");
            String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
           // String doc="<iframe src='http://docs.google.com/viewer?url="+myPdfUrl+"&embedded=true' width='100%' height='100%' style='border: none;'></iframe>";

            //    Log.i(, "Opening PDF: " + url);
            viewliterature.getSettings().setJavaScriptEnabled(true);
           // viewliterature.getSettings().setAllowFileAccess(true);
            viewliterature.loadUrl(url);
           // viewliterature.loadData( doc , "text/html",  "UTF-8");
        }


       /* Intent i = getIntent();
        String myPdfUrl = Utility.URLFORIMAGE + i.getStringExtra("Filepath");
        String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
        //    Log.i(, "Opening PDF: " + url);
        viewliterature.getSettings().setJavaScriptEnabled(true);
        viewliterature.loadUrl(url);*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(navigatevalue.equalsIgnoreCase("fromadmin")){
            Intent i = new Intent(ViewLiteratureActivity.this, AdminMultipleLiteratureList.class);
            i.putExtra("LID",String.valueOf(LID));
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(ViewLiteratureActivity.this, LiteratureActivity.class);
            startActivity(i);
            finish();
        }

    }


}
