package org.reactome.server.repository.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneToDiseases {
    private String acc;
    private Integer count;
    private List<Interactor> interactors;
}


