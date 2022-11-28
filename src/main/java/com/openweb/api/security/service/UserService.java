package com.openweb.api.security.service;


import com.openweb.api.security.domain.User;
import com.openweb.api.security.exception.AccountResourceException;
import com.openweb.api.security.repository.AuthorityRepository;
import com.openweb.api.security.repository.UserRepository;
import com.openweb.api.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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

}
