/**
 * 
 */
package tw.weikun.androidwidgetproject.widget.view.textview;

import tw.weikun.androidwidgetproject.R;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * From android 4.1 / 4.2, the following Roboto font families are available:
 * <pre><code>
 * android:fontFamily="sans-serif" // roboto regular
 * android:fontFamily="sans-serif-light" // roboto light
 * android:fontFamily="sans-serif-condensed" // roboto condensed
 * android:fontFamily="sans-serif-thin" // roboto thin (android 4.2) </code>
 * </pre><br>
 * In code, You can also use Typeface.create().
 * 
 * <br>
 * For example, Typeface.create("sans-serif-light", Typeface.NORMAL).
 * <p>
 * @author weikunlu
 * 
 */
public class FontTextView extends TextView {

	static AssetManager mAssetManager;

	public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializedAssetManager(context);
		renderCustomAttr(context, attrs);
	}

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializedAssetManager(context);
		renderCustomAttr(context, attrs);
	}

	void initializedAssetManager(Context context) {
		if (mAssetManager == null) {
			mAssetManager = context.getAssets();
		}
	}

	void renderCustomAttr(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.FontTextView);

		final int N = typedArray.getIndexCount();
		for (int i = 0; i < N; ++i) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.FontTextView_fontStyle:
				String fontStyle = typedArray.getString(attr);
				setFontStyle(fontStyle);
				break;
			}
		}
		typedArray.recycle();
	}

	private void setFontStyle(String font) {
		try {
			Typeface createFromAsset = Typeface.createFromAsset(mAssetManager,
					"fonts/" + font);
			setTypeface(createFromAsset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
