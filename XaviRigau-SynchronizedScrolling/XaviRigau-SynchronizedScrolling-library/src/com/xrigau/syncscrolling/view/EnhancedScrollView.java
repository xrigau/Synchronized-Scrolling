package com.xrigau.syncscrolling.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


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

	static interface OnScrollChangedListener {
		public void onVerticalScrollChanged(int offsetY);
	}
}