package com.diego.springboot.msvc.products.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diego.springboot.msvc.products.entities.Product;
import com.diego.springboot.msvc.products.respositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

    final private ProductRepository repository;
    final private Environment environment;

    public ProductServiceImpl(ProductRepository repository, Environment environment){
        this.repository = repository;
        this.environment = environment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return ((List<Product>) repository.findAll());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }
    

    @Override
    @Transactional
    public Product save(Product product) {
        return repository.save(product);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Product update(Long id, Product productDetails) {
        return repository.findById(id).map(product -> {
            product.setNombreProducto(productDetails.getNombreProducto());
            product.setPrecioProducto(productDetails.getPrecioProducto());
            product.setCategoriaProducto(productDetails.getCategoriaProducto());
            product.setImagenProducto(productDetails.getImagenProducto()); // Establece el valor correctamente
            // Aquí puedes agregar otros campos que desees actualizar
            return repository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
    
}
