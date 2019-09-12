package org.reactome.server.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactome.server.config.DiseaseDigesterJpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = {DiseaseDigesterJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)
public class DiseaseItemRepositoryTest {
    @Autowired
    private DiseaseItemRepository diseaseItemRepository;
    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() throws IOException {

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("digester.sql");
        assert inputStream != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        for (String line; (line = bufferedReader.readLine()) != null; ) {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery(line).executeUpdate();
            entityManager.getTransaction().commit();
        }
    }

    @After
    public void cleanUp() {
        diseaseItemRepository.deleteAll();
    }

    @Test
    public void findAll() {
//        Pageable pageable01 = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "diseaseName"));
////        diseaseItemRepository.findAllByDiseaseClassContaining("a", pageable01).getContent().forEach(d -> System.out.println(d.getDiseaseClass() + ":" + d.getDiseaseName()));
//        Pageable pageable02 = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "diseaseName"));
////        diseaseItemRepository.findAllByDiseaseClassContaining("a", pageable02).getContent().forEach(d -> System.out.println(d.getDiseaseClass() + ":" + d.getDiseaseName()));
////        diseaseItemRepository.findAllByDiseaseNameContaining("ab", pageable02).getContent().forEach(d -> System.out.println(d.getDiseaseClass() + ":" + d.getDiseaseName()));
////        System.out.println(diseaseItemRepository.findAllOrOrderByDiseaseName(pageable));
////        diseaseItemRepository.findAllByDiseaseClassOrderByGeneItems("a").forEach(d -> System.out.println(d.getGeneItems().size()));
//        Page<DiseaseItem> page = diseaseItemRepository.findByDiseaseNameContainingOrderByGeneItemsDesc("1", PageRequest.of(2, 5));
//        System.out.println("total pages: " + page.getTotalPages());
//        page.getContent().forEach(d -> System.out.println(d.getDiseaseName()));
//
////        diseaseItemRepository.findAllOrderByGeneItemsDesc(PageRequest.of(0,50)).getContent().forEach(d-> System.out.println(d.getGeneItems().size()));
//        diseaseItemRepository.findAllOrderByGeneItemsDesc(PageRequest.of(0,50)).forEach(d-> System.out.println(d.getGeneItems().size()));
    }
}
