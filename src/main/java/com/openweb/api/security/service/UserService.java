package com.openweb.api.security.service;


import com.openweb.api.security.domain.Authority;
import com.openweb.api.security.domain.User;
import com.openweb.api.security.exception.AccountResourceException;
import com.openweb.api.security.repository.AuthorityRepository;
import com.openweb.api.security.repository.UserRepository;
import com.openweb.api.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Service class for managing users.
 */
@Service
@Transactional
@Slf4j
public class UserService {


    /**
     *
     */
    private final UserRepository userRepository;
    /**
     *
     */
    private final PasswordEncoder passwordEncoder;
    /**
     *
     */
    private final AuthorityRepository authorityRepository;


    /**
     * @param userRepository
     * @param passwordEncoder
     * @param authorityRepository
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    /**
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }


    /**
     * @return
     */
    @Transactional(readOnly = true)
    public User getUser() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin).orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * Gets a list of all the roles.
     *
     * @return a list of all the roles.
     */
    @Transactional(readOnly = true)
    public List<String> getRoles() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    /**
     * @param email
     * @return
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

}
