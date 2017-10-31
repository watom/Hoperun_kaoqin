package com.watom999.www.hoperun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ETitleBar extends RelativeLayout {

	private ImageView mBackImageView;
	private TextView mBackTextView;
	private ImageView mBackIconView;
	private TextView mTitleTextView;
	private ImageView mTitleIconView;
	private ImageView mRightImageView1;
	private ImageView mRightImageView2;
	private TextView mRightTextView;
	private RelativeLayout mTitleContainer;
	private LinearLayout mBackContainer;
	private RelativeLayout mMain;

	private Paint mPaint;
	private int mLineHeight;
	private boolean mShowLine = true;
	private String mTitleText;

	public ETitleBar(Context context) {
		this(context, null);
	}

	public ETitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ETitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWillNotDraw(false);
		LayoutInflater.from(context).inflate(R.layout.e_title_bar, this, true);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.E_TitleBar_View);
		mTitleText = a.getString(R.styleable.E_TitleBar_View_titlebar_text);
		a.recycle();
		initView(context);
	}

	private void initView(final Context context) {
		mBackImageView = (ImageView) findViewById(R.id.iv_back);
		mBackIconView = (ImageView) findViewById(R.id.iv_back_right);
		mTitleIconView = (ImageView) findViewById(R.id.iv_title_right);
		mRightImageView1 = (ImageView) findViewById(R.id.imageview_1);
		mRightImageView2 = (ImageView) findViewById(R.id.imageview_2);
		mBackTextView = (TextView) findViewById(R.id.tv_left);
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
		mRightTextView = (TextView) findViewById(R.id.tv_right);
		mTitleContainer = (RelativeLayout) findViewById(R.id.rl_title);
		mBackContainer = (LinearLayout) findViewById(R.id.ll_back_container);
		mMain = (RelativeLayout) findViewById(R.id.rl_titlebar);
		setBackBtnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mBackImageView.getVisibility() == View.VISIBLE
						&& context instanceof Activity) {
					((Activity) context).finish();
				}
			}
		});

		setTitle(mTitleText);
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#049888"));
		mPaint.setAntiAlias(true);
		mLineHeight = this.getResources().getDimensionPixelSize(
				R.dimen.e_divider_line_height);
	}

	/**
	 * 是否显示返回图标
	 *
	 * @param visible
	 */
	public void setBackBtnVisible(boolean visible) {
		if (visible) {
			mBackImageView.setVisibility(View.VISIBLE);
		} else {
			mBackImageView.setVisibility(View.GONE);
		}
	}

	public TextView getmBackTextView() {
		return mBackTextView;
	}

	/**
	 * 是否显示左侧返回按钮和文字
	 *
	 * @param visible
	 */
	public void setBackContainerVisibile(boolean visible) {
		if (visible) {
			mBackContainer.setVisibility(View.VISIBLE);
		} else {
			mBackContainer.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 是否显示左侧文字
	 *
	 * @param visible
	 */
	public void setBackTextVisible(boolean visible) {
		if (visible) {
			mBackTextView.setVisibility(View.VISIBLE);
		} else {
			mBackTextView.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置返回监听
	 *
	 * @param listener
	 */
	public void setBackBtnClickListener(View.OnClickListener listener) {
		mBackContainer.setOnClickListener(listener);
	}

	/**
	 * 设置左部分文字监听
	 *
	 * @param listener
	 */
	public void setLeftClickListener(View.OnClickListener listener) {
		setBackBtnClickListener(listener);
	}

	/**
	 * 设置左侧文字，默认不显示
	 *
	 * @param text
	 */
	public void setBackBtnText(String text) {
		mBackTextView.setText(text);
	}

	/**
	 * 是否显示左侧文字后的小图标,默认为下箭头"∨"（可通过{@link #getLeftIconView()} 来修改图标），不显示
	 *
	 * @param visible
	 */
	public void setLeftIconVisible(boolean visible) {
		if (visible) {
			mBackIconView.setVisibility(View.VISIBLE);
		} else {
			mBackIconView.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		mTitleTextView.setText(title);
	}

	/**
	 * 设置标题点击事件监听
	 *
	 * @param listener
	 */
	public void setTitleClickListener(View.OnClickListener listener) {
		mTitleContainer.setOnClickListener(listener);
	}

	/**
	 * 是否显示标题文字后的小图标,默认为下箭头"∨"（可通过{@link #getTitleIconView()} 来修改图标），不显示
	 *
	 * @param visible
	 */
	public void setTitleRightIconVisible(boolean visible) {
		if (visible) {
			mTitleIconView.setVisibility(View.VISIBLE);
		} else {
			mTitleIconView.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置右侧1的按钮监听事件
	 *
	 * @param listener
	 */
	public void setRightBtn1ClickListener(View.OnClickListener listener) {
		mRightImageView1.setOnClickListener(listener);
	}

	/**
	 * 设置右侧2的按钮监听事件
	 *
	 * @param listener
	 */
	public void setRightBtn2ClickListener(View.OnClickListener listener) {
		mRightImageView2.setOnClickListener(listener);
	}

	/**
	 * 设置标题栏右侧文字监听事件
	 *
	 * @param listener
	 */
	public void setRightTextClickListener(View.OnClickListener listener) {
		mRightTextView.setOnClickListener(listener);
	}

	/**
	 * 设置右侧1按钮的图标
	 * 
	 * @param drawable
	 */
	public void setRightBtn1Icon(Drawable drawable) {
		mRightImageView1.setImageDrawable(drawable);
		mRightImageView1.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置右侧1按钮的图标
	 * 
	 * @param resId
	 */
	public void setRightBtn1Icon(int resId) {
		mRightImageView1.setImageResource(resId);
		mRightImageView1.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置标题栏右侧文字
	 * 
	 * @param text
	 */
	public void setRightText(String text) {
		mRightTextView.setText(text);
		mRightTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置右侧2按钮的图标
	 * 
	 * @param drawable
	 */
	public void setRightBtn2Icon(Drawable drawable) {
		mRightImageView2.setImageDrawable(drawable);
		mRightImageView2.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置右侧2按钮的图标
	 * 
	 * @param resId
	 */
	public void setRightBtn2Icon(int resId) {
		mRightImageView2.setImageResource(resId);
		mRightImageView2.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取左侧图标控件
	 * 
	 * @return
	 */
	public ImageView getLeftImageView() {
		return mBackImageView;
	}

	/**
	 * 获取左侧文字后面的图标view
	 * 
	 * @return
	 */
	public ImageView getLeftIconView() {
		return mBackIconView;
	}

	/**
	 * 获取标题文字后面的图标view
	 * 
	 * @return
	 */
	public ImageView getTitleIconView() {
		return mTitleIconView;
	}

	/**
	 * 获取中间文字区域的容器，可添加自定义的View（注：不影响标题栏左侧和右侧显示）
	 * 
	 * @return
	 */
	public RelativeLayout getTitleContainer() {
		return mTitleContainer;
	}

	/**
	 * 获取标题View
	 * 
	 * @return
	 */
	public RelativeLayout getMainView() {
		return mMain;
	}

	/**
	 * 获取标题栏
	 * 
	 * @return
	 */
	public TextView getTitleView() {
		return mTitleTextView;
	}

	/**
	 * 获取标题栏右侧按钮1View
	 * 
	 * @return
	 */
	public ImageView getRightView1() {
		return mRightImageView1;
	}

	/**
	 * 获取标题栏右侧按钮2View
	 * 
	 * @return
	 */
	public ImageView getRightView2() {
		return mRightImageView2;
	}

	/**
	 * 获取右侧文字View
	 * 
	 * @return
	 */
	public TextView getRightTextView() {
		return mRightTextView;
	}

	/**
	 * 是否显示标题栏底部线条
	 * 
	 * @param show
	 */
	public void setShowLine(boolean show) {
		mShowLine = show;
	}

	@SuppressLint("NewApi")
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (mShowLine) {
			canvas.drawRect(0, this.getHeight() - mLineHeight, this.getWidth(),
					this.getHeight(), mPaint);
		}
	}

}
