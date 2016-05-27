package me.ayaseruri.imageeditor.utils;

import android.content.Context;
import android.content.Intent;

import me.ayaseruri.imageeditor.global.IntentConstant;
import me.ayaseruri.imageeditor.view.ImageEditorActivity_;

/**
 * Created by wufeiyang on 16/5/27.
 */
public class StartUtils implements IntentConstant{

    public static void startEditor(Context context, String path){
        Intent intent = new Intent(context, ImageEditorActivity_.class);
        intent.putExtra(IMAGE_PATH, path);
        context.startActivity(intent);
    }
}
