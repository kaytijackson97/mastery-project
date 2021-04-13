package ui;

import java.util.Scanner;

public class ConsoleIO {

    private static final String INVALID_NUMBER_PROMPT = "Input must be a whole number";

    private final Scanner console = new Scanner(System.in);

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    public String readString(String prompt) {
        println(prompt);
        return console.nextLine();
    }

    public String readRequiredString(String prompt) {
        String input = "";
        do {
            input = readString(prompt);

        } while (input.isBlank());
        return input;
    }

    public int readInt(String prompt) {
        int newValue = 0;
        do {
            String input = readRequiredString(prompt);
            try {
                newValue = Integer.parseInt(input);
                return newValue;
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER_PROMPT);
            }
        } while (true);
    }

    public int readInt(String prompt, int min, int max) {
        int newValue = 0;

        do {
            newValue = readInt(prompt);
        } while (newValue < min || newValue > max);

        return newValue;
    }
}
