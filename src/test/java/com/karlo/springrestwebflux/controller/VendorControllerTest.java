package com.karlo.springrestwebflux.controller;

import com.karlo.springrestwebflux.domain.Vendor;
import com.karlo.springrestwebflux.repository.VendorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = VendorController.class)
@Import(VendorRepository.class)
class VendorControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    VendorRepository vendorRepository;

    @Test
    void getAll() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Vendor1").lastName("VendorLast1").build(),
                        Vendor.builder().firstName("Vendor2").lastName("VendorLast2").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        BDDMockito.given(vendorRepository.findById("id"))
                .willReturn(Mono.just(Vendor.builder().firstName("Vendor1").lastName("VendorLast1").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/id")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(1);
    }
}