package learn.ui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleIO {

    private static final String INVALID_NUMBER_PROMPT = "Input must be a number";
    private static final String INVALID_DATE_FORMAT_PROMPT = "Input must be formatted as YYYY-MM-DD";
    private static final String INVALID_DATE_PROMPT = "Input must be after %s%n";
    private static final String INVALID_BOOLEAN_PROMPT = "Input must be [y/n]";
    private static final String INVALID_EMAIL_PROMPT = "Invalid email";
    private static final String INVALID_PHONE_FORMAT_PROMPT = "Invalid phone number format (ex. (123) 4567890)";
    private static final String INVALID_AMOUNT = "Invalid amount";
    private static final String INVALID_POSTAL_CODE = "Invalid postal code";

    private final Scanner console = new Scanner(System.in);
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public int readInt(String prompt, int previousValue) {
        int newValue = 0;
        do {
            String input = readString(prompt);
            if (input.isBlank()) {
                return previousValue;
            }

            newValue = getInt(input);
        } while (newValue < 0);
        return newValue;
    }

    public int readRequiredInt(String prompt) {
        int newValue = 0;
        do {
            String input = readRequiredString(prompt);
            newValue = getInt(input);

        } while (newValue < 0);
        return newValue;
    }

    public int readRequiredInt(String prompt, int min, int max) {
        int newValue = 0;

        do {
            newValue = readRequiredInt(prompt);
        } while (newValue < min || newValue > max);

        return newValue;
    }

    public String readRequiredPostalCode(String prompt) {
        String postalCode = "";
        do {
            postalCode = readRequiredString(prompt);
            if (postalCode.length() != 5) {
                println(INVALID_POSTAL_CODE);
                postalCode = "";
                continue;
            }
            int postalCodeInt = getInt(postalCode);
            if (postalCodeInt < 0) {
                println(INVALID_POSTAL_CODE);
                postalCode = "";
            }
        } while (postalCode.isBlank());
        return postalCode;
    }

    public String readPostalCode(String prompt, String previousPostalCode) {
        String postalCode = "";
        do {
            postalCode = readString(prompt);
            if (postalCode.isBlank()) {
                return previousPostalCode;
            }
            if (postalCode.length() != 5) {
                println(INVALID_POSTAL_CODE);
                postalCode = "";
                continue;
            }
            int postalCodeInt = getInt(postalCode);
            if (postalCodeInt < 0) {
                println(INVALID_POSTAL_CODE);
                postalCode = "";
            }
        } while (postalCode.isBlank());
        return postalCode;
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
            } catch (DateTimeParseException ex) {
                System.out.println(INVALID_DATE_FORMAT_PROMPT);
            }
            if (date.isBefore(LocalDate.now().plusDays(1))) {
                printf(INVALID_DATE_PROMPT, LocalDate.now());
            } else {
                return date;
            }
        } while (true);
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

    public String readState(String prompt, String previousState) {
        do {
            String state = readString(prompt);
            if (state.isBlank()) {
                return previousState;
            }

            if (state.length() != 2) {
                println("State must be abbreviation (ex. MN).");
                continue;
            }

            if (!Character.isLetter(state.charAt(0)) || !Character.isLetter(state.charAt(1))) {
                println("State must be abbreviation (ex. MN).");
                continue;
            }
            return state;
        } while (true);
    }

    public String readRequiredState(String prompt) {
        do {
            String state = readRequiredString(prompt);

            if (state.length() != 2) {
                println("State must be abbreviation (ex. MN).");
                continue;
            }

            if (!Character.isLetter(state.charAt(0)) || !Character.isLetter(state.charAt(1))) {
                println("State must be abbreviation (ex. MN).");
                continue;
            }
            return state;
        } while (true);
    }

    public String readRequiredEmail(String prompt) {
        while (true) {
            String email = readRequiredString(prompt);

            String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);

            if (matcher.matches()) {
                return email;
            }

            println(INVALID_EMAIL_PROMPT);
        }
    }

    public String readPhone(String prompt, String previousPhone) {
        String phone = "";
        do {
            phone = readString(prompt);
            if (phone.isBlank()) {
                return previousPhone;
            }

            phone = getPhone(phone);

            if (phone == null) {
                println(INVALID_PHONE_FORMAT_PROMPT);
            }

        } while (phone == null);
        return phone;
    }

    public String readRequiredPhone(String prompt) {
        String phone = "";
        do {
            phone = readRequiredString(prompt);

            phone = getPhone(phone);

            if (phone == null) {
                println(INVALID_PHONE_FORMAT_PROMPT);
            }

        } while (phone == null);
        return phone;
    }

    public BigDecimal readBigDecimal(String prompt, BigDecimal previousRate) {
        while (true) {
            String rate = readString(prompt);
            if (rate.isBlank()) {
                return previousRate;
            }

            try {
                return new BigDecimal(rate).setScale(2, RoundingMode.HALF_EVEN);
            } catch (NumberFormatException ex) {
                println(INVALID_AMOUNT);
            }
        }
    }

    public BigDecimal readRequiredBigDecimal(String prompt) {
        while (true) {
            String rate = readRequiredString(prompt);

            try {
                return new BigDecimal(rate).setScale(2, RoundingMode.HALF_EVEN);
            } catch (NumberFormatException ex) {
                println(INVALID_AMOUNT);
            }
        }
    }

    //support methods
    private int getInt(String input) {
        int newValue = -1;
        try {
            newValue = Integer.parseInt(input);
            return newValue;
        } catch (NumberFormatException ex) {
            println(INVALID_NUMBER_PROMPT);
        }
        return newValue;
    }

    private String getPhone(String phone) {
        if (phone == null) {
            return null;
        }

        String phoneRegex = "[(]\\d\\d\\d[)]\\s\\d\\d\\d\\d\\d\\d\\d";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            return null;
        }

        return phone;
    }
}
