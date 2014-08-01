package com.yildizkabaran.twittersplash.view;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.FrameLayout;

import com.yildizkabaran.twittersplash.BuildConfig;
import com.yildizkabaran.twittersplash.R;
import com.yildizkabaran.twittersplash.view.SplashView.ISplashListener;

/**
 * A simple frame layout with 2 child views, one for content one for splash
 * @author yildizkabaran
 *
 */
public class MainView extends FrameLayout {
  
  private static final String TAG = "MainView";
  
  public MainView(Context context){
    super(context);
    initialize();
  }
  
  private SplashView mSplashView;
  private ContentView mContentView;
  
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
    
    // pretend like we are going to load something in the background
    startLoadingData();
  }
  
  private void startLoadingData(){
    // finish "loading data" in a random time between 1 and 3 seconds
    Random random = new Random();
    postDelayed(new Runnable(){
      @Override
      public void run(){
        onLoadingDataEnded();
      }
    }, 1000 + random.nextInt(2000));
  }
  
  private void onLoadingDataEnded(){
    Context context = getContext();
    // now that our data is loaded we can initialize the content view
    mContentView = new ContentView(context);
    // add the content view to the background
    addView(mContentView, 0);
    
    // start the splash animation
    mSplashView.splashAndDisappear(new ISplashListener(){
      @Override
      public void onStart(){
        // log the animation start event
        if(BuildConfig.DEBUG){
          Log.d(TAG, "splash started");
        }
      }
      
      @Override
      public void onUpdate(float completionFraction){
        // log animation update events
        if(BuildConfig.DEBUG){
          Log.d(TAG, "splash at " + String.format("%.2f", (completionFraction * 100)) + "%");
        }
      }
      
      @Override
      public void onEnd(){
        // log the animation end event
        if(BuildConfig.DEBUG){
          Log.d(TAG, "splash ended");
        }
        // free the view so that it turns into garbage
        mSplashView = null;
      }
    });
  }
}
