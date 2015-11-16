package com.jerrybean.qqslidemenu.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.jerrybean.qqslidemenu.util.ColorUtil;
import com.nineoldandroids.view.ViewHelper;

public class DragLayout extends FrameLayout {
	private ViewDragHelper mViewDragHelper;
	private View pinkView;
	private View oraView;

	public DragLayout(Context context) {
		super(context);
		init();
	}

	public DragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mViewDragHelper = ViewDragHelper.create(this, mCallback);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		pinkView = getChildAt(0);
		oraView = getChildAt(1);
	}

	// 设置布局拜访位置
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int mleft = getPaddingLeft();
		int mtop = getPaddingTop();
		pinkView.layout(mleft, mtop, pinkView.getMeasuredWidth(),
				pinkView.getMeasuredHeight());
		oraView.layout(mleft, pinkView.getBottom(),
				pinkView.getMeasuredWidth(),
				pinkView.getBottom() + oraView.getMeasuredHeight());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mViewDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mViewDragHelper.processTouchEvent(event);
		return true;
	}

	private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return child == pinkView || child == oraView;
		}

		public int getViewHorizontalDragRange(View child) {

			return getMeasuredWidth() - child.getMeasuredWidth();
		};

		@Override
		public int getViewVerticalDragRange(View child) {

			return getMeasuredHeight() - child.getMeasuredHeight();
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (left < 0)
				left = 0;
			if (left > getMeasuredWidth() - pinkView.getMeasuredWidth()) {
				left = getMeasuredWidth() - pinkView.getMeasuredWidth();
			}
			return left;
		}

		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			int orgMaxHight = getMeasuredHeight()
					- pinkView.getMeasuredHeight()
					- oraView.getMeasuredHeight();
			if (child == pinkView) {
				if (top < 0)
					top = 0;
				if (top > orgMaxHight)
					top = orgMaxHight;
			}
			if (child == oraView) {
				if (top < pinkView.getMeasuredHeight()) {
					top = pinkView.getMeasuredHeight();
				}
				if (top > getMeasuredHeight() - oraView.getMeasuredHeight()) {
					top = getMeasuredHeight() - oraView.getMeasuredHeight();
				}
			}
			return top;
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			if (changedView == pinkView) {
				oraView.layout(oraView.getLeft() + dx, oraView.getTop() + dy,
						oraView.getRight() + dx, oraView.getBottom() + dy);
			}

			else if (changedView == oraView) {
				// blueView移动的时候需要让redView跟随移动
				pinkView.layout(pinkView.getLeft() + dx,
						pinkView.getTop() + dy, pinkView.getRight() + dx,
						pinkView.getBottom() + dy);
			}
			float fraction = changedView.getLeft()*1f
					/ (getMeasuredWidth() - changedView.getMeasuredWidth());
			System.out.println("fraction+++++++++"+fraction);
			executeAnim(fraction);
		}
		/**
		 * 手指抬起的执行该方法， releasedChild：当前抬起的view xvel: x方向的移动的速度 正：向右移动， 负：向左移动
		 * yvel: y方向移动的速度
		 */
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			int centerLeft = getMeasuredWidth() / 2
					- releasedChild.getMeasuredWidth() / 2;
			if (releasedChild.getLeft() < centerLeft) {
				// 在左半边，应该向左缓慢移动
				mViewDragHelper.smoothSlideViewTo(releasedChild, 0,
						releasedChild.getTop());
				ViewCompat.postInvalidateOnAnimation(DragLayout.this);
			} else {
				// 在右半边，应该向右缓慢移动
				mViewDragHelper.smoothSlideViewTo(releasedChild,
						getMeasuredWidth() - releasedChild.getMeasuredWidth(),
						releasedChild.getTop());
				ViewCompat.postInvalidateOnAnimation(DragLayout.this);
			}
		}
	};

	protected void executeAnim(float fraction) {

		ViewHelper.setRotation(oraView, -360 * fraction);
		ViewHelper.setRotation(pinkView, 360 * fraction);

		// 设置过度颜色的渐变
		oraView.setBackgroundColor((Integer) ColorUtil.evaluateColor(fraction,
				Color.RED, Color.GREEN));
	}
	public void computeScroll() {
		if (mViewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(DragLayout.this);
		}
	};

}
