package org.reactome.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactome.server.config.DiseaseDigesterJpaConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {DiseaseDigesterJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class TestBase {
    @Test
    public void noTest() {

    }
}
