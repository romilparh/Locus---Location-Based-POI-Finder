package xyz.gautamhans.locus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Reminder_UI_Activity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    SeekBar seekBar1;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_ui);
        seekBar1=(SeekBar)findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(this);
        textView = (TextView) findViewById(R.id.textview4);
        seekBar1.setMax(10);
    }

    public void saveButtonPressed(View view){
        Context context = getApplicationContext();
        CharSequence text = "Reminder Saved";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }
    public void hide(View view) {

        TextView txtView = (TextView)findViewById(R.id.locationSelected);

        //Toggle
        if (txtView .getVisibility() == View.INVISIBLE)
            txtView.setVisibility(View.VISIBLE);

        //If you want it only one time
        //txtView.setVisibility(View.VISIBLE);

    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
        int min=5;
        if (progress<1){
            textView.setText("1");
        }
        else {
            textView.setText(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
