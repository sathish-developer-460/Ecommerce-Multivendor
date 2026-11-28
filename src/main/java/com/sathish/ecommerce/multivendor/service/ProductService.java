package com.sathish.ecommerce.multivendor.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.sathish.ecommerce.multivendor.exceptions.ProductException;
import com.sathish.ecommerce.multivendor.modal.Product;
import com.sathish.ecommerce.multivendor.modal.Seller;
import com.sathish.ecommerce.multivendor.request.CreateProductRequest;

public interface ProductService {
    public Product CreateProduct(CreateProductRequest request, Seller seller) throws ProductException;

    public void deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product product) throws ProductException;

    public Product findProductById(Long productId);

    List<Product> searchProducts();

    public Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber);

    List<Product> getProductBySellerId(Long sellerId);
}
