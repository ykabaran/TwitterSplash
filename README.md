# Twitter Splash for Android
by Yildiz Kabaran

A simple replication of the Twitter's splash animation as seen in Twitter iOS app. The animation can be used with any app logo as long as a transparent icon with a solid background is provided.

![Preview Image](http://i.imgbox.com/8wktpX5A.gif)

## Installation

Simply copy the SplashView.java file into your project and use it like you would use any other custom view.

## Usage

```
// create and customize the view
SplashView splashView = new SplashView(context);
splashView.setDuration(500); // the animation will last 0.5 seconds
splashView.setBackgroundColor(Color.WHITE); // transparent hole will look white before the animation
splashView.setIconColor(Color.rgb(23, 169, 229)); // this is the Twitter blue color
splashView.setIconResource(R.drawable.ic_twitter); // a Twitter icon with transparent hole in it
splashView.setRemoveFromParentOnEnd(true); // remove the SplashView from MainView once animation is completed

// add the view to a parent view
parentView.addView(splashView);

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

- The view has not yet been tested through XML and none of the features can be set through XML. This will be implemented later</li>
- The view has only been tested on HTC One running Android 4.4.2, and therefore needs to be tested on devices with different versions and screen resolutions.</li>
- Right now the size of the icon on the screen cannot be customized, and the intrinsic dimensions of the drawable is used. This will be fixed in future releases</li>
- Twitter logo or the images used in this demo app do not belong to me and should not be used in your apps either.</li>

## Thanks

Special thanks to Isilay Olgun for preparing the images used in this demo. Otherwise this would be a very ugly demonstration.

## Copyright and License

Feel free to use the code in any way you wish as long as you use your own image files.
