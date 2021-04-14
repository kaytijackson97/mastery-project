package learn.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleIO {

    private static final String INVALID_NUMBER_PROMPT = "Input must be a whole number";
    private static final String INVALID_DATE_FORMAT_PROMPT = "Input must be formatted as YYYY-MM-DD";
    private static final String INVALID_DATE_PROMPT = "Input must be after %s%n";
    private static final String INVALID_BOOLEAN_PROMPT = "Input must be [y/n]";

    private final Scanner console = new Scanner(System.in);
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public LocalDate readDate(String prompt, LocalDate previousDate) {
        do {
            String input = readString(prompt);
            if (input.isBlank()) {
                return previousDate;
            }
            try {
                previousDate = LocalDate.parse(input, ConsoleIO.FORMATTER);
                return previousDate;
            } catch (DateTimeParseException ex) {
                println(INVALID_DATE_FORMAT_PROMPT);
            }
            printf(INVALID_DATE_PROMPT, LocalDate.now());
        } while (true);
    }

    public LocalDate readDate(String prompt, LocalDate previousDate, LocalDate startDate) {
        LocalDate date;
        do {
            date = readDate(prompt, previousDate);
            if (date.isAfter(startDate)) {
                return date;
            }
            printf(INVALID_DATE_PROMPT, startDate);
        } while (true);
    }

    public LocalDate readRequiredDate(String prompt) {
        LocalDate date = LocalDate.now();
        do {
            String dateString = readRequiredString(prompt);
            try {
                date = LocalDate.parse(dateString, FORMATTER);
                break;
            } catch (DateTimeParseException ex) {
                System.out.println(INVALID_DATE_FORMAT_PROMPT);
            }
            printf(INVALID_DATE_PROMPT, LocalDate.now());
        } while (date.isBefore(LocalDate.now().plusDays(1)));

        return date;
    }

    public LocalDate readRequiredDate(String prompt, LocalDate startDate) {
        LocalDate date = LocalDate.now();
        do {
            date = readRequiredDate(prompt);
            if (date.isAfter(startDate)) {
                return date;
            }
            printf(INVALID_DATE_PROMPT, startDate);
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
