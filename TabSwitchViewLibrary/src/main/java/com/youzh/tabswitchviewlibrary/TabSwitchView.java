package com.youzh.tabswitchviewlibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Tab 选择
 *
 * @author youzehong
 */
public class TabSwitchView extends LinearLayout {

    private static final int TEXT_WHITE = 0xFFFFFF;
    private static final int TEXT_GREEN = 0x82C23F;
    private LinearLayout mTabLayout;
    private FrameLayout mTabBg;
    private ImageView mTabIvBg;
    private LinearLayout mTabContent;

    private ArrayList<View> viewList = new ArrayList<View>();
    private ViewPager mViewPager;

    private TabEventListener mListener;
    private int itemTextColorNormal;
    private int itemTextColorPressed;

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
        mTabLayout = (LinearLayout) view.findViewById(R.id.ll_tab);
        mTabBg = (FrameLayout) view.findViewById(R.id.bg_tab);
        mTabIvBg = (ImageView) view.findViewById(R.id.bg_iv_tab);
        mTabContent = (LinearLayout) view.findViewById(R.id.ll_tab_content);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabSwitch);
        int layoutBackground = a.getResourceId(R.styleable.TabSwitch_layoutBackground, android.R.color.white);
        int layoutPaddingLeft = a.getDimensionPixelSize(R.styleable.TabSwitch_layoutPaddingLeft, 20);
        int layoutPaddingTop = a.getDimensionPixelSize(R.styleable.TabSwitch_layoutPaddingTop, 20);
        int layoutPaddingRight = a.getDimensionPixelSize(R.styleable.TabSwitch_layoutPaddingRight, 20);
        int layoutPaddingBottom = a.getDimensionPixelSize(R.styleable.TabSwitch_layoutPaddingBottom, 20);
        int tabBackground = a.getResourceId(R.styleable.TabSwitch_tabBackground, R.mipmap.bg_gray);
        int itemSelBg = a.getResourceId(R.styleable.TabSwitch_itemSelBg, R.mipmap.bg_green);
        int itemNum = a.getInteger(R.styleable.TabSwitch_itemNum, 3);
        String itemText = a.getString(R.styleable.TabSwitch_itemText);
        itemTextColorNormal = a.getColor(R.styleable.TabSwitch_itemTextColorNormal, TEXT_GREEN);
        itemTextColorPressed = a.getColor(R.styleable.TabSwitch_itemTextColorPressed, TEXT_WHITE);

        mTabLayout.setBackgroundResource(layoutBackground);
        mTabLayout.setPadding(layoutPaddingLeft, layoutPaddingTop, layoutPaddingRight, layoutPaddingBottom);
        mTabBg.setBackgroundResource(tabBackground);
        mTabIvBg.setBackgroundResource(itemSelBg);

        ViewGroup.LayoutParams lp = mTabIvBg.getLayoutParams();
        lp.width = (DimenUtil.getScreenWidth(context) - mTabLayout.getPaddingLeft() - mTabLayout.getPaddingRight()) / itemNum;
        mTabIvBg.setLayoutParams(lp);
        tabWidth = lp.width;

        String[] text = itemText.split(",");
        for (int i = 0; i < itemNum; i++) {
            TextView textView = new TextView(context);
            textView.setText(text[i]);
            textView.setTextColor(getColorSelector(itemTextColorNormal, itemTextColorPressed));
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(mOnClickListener);
            textView.setTag(i);
            viewList.add(textView);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            textView.setLayoutParams(layoutParams);
            mTabContent.addView(textView);
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
                    if (mListener != null) {
                        mListener.tabPosition(position);
                    }
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    TextView view = (TextView) viewList.get(position);
                    view.setTextColor(evaluate(positionOffset, itemTextColorPressed, itemTextColorNormal));

                    if (position < viewList.size()-1) {
                        TextView view2 = (TextView) viewList.get(position + 1);
                        view2.setTextColor(evaluate(1- positionOffset, itemTextColorPressed, itemTextColorNormal));
                    }
                    mTabIvBg.setTranslationX(tabWidth * position + tabWidth * positionOffset);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    }

    private ColorStateList getColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_selected},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        normalColor
                }
        );
    }

    public int evaluate(float fraction, int startValue, int endValue) {
        int startInt = (Integer) startValue;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((0xff) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }

    public void setTabEventListener(TabEventListener listener) {
        mListener = listener;
    }

    public interface TabEventListener {
        public void tabPosition(int position);
    }
}
