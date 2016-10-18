package com.microsoft.azure.documentdb.sample;

import com.microsoft.azure.documentdb.sample.esb.TopicHelper;

/**
 * Created by vazvadsk on 2016-10-18.
 */
public class Main {
    public static void main(String[] args) {
        // Prints "Hello, World" in the terminal window.
        System.out.println("Starting backend process ...");
        new TopicHelper().processToDo();
    }
}
