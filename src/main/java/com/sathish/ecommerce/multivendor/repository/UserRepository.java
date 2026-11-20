package com.sathish.ecommerce.multivendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sathish.ecommerce.multivendor.modal.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
