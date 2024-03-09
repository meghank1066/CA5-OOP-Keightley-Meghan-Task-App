package com.dkit.oop.sd2.DAOs;

import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import java.util.List;


public interface TaskDaoInterface {

    // Feature 1 - Get all Entities
    List<Task> getAllTasks() throws DaoException;

    // Feature 2 - Find and Display a single Entity by Key
    Task getTaskById(int taskId) throws DaoException;

    // Feature 3 - Placeholder for future method
    public Task deleteTaskById (int taskId ) throws DaoException;


}

