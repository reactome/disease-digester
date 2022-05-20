package org.reactome.server.repository.out;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.reactome.server.util.GeneToDiseasesSerializer;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = GeneToDiseasesSerializer.class)
public class GeneToDiseases {
    private String acc;
    @EqualsAndHashCode.Exclude
    private BigInteger count;
    @EqualsAndHashCode.Exclude
    private String rawJSONArrayOfDiseases;
}


