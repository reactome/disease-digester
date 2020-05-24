package org.reactome.server.util;

import org.reactome.server.domain.DiseaseItem;

import java.util.Comparator;

public class DiseaseItemComparator {
    public static final Comparator<DiseaseItem> GeneSizASCComparator = Comparator.comparingInt(d -> d.getGeneItems().size());
    public static final Comparator<DiseaseItem> GeneSizDESCComparator = (d1, d2) -> d2.getGeneItems().size() - d1.getGeneItems().size();
    public static final Comparator<DiseaseItem> DiseaseNameASCComparator = Comparator.comparing(DiseaseItem::getDiseaseName);
    public static final Comparator<DiseaseItem> DiseaseNameDESCComparator = (d1, d2) -> d2.getDiseaseName().compareTo(d1.getDiseaseName());
}