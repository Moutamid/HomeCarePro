package com.moutamid.homecarepro.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.moutamid.homecarepro.R;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskVH> {

    Context context;
    ArrayList<TaskModel> list, allList;

    public TaskAdapter(Context context, ArrayList<TaskModel> list, ArrayList<TaskModel> allList) {
        this.context = context;
        this.list = list;
        this.allList = allList;
    }

    @NonNull
    @Override
    public TaskVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_layout, parent, false);
        return new TaskVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskVH holder, int position) {
        TaskModel model = list.get(holder.getAbsoluteAdapterPosition());

        holder.name.setText("Name : " + model.getName());
        holder.frequn.setText("Frequency : "+model.getFrequency());
        holder.desc.setText("Description: " + model.getDescription());
        holder.priority.setText(model.getPriority());

        holder.status.setChecked(model.isStatus());

        switch (model.getPriority()) {
            case Constants.HIGH:
                holder.priorityCard.setCardBackgroundColor(context.getResources().getColor(R.color.high_prio));
                break;
            case Constants.MEDIUM:
                holder.priorityCard.setCardBackgroundColor(context.getResources().getColor(R.color.medium_prio));
                break;
            case Constants.LOW:
                holder.priorityCard.setCardBackgroundColor(context.getResources().getColor(R.color.low_prio));
                break;
        }

        SimpleDateFormat format = new SimpleDateFormat(Constants.myFormat, Locale.getDefault());
        String date = format.format(model.getDate());
        holder.date.setText("Created At : "+date+"\nStarting From : " + model.getStartingDate());

        holder.status.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("Checking1", ""+isChecked);

            for (int i=0; i<allList.size(); i++) {
                if (list.get(holder.getAbsoluteAdapterPosition()).getName().equals(allList.get(i).getName())){
                    allList.get(i).setStatus(isChecked);
                   // notifyDataSetChanged();
                    Stash.put(Constants.SAVE_LIST, allList);
                }
            }
            switch (model.getFrequency()) {
                case Constants.WEEK:
                    ArrayList<TaskModel> week = Stash.getArrayList(Constants.WEEKLY_LIST, TaskModel.class);
                    for (int k=0; k<week.size(); k++) {
                        if (list.get(holder.getAbsoluteAdapterPosition()).getName().equals(week.get(k).getName())){
                            week.get(k).setStatus(isChecked);
                            // notifyDataSetChanged();
                            Stash.put(Constants.WEEKLY_LIST, week);
                        }
                    }
                    break;
                case Constants.MONTH:
                    ArrayList<TaskModel> month = Stash.getArrayList(Constants.MONTHLY_LIST, TaskModel.class);
                    for (int k=0; k<month.size(); k++) {
                        if (list.get(holder.getAbsoluteAdapterPosition()).getName().equals(month.get(k).getName())){
                            month.get(k).setStatus(isChecked);
                            // notifyDataSetChanged();
                            Stash.put(Constants.MONTHLY_LIST, month);
                        }
                    }
                    break;
                case Constants.MONTH_3:
                    ArrayList<TaskModel> month3 = Stash.getArrayList(Constants.MONTH3_LIST, TaskModel.class);
                    for (int k=0; k<month3.size(); k++) {
                        if (list.get(holder.getAbsoluteAdapterPosition()).getName().equals(month3.get(k).getName())){
                            month3.get(k).setStatus(isChecked);
                            // notifyDataSetChanged();
                            Stash.put(Constants.MONTH3_LIST, month3);
                        }
                    }
                    break;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TaskVH extends RecyclerView.ViewHolder{
        TextView name, desc, frequn, priority, date;
        MaterialCardView priorityCard, mainCard;
        SwitchMaterial status;
        public TaskVH(@NonNull View itemView) {
            super(itemView);
            mainCard = itemView.findViewById(R.id.mainCard);
            name = itemView.findViewById(R.id.taskName);
            desc = itemView.findViewById(R.id.taskDesc);
            frequn = itemView.findViewById(R.id.frequncy);
            priority = itemView.findViewById(R.id.priority);
            status = itemView.findViewById(R.id.status);
            priorityCard = itemView.findViewById(R.id.priorityCard);
            date = itemView.findViewById(R.id.time);
        }
    }
}
