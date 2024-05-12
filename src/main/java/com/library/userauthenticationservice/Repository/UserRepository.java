package com.library.userauthenticationservice.Repository;

import com.library.userauthenticationservice.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Override
    User save(User user);
}
