package xyz.gautamhans.locus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Gautam on 09-Apr-17.
 */

public class Category extends AppCompatActivity {
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent mIntent = getIntent();
        int itemIndex = mIntent.getIntExtra("clickIndex", 0);

        // TODO (1) : find a better solution to this
        switch (itemIndex){
            case 0:
                setTitle("ATMs");
                break;
            case 1:
                setTitle("Banks");
                break;
            case 2:
                setTitle("Bars");
                break;
            default:
                setTitle("Respected Category Here");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home) {
            Intent actMain = new Intent(Category.this, MainActivity.class);
        }
                return super.onOptionsItemSelected(item);

    }
}
