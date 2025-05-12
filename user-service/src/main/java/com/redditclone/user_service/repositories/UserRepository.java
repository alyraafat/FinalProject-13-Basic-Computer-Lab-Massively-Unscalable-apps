package com.redditclone.user_service.repositories;


import com.redditclone.user_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) AND u.id NOT IN :excludedIds")
    List<User> searchByUsername(String keyword, List<UUID> excludedIds);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) AND u.id NOT IN :excludedIds")
    List<User> searchByEmail(String keyword, List<UUID> excludedIds);


    Optional<User> findByUsername(String userName);
    Optional<User> findByEmail(String email);

    List<User> findByActivatedTrue();

    @Query("SELECT u.email FROM User u WHERE u.id IN :ids")
    List<String> findEmailsByIds(@Param("ids") List<UUID> ids);

}
