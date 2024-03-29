package com.karlo.springrestwebflux.controller;

import com.karlo.springrestwebflux.domain.Category;
import com.karlo.springrestwebflux.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    Flux<Category> getAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("{id}")
    Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<Void> create(@RequestBody Publisher<Category> categoryPublisher) {
        return categoryRepository.saveAll(categoryPublisher).then();
    }

    @PutMapping("{id}")
    Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("{id}")
    Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
        Category foundCategory = categoryRepository.findById(id).block();

        if (!foundCategory.getDescription().equals(category.getDescription())) {
            foundCategory.setDescription(category.getDescription());
            return categoryRepository.save(foundCategory);
        }
        return Mono.just(foundCategory);
    }
}
