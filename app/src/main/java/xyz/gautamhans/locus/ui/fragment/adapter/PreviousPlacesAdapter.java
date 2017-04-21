package xyz.gautamhans.locus.ui.fragment.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.Revisitor;
import xyz.gautamhans.locus.entity.LocationPlace;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class PreviousPlacesAdapter extends ArrayAdapter<LocationPlace> {
    private List<LocationPlace> places;
    private Context mContext;

    public PreviousPlacesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.places = Revisitor.getInstance(context).getAllPlaces();
        this.mContext = context.getApplicationContext();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    final private Filter mFilter = new Filter() {

        @Override
        public String convertResultToString(Object resultValue) {
            return resultValue.toString();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<LocationPlace> suggestions = new ArrayList<LocationPlace>();
                for (LocationPlace customer : places) {
                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (customer.toString().toLowerCase().startsWith(
                            constraint.toString().toLowerCase())) {
                        suggestions.add(customer);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<LocationPlace>) results.values);
            }
            notifyDataSetChanged();
        }
    };


    public void refreshPlaces() {
        clear();
        this.places = Revisitor.getInstance(mContext).getAllPlaces();
    }
}
