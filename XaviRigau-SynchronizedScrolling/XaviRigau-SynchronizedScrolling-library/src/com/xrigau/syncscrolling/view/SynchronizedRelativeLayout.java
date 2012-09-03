package com.xrigau.syncscrolling.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.xrigau.syncscrolling.view.EnhancedScrollView.OnScrollChangedListener;

/**
 * <p>Manages the vertical synchronized scrolling of a view in an
 * {@link EnhancedScrollView}. This class is an extension of
 * {@link RelativeLayout}.</p>
 * 
 * <p>There's a really good article that I've based on to create this library,
 * you can find it at <a href=
 * "http://www.pushing-pixels.org/2011/07/18/android-tips-and-tricks-synchronized-scrolling.html"
 * >Kirill Grouchnikov's blog</a>. The basics is that we'll have the sync view
 * (the one that will scroll or stay on top of the screen, depending of the
 * scroll position) and a placeholder (the dummy view) that will keep the space
 * in your layout. The sync view will be like floating sometimes over the dummy
 * view and sometimes will be on top of the screen, depending on the user
 * scroll.</p>
 * 
 * <p>You must place at least two views inside a SynchronizedRelativeLayout:</p>
 * <ul>
 * 	<li>Synchronized View: The view to does the synchronized scrolling.</li>
 * 	<li>Dummy Placeholder: A dummy view that acts as a placeholder so the
 * 	synchronized view will be put over it.</li>
 * </ul>
 * 
 * <p>This class can only be used from XML inflation.So you must declare both
 * the synchronizedView and the placeholderView attributes in your layout when
 * you use this class or you'll get an {@link IllegalArgumentException}. That
 * attributes must refer to valid id's of views that exist inside this
 * SynchronizedRelativeLayout.</p>
 * 
 * <p>In addition, this class must be directly or indirectly contained by an
 * {@link EnhancedScrollView} <b>always</b>, but you can have more than one.
 * </p>
 * 
 * <p>There's an example of how to use an SynchronizedRelativeLayout:</p>
 * 
 * <pre>
 *     &lt;com.xrigau.syncscrolling.view.EnhancedScrollView
 *         android:fadingEdge="none"
 *         android:layout_width="fill_parent"
 *         android:layout_height="wrap_content" &gt;
 * 
 *         &lt;com.xrigau.syncscrolling.view.SynchronizedRelativeLayout
 *             android:layout_width="fill_parent"
 *             android:layout_height="wrap_content"
 *             xrigau:placeholderView="@+id/base_view"
 *             xrigau:synchronizedView="@+id/synchronized_view" &gt;
 * 
 *             &lt;LinearLayout
 *                 android:layout_width="fill_parent"
 *                 android:layout_height="wrap_content"
 *                 android:orientation="vertical" &gt;
 * 
 *                 &lt;View
 *                     android:layout_width="fill_parent"
 *                     android:layout_height="100dip"
 *                     android:background="#00ff00" /&gt;
 * 				
 *                 &lt;!-- This is the dummy placeholder that will keep space for the synchronized view. --&gt;
 *                 &lt;View
 *                     android:id="@+id/base_view"
 *                     android:layout_width="fill_parent"
 *                     android:layout_height="0dp" /&gt;
 * 
 *                 &lt;View
 *                     android:layout_width="fill_parent"
 *                     android:layout_height="1000dip"
 *                     android:background="#00ff00" /&gt;
 *             &lt;/LinearLayout&gt;
 * 
 *             &lt;!-- This is the the synchronized view that will be put over the dummy placeholder and synchronized. --&gt;
 *             &lt;View
 *                 android:id="@+id/synchronized_view"
 *                 android:layout_width="fill_parent"
 *                 android:layout_height="100dip"
 *                 android:background="#ff0000" /&gt;
 *         &lt;/com.xrigau.syncscrolling.view.SynchronizedRelativeLayout&gt;
 *     &lt;/com.xrigau.syncscrolling.view.EnhancedScrollView&gt;
 * </pre>
 * 
 * @author Xavi Rigau.
 * @version 1.0.0
 */
public class SynchronizedRelativeLayout extends RelativeLayout implements OnScrollChangedListener {

	private int mPlaceholderViewId, mSynchronizedViewId;
	private View mSynchronizedView, mPlaceholderView;
	private int mGravity = Gravity.LEFT;
	
	private int mLastSyncViewPos = 0;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * 
	 * @param context
	 *            The context.
	 * @param attrs
	 *            Some attrs.
	 * @param defStyle
	 *            Style.
	 */
	public SynchronizedRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * 
	 * @param context
	 *            The context.
	 * @param attrs
	 *            Some attrs.
	 */
	public SynchronizedRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	/**
	 * <p>
	 * Init stuff.
	 * </p>
	 * 
	 * @param context
	 *            The context.
	 * @param attrs
	 *            The attributes.
	 */
	private void init(Context context, AttributeSet attrs) {
		final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SynchronizedRelativeLayout);

		mGravity = a.getInt(R.styleable.SynchronizedRelativeLayout_syncView_gravity, mGravity);

		RuntimeException e = null;
		mPlaceholderViewId = a.getResourceId(R.styleable.SynchronizedRelativeLayout_placeholderView, 0);
		if (mPlaceholderViewId == 0) {
			e = new IllegalArgumentException(a.getPositionDescription() + ": The placeholderView attribute is required and must refer to a valid child.");
		}
		mSynchronizedViewId = a.getResourceId(R.styleable.SynchronizedRelativeLayout_synchronizedView, 0);
		if (mSynchronizedViewId == 0) {
			e = new IllegalArgumentException(a.getPositionDescription() + ": The synchronizedView attribute is required and must refer to a valid child.");
		}
		a.recycle();

		if (e != null) {
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		findEnhancedScrollViewRecursive(getParent());
	}

	/**
	 * <p>
	 * Looks for a parent view (direct or indirect) that is an instance of
	 * {@link EnhancedScrollView} recursively, and add self to the list of
	 * scroll listeners.
	 * </p>
	 * 
	 * @param parent
	 *            The parent View to check.
	 */
	private void findEnhancedScrollViewRecursive(ViewParent parent) {
		if (parent == null) {
			throw new IllegalArgumentException("A SynchronizedRelativeLayout must be inside EnhancedScrollView, directly or indirectly.");
		}

		if (parent instanceof EnhancedScrollView) {
			((EnhancedScrollView) parent).addOnScrollListener(this);
		} else {
			findEnhancedScrollViewRecursive(parent.getParent());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mPlaceholderView = findViewById(mPlaceholderViewId);
		mSynchronizedView = findViewById(mSynchronizedViewId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		android.view.ViewGroup.LayoutParams lp = mPlaceholderView.getLayoutParams();
		if (lp.height != mSynchronizedView.getMeasuredHeight()) {
			lp.height = mSynchronizedView.getMeasuredHeight();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			super.onLayout(changed, l, t, r, b);
			int top = mPlaceholderView.getTop();
			int left = mSynchronizedView.getLeft();
	
			/*
			 * Adjust the left position depending on the syncView_gravity attribute.
			 */
			switch (mGravity) {
			case Gravity.CENTER_HORIZONTAL:
				left = (getWidth() - mSynchronizedView.getMeasuredWidth()) / 2;
				break;
			case Gravity.RIGHT:
				left = getWidth() - mSynchronizedView.getMeasuredWidth();
				break;
			}
	
			// TODO: Maybe adjust the left and right with the View padding?
			mSynchronizedView.layout(left, top, left + mSynchronizedView.getMeasuredWidth(), top + mSynchronizedView.getMeasuredHeight());
			mPlaceholderView.layout(left, top, left + mSynchronizedView.getMeasuredWidth(), top + mSynchronizedView.getMeasuredHeight());
			
			mLastSyncViewPos = getTop() + getHeight() - mSynchronizedView.getMeasuredHeight();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onVerticalScrollChanged(int offsetY) {
		// Offset relative to this layout's top.
		final int relativeOffsetY = offsetY - getTop();

		if (relativeOffsetY >= mPlaceholderView.getTop()) {
			// If scroll has reached the top of this view, we calculate another offset to move the sync view up.
			int reverseOffsetY = 0;
			if (offsetY > mLastSyncViewPos) {
				reverseOffsetY = offsetY - mLastSyncViewPos;
			}
			
			// Instead of repositioning the view, we adjust the top offset of it
			// to improve performance.
			mSynchronizedView.offsetTopAndBottom(relativeOffsetY - mSynchronizedView.getTop() - reverseOffsetY);
		} else {
			// The offset should be 0, but it doesn't work, so this is the
			// correct number.
			mSynchronizedView.offsetTopAndBottom(mPlaceholderView.getTop() - mSynchronizedView.getTop());
		}
	}

	/**
	 * <p>
	 * Static class containing the possible values that the syncView_layout
	 * attribute can have.
	 * </p>
	 * 
	 * @author Xavi Rigau
	 * 
	 */
	public static class Gravity {

		public static final int LEFT = 0;
		public static final int CENTER_HORIZONTAL = 1;
		public static final int RIGHT = 2;
	}
}