<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>DisGeNET overlay of gene-disease associations</title>
    <script src="${pageContext.request.contextPath}/resources/js/vue.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/axios.js"></script>
    <script type="module" src="${pageContext.request.contextPath}/resources/js/disease_digester.js"></script>
</head>
<body>
<h2>DisGeNET overlay of gene-disease associations</h2>

<a href="http://www.disgenet.org/" target="_blank">DisGeNET</a>(Pinero J, et al, Nucleic Acids Res. 2019) is a database
of gene-disease
associations.
We have pre-processed DisGeNET curated gene-disease associations (Release v6.0) for overlay onto
Reactome.
For each disease, clicking on the "Analysis" button will show the results of Reactome pathway analysis with the set of
genes associated with that disease.
If you are interested in overlaying other data sources onto Reactome in a similar manner, please <a
        href="mailto:help@reactome.org">contact us</a>.
<br>

<div>
    <%--    <h4>Parameters</h4>--%>
    <table align="center">
        <caption>Parameters</caption>
        <tbody>
        <tr>
            <td>Minimum number of genes per disease</td>
            <td>Slider and numeric box</td>
            <td>The Reactome pathway analysis presented here only makes sense for multiple genes.
                Diseases with less gene associations are not displayed in the table.
            </td>
        </tr>
        <tr>
            <td>Project to human</td>
            <td>Tick box</td>
            <td>All provided gene names are mapped to human orthologs, where possible.</td>
        </tr>
        <tr>
            <td>Include interactors</td>
            <td>Tick box</td>
            <td>Include interactors Tick box Include high confidence interactors from IntAct in Reactome.</td>
        </tr>
        <tr>
            <td>Default result view</td>
            <td>ReacFoam <br>
                Fireworks
            </td>
            <td>Default result view ReacFoam
                Fireworks Reactome provides two different options for the first view of the analysis results.
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div id="disease_digester">
    <%--    the table div--%>
    <div>
        <table align="center">
            <caption>
                <%--    the parameter wegeit div--%>
                <div align="center">
                    <p>Minimum number of genes/disease:
                        <input type="range" min="1" v-bind:max="maxGeneSize" v-model="geneSize"
                               @change="changeMinGeneSize($event.target.value)">
                        <input type="number" min="1" v-bind:max="maxGeneSize" v-model="geneSize"
                               @change="changeMinGeneSize($event.target.value)">
                    </p>
                    <p>
                        Analyze parameter:
                        <input type="checkbox" name="analysisParameters" checked @change="switchIfProjectToHuman">Project
                        to human &nbsp;
                        <input type="checkbox" name="analysisParameters" @change="switchIfIncludeInteractors">Include
                        interactors
                        <br>
                        <input type="radio" name="analysisParameters" checked @change="switchIfRedirectToReacFoam">ReacFoam
                        &nbsp;
                        <input type="radio" name="analysisParameters" @change="switchIfRedirectToReacFoam">Fireworks
                    </p>
                </div>
            </caption>
            <thead>
            <tr>
                <th>Check in Pathway Browser</th>
                <th>Disease name
                    <button @click="sortByDiseaseName">{{order}}</button>
                    <br>
                    <input type="text" placeholder="Disease name filter" v-model="nameKeyword" list="nameKeywords"
                           @change="searchDiseaseName($event.target.value)">
                    <datalist id="nameKeywords">
                        <option v-for="nameKeyword in nameKeywords">{{nameKeyword}}</option>
                    </datalist>
                </th>
                <th>Disease class
                    <button @click="sortByDiseaseClass">{{order}}</button>
                    <br>
                    <input type="text" placeholder="Disease class filter" v-model="classKeyword" list="classKeywords"
                           @change="searchDiseaseClass($event.target.value)">
                    <datalist id="classKeywords">
                        <option v-for="classKeyword in classKeywords">{{classKeyword}}</option>
                    </datalist>
                </th>
                <th>Number of genes
                    <button @click="sortByGeneNumber">{{order}}</button>
                </th>
                <th>Gene list</th>
                <th>Disease id</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="disease in diseases">
                <td>
                    <button @click="analyze(disease.geneItems)">Analysis</button>
                </td>
                <td>{{disease.diseaseName}}</td>
                <td>{{disease.diseaseClass}}</td>
                <td>{{disease.geneItems.length}}</td>
                <td>{{getGeneList(disease.geneItems,', ')}}</td>
                <td>{{disease.diseaseId}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <%--        the table footer div--%>
    <div>
        <p align="center">
            <button @click="goto(1)" v-if="!first">First</button>
            <button @click="prevPage" v-if="!first">Prev</button>
            <button @click="goto(page)" v-for="page in pages">{{page}}
            </button>
            <button @click="nextPage" v-if="!last">Next</button>
            <button @click="goto(totalPages)" v-if="pageNumber !== totalPages">Last</button>
            <br>
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
        </p>
    </div>
</div>
<br>
</body>
</html>
