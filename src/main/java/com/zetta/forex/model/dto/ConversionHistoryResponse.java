package com.zetta.forex.model.dto;

import com.zetta.forex.model.entity.ConversionHistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistoryResponse {
    private List<ConversionHistoryEntity> content;
    private PageMetadata metadata;

    // Static factory method to create response from Page
    public static ConversionHistoryResponse fromPage(Page<ConversionHistoryEntity> page) {
        return new ConversionHistoryResponse(
                page.getContent(),
                new PageMetadata(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()
                )
        );
    }

    // Nested class for pagination metadata
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageMetadata {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }
}
