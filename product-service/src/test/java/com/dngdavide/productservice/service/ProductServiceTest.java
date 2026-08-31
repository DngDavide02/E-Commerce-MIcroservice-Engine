package com.dngdavide.productservice.service;

import com.dngdavide.productservice.dto.ProductRequest;
import com.dngdavide.productservice.dto.ProductResponse;
import com.dngdavide.productservice.entity.Product;
import com.dngdavide.productservice.exception.ProductNotFoundException;
import com.dngdavide.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createSavesAndReturnsProduct() {
        ProductRequest request = new ProductRequest("Keyboard", "Mechanical keyboard", BigDecimal.valueOf(59.99), "Electronics");
        Product saved = new Product(request.name(), request.description(), request.price(), request.category());
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.create(request);

        assertThat(response.name()).isEqualTo("Keyboard");
        assertThat(response.price()).isEqualByComparingTo("59.99");
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(productRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(42L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteRemovesExistingProduct() {
        Product product = new Product("Mouse", "Wireless mouse", BigDecimal.TEN, "Electronics");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository).delete(product);
    }
}
