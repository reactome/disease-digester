package org.reactome.server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@Data
public class OpenTargetRow {
    private String diseaseId;
    private String diseaseLabel;
    private Integer targetCount;
    private List<String> targetList;
    private List<OpenTargetDetail> details;
}
