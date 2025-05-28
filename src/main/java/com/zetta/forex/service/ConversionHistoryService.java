package com.zetta.forex.service;

import com.zetta.forex.model.criteria.ConversionHistoryCriteria;
import com.zetta.forex.model.entity.ConversionHistoryEntity;
import com.zetta.forex.repo.ConversionHistoryRepository;
import com.zetta.forex.specification.ConversionHistorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversionHistoryService {

    private final ConversionHistoryRepository repository;
    private final ConversionHistorySpecification specification;

    public Page<ConversionHistoryEntity> getConversionHistory(ConversionHistoryCriteria criteria, Pageable pageable) {

        return repository.findAll(
                specification.getConversionHistorySpec(criteria),
                pageable
        );
    }
}
