package io.github.raeperd.realworld.domain.user;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserFindService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public User signUp(UserSignUpRequest request) {
        final var encodedPassword = Password.of(request.getRawPassword(), passwordEncoder);
        return userRepository.save(User.of(request.getEmail(),
                request.getUserName(),
                encodedPassword));
    }

    @Transactional(readOnly = true)
    public Optional<User> login(Email email, String rawPassword) {
        var loginUser = userRepository.findFirstByEmail(email)
                .filter(user -> user.matchesPassword(rawPassword, passwordEncoder));
        loginUser.orElseThrow(NoSuchElementException::new);
        return loginUser;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(UserName userName) {
        return userRepository.findFirstByProfileUserName(userName);
    }

    @Transactional
    public User updateUser(long id, UserUpdateRequest request) {
        final var user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        request.getEmailToUpdate()
                .ifPresent(user::changeEmail);
        request.getUserNameToUpdate()
                .ifPresent(user::changeName);
        request.getPasswordToUpdate()
                .map(rawPassword -> Password.of(rawPassword, passwordEncoder))
                .ifPresent(user::changePassword);
        request.getImageToUpdate()
                .ifPresent(user::changeImage);
        request.getBioToUpdate()
                .ifPresent(user::changeBio);
        return userRepository.save(user);
    }

}
