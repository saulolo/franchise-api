package com.accenture.franchise.application.config;

import com.accenture.franchise.domain.gateway.BranchRepositoryGateway;
import com.accenture.franchise.domain.gateway.ProductRepositoryGateway;
import com.accenture.franchise.domain.usecase.ProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProductUseCase productUseCase(
            ProductRepositoryGateway productRepositoryGateway,
            BranchRepositoryGateway branchRepositoryGateway) {
        return new ProductUseCase(productRepositoryGateway, branchRepositoryGateway);
    }
}
