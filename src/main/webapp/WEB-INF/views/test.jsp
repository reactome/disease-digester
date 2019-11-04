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
                <%--TODO: input box auto-completing--%>
                <br><input @change="searchDiseaseName" type="text"
                           placeholder="Disease name filter"
                           v-bind:value="name"></th>
            <th>Disease class
                <button @click="sortByDiseaseClass">{{order}}</button>
                <br><input @change="searchDiseaseClass" type="text" placeholder="Disease class filter"
                           v-bind:value="clzss">
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
        Page:<input @change="gotoPage" type="text" v-bind:value="pageNumber"/> of {{totalPages}} with page size
        <select @change="changeSize">
            <option value="20">20</option>
            <option value="30">30</option>
            <option value="40" selected="selected">40</option>
            <option value="50">50</option>
            <option value="60">60</option>
            <option value="70">70</option>
        </select>
    </p>
</div>
</body>
<script src="${pageContext.request.contextPath}/resources/js/vue.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/axios.js"></script>
<script type="text/javascript">
    let app = new Vue({
        el: '#app',
        data: {
            diseases: [],
            name: null,
            clzss: null,
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
                console.log(url);
                axios.get(url).then(res => this.setData(res.data))
                    .catch(function (error) {
                        console.log(error);
                    });
            },
            loadDataByFunc(page) {
                if (this.name != null) {
                    this.loadData('/findByDiseaseName?name=' + this.name, page, this.size, this.sort, this.order);
                } else if (this.clzss != null) {
                    this.loadData('/findByDiseaseClass?class=' + this.clzss, page, this.size, this.sort, this.order)
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
                console.log(data);
                <%--axios.post('${pageContext.request.contextPath}/analyze', payload)--%>
                axios.post('http://localhost:8080/disease-digester/analyze', data)
                    .then(res => window.open(res.data, replace = true))
                    .catch(err => {
                        console.log(err)
                    });
            },
            changeSize(event) {
                let size = event.target.value;
                let page = size > this.size ? 1 : this.pageNumber;
                this.size = size;
                if (this.clzss == null && this.name == null) {
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
            gotoPage(event) {
                let page = event.target.value;
                if (page <= this.totalPages) {
                    this.goto(page);
                }
            },
            searchDiseaseName(event) {
                let name = event.target.value;
                this.name = name;
                this.clzss = null;
                this.loadData('/findByDiseaseName?name=' + name, 1, this.size, this.sort, this.order);
            },
            searchDiseaseClass(event) {
                let clzss = event.target.value;
                this.clzss = clzss;
                this.name = null;
                this.loadData('/findByDiseaseClass?class=' + clzss, 1, this.size, this.sort, this.order);
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
</html>
