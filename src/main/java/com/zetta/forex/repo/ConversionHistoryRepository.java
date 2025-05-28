package com.zetta.forex.repo;

import com.zetta.forex.model.entity.ConversionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConversionHistoryRepository extends JpaRepository<ConversionHistoryEntity, Long>,
        JpaSpecificationExecutor<ConversionHistoryEntity> {
}
