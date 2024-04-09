package com.dkit.oop.sd2.JSON;


import com.google.gson.Gson;
import com.dkit.oop.sd2.DTOs.Task;

//public class JsonConv {
//
//    private Gson gsonParser;
//
//
//
//
//}
import java.util.List;


// meghan <3
public class JsonConv {


    private static Gson gson = new Gson();

    /**
     * Converting our list of entities to json strings <3
     * <p>
     * // meghan keightley
     * JSON representation of the list of tasks
     */

    //will take objects list to output
    public static String TaskConversionToJson(List<Task> list) {
        return gson.toJson(list);
    }
}