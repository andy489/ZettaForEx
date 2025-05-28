package com.zetta.forex.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "conversions")
@Accessors(chain = true)
@ToString(callSuper = true)
public class ConversionHistoryEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "TIMESTAMP")
    private Instant timestamp;

    @Column(nullable = false, length = 3)
    private String source;

    @Column(nullable = false, length = 3)
    private String target;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal result;
}
