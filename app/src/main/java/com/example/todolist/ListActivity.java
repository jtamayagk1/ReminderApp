package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<Reminder> reminders;
    Adapter adapter;
    RecyclerView rvReminderList;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int reminderID = reminders.get(position).getReminderID();
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            intent.putExtra("reminderID", reminderID);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initListButton();
        initSettingsButton();
        initAddButton();

        String sortBy = getSharedPreferences("ToDoListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "date");
        String sortOrder = getSharedPreferences("ToDoListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        DataSource ds = new DataSource(this);
        try{
            ds.open();
            reminders = ds.getReminders(sortBy, sortOrder);
            ds.close();
            rvReminderList = findViewById(R.id.rvReminderList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rvReminderList.setLayoutManager(layoutManager);
            adapter = new Adapter(reminders, ListActivity.this);
            adapter.setOnItemClickListener(onItemClickListener);
            rvReminderList.setAdapter(adapter);
        }catch (Exception ex){
            Toast.makeText(this, "Error retrieving reminders", Toast.LENGTH_LONG).show();

        }
    }

    public void initSettingsButton(){
        ImageButton ibtnSettings = findViewById(R.id.ibtnSettings);
        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void initListButton(){
        ImageButton ibtnList = findViewById(R.id.ibtnList);
        ibtnList.setEnabled(false);
    }

    public void initAddButton(){
        ImageButton ibtnAdd = findViewById(R.id.ibtnAdd);
        ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}