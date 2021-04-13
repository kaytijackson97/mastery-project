package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleIO {

    private static final String INVALID_NUMBER_PROMPT = "Input must be a whole number";
    private static final String INVALID_DATE_PROMPT = "Input must be formatted as YYYY-MM-DD";
    private static final String INVALID_BOOLEAN_PROMPT = "Input must be [y/n]";

    private final Scanner console = new Scanner(System.in);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public LocalDate readDate(String prompt) {
        LocalDate date = LocalDate.now();
        do {
            String dateString = readRequiredString(prompt);
            try {
                date = LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException ex) {
                System.out.println(INVALID_DATE_PROMPT);
            }
        } while (date.isBefore(LocalDate.now().plusDays(1)));

        return date;
    }

    public LocalDate readDate(String prompt, LocalDate startDate) {
        LocalDate date = LocalDate.now();
        do {
            date = readDate(prompt);
            if (date.isAfter(startDate)) {
                return date;
            }
        } while (true);
    }

    public boolean readBoolean(String prompt) {
        do {
            String input = readRequiredString(prompt);
            switch (input.toLowerCase()) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    println(INVALID_BOOLEAN_PROMPT);
            }
        } while (true);
    }
}
