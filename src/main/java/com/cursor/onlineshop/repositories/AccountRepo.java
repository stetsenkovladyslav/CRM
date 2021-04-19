package com.cursor.onlineshop.repositories;

import com.cursor.onlineshop.entities.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);
}
