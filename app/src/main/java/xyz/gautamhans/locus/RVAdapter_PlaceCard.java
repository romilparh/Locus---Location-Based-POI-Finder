package xyz.gautamhans.locus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gautam on 02-Apr-17.
 */

public class RVAdapter_PlaceCard extends
        RecyclerView.Adapter<RVAdapter_PlaceCard.PlaceCardViewHolder> {

    List<PlaceCardDetails> placeCardDetails;

    RVAdapter_PlaceCard(List<PlaceCardDetails> placeCardDetails) {
        this.placeCardDetails = placeCardDetails;
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
        holder.iv_place_photo.setImageResource(placeCardDetails.get(position).place_photoId);
        holder.tv_place_name.setText(placeCardDetails.get(position).place_name);
        holder.tv_place_address.setText(placeCardDetails.get(position).place_address);
        holder.rb_place_rating.setRating((float) placeCardDetails.get(position).place_rating);
    }

    @Override
    public int getItemCount() {
        return placeCardDetails.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PlaceCardViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_place_photo;
            TextView tv_place_name, tv_place_address;
            RatingBar rb_place_rating;

            public PlaceCardViewHolder(View itemView) {
                super(itemView);
                iv_place_photo = (ImageView) itemView.findViewById(R.id.place_photo);
                tv_place_name = (TextView) itemView.findViewById(R.id.place_name);
                tv_place_address = (TextView) itemView.findViewById(R.id.place_address);
                rb_place_rating = (RatingBar) itemView.findViewById(R.id.place_rating);
            }
        }
    }

