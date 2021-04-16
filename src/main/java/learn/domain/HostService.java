package learn.domain;

import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.HostRepository;

public class HostService implements UserService {

    private final HostRepository repository;
    private final Validation validation = new Validation();

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    @Override
    public Result<User> findByEmail(String email) throws DataAccessException {
        Result<User> result = validation.validateEmail(email);
        if (!result.isSuccess()) {
            return result;
        }

        User host = repository.findByEmail(email);
        if (host == null) {
            result.addErrorMessage("Host could not be found.");
            return result;
        }

        result.setPayload(host);
        return result;
    }

    @Override
    public Result<User> addUser(User host) throws DataAccessException {
        Result<User> result = validation.validateUser(host, "Host");
        if (!result.isSuccess()) {
            return result;
        }

        result = validateHost(host);
        if (!result.isSuccess()) {
            return result;
        }

        if (host.getId() != null) {
            result.addErrorMessage("Cannot set Id.");
            return result;
        }

        User existingHost = repository.findByEmail(host.getEmail());
        if (existingHost != null) {
            result.addErrorMessage("User with this email already exists.");
        }

        host = repository.add(host);
        if (host == null) {
            result.addErrorMessage("Could not add host.");
            return result;
        }

        result.setPayload(host);
        return result;
    }

    @Override
    public Result<User> editUser(User host) throws DataAccessException {
        Result<User> result = validation.validateUser(host, "Host");
        if (!result.isSuccess()) {
            return result;
        }

        result = validateHost(host);
        if (!result.isSuccess()) {
            return result;
        }

        boolean isSuccess = repository.update(host);
        if (!isSuccess) {
            result.addErrorMessage("Could not update host.");
            return result;
        }

        result.setPayload(host);
        return result;
    }

    @Override
    public Result<User> deleteUser(User host) throws DataAccessException {
        Result<User> result = new Result<>();
        if (host == null) {
            result.addErrorMessage("Host cannot be null.");
            return result;
        }

        if (host.getEmail() == null || host.getEmail().isBlank()) {
            result.addErrorMessage("Email cannot be null.");
            return result;
        }

        boolean isSuccess = repository.deleteByEmail(host.getEmail());
        if (!isSuccess) {
            result.addErrorMessage("Could not delete host.");
        }

        return result;
    }


    private Result<User> validateHost(User user) {
        Result<User> result = validation.validateFullAddress(user);
        if (!result.isSuccess()) {
            return result;
        }

        result = validation.validateRates(user);
        return result;
    }
}
