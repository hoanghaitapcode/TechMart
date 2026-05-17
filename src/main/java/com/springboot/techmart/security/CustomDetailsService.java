package com.springboot.techmart.security;

import com.springboot.techmart.entity.User;
import com.springboot.techmart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        //lay user tu db
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm tấy user: " + username));

        //dung nguyen alias boi vi xung dot voi entity user
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public UserDetails loadUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm tấy user: " + id));

        //dung nguyen alias boi vi xung dot voi entity user
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId().toString()) // Lưu ID vào biến username để SecurityUtils lấy ra được
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }


}
