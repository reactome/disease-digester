package org.reactome.server.repository.out;

public interface Sorted<X> {
    X getResult();
    Long getAssociatedGeneCount();
}
