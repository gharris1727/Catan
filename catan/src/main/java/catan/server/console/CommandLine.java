package catan.server.console;

import java.util.Scanner;

/**
 * Created by greg on 4/27/16.
 * A class to read lines from stdin and spit them to the server console.
 */
public class CommandLine extends Thread {

    private boolean running;
    private final Console console;

    public CommandLine(Console console) {
        running = true;
        this.console = console;
    }

    @Override
    public void run() {
        //TODO: handle errors and recover from them.
        try (Scanner input = new Scanner(System.in)) {
            while (running) {
                console.process(input.nextLine());
            }
        }
    }

    public void close() {
        running = false;
    }
}
