package com.diego.springboot.msvc.products.respositories;

import org.springframework.data.repository.CrudRepository;

import com.diego.springboot.msvc.products.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

    
}
