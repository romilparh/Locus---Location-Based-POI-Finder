package xyz.gautamhans.locus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.retrofit.pojosplaces.Photo;
import xyz.gautamhans.locus.retrofit.pojosplaces.Result;
import xyz.gautamhans.locus.ui.MainActivity;

/**
 * Created by Gautam on 02-Apr-17.
 */

public class RVAdapter_PlaceCard extends
        RecyclerView.Adapter<RVAdapter_PlaceCard.PlaceCardViewHolder> {

    List<Result> resultList;
    private String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private String API_KEY = "&key=AIzaSyBE8jPCH28fGzNwldLfR2h5WTgMC_IvuJI";
    private String finalImageUrl;
    private Context context;
    private String photoReference;

    private ListItemClickListener mListItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String place_id, String photoReference);
    }

    public RVAdapter_PlaceCard(List<Result> resultList, Context context, ListItemClickListener mListItemClickListener) {
        this.resultList = resultList;
        this.context = context;
        this.mListItemClickListener = mListItemClickListener;
    }

    @Override
    public RVAdapter_PlaceCard.PlaceCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_places_cards, parent, false);
        PlaceCardViewHolder pcvh = new PlaceCardViewHolder(v);
        return pcvh;
    }

    @Override
    public void onBindViewHolder(RVAdapter_PlaceCard.PlaceCardViewHolder holder, int position) {
        holder.tv_place_name.setText(resultList.get(position).getName());

        if (resultList.get(position).getRating() != null) {
            float rating = Float.parseFloat(String.valueOf(resultList.get(position).getRating()));
            holder.rb_place_rating.setRating(rating);
        } else {
            holder.rb_place_rating.setVisibility(View.GONE);
        }

        holder.tv_place_address.setText(resultList.get(position).getFormattedAddress());
        List<Photo> photoList= resultList.get(position).getPhotos();
        try {

            Log.d("list size", ": " + photoList.size());
            Log.d("photoRef ", ": " + photoList.get(0).getPhotoReference());
            if(photoList.get(0).getPhotoReference()!=null){
                finalImageUrl = photoBaseUrl + photoList.get(0).getPhotoReference() +API_KEY;
                Picasso.with(context).load(finalImageUrl).fit().into(holder.iv_place_photo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PlaceCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_place_photo;
        TextView tv_place_name, tv_place_address;
        RatingBar rb_place_rating;

        public PlaceCardViewHolder(View itemView) {
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

