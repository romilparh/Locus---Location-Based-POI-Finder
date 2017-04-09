package xyz.gautamhans.locus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Gautam on 09-Apr-17.
 */

public class Category extends AppCompatActivity {
    String type;

    public Category(String type){
        this.type = type;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_layout);
    }
}
