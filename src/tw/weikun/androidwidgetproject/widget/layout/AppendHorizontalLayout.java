package tw.weikun.androidwidgetproject.widget.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class AppendHorizontalLayout extends ViewGroup {

	public static final String TAG = AppendHorizontalLayout.class.getSimpleName();
	
	private static final int VIEW_MARGIN = 12;

	public AppendHorizontalLayout(Context context) {
		super(context);
	}

	public AppendHorizontalLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AppendHorizontalLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		Log.d(TAG, "child size: "+count);		
		Log.d(TAG, String.format("layout: %d  %d  %d  %d", l, t, r, b));
		
		int x=VIEW_MARGIN,y=VIEW_MARGIN;
		int ph = 0;//previous height.
		int fh = 0;//first object's height per row.
		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			
			if((x+width) > r){
				x = VIEW_MARGIN;
				
				if(ph==0){
					ph = Math.max(fh, height);
				}else if(ph==-1){
					ph = fh;
				}
				
				y += VIEW_MARGIN + ph;
				
				ph = -1;
				fh = height;
			}else{
				ph = Math.max(fh, height);
				if(fh==0) fh = height;
			}
			child.layout(x, y, x+width, y+height);
			x+= VIEW_MARGIN + width;
		}
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int totalHeight=0;
		for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            
            totalHeight+=child.getMeasuredHeight() + 2*VIEW_MARGIN;
        }
		
		if ((getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE && 
			    getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
			totalHeight/=2;
        }
		Log.d(TAG, "measure height is "+totalHeight);
        setMeasuredDimension(widthMeasureSpec, totalHeight);
	}
	
}
