package org.reactome.server.service;

import org.reactome.server.domain.DiseaseItem;
import org.reactome.server.repository.DiseaseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiseaseItemService {

    private final DiseaseItemRepository diseaseItemRepository;

    @Autowired
    public DiseaseItemService(DiseaseItemRepository diseaseItemRepository) {
        this.diseaseItemRepository = diseaseItemRepository;
    }

    public List<DiseaseItem> findAll() {
        return diseaseItemRepository.findAll();
    }

    public void saveAll(List<DiseaseItem> diseaseItems) {
        diseaseItemRepository.saveAll(diseaseItems);
    }
}