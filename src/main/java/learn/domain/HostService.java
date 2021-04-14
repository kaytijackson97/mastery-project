package learn.domain;

import learn.models.Host;
import learn.repository.DataAccessException;
import learn.repository.HostRepository;

public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public Result<Host> findByEmail(String email) throws DataAccessException {
        Result<Host> result = validateEmail(email);
        if (!result.isSuccess()) {
            return result;
        }

        Host host = repository.findByEmail(email);
        if (host == null) {
            result.addErrorMessage("Host could not be found.");
            return result;
        }

        result.setPayload(host);
        return result;
    }

    public Result<Host> validateEmail(String email) throws DataAccessException {
        Result<Host> result = new Result<>();
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
