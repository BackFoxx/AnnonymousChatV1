package toyproject.annonymouschat.User.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.annonymouschat.User.dto.UserLoginDto;
import toyproject.annonymouschat.User.dto.UserRegistrationDto;
import toyproject.annonymouschat.User.entity.User;
import toyproject.annonymouschat.User.repository.UserRepository;
import toyproject.annonymouschat.config.exception.DuplicateUserException;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String registration(UserRegistrationDto dto) throws DuplicateUserException {
        User userByEmail = userRepository.findByUserEmail(dto.getUserEmail());
        if (userByEmail != null) {
            throw new DuplicateUserException("이미 사용자가 존재하는 이메일입니다.");
        }

        User user = new User(null, dto.getUserEmail(), dto.getPassword());
        return userRepository.registration(user);
    }

    public User login(UserLoginDto dto) {
        User findUser = userRepository.findByUserEmail(dto.getUserEmail());
        if (findUser == null) {
            throw new NoSuchElementException("존재하지 않는 사용자입니다.");
        }
        if (findUser.getPassword().equals(dto.getPassword())) {
            return findUser;
        }
        throw new NoSuchElementException("비밀번호가 틀립니다.");
    }
}
