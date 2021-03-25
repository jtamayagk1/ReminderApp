package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DateSorter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter {
    private ArrayList<Reminder> reminderData;
    private Context parentContext;
    private View.OnClickListener mOnItemClickListener;

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTask;
        public TextView tvPriority;
        public TextView tvTime;
        public TextView tvDate;
        public Button btnDelete;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTask = itemView.findViewById(R.id.tvTask);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            tvDate = itemView.findViewById(R.id.tvDateItem);
            tvTime = itemView.findViewById(R.id.tvTimeItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteReminder);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);

        }

        public TextView getReminderTask() {
            return tvTask;
        }

        public TextView getReminderPriority() {
            return tvPriority;
        }

        public TextView getReminderDate() {
            return tvDate;
        }

        public TextView getReminderTime() {
            return tvTime;
        }

        public Button getDeleteButton() {
            return btnDelete;
        }
    }
    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    public Adapter (ArrayList<Reminder> arrayList, Context context){
        reminderData = arrayList;
        parentContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ReminderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReminderViewHolder rvh = (ReminderViewHolder)holder;
        rvh.getReminderTask().setText(reminderData.get(position).getTask());
        if(reminderData.get(position).getPriority() == 1){
            rvh.getReminderPriority().setText("Priority: High");
        }else if (reminderData.get(position).getPriority() == 2){
            rvh.getReminderPriority().setText("Priority: Medium");
        }else {
            rvh.getReminderPriority().setText("Priority: Low");
        }
        rvh.getReminderDate().setText(DateFormat.format("MM/dd/yyyy", reminderData.get(position).getDate()));
        rvh.getReminderTime().setText(reminderData.get(position).getTime());
        rvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderData.size();
    }

    private void deleteItem(int position){
        Reminder reminder = reminderData.get(position);
        DataSource ds = new DataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteReminder(reminder.getReminderID());
            ds.close();
            if(didDelete){
                reminderData.remove(position);
                notifyDataSetChanged();
                Toast.makeText(parentContext, "Reminder Deleted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_LONG).show();
        }
    }
}




