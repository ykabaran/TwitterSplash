package com.yildizkabaran.twittersplash.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

import com.yildizkabaran.twittersplash.R;

/**
 * A simple frame layout with 2 child views, one for content one for splash
 * @author yildizkabaran
 *
 */
public class MainView extends FrameLayout {
  
  public MainView(Context context){
    super(context);
    initialize();
  }
  
  private SplashView mSplashView;
  
  private void initialize(){
    Context context = getContext();
    
    // initialize the view with all default values
    // you don't need to set these default values, they are already set, except for setIconResource
    // this is only for demonstration purposes
    mSplashView = new SplashView(context);
    mSplashView.setDuration(500); // the animation will last 0.5 seconds
    mSplashView.setBackgroundColor(Color.WHITE); // transparent hole will look white before the animation
    mSplashView.setIconColor(Color.rgb(23, 169, 229)); // this is the Twitter blue color
    mSplashView.setIconResource(R.drawable.ic_twitter); // a Twitter icon with transparent hole in it
    mSplashView.setRemoveFromParentOnEnd(true); // remove the SplashView from MainView once animation is completed
    
    // add the view
    addView(mSplashView);
  }
  
  public void unsetSplashView(){
    mSplashView = null;
  }
  
  public SplashView getSplashView(){
    return mSplashView;
  }
}
