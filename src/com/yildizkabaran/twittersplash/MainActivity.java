package com.yildizkabaran.twittersplash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.yildizkabaran.twittersplash.view.MainView;

public class MainActivity extends Activity {

  private View mMainView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // create the main view and it will handle the rest
    mMainView = new MainView(getApplicationContext());
    setContentView(mMainView);
  }
}
