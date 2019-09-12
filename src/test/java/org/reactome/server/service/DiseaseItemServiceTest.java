package org.reactome.server.service;

import org.reactome.server.TestBase;


public class DiseaseItemServiceTest extends TestBase {

//    @Autowired
//    DiseaseItemService diseaseItemService;
//    @Autowired
//    private DiseaseItemRepository diseaseItemRepository;
//
//    @Before
//    public void setUp() {
//        List<DiseaseItem> diseaseItems = Arrays.asList(
//                new DiseaseItem("dis1", "dis1Name", "cancer"),
//                new DiseaseItem("dis2", "dis2Name", "immune"));
//        diseaseItemRepository.saveAll(diseaseItems);
//    }
//
//    @After
//    public void cleanUp() {
//        diseaseItemRepository.deleteAll();
//    }
//
//    @Test
//    public void getPaginationResult() {
//        int pageNumber = 1;
//        int pageSize = 2;
//        long totalCount = 2;
//        String sortBy = "diseaseName";
//        String orderBy = "asc";
//        PaginationResult paginationResult = diseaseItemService.findAll(pageNumber, pageSize, sortBy, orderBy);
//        assertEquals(pageNumber, (long) paginationResult.getPageNumber());
//        assertEquals(pageSize, (long) paginationResult.getPageSize());
//        assertEquals(totalCount, (long) paginationResult.getTotalCount());
//        assertEquals(sortBy, paginationResult.getSortBy());
//        assertEquals(orderBy, paginationResult.getOrderBy());
////        assertEquals(0, paginationResult.getDiseaseItems().get(0).getGenes().size());
////        assertEquals(0, paginationResult.getDiseaseItems().get(1).getGenes().size());
//    }
//
//    @Test
//    public void getPaginationResultByDiseaseName() {
//        int pageNumber = 1;
//        int pageSize = 2;
//        long totalCount = 1;
//        String diseaseName = "dis1Name";
//        String sortBy = "diseaseName";
//        String orderBy = "asc";
//        PaginationResult paginationResult = diseaseItemService.findDiseaseItemsByDiseaseName(diseaseName, pageNumber, pageSize, sortBy, orderBy);
//        assertEquals(1, paginationResult.getDiseaseItems().size());
//        assertEquals(totalCount, (long) paginationResult.getTotalCount());
////        DiseaseItem diseaseItem = paginationResult.getDiseaseItems().get(0);
////        assertEquals(diseaseName, diseaseItem.getDiseaseName());
//    }
//
//    @Test
//    public void getPaginationResultByDiseaseClass() {
//        int pageNumber = 1;
//        int pageSize = 2;
//        long totalCount = 1;
//        String diseaseClass = "immune";
//        String sortBy = "diseaseName";
//        String orderBy = "asc";
//        PaginationResult paginationResult = diseaseItemService.findDiseaseItemsByDiseaseClass(diseaseClass, pageNumber, pageSize, sortBy, orderBy);
//        assertEquals(1, paginationResult.getDiseaseItems().size());
//        assertEquals(totalCount, (long) paginationResult.getTotalCount());
//        DiseaseItem diseaseItem = paginationResult.getDiseaseItems().get(0);
//        assertEquals(diseaseClass, diseaseItem.getDiseaseClass());
//    }
}