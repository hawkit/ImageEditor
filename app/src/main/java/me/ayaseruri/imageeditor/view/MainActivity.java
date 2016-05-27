package me.ayaseruri.imageeditor.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.ayaseruri.imageeditor.R;
import me.ayaseruri.imageeditor.presenter.MainActivityPresenter;
import me.ayaseruri.imageeditor.presenter.MainImageListAdapter;
import me.ayaseruri.imageeditor.utils.GridSpacingItemDecoration;
import me.ayaseruri.imageeditor.utils.LocalDisplay;
import me.ayaseruri.imageeditor.utils.StartUtils;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements IMainActivity{

    private static final int EXTERNAL_STORAGE_REQ_CODE = 0;

    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;
    @ViewById(R.id.head_img)
    MainHead mMainHead;

    private MainActivityPresenter mPresenter;
    private MainImageListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalDisplay.init(this);
    }

    @AfterViews
    void init(){
        mPresenter = new MainActivityPresenter(this);

        mAdapter = new MainImageListAdapter(new MainImageListAdapter.IOnItemClick() {
            @Override
            public void onItemClick(String path) {
                mMainHead.bind(path);
            }
        });
        mAdapter.setHead(MainImageListHead_.build(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, LocalDisplay.dp2px(4), true));

        requestPermission();
    }

    @Override
    public void onGetRecentPics(List<String> paths) {
        mAdapter.refresh(paths);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case EXTERNAL_STORAGE_REQ_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mPresenter.refreshRecentPics();
                }else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Click(R.id.toolbar_next)
    void onToolbarNext(){
        StartUtils.startEditor(this, mMainHead.getPath());
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"需要此权限读取您的照片信息",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQ_CODE);
            }
        }else {
            mPresenter.refreshRecentPics();
        }
    }
}
