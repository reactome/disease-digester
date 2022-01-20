package org.reactome.server.repository.out;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactome.server.util.GeneToDiseasesSerializer;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = GeneToDiseasesSerializer.class)
public class GeneToDiseases {
    private String acc;
    private BigInteger count;
    private String rawJSONArrayOfDiseases;
}


