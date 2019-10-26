package com.karlo.springrestwebflux.controller;

import com.karlo.springrestwebflux.domain.Category;
import com.karlo.springrestwebflux.domain.Vendor;
import com.karlo.springrestwebflux.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    Flux<Vendor> getAll(){
        return vendorRepository.findAll();
    }

    @GetMapping("{id}")
    Mono<Vendor> getById(@PathVariable String id){
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher){
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @PutMapping("{id}")
    Mono<Vendor> update(@PathVariable  String id,@RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);

    }
}
