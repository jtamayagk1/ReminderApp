package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initListButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();
    }

    public void initSettingsButton(){
        ImageButton ibtnSettings = findViewById(R.id.ibtnSettings);
        ibtnSettings.setEnabled(false);
    }

    public void initListButton(){
        ImageButton ibtnList = findViewById(R.id.ibtnList);
        ibtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void initSettings() {
        String sortBy = getSharedPreferences("ToDoListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "date");
        String sortOrder = getSharedPreferences("ToDoListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");
        RadioButton rbTask = findViewById(R.id.rbName);
        RadioButton rbPriority = findViewById(R.id.rbPriority);
        RadioButton rbDate = findViewById(R.id.rbDate);

        if (sortBy.equalsIgnoreCase("task")) {
            rbTask.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        } else {
            rbDate.setChecked(true);
        }

        RadioButton rbASC = findViewById(R.id.rbASC);
        RadioButton rbDEC = findViewById(R.id.rbDEC);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbASC.setChecked(true);
        } else {
            rbDEC.setChecked(true);
        }
    }

    public void initSortByClick(){
        RadioGroup rgSortBy = findViewById(R.id.rgSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbTask = findViewById(R.id.rbName);
                RadioButton rbPriority = findViewById(R.id.rbPriority);

                if(rbTask.isChecked()){
                    getSharedPreferences("ToDoListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield","task").apply();
                }else if(rbPriority.isChecked()){
                    getSharedPreferences("ToDoListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield","priority").apply();
                }else{
                    getSharedPreferences("ToDoListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield","date").apply();
                }
            }
        });
    }

    public void initSortOrderClick(){
        RadioGroup rgSortOrder = findViewById(R.id.rgSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbASC = findViewById(R.id.rbASC);
                if(rbASC.isChecked()){
                    getSharedPreferences("ToDoListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortorder","ASC").apply();
                }else{
                    getSharedPreferences("ToDoListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortorder","DESC").apply();
                }
            }
        });
    }
}