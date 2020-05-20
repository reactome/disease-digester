<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="header.jsp"/>

<div class="favth-col-xs-12">

    <div>
        <h2>DisGeNET overlay of gene-disease associations</h2>

        <a href="http://www.disgenet.org/" target="_blank">DisGeNET </a>
        (<a href="https://www.ncbi.nlm.nih.gov/pubmed/31680165" target="_blank">Pinero J, et al, Nucleic Acids Res.
        2019</a>) is a database of gene-disease associations.
        We have pre-processed DisGeNET curated gene-disease associations (Release v6.0) for overlay onto Reactome.
        For each disease, clicking on the "Analysis" button will show the results of Reactome pathway analysis with
        the set of genes associated with that disease.
        If you are interested in overlaying other data sources onto Reactome in a similar manner, please
        <a href="mailto:help@reactome.org">contact us</a>.
    </div>

    <div id="disease_digester">
        <%--    the table div--%>
        <div id="r-notresponsive-table" class="details-wrap enlarge-table">
            <table class="reactome table-parameter">
                <caption>Parameters</caption>
                <thead>
                <tr>
                    <th width="25%" scope="col">Parameter</th>
                    <th width="20%" scope="col">Option</th>
                    <th width="55%" scope="col">Description</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td data-label="Parameter">Minimum number of genes per disease</td>
                    <td data-label="Option">
                        <input type="range" min="1" :max="maxGeneSize" v-model="geneSize"
                               @change="changeMinGeneSize($event.target.value)">
                        <input class="gene" type="number" min="1" :max="maxGeneSize" v-model="geneSize"
                               @change="changeMinGeneSize($event.target.value)">
                    </td>
                    <td data-label="Description">The Reactome pathway analysis presented here only makes sense for
                        multiple genes. Diseases with less gene associations than this parameter are not displayed in
                        the table. For small numbers of genes, it might be helpful to switch from "pValue" to "coverage"
                        at the bottom of the visualisation.
                    </td>
                </tr>
                <tr>
                    <td data-label="Parameter">Score</td>
                    <td data-label="Option">
                        <div>
                            <input type="radio" name="score" value=null :checked="cutoffValue==null"
                                   @change="changeCutoffValue($event.target.value)">Low (no filter)
                        </div>
                        <div>
                            <input type="radio" name="score" value=0.33 :checked="cutoffValue==0.33"
                                   @change="changeCutoffValue($event.target.value)">Medium (> 0.33)
                        </div>
                        <div>
                            <input type="radio" name="score" value=0.66 :checked="cutoffValue==0.66"
                                   @change="changeCutoffValue($event.target.value)">High (> 0.66)
                        </div>
                    </td>
                    <td data-label="Description">Score of the gene-disease association as provided by DisGeNET.
                    </td>
                </tr>
                <tr>
                    <td data-label="Parameter">Include interactors</td>
                    <td data-label="Option"><input type="checkbox" name="analysisParameters"
                                                   @change="switchIfIncludeInteractors" :checked="includeInteractors">
                    </td>
                    <td data-label="Description">Include high confidence interactors from IntAct in Reactome.
                    </td>
                </tr>
                <tr>
                    <td data-label="Parameter">Default result view</td>
                    <td data-label="Option">
                        <div>
                            <input type="radio" name="analysisParameters" :checked="redirectToReacFoam"
                                   @change="switchIfRedirectToReacFoam">ReacFoam
                        </div>
                        <div>
                            <input type="radio" name="analysisParameters" :checked="!redirectToReacFoam"
                                   @change="switchIfRedirectToReacFoam">Fireworks
                        </div>
                    </td>
                    <td data-label="Description">Reactome provides two different options for the first view of the
                        analysis results. Choose your preference.
                    </td>
                </tr>
                <tr>
                    <td data-label="Parameter">Reset the filters</td>
                    <td data-label="Option">
                        <button @click="resetParameters">Reset</button>
                    </td>
                    <td data-label="Description">Clear disease name filter, and reset to default values.</td>
                </tr>
                </tbody>
            </table>

            <div class="table-wrapper">
                <table class="reactome">
                    <caption>Overlay Table</caption>
                    <thead>
                    <tr>
                        <th scope="col">Check in Pathway Browser</th>
                        <th scope="col">Disease name
                            <button class="btn-sort" @click="sortByDiseaseName">
                                <span class="fa fa-angle-up"></span>
                                <span class="fa fa-angle-down"></span>
                            </button>
                            <input type="text" placeholder="Disease name filter" v-model="nameKeyword"
                                   list="nameKeywords"
                                   @change="searchDiseaseName($event.target.value)">
                            <datalist id="nameKeywords">
                                <option v-for="nameKeyword in nameKeywords">{{nameKeyword}}</option>
                            </datalist>
                        </th>
                        <th scope="col">Number of genes
                            <button class="btn-sort" @click="sortByGeneNumber">
                                <span class="fa fa-angle-up"></span>
                                <span class="fa fa-angle-down"></span></button>
                        </th>
                        <th scope="col">Gene list</th>
                        <th scope="col">Disease id</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="disease in diseases">
                        <td data-label="Check">
                            <button class="btn btn-primary" @click="analyze(disease.geneItems)">Analysis</button>
                        </td>
                        <td data-label="Disease Name">{{disease.diseaseName}}</td>
                        <td data-label="Number of genes">{{disease.geneItems.length}}</td>
                        <td data-label="Gene list">
                            <div class="td-text-overflow">{{getGeneList(disease.geneItems)}}</div>
                        </td>
                        <td data-label="Disease id"><a
                                :href="'https://www.disgenet.org/search?disease='+disease.diseaseId"
                                target="_blank">{{disease.diseaseId}}</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <%--        the table footer div--%>
        <div>
            <pagination :page-number="pageNumber" :page-size="pageSize" :total-page="totalPage"
                        @change-page-number="changePageNumber" @change-page-size="changePageSize"></pagination>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>
