package me.ayaseruri.imageeditor.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import me.ayaseruri.imageeditor.R;

/**
 * Created by wufeiyang on 16/5/27.
 */
public class InfoPop {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private EditText mBandET, mPriceET, mLocationET;
    private String mBand, mPrice, mLocation;
    private Button mButton;
    private IOnInfoOk mIOnInfoOk;

    public InfoPop(Context context) {
        mContext = context;
        mPopupWindow = new PopupWindow(LayoutInflater.from(context).inflate(R.layout.info_pop, null)
                , WindowManager.LayoutParams.MATCH_PARENT
                , WindowManager.LayoutParams.WRAP_CONTENT, true);

        mBandET = (EditText) mPopupWindow.getContentView().findViewById(R.id.brand);
        mPriceET = (EditText) mPopupWindow.getContentView().findViewById(R.id.price);
        mLocationET = (EditText) mPopupWindow.getContentView().findViewById(R.id.location);

        mButton = (Button) mPopupWindow.getContentView().findViewById(R.id.ok);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = mBandET.getText().toString();
                if(TextUtils.isEmpty(brand)){
                    Toast.makeText(mContext, "品牌不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                String price = mPriceET.getText().toString();
                if(TextUtils.isEmpty(price)){
                    Toast.makeText(mContext, "价格不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                String location = mLocationET.getText().toString();
                if(TextUtils.isEmpty(location)){
                    Toast.makeText(mContext, "购买地不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                if(null != mIOnInfoOk){
                    mIOnInfoOk.onInfoComplete(brand, price, location);
                }
            }
        });
    }

    public void show(View parent){
        mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public void setIOnInfoOk(IOnInfoOk IOnInfoOk) {
        mIOnInfoOk = IOnInfoOk;
    }

    public interface IOnInfoOk{
        void onInfoComplete(String brand, String price, String location);
    }
}
