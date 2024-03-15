package com.dkit.oop.sd2.DAOs;

import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import java.util.List;


public interface TaskDaoInterface {

    /**
     * Meghan Keightley 9 Mar 2024
     */
    // Feature 1 - Get all Entities
    List<Task> getAllTasks() throws DaoException;

    /**
     * Meghan Keightley 9 Mar 2024.
     */
    // Feature 2 - Find and Display a single Entity by Key
    Task getTaskById(int taskId) throws DaoException;

    /**
     * Meghan Keightley 9 Mar 2024
     */
    // Feature 3 - Placeholder for future method
    public Task deleteTaskById (int taskId ) throws DaoException;


    /*Feature - 4*/
    public Task addTask(Task task) throws DaoException;


    /**
     * Meghan Keightley 15 Mar 2024
     */
    // Feature 5 - Update an existing Entity by ID
    Task updateTaskById(int taskId, Task updatedTask) throws DaoException;

    /**
     * Meghan Keightley 15 Mar 2024
     */
    // Feature 6 -  Get list of entities matching a filter (based on DTO object)
    List<Task> FilteringTasks(Task filter) throws DaoException;


}

