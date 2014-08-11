package com.gregswebserver.catan.network;

import java.util.ArrayList;

/**
 * Created by Greg on 8/10/2014.
 * Call-response structure for communicating from client to server.
 */
public class Request {

    private Object command;
    private boolean completed;
    private ArrayList<String> result;

    public Request(Object command) {
        this.command = command;
        completed = false;
    }

    public void setCompleted() {
        synchronized (this) {
            completed = true;
        }
    }

    public boolean getCompleted() {
        synchronized (this) {
            return completed;
        }
    }

    public Object getCommand() {
        return command;
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> o) {
        result = o;
    }
}
