package org.reactome.server.repository.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interactor {
    private String acc;
    private String alias;
    private Float score;
    private String accURL;
}
