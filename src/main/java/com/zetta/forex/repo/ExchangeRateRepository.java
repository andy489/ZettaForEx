package com.zetta.forex.repo;

import com.zetta.forex.model.entity.ExchangeRatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRatesEntity, Long> {

    default Optional<ExchangeRatesEntity> findSingleton() {
        return findById(1L);
    }

    default ExchangeRatesEntity saveSingleton(ExchangeRatesEntity exchangeRatesEntity) {
        exchangeRatesEntity.setId(1L);
        return save(exchangeRatesEntity);
    }
}
