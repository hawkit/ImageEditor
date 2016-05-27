package me.ayaseruri.imageeditor.view;

import android.content.Context;

import java.util.List;

/**
 * Created by wufeiyang on 16/5/26.
 */
public interface IMainActivity {
    void onGetRecentPics(List<String> paths);
    Context getContext();
}
