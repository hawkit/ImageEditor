package me.ayaseruri.imageeditor.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by wufeiyang on 16/5/26.
 */
public class MainActivityModel implements IMainActivityModel{

    private static final int RECENT_PIC_COUNT = 20;
    private static final String[] sProjection = new String[]{
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
    };

    private Context mContext;

    public MainActivityModel(Context context) {
        mContext = context;
    }

    @Override
    public ArrayList<String> getRecentPics() {
        ArrayList<String> arrayList = new ArrayList<>();

        final Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , sProjection, null
                , null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        if(null == cursor){
            return arrayList;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        for(int i=0; i < Math.min(RECENT_PIC_COUNT, cursor.getCount()); i++){
            arrayList.add(cursor.getString(columnIndex));
            cursor.moveToNext();
        }
        return arrayList;
    }
}
