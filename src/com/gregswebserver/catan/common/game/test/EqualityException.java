package com.gregswebserver.catan.common.game.test;

import com.gregswebserver.catan.common.log.Logger;

/**
 * Created by greg on 6/19/16.
 * An exception thrown when two objects are unequal and should be.
 */
public class EqualityException extends Exception {

    public static String buildMessage(String message, Object a, Object b) {
        StringBuilder out = new StringBuilder();
        out.append(Logger.ANSI_WHITE);
        out.append(message);
        out.append(": ");

        String sA = a == null ? "null" : a.toString();
        String sB = b == null ? "null" : b.toString();

        if (sA.equals(sB)) {
            //Just print out the whole string as there were no changes.
            out.append(Logger.ANSI_WHITE);
            out.append(sA);
        } else if (sA.length() == 0) {
            out.append(Logger.ANSI_GREEN);
            out.append(sB);
        } else if (sB.length() == 0) {
            out.append(Logger.ANSI_RED);
            out.append(sA);
        } else {
            int[][] array = new int[sB.length()+1][sA.length()+1];
            //Put together the first row.
            for (int j = 0; j <= sA.length(); j++) {
                //Pure deletions from sA -> sB
                array[0][j] = j;
            }
            //Loop over the sB from the top of the table downward.
            for (int i = 1; i <= sB.length(); i++) {
                //Pure addition from sA -> sB
                array[i][0] = array[i-1][0] + 1;
                //Iteratively compute the edit distance for each character of sA.
                for (int j = 1; j <= sA.length(); j++) {
                    //If the character matches, we get away without an edit.
                    if (sB.charAt(i-1) == sA.charAt(j-1))
                        array[i][j] = array[i-1][j-1];
                    else //Otherwise the edit can be an addition, or deletion.
                        array[i][j] = Math.min(array[i-1][j], array[i][j-1]) + 1;
                }
            }
            //Print out the string by walking backward to reconstruct the correct path.
            int[] directions = new int[sA.length() + sB.length() + 1];
            //The indices array represents the lowest constituent index for each column
            for (int i=sB.length(), j=sA.length();i!=0 || j!=0;) {
                if (i!=0 && j!=0 && array[i][j] == array[i-1][j-1] && sB.charAt(i-1) == sA.charAt(j-1))
                    directions[--i + --j] = 1; //EXACT
                else if (j==0 || (i != 0 && array[i][j] == array[i-1][j]+1))
                    directions[--i + j] = 2; //ADDITION
                else if (i==0 || (j != 0 && array[i][j] == array[i][j-1]+1))
                    directions[i + --j] = 3; //DELETION
            }
            //Track the last color we printed to avoid spamming color chars.
            String lastColor = Logger.ANSI_WHITE;
            //Loop over the backtracking array.
            for (int i=0, j=0; i<sB.length() || j<sA.length();) {
                switch (directions[i+j]) {
                    case 1: //EXACT
                        if (lastColor != Logger.ANSI_WHITE)
                            out.append(Logger.ANSI_WHITE);
                        lastColor = Logger.ANSI_WHITE;
                        out.append(sA.charAt(j));
                        i++;
                        j++;
                        break;
                    case 2: //ADDITION
                        if (lastColor != Logger.ANSI_GREEN)
                            out.append(Logger.ANSI_GREEN);
                        lastColor = Logger.ANSI_GREEN;
                        out.append(sB.charAt(i));
                        i++;
                        break;
                    case 3: //DELETION
                        if (lastColor != Logger.ANSI_RED)
                            out.append(Logger.ANSI_RED);
                        lastColor = Logger.ANSI_RED;
                        out.append(sA.charAt(j));
                        j++;
                        break;
                }
            }
        }
        out.append(Logger.ANSI_WHITE);
        out.append(".");
        out.append(Logger.ANSI_RESET);
        return out.toString();
    }
    
    public EqualityException(String message, Object a, Object b) {
        super(buildMessage(message, a, b));
    }

    public EqualityException(String message, Exception cause) {
        super(message, cause);
    }
}
