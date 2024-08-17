package com.spring.chatserver.service;

import com.spring.chatserver.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    //    @Override
    //    @Transactional
    //    public UserDetails loadUserByUsername(String username)
    //            throws UsernameNotFoundException {
    //        // Let people login with either username or email
    //        return userRepository.findByUsername(username)
    //                .orElseThrow(() ->
    //                        new ResourceNotFoundException("User not found with username: ", username, username)
    //                );
    //    }
        //    @Transactional
    UserDetails loadUserByUsername(String username)
                throws UsernameNotFoundException;

    UserDto updateUserDetails(UserDto userDto);

    UserDto getUserDetails(String id);

    List<UserDto> findUsersByNameOrName(String name);
}
