package catan.common.launcher;

/**
 * Created by greg on 4/23/16.
 * Class to ingest the argument list and control startup behavior.
 */
public class StartupOptions {

    private boolean hasError = false;
    private String errorMessage = "";

    public StartupOptions(String[] args) {
        for (int i = 0; i < args.length; i++) {
            //TODO: parse the command line arguments into startup commands.
        }
    }

    public boolean hasError() {
        return hasError;
    }

    public void printError() {
        System.out.println(errorMessage);
    }
}
