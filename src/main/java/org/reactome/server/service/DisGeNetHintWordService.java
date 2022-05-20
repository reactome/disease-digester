package org.reactome.server.service;

import org.reactome.server.domain.DiseaseNameHintWord;
import org.reactome.server.domain.model.SourceDatabase;
import org.reactome.server.repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DisGeNetHintWordService {
    private final DiseaseRepository diseaseRepository;

    @Autowired
    public DisGeNetHintWordService(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    private static volatile DiseaseNameHintWord diseaseName;
    private static final String STOP_WORD = "stop_word.txt";
    private static final Set<String> STOP_WORD_SET = new BufferedReader(new InputStreamReader(Objects.requireNonNull(DisGeNetHintWordService.class.getClassLoader().getResourceAsStream(STOP_WORD)))).lines().collect(Collectors.toSet());

    private void setDiseaseNameHintWord() {
        Set<String> nameSet = diseaseRepository.selectDiseaseName(SourceDatabase.DISGENET).stream()
                .flatMap(s -> Stream.of(s.split(" ")))
                .filter(s -> s.length() > 3)
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(TreeSet::new));
        nameSet.removeAll(STOP_WORD_SET);
        diseaseName = new DiseaseNameHintWord(nameSet);
    }

    public DiseaseNameHintWord getDiseaseNameHintWord() {
        if (null == diseaseName) {
            synchronized (DisGeNetHintWordService.class) {
                setDiseaseNameHintWord();
            }
        }
        return diseaseName;
    }
}
