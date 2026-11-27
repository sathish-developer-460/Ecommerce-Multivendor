package com.sathish.ecommerce.multivendor.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sathish.ecommerce.multivendor.exceptions.ProductException;
import com.sathish.ecommerce.multivendor.modal.Category;
import com.sathish.ecommerce.multivendor.modal.Product;
import com.sathish.ecommerce.multivendor.modal.Seller;
import com.sathish.ecommerce.multivendor.repository.CategoryRepository;
import com.sathish.ecommerce.multivendor.repository.ProductRepository;
import com.sathish.ecommerce.multivendor.request.CreateProductRequest;
import com.sathish.ecommerce.multivendor.service.ProductService;

@Service
@RequestArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest request, Seller seller) {
        Category category1 = categoryRepository.findCategoryId(request.getCategory());
        if (category1 == null) {
            Category category = new Category();
            category.setCategory(request.getCategory());
            category.setLevel(1);
            category1 = categoryRepository.save(category);
        }
        Category category2 = categoryRepository.findByCategoryId(request.getCategory2());
        if(category2 == null){
            Category category= new Category;
            category.setCategoryId(request.getCategory2());
            category.setLevel(2);
            category.setParentCategory(category1);
            category2 = categoryRepository.save(category);
        }
        Category category3= categoryRepository.findByCategoryId(request.getCategory3());
        if(category3= null){
            Category category= new Category();
            category.setCategoryId(request.getCategory3());
            category.setLevel(3);
            category.setParenetCategory(category);
        }
        int discountPercentage = calculateDiscountPercentage(request.getMrpPrice(), request.getSellingPrice());
        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(request.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setSellingPrice(request.getSellingPrice());
        product.setImages(request.getImages());
        product.setMrpPrice(request.getMrpPrice());
        product.setSizes(request.getSizes());
        product.setDiscountPercent(discountPercentage);
        return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice < 0) {
            throw new IllegalArgumentException("Actual price must be greater than 0");
        }
        double discount = myprice - sellingPrice;
        // convert this discount into percentage
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("product not found with id " + productId));
    }

    @Override
    public List<Product> searchProducts() {
        return List.of();
    }

    @Override 
    public Page<Product> getAllProduct(String category, String brand, String colors, String sizes, Integer minPrice, Integer maxPrice){
        Specification<Product> spec=(root,query, criteriaBuilder)->{
            List<Predicate>Predicates = new ArrayList<>();
            if(category != null){
                Join<Product, Category>categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if(colors != null && !colors.isEmpty()){
                System.out.println("color "+ color);
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            //Filter by size (single value)
            if(sizes != null && !sizes.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));

            }
            if(minPrice != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice))
            }
            if(maxPrice != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }
            //min discount 20 % 
            if(minDiscount != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minDiscount));
            }
            if(stock != null){
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
        
        return null;
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {

    }
}
