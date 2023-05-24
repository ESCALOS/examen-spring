package com.nanoka.examen.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nanoka.examen.models.UserEntity;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
