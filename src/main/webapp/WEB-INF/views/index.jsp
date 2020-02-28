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
        <div id="r-responsive-table" class="details-wrap enlarge-table">
            <table class="reactome table-parameter">
                <caption>Parameters</caption>
                <thead>
                <tr>
                    <th width="20%" scope="col">Parameter</th>
                    <th width="20%" scope="col">Option</th>
                    <th width="70%" scope="col">Description</th>
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
                <%--                <tr>--%>
                <%--                    <td data-label="Parameter">Project to human</td>--%>
                <%--                    <td data-label="Option"><input type="checkbox" name="analysisParameters" checked--%>
                <%--                                                   @change="switchIfProjectToHuman"></td>--%>
                <%--                    <td data-label="Description">All provided gene names are mapped to human orthologs, where--%>
                <%--                        possible.--%>
                <%--                    </td>--%>
                <%--                </tr>--%>
                <tr>
                    <td data-label="Parameter">Include interactors</td>
                    <td data-label="Option"><input type="checkbox" name="analysisParameters"
                                                   @change="switchIfIncludeInteractors"></td>
                    <td data-label="Description">Include high confidence interactors from IntAct in Reactome.
                    </td>
                </tr>
                <tr>
                    <td data-label="Parameter">Default result view</td>
                    <td data-label="Option">
                        <div>
                            <input type="radio" name="analysisParameters" checked @change="switchIfRedirectToReacFoam">ReacFoam
                        </div>
                        <div>
                            <input type="radio" name="analysisParameters" @change="switchIfRedirectToReacFoam">Fireworks
                        </div>
                    </td>
                    <td data-label="Description">Reactome provides two different options for the first view of the
                        analysis results. Choose your preference.
                    </td>
                </tr>
                <tr>
                    <td data-label="Parameter">Reset the filters</td>
                    <td data-label="Option">
                        <button @click="resetKeyWord">Reset</button>
                    </td>
                    <td data-label="Description">Clear all filters, and reset to default values.</td>
                </tr>
                </tbody>
            </table>
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
                        <input type="text" placeholder="Disease name filter" v-model="nameKeyword" list="nameKeywords"
                               @change="searchDiseaseName($event.target.value)">
                        <datalist id="nameKeywords">
                            <option v-for="nameKeyword in nameKeywords">{{nameKeyword}}</option>
                        </datalist>
                    </th>
                    <th scope="col">Disease class
                        <button class="btn-sort" @click="sortByDiseaseClass">
                            <span class="fa fa-angle-up"></span>
                            <span class="fa fa-angle-down"></span></button>
                        <input type="text" placeholder="Disease class filter" v-model="classKeyword"
                               list="classKeywords"
                               @change="searchDiseaseClass($event.target.value)">
                        <datalist id="classKeywords">
                            <option v-for="classKeyword in classKeywords">{{classKeyword}}</option>
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
                    <td data-label="Disease class">{{disease.diseaseClass}}</td>
                    <td data-label="Number of genes">{{disease.geneItems.length}}</td>
                    <td data-label="Gene list">
                        <div class="td-text-overflow">{{getGeneList(disease.geneItems,', ')}}</div>
                    </td>
                    <td data-label="Disease id">{{disease.diseaseId}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <%--        the table footer div--%>
        <div>
            <pagination :page-number="pageNumber" :page-size="pageSize" :total-page="totalPage"
                        @change-page-number="changePageNumber" @change-page-size="changePageSize"></pagination>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>
