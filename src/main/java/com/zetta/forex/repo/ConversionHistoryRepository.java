package com.zetta.forex.repo;

import com.zetta.forex.model.entity.ConversionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversionHistoryRepository extends JpaRepository<ConversionHistoryEntity, Long> {
}
