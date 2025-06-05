package com.webframework2.work.controller;

import com.webframework2.work.domain.Product;
import com.webframework2.work.dto.ProductRequest;
import com.webframework2.work.service.ProductService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductContorller {
    
    private final ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("productRequest", new ProductRequest());
        return "product-form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createProduct(@Valid @ModelAttribute ProductRequest productRequest,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "product-form";
        }

        productService.createProduct(productRequest);
        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        ProductRequest request = new ProductRequest(product.getName(), product.getPrice());
        model.addAttribute("productRequest", request);
        model.addAttribute("productId", id);
        return "product-edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute ProductRequest productRequest,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productId", id);
            return "product-edit";
        }

        productService.updateProduct(id, productRequest);
        return "redirect:/products";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
