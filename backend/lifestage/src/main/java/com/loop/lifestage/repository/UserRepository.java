package com.loop.lifestage.repository;

import com.loop.lifestage.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {}
