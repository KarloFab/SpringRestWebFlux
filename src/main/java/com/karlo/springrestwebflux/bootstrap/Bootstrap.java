package com.karlo.springrestwebflux.bootstrap;

import com.karlo.springrestwebflux.domain.Category;
import com.karlo.springrestwebflux.domain.Vendor;
import com.karlo.springrestwebflux.repository.CategoryRepository;
import com.karlo.springrestwebflux.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;

public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block() == 0){
            addCategories();
            addVendors();

        }
    }

    private void addVendors() {
        vendorRepository.save(Vendor.builder().firstName("Vendor").lastName("VendorLast").build());
        vendorRepository.save(Vendor.builder().firstName("Vendor1").lastName("VendorLast1").build());
        vendorRepository.save(Vendor.builder().firstName("Vendor2").lastName("VendorLast2").build());
    }

    private void addCategories() {
        categoryRepository.save(Category.builder().description("Fruits").build());
        categoryRepository.save(Category.builder().description("Breads").build());
        categoryRepository.save(Category.builder().description("Meats").build());
    }
}
