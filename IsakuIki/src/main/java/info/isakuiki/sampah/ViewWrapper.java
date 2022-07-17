package info.isakuiki.sampah;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder{
    public ViewWrapper(V itemView) {
        super(itemView);
    }
}