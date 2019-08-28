package org.reactome.server.repository;

import org.reactome.server.domain.DiseaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseItemRepository extends JpaRepository<DiseaseItem, String> {
    public List<DiseaseItem> findDiseaseItemsByDiseaseClassContaining(String diseaseClass);

    public List<DiseaseItem> findDiseaseItemsByDiseaseNameContaining(String diseaseName);
}