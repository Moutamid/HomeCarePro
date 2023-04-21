package com.moutamid.homecarepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.moutamid.homecarepro.R;
import com.moutamid.homecarepro.models.TaskModel;
import com.moutamid.homecarepro.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.TaskVH> {

    Context context;
    ArrayList<TaskModel> list, allList;

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

        if (model.isStatus()){
            holder.status.setChecked(true);
        } else {
            holder.status.setChecked(false);
        }

        if (model.getPriority().equals(Constants.HIGH)){
            holder.priorityCard.setCardBackgroundColor(context.getResources().getColor(R.color.high_prio));
        } else if (model.getPriority().equals(Constants.MEDIUM)){
            holder.priorityCard.setCardBackgroundColor(context.getResources().getColor(R.color.medium_prio));
        } else if (model.getPriority().equals(Constants.LOW)) {
            holder.priorityCard.setCardBackgroundColor(context.getResources().getColor(R.color.low_prio));
        }

        SimpleDateFormat format = new SimpleDateFormat(Constants.myFormat);
        String date = format.format(model.getDate());
        holder.date.setText("Created At : "+date+"\nStarted From : " + model.getStartingDate());
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
