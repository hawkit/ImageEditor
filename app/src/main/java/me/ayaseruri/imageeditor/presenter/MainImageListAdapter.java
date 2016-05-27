package me.ayaseruri.imageeditor.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.ayaseruri.imageeditor.view.MainImageListItem;
import me.ayaseruri.imageeditor.view.MainImageListItem_;

/**
 * Created by wufeiyang on 16/5/26.
 */
public class MainImageListAdapter extends RecyclerView.Adapter<MainImageListAdapter.MainImageViewHolder> {

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_NORMAL = 1;

    private MainImageListItem mLastChecked;
    private List<String> mPaths;
    private View mHead;
    private IOnItemClick mIOnItemClick;

    public MainImageListAdapter(IOnItemClick iOnItemClick) {
        mPaths = new ArrayList<>();
        mIOnItemClick = iOnItemClick;
    }

    @Override
    public MainImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = MainImageListItem_.build(parent.getContext());;
        switch (viewType){
            case TYPE_HEAD:
                view = mHead;
                break;
            case TYPE_NORMAL:
                break;
            default:
                break;
        }
        return new MainImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainImageViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            position--;
            final MainImageListItem item = (MainImageListItem) holder.itemView;

            item.bind(mPaths.get(position));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mLastChecked){
                        mLastChecked.setchecked(false);
                    }
                    item.setchecked(true);
                    mLastChecked = item;

                    if(null != mIOnItemClick){
                        mIOnItemClick.onItemClick(item.getPath());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == mHead ? mPaths.size() : mPaths.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(null != mHead && position == 0){
            return TYPE_HEAD;
        }
        return TYPE_NORMAL;
    }

    public void setHead(View head) {
        mHead = head;
        notifyDataSetChanged();
    }

    public void refresh(List<String> paths){
        mPaths.clear();
        mPaths.addAll(paths);
        notifyDataSetChanged();
    }

    static class MainImageViewHolder extends RecyclerView.ViewHolder{
        public MainImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface IOnItemClick{
        void onItemClick(String path);
    }
}
