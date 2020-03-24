Vue.component('pagination', {
    props: {
        pageNumber: {
            type: Number,
            default: 1,
        },
        pageSize: {
            type: Number,
            default: 50,
        },
        totalPage: Number,
    },
    data: function () {
        return {
            index: this.pageNumber,
            size: this.pageSize,
        }
    },
    template: "<div class='pagination'>" +
        "<ul  class='pagination-list'>" +
        "<li :class='{active:index===1 || totalPage===1}' ><a class='pagenav' @click='index=1'>first</a></li>" +
        "<li :class='{active:index===id,hide:totalPage<=2}' v-for='id in ids' ><a class='pagenav' @click='index=id'>{{id}}</a></li>" +
        "<li :class='{active:index===totalPage,hide:totalPage===1}' ><a class='pagenav' @click='index=totalPage'>last</a></li>" +
        "</ul>" +
        "<span>" +
        "<p>Total: {{totalPage}} pages with page size: </p>" +
        "<select @change='size=$event.target.value'>" +
        "<option value=10>10</option>" +
        "<option value=50 selected='selected'>50</option>" +
        "<option value=100>100</option>" +
        "</select>" +
        "</span>" +
        "</div>",
    watch: {
        index(index) {
            this.index = parseInt(index);
            this.$emit('change-page-number', this.index);
        },
        size(size) {
            this.size = parseInt(size);
            this.$emit('change-page-size', this.size);
        }
    },
    computed: {
        ids: function () {
            let width = 5;
            let half = parseInt(width / 2);
            if (this.totalPage <= 2) {
                return null;
            } else if (this.totalPage <= width) {
                return this.range(2, this.totalPage - 2);
            } else if (this.totalPage - width < 2) {
                return this.range(2, width - 1);
            } else if (this.index <= half + 1) {
                return this.range(2, width);
            } else if (this.totalPage - this.index <= half) {
                return this.range(this.totalPage - width, width);
            } else {
                return this.range(this.index - half, width);
            }
        }
    },
    methods: {
        range: function (startAt = 0, size) {
            return [...Array(size).keys()].map(i => i + startAt);
        },
    }
});

let disease_digester = new Vue({
    el: '#disease_digester',
    data: {
        diseases: [],
        geneSize: 10,
        maxGeneSize: null,
        analysisParameter: 'projection',
        projectToHuman: true,
        includeInteractors: false,
        redirectToReacFoam: true,
        nameKeyword: null,
        nameKeywords: null,
        classKeyword: null,
        classKeywords: null,
        first: null,
        last: null,
        pageSize: 50,
        offset: null,
        pageNumber: 1,
        showPageNumber: [],
        totalPage: null,
        totalElements: null,
        sort: 'gene',
        order: 'desc',
    },
    created: function () {
        axios.get('/overlay/disgenet/findAll?pageNumber=1&pageSize=50&geneSize=10&sort=gene&order=desc')
            .then(res => this.setData(res.data))
            .catch(err => console.log(err));
        axios.get('/overlay/disgenet/diseaseNameHintWord')
            .then(res => {
                this.nameKeywords = res.data.keywords;
            })
            .catch(err => console.log(err));
        axios.get('/overlay/disgenet/diseaseClassHintWord')
            .then(res => {
                this.classKeywords = res.data.keywords;
            })
            .catch(err => console.log(err));
        axios.get('/overlay/disgenet/getMaxGeneSize')
            .then(res => {
                // this.maxGeneSize = Math.log(res.data);
                this.maxGeneSize = res.data;
            })
            .catch(err => console.log(err));
    },
    // computed: {},
    // watch: {},
    methods: {
        setData: function (data) {
            this.diseases = data.content;
            this.first = data.first;
            this.last = data.last;
            this.pageSize = data.size;
            this.offset = data.pageable.offset;
            this.totalPage = data.totalPages;
            this.totalElements = data.totalElements;
            this.pageNumber = data.number + 1;
            if (this.diseases.length === 0) {
                window.alert('No entry in the query results has more than: ' + this.geneSize + ' genes( set as parameter in "Minimum number of genes per disease"), now this value will be set to one for showing the results!');
                this.geneSize = 1;
                this.refreshPageData();
            }
        },
        loadData: function (func = '/findAll', pageNumber, pageSize, geneSize, sort = 'gene', order = 'desc') {
            let url = null;
            if (func.match('/findAll')) {
                url = '/overlay/disgenet' + func + '?pageNumber=' + pageNumber + '&pageSize=' + pageSize + '&geneSize=' + geneSize + '&sort=' + sort + '&order=' + order;
            } else {
                url = '/overlay/disgenet' + func + '&pageNumber=' + pageNumber + '&pageSize=' + pageSize + '&geneSize=' + geneSize + '&sort=' + sort + '&order=' + order;
            }
            console.log(url);
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
        getGeneList(geneItems) {
            //do not directly return the `geneItems.sort().join(', ')`,or the Vue will arose: You may have an infinite update loop in a component render function.
            let geneList = [];
            for (let i = 0; i < geneItems.length; i++) {
                geneList.push(geneItems[i]);
            }
            return geneList.sort().join(', ');
        },
        async analyze(geneItems) {
            let data = {
                "projectToHuman": this.projectToHuman,
                "includeInteractors": this.includeInteractors,
                "redirectToReacFoam": this.redirectToReacFoam,
                "genes": geneItems
            };
            let url = this.redirectToReacFoam ?
                "https://reactome.org/reacfoam/" : "https://reactome.org/PathwayBrowser/";
            //to avoid the popup-blocker, must open a page immediately after the btn on-click event,
            // then await to set the true location from the response url,
            let analyzeWindow = window.open(url);
            analyzeWindow.focus();
            await axios.post('/overlay/disgenet/analyze', data)
                .then(res => {
                    url = res.data;
                }).catch(err => {
                    console.log(err)
                });
            analyzeWindow.location = url;
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
        changePageNumber(pageNumber) {
            // pageNumber = parseInt(pageNumber);
            // if (this.pageNumber < pageNumber && pageNumber < this.totalPage) {
            this.pageNumber = pageNumber;
            // } else if (pageNumber <= 1) {
            //     this.pageNumber = 1;
            // } else if (pageNumber >= this.totalPage) {
            //     this.pageNumber = this.totalPage;
            // }
            this.refreshPageData();
        },
        changePageSize(pageSize) {
            this.pageNumber = pageSize > this.pageSize ? 1 : this.pageNumber;
            this.pageSize = pageSize;
            this.refreshPageData();
        },
        // switchIfProjectToHuman() {
        //     this.projectToHuman = !this.projectToHuman;
        //     // console.log(this.projectToHuman + ' : ' + this.includeInteractors);
        // },
        switchIfIncludeInteractors() {
            this.includeInteractors = !this.includeInteractors;
            // console.log(this.projectToHuman + ' : ' + this.includeInteractors);
        },
        switchIfRedirectToReacFoam() {
            this.redirectToReacFoam = !this.redirectToReacFoam;
            // console.log(this.redirectToReacFoam);
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
            // console.log(this.sort);
            this.reverseOrderAndLoadData();
        },
        sortByDiseaseClass() {
            this.sort = 'class';
            // console.log(this.sort);
            this.reverseOrderAndLoadData();
        },
        sortByGeneNumber() {
            this.sort = 'gene';
            // console.log(this.sort);
            this.reverseOrderAndLoadData();
        },
        searchDiseaseName(nameKeyword) {
            this.nameKeyword = nameKeyword;
            this.classKeyword = null;
            // this.geneSize = 1;
            this.loadData('/findByDiseaseName?name=' + this.nameKeyword, 1, this.pageSize, this.geneSize, this.sort, this.order);
        },
        searchDiseaseClass(classKeyword) {
            this.classKeyword = classKeyword;
            this.nameKeyword = null;
            // this.geneSize = 1;
            //set the geneSize as one so that there can always have the results in the table when some entry may have less then the default geneSize
            this.loadData('/findByDiseaseClass?class=' + this.classKeyword, 1, this.pageSize, this.geneSize, this.sort, this.order);
        },
        resetKeyWord() {
            this.nameKeyword = null;
            this.classKeyword = null;
            this.geneSize = 10;
            this.refreshPageData();
        },
    },
});
export {disease_digester};