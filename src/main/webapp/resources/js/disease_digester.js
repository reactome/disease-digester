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
        axios.get('/overlay/disgenet/findAll?pageNumber=1&pageSize=40&geneSize=10&sort=disease&order=asc')
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
                url = '/overlay/disgenet' + func + '?pageNumber=' + pageNumber + '&pageSize=' + pageSize + '&geneSize=' + geneSize + '&sort=' + sort + '&order=' + order;
            } else {
                url = '/overlay/disgenet' + func + '&pageNumber=' + pageNumber + '&pageSize=' + pageSize + '&geneSize=' + geneSize + '&sort=' + sort + '&order=' + order;
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
                "redirectToReacFoam": this.redirectToReacFoam,
                "genes": this.genes
            };
            axios.post('/overlay/disgenet/analyze', data)
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
            // console.log(this.projectToHuman + ' : ' + this.includeInteractors);
        },
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
});
export {disease_digester};