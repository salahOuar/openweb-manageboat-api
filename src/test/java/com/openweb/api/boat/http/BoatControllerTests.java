package com.openweb.api.boat.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openweb.api.BoatApplication;
import com.openweb.api.TestUtils;
import com.openweb.api.boat.domain.Boat;
import com.openweb.api.boat.dto.BoatDTO;
import com.openweb.api.boat.repository.BoatRepository;
import com.openweb.api.boat.service.BoatService;
import com.openweb.api.security.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BoatApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class BoatControllerTests {


    private final String username = "admin@openweb.com";
    private final String value = "admin@openweb.com";

    private final String boatName = "Marité";

    private final String boatDescription = "Marité, le souvenir de l’épopée morutière";


    User connectedUser;
    Boat expectedBoat;
    BoatDTO expectedBoatDTO;
    @Autowired
    BoatRepository boatRepository;
    @Autowired
    private BoatService boatService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        connectedUser = new User();
        connectedUser.setId(Long.valueOf(3));
        expectedBoatDTO = BoatDTO.builder().name("L'Hermione").description("L'Hermione, mythique frégate du 18e siècle de La Fayette").build();
        expectedBoat = boatService.save(expectedBoatDTO);
        expectedBoat.setUser(connectedUser);
        boatRepository.save(expectedBoat);
    }

    @After
    public void clear() {
        boatRepository.deleteAll();
    }

    @WithMockUser(value = value, username = username)
    @Test
    public void givenFindAllUserBoats() throws Exception {
        MvcResult result = mvc.perform(get("/api/boats").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<BoatDTO> boats = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(boats.size()).isEqualTo(1);
        assertThat(boats.get(0).getName()).isEqualTo(expectedBoatDTO.getName());
        assertThat(boats.get(0).getDescription()).isEqualTo(expectedBoatDTO.getDescription());
    }


    @WithMockUser(value = value, username = username)
    @Test
    public void postAddNewBoat() throws Exception {
        BoatDTO newBoat = BoatDTO.builder().name(boatName).description(boatDescription).build();

        MvcResult result = mvc.perform(post("/api/boats").contentType(MediaType.APPLICATION_JSON).content(TestUtils.convertObjectToJsonBytes(newBoat))).andExpect(status().isCreated()).andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        BoatDTO boat = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(boat.getName()).isEqualTo(newBoat.getName());
        assertThat(boat.getDescription()).isEqualTo(newBoat.getDescription());

        result = mvc.perform(get("/api/boats").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<BoatDTO> boats = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(boats.size()).isEqualTo(2);
        assertThat(boats.get(0).getName()).isEqualTo(expectedBoatDTO.getName());
        assertThat(boats.get(0).getDescription()).isEqualTo(expectedBoatDTO.getDescription());

        assertThat(boats.get(1).getName()).isEqualTo(newBoat.getName());
        assertThat(boats.get(1).getDescription()).isEqualTo(newBoat.getDescription());
    }

    @WithMockUser(value = value, username = username)
    @Test
    public void deleteUserBoat() throws Exception {

        mvc.perform(delete("/api/boats/" + expectedBoat.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        MvcResult result = mvc.perform(get("/api/boats").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<BoatDTO> boats = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(boats.size()).isEqualTo(0);
    }


    @WithMockUser(value = value, username = username)
    @Test
    public void putUpdateBoat() throws Exception {
        BoatDTO updateBoatDTO = BoatDTO.builder().name(boatName).description(boatDescription).build();

        MvcResult result = mvc.perform(put("/api/boats/" + expectedBoat.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtils.convertObjectToJsonBytes(updateBoatDTO))).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        BoatDTO boat = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(boat.getName()).isEqualTo(updateBoatDTO.getName());
        assertThat(boat.getDescription()).isEqualTo(updateBoatDTO.getDescription());
        assertThat(boat.getId()).isEqualTo(expectedBoat.getId());

        result = mvc.perform(get("/api/boats").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<BoatDTO> boats = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(boats.size()).isEqualTo(1);
        assertThat(boats.get(0).getName()).isEqualTo(updateBoatDTO.getName());
        assertThat(boats.get(0).getDescription()).isEqualTo(updateBoatDTO.getDescription());

    }

    @WithMockUser(value = value, username = username)
    @Test
    public void givenFindUserBoatById() throws Exception {
        MvcResult result = mvc.perform(get("/api/boats/" + expectedBoat.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        BoatDTO boat = objectMapper.readValue(content, new TypeReference<>() {
        });
        assertThat(boat.getName()).isEqualTo(expectedBoat.getName());
        assertThat(boat.getDescription()).isEqualTo(expectedBoat.getDescription());
        assertThat(boat.getId()).isEqualTo(expectedBoat.getId());
    }

    @WithMockUser(value = value, username = username)
    @Test
    public void givenFindUserBoatByIdCaseBoatNotFoundThenReturn404() throws Exception {
        MvcResult result = mvc.perform(get("/api/boats/" + Long.valueOf(18)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError()).andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(HttpStatus.NOT_FOUND.value()).isEqualTo(result.getResponse().getStatus());
        assertThat(content).isEqualTo("Boat with id 18 was not found");
    }

    @WithMockUser(value = "user@openweb.com", username = "user@openweb.com")
    @Test
    public void deleteUserBoatCaseUnauthorizedAction() throws Exception {

        MvcResult result = mvc.perform(delete("/api/boats/" + expectedBoat.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError()).andReturn();
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(HttpStatus.UNAUTHORIZED.value()).isEqualTo(result.getResponse().getStatus());
        assertThat(content).isEqualTo("Unauthorized action");
    }


}
