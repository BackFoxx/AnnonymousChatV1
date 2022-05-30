package toyproject.annonymouschat.User.service;

import toyproject.annonymouschat.User.dto.UserLoginDto;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.model.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.User.repository.UserRepositoryImpl;
import toyproject.annonymouschat.config.exception.DuplicateUserException;

import java.util.NoSuchElementException;

public class UserService {
    private UserRepository userRepository = new UserRepositoryImpl();

    public String registration(UserRegistrationDto dto) throws DuplicateUserException {
        User userByEmail = userRepository.findByUserEmail(dto.getUserEmail());
        if (userByEmail != null) {
            throw new DuplicateUserException("이미 사용자가 존재하는 이메일입니다.");
        }
        return userRepository.registration(dto);
    }

    public User login(UserLoginDto dto) {
        User findUser = userRepository.findByUserEmail(dto.getUserEmail());
        if (findUser.getPassword().equals(dto.getPassword())) {
            return findUser;
        }
        throw new NoSuchElementException("비밀번호가 틀립니다.");
    }
}
