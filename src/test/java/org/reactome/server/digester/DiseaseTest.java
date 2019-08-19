package org.reactome.server.digester;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactome.server.config.DiseaseDigesterJpaConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Guilherme S Viteri <gviteri@ebi.ac.uk>
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DiseaseDigesterJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class DiseaseTest {

//    @Autowired
//    public DiseaseRepository diseaseRepository;

    @Test
    public void test() {
    }
}
