package com.xrigau.syncscrolling.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * <p>Extension of a {@link ScrollView} that you should use to contain one or more
 * {@link SynchronizedRelativeLayout}. This class overrides
 * {@link View#onScrollChanged()} to notify scroll changes to a list of
 * {@link OnScrollChangedListener}. This allows you having one or more
 * {@link SynchronizedRelativeLayout} child views. As an ScrollView can only
 * have one direct child, if you want to have more than one SynchronizedLayout,
 * you should use a layout to contain them. This is how you can use it with one
 * SynchronizedRelativeLayout. Nothing different from a ScrollView:</p>
 * 
 * <pre>
 * &lt;com.xrigau.syncscrolling.view.EnhancedScrollView
 *         android:fadingEdge="none"
 *         android:layout_width="fill_parent"
 *         android:layout_height="wrap_content" &gt;
 * 
 *         &lt;com.xrigau.syncscrolling.view.SynchronizedRelativeLayout
 *             ... your stuff... &gt;
 *             ... your views...
 *         &lt;/com.xrigau.syncscrolling.view.SynchronizedRelativeLayout&gt;
 *     &lt;/com.xrigau.syncscrolling.view.EnhancedScrollView&gt;
 * 
 * </pre>
 * 
 * <p>In case that you want to have two (or more), your xml should look similar to
 * this:</p>
 * 
 * <pre>
 * &lt;com.xrigau.syncscrolling.view.EnhancedScrollView
 *         android:layout_width="fill_parent"
 *         android:layout_height="wrap_content"
 *         android:fadingEdge="none" &gt;
 * 
 *         &lt;LinearLayout
 *             android:layout_width="fill_parent"
 *             android:layout_height="wrap_content"
 *             android:orientation="vertical" &gt;
 * 
 *             &lt;com.xrigau.syncscrolling.view.SynchronizedRelativeLayout
 *                 ...your stuff... &gt;
 *                 ... your views...
 *             &lt;/com.xrigau.syncscrolling.view.SynchronizedRelativeLayout&gt;
 * 
 *             &lt;com.xrigau.syncscrolling.view.SynchronizedRelativeLayout
 *                 ...other stuff... &gt;
 *                 ... other views...
 *             &lt;/com.xrigau.syncscrolling.view.SynchronizedRelativeLayout&gt;
 *         &lt;/LinearLayout&gt;
 *     &lt;/com.xrigau.syncscrolling.view.EnhancedScrollView&gt;
 * </pre>
 * 
 * <p>Fore more code, take a look at {@link SynchronizedRelativeLayout}'s Javadoc.</p>
 * 
 * @author Xavi Rigau
 * @version 1.0.0
 * 
 */
public class EnhancedScrollView extends ScrollView {

	private ArrayList<OnScrollChangedListener> mOnScrollListener = new ArrayList<EnhancedScrollView.OnScrollChangedListener>();

	public EnhancedScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EnhancedScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EnhancedScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		for (OnScrollChangedListener oscl : mOnScrollListener) {
			oscl.onVerticalScrollChanged(t);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		post(new Runnable() {

			@Override
			public void run() {
				for (OnScrollChangedListener oscl : mOnScrollListener) {
					oscl.onVerticalScrollChanged(getScrollY());
				}
				invalidate();
			}
		});
	}

	public void addOnScrollListener(OnScrollChangedListener onScrollListener) {
		this.mOnScrollListener.add(onScrollListener);
	}

	/**
	 * Interface used to notify the amount of vertical scroll offset of this
	 * view.
	 * 
	 * @author Xavi Rigau
	 * 
	 */
	static interface OnScrollChangedListener {

		public void onVerticalScrollChanged(int offsetY);
	}
}