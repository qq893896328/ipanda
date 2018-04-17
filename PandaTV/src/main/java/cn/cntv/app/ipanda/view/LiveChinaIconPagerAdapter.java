package cn.cntv.app.ipanda.view;

public interface LiveChinaIconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}