package com.jerrybean.qqslidemenu.view;

import com.jerrybean.qqslidemenu.util.ColorUtil;
import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.view.ViewHelper;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SlideMenu extends FrameLayout {

	private ViewDragHelper mViewDragHelper;
	private View menuView;
	private View mainView;
	
	private float dragRange;
	/** 屏幕宽度 */
	private int width;

	private FloatEvaluator floatEvaluator ;//float的计算器
	private IntEvaluator intEvaluator;//int的计算器
	private OnDragStateChangeListener mListener;
	private DragState mCurrentState = DragState.ISCLOSE;
	
	public SlideMenu(Context context) {
		super(context);
		init();
	}

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlideMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mViewDragHelper = ViewDragHelper.create(this, callback);
		floatEvaluator = new FloatEvaluator();
		intEvaluator = new IntEvaluator();
	}

	// 在此方法里初始化view
	@Override
	protected void onFinishInflate() {
		if (getChildCount() != 2) {
			throw new IllegalAccessError("SlideMenu only have two child!");
		}
		menuView = getChildAt(0);
		mainView = getChildAt(1);
	}

	/**
	 * 该方法在onMeasure执行完之后执行，那么可以在该方法中初始化自己和子View的宽高
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = getMeasuredWidth();
		dragRange = width * 0.6f;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
	case MotionEvent.ACTION_DOWN:
		startX = ev.getX();
			startY = ev.getY();
		break;
		case MotionEvent.ACTION_MOVE:
		endX = ev.getX();
		endY = ev.getY();
			if(Math.abs(endX-startX)-Math.abs(endY-startY)<30){
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return mViewDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		mViewDragHelper.processTouchEvent(event);
		return true;
	}

	private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
		/**
		 * 用于判断是否捕获当前child的触摸事件 child: 当前触摸的子View return: true:就捕获并解析 false：不处理
		 */
		@Override
		public boolean tryCaptureView(View child, int pointerId) {

			return child == menuView || child == mainView;
		}

		/**
		 * 获取view水平方向的拖拽范围,但是目前不能限制边界,返回的值目前用在手指抬起的时候view缓慢移动的动画世界的计算上面; 最好不要返回0
		 */
		@Override
		public int getViewHorizontalDragRange(View child) {

			return (int) dragRange;
		}

		/**
		 * 获取view竖直方向的拖拽范围,但是目前不能限制边界,返回的值目前用在手指抬起的时候view缓慢移动的动画世界的计算上面; 最好不要返回0
		 */
		public int getViewVerticalDragRange(View child) {
			return child.getTop();
		};

		/**
		 * 控制child在水平方向的移动 left: 表示ViewDragHelper认为你想让当前child的left改变的值,
		 * left=child.getLeft()+dx dx: 本次child水平方向移动的距离 return:
		 * 表示你真正想让child的left变成的值
		 */
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			//
			if (child == mainView) {
				if (left < 0)
					left = 0;
				if (left > dragRange)
					left = (int) dragRange;
			}
			return left;
		}

		/**
		 * 当child的位置改变的时候执行,一般用来做其他子View的伴随移动 changedView：位置改变的child
		 * left：child当前最新的left top: child当前最新的top dx: 本次水平移动的距离 dy: 本次垂直移动的距离
		 */
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			if (changedView == menuView) {
				menuView.layout(0, 0, menuView.getMeasuredWidth(),
						menuView.getMeasuredHeight());

				int slideWidth = mainView.getLeft() + dx;
				if (slideWidth > dragRange)
					slideWidth = (int) dragRange;
				mainView.layout(slideWidth, 0,
						slideWidth + mainView.getMeasuredWidth(),
						mainView.getMeasuredHeight());
			}
			//计算滑动百分比
			float fraction = mainView.getLeft()/dragRange;
			//System.out.println("fraction======="+fraction);
			//
			executeAnima(fraction);
			//回调//
			if(fraction == 0 &&mCurrentState!=DragState.ISCLOSE){
				mCurrentState = DragState.ISCLOSE;
				if(mListener!=null){
					mListener.onClose();
				}
			}else if(fraction >0.99f &&mCurrentState!=DragState.ISOPEN){
				mCurrentState = DragState.ISOPEN;
				if(mListener!= null){
					mListener.onOpen();
				}
			}
			if(mListener != null){
				mListener.onDraging(fraction);
			}
			
			
		};

		// xvel:x轴的移动速度
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			if (mainView.getLeft() < dragRange / 2) {
				close();
			} else {
				open();
			}
			//如果滑动的速度大于2000，并且当前状态不是打开的时候
			if(xvel>2000 && mCurrentState!=DragState.ISOPEN){
				open();
			}else if(xvel<-2000 && mCurrentState !=DragState.ISCLOSE){
				close();
			}
			
		}
	};
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	public void open() {
		mViewDragHelper.smoothSlideViewTo(mainView, (int) dragRange,
				mainView.getTop());
		ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
	}

	public void close() {
		// 向左移动
		mViewDragHelper.smoothSlideViewTo(mainView, 0,
				mainView.getTop());
		ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
	}
	

	public void computeScroll() {
		if (mViewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
		}

	}

	/**
	 * 初始化动画
	 * @param fraction 偏移百分比
	 */
	protected void executeAnima(float fraction) {
		//主界面缩小动画
		ViewHelper.setScaleX(mainView,floatEvaluator.evaluate(fraction, 1f, 0.8f));
		ViewHelper.setScaleY(mainView,floatEvaluator.evaluate(fraction, 1f, 0.8f));
		//侧边栏放大动画
		ViewHelper.setScaleX(menuView, floatEvaluator.evaluate(fraction, 0.5f, 1f));
		ViewHelper.setScaleY(menuView, floatEvaluator.evaluate(fraction, 0.5f, 1f));
		//侧边栏淡入动画
		ViewHelper.setAlpha(menuView, floatEvaluator.evaluate(fraction, 0.3f, 1f));
		//侧边栏平移动画
		ViewHelper.setTranslationX(menuView, floatEvaluator.evaluate(fraction, -menuView.getMeasuredWidth(),0));
		//给SlideMenu添加黑色遮罩效果
		getBackground().setColorFilter((Integer) ColorUtil.evaluateColor(fraction, Color.BLACK, Color.TRANSPARENT)
				,Mode.SRC_OVER);
	};
	
	/**
	 *回调 
	 */ 
	public interface OnDragStateChangeListener{
		//侧边栏打开的回调
		public void onOpen();
		//侧边栏关闭的回调
		public void onClose();
		//正在拖拽的回调
		public void onDraging(float fraction);
	}
	
	public void setOnDragStateChangeListener(OnDragStateChangeListener listener){
		mListener = listener;
	}
	
	//得到当前的状态
	public DragState getCurrentState(){
		return mCurrentState;
	}
	//定义开关状态的枚举
	public enum DragState{
		ISOPEN,ISCLOSE;
	}
	
}
