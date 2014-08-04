# Twitter Splash for Android
by Yildiz Kabaran

A simple replication of the Twitter's splash animation as seen in Twitter iOS app. The animation can be used with any app logo as long as a transparent icon with a solid background is provided.

![Preview Image](http://i.imgbox.com/8wktpX5A.gif)

## Installation

Simply copy the SplashView.java file into your project and use it like you would use any other custom view. If you would like to inflate from XML, then you might also want to copy the resources in attrs.xml file as well.

## Usage

You can create in code:
```
// create and customize the view
SplashView splashView = new SplashView(context);
splashView.setDuration(500); // the animation will last 0.5 seconds
splashView.setHoleFillColor(Color.WHITE); // transparent hole will look white before the animation
splashView.setIconColor(Color.rgb(23, 169, 229)); // this is the Twitter blue color
splashView.setIconResource(R.drawable.ic_twitter); // a Twitter icon with transparent hole in it
splashView.setRemoveFromParentOnEnd(true); // remove the SplashView from MainView once animation is completed

// add the view to a parent view
parentView.addView(splashView);
```

or in XML:
```
<com.yildizkabaran.twittersplash.view.SplashView 
    xmlns:app="http://schemas.android.com/apk/res/com.yildizkabaran.twittersplash"
	android:id="@+id/splash_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:icon="@drawable/ic_twitter"
    app:iconColor="@color/twitter_blue"
    app:duration="500"
    app:holeFillColor="@color/white"
    app:removeFromParentOnEnd="true" />
```

then to run the animation, simply call:
```
// run the animation and listen to the animation events (listener can be left as null)
mSplashView.splashAndDisappear(new ISplashListener(){
	@Override
	public void onStart(){

	}
	
	@Override
	public void onUpdate(float completionFraction){

	}

	@Override
	public void onEnd(){

	}
});
```

## Notes

- The view has only been tested on HTC One running Android 4.4.2, and therefore needs to be tested on devices with different versions and screen resolutions.
- Right now the size of the icon on the screen cannot be customized, and the intrinsic dimensions of the drawable is used. This will be fixed in future releases
- Twitter logo or the images used in this demo app do not belong to me and should not be used in your apps either.

## Thanks

Special thanks to Isilay Olgun for preparing the images used in this demo. Otherwise this would be a very ugly demonstration.

## Copyright and License

Feel free to use the code in any way you wish as long as you use your own image files.
