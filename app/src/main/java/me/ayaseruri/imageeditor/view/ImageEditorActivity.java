package me.ayaseruri.imageeditor.view;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.io.File;
import java.util.Arrays;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
import me.ayaseruri.imageeditor.R;
import me.ayaseruri.imageeditor.global.IntentConstant;
import me.ayaseruri.imageeditor.presenter.BaseRecyclerAdapter;
import me.ayaseruri.imageeditor.utils.GridSpacingItemDecoration;
import me.ayaseruri.imageeditor.utils.LocalDisplay;

@Fullscreen
@EActivity(R.layout.activity_image_editor)
public class ImageEditorActivity extends AppCompatActivity implements IntentConstant, SeekBarPopup.IOnSeekPopupAction {

    private static final String TAB_TAG = "标签";
    private static final String TAB_Filter = "滤镜";
    private static final String TAB_Edit = "编辑";

    private static final float DEFAULT_BRIGHTNESS = 0.0f; // -1 : 1
    private static final float DEFAULT_WHITE_BALANCE = 5000.0f;
    private static final float DEFAULT_SATURATION = 1.0f; //0.0 : 2.0
    private static final float DEFAULT_CONTRAST = 1.0f; // 0.0 : 4.0

    @ViewById(R.id.gpu_image)
    GPUImageView mGPUImage;
    @ViewById(R.id.tab)
    TabLayout mTabLayout;
    @ViewById(R.id.recycler)
    RecyclerView mRecyclerView;
    @ViewById(R.id.edit_title)
    TextView mEditTitle;
    @ViewById(R.id.tag_hint)
    TextView mTagHint;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.seek_bar_pop)
    SeekBarPopup mSeekBarPopup;
    @ViewById(R.id.image_tag)
    ImageTagView mImageTag;

    @StringArrayRes(R.array.image_edit_item_title)
    String[] mImageEditItemTitle;

    private float mBrightness = DEFAULT_BRIGHTNESS, mWhiteBalance = DEFAULT_WHITE_BALANCE
            , mSaturation = DEFAULT_SATURATION, mContrast = DEFAULT_CONTRAST;

    private String mPath;
    private BaseRecyclerAdapter mEditAdapter;
    private BaseRecyclerAdapter mFilterAdapter;

    private GPUImageBrightnessFilter mBrightnessFilter;
    private GPUImageWhiteBalanceFilter mWhiteBalanceFilter;
    private GPUImageSaturationFilter mSaturationFilter;
    private GPUImageContrastFilter mContrastFilter;
    private GPUImageFilterGroup mFilterGroup;
    private boolean isAddTag = true;

    @AfterViews
    void init(){
        initToolbar();

        mPath = getIntent().getStringExtra(IMAGE_PATH);
        if(TextUtils.isEmpty(mPath)){
            finish();
        }

        File imageFile = new File(mPath);
        if(imageFile.exists()){
            mGPUImage.setScaleType(GPUImage.ScaleType.CENTER_CROP);
            mGPUImage.setImage(imageFile);
        }else {
            Toast.makeText(this, "图片不存在", Toast.LENGTH_LONG).show();
        }

        mSeekBarPopup.setIOnSeekPopupAction(ImageEditorActivity.this);

        initImageFilter();
        initEditBar();
        initTab();
    }



    private void initToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void initImageFilter(){
        mBrightnessFilter = new GPUImageBrightnessFilter();
        mWhiteBalanceFilter = new GPUImageWhiteBalanceFilter();
        mSaturationFilter = new GPUImageSaturationFilter();
        mContrastFilter = new GPUImageContrastFilter();
        mFilterGroup = new GPUImageFilterGroup();

        mFilterGroup.addFilter(mBrightnessFilter);
        mFilterGroup.addFilter(mWhiteBalanceFilter);
        mFilterGroup.addFilter(mSaturationFilter);
        mFilterGroup.addFilter(mContrastFilter);
    }

    private void initEditBar(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(mImageEditItemTitle.length, LocalDisplay.dp2px(4), false));
        mEditAdapter = new BaseRecyclerAdapter<String, ImageEditItem>() {
            @Override
            protected ImageEditItem onCreateItemView(ViewGroup parent, int viewType) {
                return ImageEditItem_.build(parent.getContext());
            }

            @Override
            protected void onBindView(final ImageEditItem imageEditItem, String s, final int postion) {
                imageEditItem.bind(s);
                imageEditItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float value = 0f;
                        switch (postion){
                            case SeekBarPopup.TYPE_BRIGHTESS:
                                value = 50 + mBrightness * 50;
                                break;
                            case SeekBarPopup.TYPE_CONTRAST:
                                value = mContrast * 50;
                                break;
                            case SeekBarPopup.TYPE_SATURATION:
                                value = mSaturation * 50;
                                break;
                            case SeekBarPopup.TYPE_TMP:
                                value = mWhiteBalance / 100;
                                break;
                            default:
                                break;
                        }
                        mSeekBarPopup.init(value);
                        mSeekBarPopup.setType(postion);
                    }
                });
            }
        };
    }

    private void initTab(){
        mTabLayout.addTab(mTabLayout.newTab().setText(TAB_TAG), true);
        mTabLayout.addTab(mTabLayout.newTab().setText(TAB_Filter));
        mTabLayout.addTab(mTabLayout.newTab().setText(TAB_Edit));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mEditTitle.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mTagHint.setVisibility(View.GONE);

                String title = tab.getText() == null ? "" : tab.getText().toString();

                switch (title){
                    case TAB_TAG:
                        mTagHint.setVisibility(View.VISIBLE);
                        isAddTag = true;
                        break;
                    case TAB_Filter:
                        mEditTitle.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        isAddTag = false;
                        break;
                    case TAB_Edit:
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mRecyclerView.setAdapter(mEditAdapter);
                        mEditAdapter.refresh(Arrays.asList(mImageEditItemTitle));
                        isAddTag = false;
                        break;
                    default:
                        break;
                }

                mImageTag.setEnbale(isAddTag);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onSeekBar(int type, int value) {
        switch (type){
            case SeekBarPopup.TYPE_BRIGHTESS:
                mBrightness = (value - 50) / 100.0f;
                mBrightnessFilter.setBrightness(mBrightness);
                break;
            case SeekBarPopup.TYPE_CONTRAST:
                mContrast = value/50.0f;
                mContrastFilter.setContrast(mContrast);
                break;
            case SeekBarPopup.TYPE_SATURATION:
                mSaturation = value/50.0f;
                mSaturationFilter.setSaturation(mSaturation);
                break;
            case SeekBarPopup.TYPE_TMP:
                mWhiteBalance = value * 100.0f;
                mWhiteBalanceFilter.setTemperature(mWhiteBalance);
                break;
            default:
                break;
        }

        mGPUImage.setFilter(mFilterGroup);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onOk() {

    }
}
