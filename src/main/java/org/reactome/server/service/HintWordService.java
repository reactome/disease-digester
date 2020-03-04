package org.reactome.server.service;

import org.reactome.server.domain.HintWord;
import org.reactome.server.repository.DiseaseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HintWordService {
    private DiseaseItemRepository diseaseItemRepository;

    @Autowired
    public HintWordService(DiseaseItemRepository diseaseItemRepository) {
        this.diseaseItemRepository = diseaseItemRepository;
    }

    private static HintWord diseaseName;
    private static HintWord diseaseClass;
    private static final String STOP_WORD = "stop_word.txt";
    private static final Set<String> STOP_WORD_SET = new BufferedReader(new InputStreamReader(Objects.requireNonNull(HintWordService.class.getClassLoader().getResourceAsStream(STOP_WORD)))).lines().collect(Collectors.toSet());
    private static final String DISEASE_CLASS = "disease-class.properties";
    private static final InputStreamReader DISEASE_CLASS_STREAM = new InputStreamReader(Objects.requireNonNull(HintWordService.class.getClassLoader().getResourceAsStream(DISEASE_CLASS)));

    private void setDiseaseNameHintWord() {
        Set<String> nameSet = diseaseItemRepository.findAll().stream()
                .map(diseaseItem -> diseaseItem.getDiseaseName().split("_"))
                .flatMap(Arrays::stream).filter(s -> s.length() > 3)
                .collect(Collectors.toSet());
        nameSet.removeAll(STOP_WORD_SET);
        diseaseName = new HintWord(nameSet);
    }

    private void setDiseaseClassHintWord() {
        Properties properties = new Properties();
        try {
            properties.load(DISEASE_CLASS_STREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> classSet = properties.entrySet().parallelStream()
                .map(Map.Entry::getValue)
                .map(s -> s.toString().split(" "))
                .flatMap(Arrays::stream)
                .filter(s -> !s.equals("and"))
                .filter(s -> !s.equals("of"))
                .map(s -> s.replace(",", ""))
                .filter(s -> diseaseItemRepository.existsByDiseaseClassContains(s))
                .collect(Collectors.toSet());
        diseaseClass = new HintWord(classSet);
    }

    public HintWord getDiseaseNameHintWord() {
        if (diseaseName == null) {
            setDiseaseNameHintWord();
        }
        return diseaseName;
    }

    public HintWord getDiseaseClassHintWord() {
        if (diseaseClass == null) {
            setDiseaseClassHintWord();
        }
        return diseaseClass;
    }
}
