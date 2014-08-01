package com.yildizkabaran.twittersplash.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.animation.OvershootInterpolator;

import com.yildizkabaran.twittersplash.BuildConfig;

/**
 * A simple view class that will display an enlarging icon animation. For best results the provided icon should have a
 * solid, mono-color background with a transparent hole where the icon's silhouette is supposed to be
 * @author yildizkabaran
 *
 */
public class SplashView extends View {

  private static final String TAG = "SplashView";
  
  /**
   * A simple interface to listen to the state of the splash animation
   * @author yildizkabaran
   *
   */
  public static interface ISplashListener {
    public void onStart();
    public void onUpdate(float completionFraction);
    public void onEnd();
  }
  
  /**
   * A Context constructor is provided for creating the view by code. All other constructors use this constructor.
   * @param context
   */
  public SplashView(Context context){
    super(context);
    initialize();
  }

  /**
   * This constructor is redirected to the Context constructor.
   * @param context
   * @param attrs
   */
  public SplashView(Context context, AttributeSet attrs) {
    this(context);
  }

  /**
   * This constructor is redirected to the Context constructor.
   * @param context
   * @param attrs
   */
  public SplashView(Context context, AttributeSet attrs, int defStyleAttr) {
    this(context);
  }

  public static final int DEFAULT_BG_COLOR = Color.WHITE;
  public static final int DEFAULT_ICON_COLOR = Color.rgb(23, 169, 229);
  public static final long DEFAULT_DURATION = 400;
  public static final boolean DEFAULT_REMOVE_FROM_PARENT_ON_END = true;
  
  private static final int PAINT_STROKE_WIDTH = 2; // give a stroke width to the paint so that the rectangles get a little overlap
  
  private Drawable mIcon; // most important item, cannot be null
  private int mBgColor = DEFAULT_BG_COLOR; // color to be shown in the transparent hole before the animation starts
  private int mIconColor = DEFAULT_ICON_COLOR; // should be the same color of as the icon background
  private long mDuration = DEFAULT_DURATION; // total duration, in ms, of the animation
  private boolean mRemoveFromParentOnEnd = true; // a flag for removing the view from its parent once the animation is over
  private float mCurrentScale = 1; // used for keeping track of how far along the animation we are
  
  // cache some dimension values to make the onDraw method simpler looking
  private int mWidth, mHeight;
  private int mIconWidth, mIconHeight;
  private float mMaxScale = 1;
  
  // cache some object allocations so that they don't have to be allocated in onDraw
  private Paint mPaint = new Paint();
  private RectF mIconRect = new RectF();
  
  /**
   * Initialized the view properties. No much is done in this method since most variables already have set defaults
   */
  private void initialize(){
    // make the entire view transparent so that the transparent hole can show
    setBackgroundColor(Color.TRANSPARENT);

    // set fill style on the paint so that the rectangles get filled
    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mPaint.setStrokeWidth(PAINT_STROKE_WIDTH);
  }
  
  /**
   * Set the background color of the view that will be seen through the transparent hole of the icon before the animation starts
   */
  public void setBackgroundColor(int bgColor){
    mBgColor = bgColor;
  }
  
  /**
   * Set the color of the icon. This value will be used to draw 4 rectangles around the icon to fill the entire view.
   * If not set, or set incorrectly the edges of the icon image will be visible. There are a few tricks to make this animation
   * look right, and this is one of them. Make sure this color is set correctly.
   * @param iconColor
   */
  public void setIconColor(int iconColor){
    mIconColor = iconColor;
  }
  
  /**
   * Set the duration of the entire animation in milliseconds.
   * @param duration
   */
  public void setDuration(long duration){
    if(duration < 0){
      throw new IllegalArgumentException("duration cannot be less than 0");
    }
    
    mDuration = duration;
  }
  
  /**
   * Set the resource id of the Drawable to be used as the icon. See setIconDrawable(Drawable icon) for more details.
   * @param resId
   */
  public void setIconResource(int resId){
    Drawable icon = getResources().getDrawable(resId);
    
    if(icon == null){
      throw new IllegalArgumentException("no drawable found for the resId: " + resId);
    }
    
    setIconDrawable(icon);
  }
  
  /**
   * Set the Drawable to be used as the icon. It can be any kind of Drawable that has an intrinsic width and height.
   * So far changing the size of the Drawable is not supported but can be added in the future
   * @param icon
   */
  public void setIconDrawable(Drawable icon){
    if(icon == null){
      throw new IllegalArgumentException("icon cannot be null");
    }
    
    mIcon = icon;
    mIconWidth = mIcon.getIntrinsicWidth();
    mIconHeight = mIcon.getIntrinsicHeight();
    // set the bounds of the drawable to its own dimensions
    // canvas scaling will be used to change the bounds of the icon
    Rect iconBounds = new Rect();
    iconBounds.left = 0;
    iconBounds.top = 0;
    iconBounds.right = mIconWidth;
    iconBounds.bottom = mIconHeight;
    mIcon.setBounds(iconBounds);
    
    setMaxScale();
  }
  
  /**
   * Set the flag to remove or keep the view after the animation is over. This is set to true by default. The view must be inside a ViewManager
   * (or ViewParent) for this to work. Otherwise, the view will not be removed and a warning log will be produced.
   * @param shouldRemove
   */
  public void setRemoveFromParentOnEnd(boolean shouldRemove){
    mRemoveFromParentOnEnd = shouldRemove;
  }
  
  /**
   * A helper method for determining for large the icon should be enlarged before the animation ends. There is a chance that the entire view will not become
   * transparent by the end of the animation.
   */
  private void setMaxScale(){
    if(mIconWidth < 1 || mIconHeight < 1){
      mMaxScale = 1;
      return;
    }
    
    mMaxScale = 2 * Math.max((float) mWidth/mIconWidth, (float) mHeight/mIconHeight);
    
    // just to make sure the animation does not actually work backwards
    if(mMaxScale < 1){
      mMaxScale = 1;
    }
  }
  
  /**
   * Starts the splash and disappear animation. If a listener is provided it will notify the listener on animation events
   * @param listener
   */
  public void splashAndDisappear(final ISplashListener listener){
    // create an animator from scale 1 to max
    final ValueAnimator animator = ValueAnimator.ofFloat(1, mMaxScale);
    // set the duration
    animator.setDuration(mDuration);
    // set an overshoot interpolator with a low tension value so that the icon becomes a little smaller before it expands
    animator.setInterpolator(new OvershootInterpolator(1F));
    
    // add an update listener so that we draw the view on each update
    animator.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        // keep in mind that the animation runs in reverse to get the desired effect from the interpolator
        // therefore we need to subtract to correct for this effect, and then add 1 so that the scale doesn't dip below 0
        // this is NOT fool-proof, and therefore can be made better
        mCurrentScale = 1 + mMaxScale - (Float) animation.getAnimatedValue();
        
        // invalidate the view so that it gets redraw if it needs to be
        invalidate();
        
        // notify the listener if set, but remember the animation runs in reverse
        if(listener != null){
          listener.onUpdate(1 - animation.getAnimatedFraction());
        }
      }
    });
    
    // add a listener for the general animation events, use the AnimatorListenerAdapter so that we don't clutter the code
    animator.addListener(new AnimatorListenerAdapter(){
      @Override
      public void onAnimationStart(Animator animation){
        // notify the listener of animation start (if listener is set)
        if(listener != null){
          listener.onStart();
        }
      }
      
      @Override
      public void onAnimationEnd(Animator animation){
        // check if we need to remove the view on animation end
        if(mRemoveFromParentOnEnd){
          // get the view parent
          ViewParent parent = getParent();
          // check if a parent exists and that it implements the ViewManager interface
          if(parent != null && parent instanceof ViewManager){
            ViewManager viewManager = (ViewManager) parent;
            // remove the view from its parent
            viewManager.removeView(SplashView.this);
          } else if(BuildConfig.DEBUG) {
            // even though we had to remove the view we either don't have a parent, or the parent does not implement the method
            // necessary to remove the view, therefore create a warning log (but only do this if we are in DEBUG mode)
            Log.w(TAG, "splash view not removed after animation ended because no ViewManager parent was found");
          }
        }
        
        // notify the listener of animation end (if listener is set)
        if(listener != null){
          listener.onEnd();
        }
      }
    });
    
    // start the animation using post so that the animation does not start if the view is not in foreground
    post(new Runnable(){
      @Override
      public void run(){
        // start the animation in reverse to get the desired effect from the interpolator
        animator.reverse();
      }
    });
  }
  
  @Override
  protected void onSizeChanged (int w, int h, int oldw, int oldh) {
    // do whatever the super wants to do
    super.onSizeChanged(w, h, oldw, oldh);
    
    // cache the width and height for easy access
    mWidth = w;
    mHeight = h;
    
    // re-set the max scale because the size has changed
    setMaxScale();
  }
  
  @Override
  protected void onDraw(Canvas canvas){
    // calculate the scaled width and height
    float iconWidth = mIconWidth * mCurrentScale;
    float iconHeight = mIconHeight * mCurrentScale;
    
    // calculate all corners of the icon rectangle with the icon in the middle
    mIconRect.left = (mWidth - iconWidth) / 2;
    mIconRect.right = mIconRect.left + iconWidth;
    mIconRect.top = (mHeight - iconHeight) / 2;
    mIconRect.bottom = mIconRect.top + iconHeight;
    
    // if the scale is less than 2, then don't enable the transparent hole yet
    if(mCurrentScale < 2){
      // draw a bgColored rectangle right underneath the icon, make the rectangle a little bigger using the threshold value
      mPaint.setColor(mBgColor);
      canvas.drawRect(mIconRect.left, mIconRect.top, mIconRect.right, mIconRect.bottom, mPaint);
    }
    
    // draw 4 rectangles around the icon to cover the entire screen, use threshold value to expand and overlap the rectangles
    mPaint.setColor(mIconColor);
    canvas.drawRect(0, 0, mIconRect.left, mHeight, mPaint);
    canvas.drawRect(mIconRect.left, 0, mIconRect.right, mIconRect.top, mPaint);
    canvas.drawRect(mIconRect.left, mIconRect.bottom, mIconRect.right, mHeight, mPaint);
    canvas.drawRect(mIconRect.right, 0, mWidth, mHeight, mPaint);
    
    if(mIcon != null){
      // save the current canvas state
      canvas.save();
      // translate the canvas to draw the icon
      canvas.translate(mIconRect.left, mIconRect.top);
      // scale the canvas for the desired icon scale
      canvas.scale(mCurrentScale, mCurrentScale);
      // draw the icon on the canvas
      mIcon.draw(canvas);
      // restore the canvas to its original state
      canvas.restore();
    } else if(BuildConfig.DEBUG){
      // if the icon is not set then log a warning message if we are in debug mode, this message will be logged every time onDraw is called
      Log.w(TAG, "icon is not set when the view needs to be drawn");
    }
  }
}
