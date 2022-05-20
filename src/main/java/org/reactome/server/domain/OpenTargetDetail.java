package org.reactome.server.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class OpenTargetDetail {
    private String target;
    private String studyId;
    private String variantId;
    private Float l2g;
}
