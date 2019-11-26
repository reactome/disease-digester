<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="header.jsp"/>

<div class="favth-col-xs-12">

    <div>
        <h2>DisGeNET overlay of gene-disease associations</h2>

        <a href="http://www.disgenet.org/" target="_blank">DisGeNET </a>(<a
            href="https://www.ncbi.nlm.nih.gov/pubmed/31680165" target="_blank">Pinero J, et al, Nucleic Acids Res.
        2019</a>) is a
        database
        of gene-disease
        associations.
        We have pre-processed DisGeNET curated gene-disease associations (Release v6.0) for overlay onto
        Reactome.
        For each disease, clicking on the "Analysis" button will show the results of Reactome pathway analysis with the
        set of
        genes associated with that disease.
        If you are interested in overlaying other data sources onto Reactome in a similar manner, please <a
            href="mailto:help@reactome.org">contact us</a>.
    </div>

    <div id="disease_digester">
        <%--    the table div--%>
        <div id="r-responsive-table" class="details-wrap enlarge-table">
            <table class="reactome table-parameter">
                <caption>Parameters</caption>
                <thead>
                <tr>
                    <th scope="col">Parameter</th>
                    <th scope="col">Option</th>
                    <th scope="col">Description</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Minimum number of genes per disease</td>
                    <td>
                        <input type="range" min="1" v-bind:max="maxGeneSize" v-model="geneSize"
                               @change="changeMinGeneSize($event.target.value)">
                        <input type="number" min="1" v-bind:max="maxGeneSize" v-model="geneSize"
                               @change="changeMinGeneSize($event.target.value)">
                    </td>
                    <td>The Reactome pathway analysis presented here only makes sense for multiple genes.
                        Diseases with less gene associations are not displayed in the table.
                    </td>
                </tr>
                <tr>
                    <td>Project to human</td>
                    <td><input type="checkbox" name="analysisParameters" checked @change="switchIfProjectToHuman"></td>
                    <td>All provided gene names are mapped to human orthologs, where possible.</td>
                </tr>
                <tr>
                    <td>Include interactors</td>
                    <td><input type="checkbox" name="analysisParameters" @change="switchIfIncludeInteractors"></td>
                    <td>Include interactors Tick box Include high confidence interactors from IntAct in Reactome.</td>
                </tr>
                <tr>
                    <td>Default result view</td>
                    <td>
                        <div>
                            <input type="radio" name="analysisParameters" checked @change="switchIfRedirectToReacFoam">ReacFoam
                        </div>
                        <div>
                            <input type="radio" name="analysisParameters" @change="switchIfRedirectToReacFoam">Fireworks
                        </div>
                    </td>
                    <td>Default result view ReacFoam
                        Fireworks Reactome provides two different options for the first view of the analysis results.
                    </td>
                </tr>
                </tbody>
            </table>


            <table class="reactome">
                <caption>Overlay Table</caption>
                <thead>
                <tr>
                    <th scope="col">Check in Pathway Browser</th>
                    <th scope="col">Disease name
                        <button class="btn-sort" @click="sortByDiseaseName">{{order}}</button>
                        <br>
                        <input type="text" placeholder="Disease name filter" v-model="nameKeyword" list="nameKeywords"
                               @change="searchDiseaseName($event.target.value)">
                        <datalist id="nameKeywords">
                            <option v-for="nameKeyword in nameKeywords">{{nameKeyword}}</option>
                        </datalist>
                    </th>
                    <th scope="col">Disease class
                        <button class="btn-sort" @click="sortByDiseaseClass">{{order}}</button>
                        <br>
                        <input type="text" placeholder="Disease class filter" v-model="classKeyword"
                               list="classKeywords"
                               @change="searchDiseaseClass($event.target.value)">
                        <datalist id="classKeywords">
                            <option v-for="classKeyword in classKeywords">{{classKeyword}}</option>
                        </datalist>
                    </th>
                    <th scope="col">Number of genes
                        <button class="btn-sort" @click="sortByGeneNumber">{{order}}</button>
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
            <div id="pagination" class="pagination">
                <ul class="pagination-list">
                    <li class="active"><a class="pagenav" @click="goto(1)" v-if="!first">First</a></li>
                    <li><a class="pagenav" @click="prevPage" v-if="!first">Prev</a></li>
                    <ul v-for="page in pages">
                        <li><a class="pagenav" @click="goto(page)">{{page}}</a></li>
                    </ul>
                    <li><a class="pagenav" @click="nextPage" v-if="!last">Next</a></li>
                    <li><a class="pagenav" @click="goto(totalPages)" v-if="pageNumber !== totalPages">Last</a></li>
                </ul>
            </div>
            <span>
                Showing: {{offset+1}} - {{((offset+pageSize)>totalElements)?totalElements:offset+pageSize}} of
                {{totalElements}}
                records &nbsp;&nbsp;&nbsp;
                Page:<input type="number" min="1" v-bind:max="totalPages" @change="goto($event.target.value)"
                            v-bind:value="pageNumber"/> of
                {{totalPages}}
                with page size
                <select @change="changePageSize($event.target.value)">
                    <option value="10">10</option>
                    <option value="50" selected="selected">50</option>
                    <option value="100">100</option>
                </select>
            </span>
        </div>
    </div>
</div>
<c:import url="footer.jsp"/>
