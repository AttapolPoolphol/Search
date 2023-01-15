/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import static org.apache.lucene.document.LatLonDocValuesField.newDistanceSort;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
//import org.apache.lucene.spatial.geopoint.document.GeoPointField;
//import org.apache.lucene.spatial.geopoint.search.GeoPointDistanceQuery;
//import static org.apache.lucene.spatial.util.GeoEncodingUtils.mortonUnhashLat;
//import org.apache.lucene.spatial.util.MortonEncoder;
import org.apache.lucene.search.SortField;
import org.apache.lucene.document.LatLonPoint;
import static org.apache.lucene.document.LatLonPoint.newDistanceQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.spatial.vector.DistanceValueSource;

/**
 *
 * @author GT_4953
 */
public class _cl_GetData {

    private final int INDEX_NOSTRAID = 0;
    private final int INDEX_NAME_ENGLISH_RETURN = 1;
    private final int INDEX_NAME_LOCAL_RETURN = 2;
    private final int INDEX_BRANCH_ENGLISH_RETURN = 3;
    private final int INDEX_BRANCH_LOCAL_RETURN = 4;

    private final int INDEX_ADMIN1_ENG = 5;
    private final int INDEX_ADMIN2_ENG = 6;
    private final int INDEX_ADMIN3_ENG = 7;
    private final int INDEX_ADMIN4_ENG = 8;
    private final int INDEX_ADMIN1_LOCAL = 9;
    private final int INDEX_ADMIN2_LOCAL = 10;
    private final int INDEX_ADMIN3_LOCAL = 11;
    private final int INDEX_ADMIN4_LOCAL = 12;

    private final int INDEX_HNO = 13;
    private final int INDEX_TEL = 14;
    private final int INDEX_POST = 15;

    private final int INDEX_CATCODE = 16;
    private final int INDEX_LOCAL_CAT = 17;
    private final int INDEX_LATLON = 18;
    private final int INDEX_LATLON_R1 = 19;
    private final int INDEX_LATLON_R2 = 20;
    private final int INDEX_LATLON_R3 = 21;
    private final int INDEX_LATLON_R4 = 22;

    private final int INDEX_DIS = 23;
    private final int INDEX_SCORE = 24;
    private final int INDEX_POP = 25;
    private final int INDEX_HITSCORE = 26;

    private final int INDEX_WB_ALL = 27;
    private final int INDEX_WB_ALL_LOCAL = 28;
    private final int INDEX_WB_ALL_ENGLISH = 29;

    private final int INDEX_WB_NAME_ENGLISH = 30;
    private final int INDEX_WB_NAME_LOCAL = 31;
    private final int INDEX_COMPARE_NAME_ENGLISH = 32;
    private final int INDEX_COMPARE_NAME_LOCAL = 33;

    private final int INDEX_WB_NAME_THEN = 34;
    private final int INDEX_WB_NAME_ENTH = 35;
    private final int INDEX_COMPARE_NAME_THEN = 36;
    private final int INDEX_COMPARE_NAME_ENTH = 37;

    private final int INDEX_GROUP_ALL = 38;
    private final int INDEX_GROUP_NAME = 39;

    private final int INDEX_ADMIN_CODE = 40;

    // INDEX FOR SOUNDEX
    private final int INDEX_SOUND_NAME_LOCAL = 41;
    private final int INDEX_SOUND_NAME_THEN = 42;
    private final int INDEX_SOUND_NAME_ENTH = 43;
    private final int INDEX_HouseNumber_sound = 44;
    private final int INDEX_Telephone_sound = 45;
    private final int INDEX_PostCode_sound = 46;

    private final int INDEX_SOUND_LOCAL = 47;

    //SUM INDEX
    private final int NUM_OF_RES_INDEX = 41;// NORMAL
    private final int NUM_OF_RES_INDEX_SOUNDEX = 48;// SOUNDEX

    private int INDEX_Master_ID = 0;

    private String KEYWORD_SEARCH_SET = "";
    private String KEYWORD_SEARCH_SET_WB = "";

    private String KEYWORD_SEARCH_SOUNDEX = "";
    private String KEYWORD_SEARCH_SOUNDEX_DELI = "";

    private String KEYWORD_SEARCH = "";

    public String URL_WB;
    private Analyzer analyzer_th = new WhitespaceAnalyzer();
    private int PROCESS_ROW;

    public _cl_GetData(int row_p) {
        this.PROCESS_ROW = row_p;
    }

    public int NostraSearch_TotalResult(_data_input input, IndexSearcher searcher) {
        try {
            BooleanQuery.Builder query = GenQuery_Total_Search(input);
            Query qq = query.build();
            TopDocs td = searcher.search(qq, 1);
            long s = td.totalHits.value;
            if (s<=0)
                return 0;
            else
                return (int)s;
        } catch (Exception e) {
            return 0;
        }

    }

    public int NostraSearch_TotalResult(_data_input_soundex input, IndexSearcher searcher) {
        try {
            BooleanQuery.Builder query = GenQuery_Total_Search_soundex(input);
            Query qq = query.build();
            TopDocs td = searcher.search(qq, 1);
            long s = td.totalHits.value;
            if (s<=0)
                return 0;
            else
                return (int)s;
        } catch (Exception e) {
            return 0;
        }

    }
    
    public _data_result NostraSearch(_data_input input, IndexSearcher searcher) {
        try {
            BooleanQuery.Builder query = GenQuery_Search(input);
            _data_result res = QuerySearch(searcher, query, PROCESS_ROW, input.Lat, input.Lon);
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable(res);
                res = SortGroupAction(res);
                return res;
            }
            //return res;

        } catch (Exception e) {
            return null;
        }

    }

    public _data_result NostraSearch(_data_input_soundex input, IndexSearcher searcher) {
        try {
            BooleanQuery.Builder query = GenQuery_Search_soundex(input);
            _data_result res = QuerySearch_soundex(searcher, query, PROCESS_ROW, input.Lat, input.Lon);
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable_sound(res);
                res = SortGroupAction(res);
                return res;
            }
            //return res;

        } catch (Exception e) {
            return null;
        }

    }

    public _data_result QuerySearch(IndexSearcher searcher, BooleanQuery.Builder booleanQuery, int MaxResult, String lat, String lon) {
        int hitsPerPage = MaxResult;

        try {
            Sort sort;
            if (CheckNullEmptyString(lat) || CheckNullEmptyString(lon)) {
                sort = new Sort(
                        new SortField[]{SortField.FIELD_SCORE
                        });
            } else {
                sort = new Sort(
                        new SortField[]{SortField.FIELD_SCORE,
                            new SortField("hitscoreSort", SortField.Type.DOC.LONG, true),
                            newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
                        });
            }
            TopFieldDocs TFD = searcher.search(booleanQuery.build(), hitsPerPage, sort);
            //TopFieldDocs TFD = searcher.search(booleanQuery.build(), hitsPerPage, sort, true, true);
            ScoreDoc[] hits = TFD.scoreDocs;
            long total_Res = TFD.totalHits.value;

//            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
//            searcher.search(booleanQuery.build(), collector);
//            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length <= 0) {
                return null;
            }

            String res[][] = new String[hits.length][NUM_OF_RES_INDEX];
            // Iterate through the results:
            try {
                for (int i = 0; i < hits.length; i++) {
                    Document d = searcher.doc(hits[i].doc);
                    int j = 0;

                    //nostra id
                    res[i][j++] = d.get("NostraID");

                    //name_Eng_Retrun to user
                    if (CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                        res[i][j++] = d.get("Name_English").trim();
                    } else {
                        res[i][j++] = d.get("Name_English").trim() + " " + d.get("Name_Branch_English").trim();
                    }

                    //name_Local_Retrun to user
                    if (CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                        res[i][j++] = d.get("Name_Local").trim();
                    } else {
                        res[i][j++] = d.get("Name_Local").trim() + " " + d.get("Name_Branch_Local").trim();
                    }
                    //branchE
                    if (!CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                        res[i][j++] = d.get("Name_Branch_English").trim();
                    } else {
                        res[i][j++] = "";
                    }

                    //branchL
                    if (!CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                        res[i][j++] = d.get("Name_Branch_Local").trim();
                    } else {
                        res[i][j++] = "";
                    }

                    //admin
                    res[i][j++] = d.get("AdminLevel_1_English");
                    res[i][j++] = d.get("AdminLevel_2_English");
                    res[i][j++] = d.get("AdminLevel_3_English");
                    res[i][j++] = d.get("AdminLevel_4_English");

                    res[i][j++] = d.get("AdminLevel_1_Local");
                    res[i][j++] = d.get("AdminLevel_2_Local");
                    res[i][j++] = d.get("AdminLevel_3_Local");
                    res[i][j++] = d.get("AdminLevel_4_Local");

                    res[i][j++] = d.get("HouseNumber");
                    res[i][j++] = d.get("Telephone");
                    res[i][j++] = d.get("PostCode");

                    //cat
                    res[i][j++] = d.get("Category_Return");
                    res[i][j++] = d.get("LocalCatCode");

                    //latlon
//                    Double tmplat = GeoPointField.decodeLatitude(Long.parseLong(d.get("LatLonGeo")));
//                    Double tmplon = GeoPointField.decodeLongitude(Long.parseLong(d.get("LatLonGeo")));
                    Double tmplat = Double.parseDouble(d.get("Lat"));
                    Double tmplon = Double.parseDouble(d.get("Lon"));

                    res[i][j++] = tmplat.toString() + "," + tmplon.toString();

                    res[i][j++] = d.get("LatLon_Route1");
                    res[i][j++] = d.get("LatLon_Route2");
                    res[i][j++] = d.get("LatLon_Route3");
                    res[i][j++] = d.get("LatLon_Route4");

                    //**********FOR SORT**********//
                    //dist
                    if (!CheckNullEmptyString(lat) && !CheckNullEmptyString(lon)) {
                        Double dis = distance(tmplat, tmplon, Double.parseDouble(lat), Double.parseDouble(lon), "K");
                        res[i][j++] = dis.toString();
                    } else {
                        res[i][j++] = "0";
                    }

                    float score = hits[i].score;
                    res[i][j++] = Float.toString(score);

                    res[i][j++] = d.get("Popularity");
                    res[i][j++] = d.get("hitscore");

                    //**********FOR GROUP**********//
                    res[i][j++] = d.get("text_search");

                    res[i][j++] = d.get("Description_Local");
                    res[i][j++] = d.get("Description_English");

                    //name compare  E_WB
                    if (CheckNullEmptyString(d.get("Name_Branch_English_w").trim())) {
                        res[i][j++] = d.get("Name_English_w").trim();
                    } else {
                        res[i][j++] = d.get("Name_English_w").trim() + " " + d.get("Name_Branch_English_w").trim();
                    }
                    //name compare L_WB
                    if (CheckNullEmptyString(d.get("Name_Branch_Local_w").trim())) {
                        res[i][j++] = d.get("Name_Local_w").trim();
                    } else {
                        res[i][j++] = d.get("Name_Local_w").trim() + " " + d.get("Name_Branch_Local_w").trim();
                    }

                    //name E compare
                    if (CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                        res[i][j++] = d.get("Display_EN").trim();
                    } else {
                        res[i][j++] = d.get("Display_EN").trim() + " " + d.get("Name_Branch_English").trim();
                    }
                    //name L compare
                    if (CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                        res[i][j++] = d.get("Display_TH").trim();
                    } else {
                        res[i][j++] = d.get("Display_TH").trim() + " " + d.get("Name_Branch_Local").trim();
                    }

                    //name ThEn / EnTh
                    // INDEX_WB_NAME_THEN = 32;
                    //INDEX_WB_NAME_ENTH = 33;
                    //INDEX_COMPARE_NAME_THEN = 34;
                    // INDEX_COMPARE_NAME_ENTH = 35;
                    res[i][j++] = res[i][INDEX_WB_NAME_LOCAL] + " " + res[i][INDEX_WB_NAME_ENGLISH];
                    res[i][j++] = res[i][INDEX_WB_NAME_ENGLISH] + " " + res[i][INDEX_WB_NAME_LOCAL];

                    res[i][j++] = res[i][INDEX_COMPARE_NAME_LOCAL] + " " + res[i][INDEX_COMPARE_NAME_ENGLISH];
                    res[i][j++] = res[i][INDEX_COMPARE_NAME_ENGLISH] + " " + res[i][INDEX_COMPARE_NAME_LOCAL];

                    //sort ALL
                    res[i][j++] = "4";
                    //Sort NAME
                    res[i][j++] = "4";

                    res[i][j++] = d.get("Admin_Code").trim();
                }

            } catch (Exception exception) {
                System.out.println(exception.toString());
                return null;
            }

            _data_result FinalResult = new _data_result();
            String header[] = {
                "NostraId",
                "Name_E",
                "Name_L",
                "Branch_E",
                "Branch_L",
                "AdminLevel1_E",
                "AdminLevel2_E",
                "AdminLevel3_E",
                "AdminLevel4_E",
                "AdminLevel1_L",
                "AdminLevel2_L",
                "AdminLevel3_L",
                "AdminLevel4_L",
                "HouseNo",
                "Telephone",
                "PostCode",
                "Catcode",
                "LocalCatCode",
                "LatLon",
                "LatLon_Route1",
                "LatLon_Route2",
                "LatLon_Route3",
                "LatLon_Route4",
                "dist",
                "score",
                "Popularity",
                "hitscore",
                "text_search",
                "des_local",
                "des_eng",
                "Name_E_sort_WB",
                "Name_L_sort_WB",
                "Name_E_sort",
                "Name_L_sort",
                "NAME_THEN_wb",
                "NAME_ENTH_wb",
                "NAME_THEN",
                "NAME_ENTH",
                "Group_All",
                "Group_Name",
                "Admin_Code"
            };

            FinalResult.HeaderTable = header;
            FinalResult.DataTable = res;
            FinalResult.TotalSearch = (int)total_Res;

            return FinalResult;
        } catch (Exception exception) {
            System.out.println(exception.toString());
            return null;
        }
    }

    public _data_result QuerySearch_soundex(IndexSearcher searcher, BooleanQuery.Builder booleanQuery, int MaxResult, String lat, String lon) {
        int hitsPerPage = MaxResult;

        try {
            Sort sort;
            if (CheckNullEmptyString(lat) || CheckNullEmptyString(lon)) {
                sort = new Sort(
                        new SortField[]{SortField.FIELD_SCORE
                        });
            } else {
                sort = new Sort(
                        new SortField[]{SortField.FIELD_SCORE,
                            new SortField("hitscoreSort", SortField.Type.DOC.LONG, true),
                            newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
                        });
            }

            TopFieldDocs TFD = searcher.search(booleanQuery.build(), hitsPerPage, sort);
            ScoreDoc[] hits = TFD.scoreDocs;
            long total_Res = TFD.totalHits.value;

//            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
//            searcher.search(booleanQuery.build(), collector);
//            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length <= 0) {
                return null;
            }

            String res[][] = new String[hits.length][NUM_OF_RES_INDEX_SOUNDEX];
            // Iterate through the results:
            try {
                for (int i = 0; i < hits.length; i++) {
                    Document d = searcher.doc(hits[i].doc);
                    int j = 0;

                    //nostra id
                    res[i][j++] = d.get("NostraID");

                    //name_Eng_Retrun to user
                    if (CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                        res[i][j++] = d.get("Name_English").trim();
                    } else {
                        res[i][j++] = d.get("Name_English").trim() + " " + d.get("Name_Branch_English").trim();
                    }

                    //name_Local_Retrun to user
                    if (CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                        res[i][j++] = d.get("Name_Local").trim();
                    } else {
                        res[i][j++] = d.get("Name_Local").trim() + " " + d.get("Name_Branch_Local").trim();
                    }
                    //branchE
                    if (!CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                        res[i][j++] = d.get("Name_Branch_English").trim();
                    } else {
                        res[i][j++] = "";
                    }

                    //branchL
                    if (!CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                        res[i][j++] = d.get("Name_Branch_Local").trim();
                    } else {
                        res[i][j++] = "";
                    }

                    //admin
                    res[i][j++] = d.get("AdminLevel_1_English");
                    res[i][j++] = d.get("AdminLevel_2_English");
                    res[i][j++] = d.get("AdminLevel_3_English");
                    res[i][j++] = d.get("AdminLevel_4_English");

                    res[i][j++] = d.get("AdminLevel_1_Local");
                    res[i][j++] = d.get("AdminLevel_2_Local");
                    res[i][j++] = d.get("AdminLevel_3_Local");
                    res[i][j++] = d.get("AdminLevel_4_Local");

                    res[i][j++] = d.get("HouseNumber");
                    res[i][j++] = d.get("Telephone");
                    res[i][j++] = d.get("PostCode");

                    //cat
                    res[i][j++] = d.get("Category_Return");
                    res[i][j++] = d.get("LocalCatCode");

                    //latlon
//                    Double tmplat = GeoPointField.decodeLatitude(Long.parseLong(d.get("LatLonGeo")));
//                    Double tmplon = GeoPointField.decodeLongitude(Long.parseLong(d.get("LatLonGeo")));
                    Double tmplat = Double.parseDouble(d.get("Lat"));
                    Double tmplon = Double.parseDouble(d.get("Lon"));
    
                    res[i][j++] = tmplat.toString() + "," + tmplon.toString();

                    res[i][j++] = d.get("LatLon_Route1");
                    res[i][j++] = d.get("LatLon_Route2");
                    res[i][j++] = d.get("LatLon_Route3");
                    res[i][j++] = d.get("LatLon_Route4");

                    //**********FOR SORT**********//
                    //dist
                    if (!CheckNullEmptyString(lat) && !CheckNullEmptyString(lon)) {
                        Double dis = distance(tmplat, tmplon, Double.parseDouble(lat), Double.parseDouble(lon), "K");
                        res[i][j++] = dis.toString();
                    } else {
                        res[i][j++] = "0";
                    }

                    float score = hits[i].score;
                    res[i][j++] = Float.toString(score);

                    res[i][j++] = d.get("Popularity");
                    res[i][j++] = d.get("hitscore");

                    //**********FOR GROUP**********//
                    res[i][j++] = d.get("text_search");

                    res[i][j++] = d.get("Description_Local");
                    res[i][j++] = d.get("Description_English");

                    //name compare  E_WB
                    if (CheckNullEmptyString(d.get("Name_Branch_English_w").trim())) {
                        res[i][j++] = d.get("Name_English_w").trim();
                    } else {
                        res[i][j++] = d.get("Name_English_w").trim() + " " + d.get("Name_Branch_English_w").trim();
                    }
                    //name compare L_WB
                    if (CheckNullEmptyString(d.get("Name_Branch_Local_w").trim())) {
                        res[i][j++] = d.get("Name_Local_w").trim();
                    } else {
                        res[i][j++] = d.get("Name_Local_w").trim() + " " + d.get("Name_Branch_Local_w").trim();
                    }

                    //name E compare
                    if (CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                        res[i][j++] = d.get("Display_EN").trim();
                    } else {
                        res[i][j++] = d.get("Display_EN").trim() + " " + d.get("Name_Branch_English").trim();
                    }
                    //name L compare
                    if (CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                        res[i][j++] = d.get("Display_TH").trim();
                    } else {
                        res[i][j++] = d.get("Display_TH").trim() + " " + d.get("Name_Branch_Local").trim();
                    }

                    //name ThEn / EnTh
                    // INDEX_WB_NAME_THEN = 32;
                    //INDEX_WB_NAME_ENTH = 33;
                    //INDEX_COMPARE_NAME_THEN = 34;
                    // INDEX_COMPARE_NAME_ENTH = 35;
                    res[i][j++] = res[i][INDEX_WB_NAME_LOCAL] + " " + res[i][INDEX_WB_NAME_ENGLISH];
                    res[i][j++] = res[i][INDEX_WB_NAME_ENGLISH] + " " + res[i][INDEX_WB_NAME_LOCAL];

                    res[i][j++] = res[i][INDEX_COMPARE_NAME_LOCAL] + " " + res[i][INDEX_COMPARE_NAME_ENGLISH];
                    res[i][j++] = res[i][INDEX_COMPARE_NAME_ENGLISH] + " " + res[i][INDEX_COMPARE_NAME_LOCAL];

                    //sort ALL
                    res[i][j++] = "4";
                    //Sort NAME
                    res[i][j++] = "4";

                    res[i][j++] = d.get("Admin_Code").trim();

                    if (CheckNullEmptyString(d.get("Name_Branch_Local_sound").trim())) {
                        res[i][j++] = d.get("Name_Local_sound").trim();
                    } else {
                        res[i][j++] = d.get("Name_Local_sound").trim() + " " + d.get("Name_Branch_Local_sound").trim();
                    }
                    //compare sound
                    res[i][j++] = res[i][INDEX_SOUND_NAME_LOCAL] + " " + res[i][INDEX_WB_NAME_ENGLISH];
                    res[i][j++] = res[i][INDEX_WB_NAME_ENGLISH] + " " + res[i][INDEX_SOUND_NAME_LOCAL];

                    res[i][j++] = d.get("HouseNumber_sound");
                    res[i][j++] = d.get("Telephone_sound");
                    res[i][j++] = d.get("PostCode_sound");

                    res[i][j++] = d.get("local_soundex");

                }

            } catch (Exception exception) {
                System.out.println(exception.toString());
                return null;
            }

            _data_result FinalResult = new _data_result();
            String header[] = {
                "NostraId",
                "Name_E",
                "Name_L",
                "Branch_E",
                "Branch_L",
                "AdminLevel1_E",
                "AdminLevel2_E",
                "AdminLevel3_E",
                "AdminLevel4_E",
                "AdminLevel1_L",
                "AdminLevel2_L",
                "AdminLevel3_L",
                "AdminLevel4_L",
                "HouseNo",
                "Telephone",
                "PostCode",
                "Catcode",
                "LocalCatCode",
                "LatLon",
                "LatLon_Route1",
                "LatLon_Route2",
                "LatLon_Route3",
                "LatLon_Route4",
                "dist",
                "score",
                "Popularity",
                "hitscore",
                "text_search",
                "des_local",
                "des_eng",
                "Name_E_sort_WB",
                "Name_L_sort_WB",
                "Name_E_sort",
                "Name_L_sort",
                "NAME_THEN_wb",
                "NAME_ENTH_wb",
                "NAME_THEN",
                "NAME_ENTH",
                "Group_All",
                "Group_Name",
                "Admin_Code",
                "SOUND_NAME_LOCAL",
                "SOUND_NAME_THEN",
                "SOUND_NAME_ENTH",
                "HouseNumber_sound",
                "Telephone_sound",
                "PostCode_sound",
                "local_soundex"
            };

            FinalResult.HeaderTable = header;
            FinalResult.DataTable = res;
            FinalResult.TotalSearch = (int)total_Res;

            return FinalResult;
        } catch (Exception exception) {
            System.out.println(exception.toString());
            return null;
        }
    }

    public BooleanQuery.Builder GenQuery_Search(_data_input input) throws ParseException {
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        if (CheckNullEmptyString(input.keyword)) {

            booleanQuery.add(new QueryParser("text_search", analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);
            booleanQuery = NostraFilter(booleanQuery, input);

        } else {
            KEYWORD_SEARCH_SET = input.search_keyword;
            KEYWORD_SEARCH_SET_WB = input.search_keyword_WB;

            if (CheckNullEmptyString(KEYWORD_SEARCH_SET_WB)) {
                booleanQuery.add(new QueryParser("text_search", analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

            } else {
                //
                //BOOST SCORE FUTURE FEATURE
                //
//                String[] tmpWB_arr = KEYWORD_SEARCH_SET_WB.split("\\|");
//                String TmpWB = "";
//                
//                if(tmpWB_arr.length >1)
//                {
//                    TmpWB += "( " + tmpWB_arr[0] + " )^2";
//                    for(int c_tmpWB_arr =1; c_tmpWB_arr <tmpWB_arr.length ;c_tmpWB_arr++)
//                    {
//                        TmpWB += " " + tmpWB_arr[c_tmpWB_arr];
//                    }
//                }
//                else
//                    TmpWB = KEYWORD_SEARCH_SET_WB.replace("|", " ");
                String TmpWB = KEYWORD_SEARCH_SET_WB.replace("|", " ");

                TmpWB = TmpWB.replace("/", "\\/");
                booleanQuery.add(new QueryParser("text_search", analyzer_th).parse(TmpWB), BooleanClause.Occur.MUST);
            }
            booleanQuery = NostraFilter(booleanQuery, input);
        }

        return booleanQuery;
    }
    public BooleanQuery.Builder GenQuery_Total_Search(_data_input input) throws ParseException {
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        String fieldIndex = "Total_wb";
        if (CheckNullEmptyString(input.keyword)) {

            booleanQuery.add(new QueryParser(fieldIndex, analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);
            booleanQuery = NostraFilter(booleanQuery, input);

        } else {
            KEYWORD_SEARCH_SET = input.search_keyword;
            KEYWORD_SEARCH_SET_WB = input.search_keyword_WB;

            if (CheckNullEmptyString(KEYWORD_SEARCH_SET_WB)) {
                booleanQuery.add(new QueryParser(fieldIndex, analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

            } else {
                String TmpWB = KEYWORD_SEARCH_SET_WB.replace("|", " ");

                TmpWB = TmpWB.replace("/", "\\/");
                booleanQuery.add(new QueryParser(fieldIndex, analyzer_th).parse(TmpWB), BooleanClause.Occur.MUST);
            }
            booleanQuery = NostraFilter(booleanQuery, input); 
        }
        booleanQuery.add(new QueryParser("Master_ID", analyzer_th).parse("0"), BooleanClause.Occur.FILTER);
        return booleanQuery;
    }
    public BooleanQuery.Builder GenQuery_Search_soundex(_data_input_soundex input) throws ParseException {

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        KEYWORD_SEARCH = input.keyword;
        KEYWORD_SEARCH_SOUNDEX = input.soundex_keyword;
        KEYWORD_SEARCH_SOUNDEX_DELI = input.soundex_keyword_delimeter;

        String Tmp = "";
        String[] Tmp_arr = KEYWORD_SEARCH_SOUNDEX.split(" ");

        for (int i = 0; i < Tmp_arr.length; i++) {
            Tmp += "\"" + Tmp_arr[i] + "\" ";
        }

        //TmpWB = TmpWB.replace("/", "\\/");
        booleanQuery.add(new QueryParser("soundex", analyzer_th).parse(Tmp.trim()), BooleanClause.Occur.MUST);

        booleanQuery = NostraFilter(booleanQuery, input);

        return booleanQuery;
    }

    public BooleanQuery.Builder GenQuery_Total_Search_soundex(_data_input_soundex input) throws ParseException {
        String fieldIndex = "Total_sound";
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        KEYWORD_SEARCH = input.keyword;
        KEYWORD_SEARCH_SOUNDEX = input.soundex_keyword;
        KEYWORD_SEARCH_SOUNDEX_DELI = input.soundex_keyword_delimeter;

        String Tmp = "";
        String[] Tmp_arr = KEYWORD_SEARCH_SOUNDEX.split(" ");
        
        for (int i = 0; i < Tmp_arr.length; i++) {
            Tmp += "\"" + Tmp_arr[i] + "\" ";
        }
        
        booleanQuery.add(new QueryParser(fieldIndex, analyzer_th).parse(Tmp.trim()), BooleanClause.Occur.MUST);

        booleanQuery = NostraFilter(booleanQuery, input);
        booleanQuery.add(new QueryParser("Master_ID", analyzer_th).parse("0"), BooleanClause.Occur.FILTER);

        return booleanQuery;
    }
    public boolean CheckNullEmptyString(String input) {
        if (input == null) {
            return true;
        } else if (input.toUpperCase().trim().equals("N/A")) {
            return true;
        }
        return input.trim().length() == 0;
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private BooleanQuery.Builder NostraFilter(BooleanQuery.Builder q, _data_input input) throws ParseException {
        if (!CheckNullEmptyString(input.Category)) {

            String[] cat = input.Category.split("\\|");
            String tmp = "";

            for (int i = 0; i < cat.length; i++) {
                if (cat[i].trim().equals("FOOD")) {
                    cat[i] = cat[i].replace("FOOD", "FOOD-BAKECOF FOOD-FASTFOOD FOOD-FOODSHOP FOOD-PUB FOOD-RESRAURANT");
                } else if (cat[i].trim().equals("SHOPPING")) {
                    cat[i] = cat[i].replace("SHOPPING", "SHOPPING-CONVSTORE SHOPPING-DEPTSTORE SHOPPING-MARKET SHOPPING-STORE");
                } else if (cat[i].trim().equals("FUEL")) {
                    cat[i] = cat[i].replace("FUEL", "FUEL-LPG FUEL-NGP FUEL-OIL FUEL-EV");
                }
                tmp += cat[i] + " ";
            }
            q.add(new QueryParser("Category", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
        }

        if (!CheckNullEmptyString(input.LocalCatCode)) {
            String tmp = input.LocalCatCode.replace("|", " ");

            q.add(new QueryParser("LocalCatCode", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
        }

        if (input.radius > 0) {

            Query q_geodis = newDistanceQuery("LatLonPoint", Double.parseDouble(input.Lat), Double.parseDouble(input.Lon), input.radius);
            q.add(q_geodis, BooleanClause.Occur.FILTER);
        }

        if (!CheckNullEmptyString(input.AdminLevel_1)) {
            String tmpid = input.AdminLevel_1.trim();
            if (!CheckNullEmptyString(input.AdminLevel_2)) {
                tmpid += input.AdminLevel_2.trim();
                if (!CheckNullEmptyString(input.AdminLevel_3)) {
                    tmpid += input.AdminLevel_3.trim();
                } else {
                    tmpid += "*";
                }
            } else {
                tmpid += "*";
            }
            q.add(new QueryParser("Admin_Code", analyzer_th).parse(tmpid), BooleanClause.Occur.FILTER);
        }

        if (!CheckNullEmptyString(input.AdminLevel_4)) {
            if (This_thai(input.AdminLevel_4)) {
                q.add(new QueryParser("AdminLevel_4_Local", analyzer_th).parse(input.AdminLevel_4), BooleanClause.Occur.FILTER);
            } else {
                q.add(new QueryParser("AdminLevel_4_English", analyzer_th).parse(input.AdminLevel_4), BooleanClause.Occur.FILTER);
            }
        }

        if (!CheckNullEmptyString(input.PostCode)) {
            q.add(new QueryParser("PostCode", analyzer_th).parse(input.PostCode), BooleanClause.Occur.FILTER);
        }

        return q;
    }

    private BooleanQuery.Builder NostraFilter(BooleanQuery.Builder q, _data_input_soundex input) throws ParseException {
        if (!CheckNullEmptyString(input.Category)) {

            String[] cat = input.Category.split("\\|");
            String tmp = "";

            for (int i = 0; i < cat.length; i++) {
                if (cat[i].trim().equals("FOOD")) {
                    cat[i] = cat[i].replace("FOOD", "FOOD-BAKECOF FOOD-FASTFOOD FOOD-FOODSHOP FOOD-PUB FOOD-RESRAURANT");
                } else if (cat[i].trim().equals("SHOPPING")) {
                    cat[i] = cat[i].replace("SHOPPING", "SHOPPING-CONVSTORE SHOPPING-DEPTSTORE SHOPPING-MARKET SHOPPING-STORE");
                } else if (cat[i].trim().equals("FUEL")) {
                    cat[i] = cat[i].replace("FUEL", "FUEL-LPG FUEL-NGP FUEL-OIL FUEL-EV");
                }
                tmp += cat[i] + " ";
            }
            q.add(new QueryParser("Category", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
        }

        if (!CheckNullEmptyString(input.LocalCatCode)) {
            String tmp = input.LocalCatCode.replace("|", " ");

            q.add(new QueryParser("LocalCatCode", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
        }

        if (input.radius > 0) {

            Query q_geodis = newDistanceQuery("LatLonPoint", Double.parseDouble(input.Lat), Double.parseDouble(input.Lon), input.radius);
            q.add(q_geodis, BooleanClause.Occur.FILTER);
        }

        if (!CheckNullEmptyString(input.AdminLevel_1)) {
            String tmpid = input.AdminLevel_1.trim();
            if (!CheckNullEmptyString(input.AdminLevel_2)) {
                tmpid += input.AdminLevel_2.trim();
                if (!CheckNullEmptyString(input.AdminLevel_3)) {
                    tmpid += input.AdminLevel_3.trim();
                } else {
                    tmpid += "*";
                }
            } else {
                tmpid += "*";
            }
            q.add(new QueryParser("Admin_Code", analyzer_th).parse(tmpid), BooleanClause.Occur.FILTER);
        }

        if (!CheckNullEmptyString(input.AdminLevel_4)) {
            if (This_thai(input.AdminLevel_4)) {
                q.add(new QueryParser("AdminLevel_4_Local", analyzer_th).parse(input.AdminLevel_4), BooleanClause.Occur.FILTER);
            } else {
                q.add(new QueryParser("AdminLevel_4_English", analyzer_th).parse(input.AdminLevel_4), BooleanClause.Occur.FILTER);
            }
        }

        if (!CheckNullEmptyString(input.PostCode)) {
            q.add(new QueryParser("PostCode", analyzer_th).parse(input.PostCode), BooleanClause.Occur.FILTER);
        }

        return q;
    }

    private boolean This_thai(String input) {
        Pattern r = Pattern.compile("[ก-ฮฯ-ูเ-ํ๑-๙]+");
        Matcher m = r.matcher(input);
        return m.find();
    }

    private boolean This_Eng(String input) {
        Pattern r = Pattern.compile("[A-Za-z]+");
        Matcher m = r.matcher(input);
        return m.find();
    }

    public int Set_NUM_SortGroup(String MainName, String MainName_wb, String Key, String Key_wb) {
        if (CheckCase2(MainName_wb, Key_wb))//Check wb token ครบไหม
        {
            if (CheckCase0(MainName, Key))// Excat Or *key*
            {
                return 0;
            } else if (CheckCase1(MainName_wb, Key_wb))//Token เรียงตามลำดับ
            {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }
    
    public int Set_NUM_SortGroup_sound(String MainName, String MainName_wb, String Key, String Key_wb) {
        if (CheckCase2(MainName_wb, Key_wb))//Check wb token ครบไหม
        {
            if (CheckCase0(MainName, Key))// Excat Or *key*
            {
                return 0;
            } else if (CheckCase1(MainName_wb, Key_wb))//Token เรียงตามลำดับ
            {
                return 1;
            } else {
                return 2;
            }
        } else {
            return 3;
        }
    }

    public boolean CheckCase0(String mainW, String keyW) {
        mainW = (mainW.replace(" ", "")).trim();
        keyW = (keyW.replace(" ", "")).trim();

        return This_String_Input_Like(keyW, mainW);
    }

    public boolean CheckCase1(String mainW, String keyW) {
        String tmp_keyW = keyW.replaceAll("^\\s+", "");//trim left
        tmp_keyW = tmp_keyW.replaceAll("\\s+$", "");//trim right

        //tmp_keyW = tmp_keyW.replace(" ", "(\\s+)(|\\W+)(|\\s+)");
        tmp_keyW = tmp_keyW.replace(" ", "(\\s+).*");
        tmp_keyW = "(\\s+|^)" + tmp_keyW + "(\\s+|$)";

        try {
            tmp_keyW = tmp_keyW.replace("?", "[?]");
            Pattern p = Pattern.compile(tmp_keyW);
            Matcher match = p.matcher(mainW);

            return match.find();
        } catch (Exception e) {
            return false;
        }
    }
    

    public boolean CheckCase2(String mainW, String keyW) {

        String[] m = mainW.split(" ");
        String[] k = keyW.split(" ");

        for (int i = 0; i < k.length; i++) {
            if (!CheckNullEmptyString(k[i])) {
                if (!Arrays.asList(m).contains(k[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean CheckCase3(String mainW, String keyW) {

        return true;
    }

    public boolean This_String_Input_Like(String input, String DataCompare) {
//        String tmp = input.replace(" ", "(\\w+|\\W+|)");
//        Pattern left = Pattern.compile("(\\w+|\\W+|)" + tmp);
//        Pattern center = Pattern.compile("(\\w+|\\W+|)" + tmp + "(\\w+|\\W+|)");
//        Pattern right = Pattern.compile(tmp + "(\\w+|\\W+|)");

        String tmp = input.replace(" ", "(\\S+|)");
        
        tmp = tmp.replace("?", "[?]");
        
        Pattern left = Pattern.compile("(\\S+|)" + tmp);
        Pattern center = Pattern.compile("(\\S+|)" + tmp + "(\\S+|)");
        Pattern right = Pattern.compile(tmp + "(\\S+|)");

        Matcher m_left = left.matcher(DataCompare);
        Matcher m_center = center.matcher(DataCompare);
        Matcher m_right = right.matcher(DataCompare);

        if (m_left.find()) {
            return true;
        } else if (m_center.find()) {
            return true;
        } else if (m_right.find()) {
            return true;
        }

        return false;
    }

    public String Grouping(String MainWord, String MainWord_wb, String ShortWord, String ShortWord_wb) {
        String[] tmp_sw = ShortWord.split("\\|");
        String[] tmp_sw_wb = ShortWord_wb.split("\\|");
        int res = 10;
        for (int i = 0; i < tmp_sw.length; i++) {
            int tmpI = Set_NUM_SortGroup(MainWord, MainWord_wb, tmp_sw[i], tmp_sw_wb[i]);
            if (i < res) {
                res = i;
            }
        }
        return String.valueOf(res);
    }

    public _data_result InputGroupToTable(_data_result input) {
        String[] tmpKeywordSplit = KEYWORD_SEARCH_SET.split("\\|");
        String[] tmpKeywordSplit_wb = KEYWORD_SEARCH_SET_WB.split("\\|");
        int loop = tmpKeywordSplit.length;
//        int counter = 0;
//        long t1 = System.currentTimeMillis();
        for (int i = 0; i < input.DataTable.length; i++) {
            for (int j = 0; j < loop; j++) {

                String trimString = tmpKeywordSplit_wb[j].replaceAll("^\\s+", "");
                if (CheckNullEmptyString(trimString.trim())) {
                    continue;
                }

                if (This_thai(tmpKeywordSplit[j]) && This_Eng(tmpKeywordSplit[j])) {
                    int idx = -1;
                    int idx_wb = -1;
                    for (int ij = 0; ij < tmpKeywordSplit[j].length(); ij++) {
                        String tmp = tmpKeywordSplit[j].charAt(ij) + "";
                        if (This_thai(tmp.trim())) {
                            idx = INDEX_COMPARE_NAME_THEN;
                            idx_wb = INDEX_WB_NAME_THEN;
                            break;
                        } else if (This_Eng(tmp.trim())) {
                            idx = INDEX_COMPARE_NAME_ENTH;
                            idx_wb = INDEX_WB_NAME_ENTH;
                            break;
                        }
                    }
                    String TmpAll = "";
                    String TmpAll_wb = "";

                    String TmpN = input.DataTable[i][idx];
                    String TmpN_WB = input.DataTable[i][idx_wb];

                    if (idx == INDEX_COMPARE_NAME_THEN) {
                        TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN4_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN1_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN2_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN3_LOCAL] + " "
                                + input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH] + " "
                                + input.DataTable[i][INDEX_ADMIN4_ENG] + " "
                                + input.DataTable[i][INDEX_ADMIN1_ENG] + " "
                                + input.DataTable[i][INDEX_ADMIN2_ENG] + " "
                                + input.DataTable[i][INDEX_ADMIN3_ENG] + " "
                                + input.DataTable[i][INDEX_HNO] + " "
                                + input.DataTable[i][INDEX_TEL] + " "
                                + input.DataTable[i][INDEX_POST];
                        TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_LOCAL] + " "
                                + input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                                + input.DataTable[i][INDEX_HNO] + " "
                                + input.DataTable[i][INDEX_TEL] + " "
                                + input.DataTable[i][INDEX_POST];
                    } else {
                        TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH] + " "
                                + input.DataTable[i][INDEX_ADMIN4_ENG] + " "
                                + input.DataTable[i][INDEX_ADMIN1_ENG] + " "
                                + input.DataTable[i][INDEX_ADMIN2_ENG] + " "
                                + input.DataTable[i][INDEX_ADMIN3_ENG] + " "
                                + input.DataTable[i][INDEX_COMPARE_NAME_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN4_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN1_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN2_LOCAL] + " "
                                + input.DataTable[i][INDEX_ADMIN3_LOCAL] + " "
                                + input.DataTable[i][INDEX_HNO] + " "
                                + input.DataTable[i][INDEX_TEL] + " "
                                + input.DataTable[i][INDEX_POST];
                        TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                                + input.DataTable[i][INDEX_WB_ALL_LOCAL] + " "
                                + input.DataTable[i][INDEX_HNO] + " "
                                + input.DataTable[i][INDEX_TEL] + " "
                                + input.DataTable[i][INDEX_POST];
                    }

                    int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpKeywordSplit[j], tmpKeywordSplit_wb[j]);
                    int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpKeywordSplit[j], tmpKeywordSplit_wb[j]);

                    int OldA = Integer.parseInt(input.DataTable[i][INDEX_GROUP_ALL].trim());
                    int OldN = Integer.parseInt(input.DataTable[i][INDEX_GROUP_NAME].trim());

                    if (OldA > tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }
                    if (OldN > tmpGname) {
                        input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGname);
                    }

                } else if (This_thai(tmpKeywordSplit[j])) {
                    long startTime = System.currentTimeMillis();
                    String TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL] + " "
                            + input.DataTable[i][INDEX_ADMIN4_LOCAL] + " "
                            + input.DataTable[i][INDEX_ADMIN1_LOCAL] + " "
                            + input.DataTable[i][INDEX_ADMIN2_LOCAL] + " "
                            + input.DataTable[i][INDEX_ADMIN3_LOCAL] + " "
                            + input.DataTable[i][INDEX_HNO] + " "
                            + input.DataTable[i][INDEX_TEL] + " "
                            + input.DataTable[i][INDEX_POST];
                    String TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_LOCAL] + " "
                            + input.DataTable[i][INDEX_HNO] + " "
                            + input.DataTable[i][INDEX_TEL] + " "
                            + input.DataTable[i][INDEX_POST];

                    String TmpN = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL];
                    String TmpN_WB = input.DataTable[i][INDEX_WB_NAME_LOCAL];

                    int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpKeywordSplit[j], tmpKeywordSplit_wb[j]);
                    int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpKeywordSplit[j], tmpKeywordSplit_wb[j]);

                    int OldA = Integer.parseInt(input.DataTable[i][INDEX_GROUP_ALL].trim());
                    int OldN = Integer.parseInt(input.DataTable[i][INDEX_GROUP_NAME].trim());

                    if (OldA > tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }
                    if (OldN > tmpGname) {
                        input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGname);
                    }
//                    long process = System.currentTimeMillis() - startTime;
//                    if(process >10)
//                        counter++;
                } else {
                    long startTime = System.currentTimeMillis();
                    String TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH] + " "
                            + input.DataTable[i][INDEX_ADMIN4_ENG] + " "
                            + input.DataTable[i][INDEX_ADMIN1_ENG] + " "
                            + input.DataTable[i][INDEX_ADMIN2_ENG] + " "
                            + input.DataTable[i][INDEX_ADMIN3_ENG] + " "
                            + input.DataTable[i][INDEX_HNO] + " "
                            + input.DataTable[i][INDEX_TEL] + " "
                            + input.DataTable[i][INDEX_POST];
                    String TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                            + input.DataTable[i][INDEX_HNO] + " "
                            + input.DataTable[i][INDEX_TEL] + " "
                            + input.DataTable[i][INDEX_POST];

                    String TmpN = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH];
                    String TmpN_WB = input.DataTable[i][INDEX_WB_NAME_ENGLISH];

                    //long t1_tmpGall = System.currentTimeMillis();
                    int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpKeywordSplit[j], tmpKeywordSplit_wb[j]);
                    //long t2_tmpGall = System.currentTimeMillis() - t1_tmpGall;
                    //System.out.println(t2_tmpGall);

                    //long t1_tmpGname = System.currentTimeMillis();
                    int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpKeywordSplit[j], tmpKeywordSplit_wb[j]);
                    //long t2_tmpGname = System.currentTimeMillis() - t1_tmpGname;
                    //System.out.println(t2_tmpGname);

                    int OldA = Integer.parseInt(input.DataTable[i][INDEX_GROUP_ALL].trim());
                    int OldN = Integer.parseInt(input.DataTable[i][INDEX_GROUP_NAME].trim());

                    if (OldA > tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }
                    if (OldN > tmpGname) {
                        input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGname);
                    }
                    long process = System.currentTimeMillis() - startTime;
                    //if(process >10)
                    //    counter++;

                }

            }
        }
        //long t2 = System.currentTimeMillis() - t1;
        return input;
    }

    public _data_result InputGroupToTable_sound(_data_result input) {
        String tmpKeyword = KEYWORD_SEARCH;
        String tmpSound = KEYWORD_SEARCH_SOUNDEX;
        for (int i = 0; i < input.DataTable.length; i++) {
            //for (int j = 0; j < loop; j++) {

            String trimString = tmpSound.replaceAll("^\\s+", "");
            if (CheckNullEmptyString(trimString.trim())) {
                continue;
            }

            if (This_thai(tmpKeyword) && This_Eng(tmpKeyword)) {
                int idx = -1;
                int idx_wb = -1;
                for (int ij = 0; ij < tmpKeyword.length(); ij++) {
                    String tmp = tmpKeyword.charAt(ij) + "";
                    if (This_thai(tmp.trim())) {
                        idx = INDEX_SOUND_NAME_THEN;
                        idx_wb = INDEX_SOUND_NAME_THEN;
                        break;
                    } else if (This_Eng(tmp.trim())) {
                        idx = INDEX_SOUND_NAME_ENTH;
                        idx_wb = INDEX_SOUND_NAME_ENTH;
                        break;
                    }
                }
                String TmpAll = "";
                String TmpAll_wb = "";

                String TmpN = input.DataTable[i][idx];
                String TmpN_WB = input.DataTable[i][idx_wb];

                if (idx == INDEX_SOUND_NAME_THEN) {
                    TmpAll = input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                            + input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                            + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                            + input.DataTable[i][INDEX_Telephone_sound] + " "
                            + input.DataTable[i][INDEX_PostCode_sound];
                    TmpAll_wb = input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                            + input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                            + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                            + input.DataTable[i][INDEX_Telephone_sound] + " "
                            + input.DataTable[i][INDEX_PostCode_sound];
                } else {
                    TmpAll = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                            + input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                            + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                            + input.DataTable[i][INDEX_Telephone_sound] + " "
                            + input.DataTable[i][INDEX_PostCode_sound];
                    TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                            + input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                            + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                            + input.DataTable[i][INDEX_Telephone_sound] + " "
                            + input.DataTable[i][INDEX_PostCode_sound];
                }

                int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpSound, tmpSound);
                int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpSound, tmpSound);

                int OldA = Integer.parseInt(input.DataTable[i][INDEX_GROUP_ALL].trim());
                int OldN = Integer.parseInt(input.DataTable[i][INDEX_GROUP_NAME].trim());

                if (OldA > tmpGall) {
                    input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                }
                if (OldN > tmpGname) {
                    input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGname);
                }

            } else if (This_thai(tmpKeyword)) {
                //long startTime = System.currentTimeMillis();
                String TmpAll = input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                        + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                        + input.DataTable[i][INDEX_Telephone_sound] + " "
                        + input.DataTable[i][INDEX_PostCode_sound];
                String TmpAll_wb = input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                        + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                        + input.DataTable[i][INDEX_Telephone_sound] + " "
                        + input.DataTable[i][INDEX_PostCode_sound];

                String TmpN = input.DataTable[i][INDEX_SOUND_LOCAL];
                String TmpN_WB = input.DataTable[i][INDEX_SOUND_LOCAL];

                int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpSound, tmpSound);
                int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpSound, tmpSound);

                int OldA = Integer.parseInt(input.DataTable[i][INDEX_GROUP_ALL].trim());
                int OldN = Integer.parseInt(input.DataTable[i][INDEX_GROUP_NAME].trim());

                if (OldA > tmpGall) {
                    input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                }
                if (OldN > tmpGname) {
                    input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGname);
                }
//                    long process = System.currentTimeMillis() - startTime;
//                    if(process >10)
//                        counter++;
            } else {
                long startTime = System.currentTimeMillis();
                String TmpAll = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                        + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                        + input.DataTable[i][INDEX_Telephone_sound] + " "
                        + input.DataTable[i][INDEX_PostCode_sound];
                String TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
                        + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                        + input.DataTable[i][INDEX_Telephone_sound] + " "
                        + input.DataTable[i][INDEX_PostCode_sound];

                String TmpN = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH];
                String TmpN_WB = input.DataTable[i][INDEX_WB_NAME_ENGLISH];

                //long t1_tmpGall = System.currentTimeMillis();
                int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpSound, tmpSound);
                //long t2_tmpGall = System.currentTimeMillis() - t1_tmpGall;
                //System.out.println(t2_tmpGall);

                //long t1_tmpGname = System.currentTimeMillis();
                int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpSound, tmpSound);
                //long t2_tmpGname = System.currentTimeMillis() - t1_tmpGname;
                //System.out.println(t2_tmpGname);

                int OldA = Integer.parseInt(input.DataTable[i][INDEX_GROUP_ALL].trim());
                int OldN = Integer.parseInt(input.DataTable[i][INDEX_GROUP_NAME].trim());

                if (OldA > tmpGall) {
                    input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                }
                if (OldN > tmpGname) {
                    input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGname);
                }
                long process = System.currentTimeMillis() - startTime;
                //if(process >10)
                //    counter++;

            }

            //}
        }
        //long t2 = System.currentTimeMillis() - t1;
        return input;
    }

    private String[][] SortGroupAll(String[][] input) {

        Arrays.sort(input, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final Double time1 = Double.parseDouble(entry1[INDEX_GROUP_ALL]);
                final Double time2 = Double.parseDouble(entry2[INDEX_GROUP_ALL]);
                return time1.compareTo(time2);
            }
        });

        return input;
    }

    private String[][] SortGroupName(String[][] input) {

        Arrays.sort(input, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final Double time1 = Double.parseDouble(entry1[INDEX_GROUP_NAME]);
                final Double time2 = Double.parseDouble(entry2[INDEX_GROUP_NAME]);
                return time1.compareTo(time2);
            }
        });

        return input;
    }

    private _data_result SortGroupAction(_data_result input) {
        input.DataTable = SortGroupName(input.DataTable);
        input.DataTable = SortGroupAll(input.DataTable);
        return input;
    }

 
}
