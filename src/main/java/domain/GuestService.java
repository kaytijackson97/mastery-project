package domain;

import models.User;

public class GuestService implements UserService{
    @Override
    public User findByEmail(String email) {
        return null;
    }
}
