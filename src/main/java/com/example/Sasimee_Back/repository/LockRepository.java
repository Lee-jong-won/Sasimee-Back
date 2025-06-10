package com.example.Sasimee_Back.repository;

import com.example.Sasimee_Back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LockRepository extends JpaRepository<User, Long> {

    @Query(value = "select get_lock(:key, 300)", nativeQuery = true)
    Long getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);

}
