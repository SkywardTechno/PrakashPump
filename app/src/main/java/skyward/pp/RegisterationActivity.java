package skyward.pp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class                         RegisterationActivity extends AppCompatActivity {

    Button register;
    int mFlipping = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);

        if (mFlipping == 0) {
            /** Start Flipping */
            flipper.startFlipping();
            mFlipping = 1;

        }

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(RegisterationActivity.this, VerificationcodeActivity.class);
                startActivity(i);
            }
        });


    }
}
