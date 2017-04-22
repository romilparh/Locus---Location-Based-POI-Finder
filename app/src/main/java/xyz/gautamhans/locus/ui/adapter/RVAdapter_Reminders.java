package xyz.gautamhans.locus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.db.DatabaseModel;

/**
 * Created by Gautam on 22-Apr-17.
 */


public class RVAdapter_Reminders extends RecyclerView.Adapter<RVAdapter_Reminders.ViewHolder> {

    static List<DatabaseModel> dbList;
    static Context context;

    public RVAdapter_Reminders(Context context, List<DatabaseModel> dbList){
        this.dbList = new ArrayList<DatabaseModel>();
        this.context = context;
        this.dbList = dbList;
    }

    @Override
    public RVAdapter_Reminders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_reminder_template, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RVAdapter_Reminders.ViewHolder holder, int position) {
        holder.title.setText(dbList.get(position).getTitle());
        holder.description.setText(dbList.get(position).getDescription());
        holder.address.setText(dbList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description, address;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.task_title);
            description = (TextView) itemView.findViewById(R.id.desc_title_text_tv);
            address = (TextView) itemView.findViewById(R.id.address_title_text_tv);
        }
    }
}