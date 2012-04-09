package com.xrigau.syncscrolling.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.xrigau.syncscrolling.view.EnhancedScrollView.OnScrollChangedListener;

public class SynchronizedScrollLayout extends RelativeLayout implements OnScrollChangedListener {

	private int mPlaceholderViewId, mSynchronizedViewId;
	private View mSynchronizedView, mPlaceholderView;
	private int mGravity = Gravity.LEFT;
	 
	public SynchronizedScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public SynchronizedScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SynchronizedScrollLayout);
		
		mGravity = a.getInt(R.styleable.SynchronizedScrollLayout_syncView_gravity, mGravity);
		
		RuntimeException e = null;
		mPlaceholderViewId = a.getResourceId(R.styleable.SynchronizedScrollLayout_placeholderView, 0);
        if (mPlaceholderViewId == 0) {
                e = new IllegalArgumentException(a.getPositionDescription() + ": The placeholderView attribute is required and must refer to a valid child.");
        }
        mSynchronizedViewId = a.getResourceId(R.styleable.SynchronizedScrollLayout_synchronizedView, 0);
        if (mSynchronizedViewId == 0) {
                e = new IllegalArgumentException(a.getPositionDescription() + ": The synchronizedView attribute is required and must refer to a valid child.");
        }
        a.recycle();
        
        if (e != null) {
                throw e;
        }
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		findEnhancedScrollViewRecursive(getParent());
	}
	
	private void findEnhancedScrollViewRecursive(ViewParent parent) {
		if (parent == null) {
			throw new IllegalArgumentException("A SynchronizedScrollLayout must be inside EnhancedScrollView, directly or indirectly.");
		}
		
		if (parent instanceof EnhancedScrollView) {
			((EnhancedScrollView) parent).addOnScrollListener(this);
		} else {
			findEnhancedScrollViewRecursive(parent.getParent());
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mPlaceholderView = findViewById(mPlaceholderViewId);
		mSynchronizedView = findViewById(mSynchronizedViewId);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		android.view.ViewGroup.LayoutParams lp = mPlaceholderView.getLayoutParams();
		if (lp.height != mSynchronizedView.getMeasuredHeight()) {
			lp.height = mSynchronizedView.getMeasuredHeight();
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		int top = mPlaceholderView.getTop();
		int left = mSynchronizedView.getLeft();
		
		switch (mGravity) {
		case Gravity.CENTER_HORIZONTAL:
			left = (getWidth() - mSynchronizedView.getMeasuredWidth()) / 2;
			break;
		case Gravity.RIGHT:
			left = getWidth() - mSynchronizedView.getMeasuredWidth();
			break;
		}
			
		mSynchronizedView.layout(left, top, left + mSynchronizedView.getMeasuredWidth(), top + mSynchronizedView.getMeasuredHeight());
		mPlaceholderView.layout(left, top, left + mSynchronizedView.getMeasuredWidth(), top + mSynchronizedView.getMeasuredHeight());
	}

	@Override
	public void onVerticalScrollChanged(int offsetY) {
		offsetY -= getTop();
		if (offsetY >= mPlaceholderView.getTop()) {
			mSynchronizedView.offsetTopAndBottom(offsetY - mSynchronizedView.getTop());
		} else {
			// The offset should be 0, but it doesn't work, so this is the correct number.
			mSynchronizedView.offsetTopAndBottom(mPlaceholderView.getTop() - mSynchronizedView.getTop());
		}
	}
	
	public static class Gravity {
		public static final int LEFT = 0;
		public static final int CENTER_HORIZONTAL = 1;
		public static final int RIGHT = 2;
	}
}