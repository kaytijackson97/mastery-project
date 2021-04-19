package learn.domain;

import learn.models.User;

import java.math.BigDecimal;

public class Validation {

    public Result<User> validateUser(User user, String userType) {
        Result<User> result = validateNoNulls(user, userType);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateFields(user);
        return result;
    }

    public Result<User> validateNoNulls(User user, String userType) {
        Result<User> result = new Result<>();

        if (user == null) {
            result.addErrorMessage(userType + " cannot be null.");
            return result;
        }

        if (user.getLastName() == null || user.getLastName().isBlank()) {
            result.addErrorMessage("Last name cannot be empty.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            result.addErrorMessage("Email cannot be empty.");
        }

        if (user.getPhone() == null || user.getPhone().isBlank()) {
            result.addErrorMessage("Phone cannot be empty.");
        }

        if (user.getState() == null || user.getState().isBlank()) {
            result.addErrorMessage("State cannot be empty.");
        }
        return result;
    }

    public Result<User> validateFields(User user) {
        Result<User> result = validateEmail(user.getEmail());
        if (!result.isSuccess()) {
            return result;
        }

        result = validatePhone(user.getPhone());
        if (!result.isSuccess()) {
            return result;
        }

        result = validateState(user.getState());
        return result;
    }

    public Result<User> validateFullName(User user) {
        Result<User> result = new Result<>();
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            result.addErrorMessage("Name cannot be empty.");
        }

        String name = user.getFullName();
        String[] nameFields = name.split(",");
        if (nameFields.length != 2) {
            result.addErrorMessage("Invalid Name.");
        }

        if (nameFields[0] == null || nameFields[0].isBlank()) {
            result.addErrorMessage("First name cannot be empty.");
        }
        return result;
    }

    public Result<User> validateEmail(String email) {
        Result<User> result = new Result<>();
        if (email == null || email.isBlank()) {
            result.addErrorMessage("Email cannot but empty.");
            return result;
        }

        if (email.contains(",")) {
            result.addErrorMessage("Email cannot contain a comma");
            return result;
        }

        if (!email.contains("@")) {
            result.addErrorMessage("Not a valid email address.");
            return result;
        }

        String[] emailParts = email.split("@");
        if (emailParts.length != 2 || !emailParts[emailParts.length - 1].contains(".")) {
            result.addErrorMessage("Not a valid email address.");
        }
        return result;
    }

    public Result<User> validatePhone(String phone) {
        Result<User> result = new Result<>();

        if (phone.length() != 13) {
            result.addErrorMessage("Not a valid phone format.");
            return result;
        }

        for (int i = 0; i < phone.length(); i++) {
            switch (i) {
                case 0:
                    if (phone.charAt(i) != '(') {
                        result.addErrorMessage("Not a valid phone format.");
                    }
                    break;

                case 4:
                    if (phone.charAt(i) != ')') {
                        result.addErrorMessage("Not a valid phone format.");
                    }
                    break;

                case 5:
                    if (phone.charAt(i) != ' ') {
                        result.addErrorMessage("Not a valid phone format.");
                    }
                    break;

                default:
                    if (!Character.isDigit(phone.charAt(i))) {
                        result.addErrorMessage("Not a valid phone format.");
                    }
                    break;
            }
        }
        return result;
    }

    public Result<User> validateFullAddress(User user) {
        Result<User> result = new Result<>();

        String address = user.getFullAddress();
        String[] addressFields = address.split(",");
        if (addressFields.length != 4) {
            result.addErrorMessage("Invalid Address.");
        }

        if (addressFields[addressFields.length - 1].length() != 5) {
            result.addErrorMessage("Invalid postal code.");
        }

        try {
            String postalCode = addressFields[addressFields.length - 1];
            int postalCodeInt = Integer.parseInt(postalCode);
            if (postalCodeInt < 0) {
                result.addErrorMessage("Invalid postal code.");
            }
        } catch (NumberFormatException ex) {
            result.addErrorMessage("Invalid postal code.");
        }

        return result;
    }

    public Result<User> validateState(String state) {
        Result<User> result = new Result<>();

        if (state.length() != 2) {
            result.addErrorMessage("State must be abbreviation (ex. MN).");
        }

        if (!Character.isLetter(state.charAt(0)) || !Character.isLetter(state.charAt(1))) {
            result.addErrorMessage("State must be abbreviation (ex. MN).");
        }

        return result;
    }

    public Result<User> validateRates(User user) {
        Result<User> result = new Result<>();
        if (user.getRates().size() != 2) {
            result.addErrorMessage("Invalid rates");
        }

        if (user.getRates().get(0).compareTo(BigDecimal.ZERO) <= 0) {
            result.addErrorMessage("Standard rate must be greater than 0.");
        }

        if (user.getRates().get(1).compareTo(BigDecimal.ZERO) <= 0) {
            result.addErrorMessage("Weekend rate must be greater than 0.");
        }

        return result;
    }
}
