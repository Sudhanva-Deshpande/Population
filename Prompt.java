import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 *      Prompt.java - Uses BufferedReader.
 *      Provides utilities for user input.  This enhances the BufferedReader
 *      class so our programs can recover from "bad" input, and also provides
 *      a way to limit numerical input to a range of values.
 *
 *      The advantages of BufferedReader are speed, synchronization, and piping
 *      data in Linux.
 *
 *      @author Sudhanva Deshpande
 *      @since  August 25, 2022
 */

public class Prompt
{
    // BufferedReader variables
    private static InputStreamReader streamReader =
                    new InputStreamReader(System.in);
    private static BufferedReader buffReader =
                    new BufferedReader(streamReader);

    /**
     *   Prompts user for string of characters and returns the string.
     *   @param ask  The prompt line
     *   @return The string input
     */
    public static String getString (String ask)
    {
        System.out.print(ask+" -> ");
        String input = "";
        try
        {
            input = buffReader.readLine();
        }
        catch(IOException e)
        {
            System.err.println("ERROR: BufferedReader could not read line.");
        }
        return input;
    }

    /**
     *  Prompts the user for a character and returns the character.
     *  @param ask  The prompt line
     *  @return         The character input
     */
    public static char getChar (String ask)
    {
        String ch = getString(ask);
        boolean isChar = false;

        while(!isChar && ch.length()!=1)
        {
            if(ch.length() == 1)
                isChar = true;
            else
                ch = getString(ask);
        }

        return ch.charAt(0);
    }

    /**
     *  Prompts the user for an integer and returns the integer.
     *  @param ask  The prompt line
     *  @return     The integer input
     */
    public static int getInt (String ask)
    {
        int val = 0;
        boolean found = false;

        while(!found)
        {
            String str = getString(ask);
            try
            {
                val = Integer.parseInt(str);
                found = true;
            }
            catch(NumberFormatException e)
            {
                found = false;
                System.out.println("Invalid input. Try again");
            }
        }
        return val;
    }

    /**
     *      Prompts the user for an integer using a range of min to max,
     *      and returns the integer.
     *      @param ask  The prompt line
     *      @param min  The minimum integer accepted
     *      @param max  The maximum integer accepted
     *      @return The integer input
     */
    public static int getInt (String ask, int min, int max)
    {
        int val = 0;
        do
        {
                val = getInt(ask + " (" + min +", "+max+ ")");
        } while(val < min || val > max);
        return val;
    }

    /**
     *      Prompts the user for a double and returns the double.
     *      @param ask  The prompt line
     *      @return  The double input
     */
    public static double getDouble (String ask)
    {
        double dble = 0.0;
        boolean isDble = false;

        while(!isDble)
        {
            String str = getString(ask);
            try
            {
                dble = Double.parseDouble(str);
                isDble = true;
            }
            catch(NumberFormatException e)
            {
                isDble = false;
            }
        }
        return dble;
    }

    /**
    *      Prompts the user for a double and returns the double.
    *      @param ask  The prompt line
    *      @param min  The minimum double accepted
    *      @param max  The maximum double accepted
    *      @return  The double input
    */
    public static double getDouble (String ask, double min, double max)
    {
        double dble = 0.0;
        do
        {
            dble = getDouble(ask + " (" + min +", "+max+ ")");
        } while(dble < min || dble > max);
        return dble;
    }
}


