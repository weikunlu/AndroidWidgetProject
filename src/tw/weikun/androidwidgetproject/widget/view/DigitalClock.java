package tw.weikun.androidwidgetproject.widget.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;

public class DigitalClock extends View {
	
	public static final String TAG = DigitalClock.class.getSimpleName();
	
	private static final String FORMAT_TIME = "hh:mm";
	private static final String FORMAT_TIME24H = "HH:mm";
	private static final String FORMAT_APM = "aa";
	private static final int GAP_SPACE = 20;
	
	private static SimpleDateFormat sdformat = new SimpleDateFormat(FORMAT_TIME24H);
	
	Paint mTextPaint, mTextPaint2;
    int mAscent;
	String mTimeString = "00:00";
	String mTimeString2 = "AM";
	Rect rect = new Rect();
	
	Calendar mCalendar;
	Runnable mTicker;
	Handler mHandler;
	
    static final float SHADOW_LARGE_RADIUS = 3.0f;
    static final float SHADOW_Y_OFFSET = 2.0f;
    static final int SHADOW_LARGE_COLOUR = 0xDD000000;
	
	public DigitalClock(Context context) {
		super(context);
		initClock();
	}
	
	public DigitalClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initClock();
	}

	public DigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		initClock();
	}

	/**
	 * initiated clock theme and got time instance.
	 */
	void initClock(){
		int timeFontSize = 90;
		int ampmFontSize = 36;
		if ((getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
			        Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			timeFontSize = 128;
			ampmFontSize = 42;
		}
		
		mTextPaint = new Paint();
		mTextPaint.setTextSize(timeFontSize);
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setShadowLayer(SHADOW_LARGE_RADIUS, 0.0f, SHADOW_Y_OFFSET, SHADOW_LARGE_COLOUR);
		mTextPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		mAscent = (int) mTextPaint.ascent();
		
		mTextPaint2 = new Paint();
		mTextPaint2.setTextSize(ampmFontSize);
		mTextPaint2.setColor(Color.WHITE);
		mTextPaint2.setShadowLayer(SHADOW_LARGE_RADIUS, 0.0f, SHADOW_Y_OFFSET, SHADOW_LARGE_COLOUR);
		mTextPaint2.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		setMeasuredDimension(widthMeasureSpec, measureHeight(heightMeasureSpec));
	}
	
	/**
     * measure height of the view.
     * 
     * @param measureSpec A measureSpec packed into int type
     * @return The height of the view, honoring constraints from measureSpec
     */
	private int measureHeight(int measureSpec) {
		 
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
 
        mAscent = (int) mTextPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
                    + getPaddingBottom();
            //Log.i(TAG, "Height mTextPaint.descent(): "+mTextPaint.descent());
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
	
	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawText(mTimeString, getPaddingLeft(), getPaddingTop() - mAscent, mTextPaint);
		
		//grab the width of the time string into Rect object
		mTextPaint.getTextBounds(mTimeString, 0, mTimeString.length(), rect);
		
		canvas.drawText(mTimeString2, getPaddingLeft()+rect.width()+GAP_SPACE, getPaddingTop() - mAscent, mTextPaint2);
		
	}
	
	void setText(CharSequence text, CharSequence text2){
		mTimeString = text.toString();
		
		mTimeString2 = text2.toString();
		
		invalidate();
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onAttachedToWindow()
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		mHandler = new Handler();
		
		mTicker = new Runnable() {
			@Override
			public void run() {
				boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
				
				long current = System.currentTimeMillis();
				mCalendar.setTimeZone(TimeZone.getDefault());
				mCalendar.setTimeInMillis(current);
				
				if(is24HourFormat){
					setText(sdformat.format(mCalendar.getTime()), "");
				}else{
					setText(DateFormat.format(FORMAT_TIME, mCalendar), DateFormat.format(FORMAT_APM, mCalendar));
				}
				
				int sec = mCalendar.get(Calendar.SECOND);
				
				mHandler.postDelayed(mTicker, (60-sec)*1000);
			}
		};
		mHandler.postAtTime(mTicker, 10);
		
	}
	
	public void refreshView(){
		
		boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
		
		long current = System.currentTimeMillis();
		mCalendar.setTimeZone(TimeZone.getDefault());
		mCalendar.setTimeInMillis(current);
		
		if(is24HourFormat){
			setText(sdformat.format(mCalendar.getTime()), "");
		}else{
			setText(DateFormat.format(FORMAT_TIME, mCalendar), DateFormat.format(FORMAT_APM, mCalendar));
		}
		
	}

}
