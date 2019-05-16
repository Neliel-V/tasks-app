package com.jacob.pchat;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("select * from tasks order by dateadded desc")
    List<Task> getTasks();
    @Insert()
    void insert(Task task);
    @Update()
    void update(Task task);
    @Delete()
    void delete(Task task);
    @Query("select * from tasks where state=:state")
    List<Task> getTasksByState(int state);
    @Query("update tasks set state=:state")
    void updateAllToState(int state);

}
