package org.reactome.server.repository;

import org.reactome.server.domain.DiseaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseItemRepository extends JpaRepository<DiseaseItem, String> {
}