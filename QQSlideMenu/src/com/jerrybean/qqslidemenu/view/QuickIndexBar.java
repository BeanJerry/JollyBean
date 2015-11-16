package com.jerrybean.qqslidemenu.view;

import com.jerrybean.qqslidemenu.R;

import android.R.interpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexBar extends View {

	private Paint paint;

	private String[] quickIndex = new String[] { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	// 格子的高度
	private float cellSize;

	public QuickIndexBar(Context context) {
		super(context);
		init();
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(getResources().getDimension(R.dimen.quick_size));
		paint.setColor(Color.WHITE);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// 每一格的高度
		cellSize = getMeasuredHeight() * 1f / quickIndex.length;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		paint.setTextAlign(Align.CENTER);

		for (int i = 0; i < quickIndex.length; i++) {
			// 字母的高度
			int textSize = getTextSize(quickIndex[i]);
			paint.setColor(lastIndex == i?Color.BLACK:Color.GRAY);
			// 字母所在的位置= 格子高度的一半+字母高度的一半+格子的数量
			canvas.drawText(quickIndex[i], getMeasuredWidth() / 2, cellSize / 2
					+ textSize / 2 + i * cellSize, paint);
		}
	}

	/**
	 * @param text
	 *            要测量的字母
	 * @return 字母的高度
	 */
	protected int getTextSize(String text) {
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		return bounds.height();
	}
	private int lastIndex = -1;//记录上一个字母的索引

	private onTouchLetterListener listener;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
		case MotionEvent.ACTION_MOVE:
			float MoveY = event.getY();
			
			int index = (int) (MoveY/cellSize);
			//如果当前索引和上一个索引不相同才将选中的字母回调出去
			if(index!=lastIndex){
				//System.out.println(quickIndex[index]);
				if(index>=0&&index<quickIndex.length){
					if(listener!=null){
						listener.onTouchLetter(quickIndex[index]);
					}
				}
			}
			lastIndex = index;
			setBackgroundColor(getResources().getColor(R.color.quickindex));
			break;
		case MotionEvent.ACTION_UP:
			//重置索引
			lastIndex = -1;
			setBackgroundColor(Color.WHITE);
			break;
		}
		invalidate();
		return true;
	}
	
	public void setOnTouchLetterListener(onTouchLetterListener listener){
		this.listener = listener;
	}
	public interface onTouchLetterListener{
		void onTouchLetter(String quickIndex);
	}
}
