package com.spring.chatserver.service.ServiceImpl;

import com.spring.chatserver.dto.UserDto;
import com.spring.chatserver.exception.ResourceNotFoundException;
import com.spring.chatserver.model.User;
import com.spring.chatserver.repository.UserRepository;
import com.spring.chatserver.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;



    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with username: ", username, username)
                );
    }



    @Override
    public UserDto updateUserDetails(UserDto userDto) {
        User savedUser = userRepository.findById(String.valueOf(userDto.getUserId()))
                .map(user -> User.builder()
                        .name(userDto.getName())
                        .build())
                .map(userRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User details", userDto.getUserId()));

        return UserDto.builder()
                .userId(savedUser.getId())
                .userName(savedUser.getUsername())
                .name(savedUser.getName())
                .build();

    }

    @Override
    public UserDto getUserDetails(String id) {
        return userRepository.findById(id)
                .map(user -> UserDto.builder()
                        .userName(user.getUsername())
                        .userId(user.getId())
                        .name(user.getName())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("User", "User details", id));
    }

    @Override
    public List<UserDto> findUsersByNameOrName(String name) {
        List<User> users = userRepository.findUsersByNameOrName(name);
        System.out.println("Search String: " + name);
        System.out.println("Users found: " + users.size());

        return users.stream().map(user -> UserDto.builder()
                .userName(user.getUsername())
                .userId(user.getId())
                .name(user.getName())
                .build()).toList();
    }
}
