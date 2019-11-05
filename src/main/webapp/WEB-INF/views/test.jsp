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
    <p align="center">
        <button @click="goto(1)" v-if="!first">First</button>
        <button @click="prevPage" v-if="!first">Prev</button>
        <button @click="goto(page)" v-for="page in showPageNumber.slice(pageNumber-1,pageNumber+5)">{{page}}
        </button>
        <button @click="nextPage">Next</button>
        <button @click="goto(totalPages)" v-if="!last">Last</button>
        <br>
        Showing: {{offset+1}} - {{((offset+size)>totalElements)?totalElements:offset+size}} of {{totalElements}}
        records &nbsp;&nbsp;&nbsp;
        Page:<input @change="gotoPage($event.target.value)" type="text" v-bind:value="pageNumber"/> of {{totalPages}}
        with page size
        <select @change="changeSize($event.target.value)">
            <option value="20">20</option>
            <option value="30">30</option>
            <option value="40" selected="selected">40</option>
            <option value="50">50</option>
            <option value="60">60</option>
            <option value="70">70</option>
        </select>
    </p>
</div>
<script src="${pageContext.request.contextPath}/resources/js/vue.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/axios.js"></script>
<script type="text/javascript">
    let app = new Vue({
        el: '#app',
        data: {
            diseases: [],
            nameKeyword: null,
            nameKeywords: null,
            classKeyword: null,
            classKeywords: null,
            first: null,
            last: null,
            size: 40,
            offset: null,
            pageNumber: null,
            showPageNumber: [],
            totalPages: null,
            totalElements: null,
            sort: 'disease',
            order: 'asc',
            genes: null,
        },
        created: function () {
            axios.get('${pageContext.request.contextPath}/findAll?page=1&size=40&sort=disease&order=asc')
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
        },
        methods: {
            setData: function (data) {
                // console.log(data);
                this.diseases = data.content;
                this.first = data.first;
                this.last = data.last;
                this.size = data.size;
                this.offset = data.pageable.offset;
                this.pageNumber = data.number + 1;
                this.totalPages = data.totalPages;
                this.totalElements = data.totalElements;
                this.showPageNumber = [];
                for (let i = 0; i < this.totalPages; i++) {
                    this.showPageNumber.push(i + 1);
                }
            },
            loadData: function (func = '/findAll', page = 1, size = 40, sort = 'disease', order = 'asc') {
                let url = null;
                if (func.match('/findAll')) {
                    url = '${pageContext.request.contextPath}' + func + '?page=' + page + '&size=' + size + '&sort=' + sort + '&order=' + order;
                } else {
                    url = '${pageContext.request.contextPath}' + func + '&page=' + page + '&size=' + size + '&sort=' + sort + '&order=' + order;
                }
                axios.get(url).then(res => this.setData(res.data))
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            loadDataByFunc(page) {
                if (this.nameKeyword != null) {
                    this.loadData('/findByDiseaseName?name=' + this.nameKeyword, page, this.size, this.sort, this.order);
                } else if (this.classKeyword != null) {
                    this.loadData('/findByDiseaseClass?class=' + this.classKeyword, page, this.size, this.sort, this.order)
                } else {
                    this.loadData('/findAll', page, this.size, this.sort, this.order)
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
                let data = {"genes": this.genes};
                axios.post('${pageContext.request.contextPath}/analyze', data)
                    .then(res => {
                        // window.open(res.data, replace = true).focus();
                        window.open(res.data).focus();
                    }).catch(err => {
                    console.log(err)
                });
            },
            changeSize(size) {
                let page = size > this.size ? 1 : this.pageNumber;
                this.size = size;
                if (this.classKeyword == null && this.nameKeyword == null) {
                    this.loadData('/findAll', page, this.size, this.sort, this.order);
                }
                this.loadDataByFunc(page);
            },
            reverseOrderAndLoadData() {
                if (this.order === 'asc') {
                    this.order = 'desc';
                } else {
                    this.order = 'asc';
                }
                this.loadDataByFunc(this.pageNumber);
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
            goto(page) {
                this.loadDataByFunc(page);
            },
            gotoPage(page) {
                if (page <= this.totalPages) {
                    this.goto(page);
                }
            },
            searchDiseaseName(nameKeyword) {
                this.nameKeyword = nameKeyword;
                this.classKeyword = null;
                this.loadData('/findByDiseaseName?name=' + this.nameKeyword, 1, this.size, this.sort, this.order);
            },
            searchDiseaseClass(classKeyword) {
                this.classKeyword = classKeyword;
                this.nameKeyword = null;
                this.loadData('/findByDiseaseClass?class=' + this.classKeyword, 1, this.size, this.sort, this.order);
            },
            sortBy(sort) {
                this.sort = sort;
            },
            orderBy(order) {
                this.order = order;
            },
            prevPage: function () {
                if (!this.first) {
                    this.loadDataByFunc(this.pageNumber - 1);
                }
            },
            nextPage: function () {
                if (!this.last) {
                    this.loadDataByFunc(this.pageNumber + 1);
                }
            }
        },
    })
</script>
</body>
</html>
