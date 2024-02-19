package org.example.springbootauthentication.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootauthentication.domain.User;
import org.example.springbootauthentication.dto.UserDTO;
import org.example.springbootauthentication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not Exist Email"));

        return modelMapper.map(user, UserDTO.class);
    }
}
