package xyz.gautamhans.locus;

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

public class RVCat_Adapter extends RecyclerView.Adapter<RVCat_Adapter.CategoryDetailsViewHolder>{

    List<CategoryDetails> categoryDetailsList;

    RVCat_Adapter(List<CategoryDetails> categoryDetailsList){
        this.categoryDetailsList = categoryDetailsList;
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

    public static class CategoryDetailsViewHolder extends RecyclerView.ViewHolder{
        ImageView catPhoto;

        public CategoryDetailsViewHolder(View itemView) {
            super(itemView);
            catPhoto = (ImageView)itemView.findViewById(R.id.cat_logo);
        }
    }
}
