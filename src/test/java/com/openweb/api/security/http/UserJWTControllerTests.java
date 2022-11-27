package com.openweb.api.security.http;


import com.openweb.api.BoatApplication;
import com.openweb.api.TestUtils;
import com.openweb.api.boat.handler.ErrorHandler;
import com.openweb.api.security.domain.User;
import com.openweb.api.security.dto.LoginDTO;
import com.openweb.api.security.jwt.TokenProvider;
import com.openweb.api.security.repository.UserRepository;
import com.openweb.api.security.service.UserService;
import com.openweb.api.security.util.CookieUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserJWTController REST controller.
 *
 * @see UserJWTController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BoatApplication.class)
public class UserJWTControllerTests {

    @Autowired
    ErrorHandler errorHandler;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CookieUtil cookieUtil;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        UserJWTController userJWTController = new UserJWTController(tokenProvider, userService, authenticationManagerBuilder, cookieUtil);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userJWTController).setControllerAdvice(errorHandler)
                .build();
    }

    @Test
    public void testAuthorize() throws Exception {

        User user = new User();
        user.setLogin("user-jwt-controller");
        user.setEmail("user-jwt-controller@example.com");
        user.setPassword(passwordEncoder.encode("test"));

        userRepository.saveAndFlush(user);

        LoginDTO login = new LoginDTO();
        login.setUsername("user-jwt-controller");
        login.setPassword("test");
        mockMvc.perform(post("/api/authenticate")
                        .contentType(TestUtils.APPLICATION_JSON_UTF8)
                        .content(TestUtils.convertObjectToJsonBytes(login)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, not(nullValue())))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, not(isEmptyString())));
    }


    @Test
    public void testAuthorizeFails() throws Exception {
        LoginDTO login = new LoginDTO();
        login.setUsername("wrong-user");
        login.setPassword("wrong password");

        mockMvc.perform(post("/api/authenticate")
                        .contentType(TestUtils.APPLICATION_JSON_UTF8)
                        .content(TestUtils.convertObjectToJsonBytes(login)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }
}
