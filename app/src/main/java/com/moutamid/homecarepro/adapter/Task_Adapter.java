package com.moutamid.homecarepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import java.util.Locale;

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.TaskVH> {

    Context context;
    ArrayList<TaskModel> list, allList;

    public Task_Adapter(Context context, ArrayList<TaskModel> list, ArrayList<TaskModel> allList) {
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

        holder.status.setVisibility(View.GONE);
        holder.textView.setVisibility(View.GONE);

        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TaskVH extends RecyclerView.ViewHolder{
        TextView name, desc, frequn, priority, date, textView;
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
            textView = itemView.findViewById(R.id.textView);
        }
    }

}
