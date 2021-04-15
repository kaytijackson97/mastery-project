package learn.domain;

import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.HostRepository;

public class HostService implements UserService{

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    @Override
    public Result<User> findByEmail(String email) throws DataAccessException {
        Result<User> result = validateEmail(email);
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
