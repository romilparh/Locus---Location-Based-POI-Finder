package xyz.gautamhans.locus.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.gautamhans.locus.R;
import xyz.gautamhans.locus.db.DBHelper;
import xyz.gautamhans.locus.db.DatabaseModel;

/**
 * Created by Gautam on 22-Apr-17.
 */


public class RVAdapter_Reminders extends RecyclerView.Adapter<RVAdapter_Reminders.ViewHolder> {

    List<DatabaseModel> dbList;
    Context context;
    DBHelper dbHelper;
    private int position;

    private RVAdapter_Reminders.ReminderListener mReminderListener;

    public interface ReminderListener{
        void onOptionsClick(View v, int reminderIndex);
        void onCardClick(View v, int position);
    }

    public RVAdapter_Reminders(Context context, List<DatabaseModel> dbList, RVAdapter_Reminders.ReminderListener mReminderListener) {
        this.context = context;
        this.dbList = dbList;
        this.mReminderListener = mReminderListener;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public RVAdapter_Reminders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_reminder_template, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RVAdapter_Reminders.ViewHolder holder, final int position) {
        holder.title.setText(dbList.get(position).getTitle());
        holder.description.setText(dbList.get(position).getDescription());
        holder.address.setText(dbList.get(position).getAddress());
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView title, description, address, buttonViewOptions;
        public CardView mCard;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.task_title);
            description = (TextView) itemView.findViewById(R.id.desc_title_text_tv);
            address = (TextView) itemView.findViewById(R.id.address_title_text_tv);
            buttonViewOptions = (TextView) itemView.findViewById(R.id.buttonViewOptions);
            mCard = (CardView) itemView.findViewById(R.id.cv_reminder);

            this.buttonViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RVAdapter_Reminders.this.mReminderListener.onOptionsClick(buttonViewOptions, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RVAdapter_Reminders.this.mReminderListener.onCardClick(mCard, getAdapterPosition());
                }
            });

        }

    }
}