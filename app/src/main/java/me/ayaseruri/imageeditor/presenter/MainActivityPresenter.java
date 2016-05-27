package me.ayaseruri.imageeditor.presenter;

import me.ayaseruri.imageeditor.model.IMainActivityModel;
import me.ayaseruri.imageeditor.model.MainActivityModel;
import me.ayaseruri.imageeditor.view.IMainActivity;

/**
 * Created by wufeiyang on 16/5/26.
 */
public class MainActivityPresenter {
    private IMainActivityModel mMainActivityModel;
    private IMainActivity mMainActivity;

    public MainActivityPresenter(IMainActivity mainActivity) {
        mMainActivity = mainActivity;
        mMainActivityModel = new MainActivityModel(mMainActivity.getContext());
    }

    public void refreshRecentPics(){
        mMainActivity.onGetRecentPics(mMainActivityModel.getRecentPics());
    }
}
