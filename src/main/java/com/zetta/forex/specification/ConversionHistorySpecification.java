package com.zetta.forex.specification;

import com.zetta.forex.model.criteria.ConversionHistoryCriteria;
import com.zetta.forex.model.entity.ConversionHistoryEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConversionHistorySpecification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionHistorySpecification.class);


    public Specification<ConversionHistoryEntity> getConversionHistorySpec(ConversionHistoryCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Convert input dates to UTC+2 for comparison with database
            if (criteria.getFromDate() != null) {
                LocalDateTime fromDateUtc2 = criteria.getFromDate();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("timestamp"),
                        fromDateUtc2
                ));
            }

            if (criteria.getToDate() != null) {
                LocalDateTime toDateUtc2 = criteria.getToDate();
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("timestamp"),
                        toDateUtc2
                ));
            }

            // Currency filters
            if (criteria.getSourceCurrency() != null && !criteria.getSourceCurrency().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("source"), criteria.getSourceCurrency().toUpperCase()));
            }
            if (criteria.getTargetCurrency() != null && !criteria.getTargetCurrency().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("target"), criteria.getTargetCurrency().toUpperCase()));
            }

            // Amount range filter
            if (criteria.getMinAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("amount"), criteria.getMinAmount()));
            }
            if (criteria.getMaxAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("amount"), criteria.getMaxAmount()));
            }

            // Result range filter
            if (criteria.getMinResult() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("result"), criteria.getMinResult()));
            }
            if (criteria.getMaxResult() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("result"), criteria.getMaxResult()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
