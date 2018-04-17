/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package cn.cntv.app.ipanda.ui.view;

/**
 * Interface representing a View that will use to load data.
 */
public interface LoadDataView {
  /**
   * Show a view with a progress bar indicating a loading process.
   */
  void showLoading();

  /**
   * Hide a loading view.
   */
  void hideLoading();

}
