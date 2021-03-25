package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePicker.SaveDateListener {

    TextView currentDate;
    int newHour;
    int newMinute;
    Reminder currentReminder;
    EditText editTextTasks;
    EditText editTextDetails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDateAndTime();
        initChangeDate();
        initChangeTime();
        initListButton();
        initSettingsButton();
        initSaveButton();
        initTextChangedEvents();
        editTextFocus();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            initReminder(extras.getInt("reminderID"));
        }else {
            currentReminder = new Reminder();
            RadioButton rbLow = findViewById(R.id.rbLow);
            rbLow.setChecked(true);
        }


    }

    public void initSettingsButton(){
        ImageButton ibtnSettings = findViewById(R.id.ibtnSettings);
        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void initListButton(){
        ImageButton ibtnList = findViewById(R.id.ibtnList);
        ibtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void getDateAndTime(){
        String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        currentDate = findViewById(R.id.tvDate);
        currentDate.setText(date);
        TextView currentTime = findViewById(R.id.tvTime);
        currentTime.setText(new SimpleDateFormat("hh:mm a", Locale.US).format(new Date()));
    }
    public void initChangeDate(){
        Button btnChangeDate = findViewById(R.id.btnDate);
        btnChangeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DatePicker datePicker = new DatePicker();
                datePicker.show(fm, "DatePick");
            }
        });
    }
    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        currentDate = findViewById(R.id.tvDate);
        currentDate.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentReminder.setDate(selectedTime);
    }

    public void initChangeTime(){
        TextView tvTime = findViewById(R.id.tvTime);
        Button btnTimePicker = findViewById(R.id.btnTime);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMin = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String ampm;
                        newHour = hourOfDay;
                        newMinute = minute;
                        if (hourOfDay >= 12){
                            ampm = "PM";
                        }else{
                            ampm = "AM";
                        }
                        calendar.set(0,0,0,hourOfDay,minute);
                        tvTime.setText(DateFormat.format("hh:mm aa", calendar));
                        currentReminder.setTime(tvTime.getText().toString());
                    }
                }, currentHour,currentMin,false);

                timePicker.show();
            }
        });
    }

    private void initTextChangedEvents() {
        final EditText tfTask = findViewById(R.id.tfToDo);
        tfTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                currentReminder.setTask(tfTask.getText().toString());
            }
        });
        final EditText tfDetails = findViewById(R.id.tfDetails);
        tfDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                currentReminder.setDetails(tfDetails.getText().toString());
            }
        });
    }

    public void initSaveButton(){
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempTask = currentReminder.getTask();
                String capTask = tempTask.substring(0,1).toUpperCase() + tempTask.substring(1);
                currentReminder.setTask(capTask);
                RadioButton rbHigh = findViewById(R.id.rbHigh);
                RadioButton rbMed = findViewById(R.id.rbMedium);
                if(rbHigh.isChecked()){
                    currentReminder.setPriority(1);
                }else if(rbMed.isChecked()){
                    currentReminder.setPriority(2);
                }else{
                    currentReminder.setPriority(3);
                }

                if(currentReminder.getTime() == null){
                    currentReminder.setTime(new SimpleDateFormat("hh:mm a", Locale.US).format(new Date()));

                }
                boolean wasSuccessful;
                DataSource ds = new DataSource(MainActivity.this);
                try{
                    ds.open();
                    if(currentReminder.getReminderID() == -1){
                        wasSuccessful = ds.insertReminder(currentReminder);
                        if(wasSuccessful){
                            int newID = ds.getLastReminderID();
                            currentReminder.setReminderID(newID);
                        }
                    }else{
                        wasSuccessful = ds.updateReminder(currentReminder);
                    }
                    ds.close();

                }catch (Exception ex){
                    wasSuccessful = false;
                }
                if(wasSuccessful){
                    initReset();
                    Toast.makeText(getBaseContext(), "Success!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void editTextFocus(){
        editTextTasks = findViewById(R.id.tfToDo);
        editTextDetails = findViewById(R.id.tfDetails);

        editTextTasks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !editTextDetails.isFocused() ){
                    hideKeyboard(v);
                }
            }
        });
        editTextDetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !editTextTasks.isFocused()){
                    hideKeyboard(v);
                }
            }
        });

        RadioButton rbHigh = findViewById(R.id.rbHigh);
        rbHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        RadioButton rbMed = findViewById(R.id.rbMedium);
        rbMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        RadioButton rbLow = findViewById(R.id.rbLow);
        rbLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText task = findViewById(R.id.tfToDo);
        imm.hideSoftInputFromWindow(task.getWindowToken(), 0);
        EditText details = findViewById(R.id.tfDetails);
        imm.hideSoftInputFromWindow(details.getWindowToken(), 0);
    }

    private void initReminder(int id){
        DataSource ds = new DataSource(MainActivity.this);
        try{
            ds.open();
            currentReminder = ds.getSpecificReminder(id);
            ds.close();
        }catch (Exception ex){
            Toast.makeText(this, "Load Reminder Failed", Toast.LENGTH_LONG).show();
        }
        EditText task = findViewById(R.id.tfToDo);
        EditText details = findViewById(R.id.tfDetails);
        TextView date = findViewById(R.id.tvDate);
        TextView time = findViewById(R.id.tvTime);
        RadioButton high = findViewById(R.id.rbHigh);
        RadioButton med = findViewById(R.id.rbMedium);
        RadioButton low = findViewById(R.id.rbLow);


        task.setText(currentReminder.getTask());
        details.setText(currentReminder.getDetails());
        date.setText(DateFormat.format("MM/dd/yyyy", currentReminder.getDate().getTimeInMillis()).toString());
        time.setText(currentReminder.getTime());
        if(currentReminder.getPriority() == 3){
            low.setChecked(true);
        }else if (currentReminder.getPriority() == 2){
            med.setChecked(true);
        }else {
            high.setChecked(true);
        }
    }

    private void initReset(){
        EditText task = findViewById(R.id.tfToDo);
        EditText details = findViewById(R.id.tfDetails);
        RadioButton rbLow = findViewById(R.id.rbLow);


        task.setText("");
        details.setText("");
        rbLow.setChecked(true);
        getDateAndTime();
    }

}