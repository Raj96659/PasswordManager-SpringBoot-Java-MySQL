package com.passwordmanager.repository;

import com.passwordmanager.entity.PasswordEntry;
import com.passwordmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PasswordEntryRepository
        extends JpaRepository<PasswordEntry, Long> {

    List<PasswordEntry> findByUser(User user);

    List<PasswordEntry> findByUserAndAccountNameContaining(
            User user,
            String accountName);

    List<PasswordEntry> findByUserAndFavoriteTrue(User user);

    List<PasswordEntry> findByUserAndCategory(
            User user,
            String category);


}


