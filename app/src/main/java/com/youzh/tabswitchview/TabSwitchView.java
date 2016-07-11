package com.youzh.tabswitchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Tab 选择
 *
 * @author youzehong
 */
public class TabSwitchView extends LinearLayout {

    @BindView(R.id.iv_tab_bg)
    ImageView mIvTabBg;
    @BindView(R.id.ll_tab_content)
    LinearLayout mLLContent;
    @BindView(R.id.ll_tab)
    LinearLayout mTabLayout;

    private boolean isScrolling = false; // 手指是否在滑动
    private boolean isBackScrolling = false; // 手指离开后的回弹
    private long startTime = 0l;
    private long currentTime = 0l;

    private ArrayList<View> viewList = new ArrayList<View>();
    private ViewPager mViewPager;

    private TabEventListener mListener;

    public TabSwitchView(Context context) {
        this(context, null);
    }

    public TabSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.layout_tabs_switch, this);
        ButterKnife.bind(this, view);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabSwitch);
        int itemNum = a.getInteger(R.styleable.TabSwitch_itemNum, 3);
        String itemText = a.getString(R.styleable.TabSwitch_itemText);
        int itemBackground = a.getResourceId(R.styleable.TabSwitch_itemBackground, R.drawable.selector_bg_green_transparent);
        ColorStateList itemTextColor = a.getColorStateList(R.styleable.TabSwitch_itemTextColor);
        int itemSelBg = a.getResourceId(R.styleable.TabSwitch_itemSelBg, R.mipmap.bg_green);
        String[] text = itemText.split(",");

        mIvTabBg.setBackgroundResource(itemSelBg);
        ViewGroup.LayoutParams lp = mIvTabBg.getLayoutParams();
        lp.width = (DimenUtil.getScreenWidth(context) - mTabLayout.getPaddingLeft() - mTabLayout.getPaddingRight()) / itemNum;
        mIvTabBg.setLayoutParams(lp);
        tabWidth = lp.width;

        for (int i = 0; i < itemNum; i++) {
            TextView textView = new TextView(context);
            textView.setText(text[i]);
            textView.setTextColor(itemTextColor);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(mOnClickListener);
            textView.setTag(i);
            viewList.add(textView);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            textView.setLayoutParams(layoutParams);
            mLLContent.addView(textView);
        }
        setSelect(viewList.get(0));
        a.recycle();
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            setSelect(v);
            int tag = (Integer) v.getTag();
            mViewPager.setCurrentItem(tag);
            if (mListener != null) {
                mListener.tabPosition(tag);
            }
        }
    };
    private int tabWidth;

    public void setSelect(View tv) {
        for (View v : viewList) {
            v.setSelected(v == tv);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager != null) {
            mViewPager = viewPager;
            viewPager.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(final int position) {
                    movePositionX(position);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    currentTime = System.currentTimeMillis();
                    if (isScrolling && (currentTime - startTime > 200)) {
                        movePositionX(position, tabWidth * positionOffset);
                        startTime = currentTime;
                    }
                    if (isBackScrolling) {
                        movePositionX(position);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    switch (state) {
                        case 1:
                            isScrolling = true;
                            isBackScrolling = false;
                            break;
                        case 2:
                            isScrolling = false;
                            isBackScrolling = true;
                            break;
                        default:
                            isScrolling = false;
                            isBackScrolling = false;
                            break;
                    }

                }
            });
        }
    }


    private void movePositionX(final int position, float positionOffsetPixels) {
        float translationX = mIvTabBg.getTranslationX();
        float toX = tabWidth * position + positionOffsetPixels;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mIvTabBg, "translationX", translationX, toX);
        objectAnimator.setDuration(400);
        objectAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setSelect(viewList.get(position));
                if (mListener != null) {
                    mListener.tabPosition(position);
                }
            }

        });
        objectAnimator.start();
    }

    private void movePositionX(int toPosition) {
        movePositionX(toPosition, 0);
    }


    public void setTabEventListener(TabEventListener listener) {
        mListener = listener;
    }

    public interface TabEventListener {
        public void tabPosition(int position);
    }
}
