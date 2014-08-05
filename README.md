# Twitter Splash for Android
by Yildiz Kabaran

A simple replication of Twitter's splash animation as seen in Twitter iOS app. The animation can be used with any app logo as long as a transparent icon with a solid background is provided.

![Preview Image](http://i.imgbox.com/dMkufUcJ.gif)

## Installation

Simply copy the SplashView.java and attrs.xml files into your project and use it like you would use any other custom view.

## Usage

You can create in code:
```
// create and customize the view
SplashView splashView = new SplashView(context);
// the animation will last 0.5 seconds
splashView.setDuration(500);
// transparent hole will look white before the animation
splashView.setHoleFillColor(Color.WHITE);
// this is the Twitter blue color
splashView.setIconColor(Color.rgb(23, 169, 229));
// a Twitter icon with transparent hole in it
splashView.setIconResource(R.drawable.ic_twitter);
// remove the SplashView from MainView once animation is completed
splashView.setRemoveFromParentOnEnd(true);
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
splashView.splashAndDisappear(new ISplashListener(){
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

In the sample code a boolean DO_XML variable is provided to demonstrate both methods.

Please feel free to ask for any fixes/customizations/additions to this view.

## Notes

- The view has only been tested on HTC One running Android 4.4.2, and therefore needs to be tested on devices with different versions and screen resolutions.
- Right now the size of the icon on the screen cannot be customized, and the intrinsic dimensions of the drawable is used. This will be fixed in future releases
- Twitter logo or the images used in this demo app do not belong to me and should not be used in your apps either.

## Thanks

Special thanks to Isilay Olgun for preparing the images used in this demo. Otherwise this would be a very ugly looking demonstration.

## Copyright and License

Feel free to use the code in any way you wish as long as you use your own image files.
