package com.karlo.springrestwebflux.controller;

import com.karlo.springrestwebflux.domain.Category;
import com.karlo.springrestwebflux.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CategoryController.class)
@Import(CategoryRepository.class)
class CategoryControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    CategoryRepository categoryRepository;

    @Test
    void getAll() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));

        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryRepository.findById("id"))
                .willReturn(Mono.just(Category.builder().description("Cat").build()));

        webTestClient.get()
                .uri("/api/v1/categories/id")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(1);
    }

    @Test
    void create() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description("Category").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(categoryToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void update() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().description("Category").build());

        webTestClient.put()
                .uri("/api/v1/categories/someid")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patch() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("Bla").build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri("/api/v1/categories/bla")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }
}