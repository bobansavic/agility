package com.bobansavic.agility.clojure;

import com.bobansavic.agility.model.Project;
import com.bobansavic.agility.model.User;
import com.bobansavic.agility.model.UserCljDto;
import com.bobansavic.agility.model.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

@Service
public class ClojureDataAccessService {
    private static final Logger log = LoggerFactory.getLogger(ClojureDataAccessService.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void postConstruct() {
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public User login(String email, String password) throws URISyntaxException, IOException {
        String basicUrl = "http://localhost:3000/users/login";
//        URI uri = new URI(basicUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basicUrl)
                .queryParam("authToken", "agility")
                .queryParam("email", email)
                .queryParam("password", password);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        UserCljDto dto = objectMapper.readValue(result.getBody(), UserCljDto.class);
        User user = cljDtoToUser(dto);
        return user;
    }

    public UserRole findRole(Long roleId) throws IOException {
        String basicUrl = "http://localhost:3000/roles";
//        URI uri = new URI(basicUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basicUrl)
                .queryParam("authToken", "agility")
                .queryParam("role_id", roleId);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        UserRole role = objectMapper.readValue(result.getBody(), UserRole.class);
        System.out.println("Result: " + result.getBody());
        return role;
    }

    public Set<Project> getAllProjects() throws JsonProcessingException {
        String basicUrl = "http://localhost:3000/projects/get";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basicUrl)
                .queryParam("authToken", "agility");
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        Set<Project> projects = objectMapper.readValue(result.getBody(), new TypeReference<Set<Project>>() {});
        System.out.println("Result: " + result.getBody());
        return projects;
    }

    public void createProject(String title) {
        String basicUrl = "http://localhost:3000/projects/create";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basicUrl)
                .queryParam("authToken", "agility")
                .queryParam("title", title);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        System.out.println("Result status: " + result.getStatusCodeValue());
    }

    public boolean checkIfProjectExists(String title) {
        String basicUrl = "http://localhost:3000/projects/check";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basicUrl)
                .queryParam("authToken", "agility")
                .queryParam("title", title);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        System.out.println("Result status: " + result.getStatusCodeValue());
        return result.getStatusCodeValue() != 201;
    }

    public void deleteProject(String title) {
        String basicUrl = "http://localhost:3000/projects/delete";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basicUrl)
                .queryParam("authToken", "agility")
                .queryParam("title", title);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, requestEntity, String.class);
        log.info("Successfully deleted project: {}", title);
        System.out.println("Result status: " + result.getStatusCodeValue());
    }

    public Set<Project> findProjectsByEmailOrUsername(String value) throws JsonProcessingException {
        String basicUrl = "http://localhost:3000/projects/get";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(basicUrl)
                .queryParam("authToken", "agility")
                .queryParam("email", value);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
        Set<Project> projects = objectMapper.readValue(result.getBody(), new TypeReference<Set<Project>>() {});
        System.out.println("Result: " + result.getBody());
        return projects;
    }

    private User cljDtoToUser (UserCljDto dto) throws IOException {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(findRole(dto.getRole_id()));
        return user;
    }

    public boolean isClojureServiceAvailable() {
        try {
            String pingUrl = "http://localhost:3000";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(pingUrl);
            HttpEntity<?> requestEntity = new HttpEntity<>(null);
            ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class);
            return result.getStatusCodeValue() == 200;
        } catch (Exception e) {
            log.warn("Clojure Data Access Module web service is unavailable! Defaulting to Hibernate...");
            return false;
        }
    }
}
