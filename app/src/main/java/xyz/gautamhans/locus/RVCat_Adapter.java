package xyz.gautamhans.locus;

import android.app.LauncherActivity;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Gautam on 02-Apr-17.
 */

public class RVCat_Adapter extends RecyclerView.Adapter<RVCat_Adapter.CategoryDetailsViewHolder> {

    final private ListItemClickListener mOnClickListener;

    //Interface to handle click events
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String type);
    }

    List<CategoryDetails> categoryDetailsList;

    //constructor to assign data at runtime
    RVCat_Adapter(List<CategoryDetails> categoryDetailsList,
                  ListItemClickListener listener) {
        this.categoryDetailsList = categoryDetailsList;
        mOnClickListener = listener;
    }

    @Override
    public RVCat_Adapter.CategoryDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_cat, parent, false);
        CategoryDetailsViewHolder cdvh = new CategoryDetailsViewHolder(v);
        return cdvh;
    }

    @Override
    public void onBindViewHolder(RVCat_Adapter.CategoryDetailsViewHolder holder, int position) {
        holder.catPhoto.setImageResource(categoryDetailsList.get(position).photoId);
    }

    @Override
    public int getItemCount() {
        return categoryDetailsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class CategoryDetailsViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        ImageView catPhoto;

        public CategoryDetailsViewHolder(View itemView) {
            super(itemView);
            catPhoto = (ImageView) itemView.findViewById(R.id.cat_logo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String type = String.valueOf(categoryDetailsList.get(clickedPosition).photoId);
            mOnClickListener.onListItemClick(clickedPosition, type);
        }
    }

}
