package xyz.gautamhans.locus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gautam on 09-Apr-17.
 */

public class RVAdapter_CategoryDetails extends
        RecyclerView.Adapter<RVAdapter_CategoryDetails.ViewHolder> {

    private List<Example> exampleList;
    private Context context;
    private List<Result> resultList;
    private String photoBaseUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private String API_KEY = "&key=AIzaSyBE8jPCH28fGzNwldLfR2h5WTgMC_IvuJI";

    RVAdapter_CategoryDetails(List<Result> exampleList){
        this.resultList = exampleList;
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_place_photo;
        TextView tv_place_name, tv_place_address;
        RatingBar rb_place_rating;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_place_photo = (ImageView) itemView.findViewById(R.id.place_photo);
            tv_place_name = (TextView) itemView.findViewById(R.id.place_name);
            tv_place_address = (TextView) itemView.findViewById(R.id.place_address);
            rb_place_rating = (RatingBar) itemView.findViewById(R.id.place_rating);
        }
    }
}
