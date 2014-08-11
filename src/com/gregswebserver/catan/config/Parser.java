package com.gregswebserver.catan.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by Greg on 8/10/2014.
 * Parser class to parse through a given file and return any formatting data along with the real data.
 */
public class Parser extends Reader {

    private BufferedReader buffer;
    private ParseTag activeTag;

    public Parser(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
    }

    public String readData() {
        String line;
        try {
            if ((line = buffer.readLine()) != null) {
                line.trim();
                if (line.charAt(0) == '<') { //Tag detected
                    for (ParseTag tag : ParseTag.values()) {
                        if (line.equals(tag.getText())) {
                            activeTag = tag;
                        }
                    }
                    return readData(); //Call again to find the next piece of valid data.
                } else {
                    return line; // Return the raw data.
                }
            }
        } catch (IOException e) {
        }
        return null;
    }

    public ParseTag getActiveTag() {
        return activeTag;
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        return 0;
    }

    public void close() throws IOException {
        buffer.close();
    }
}
