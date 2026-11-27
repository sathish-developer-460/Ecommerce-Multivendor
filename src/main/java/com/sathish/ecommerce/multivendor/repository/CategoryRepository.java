package com.sathish.ecommerce.multivendor.repository;

import com.sathish.ecommerce.multivendor.model;

import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(String categoryId);

}
