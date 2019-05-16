package com.jacob.pchat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewTasks extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.listoftasks)
    ListView listViewtasks;
    @BindView(R.id.viewtasklinearlayout)
    LinearLayout viewtasklinearlayout;
    TaskDao taskDao;
    TasksBaseAdapter tasksBaseAdapter;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskDao=DatabaseClass.getInstance(this).getTask();
        setContentView(R.layout.activity_view_tasks);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog addItem=new Dialog(ViewTasks.this);
                addItem.setContentView(R.layout.add_item_dialog);
                ImageView closebutton=addItem.findViewById(R.id.ButtonCloseAddTaskDialog);
                closebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItem.dismiss();
                    }
                });
                final EditText txttaskname=addItem.findViewById(R.id.InputTaskName);
                final EditText txttaskDescription=addItem.findViewById(R.id.InputTaskDescription);
                final RadioGroup radioGroup=addItem.findViewById(R.id.radioTaskSTate);
                Button addTask=addItem.findViewById(R.id.ButtonSaveTask);
                addTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String taskname=txttaskname.getText().toString();
                        String description=txttaskDescription.getText().toString();
                        int id=radioGroup.getCheckedRadioButtonId();
                        RadioButton stateradiobutton=addItem.findViewById(id);
                        String state=(String) stateradiobutton.getText();
                        int x;
                        if(state.equalsIgnoreCase("Complete")){
                            x=1;
                        }else{
                            x=0;
                        }
                        if(taskname.isEmpty()){
                            Toast.makeText(ViewTasks.this, "Please fill in the task title", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(description.isEmpty()){
                            Toast.makeText(ViewTasks.this, "Please fill in the task description", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Date d=new Date();
                        long time=d.getTime();
                        Task newTask=new Task();
                        newTask.setName(taskname);
                        newTask.setDescription(description);
                        newTask.setState(x);
                        newTask.setDateadded(time);
                        taskDao.insert(newTask);
                        addItem.dismiss();
                        String title=(String) getSupportActionBar().getTitle();
                        tasksBaseAdapter.notifyDataSetChanged();
                        if(title.equalsIgnoreCase("Tasks")){
                            List<Task> tasks=taskDao.getTasks();
                            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,tasks,taskDao,listViewtasks,"Tasks");
                            listViewtasks.setAdapter(tasksBaseAdapter);

                        }else if(title.equalsIgnoreCase("Completed Tasks")){
                            List<Task> tasks=taskDao.getTasksByState(1);
                            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,tasks,taskDao,listViewtasks,"Completed Tasks");
                            listViewtasks.setAdapter(tasksBaseAdapter);

                        }else{
                            List<Task> tasks=taskDao.getTasksByState(0);
                            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,tasks,taskDao,listViewtasks,"Incomplete Tasks");
                            listViewtasks.setAdapter(tasksBaseAdapter);

                        }

                        Snackbar.make(viewtasklinearlayout.getRootView(),"Task saved successfully ",Snackbar.LENGTH_SHORT)
                                .setDuration(5000)
                                .show();


                    }
                });
                addItem.setCancelable(false);
                addItem.show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final List<Task> taskList=taskDao.getTasks();
        tasksBaseAdapter=new TasksBaseAdapter(this,taskList,taskDao,listViewtasks,"Tasks");
        listViewtasks.setAdapter(tasksBaseAdapter);


    }

    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_tasks, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.markAllComplete){
            taskDao.updateAllToState(1);
            tasksBaseAdapter.notifyDataSetChanged();
            List<Task> tasks=taskDao.getTasksByState(0);
            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,tasks,taskDao,listViewtasks,"Incomplete Tasks");
            listViewtasks.setAdapter(tasksBaseAdapter);
            Snackbar.make(viewtasklinearlayout.getRootView(),"Tasks updated as complete",Snackbar.LENGTH_SHORT)
                    .setDuration(3000)
                    .show();
        }

        if(id == R.id.markAllIncomplete){
            taskDao.updateAllToState(0);
            tasksBaseAdapter.notifyDataSetChanged();
            List<Task> tasks=taskDao.getTasksByState(1);
            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,tasks,taskDao,listViewtasks,"Incomplete Tasks");
            listViewtasks.setAdapter(tasksBaseAdapter);
            Snackbar.make(viewtasklinearlayout.getRootView(),"Task updated as incomplete",Snackbar.LENGTH_SHORT)
                    .setDuration(3000)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(Build.VERSION.SDK_INT>11){
            invalidateOptionsMenu();
            menu.clear();
            String tab=(String) getSupportActionBar().getTitle();
            if(tab.equalsIgnoreCase("Tasks")){
                //getMenuInflater().inflate(R.menu.view_tasks,menu);
            }else if(tab.equalsIgnoreCase("Completed Tasks")){
                getMenuInflater().inflate(R.menu.complete_tasks_tab_menu,menu);
            }else{
                getMenuInflater().inflate(R.menu.incomplete_task_menu,menu);
            }

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.seeAllTasks) {
            getSupportActionBar().setTitle("Tasks");
            tasksBaseAdapter.notifyDataSetChanged();
            List<Task> alltasks=taskDao.getTasks();
            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,alltasks,taskDao,listViewtasks,"Tasks");
            listViewtasks.setAdapter(tasksBaseAdapter);
            // Handle the camera action
        } else if (id == R.id.seeCompleteTasks) {
            getSupportActionBar().setTitle("Completed Tasks");
            tasksBaseAdapter.notifyDataSetChanged();
            List<Task> alltasks=taskDao.getTasksByState(1);
            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,alltasks,taskDao,listViewtasks,"Completed Tasks");
            listViewtasks.setAdapter(tasksBaseAdapter);

        } else if (id == R.id.seeIncompleteTasks) {
            getSupportActionBar().setTitle("Incomplete Tasks");
            tasksBaseAdapter.notifyDataSetChanged();
            List<Task> alltasks=taskDao.getTasksByState(0);
            tasksBaseAdapter=new TasksBaseAdapter(ViewTasks.this,alltasks,taskDao,listViewtasks,"Incomplete Tasks");
            listViewtasks.setAdapter(tasksBaseAdapter);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


}
