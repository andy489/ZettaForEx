package com.zetta.forex.aop;

import com.zetta.forex.config.MapStructMapper;
import com.zetta.forex.model.dto.ZettaConversionResponseDto;
import com.zetta.forex.model.entity.ConversionHistoryEntity;
import com.zetta.forex.repo.ConversionHistoryRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@Aspect
@Configuration
public class ConversionHistoryAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionHistoryAspect.class);

    private final MapStructMapper mapper;
    private final ConversionHistoryRepository conversionHistoryRepository;

    public ConversionHistoryAspect(MapStructMapper mapper, ConversionHistoryRepository conversionHistoryRepository) {
        this.mapper = mapper;
        this.conversionHistoryRepository = conversionHistoryRepository;
    }

    @Pointcut("execution(* com.zetta.forex.service.ZettaForexApiService.calcAmount(..))")
    public void onAllSampleComponentMethods() {
    }

    @AfterReturning(pointcut = "onAllSampleComponentMethods()", returning = "result")
    public void beforeEachMethod(JoinPoint joinPoint, ResponseEntity<ZettaConversionResponseDto> result) {

        Object[] args = joinPoint.getArgs();

        ZettaConversionResponseDto zettaConversionResponseDto = result.getBody();

        ConversionHistoryEntity conversionHistoryEntity = mapper.toEntity(zettaConversionResponseDto);
        conversionHistoryEntity.setAmount(new BigDecimal(args[0].toString()));

        conversionHistoryRepository.save(conversionHistoryEntity);

    }
}
