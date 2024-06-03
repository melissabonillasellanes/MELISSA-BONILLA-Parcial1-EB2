package com.elaparato.repository;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.catalina.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final Keycloak keycloakClient;

    @Value("${elaparato.keycloak.realm}")
    private String realm;

    @Override
    public List<User> findAll() {
        return keycloakClient.realm(realm).users().list().stream()
                .map(this::toUser).collect(Collectors.toList());
    }

    private User toUser(UserRepresentation userRepresentation) {
        return User.builder()
                .id(userRepresentation.getId())
                .userName(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .build();
    }

    @Override
    public List<User> findByUserName(String userName) {
        List<UserRepresentation> userRepresentation = keycloakClient
                .realm(realm)
                .users()
                .search(userName);
        return userRepresentation.stream().map(this::toUser)
                .collect(Collectors.toList());

    }

    private User fromUserRepresentation(UserRepresentation userRepresentation) {
        return new User(userRepresentation.getId(), userRepresentation.getUsername()
                , userRepresentation.getEmail(), userRepresentation.getFirstName(), userRepresentation.getLastName());
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public User deleteUserById(String id) {
        return null;
    }
}
