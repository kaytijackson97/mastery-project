package learn.domain;

import learn.models.Guest;
import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.GuestRepository;

public class GuestService implements UserService {

    private final GuestRepository repository;
    private final Validation validation = new Validation();

    public GuestService(GuestRepository guestRepository) {
        this.repository = guestRepository;
    }

    public Result<User> findByEmail(String email) throws DataAccessException {
        Result<User> result = validation.validateEmail(email);
        if (!result.isSuccess()) {
            return result;
        }

        User guest = repository.findByEmail(email);
        if (guest == null) {
            result.addErrorMessage("Guest could not be found.");
            return result;
        }

        result.setPayload(guest);
        return result;
    }

    @Override
    public Result<User> addUser(User guest) throws DataAccessException {
        Result<User> result = validation.validateUser(guest, "Guest");
        if (!result.isSuccess()) {
            return result;
        }

        result = validateGuest(guest);
        if (!result.isSuccess()) {
            return result;
        }

        if (guest.getId() != null) {
            result.addErrorMessage("Cannot set Id.");
            return result;
        }

        Guest existingGuest = repository.findByEmail(guest.getEmail());
        if (existingGuest != null) {
            result.addErrorMessage("User with this email already exists.");
            return result;
        }

        guest = repository.add(guest);
        if (guest == null) {
            result.addErrorMessage("Could not add guest.");
            return result;
        }

        result.setPayload(guest);
        return result;
    }

    @Override
    public Result<User> editUser(User guest) throws DataAccessException {
        Result<User> result = validation.validateUser(guest, "Guest");
        if (!result.isSuccess()) {
            return result;
        }

        result = validateGuest(guest);
        if (!result.isSuccess()) {
            return result;
        }

        boolean isSuccess = repository.update(guest);
        if (!isSuccess) {
            result.addErrorMessage("Could not edit guest.");
            return result;
        }

        result.setPayload(guest);
        return result;
    }

    @Override
    public Result<User> deleteUser(User guest) throws DataAccessException {
        Result<User> result = new Result<>();
        if (guest == null) {
            result.addErrorMessage("Host cannot be null.");
            return result;
        }

        if (guest.getEmail() == null || guest.getEmail().isBlank()) {
            result.addErrorMessage("Email cannot be null.");
            return result;
        }

        boolean isSuccess = repository.deleteByEmail(guest.getEmail());
        if (!isSuccess) {
            result.addErrorMessage("Could not delete host.");
        }

        return result;
    }

    private Result<User> validateGuest(User user) {
        Result<User> result = validation.validateFullName(user);
        if (!result.isSuccess()) {
            return result;
        }

        result = validation.validateFullName(user);

        return result;
    }
}
