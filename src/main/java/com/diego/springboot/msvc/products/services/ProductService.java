package com.diego.springboot.msvc.products.services;

import java.util.List;
import java.util.Optional;

import com.diego.springboot.msvc.products.entities.Product;

public interface ProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);
}