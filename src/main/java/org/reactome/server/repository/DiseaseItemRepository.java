package org.reactome.server.repository;

import org.reactome.server.domain.DiseaseItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiseaseItemRepository extends JpaRepository<DiseaseItem, String> {
    // TODO: 19-8-29 the performance of use spring data jpa vs criteria api
    Page<DiseaseItem> findDiseaseItemsByDiseaseClassContaining(String diseaseClass, Pageable pageable);

    Page<DiseaseItem> findDiseaseItemsByDiseaseNameContaining(String diseaseName, Pageable pageable);
}