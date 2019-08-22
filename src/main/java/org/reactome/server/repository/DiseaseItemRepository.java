package org.reactome.server.repository;

import org.reactome.server.domain.DiseaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseItemRepository extends JpaRepository<DiseaseItem, String> {
    List<DiseaseItem> getDiseaseItemByDiseaseNameIsContaining(String diseaseName);
    List<DiseaseItem> getByDiseaseNameContains(String diseaseName);
}