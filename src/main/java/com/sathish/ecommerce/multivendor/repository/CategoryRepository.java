package com.sathish.ecommerce.multivendor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sathish.ecommerce.multivendor.modal.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(String categoryId);
}
