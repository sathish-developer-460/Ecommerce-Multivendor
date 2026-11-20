package com.sathish.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sathish.ecommerce.modal.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
