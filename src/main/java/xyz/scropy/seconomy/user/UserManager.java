package xyz.scropy.seconomy.user;

import xyz.scropy.seconomy.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserManager {

    private final Repository<User, Long> repository;

    public UserManager(Repository<User, Long> repository) {
        this.repository = repository;
    }

    public User getUser(UUID uniqueId) {
        Optional<User> userOptional  = repository.getEntry(new User(uniqueId));
        if(userOptional.isEmpty()) {
            User user = new User(uniqueId);
            repository.addEntry(user);
            return user;
        }
        return userOptional.get();
    }

    public void createUserIfAbsent(UUID uniqueId) {
        Optional<User> userOptional  = repository.getEntry(new User(uniqueId));
        if(userOptional.isPresent()) return;
        User user = new User(uniqueId);
        repository.addEntry(user);
    }
}
