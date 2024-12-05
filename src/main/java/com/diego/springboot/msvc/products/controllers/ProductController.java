package com.diego.springboot.msvc.products.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diego.springboot.msvc.products.entities.Product;
import com.diego.springboot.msvc.products.services.ProductService;

@RestController
@RequestMapping("/producto")
public class ProductController {

    final private ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> details(@PathVariable Long id) {
        Optional<Product> productOptional = service.findById(id);
        return productOptional.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombreProducto}")
    public ResponseEntity<List<Product>> detailsByName(@PathVariable String nombreProducto) {
        List<Product> products = service.findByName(nombreProducto);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si no se encuentran productos
        }
        return ResponseEntity.ok(products); // 200 con la lista de productos
    }
    
    @GetMapping("/categoria/{categoriaProducto}")
    public ResponseEntity<List<Product>> detailsByCategory(@PathVariable String categoriaProducto) {
        List<Product> products = service.findByCategory(categoriaProducto);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si no se encuentran productos
        }
        return ResponseEntity.ok(products); // 200 con la lista de productos
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product savedProduct = service.save(product);
        return ResponseEntity.created(URI.create("/producto/" + savedProduct.getIdProducto())).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = service.update(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
