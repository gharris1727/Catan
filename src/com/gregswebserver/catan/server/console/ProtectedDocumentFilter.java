package com.gregswebserver.catan.server.console;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Created by Greg on 8/12/2014.
 * Document filter allowing for both editable and non-editable sections of a JTextArea.
 */
public class ProtectedDocumentFilter extends DocumentFilter {

    private UserInput userInput;

    public ProtectedDocumentFilter(UserInput userInput) {
        this.userInput = userInput;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (offset >= getUserInput().getUserInputStart()) {
            super.insertString(fb, offset, string, attr);
        }
    }

    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (offset >= getUserInput().getUserInputStart()) {
            super.remove(fb, offset, length);
        }
    }

    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (offset >= getUserInput().getUserInputStart()) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
