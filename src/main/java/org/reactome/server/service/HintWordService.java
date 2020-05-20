package org.reactome.server.service;

import org.reactome.server.domain.HintWord;
import org.reactome.server.repository.DiseaseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HintWordService {
    private DiseaseItemRepository diseaseItemRepository;

    @Autowired
    public HintWordService(DiseaseItemRepository diseaseItemRepository) {
        this.diseaseItemRepository = diseaseItemRepository;
    }

    private static volatile HintWord diseaseName;
    private static final String STOP_WORD = "stop_word.txt";
    private static final Set<String> STOP_WORD_SET = new BufferedReader(new InputStreamReader(Objects.requireNonNull(HintWordService.class.getClassLoader().getResourceAsStream(STOP_WORD)))).lines().collect(Collectors.toSet());

    private void setDiseaseNameHintWord() {
        Set<String> nameSet = diseaseItemRepository.findAll().stream()
                .map(diseaseItem -> diseaseItem.getDiseaseName().split("_"))
                .flatMap(Arrays::stream).filter(s -> s.length() > 3)
                .collect(Collectors.toSet());
        nameSet.removeAll(STOP_WORD_SET);
        diseaseName = new HintWord(nameSet);
    }

    public HintWord getDiseaseNameHintWord() {
        if (diseaseName == null) {
            setDiseaseNameHintWord();
        }
        return diseaseName;
    }
}
