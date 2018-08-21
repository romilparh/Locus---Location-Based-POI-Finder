package xyz.gautamhans.locus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.retrofit.pojos.Example;
import xyz.gautamhans.locus.retrofit.pojos.Result;

/**
 * Created by Gautam on 09-Apr-17.
 */

public class RVAdapter_CategoryDetails extends
        RecyclerView.Adapter<RVAdapter_CategoryDetails.ViewHolder> {

    final private RVAdapter_CategoryDetails.ListItemClickListener mListItemClickListener;

    //Interface for handling Item Clicks
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String place_id, String photoReference);
    }

    private Context context;
    private List<Result> resultList;


    // Constructor
    public RVAdapter_CategoryDetails(ListItemClickListener listener, List<Result> exampleList) {
        this.resultList = exampleList;
        mListItemClickListener = listener;
    }


    @Override
    public RVAdapter_CategoryDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.category_cards_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVAdapter_CategoryDetails.ViewHolder holder, int position) {
        holder.tv_place_name.setText(resultList.get(position).getName());
//        holder.rb_place_rating.setRating(resultList.get(position).getRating());
        holder.tv_place_address.setText(resultList.get(position).getVicinity());
//        String imageUrl = photoBaseUrl + resultList.get(position).getReference() + API_KEY;
//        Picasso.with(context).load(imageUrl).into(holder.iv_place_photo);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_place_photo;
        TextView tv_place_name, tv_place_address;
        RatingBar rb_place_rating;
        String photoReference;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_place_photo = (ImageView) itemView.findViewById(R.id.place_photo);
            tv_place_name = (TextView) itemView.findViewById(R.id.place_name);
            tv_place_address = (TextView) itemView.findViewById(R.id.place_address);
            rb_place_rating = (RatingBar) itemView.findViewById(R.id.place_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String place_id = resultList.get(clickedPosition).getPlaceId();
            // Log.d(String.valueOf(this), String.valueOf(resultList.get(clickedPosition).getPhotos().size()));

            if (resultList.get(clickedPosition).getPhotos() != null)
                try{
                    photoReference = resultList.get(clickedPosition).getPhotos().get(clickedPosition).getPhotoReference();
                }catch (Exception e){
                    e.printStackTrace();
                }
                mListItemClickListener.onListItemClick(clickedPosition, place_id, photoReference);
        }
    }
}
