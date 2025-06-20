package gov.nih.nci.bento_ri.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gov.nih.nci.bento.model.AbstractPrivateESDataFetcher;
import gov.nih.nci.bento.model.search.mapper.TypeMapperImpl;
import gov.nih.nci.bento.model.search.mapper.TypeMapperService;
import gov.nih.nci.bento.service.ESService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.client.Request;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

@Component
public class PrivateESDataFetcher extends AbstractPrivateESDataFetcher {
    private static final Logger logger = LogManager.getLogger(PrivateESDataFetcher.class);
    private final TypeMapperService typeMapper = new TypeMapperImpl();

    final String PAGE_SIZE = "first";
    final String OFFSET = "offset";
    final String ORDER_BY = "order_by";
    final String SORT_DIRECTION = "sort_direction";

    final String STUDIES_END_POINT = "/studies/_search";
    final String STUDIES_COUNT_END_POINT = "/studies/_count";

    final String SUBJECTS_END_POINT = "/subjects/_search";
    final String SUBJECTS_COUNT_END_POINT = "/subjects/_count";
    final String SUBJECT_IDS_END_POINT = "/subject_ids/_search";
    final String SAMPLES_END_POINT = "/samples/_search";
    final String SAMPLES_COUNT_END_POINT = "/samples/_count";
    final String FILES_END_POINT = "/files/_search";
    final String STUDY_DATA_TYPES_END_POINT = "/study_data_types/_search";
    final String FILES_EXPERIMENTAL_STRATEGY_END_POINT = "/file_experimental_strategies/_search";
    final String FILES_COUNT_END_POINT = "/files/_count";
    final String PROGRAMS_END_POINT = "/programs/_search";
    final String PROGRAMS_COUNT_END_POINT = "/programs/_count";
    final String NODES_END_POINT = "/model_nodes/_search";
    final String NODES_COUNT_END_POINT = "/model_nodes/_count";
    final String PROPERTIES_END_POINT = "/model_properties/_search";
    final String PROPERTIES_COUNT_END_POINT = "/model_properties/_count";
    final String VALUES_END_POINT = "/model_values/_search";
    final String VALUES_COUNT_END_POINT = "/model_values/_count";
    final String GS_ABOUT_END_POINT = "/about_page/_search";
    final String GS_MODEL_END_POINT = "/data_model/_search";
    final String VERSION_END_POINT = "/version/_search";

    final int GS_LIMIT = 10;
    final String GS_END_POINT = "endpoint";
    final String GS_COUNT_ENDPOINT = "count_endpoint";
    final String GS_RESULT_FIELD = "result_field";
    final String GS_COUNT_RESULT_FIELD = "count_result_field";
    final String GS_SEARCH_FIELD = "search_field";
    final String GS_COLLECT_FIELDS = "collect_fields";
    final String GS_SORT_FIELD = "sort_field";
    final String GS_CATEGORY_TYPE = "type";
    final String GS_ABOUT = "about";
    final String GS_HIGHLIGHT_FIELDS = "highlight_fields";
    final String GS_HIGHLIGHT_DELIMITER = "$";
    final Set<String> RANGE_PARAMS = Set.of("number_of_study_participants", "number_of_study_samples");

    private MemgraphDataFetcher memgraphDataFetcher;

    public PrivateESDataFetcher(ESService esService, MemgraphDataFetcher memgraphDataFetcher) {
        super(esService);
        HashMap<String, String> file_ids_map;
    }


    Map<String, Object> searchSubjects(Map<String, Object> params) throws IOException {
        final String AGG_NAME = "agg_name";
        final String AGG_ENDPOINT = "agg_endpoint";
        final String WIDGET_QUERY = "widgetQueryName";
        final String FILTER_COUNT_QUERY = "filterCountQueryName";
        // Query related values
        final List<Map<String, String>> TERM_AGGS = new ArrayList<>();
        TERM_AGGS.add(Map.of(
                AGG_NAME, "studies",
                WIDGET_QUERY, "subjectCountByStudy",
                FILTER_COUNT_QUERY, "filterSubjectCountByStudy",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "experimental_strategies",
                WIDGET_QUERY, "subjectCountByExperimentalStrategy",
                FILTER_COUNT_QUERY, "filterSubjectCountByExperimentalStrategy",
                AGG_ENDPOINT, FILES_EXPERIMENTAL_STRATEGY_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "accesses",
                WIDGET_QUERY, "subjectCountByAccess",
                FILTER_COUNT_QUERY, "filterSubjectCountByAccess",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "sex",
                WIDGET_QUERY, "subjectCountBySex",
                FILTER_COUNT_QUERY, "filterSubjectCountBySex",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "is_tumor",
                WIDGET_QUERY,"subjectCountByIsTumor",
                FILTER_COUNT_QUERY, "filterSubjectCountByIsTumor",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "analyte_type",
                WIDGET_QUERY, "subjectCountByAnalyteType",
                FILTER_COUNT_QUERY, "filterSubjectCountByAnalyteType",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "file_types",
                WIDGET_QUERY, "subjectCountByFileType",
                FILTER_COUNT_QUERY, "filterSubjectCountByFileType",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "site",
                WIDGET_QUERY, "subjectCountByDiseaseSite",
                FILTER_COUNT_QUERY, "filterSubjectCountByDiseaseSite",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "library_strategies",
                WIDGET_QUERY, "subjectCountByLibraryStrategy",
                FILTER_COUNT_QUERY, "filterSubjectCountByLibraryStrategy",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "library_source_materials",
                WIDGET_QUERY, "subjectCountByLibrarySourceMaterial",
                FILTER_COUNT_QUERY, "filterSubjectCountByLibrarySourceMaterial",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "library_source_molecules",
                WIDGET_QUERY, "subjectCountByLibrarySourceMolecule",
                FILTER_COUNT_QUERY, "filterSubjectCountByLibrarySourceMolecule",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "library_selections",
                WIDGET_QUERY, "subjectCountByLibrarySelection",
                FILTER_COUNT_QUERY, "filterSubjectCountByLibrarySelection",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "library_layouts",
                WIDGET_QUERY, "subjectCountByLibraryLayout",
                FILTER_COUNT_QUERY, "filterSubjectCountByLibraryLayout",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "platforms",
                WIDGET_QUERY, "subjectCountByPlatform",
                FILTER_COUNT_QUERY, "filterSubjectCountByPlatform",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "instrument_models",
                WIDGET_QUERY, "subjectCountByInstrumentModel",
                FILTER_COUNT_QUERY, "filterSubjectCountByInstrumentModel",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "reference_genome_assemblies",
                WIDGET_QUERY, "subjectCountByReferenceGenomeAssembly",
                FILTER_COUNT_QUERY, "filterSubjectCountByReferenceGenomeAssembly",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "primary_diagnoses",
                WIDGET_QUERY, "subjectCountByPrimaryDiagnosis",
                FILTER_COUNT_QUERY, "filterSubjectCountByPrimaryDiagnosis",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "phs_accession",
                WIDGET_QUERY, "subjectCountByPhsAccession",
                FILTER_COUNT_QUERY, "filterSubjectCountByPhsAccession",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "study_data_types",
                WIDGET_QUERY, "subjectCountByStudyDataType",
                FILTER_COUNT_QUERY, "filterSubjectCountByStudyDataType",
                AGG_ENDPOINT, STUDY_DATA_TYPES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "acl",
                WIDGET_QUERY, "subjectCountByAcl",
                FILTER_COUNT_QUERY, "filterSubjectCountByAcl",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "sample_types",
                WIDGET_QUERY, "subjectCountBySampleType",
                FILTER_COUNT_QUERY, "filterSubjectCountBySampleType",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "image_modality",
                WIDGET_QUERY, "subjectCountByImageModality",
                FILTER_COUNT_QUERY, "filterSubjectCountByImageModality",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "analytical_fractions",
                WIDGET_QUERY, "subjectCountByAnalyticalFractions",
                FILTER_COUNT_QUERY, "filterSubjectCountByAnalyticalFractions",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "instrument_makes",
                WIDGET_QUERY, "subjectCountByInstrumentMakes",
                FILTER_COUNT_QUERY, "filterSubjectCountByInstrumentMakes",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "proteomic_design_descriptions",
                WIDGET_QUERY, "subjectCountByProteomicDesignDescriptions",
                FILTER_COUNT_QUERY, "filterSubjectCountByProteomicDesignDescriptions",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "tissue_fixative",
                WIDGET_QUERY, "subjectCountByTissueFixative",
                FILTER_COUNT_QUERY, "filterSubjectCountByTissueFixative",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "imaging_assay_type",
                WIDGET_QUERY, "subjectCountByImagingAssayType",
                FILTER_COUNT_QUERY, "filterSubjectCountByImagingAssayType",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "organ_or_tissue",
                WIDGET_QUERY, "subjectCountByOrganOrTissue",
                FILTER_COUNT_QUERY, "filterSubjectCountByOrganOrTissue",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "is_supplementary_file",
                WIDGET_QUERY, "subjectCountByIsSupplementaryFile",
                FILTER_COUNT_QUERY, "filterSubjectCountByIsSupplementaryFile",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        // Donut Count Fields
        TERM_AGGS.add(Map.of(
                AGG_NAME, "experimental_strategies",
                WIDGET_QUERY, "donutCountByExperimentalStrategy",
                AGG_ENDPOINT, FILES_EXPERIMENTAL_STRATEGY_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "sex",
                WIDGET_QUERY, "donutCountBySex",
                AGG_ENDPOINT, SUBJECTS_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "file_types",
                WIDGET_QUERY, "donutCountByFileType",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "study_data_types",
                WIDGET_QUERY, "donutCountByStudyDataTypes",
                AGG_ENDPOINT, STUDY_DATA_TYPES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "image_modality",
                WIDGET_QUERY, "donutCountByImageModality",
                AGG_ENDPOINT, FILES_END_POINT
        ));
        TERM_AGGS.add(Map.of(
                AGG_NAME, "sample_types",
                WIDGET_QUERY, "donutCountBySampleType",
                AGG_ENDPOINT, SAMPLES_END_POINT
        ));


        List<String> agg_names = new ArrayList<>();
        for (var agg: TERM_AGGS) {
            agg_names.add(agg.get(AGG_NAME));
        }
        final String[] TERM_AGG_NAMES = agg_names.toArray(new String[TERM_AGGS.size()]);

        Map<String, Object> query = esService.buildFacetFilterQuery(params, RANGE_PARAMS);
        Request sampleCountRequest = new Request("GET", SAMPLES_COUNT_END_POINT);
        sampleCountRequest.setJsonEntity(gson.toJson(query));
        JsonObject sampleCountResult = esService.send(sampleCountRequest);
        int numberOfSamples = sampleCountResult.get("count").getAsInt();

        Request fileCountRequest = new Request("GET", FILES_COUNT_END_POINT);
        fileCountRequest.setJsonEntity(gson.toJson(query));
        JsonObject fileCountResult = esService.send(fileCountRequest);
        int numberOfFiles = fileCountResult.get("count").getAsInt();

        Request subjectCountRequest = new Request("GET", SUBJECTS_COUNT_END_POINT);
        subjectCountRequest.setJsonEntity(gson.toJson(query));
        JsonObject subjectCountResult = esService.send(subjectCountRequest);
        int numberOfSubjects = subjectCountResult.get("count").getAsInt();


        // Get aggregations
        Map<String, Object> aggQuery = esService.addAggregations(query, TERM_AGG_NAMES);
        Request fileRequest = new Request("GET", FILES_END_POINT);
        fileRequest.setJsonEntity(gson.toJson(aggQuery));
        JsonObject subjectResult = esService.send(fileRequest);
        Map<String, JsonArray> aggs = esService.collectTermAggs(subjectResult, TERM_AGG_NAMES);

        Map<String, Object> data = new HashMap<>();
        data.put("numberOfStudies", aggs.get("phs_accession").size());
        data.put("numberOfSubjects", numberOfSubjects);
        data.put("numberOfSamples", numberOfSamples);
        data.put("numberOfFiles", numberOfFiles);
        data.put("numberOfDiseaseSites", aggs.get("site").size());

        // widgets data and facet filter counts
        for (var agg: TERM_AGGS) {
            String field = agg.get(AGG_NAME);
            String widgetQueryName = agg.get(WIDGET_QUERY);
            String filterCountQueryName = agg.get(FILTER_COUNT_QUERY);
            String endpoint = agg.get(AGG_ENDPOINT);
            // subjectCountByXXXX
            List<Map<String, Object>> widgetData;
            if (endpoint.equals(FILES_END_POINT)) {
                widgetData = getGroupCountHelper(aggs.get(field));
                data.put(widgetQueryName, widgetData);
            } else {
                widgetData = subjectCountBy(field, params, endpoint);;
                data.put(widgetQueryName, widgetData);
            }
            // filterSubjectCountByXXXX
            if (params.containsKey(field) && ((List<String>)params.get(field)).size() > 0) {
                List<Map<String, Object>> filterCount = filterSubjectCountBy(field, params, endpoint);;
                data.put(filterCountQueryName, filterCount);
            } else {
                data.put(filterCountQueryName, widgetData);
            }
        }

        final Map<String, String> RANGE_MAXIMUMS = new HashMap<>();
        RANGE_MAXIMUMS.put("number_of_participants",  "filterSubjectCountByNumberOfStudyParticipants");
        RANGE_MAXIMUMS.put("number_of_samples",  "filterSubjectCountByNumberOfStudySamples");
        final String[] RANGE_MAX_NAMES = RANGE_MAXIMUMS.keySet().toArray(new String[0]);

        Map<String, Object> maximumQuery = esService.buildMaximumQuery(RANGE_MAX_NAMES);
        Request maxRequest = new Request("GET", STUDIES_END_POINT);
        maxRequest.setJsonEntity(gson.toJson(maximumQuery));
        JsonObject maxResult = esService.send(maxRequest);
        Map<String, Integer> maximumValues = esService.collectMaximumAggs(maxResult, RANGE_MAX_NAMES);
        for (String field: RANGE_MAX_NAMES) {
            data.put(RANGE_MAXIMUMS.get(field), Map.of("lowerBound", 0,"upperBound", maximumValues.get(field)));
        }
        return data;
    }

    List<Map<String, Object>> subjectOverview(Map<String, Object> params) throws IOException {
        final String[][] PROPERTIES = new String[][]{
                new String[]{"subject_id", "subject_ids"},
                new String[]{"study_acronym", "studies"},
                new String[]{"phs_accession", "phs_accession"},
                new String[]{"sex", "sex"},
                new String[]{"site", "site"},
                new String[]{"samples", "samples"},
                new String[]{"files", "files"},
                new String[]{"analyte_type", "analyte_type"},
                new String[]{"race", "race"},
                new String[]{"ethnicity", "ethnicity"},
                new String[]{"primary_diagnosis", "primary_diagnoses"}
        };

        String defaultSort = "subject_ids"; // Default sort order

        Map<String, String> sortFieldMapping = Map.ofEntries(
                Map.entry("subject_id", "subject_ids"),
                Map.entry("study_acronym", "studies"),
                Map.entry("phs_accession", "phs_accession"),
                Map.entry("sex", "sex"),
                Map.entry("site", "site_sort"),
                Map.entry("analyte_type", "sample_types_sort"),
                Map.entry("race", "race"),
                Map.entry("ethnicity", "ethnicity"),
                Map.entry("primary_diagnosis", "primary_diagnoses_sort"),
                Map.entry("image_modality", "image_modality_sort"),
                Map.entry("organ_or_tissue", "organ_or_tissue_sort"),
                Map.entry("imaging_assay_type", "imaging_assay_type_sort"),
                Map.entry("tissue_fixative", "tissue_fixative_sort")
        );

        return overview(SUBJECTS_END_POINT, params, PROPERTIES, defaultSort, sortFieldMapping);
    }

    List<Map<String, Object>> sampleOverview(Map<String, Object> params) throws IOException {
        final String[][] PROPERTIES = new String[][]{
                new String[]{"study_acronym", "studies"},
                new String[]{"phs_accession", "phs_accession"},
                new String[]{"subject_id", "subject_ids"},
                new String[]{"sample_id", "sample_id"},
                new String[]{"is_tumor", "is_tumor"},
                new String[]{"analyte_type", "analyte_type"},
                new String[]{"files", "files"},
                new String[]{"sample_type", "analyte_type"},
                new String[]{"sample_tumor_status", "is_tumor"},
                new String[]{"organ_or_tissue", "organ_or_tissue"}
        };

        String defaultSort = "sample_id"; // Default sort order

        Map<String, String> sortFieldMapping = Map.ofEntries(
                Map.entry("study_acronym", "studies"),
                Map.entry("phs_accession", "phs_accession"),
                Map.entry("subject_id", "subject_ids"),
                Map.entry("sample_id", "sample_id"),
                Map.entry("is_tumor", "is_tumor"),
                Map.entry("analyte_type", "analyte_type"),
                Map.entry("sample_type", "analyte_type"),
                Map.entry("sample_tumor_status", "is_tumor"),
                Map.entry("image_modality", "image_modality_sort"),
                Map.entry("organ_or_tissue", "organ_or_tissue_sort"),
                Map.entry("imaging_assay_type", "imaging_assay_type_sort"),
                Map.entry("tissue_fixative", "tissue_fixative_sort")
        );

        return overview(SAMPLES_END_POINT, params, PROPERTIES, defaultSort, sortFieldMapping);
    }

    List<Map<String, Object>> fileOverview(Map<String, Object> params) throws IOException {
        // Following String array of arrays should be in form of "GraphQL_field_name", "ES_field_name"
        final String[][] PROPERTIES = new String[][]{
            new String[]{"study_acronym", "studies"},
            new String[]{"accesses", "accesses"},
            new String[]{"phs_accession", "phs_accession"},
            new String[]{"subject_id", "subject_ids"},
            new String[]{"sample_id", "sample_id"},
            new String[]{"sample_types", "sample_types"},
            new String[]{"experimental_strategy", "experimental_strategies"},
            new String[]{"sex", "sex"},
            new String[]{"race", "races"},
            new String[]{"primary_diagnoses", "primary_diagnoses"},
            new String[]{"analyte_type", "analyte_type"},
            new String[]{"is_tumor", "is_tumor"},
            new String[]{"file_name", "file_name"},
            new String[]{"file_type", "file_type"},
            new String[]{"file_size", "file_size"},
            new String[]{"file_id", "file_id"},
            new String[]{"md5sum", "md5sum"},
            new String[]{"study_data_type", "study_data_types"},
            new String[]{"library_strategy", "library_strategies"},
            new String[]{"library_layouts", "library_layouts"},
            new String[]{"image_modality", "image_modality"},
            new String[]{"organ_or_tissue", "organ_or_tissue"},
            new String[]{"release_datetime", "release_datetime"},
            new String[]{"is_supplementary_file", "is_supplementary_file"},
            new String[]{"supplementary_file_names", "supplementary_file_names"}
    };

        String defaultSort = "file_name"; // Default sort order
        
        Map<String, String> sortFieldMapping = Map.ofEntries(
                Map.entry("study_acronym", "studies"),
                Map.entry("accesses", "accesses"),
                Map.entry("phs_accession", "phs_accession"),
                Map.entry("subject_id", "subject_ids"),
                Map.entry("sample_id", "sample_id"),
                Map.entry("experimental_strategy", "experimental_strategies_sort"),
                Map.entry("sex", "sex_sort"),
                Map.entry("race", "races_sort"),
                Map.entry("primary_diagnoses", "primary_diagnoses_sort"),
                Map.entry("analyte_type", "analyte_type_sort"),
                Map.entry("is_tumor", "is_tumor_sort"),
                Map.entry("file_name", "file_name"),
                Map.entry("file_type", "file_type"),
                Map.entry("file_size", "file_size"),
                Map.entry("file_id", "file_id"),
                Map.entry("md5sum", "md5sum"),
                Map.entry("study_data_type", "study_data_types"),
                Map.entry("library_strategy", "library_strategies_sort"),
                Map.entry("library_layouts", "library_layouts_sort"),
                Map.entry("image_modality", "image_modality_sort"),
                Map.entry("organ_or_tissue", "organ_or_tissue_sort"),
                Map.entry("imaging_assay_type", "imaging_assay_type_sort"),
                Map.entry("tissue_fixative", "tissue_fixative_sort"),
                Map.entry("is_supplementary_file", "is_supplementary_file"),
                Map.entry("supplementary_file_names", "supplementary_file_names_sort")
        );

        List<Map<String, Object>> fileOverview = overview(FILES_END_POINT, params, PROPERTIES, defaultSort, sortFieldMapping);
        return fileOverview;
    }

    private List<Map<String, Object>> overview(String endpoint, Map<String, Object> params, String[][] properties, String defaultSort, Map<String, String> mapping) throws IOException {

        Request request = new Request("GET", endpoint);
        Map<String, Object> query = esService.buildFacetFilterQuery(params, RANGE_PARAMS, Set.of(PAGE_SIZE, OFFSET, ORDER_BY, SORT_DIRECTION));
        String order_by = (String)params.get(ORDER_BY);
        String direction = ((String)params.get(SORT_DIRECTION)).toLowerCase();
        query.put("sort", mapSortOrder(order_by, direction, defaultSort, mapping));
        int pageSize = (int) params.get(PAGE_SIZE);
        int offset = (int) params.get(OFFSET);
        List<Map<String, Object>> page = esService.collectPage(request, query, properties, pageSize, offset);
        return page;
    }

    private Map<String, String> mapSortOrder(String order_by, String direction, String defaultSort, Map<String, String> mapping) {
        String sortDirection = direction;
        if (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc")) {
            sortDirection = "asc";
        }

        String sortOrder = defaultSort; // Default sort order
        if (mapping.containsKey(order_by)) {
            sortOrder = mapping.get(order_by);
        } else {
            logger.info("Order: \"" + order_by + "\" not recognized, use default order");
        }
        return Map.of(sortOrder, sortDirection);
    }

    private List<Map<String, Object>> armsByPrograms(Map<String, Object> params) throws IOException {
        final String category = "programs";
        final String subCategory = "study_acronym";

        String[] subCategories = new String[] { subCategory };
        Map<String, Object> query = esService.buildFacetFilterQuery(params, Set.of(), Set.of(PAGE_SIZE));
        String[] AGG_NAMES = new String[] {category};
        query = esService.addAggregations(query, AGG_NAMES);
        esService.addSubAggregations(query, category, subCategories);
        Request request = new Request("GET", SUBJECTS_END_POINT);
        request.setJsonEntity(gson.toJson(query));
        JsonObject jsonObject = esService.send(request);
        Map<String, JsonArray> aggs = esService.collectTermAggs(jsonObject, AGG_NAMES);
        JsonArray buckets = aggs.get(category);

        List<Map<String, Object>> data = new ArrayList<>();
        for (JsonElement group: buckets) {
            List<Map<String, Object>> studies = new ArrayList<>();

            for (JsonElement studyElement: group.getAsJsonObject().get(subCategory).getAsJsonObject().get("buckets").getAsJsonArray()) {
                JsonObject study = studyElement.getAsJsonObject();
                int size = study.get("doc_count").getAsInt();
                studies.add(Map.of(
                        "arm", study.get("key").getAsString(),
                        "caseSize", size,
                        "size", size
                ));
            }
            data.add(Map.of("program", group.getAsJsonObject().get("key").getAsString(),
                    "caseSize", group.getAsJsonObject().get("doc_count").getAsInt(),
                    "children", studies
            ));

        }
        return data;
    }

    private List<Map<String, Object>> subjectCountBy(String category, Map<String, Object> params, String endpoint) throws IOException {
        Map<String, Object> query = esService.buildFacetFilterQuery(params, Set.of(), Set.of(PAGE_SIZE));
        return getGroupCount(category, query, endpoint);
    }

    private List<Map<String, Object>> filterSubjectCountBy(String category, Map<String, Object> params, String endpoint) throws IOException {
        Map<String, Object> query = esService.buildFacetFilterQuery(params, Set.of(), Set.of(PAGE_SIZE, category));
        return getGroupCount(category, query, endpoint);
    }

    private List<Map<String, Object>> getGroupCount(String category, Map<String, Object> query, String endpoint) throws IOException {
        String[] AGG_NAMES = new String[] {category};
        query = esService.addAggregations(query, AGG_NAMES);
        Request request = new Request("GET", endpoint);
        request.setJsonEntity(gson.toJson(query));
        JsonObject jsonObject = esService.send(request);
        Map<String, JsonArray> aggs = esService.collectTermAggs(jsonObject, AGG_NAMES);
        JsonArray buckets = aggs.get(category);

        return getGroupCountHelper(buckets);
    }

    private List<Map<String, Object>> getGroupCountHelper(JsonArray buckets) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        for (JsonElement group: buckets) {
            data.add(Map.of("group", group.getAsJsonObject().get("key").getAsString(),
                    "subjects", group.getAsJsonObject().get("doc_count").getAsInt()
            ));
        }
        return data;
    }

    private Map<String, Object> rangeFilterSubjectCountBy(String category, Map<String, Object> params) throws IOException {
        Map<String, Object> query = esService.buildFacetFilterQuery(params, Set.of(), Set.of(PAGE_SIZE, category));
        return getRangeCount(category, query);
    }

    private Map<String, Object> getRangeCount(String category, Map<String, Object> query) throws IOException {
        String[] AGG_NAMES = new String[] {category};
        query = esService.addAggregations(query, new String[]{}, AGG_NAMES);
        Request request = new Request("GET", SUBJECTS_END_POINT);
        request.setJsonEntity(gson.toJson(query));
        JsonObject jsonObject = esService.send(request);
        Map<String, JsonObject> aggs = esService.collectRangeAggs(jsonObject, AGG_NAMES);
        return getRange(aggs.get(category).getAsJsonObject());
    }

    private Map<String, Object> getRange(JsonObject aggs) {
        final String LOWER_BOUND = "lowerBound";
        final String UPPER_BOUND = "upperBound";
        Map<String, Object> range = new HashMap<>();
        range.put("subjects", aggs.get("count").getAsInt());
        JsonElement lowerBound = aggs.get("min");
        if (!lowerBound.isJsonNull()) {
            range.put(LOWER_BOUND, lowerBound.getAsDouble());
        } else {
            range.put(LOWER_BOUND, null);
        }
        JsonElement upperBound = aggs.get("max");
        if (!upperBound.isJsonNull()) {
            range.put("upperBound", aggs.get("max").getAsDouble());
        } else {
            range.put(UPPER_BOUND, null);
        }

        return range;
    }

    Map<String, Object> globalSearch(Map<String, Object> params) throws IOException {
        Map<String, Object> result = new HashMap<>();
        String input = (String) params.get("input");
        int size = (int) params.get("first");
        int offset = (int) params.get("offset");
        List<Map<String, Object>> searchCategories = new ArrayList<>();
        searchCategories.add(Map.of(
                GS_END_POINT, STUDIES_END_POINT,
                GS_COUNT_ENDPOINT, STUDIES_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "study_count",
                GS_RESULT_FIELD, "studies",
                GS_SEARCH_FIELD, List.of("phs_accession_gs", "study_name_gs", "study_code_gs", "study_data_types_gs"),
                GS_SORT_FIELD, "phs_accession",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"phs_accession", "phs_accession_gs"},
                        new String[]{"study_code", "study_code_gs"},
                        new String[]{"study_name", "study_name_gs"},
                        new String[]{"study_data_types", "study_data_types_gs"}
                },
                GS_CATEGORY_TYPE, "study"

        ));
        searchCategories.add(Map.of(
                GS_END_POINT, SUBJECTS_END_POINT,
                GS_COUNT_ENDPOINT, SUBJECTS_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "subject_count",
                GS_RESULT_FIELD, "subjects",
                GS_SEARCH_FIELD, List.of("study_gs", "subject_id_gs", "site_gs", "sex_gs"),
                GS_SORT_FIELD, "subject_ids",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"study", "study_gs"},
                        new String[]{"subject_id", "subject_id_gs"},
                        new String[]{"site", "site_gs"},
                        new String[]{"sex", "sex_gs"},
                        new String[]{"subject_ids_filter", "subject_ids_filter"},
                },
                GS_CATEGORY_TYPE, "subject"
        ));
        searchCategories.add(Map.of(
                GS_END_POINT, SAMPLES_END_POINT,
                GS_COUNT_ENDPOINT, SAMPLES_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "sample_count",
                GS_RESULT_FIELD, "samples",
                GS_SEARCH_FIELD, List.of("sample_id_gs", "is_tumor_gs", "analyte_type_gs"),
                GS_SORT_FIELD, "sample_id",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"sample_id", "sample_id_gs"},
                        new String[]{"is_tumor", "is_tumor_gs"},
                        new String[]{"analyte_type", "analyte_type_gs"},
                        new String[]{"subject_ids_filter", "subject_ids_filter"}
                },
                GS_CATEGORY_TYPE, "sample"
        ));
        searchCategories.add(Map.of(
                GS_END_POINT, FILES_END_POINT,
                GS_COUNT_ENDPOINT, FILES_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "file_count",
                GS_RESULT_FIELD, "files",
                GS_SEARCH_FIELD, List.of("subject_id_gs","sample_id_gs","file_id_gs","file_name_gs",
                        "file_type_gs","accesses_gs","acl_gs","experimental_strategies_gs","instrument_models_gs",
                        "library_layouts_gs","library_selections_gs","library_source_materials_gs","library_source_molecules_gs","library_strategies_gs",
                        "platforms_gs","reference_genome_assemblies_gs","sites_gs", "is_supplementary_file_gs"),
                GS_SORT_FIELD, "file_id",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"subject_id", "subject_id_gs"},
                        new String[]{"sample_id", "sample_id_gs"},
                        new String[]{"file_id", "file_id_gs"},
                        new String[]{"file_name", "file_name_gs"},
                        new String[]{"file_type", "file_type_gs"},
                        new String[]{"accesses", "accesses_gs"},
                        new String[]{"acl","acl_gs"},
                        new String[]{"experimental_strategies","experimental_strategies_gs"},
                        new String[]{"instrument_models","instrument_models_gs"},
                        new String[]{"library_layouts","library_layouts_gs"},
                        new String[]{"library_selections","library_selections_gs"},
                        new String[]{"library_source_materials","library_source_materials_gs"},
                        new String[]{"library_source_molecules", "library_source_molecules_gs"},
                        new String[]{"library_strategies","library_strategies_gs"},
                        new String[]{"platforms","platforms_gs"},
                        new String[]{"reference_genome_assemblies","reference_genome_assemblies_gs"},
                        new String[]{"sites","sites_gs"},
                        new String[]{"subject_ids_filter", "subject_ids_filter"},
                        new String[]{"is_supplementary_file", "is_supplementary_file_gs"}
                },
                GS_CATEGORY_TYPE, "file"
        ));
        searchCategories.add(Map.of(
                GS_END_POINT, PROGRAMS_END_POINT,
                GS_COUNT_ENDPOINT, PROGRAMS_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "program_count",
                GS_RESULT_FIELD, "programs",
                GS_SEARCH_FIELD, List.of("program_name_gs", "program_short_description_gs", "program_full_description_gs",
                        "program_external_url_gs", "program_sort_order_gs"),
                GS_SORT_FIELD, "program_sort_order",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"program_name", "program_name_gs"},
                        new String[]{"program_short_description", "program_short_description_gs"},
                        new String[]{"program_full_description", "program_full_description_gs"},
                        new String[]{"program_external_url", "program_external_url_gs"},
                        new String[]{"program_sort_order", "program_sort_order_gs"},
                        new String[]{"type", "type_gs"}
                },
                GS_CATEGORY_TYPE, "program"
        ));
        searchCategories.add(Map.of(
                GS_END_POINT, NODES_END_POINT,
                GS_COUNT_ENDPOINT, NODES_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "model_count",
                GS_RESULT_FIELD, "model",
                GS_SEARCH_FIELD, List.of("node"),
                GS_SORT_FIELD, "node_kw",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"node_name", "node"}
                },
                GS_HIGHLIGHT_FIELDS, new String[][] {
                        new String[]{"highlight", "node"}
                },
                GS_CATEGORY_TYPE, "node"
        ));
        searchCategories.add(Map.of(
                GS_END_POINT, PROPERTIES_END_POINT,
                GS_COUNT_ENDPOINT, PROPERTIES_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "model_count",
                GS_RESULT_FIELD, "model",
                GS_SEARCH_FIELD, List.of("property", "property_description", "property_type", "property_required"),
                GS_SORT_FIELD, "property_kw",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"node_name", "node"},
                        new String[]{"property_name", "property"},
                        new String[]{"property_type", "property_type"},
                        new String[]{"property_required", "property_required"},
                        new String[]{"property_description", "property_description"}
                },
                GS_HIGHLIGHT_FIELDS, new String[][] {
                        new String[]{"highlight", "property"},
                        new String[]{"highlight", "property_description"},
                        new String[]{"highlight", "property_type"},
                        new String[]{"highlight", "property_required"}
                },
                GS_CATEGORY_TYPE, "property"
        ));
        searchCategories.add(Map.of(
                GS_END_POINT, VALUES_END_POINT,
                GS_COUNT_ENDPOINT, VALUES_COUNT_END_POINT,
                GS_COUNT_RESULT_FIELD, "model_count",
                GS_RESULT_FIELD, "model",
                GS_SEARCH_FIELD, List.of("value"),
                GS_SORT_FIELD, "value_kw",
                GS_COLLECT_FIELDS, new String[][]{
                        new String[]{"node_name", "node"},
                        new String[]{"property_name", "property"},
                        new String[]{"property_type", "property_type"},
                        new String[]{"property_required", "property_required"},
                        new String[]{"property_description", "property_description"},
                        new String[]{"value", "value"}
                },
                GS_HIGHLIGHT_FIELDS, new String[][] {
                        new String[]{"highlight", "value"}
                },
                GS_CATEGORY_TYPE, "value"
        ));

        Set<String> combinedCategories = Set.of("model") ;

        for (Map<String, Object> category: searchCategories) {
            String countResultFieldName = (String) category.get(GS_COUNT_RESULT_FIELD);
            String resultFieldName = (String) category.get(GS_RESULT_FIELD);
            String[][] properties = (String[][]) category.get(GS_COLLECT_FIELDS);
            String[][] highlights = (String[][]) category.get(GS_HIGHLIGHT_FIELDS);
            Map<String, Object> query = getGlobalSearchQuery(input, category);

            // Get count
            Request countRequest = new Request("GET", (String) category.get(GS_COUNT_ENDPOINT));
            countRequest.setJsonEntity(gson.toJson(query));
            JsonObject countResult = esService.send(countRequest);
            int oldCount = (int)result.getOrDefault(countResultFieldName, 0);
            result.put(countResultFieldName, countResult.get("count").getAsInt() + oldCount);

            // Get results
            Request request = new Request("GET", (String)category.get(GS_END_POINT));
            String sortFieldName = (String)category.get(GS_SORT_FIELD);
            query.put("sort", Map.of(sortFieldName, "asc"));
            query = addHighlight(query, category);

            if (combinedCategories.contains(resultFieldName)) {
                query.put("size", ESService.MAX_ES_SIZE);
                query.put("from", 0);
            } else {
                query.put("size", size);
                query.put("from", offset);
            }
            request.setJsonEntity(gson.toJson(query));
            JsonObject jsonObject = esService.send(request);
            List<Map<String, Object>> objects = esService.collectPage(jsonObject, properties, highlights, (int)query.get("size"), 0);

            for (var object: objects) {
                object.put(GS_CATEGORY_TYPE, category.get(GS_CATEGORY_TYPE));
            }

            List<Map<String, Object>> existingObjects = (List<Map<String, Object>>)result.getOrDefault(resultFieldName, null);
            if (existingObjects != null) {
                existingObjects.addAll(objects);
                result.put(resultFieldName, existingObjects);
            } else {
                result.put(resultFieldName, objects);
            }

        }

        List<Map<String, String>> about_results = searchAboutPage(input);
        int about_count = about_results.size();
        result.put("about_count", about_count);
        result.put("about_page", paginate(about_results, size, offset));

        for (String category: combinedCategories) {
            List<Object> pagedCategory = paginate((List)result.get(category), size, offset);
            result.put(category, pagedCategory);
        }

        return result;
    }

    private List paginate(List org, int pageSize, int offset) {
        List<Object> result = new ArrayList<>();
        int size = org.size();
        if (offset <= size -1) {
            int end_index = offset + pageSize;
            if (end_index > size) {
                end_index = size;
            }
            result = org.subList(offset, end_index);
        }
        return result;
    }

    private List<Map<String, String>> searchAboutPage(String input) throws IOException {
        final String ABOUT_CONTENT = "content.paragraph";
        Map<String, Object> query = Map.of(
                "query", Map.of("match", Map.of(ABOUT_CONTENT, input)),
                "highlight", Map.of(
                        "fields", Map.of(ABOUT_CONTENT, Map.of()),
                        "pre_tags", GS_HIGHLIGHT_DELIMITER,
                        "post_tags", GS_HIGHLIGHT_DELIMITER
                ),
                "size", ESService.MAX_ES_SIZE
        );
        Request request = new Request("GET", GS_ABOUT_END_POINT);
        request.setJsonEntity(gson.toJson(query));
        JsonObject jsonObject = esService.send(request);

        List<Map<String, String>> result = new ArrayList<>();

        for (JsonElement hit: jsonObject.get("hits").getAsJsonObject().get("hits").getAsJsonArray()) {
            for (JsonElement highlight: hit.getAsJsonObject().get("highlight").getAsJsonObject().get(ABOUT_CONTENT).getAsJsonArray()) {
                String page = hit.getAsJsonObject().get("_source").getAsJsonObject().get("page").getAsString();
                String title = hit.getAsJsonObject().get("_source").getAsJsonObject().get("title").getAsString();
                result.add(Map.of(
                        GS_CATEGORY_TYPE, GS_ABOUT,
                        "page", page,
                        "title", title,
                        "text", highlight.getAsString()
                ));
            }
        }

        return result;
    }

    private Map<String, Object> getGlobalSearchQuery(String input, Map<String, Object> category) {
        List<String> searchFields = (List<String>)category.get(GS_SEARCH_FIELD);
        List<Object> searchClauses = new ArrayList<>();
        for (String searchFieldName: searchFields) {
            searchClauses.add(Map.of("match_phrase_prefix", Map.of(searchFieldName, input)));
        }
        Map<String, Object> query = new HashMap<>();
        query.put("query", Map.of("bool", Map.of("should", searchClauses)));
        return query;
    }

    private Map<String, Object> addHighlight(Map<String, Object> query, Map<String, Object> category) {
        Map<String, Object> result = new HashMap<>(query);
        List<String> searchFields = (List<String>)category.get(GS_SEARCH_FIELD);
        Map<String, Object> highlightClauses = new HashMap<>();
        for (String searchFieldName: searchFields) {
            highlightClauses.put(searchFieldName, Map.of());
        }

        result.put("highlight", Map.of(
                        "fields", highlightClauses,
                        "pre_tags", "",
                        "post_tags", "",
                        "fragment_size", 1
                )
        );
        return result;
    }

    List<Map<String, Object>> findSubjectIdsInList(Map<String, Object> params) throws IOException {
        final String[][] properties = new String[][]{
                new String[]{"subject_id", "subject_id"},
                new String[]{"phs_accession", "phs_accession"}
        };

        Map<String, Object> query = esService.buildListQuery(params, Set.of(), true);
        Request request = new Request("GET", SUBJECT_IDS_END_POINT);

        return esService.collectPage(request, query, properties, ESService.MAX_ES_SIZE, 0);
    }

    List<Map<String, Object>> filesInList(Map<String, Object> params) throws Exception {
        final String[][] PROPERTIES = new String[][]{
            new String[]{"study_acronym", "studies"},
            new String[]{"accesses", "accesses"},
            new String[]{"phs_accession", "phs_accession"},
            new String[]{"subject_id", "subject_ids"},
            new String[]{"sample_id", "sample_id"},
            new String[]{"sample_types", "sample_types"},
            new String[]{"experimental_strategy", "experimental_strategies"},
            new String[]{"sex", "sex"},
            new String[]{"race", "races"},
            new String[]{"primary_diagnoses", "primary_diagnoses"},
            new String[]{"analyte_type", "analyte_type"},
            new String[]{"is_tumor", "is_tumor"},
            new String[]{"file_name", "file_name"},
            new String[]{"file_type", "file_type"},
            new String[]{"file_size", "file_size"},
            new String[]{"file_id", "file_id"},
            new String[]{"md5sum", "md5sum"},
            new String[]{"study_data_type", "study_data_types"},
            new String[]{"library_strategy", "library_strategies"},
            new String[]{"library_layouts", "library_layouts"},
            new String[]{"image_modality", "image_modality"},
            new String[]{"organ_or_tissue", "organ_or_tissue"},
            new String[]{"license", "license"},
            new String[]{"drs_uri", "drs_uri"},
            new String[]{"associated_file", "associated_file"},
            new String[]{"associated_drs_uri", "associated_drs_uri"},
            new String[]{"associated_md5sum", "associated_md5sum"}
    };

        String defaultSort = "file_name"; // Default sort order
        
        Map<String, String> sortFieldMapping = Map.ofEntries(
                Map.entry("study_acronym", "studies"),
                Map.entry("accesses", "accesses"),
                Map.entry("phs_accession", "phs_accession"),
                Map.entry("subject_id", "subject_ids"),
                Map.entry("sample_id", "sample_id"),
                Map.entry("experimental_strategy", "experimental_strategies_sort"),
                Map.entry("sex", "sex_sort"),
                Map.entry("race", "races_sort"),
                Map.entry("primary_diagnoses", "primary_diagnoses_sort"),
                Map.entry("analyte_type", "analyte_type_sort"),
                Map.entry("is_tumor", "is_tumor_sort"),
                Map.entry("file_name", "file_name"),
                Map.entry("file_type", "file_type"),
                Map.entry("file_size", "file_size"),
                Map.entry("file_id", "file_id"),
                Map.entry("md5sum", "md5sum"),
                Map.entry("study_data_type", "study_data_types"),
                Map.entry("library_strategy", "library_strategies_sort"),
                Map.entry("library_layouts", "library_layouts_sort"),
                Map.entry("image_modality", "image_modality"),
                Map.entry("organ_or_tissue", "organ_or_tissue"),
                Map.entry("license", "license"),
                Map.entry("drs_uri", "file_url_in_cds")
        );

        List<Map<String, Object>> filesInListResult = overview(FILES_END_POINT, params, PROPERTIES, defaultSort, sortFieldMapping);

        // Transform the specified properties in the "joinProperties" list from an arrays to a comma separated strings
        try{
            ArrayList<String> joinProperties = new ArrayList<>(Arrays.asList(
                    "experimental_strategy", "library_layouts", "library_strategy", "subject_id", "sample_id",
                    "sex", "race", "primary_diagnoses", "analyte_type", "is_tumor"));
            filesInListResult.forEach( x -> {
                x.keySet().forEach( k -> {
                    if (joinProperties.contains(k)){
                        List<String> values = (List<String>) x.get(k);
                        x.put(k, String.join(", ", values));
                    }
                });
            });
        }
        catch (ClassCastException| NullPointerException e){
            String message = "An error occurred while joining array values as a comma separated string";
            logger.error(message);
            logger.error(e);
            throw new Exception(message);
        }

        /*result.put("associated_file", associatedFileValue);
        result.put("associated_drs_uri", associatedDrsUriValue);
        result.put("associated_md5sum", associatedMd5sum);*/
        return filesInListResult;
    }

    List<String> fileIDsFromList(Map<String, Object> params) throws IOException {
        return collectFieldFromList(params, "file_id", FILES_END_POINT);
    }

    // This function search values in parameters and return a given collectField's unique values in a list
    private List<String> collectFieldFromList(Map<String, Object> params, String collectField, String endpoint) throws IOException {
        String[] idFieldArray = new String[]{collectField};
        Map<String, Object> query = esService.buildListQuery(params, Set.of());
        query = esService.addAggregations(query, idFieldArray);
        Request request = new Request("GET", endpoint);
        request.setJsonEntity(gson.toJson(query));
        JsonObject jsonObject = esService.send(request);
        return esService.collectTerms(jsonObject, collectField);
    }

}
