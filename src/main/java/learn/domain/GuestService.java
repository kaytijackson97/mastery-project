package learn.domain;

import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.GuestRepository;

public class GuestService implements UserService{

    private final GuestRepository repository;

    public GuestService(GuestRepository guestRepository) {
        this.repository = guestRepository;
    }

    public Result<User> findByEmail(String email) throws DataAccessException {
        Result<User> result = validateEmail(email);
        if (!result.isSuccess()) {
            return result;
        }

        User guest = repository.findByEmail(email);
        if(guest == null) {
            result.addErrorMessage("Guest could not be found.");
            return result;
        }

        result.setPayload(guest);
        return result;
    }

    public Result<User> validateEmail(String email) {
        Result<User> result = new Result<>();
        if (email == null || email.isBlank()) {
            result.addErrorMessage("Email cannot but empty");
            return result;
        }

        if (!email.contains("@")) {
            result.addErrorMessage("Not a valid email address");
            return result;
        }

        String[] emailParts = email.split("@");
        if (emailParts.length != 2 || !emailParts[emailParts.length - 1].contains(".")) {
            result.addErrorMessage("Not a valid email address");
        }
        return result;
    }
}
