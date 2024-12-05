package com.diego.springboot.msvc.products.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.diego.springboot.msvc.products.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diego.springboot.msvc.products.entities.Product;
import com.diego.springboot.msvc.products.services.ProductService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/producto")
public class ProductController {

    final private ProductService service;

    @Autowired
    private FileUploadService uploadService;

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

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo")MultipartFile archivo, @RequestParam("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Product> optionalProduct = service.findById(id);

        if(!archivo.isEmpty() && optionalProduct.isPresent()) {
            String nombreArchivo = null;
            Product producto = optionalProduct.get();

            try {
                nombreArchivo = uploadService.copiar(archivo);
            } catch (IOException e) {
                response.put("mensaje", "Ocurrió un error al subir la imágen");
                response.put("error", e.getMessage());
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String nombreFotoAnterior = producto.getImagenProducto();
            uploadService.eliminar(nombreFotoAnterior);
            producto.setImagenProducto(nombreArchivo);
            service.save(producto);

            response.put("producto", producto);
            response.put("mensaje", "Imagén subida correctamente a uploads/" + nombreArchivo);
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/upload/img/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {
        Resource recurso = null;

        try {
            recurso = uploadService.cargar(nombreFoto);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"");

        return new ResponseEntity<>(recurso, cabecera, HttpStatus.OK);
    }
}
