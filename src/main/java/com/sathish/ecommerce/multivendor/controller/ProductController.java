package com.sathish.ecommerce.multivendor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sathish.ecommerce.multivendor.exceptions.ProductException;
import com.sathish.ecommerce.multivendor.modal.Product;
import com.sathish.ecommerce.multivendor.service.ProductService;
import com.sathish.ecommerce.multivendor.service.SellerService;
import com.sathish.ecommerce.multivendor.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private shopService shopService;
    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final SellerService sellerService;

    @Autowired 
    ProductController(ProductService productService, UserService userService, SellerService sellerService, CategoryService){
        this.productService = productService;
        this.userService = userService;
        this.sellerService = sellerService;
        this.categoryService = categoryService;
    }

    // Retrive data from database so we need to annotate with @GetMapping()
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) throws ProductException {

        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);

    }

@GetMapping("/search")
//localhost:8080/search?query=shirt
//here query= is the RequestParam
public ResponseEntity<List<Product> searchProduct(@RequestParam(required = false) String query){


List<Product> products =productService.searchProducts(query);
return new ResponseEntity<>(products, HttpStatus.OK);
}

@GetMapping 
public ResponseEntity<Page<Product>> getAllProducts(
@RequestParam(required =  = false) String category,
@RequestParam (required =  = false) String brand,
@RequestParam (required =  = false) String color, 
@RequestParam (required =  = false) String size, 
@RequestParam (required =  = false) Integer minPrice,
@RequestParam (required =  = false) Integer maxPrice, 
@RequestParam (required =  = false) Integer minDiscount, 
@RequestParam (required =  = false) String sort, 
@RequestParam (required = false) String stock, 
@RequestParam (defaultValue =  "0") Intteger pageNumber){
    return new ResponseEntity<>(
        productService.getAllProducts(category, brand, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber), HttpStatus.OK);
    }
}
