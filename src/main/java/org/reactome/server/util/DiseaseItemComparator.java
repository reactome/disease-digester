package org.reactome.server.util;

import org.reactome.server.domain.DiseaseItem;

import java.util.Comparator;

public class DiseaseItemComparator {
    /*this class is created to sort the results list derived from the query, since there is a two step query process:
    first to select the qualified disease id(which is sorted) from disease table, then select the disease's associated genes from gene table
    so the raw results from the union-select query is out of order, need to sort it again*/
    private static int compareTo(DiseaseItem d1, DiseaseItem d2) {
        if (d1.getGeneItems().size() > d2.getGeneItems().size()) {
            return 1;
        } else if (d1.getGeneItems().size() == d2.getGeneItems().size()) {
            return d1.getDiseaseName().compareTo(d2.getDiseaseName());
        } else {
            return -1;
        }
    }

    public static final Comparator<DiseaseItem> GeneSizASCComparator = DiseaseItemComparator::compareTo;
    public static final Comparator<DiseaseItem> GeneSizDESCComparator = (d1, d2) -> compareTo(d2, d1);
    public static final Comparator<DiseaseItem> DiseaseNameASCComparator = Comparator.comparing(DiseaseItem::getDiseaseName);
    public static final Comparator<DiseaseItem> DiseaseNameDESCComparator = (d1, d2) -> d2.getDiseaseName().compareTo(d1.getDiseaseName());
}