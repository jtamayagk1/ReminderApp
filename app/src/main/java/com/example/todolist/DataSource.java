package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Calendar;

public class DataSource {
    private SQLiteDatabase database;
    private Database dbHelper;

    public DataSource(Context context){
        dbHelper = new Database(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public boolean insertReminder(Reminder r){
        Boolean didSucceed = false;
        try{
            ContentValues initialValues = new ContentValues();
            initialValues.put("task", r.getTask());
            initialValues.put("priority", r.getPriority());
            initialValues.put("date", String.valueOf(r.getDate().getTimeInMillis()));
            initialValues.put("time", r.getTime());
            initialValues.put("details", r.getDetails());

            didSucceed = database.insert("reminder", null, initialValues) > 0;
        }catch (Exception ex) {

        }
        return didSucceed;
    }

    public boolean updateReminder(Reminder r){
        boolean didSucceed = false;
        try{
            Long rowID = (long) r.getReminderID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("task", r.getTask());
            updateValues.put("priority", r.getPriority());
            updateValues.put("date", String.valueOf(r.getDate().getTimeInMillis()));
            updateValues.put("time", r.getTime());
            updateValues.put("details", r.getDetails());

            didSucceed = database.update("reminder", updateValues, "_id=" + rowID, null) > 0;
        }catch (Exception ex){

        }
        return didSucceed;
    }


    public ArrayList<Reminder> getReminders(String sortBy, String sortOrder){
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        try{
            String query = "SELECT * FROM reminder ORDER BY " + sortBy + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);
            Reminder newReminder;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                newReminder = new Reminder();
                newReminder.setReminderID(cursor.getInt(0));
                newReminder.setTask(cursor.getString(1));
                newReminder.setPriority(cursor.getInt(2));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(3)));
                newReminder.setDate(calendar);
                newReminder.setTime(cursor.getString(4));
                newReminder.setDetails(cursor.getString(5));

                reminders.add(newReminder);
                cursor.moveToNext();
            }
        }catch (Exception ex){
            reminders = new ArrayList<Reminder>();
        }
        return reminders;
    }

    public int getLastReminderID(){
        int lastID;
        try{
            String query = "SELECT MAX(_id) from reminder";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        }catch(Exception ex){
            lastID = -1;
        }
        return lastID;
    }

    public Reminder getSpecificReminder(int reminderID){
        Reminder reminder = new Reminder();
        String query = "SELECT * FROM reminder WHERE _id=" + reminderID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst()){
            reminder.setReminderID(cursor.getInt(0));
            reminder.setTask(cursor.getString(1));
            reminder.setPriority(cursor.getInt(2));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(3)));
            reminder.setDate(calendar);
            reminder.setTime(cursor.getString(4));
            reminder.setDetails(cursor.getString(5));

            cursor.close();
        }
        return reminder;
    }

    public boolean deleteReminder(int reminderID){
        boolean didDelete = false;
        try {
            didDelete = database.delete("reminder", "_id=" + reminderID, null) > 0;
        }catch (Exception ex){

        }
        return didDelete;
    }
}
