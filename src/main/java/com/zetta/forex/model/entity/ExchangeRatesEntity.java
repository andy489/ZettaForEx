package com.zetta.forex.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "rates")
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class ExchangeRatesEntity implements Serializable {

    @Id
    private Long id = 1L; // Fixed ID

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String timestamp;

    @Column(nullable = false, unique = true)
    private String source;

    @Column(nullable = false, length = 4000)
    private String quotes;
}
