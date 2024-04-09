package com.dkit.oop.sd2;

import static org.junit.Assert.assertTrue;

import com.dkit.oop.sd2.DAOs.MySqlTaskDAO;
import com.dkit.oop.sd2.DAOs.TaskDaoInterface;
import com.dkit.oop.sd2.DTOs.Task;
import com.dkit.oop.sd2.Exceptions.DaoException;
import com.dkit.oop.sd2.TaskObjects.TaskApp;
import org.junit.Test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    TaskDaoInterface taskDao = new MySqlTaskDAO();


    @Test
    public void testDisplayTaskById() throws DaoException {

        int existingTaskId = 1;

        // Retrieve the task from the database
        Task actualTask = taskDao.getTaskById(existingTaskId);

        // Check if the retrieved task is not null
        assertNotNull(actualTask);

        // Check if the task ID matches the expected ID
        assertEquals(existingTaskId, actualTask.getTaskId()); // Expected: 1, Actual: actualTask.getTaskId()

        // Check other properties of the task
        assertEquals("CA2 for Server Side", actualTask.getTitle()); // Expected: "CA2 for Server Side", Actual: actualTask.getTitle()
        assertEquals("PROGRESS", actualTask.getStatus()); // Expected: "PROGRESS", Actual: actualTask.getStatus()
        assertEquals("HIGH", actualTask.getPriority()); // Expected: "HIGH", Actual: actualTask.getPriority()
        assertEquals("Create a Blog Website with PHP & MySQL in Laravel", actualTask.getDescription()); // Expected: "Create a Blog Website with PHP & MySQL in Laravel", Actual: actualTask.getDescription()
        assertEquals("2024-03-15", actualTask.getDueDate().toString()); // Expected: "2024-03-15", Actual: actualTask.getDueDate().toString()
    }
}
