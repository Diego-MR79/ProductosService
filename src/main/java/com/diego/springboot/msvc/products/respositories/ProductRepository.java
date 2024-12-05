package com.diego.springboot.msvc.products.respositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.diego.springboot.msvc.products.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.nombreProducto LIKE %:nombreProducto%")
    List<Product> findByNombreProducto(String nombreProducto);

    @Query("SELECT p FROM Product p WHERE p.categoriaProducto LIKE %:categoriaProducto%")
    List<Product> findByCategoriaProducto(String categoriaProducto);
}
