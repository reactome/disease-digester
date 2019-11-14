<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="v-bind" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Table Demo</title>
</head>
<body>
<h1>Overlay of external binary data onto Reactome</h1>

Background: Various valuable datasets are available as sets of binary data
pairs of the form
Entity - Relationship - Protein. Examples are Drug-target or
Disease-diseaseGene relationships. We would like to develop
a mechanism to efficiently connect such data to Reactome, with minimal
development effort, largely using existing
mechanisms, namely search/overrepresentation analysis and interactor
overlay. Slides to explain the idea are
<br>
<br>
<div id="app">
    <%--    the parameter div--%>
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
            <input type="checkbox" name="analysisParameters" @change="switchIfIncludeInteractors">Include interactors
        </p>
    </div>
    <%--    the table div--%>
    <div>
        <table align="center">
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
                    <button @click="analyze(disease.geneItems)">Analyze</button>
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
<script src="${pageContext.request.contextPath}/resources/js/vue.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/axios.js"></script>
<%--todo move javascript code to some XXX.js file--%>
<script type="text/javascript">
    let app = new Vue({
        el: '#app',
        data: {
            diseases: [],
            geneSize: 10,
            maxGeneSize: null,
            analysisParameter: 'projection',
            projectToHuman: true,
            includeInteractors: false,
            nameKeyword: null,
            nameKeywords: null,
            classKeyword: null,
            classKeywords: null,
            first: null,
            last: null,
            pageSize: 40,
            offset: null,
            pageNumber: 1,
            showPageNumber: [],
            totalPages: null,
            totalElements: null,
            sort: 'disease',
            order: 'asc',
            genes: null,
        },
        created: function () {
            axios.get('${pageContext.request.contextPath}/findAll?pageNumber=1&pageSize=40&geneSize=10&sort=disease&order=asc')
                .then(res => this.setData(res.data))
                .catch(err => console.log(err));
            axios.get('${pageContext.request.contextPath}/diseaseNameHintWord')
                .then(res => {
                    this.nameKeywords = res.data.keywords;
                })
                .catch(err => console.log(err));
            axios.get('${pageContext.request.contextPath}/diseaseClassHintWord')
                .then(res => {
                    this.classKeywords = res.data.keywords;
                })
                .catch(err => console.log(err));
            axios.get('${pageContext.request.contextPath}/getMaxGeneSize')
                .then(res => {
                    // this.maxGeneSize = Math.log(res.data);
                    this.maxGeneSize = res.data;
                })
                .catch(err => console.log(err));
        },
        computed: {
            pages: function () {
                let pages = [];
                if (this.totalPages > 7 && this.totalPages - 7 > this.pageNumber) {
                    for (let i = this.pageNumber; i < this.pageNumber + 7; i++) {
                        pages.push(i);
                    }
                } else {
                    for (let i = this.pageNumber; i <= this.totalPages; i++) {
                        pages.push(i);
                    }
                }
                return pages;
            },
            // showLastButton: function () {
            //     // return !this.last && this.totalPages > 7;
            //     return !this.last;
            // },
            // showNextButton: function () {
            //     return this.pageNumber !== this.totalPages;
            // }
        },
        methods: {
            setData: function (data) {
                this.diseases = data.content;
                this.first = data.first;
                this.last = data.last;
                this.pageSize = data.size;
                this.offset = data.pageable.offset;
                this.totalPages = data.totalPages;
                this.totalElements = data.totalElements;
                // if (data.number + 1 < this.pageNumber) {
                this.pageNumber = data.number + 1;
                // }
            },
            loadData: function (func = '/findAll', pageNumber, pageSize, geneSize, sort = 'disease', order = 'asc') {
                let url = null;
                if (func.match('/findAll')) {
                    url = '${pageContext.request.contextPath}' + func + '?pageNumber=' + pageNumber + '&pageSize=' + pageSize + '&geneSize=' + geneSize + '&sort=' + sort + '&order=' + order;
                } else {
                    url = '${pageContext.request.contextPath}' + func + '&pageNumber=' + pageNumber + '&pageSize=' + pageSize + '&geneSize=' + geneSize + '&sort=' + sort + '&order=' + order;
                }
                axios.get(url).then(res => this.setData(res.data))
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            refreshPageData() {
                if (this.nameKeyword != null) {
                    this.loadData('/findByDiseaseName?name=' + this.nameKeyword, this.pageNumber, this.pageSize, this.geneSize, this.sort, this.order);
                } else if (this.classKeyword != null) {
                    this.loadData('/findByDiseaseClass?class=' + this.classKeyword, this.pageNumber, this.pageSize, this.geneSize, this.sort, this.order)
                } else {
                    this.loadData('/findAll', this.pageNumber, this.pageSize, this.geneSize, this.sort, this.order)
                }
            },
            getGeneList(geneItems, separator) {
                let geneList = [];
                for (let i = 0; i < geneItems.length; i++) {
                    geneList.push(geneItems[i].geneSymbol);
                }
                if (separator != null) {
                    return geneList.sort().join(separator);
                }
                return geneList;
            },
            analyze(geneItems) {
                this.genes = this.getGeneList(geneItems);
                let data = {
                    "projectToHuman": this.projectToHuman,
                    "includeInteractors": this.includeInteractors,
                    "genes": this.genes
                };
                axios.post('${pageContext.request.contextPath}/analyze', data)
                    .then(res => {
                        // window.open(res.data, replace = true).focus();
                        window.open(res.data).focus();
                    }).catch(err => {
                    console.log(err)
                });
            },
            updatePropertyFunc(value, target, maximum, callback) {
                //this is the generic function to update the target property value by the giving new value.
                //then evacuate the callback func
                if (value > maximum) {
                    target = maximum;
                } else if (value < 1) {
                    target = 1;
                } else {
                    target = parseInt(value);
                }
                callback();
            },
            changeMinGeneSize(geneSize) {
                // this.updatePropertyFunc(geneSize,this.geneSize,this.maxGeneSize,this.refreshPageData);
                if (geneSize > this.maxGeneSize) {
                    this.geneSize = this.maxGeneSize;
                } else if (geneSize < 1) {
                    this.geneSize = 1;
                } else {
                    this.geneSize = parseInt(geneSize);
                }
                this.refreshPageData();
            },
            changePageSize(pageSize) {
                this.pageNumber = pageSize > this.pageSize ? 1 : this.pageNumber;
                this.pageSize = parseInt(pageSize);
                this.refreshPageData();
            },
            switchIfProjectToHuman() {
                this.projectToHuman = !this.projectToHuman;
                console.log(this.projectToHuman + ' : ' + this.includeInteractors);
            },
            switchIfIncludeInteractors() {
                this.includeInteractors = !this.includeInteractors;
                console.log(this.projectToHuman + ' : ' + this.includeInteractors);
            },
            reverseOrderAndLoadData() {
                if (this.order === 'asc') {
                    this.order = 'desc';
                } else {
                    this.order = 'asc';
                }
                this.refreshPageData();
            },
            sortByDiseaseName() {
                this.sort = 'disease';
                this.reverseOrderAndLoadData();
            },
            sortByDiseaseClass() {
                this.sort = 'class';
                this.reverseOrderAndLoadData();
            },
            sortByGeneNumber() {
                this.sort = 'gene';
                this.reverseOrderAndLoadData();
            },
            goto(pageNumber) {
                // this.updatePropertyFunc(pageNumber, this.pageNumber, this.maxGeneSize, this.refreshPageData);
                if (pageNumber > this.totalPages) {
                    this.pageNumber = this.totalPages;
                } else if (pageNumber < 1) {
                    this.pageNumber = 1;
                } else {
                    this.pageNumber = parseInt(pageNumber);
                }
                this.refreshPageData();
            },
            searchDiseaseName(nameKeyword) {
                this.nameKeyword = nameKeyword;
                this.classKeyword = null;
                this.loadData('/findByDiseaseName?name=' + this.nameKeyword, 1, this.pageSize, this.geneSize, this.sort, this.order);
            },
            searchDiseaseClass(classKeyword) {
                this.classKeyword = classKeyword;
                this.nameKeyword = null;
                this.loadData('/findByDiseaseClass?class=' + this.classKeyword, 1, this.pageSize, this.geneSize, this.sort, this.order);
            },
            sortBy(sort) {
                this.sort = sort;
            },
            orderBy(order) {
                this.order = order;
            },
            prevPage: function () {
                if (!this.first) {
                    this.pageNumber -= 1;
                    this.refreshPageData();
                }
            },
            nextPage: function () {
                if (!this.last) {
                    this.pageNumber += 1;
                    this.refreshPageData();
                }
            }
        },
    })
</script>
</body>
</html>
