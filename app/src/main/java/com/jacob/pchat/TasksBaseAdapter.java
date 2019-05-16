package com.jacob.pchat;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TasksBaseAdapter extends BaseAdapter {
    Context x;
    List<Task> taskList;
    TaskDao taskDao;
    ListView listView;
    String activetab;

    public TasksBaseAdapter(Context x, List<Task> taskList, TaskDao taskDao, ListView listView,String activetab) {
        this.x = x;
        this.taskList = taskList;
        this.taskDao = taskDao;
        this.listView = listView;
        this.activetab=activetab;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if(convertView==null){
            convertView= LayoutInflater.from(x).inflate(R.layout.task_layout,null);
        }
        final Task task=taskList.get(position);
        TextView txtname=convertView.findViewById(R.id.listviewtaskname);
        TextView txtdescription=convertView.findViewById(R.id.listviewtaskdescription);
        //TextView txttime=convertView.findViewById(R.id.listviewtaskdatecreated);
        TextView txtupdatestate=convertView.findViewById(R.id.txttaskstateupdate);
        //ImageView state=convertView.findViewById(R.id.taskstateimage);
        final LinearLayout delete=convertView.findViewById(R.id.linearlayoutDeletetask);
        LinearLayout update=convertView.findViewById(R.id.linearlayoutUpdatetask);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task1=taskList.get(position);
                taskDao.delete(task1);
                if(activetab.equals("Tasks")){
                    List<Task> tasks=taskDao.getTasks();
                    notifyDataSetChanged();
                    listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Tasks"));
                }else if(activetab.equalsIgnoreCase("Completed Tasks")){
                    List<Task> tasks=taskDao.getTasksByState(1);
                    notifyDataSetChanged();
                    listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Completed Tasks"));
                }else {
                    List<Task> tasks=taskDao.getTasksByState(0);
                    notifyDataSetChanged();
                    listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Incomplete Tasks"));
                }


            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.getState()==1){
                    task.setState(0);
                    taskDao.update(task);
                    if(activetab.equalsIgnoreCase("Tasks")){
                        List<Task> tasks=taskDao.getTasks();
                        notifyDataSetChanged();
                        listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Tasks"));
                    }else if(activetab.equalsIgnoreCase("Completed Tasks")){
                        List<Task> tasks=taskDao.getTasksByState(1);
                        notifyDataSetChanged();
                        listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Completed Tasks"));
                    }else{
                        List<Task> tasks=taskDao.getTasksByState(0);
                        notifyDataSetChanged();
                        listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Incomplete Tasks"));
                    }


                }else{
                    task.setState(1);
                    taskDao.update(task);
                    if(activetab.equalsIgnoreCase("Tasks")){
                        List<Task> tasks=taskDao.getTasks();
                        notifyDataSetChanged();
                        listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Tasks"));
                    }else if(activetab.equalsIgnoreCase("Completed Tasks")){
                        List<Task> tasks=taskDao.getTasksByState(1);
                        notifyDataSetChanged();
                        listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Completed Tasks"));
                    }else{
                        List<Task> tasks=taskDao.getTasksByState(0);
                        notifyDataSetChanged();
                        listView.setAdapter(new TasksBaseAdapter(x,tasks,taskDao,listView,"Incomplete Tasks"));
                    }
                }
            }
        });

        txtname.setText(task.getName());
        if(task.getDescription().length()<256){
            txtdescription.setText(task.getDescription());
        }else{
            txtdescription.setText(task.getDescription().substring(0,256)+"...");
        }

        Date date=new Date(task.getDateadded());
        DateFormat dateFormat=new SimpleDateFormat("hh:mm a d MMMM y ");
        String createdtime=dateFormat.format(date);
        //txttime.setText("Saved at: "+createdtime);

        if(task.getState()==0){
         // state.setImageResource(R.drawable.incomplete);
            txtupdatestate.setText("Completed");
        }else{
           //state.setImageResource(R.drawable.completed);
            txtupdatestate.setText("Incomplete");
        }


        return convertView;



    }
}
