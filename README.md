# TabSwitchView
用简单的方法实现的Tab配合ViewPager滑动，各个区域都是已图片的方式展示
##DEMO
![](https://raw.githubusercontent.com/youmu178/TabSwitchView/master/action.gif)
```xml
<com.youzh.tabswitchviewlibrary.TabSwitchView
        android:id="@+id/switchTab"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:itemText="每日赚取,持有投资,到期退出"
        app:itemTextColorNormal="@color/bg_green"
        app:itemTextColorPressed="@android:color/white"/>      
```
###Attributes
|name|format|description|
|:---:|:---:|:---:|
| layoutBackground | reference | 外层背景
| layoutPaddingLeft | dimension | 外层左边内距
| layoutPaddingTop | dimension | 外层顶部内距
| layoutPaddingRight | dimension | 外层右边内距
| layoutPaddingBottom | dimension | 外层右边内距
| tabBackground | reference | 整个Tab的背景
| itemTextColorNormal | reference | Tab的正常字体
| itemTextColorPressed | reference | Tab的选中字体
| itemText | string | Tab的描述，以英文逗号分隔
| itemNum | integer | Tab的数量
| itemSelBg | reference | Tab滑动的图片
####欢迎关注
![](https://github.com/youmu178/Pic/blob/master/itbox_qr.jpg)
