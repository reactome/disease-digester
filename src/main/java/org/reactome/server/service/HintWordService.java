package org.reactome.server.service;

import org.reactome.server.domain.DiseaseNameHintWord;
import org.reactome.server.mapper.DiseaseItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HintWordService {
    private DiseaseItemMapper diseaseItemMapper;

    @Autowired
    public HintWordService(DiseaseItemMapper diseaseItemMapper) {
        this.diseaseItemMapper = diseaseItemMapper;
    }

    private static volatile DiseaseNameHintWord diseaseName;
    private static final String STOP_WORD = "stop_word.txt";
    private static final Set<String> STOP_WORD_SET = new BufferedReader(new InputStreamReader(Objects.requireNonNull(HintWordService.class.getClassLoader().getResourceAsStream(STOP_WORD)))).lines().collect(Collectors.toSet());

    private void setDiseaseNameHintWord() {
        Set<String> nameSet = diseaseItemMapper.selectDiseaseName().stream()
                .flatMap(s -> Stream.of(s.split("_")))
                .filter(s -> s.length() > 3)
                .collect(Collectors.toSet());
        nameSet.removeAll(STOP_WORD_SET);
        diseaseName = new DiseaseNameHintWord(nameSet);
    }

    public DiseaseNameHintWord getDiseaseNameHintWord() {
        if (null == diseaseName) {
            synchronized (HintWordService.class) {
                setDiseaseNameHintWord();
            }
        }
        return diseaseName;
    }
}
