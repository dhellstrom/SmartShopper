package com.summer.daniel.smartshopper;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return ListFragment.newInstance();
    }
}
