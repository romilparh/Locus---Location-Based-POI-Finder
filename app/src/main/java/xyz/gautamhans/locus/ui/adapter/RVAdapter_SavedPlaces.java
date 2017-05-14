package xyz.gautamhans.locus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.retrofit.pojos.Place;
import xyz.gautamhans.locus.ui.SavedPlaceModel;
import xyz.gautamhans.locus.ui.SavedPlaces;

/**
 * Created by Gautam on 14-May-17.
 */

public class RVAdapter_SavedPlaces extends RecyclerView.Adapter<RVAdapter_SavedPlaces.SavedPlacesViewHolder>{

    private List<Place> savedPlaces;
    private Context context;

    public RVAdapter_SavedPlaces(Context context, List<Place> savedPlaces) {
        this.context = context;
        this.savedPlaces = savedPlaces;
    }

    @Override
    public RVAdapter_SavedPlaces.SavedPlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_savedplaces_template, parent, false);
        return new SavedPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVAdapter_SavedPlaces.SavedPlacesViewHolder holder, int position) {
        holder.placeName.setText(savedPlaces.get(position).getPlaceName());
        holder.placeAddress.setText(savedPlaces.get(position).getPlaceAddress());
        if(savedPlaces.get(position).getPhotoReference().equals("na")){
            holder.placePhoto.setImageResource(R.drawable.defaultplace);
        }else{
            Picasso.with(context)
                    .load(savedPlaces.get(position).getPhotoReference())
                    .resize(400,200)
                    .into(holder.placePhoto);
        }
    }

    @Override
    public int getItemCount() {
        return savedPlaces.size();
    }

    public class SavedPlacesViewHolder extends RecyclerView.ViewHolder{

        private TextView placeName, placeAddress;
        private ImageView placePhoto;

        public SavedPlacesViewHolder(View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.place_name_tv_i);
            placeAddress = (TextView) itemView.findViewById(R.id.place_address_tv_i);
            placePhoto = (ImageView) itemView.findViewById(R.id.placePhoto);
        }
    }
}
