/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.document.Document;
import static org.apache.lucene.document.LatLonDocValuesField.newDistanceSort;
import static org.apache.lucene.document.LatLonPoint.newDistanceFeatureQuery;
//import static org.apache.lucene.document.LatLonPoint.newDistanceQuery;
import org.apache.lucene.geo.Polygon;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.*;
//import org.apache.lucene.search.FieldValueQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;;
//import org.apache.lucene.spatial.geopoint.document.GeoPointField;
//import org.apache.lucene.spatial.geopoint.search.GeoPointInPolygonQuery;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.FeatureField;
import static org.apache.lucene.document.LatLonPoint.newPolygonQuery;
import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.document.LatLonDocValuesField;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.search.BooleanClause.Occur;


/**
 *
 * @author GT_4953
 */
public class _cl_NostraSearch {

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
    private final int INDEX_ADMIN_ID1 = 41;
    private final int INDEX_ADMIN_ID2 = 42;
    private final int INDEX_ADMIN_ID3 = 43;

    // INDEX FOR SOUNDEX
    private final int INDEX_SOUND_NAME_LOCAL = 44;
    private final int INDEX_SOUND_NAME_THEN = 45;
    private final int INDEX_SOUND_NAME_ENTH = 46;
    private final int INDEX_HouseNumber_sound = 47;
    private final int INDEX_Telephone_sound = 48;
    private final int INDEX_PostCode_sound = 49;

    private final int INDEX_SOUND_LOCAL = 50;
    
    private final int INDEX_EXTRA_CONTENT = 51;

    //SUM INDEX
    private final int NUM_OF_RES_INDEX = 44;// NORMAL
    private final int NUM_OF_RES_INDEX_SOUNDEX = 52;// SOUNDEX

    private int INDEX_Master_ID = 0;

    private String KEYWORD_SEARCH_SET = "";
    private String KEYWORD_SEARCH_SET_WB = "";

    private String KEYWORD_SEARCH_SOUNDEX = "";
    private String KEYWORD_SEARCH_SOUNDEX_DELI = "";

    private String KEYWORD_SEARCH = "";

    public String URL_WB;
    private Analyzer analyzer_th = new WhitespaceAnalyzer();
    private Analyzer analyzer_th_big = new ThaiAnalyzer();
    private int PROCESS_ROW;
    private List<String> CHAR_DATA;
    private List<String> G_WORD_DATA;
    private _data_database DB = new _data_database();

    //cont
    private int CASE_NO_ADMIN = 0;
    private int CASE_ADMIN_CODE_ONLY = 1;
    private int CASE_ADMIN_STRING_ONLY = 2;
    private int CASE_ADMIN_MIX = 3;

    private int CASE_SEARCH_NORMAL = 0;
    private int CASE_SEARCH_NEARBY = 1;

//    public _cl_NostraSearch(int row_p, List<String> c, List<String> g) {
//        this.PROCESS_ROW = row_p;
//        this.CHAR_DATA = c;
//        this.G_WORD_DATA = g;
//    }
    public _cl_NostraSearch(_data_InitNostraSearch init) {
        this.PROCESS_ROW = init.PROCESS_ROW;
        this.CHAR_DATA = init.CHAR_DATA;
        this.G_WORD_DATA = init.G_WORD_DATA;
        this.DB = init.DB;
    }

    /*
     * Major Function
     */
    // soundex search
    public _data_result NostraSearch(_data_input_soundex input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search_soundex(query, input.keyword, input.soundex_keyword, input.soundex_keyword_delimeter
            );

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            if (input.radius > 0) {
                query = GenQuery_Filter_Radius(query, input.Lat, input.Lon, input.radius);
            }
            query = GenQuery_Filter_Admin123(query, input.AdminLevel_1, input.AdminLevel_2, input.AdminLevel_3);
            if (!CheckNullEmptyString(input.AdminLevel_4)) {
                query = GenQuery_Filter_Admin4(query, input.AdminLevel_4);
            }
            if (!CheckNullEmptyString(input.PostCode)) {
                query = GenQuery_Filter_PostCode(query, input.PostCode);
            }

            if (!CheckNullEmptyString(input.telephone)) {
                query = GenQuery_Filter_Telephone(query, input.telephone);
            }

            if (!CheckNullEmptyString(input.hno)) {
                query = GenQuery_Filter_Hno(query, input.hno);
            }

            // Search
            int optionSearch = CASE_SEARCH_NORMAL;
            if(CheckNullEmptyString(input.keyword.trim())
                    && !CheckNullEmptyString(input.Lat)
                    && !CheckNullEmptyString(input.Lon))
            {
                optionSearch = CASE_SEARCH_NEARBY;
            }
            
            TopFieldDocs tfd = Search_Action(searcher, query, PROCESS_ROW, input.Lat, input.Lon,optionSearch);
            _data_result res = GetDataFromTopDoc(searcher, tfd, input.Lat, input.Lon);

            // Group result
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable_sound(res);
                res = SortGroupAction(res);
                //res = InputLCS_sound(res);
                //res = SplitResultSet(res);
                //res = SortGroupAction_LCS(res);
                return res;
            }

        } catch (Exception e) {
            return null;
        }

    }

    public int NostraSearch_TotalResult(_data_input_soundex input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search_soundex(query, input.keyword, input.soundex_keyword, input.soundex_keyword_delimeter
            );

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            if (input.radius > 0) {
                query = GenQuery_Filter_Radius(query, input.Lat, input.Lon, input.radius);
            }
            query = GenQuery_Filter_Admin123(query, input.AdminLevel_1, input.AdminLevel_2, input.AdminLevel_3);
            if (!CheckNullEmptyString(input.AdminLevel_4)) {
                query = GenQuery_Filter_Admin4(query, input.AdminLevel_4);
            }
            if (!CheckNullEmptyString(input.PostCode)) {
                query = GenQuery_Filter_PostCode(query, input.PostCode);
            }

            if (!CheckNullEmptyString(input.telephone)) {
                query = GenQuery_Filter_Telephone(query, input.telephone);
            }

            if (!CheckNullEmptyString(input.hno)) {
                query = GenQuery_Filter_Hno(query, input.hno);
            }
            
            query.add(new QueryParser("Master_ID", analyzer_th).parse("0"), BooleanClause.Occur.FILTER);
            
            Query qq = query.build();
            TopDocs TFD = searcher.search(qq, 10);
            long t = TFD.totalHits.value;
            return (int)t;
            //return 0;

        } catch (Exception e) {
            return 0;
        }

    }

    // Polygon Search [Function AlongTheRoute Nearby]
    public _data_result NostraSearch(_data_input_nearby_soundex input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search_soundex(query, input.keyword, input.soundex_keyword, input.soundex_keyword_delimeter
            );

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            query = GenQuery_Layer_Landmark_Only(query);
            query = GenQuery_Filter_Polygon(query, input.PolygString_arr);

            // Search
            int optionSearch = CASE_SEARCH_NORMAL;
            if(CheckNullEmptyString(input.keyword.trim())
                    && !CheckNullEmptyString(input.Lat)
                    && !CheckNullEmptyString(input.Lon))
            {
                optionSearch = CASE_SEARCH_NEARBY;
            }
            
            TopFieldDocs tfd = Search_Action(searcher, query, PROCESS_ROW, input.Lat, input.Lon,optionSearch);
            _data_result res = GetDataFromTopDoc(searcher, tfd, input.Lat, input.Lon);

            // Group result 
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable_sound(res);
                res = SortGroupAction(res);
                return res;
            }

        } catch (Exception e) {
            return null;
        }

    }

    public _data_result NostraSearch(_data_input_nearby input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search(query, input.keyword, input.search_keyword, input.search_keyword_WB);

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            query = GenQuery_Layer_Landmark_Only(query);
            query = GenQuery_Filter_Polygon(query, input.PolygString_arr);

            // Search
            int optionSearch = CASE_SEARCH_NORMAL;
            if(CheckNullEmptyString(input.keyword.trim())
                    && !CheckNullEmptyString(input.Lat)
                    && !CheckNullEmptyString(input.Lon))
            {
                optionSearch = CASE_SEARCH_NEARBY;
            }
            
            TopFieldDocs tfd = Search_Action(searcher, query, PROCESS_ROW, input.Lat, input.Lon,optionSearch);
            _data_result res = GetDataFromTopDoc(searcher, tfd, input.Lat, input.Lon);

            // Group result 
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable(res);
                res = SortGroupAction(res);
                return res;
            }

        } catch (Exception e) {
            return null;
        }

    }

    // RouteID Serch [Function AlongTheRoute Closet]
    public _data_result NostraSearch(_data_input_close_soundex input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search_soundex(query, input.keyword, input.soundex_keyword, input.soundex_keyword_delimeter
            );
            
            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            if (!CheckNullEmptyString(input.routeID)) {
                query = GenQuery_Filter_RouteID(query, input.routeID);
            }

            // Search
            int optionSearch = CASE_SEARCH_NORMAL;
            if(CheckNullEmptyString(input.keyword.trim())
                    && !CheckNullEmptyString(input.Lat)
                    && !CheckNullEmptyString(input.Lon))
            {
                optionSearch = CASE_SEARCH_NEARBY;
            }
            
            TopFieldDocs tfd = Search_Action(searcher, query, PROCESS_ROW, input.Lat, input.Lon,optionSearch);
            _data_result res = GetDataFromTopDoc(searcher, tfd, input.Lat, input.Lon);

            // Group result 
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable_sound(res);
                res = SortGroupAction(res);
                return res;
            }

        } catch (Exception e) {
            return null;
        }

    }

    //
    public _data_result NostraSearch(_data_input_close input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search(query, input.keyword, input.search_keyword, input.search_keyword_WB);

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            if (!CheckNullEmptyString(input.routeID)) {
                query = GenQuery_Filter_RouteID(query, input.routeID);
            }

            // Search
            int optionSearch = CASE_SEARCH_NORMAL;
            if(CheckNullEmptyString(input.keyword.trim())
                    && !CheckNullEmptyString(input.Lat)
                    && !CheckNullEmptyString(input.Lon))
            {
                optionSearch = CASE_SEARCH_NEARBY;
            }
            
            TopFieldDocs tfd = Search_Action(searcher, query, PROCESS_ROW, input.Lat, input.Lon,optionSearch);
            _data_result res = GetDataFromTopDoc(searcher, tfd, input.Lat, input.Lon);

            // Group result 
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable(res);
                res = SortGroupAction(res);
                return res;
            }

        } catch (Exception e) {
            return null;
        }

    }

    // text search ***
    public _data_result NostraSearch(_data_input input, IndexSearcher searcher) {
        try {
            // GenQuery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search(query, input.keyword, input.search_keyword, input.search_keyword_WB);

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            if (input.radius > 0) {
                query = GenQuery_Filter_Radius(query, input.Lat, input.Lon, input.radius);
            }
            query = GenQuery_Filter_Admin123(query, input.AdminLevel_1, input.AdminLevel_2, input.AdminLevel_3);
            if (!CheckNullEmptyString(input.AdminLevel_4)) {
                query = GenQuery_Filter_Admin4(query, input.AdminLevel_4);
            }
            if (!CheckNullEmptyString(input.PostCode)) {
                query = GenQuery_Filter_PostCode(query, input.PostCode);
            }

            if (!CheckNullEmptyString(input.telephone)) {
                query = GenQuery_Filter_Telephone(query, input.telephone);
            }

            if (!CheckNullEmptyString(input.hno)) {
                query = GenQuery_Filter_Hno(query, input.hno);
            }

            // Search 
            int optionSearch = CASE_SEARCH_NORMAL;
            if(CheckNullEmptyString(input.keyword.trim())
                    && !CheckNullEmptyString(input.Lat)
                    && !CheckNullEmptyString(input.Lon))
            {
                optionSearch = CASE_SEARCH_NEARBY;
            }
            
            TopFieldDocs tfd = Search_Action(searcher, query, PROCESS_ROW, input.Lat, input.Lon,optionSearch);
            _data_result res = GetDataFromTopDoc(searcher, tfd, input.Lat, input.Lon);

            // Group result 
            if (CheckNullEmptyString(input.keyword)) {
                return res;
            } else {
                res = InputGroupToTable(res);
                res = SortGroupAction(res);
                //res = InputLCS(res);
                //res = SortGroupAction_LCS(res);
                return res;
            }

        } catch (Exception e) {
            return null;
        }

    }

    // big
    public _data_result NostraSearch_big(_data_input input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            //query = GenQuery_Search_soundex(query, input.keyword, input.soundex_keyword, input.soundex_keyword_delimeter);
            query = GenQuery_Search_big(query, input.keyword, input.search_keyword, input.search_keyword_WB);

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            if (input.radius > 0) {
                query = GenQuery_Filter_Radius(query, input.Lat, input.Lon, input.radius);
            }
            query = GenQuery_Filter_Admin123(query, input.AdminLevel_1, input.AdminLevel_2, input.AdminLevel_3);
            if (!CheckNullEmptyString(input.AdminLevel_4)) {
                query = GenQuery_Filter_Admin4(query, input.AdminLevel_4);
            }
            if (!CheckNullEmptyString(input.PostCode)) {
                query = GenQuery_Filter_PostCode(query, input.PostCode);
            }

            if (!CheckNullEmptyString(input.telephone)) {
                query = GenQuery_Filter_Telephone_big(query, input.telephone);
            }

            if (!CheckNullEmptyString(input.hno)) {
                query = GenQuery_Filter_Hno_big(query, input.hno);
            }
            // Search
            int optionSearch = CASE_SEARCH_NORMAL;
            if(CheckNullEmptyString(input.keyword.trim())
                    && !CheckNullEmptyString(input.Lat)
                    && !CheckNullEmptyString(input.Lon))
            {
                optionSearch = CASE_SEARCH_NEARBY;
            }
            
            TopFieldDocs tfd = Search_Action(searcher, query, PROCESS_ROW, input.Lat, input.Lon,optionSearch);
            _data_result res = GetDataFromTopDoc(searcher, tfd, input.Lat, input.Lon);

            // Group result 
            if (CheckNullEmptyString(input.keyword) && CheckNullEmptyString(input.search_keyword)) {
                return res;
            } else {
                res = InputGroupToTable_big(res);
                res = SortGroupAction(res);
                return res;
            }

        } catch (Exception e) {
            return null;
        }

    }

    public int NostraSearch_TotalResult(_data_input input, IndexSearcher searcher) {
        try {
            // GenQuery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_Search(query, input.keyword, input.search_keyword, input.search_keyword_WB);

            if (!CheckNullEmptyString(input.Category)) {
                query = GenQuery_Filter_Categoty(query, input.Category);
            }
            if (!CheckNullEmptyString(input.LocalCatCode)) {
                query = GenQuery_Filter_LocalCatCode(query, input.LocalCatCode);
            }
            if (input.radius > 0) {
                query = GenQuery_Filter_Radius(query, input.Lat, input.Lon, input.radius);
            }
            query = GenQuery_Filter_Admin123(query, input.AdminLevel_1, input.AdminLevel_2, input.AdminLevel_3);
            if (!CheckNullEmptyString(input.AdminLevel_4)) {
                query = GenQuery_Filter_Admin4(query, input.AdminLevel_4);
            }
            if (!CheckNullEmptyString(input.PostCode)) {
                query = GenQuery_Filter_PostCode(query, input.PostCode);
            }

            if (!CheckNullEmptyString(input.telephone)) {
                query = GenQuery_Filter_Telephone(query, input.telephone);
            }

            if (!CheckNullEmptyString(input.hno)) {
                query = GenQuery_Filter_Hno(query, input.hno);
            }
       
////            TopScoreDocCollector collector = TopScoreDocCollector.create(10);
////            searcher.search(query.build(), collector);
////            ScoreDoc[] hits = collector.topDocs().scoreDocs;
////            return collector.getTotalHits();
            return 0;

        } catch (Exception e) {
            return 0;
        }

    }

    public _data_result NostraSearch_ID(_data_input_nid input, IndexSearcher searcher) {
        try {
            // Genquery
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query = GenQuery_NostraID(query, input.NostraID);
            query = GenQuery_Filter_MasterID(query, "0 6");

            // Search
            TopFieldDocs tfd = Search_Action(searcher, query, 1, "", "",CASE_SEARCH_NORMAL);
            _data_result res = GetDataFromTopDoc(searcher, tfd, "", "");

            return res;

        } catch (Exception e) {
            return null;
        }
    }

    /*
     *  lucene Function
     */
    public BooleanQuery.Builder GenQuery_Search(BooleanQuery.Builder booleanQuery, String keyword, String search_keyword, String search_keyword_WB
    ) throws ParseException {

        if (CheckNullEmptyString(keyword)) {

            booleanQuery.add(new QueryParser("text_search", analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

        } else {
            KEYWORD_SEARCH_SET = search_keyword;
            KEYWORD_SEARCH_SET_WB = search_keyword_WB;

            if (CheckNullEmptyString(KEYWORD_SEARCH_SET_WB)) {
                booleanQuery.add(new QueryParser("text_search", analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

            } else {

                String TmpWB = KEYWORD_SEARCH_SET_WB.replace("|", " ");

                TmpWB = TmpWB.replace("/", "\\/");
                booleanQuery.add(new QueryParser("text_search", analyzer_th).parse(TmpWB), BooleanClause.Occur.MUST);
            }
        }

        return booleanQuery;
    }

    private BooleanQuery.Builder GenQuery_Search_soundex(//_data_input_soundex input
            BooleanQuery.Builder booleanQuery, String keyword, String soundex_keyword, String soundex_keyword_delimeter
    ) throws ParseException {

        if (CheckNullEmptyString(keyword)) {
            booleanQuery.add(new QueryParser("soundex", analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

        } else {
            KEYWORD_SEARCH = keyword;
            KEYWORD_SEARCH_SOUNDEX = soundex_keyword;
            KEYWORD_SEARCH_SOUNDEX_DELI = soundex_keyword_delimeter;
            if (CheckNullEmptyString(KEYWORD_SEARCH_SOUNDEX)) {
                booleanQuery.add(new QueryParser("soundex", analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

            } else {
                String Tmp = "";
                String Tmp2 = KEYWORD_SEARCH_SOUNDEX.replace("|", " ");
                Tmp2 = Tmp2.replaceAll("[ ]+", " ");
                String[] Tmp_arr = Tmp2.trim().split(" ");

                for (int i = 0; i < Tmp_arr.length; i++) {
                    Tmp += "\"" + Tmp_arr[i] + "\" ";
                }

                booleanQuery.add(new QueryParser("soundex", analyzer_th).parse(Tmp.trim()), BooleanClause.Occur.MUST);
            }
        }

        return booleanQuery;
    }
    
    private BooleanQuery.Builder GenQuery_Total_Search_soundex(//_data_input_soundex input
            BooleanQuery.Builder booleanQuery, String keyword, String soundex_keyword, String soundex_keyword_delimeter
    ) throws ParseException {

        String fieldIndex = "Total_sound";
        if (CheckNullEmptyString(keyword)) {
            booleanQuery.add(new QueryParser(fieldIndex, analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

        } else {
            KEYWORD_SEARCH = keyword;
            KEYWORD_SEARCH_SOUNDEX = soundex_keyword;
            KEYWORD_SEARCH_SOUNDEX_DELI = soundex_keyword_delimeter;
            if (CheckNullEmptyString(KEYWORD_SEARCH_SOUNDEX)) {
                booleanQuery.add(new QueryParser(fieldIndex, analyzer_th).parse("\\*"), BooleanClause.Occur.SHOULD);

            } else {
                String Tmp = "";
                String Tmp2 = KEYWORD_SEARCH_SOUNDEX.replace("|", " ");
                Tmp2 = Tmp2.replaceAll("[ ]+", " ");
                String[] Tmp_arr = Tmp2.trim().split(" ");

                for (int i = 0; i < Tmp_arr.length; i++) {
                    Tmp += "\"" + Tmp_arr[i] + "\" ";
                }

                booleanQuery.add(new QueryParser(fieldIndex, analyzer_th).parse(Tmp.trim()), BooleanClause.Occur.MUST);
            }
        }

        return booleanQuery;
    }

    public BooleanQuery.Builder GenQuery_Search_big(BooleanQuery.Builder booleanQuery, String keyword, String search_keyword, String search_keyword_WB
    ) throws ParseException {

        if (CheckNullEmptyString(keyword)) {

            booleanQuery.add(new QueryParser("text_search", analyzer_th_big).parse("\\*"), BooleanClause.Occur.SHOULD);

        } else {
            KEYWORD_SEARCH_SET = search_keyword;
            KEYWORD_SEARCH_SET_WB = search_keyword_WB;

            if (CheckNullEmptyString(KEYWORD_SEARCH_SET)) {
                booleanQuery.add(new QueryParser("text_search", analyzer_th_big).parse("\\*"), BooleanClause.Occur.SHOULD);

            } else {

                String TmpWB = KEYWORD_SEARCH_SET.replace("|", " ");

                TmpWB = TmpWB.replace("/", "\\/");
                booleanQuery.add(new QueryParser("text_search", analyzer_th_big).parse(TmpWB), BooleanClause.Occur.MUST);
            }
        }

        return booleanQuery;
    }

    private BooleanQuery.Builder GenQuery_Filter_Categoty(BooleanQuery.Builder q, String Category) throws ParseException {
        String[] cat = Category.split("\\|");
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
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_LocalCatCode(BooleanQuery.Builder q, String LocalCatCode) throws ParseException {
        String tmp = LocalCatCode.replace("|", " ");

        q.add(new QueryParser("LocalCatCode", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_Radius(BooleanQuery.Builder q, String Lat, String Lon, double radius) {
        
        //Query q_geodis = newDistanceQuery("LatLonPoint", Double.parseDouble(Lat), Double.parseDouble(Lon), radius);
        Query q_geodis = newDistanceFeatureQuery("LatLonData", 3, Double.parseDouble(Lat), Double.parseDouble(Lon), radius);
        q.add(q_geodis, BooleanClause.Occur.FILTER);
        return q;
    }
////////
////////    private BooleanQuery.Builder GenQuery_Filter_Admin123(BooleanQuery.Builder q, String AdminLevel_1, String AdminLevel_2, String AdminLevel_3) throws ParseException {
////////
////////        _cl_database db = new _cl_database(DB);
////////        boolean flag123 = false;
////////        String adminCode = "";
////////        //query change adminstring to code
////////        if (!CheckNullEmptyString(AdminLevel_1)
////////                && CheckNullEmptyString(AdminLevel_2)
////////                && CheckNullEmptyString(AdminLevel_3)) {
////////            try {
////////                ResultSet rs = db.QueryAdminID_1(AdminLevel_1);
////////                int rowcount = 0;
////////                if (rs != null) {
//////////                    if (rs.last()) {
//////////                        rowcount = rs.getRow();
//////////                        rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
//////////                    }
//////////                    if (rowcount != 0) {
////////                    rs.next();
////////                    AdminLevel_1 = rs.getString("admin_id1").trim();
////////                    //}
////////                }
////////
////////            } catch (Exception ex) {
////////                String tmpex = ex.toString();
////////            }
////////            flag123 = true;
////////        } else if (!CheckNullEmptyString(AdminLevel_1)
////////                && !CheckNullEmptyString(AdminLevel_2)
////////                && CheckNullEmptyString(AdminLevel_3)) {
////////            try {
////////                ResultSet rs = db.QueryAdminID_12(AdminLevel_1, AdminLevel_2);
////////                int rowcount = 0;
////////                if (rs != null) {
//////////                    if (rs.last()) {
//////////                        rowcount = rs.getRow();
//////////                        rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
//////////                    }
////////                    //if (rowcount != 0) {
////////                    rs.next();
////////                    AdminLevel_1 = rs.getString("admin_id1").trim();
////////                    AdminLevel_2 = rs.getString("admin_id2").trim();
////////                    //}
////////                }
////////
////////            } catch (Exception ex) {
////////
////////            }
////////            flag123 = true;
////////        } else if (!CheckNullEmptyString(AdminLevel_1)
////////                && !CheckNullEmptyString(AdminLevel_2)
////////                && !CheckNullEmptyString(AdminLevel_3)) {
////////            try {
////////                ResultSet rs = db.QueryAdminID_123(AdminLevel_1, AdminLevel_2, AdminLevel_3);
////////                int rowcount = 0;
////////                if (rs != null) {
//////////                    if (rs.last()) {
//////////                        rowcount = rs.getRow();
//////////                        rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
//////////                    }
////////                    //if (rowcount != 0) {
////////                    rs.next();
////////                    AdminLevel_1 = rs.getString("admin_id1").trim();
////////                    AdminLevel_2 = rs.getString("admin_id2").trim();
////////                    AdminLevel_3 = rs.getString("admin_id3").trim();
////////                    //}
////////                }
////////
////////            } catch (Exception ex) {
////////
////////            }
////////            flag123 = true;
////////        } else if (CheckNullEmptyString(AdminLevel_1)
////////                && !CheckNullEmptyString(AdminLevel_2)
////////                && !CheckNullEmptyString(AdminLevel_3)) {
////////            try {
////////                ResultSet rs = db.QueryAdminID_23(AdminLevel_2, AdminLevel_3);
////////                if (rs != null) {
////////                    while (rs.next()) {
////////                        adminCode += rs.getString("admin_id123").trim() + " ";
////////                    }
////////                }
////////
////////            } catch (Exception ex) {
////////
////////            }
////////        } else if (CheckNullEmptyString(AdminLevel_1)
////////                && CheckNullEmptyString(AdminLevel_2)
////////                && !CheckNullEmptyString(AdminLevel_3)) {
////////            try {
////////                ResultSet rs = db.QueryAdminID_3(AdminLevel_3);
////////                if (rs != null) {
////////                    while (rs.next()) {
////////                        adminCode += rs.getString("admin_id123").trim() + " ";
////////                    }
////////                }
////////
////////            } catch (Exception ex) {
////////
////////            }
////////        } else if (!CheckNullEmptyString(AdminLevel_1)
////////                && CheckNullEmptyString(AdminLevel_2)
////////                && !CheckNullEmptyString(AdminLevel_3)) {
////////            try {
////////                ResultSet rs = db.QueryAdminID_13(AdminLevel_1, AdminLevel_3);
////////                if (rs != null) {
////////                    while (rs.next()) {
////////                        adminCode += rs.getString("admin_id123").trim() + " ";
////////                    }
////////                }
////////
////////            } catch (Exception ex) {
////////
////////            }
////////        } else if (CheckNullEmptyString(AdminLevel_1)
////////                && !CheckNullEmptyString(AdminLevel_2)
////////                && CheckNullEmptyString(AdminLevel_3)) {
////////            try {
////////                ResultSet rs = db.QueryAdminID_2(AdminLevel_2);
////////                if (rs != null) {
////////                    while (rs.next()) {
////////                        adminCode += rs.getString("admin_id123").trim() + " ";
////////                    }
////////                }
////////
////////            } catch (Exception ex) {
////////
////////            }
////////        }
////////
////////        //case admin 1 / 12 /123
////////        if (flag123) {
////////            if (!CheckNullEmptyString(AdminLevel_1)) {
////////                String tmpid = AdminLevel_1.trim();
////////                if (!CheckNullEmptyString(AdminLevel_2)) {
////////                    tmpid += AdminLevel_2.trim();
////////                    if (!CheckNullEmptyString(AdminLevel_3)) {
////////                        tmpid += AdminLevel_3.trim();
////////                    } else {
////////                        tmpid += "*";
////////                    }
////////                } else {
////////                    tmpid += "*";
////////                }
////////                q.add(new QueryParser("Admin_Code", analyzer_th).parse(tmpid), BooleanClause.Occur.FILTER);
////////            }
////////        } //case admin 13 / 23 / 2 / 3
////////        //        else if (!CheckNullEmptyString(AdminLevel_1)
////////        //                && !CheckNullEmptyString(AdminLevel_2)
////////        //                && !CheckNullEmptyString(AdminLevel_3)) 
////////        else if (!CheckNullEmptyString(adminCode)) {
////////            q.add(new QueryParser("Admin_Code", analyzer_th).parse(adminCode.trim()), BooleanClause.Occur.FILTER);
////////        } else {
////////            //q.add(new QueryParser("Admin_Code", analyzer_th).parse(adminCode.trim()), BooleanClause.Occur.FILTER);
////////        }
////////
////////        /*
////////        if (!CheckNullEmptyString(AdminLevel_1)) {
////////            String tmp = AdminLevel_1.trim();
////////            q.add(new QueryParser("Admin_ID1", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
////////        }
////////        if (!CheckNullEmptyString(AdminLevel_2)) {
////////            String tmp = AdminLevel_2.trim();
////////            q.add(new QueryParser("Admin_ID2", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
////////        }
////////        if (!CheckNullEmptyString(AdminLevel_3)) {
////////            String tmp = AdminLevel_3.trim();
////////            q.add(new QueryParser("Admin_ID3", analyzer_th).parse(tmp), BooleanClause.Occur.FILTER);
////////        }
////////         */
////////        return q;
////////    }
////////

    private BooleanQuery.Builder GenQuery_Filter_Admin123(BooleanQuery.Builder q, String AdminLevel_1, String AdminLevel_2, String AdminLevel_3) {
        _cl_database db = new _cl_database(DB);
        String adminCode = "";
        if (!CheckNullEmptyString(AdminLevel_1)
                || !CheckNullEmptyString(AdminLevel_2)
                || !CheckNullEmptyString(AdminLevel_3)) {
            try {
                ResultSet rs = db.QueryAdminID_Dynamic(AdminLevel_1, AdminLevel_2, AdminLevel_3);
                if (rs != null) {
                    while (rs.next()) {
                        adminCode += rs.getString("admin_id123").trim() + " ";
                    }
                }
                q.add(new QueryParser("Admin_Code", analyzer_th).parse(adminCode.trim()), BooleanClause.Occur.FILTER);
            } catch (Exception ex) {
            }
        }
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_Admin4_big(BooleanQuery.Builder q, String AdminLevel_4) throws ParseException {
        if (This_thai(AdminLevel_4)) {
            q.add(new QueryParser("AdminLevel_4_Local", analyzer_th_big).parse(AdminLevel_4), BooleanClause.Occur.FILTER);
        } else {
            q.add(new QueryParser("AdminLevel_4_English", analyzer_th_big).parse(AdminLevel_4), BooleanClause.Occur.FILTER);
        }
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_Admin4(BooleanQuery.Builder q, String AdminLevel_4) throws ParseException {
        if (This_thai(AdminLevel_4)) {
            q.add(new QueryParser("AdminLevel_4_Local", analyzer_th).parse(AdminLevel_4), BooleanClause.Occur.FILTER);
        } else {
            q.add(new QueryParser("AdminLevel_4_English", analyzer_th).parse(AdminLevel_4), BooleanClause.Occur.FILTER);
        }
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_Hno(BooleanQuery.Builder q, String hno) throws ParseException {
        q.add(new QueryParser("HouseNumber", analyzer_th).parse(hno.replace("/", "\\/")), BooleanClause.Occur.FILTER);
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_Hno_big(BooleanQuery.Builder q, String hno) throws ParseException {
        //q.add(new QueryParser("HouseNumber", analyzer_th).createMinShouldMatchQuery("HouseNumber_SearchField", hno.replace("/", "\\/"), 100), BooleanClause.Occur.MUST);
        //q.add(new QueryParser("HouseNumber", analyzer_th_big).parse(hno.replace("/", "\\/")), BooleanClause.Occur.FILTER);
        q.add(new TermQuery(new Term("HouseNumber_StringField", hno.replace("/", "\\/"))), BooleanClause.Occur.FILTER);
        //q.add(new query
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_Telephone(BooleanQuery.Builder q, String tel) throws ParseException {
        q.add(new QueryParser("Telephone", analyzer_th).parse(tel), BooleanClause.Occur.FILTER);
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_Telephone_big(BooleanQuery.Builder q, String tel) throws ParseException {
        q.add(new QueryParser("Telephone", analyzer_th_big).parse(tel), BooleanClause.Occur.FILTER);
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_PostCode(BooleanQuery.Builder q, String PostCode) throws ParseException {
        q.add(new QueryParser("PostCode", analyzer_th).parse(PostCode), BooleanClause.Occur.FILTER);

        return q;
    }

    private TopFieldDocs Search_Action(IndexSearcher searcher, BooleanQuery.Builder booleanQuery, int MaxResult, String lat, String lon, int optionSearch) {
        int hitsPerPage = MaxResult;

        try {
            Sort sort;
            if (optionSearch == CASE_SEARCH_NEARBY) {
                sort = new Sort(
                    new SortField[]{SortField.FIELD_SCORE,
                    newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
                });
            } else if (CheckNullEmptyString(lat) || CheckNullEmptyString(lon)) {
                sort = new Sort(
                    new SortField[]{SortField.FIELD_SCORE,
                    new SortField("hitscoreSort", SortField.Type.DOC.LONG, true)
                });
            } else {
                sort = new Sort(
                    new SortField[]{SortField.FIELD_SCORE,
                    new SortField("hitscoreSort", SortField.Type.DOC.LONG, true),
                    newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
                });
            }
//            Query qq = booleanQuery.build();
//            Query qq = new FunctionScoreQuery( booleanQuery.build(),DoubleValuesSource.SCORES);
//            TopFieldDocs TFD = searcher.search(qq, hitsPerPage, sort);
//            Query featureQuery = FeatureField.newSaturationQuery("features", "pagerank");
//            Query query = new BooleanQuery.Builder()
//                .add(booleanQuery.build(), Occur.MUST)
//                .add(new BoostQuery(featureQuery, 10f), Occur.SHOULD)
//                .build();
            Query query = booleanQuery.build();
            TopFieldDocs TFD = searcher.search(query, hitsPerPage, sort, true);
            return TFD;
        } catch (Exception exception) {
            System.out.println(exception.toString());
            return null;
        }
    }

// Query featureQuery = FeatureField.newSaturationQuery("features", "pagerank");
// Query query = new BooleanQuery.Builder()
//     .add(originalQuery, Occur.MUST)
//     .add(new BoostQuery(featureQuery, 0.7f), Occur.SHOULD)
//     .build();

    public _data_result GetDataFromTopDoc(IndexSearcher searcher, TopFieldDocs TFD, String lat, String lon) {
        try {
            ScoreDoc[] hits = TFD.scoreDocs;
            long total_Res = TFD.totalHits.value;

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
                    
                    Double tmplat = Double.parseDouble(d.get("Lat"));
                    Double tmplon = Double.parseDouble(d.get("Lon"));
//                    Double tmplat = GeoPointField.decodeLatitude(Long.parseLong(d.get("LatLonGeo")));
//                    Double tmplon = GeoPointField.decodeLongitude(Long.parseLong(d.get("LatLonGeo")));

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
                    res[i][j++] = d.get("Admin_ID1").trim();
                    res[i][j++] = d.get("Admin_ID2").trim();
                    res[i][j++] = d.get("Admin_ID3").trim();

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
                    
                    //res[i][j++] = d.get("local_soundex");
                    res[i][j++] = d.get("extra_content");

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
                "AdminLevel1Code",
                "AdminLevel2Code",
                "AdminLevel3Code",
                "SOUND_NAME_LOCAL",
                "SOUND_NAME_THEN",
                "SOUND_NAME_ENTH",
                "HouseNumber_sound",
                "Telephone_sound",
                "PostCode_sound",
                "local_soundex",
                "extra_content"
            };

            FinalResult.HeaderTable = header;
            FinalResult.DataTable = res;
            FinalResult.TotalSearch = (int)total_Res;

            return FinalResult;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private BooleanQuery.Builder GenQuery_Filter_Polygon(BooleanQuery.Builder q, String[] PolygString_arr) {
        Polygon[] P_arr = new Polygon[PolygString_arr.length];
        for (int P_arr_count = 0; P_arr_count < PolygString_arr.length; P_arr_count++) {
            String tmp = PolygString_arr[P_arr_count];
            double[] polyLats = new double[5];
            double[] polyLons = new double[5];

            String[] tmp_arr = tmp.split("\\|");

            for (int i_arr = 0; i_arr < 5; i_arr++) {
                String[] tmp_point = tmp_arr[i_arr].split(",");
                polyLats[i_arr] = Double.parseDouble(tmp_point[0]);
                polyLons[i_arr] = Double.parseDouble(tmp_point[1]);
            }

            Polygon tmp_poly = new Polygon(polyLats, polyLons);
            P_arr[P_arr_count] = tmp_poly;
        }

        Query q_polygon = newPolygonQuery("LatLonPoint", P_arr);
        q.add(q_polygon, BooleanClause.Occur.FILTER);

        return null;
    }

    private BooleanQuery.Builder GenQuery_Layer_Landmark_Only(BooleanQuery.Builder q) throws ParseException {
        q.add(new QueryParser("layer", analyzer_th).parse("LANDMARK"), BooleanClause.Occur.FILTER);
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_RouteID(BooleanQuery.Builder q, String routeID) throws ParseException {
        q.add(new QueryParser("Route_id", analyzer_th).parse(routeID), BooleanClause.Occur.FILTER);
        return q;
    }

    private BooleanQuery.Builder GenQuery_NostraID(BooleanQuery.Builder q, String nostraID) throws ParseException {
        q.add(new QueryParser("NostraID", analyzer_th).parse(nostraID), BooleanClause.Occur.MUST);
        return q;
    }

    private BooleanQuery.Builder GenQuery_Filter_MasterID(BooleanQuery.Builder q, String masterID) throws ParseException {
        q.add(new QueryParser("Master_ID", analyzer_th).parse(masterID), BooleanClause.Occur.FILTER);
        return q;
    }

    /*
    Insert Group Function
     */
    public _data_result InputLCS_sound(_data_result input) {
        String[] tmpKeywordSplit = KEYWORD_SEARCH.split("\\|");
        int loop = tmpKeywordSplit.length;

        List<List<String>> gWordList = new ArrayList<List<String>>();
        for (int i = 0; i < loop; i++) {
            gWordList.add(CreateGList(tmpKeywordSplit[i]));
        }

        Double maxScore = Double.parseDouble(input.DataTable[0][INDEX_SCORE].trim());
//        int counter = 0;
//        long t1 = System.currentTimeMillis();
        for (int i = 0; i < input.DataTable.length; i++) {
            for (int j = 0; j < loop; j++) {

                String trimString = tmpKeywordSplit[j].replaceAll("^\\s+", "");
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

                    if (idx == INDEX_COMPARE_NAME_THEN) {
                        TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL] + " "
                                + input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH];

                    } else {
                        TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH] + " "
                                + input.DataTable[i][INDEX_COMPARE_NAME_LOCAL];
                    }

                    List<String> tmpGword = gWordList.get(j);
                    TmpAll = CutGeneralWord(TmpAll, tmpGword);

                    _cl_NostraRanking nostraRanking = new _cl_NostraRanking(CHAR_DATA);
                    double tmpGall = nostraRanking.LCS_Socre(TmpAll.toUpperCase(), trimString.toUpperCase());

                    double OldA = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());

                    if (OldA < tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }

                    //test 
//                    double tmpGall2 = nostraRanking.LCS_Socre2(TmpAll.toUpperCase(),trimString.toUpperCase() );
//                    double OldA2 = Double.parseDouble(input.DataTable[i][INDEX_GROUP_NAME].trim());
//                    if (OldA2 < tmpGall2) {
//                        input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGall2);
//                    }
                } else if (This_thai(tmpKeywordSplit[j])) {
                    long startTime = System.currentTimeMillis();
                    String TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL];

                    List<String> tmpGword = gWordList.get(j);
                    TmpAll = CutGeneralWord(TmpAll, tmpGword);

                    _cl_NostraRanking nostraRanking = new _cl_NostraRanking(CHAR_DATA);
                    double tmpGall = nostraRanking.LCS_Socre(TmpAll.toUpperCase(), trimString.toUpperCase());

                    double OldA = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());

                    if (OldA < tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }

                    //test 
//                    double tmpGall2 = nostraRanking.LCS_Socre2(TmpAll.toUpperCase(),trimString.toUpperCase() );
//                    double OldA2 = Double.parseDouble(input.DataTable[i][INDEX_GROUP_NAME].trim());
//                    if (OldA2 < tmpGall2) {
//                        input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGall2);
//                    }
                } else {
                    long startTime = System.currentTimeMillis();
                    String TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH];

                    List<String> tmpGword = gWordList.get(j);
                    TmpAll = CutGeneralWord(TmpAll, tmpGword);

                    _cl_NostraRanking nostraRanking = new _cl_NostraRanking(CHAR_DATA);
                    double tmpGall = nostraRanking.LCS_Socre(TmpAll.toUpperCase(), trimString.toUpperCase());
                    double OldA = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());

                    if (OldA < tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }
                    //test 
//                    double tmpGall2 = nostraRanking.LCS_Socre2(TmpAll.toUpperCase(),trimString.toUpperCase() );
//                    double OldA2 = Double.parseDouble(input.DataTable[i][INDEX_GROUP_NAME].trim());
//                    if (OldA2 < tmpGall2) {
//                        input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGall2);
//                    }
                }

            }
//            //ทดสอบ
//            Double tmpLcsScore = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());
//            Double tmpScore = Double.parseDouble(input.DataTable[i][INDEX_SCORE].trim());
//            Double nostraScore = (0.5 *tmpLcsScore) + (0.5 * (tmpScore/maxScore));
//            input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(nostraScore);
//            //ทดสอบ
//            Double tmpLcsScore = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());
//            Double tmpScore = Double.parseDouble(input.DataTable[i][INDEX_GROUP_NAME].trim());
//            Double nostraScore = (0.5 *tmpLcsScore) + (0.5 * (tmpScore));
//            input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(nostraScore);
        }
        return input;
    }

    public _data_result InputLCS(_data_result input) {
        String[] tmpKeywordSplit = KEYWORD_SEARCH_SET.split("\\|");

        int loop = tmpKeywordSplit.length;

        for (int i = 0; i < input.DataTable.length; i++) {
            for (int j = 0; j < loop; j++) {

                String trimString = tmpKeywordSplit[j].replaceAll("^\\s+", "");
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

                    if (idx == INDEX_COMPARE_NAME_THEN) {
                        TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL] + " "
                                + input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH];

                    } else {
                        TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH] + " "
                                + input.DataTable[i][INDEX_COMPARE_NAME_LOCAL];
                    }

                    _cl_NostraRanking nostraRanking = new _cl_NostraRanking(CHAR_DATA);
                    double tmpGall = nostraRanking.LCS_Socre(TmpAll.toUpperCase(), trimString.toUpperCase());

                    double OldA = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());

                    if (OldA < tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }

                } else if (This_thai(tmpKeywordSplit[j])) {
                    long startTime = System.currentTimeMillis();
                    String TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL];

                    _cl_NostraRanking nostraRanking = new _cl_NostraRanking(CHAR_DATA);
                    double tmpGall = nostraRanking.LCS_Socre(TmpAll.toUpperCase(), trimString.toUpperCase());

                    double OldA = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());

                    if (OldA < tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }
                } else {
                    String TmpAll = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH];

                    _cl_NostraRanking nostraRanking = new _cl_NostraRanking(CHAR_DATA);
                    double tmpGall = nostraRanking.LCS_Socre(TmpAll.toUpperCase(), trimString.toUpperCase());

                    double OldA = Double.parseDouble(input.DataTable[i][INDEX_GROUP_ALL].trim());

                    if (OldA < tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }

                }

            }
        }
        //long t2 = System.currentTimeMillis() - t1;
        return input;
    }

    public _data_result InputGroupToTable_sound(_data_result input) {
        String[] tmpKeyword = KEYWORD_SEARCH.split("\\|");
        String[] tmpSound = KEYWORD_SEARCH_SOUNDEX.split("\\|");

        int loop = tmpKeyword.length;
        for (int i = 0; i < input.DataTable.length; i++) {

            for (int j = 0; j < loop; j++) {

                if (input.DataTable[i][INDEX_NOSTRAID].equals("L20000426217")) {
                    int bbb = 0;
                }
                String trimString = tmpSound[j].replaceAll("^\\s+", "");
                if (CheckNullEmptyString(trimString.trim())) {
                    continue;
                }

                if (This_thai(tmpKeyword[j]) && This_Eng(tmpKeyword[j])) {
                    int idx = -1;
                    int idx_wb = -1;
                    for (int ij = 0; ij < tmpKeyword[j].length(); ij++) {
                        String tmp = tmpKeyword[j].charAt(ij) + "";
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

                    int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpSound[j], tmpSound[j]);
                    int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpSound[j], tmpSound[j]);

                    int OldA = Integer.parseInt(input.DataTable[i][INDEX_GROUP_ALL].trim());
                    int OldN = Integer.parseInt(input.DataTable[i][INDEX_GROUP_NAME].trim());

                    if (OldA > tmpGall) {
                        input.DataTable[i][INDEX_GROUP_ALL] = String.valueOf(tmpGall);
                    }
                    if (OldN > tmpGname) {
                        input.DataTable[i][INDEX_GROUP_NAME] = String.valueOf(tmpGname);
                    }

                } else if (This_thai(tmpKeyword[j])) {
                    //long startTime = System.currentTimeMillis();
                    String TmpAll = input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                            + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                            + input.DataTable[i][INDEX_Telephone_sound] + " "
                            + input.DataTable[i][INDEX_PostCode_sound];
                    String TmpAll_wb = input.DataTable[i][INDEX_SOUND_LOCAL] + " "
                            + input.DataTable[i][INDEX_HouseNumber_sound] + " "
                            + input.DataTable[i][INDEX_Telephone_sound] + " "
                            + input.DataTable[i][INDEX_PostCode_sound];

//                    String TmpN = input.DataTable[i][INDEX_SOUND_LOCAL];
//                    String TmpN_WB = input.DataTable[i][INDEX_SOUND_LOCAL];
                    String TmpN = input.DataTable[i][INDEX_SOUND_NAME_LOCAL];
                    String TmpN_WB = input.DataTable[i][INDEX_SOUND_NAME_LOCAL];

                    int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpSound[j], tmpSound[j]);
                    int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpSound[j], tmpSound[j]);

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
                    int tmpGall = Set_NUM_SortGroup(TmpAll, TmpAll_wb, tmpSound[j], tmpSound[j]);
                    //long t2_tmpGall = System.currentTimeMillis() - t1_tmpGall;
                    //System.out.println(t2_tmpGall);

                    //long t1_tmpGname = System.currentTimeMillis();
                    int tmpGname = Set_NUM_SortGroup(TmpN, TmpN_WB, tmpSound[j], tmpSound[j]);
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

    public _data_result InputGroupToTable_big(_data_result input) {
        String[] tmpKeywordSplit = KEYWORD_SEARCH_SET.split("\\|");
        String[] tmpKeywordSplit_wb = KEYWORD_SEARCH_SET_WB.split("\\|");
        int loop = tmpKeywordSplit.length;
        _cl_WB_thai wB_thai = new _cl_WB_thai();

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
                    String TmpN_WB = wB_thai.WordB(TmpN).replaceAll("^\\s+", ""); //input.DataTable[i][idx_wb];

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
                        TmpAll_wb = wB_thai.WordB(TmpAll).replaceAll("^\\s+", "");
//                        TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_LOCAL] + " "
//                                + input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
//                                + input.DataTable[i][INDEX_HNO] + " "
//                                + input.DataTable[i][INDEX_TEL] + " "
//                                + input.DataTable[i][INDEX_POST];
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
                        TmpAll_wb = wB_thai.WordB(TmpAll).replaceAll("^\\s+", "");
//                        TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
//                                + input.DataTable[i][INDEX_WB_ALL_LOCAL] + " "
//                                + input.DataTable[i][INDEX_HNO] + " "
//                                + input.DataTable[i][INDEX_TEL] + " "
//                                + input.DataTable[i][INDEX_POST];
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
                    String TmpAll_wb = wB_thai.WordB(TmpAll).replaceAll("^\\s+", "");
//                    String TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_LOCAL] + " "
//                            + input.DataTable[i][INDEX_HNO] + " "
//                            + input.DataTable[i][INDEX_TEL] + " "
//                            + input.DataTable[i][INDEX_POST];

                    String TmpN = input.DataTable[i][INDEX_COMPARE_NAME_LOCAL];
                    String TmpN_WB = wB_thai.WordB(TmpN).replaceAll("^\\s+", "");//input.DataTable[i][INDEX_WB_NAME_LOCAL];

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
                    String TmpAll_wb = wB_thai.WordB(TmpAll).replaceAll("^\\s+", "");

//                    String TmpAll_wb = input.DataTable[i][INDEX_WB_ALL_ENGLISH] + " "
//                            + input.DataTable[i][INDEX_HNO] + " "
//                            + input.DataTable[i][INDEX_TEL] + " "
//                            + input.DataTable[i][INDEX_POST];
                    String TmpN = input.DataTable[i][INDEX_COMPARE_NAME_ENGLISH];
                    String TmpN_WB = wB_thai.WordB(TmpN).replaceAll("^\\s+", "");
                    //String TmpN_WB = input.DataTable[i][INDEX_WB_NAME_ENGLISH];

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

    public boolean This_String_Input_Like(String input, String DataCompare) {
//        String tmp = input.replace(" ", "(\\w+|\\W+|)");
//        Pattern left = Pattern.compile("(\\w+|\\W+|)" + tmp);
//        Pattern center = Pattern.compile("(\\w+|\\W+|)" + tmp + "(\\w+|\\W+|)");
//        Pattern right = Pattern.compile(tmp + "(\\w+|\\W+|)");

        String tmp = input.replace(" ", "(\\S+|)");

        tmp = tmp.replace("?", "[?]");
        tmp = tmp.replace("{", "\\{");
        tmp = tmp.replace("}", "\\}");

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

    private boolean CheckCase0(String mainW, String keyW) {
        mainW = (mainW.replace(" ", "")).trim();
        keyW = (keyW.replace(" ", "")).trim();

        return This_String_Input_Like(keyW, mainW);
    }

    private boolean CheckCase1(String mainW, String keyW) {
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

    private boolean CheckCase2(String mainW, String keyW) {

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

    /*
    Sort Group Function
     */
//-----------FOR lcs
//    private String[][] SortGroupAll(String[][] input) {
//
//        Arrays.sort(input, new Comparator<String[]>() {
//            @Override
//            public int compare(final String[] entry1, final String[] entry2) {
//                final Double time1 = Double.parseDouble(entry1[INDEX_GROUP_ALL]);
//                final Double time2 = Double.parseDouble(entry2[INDEX_GROUP_ALL]);
//                return time2.compareTo(time1);
//            }
//        });
//
//        return input;
//    }
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

    private _data_result SortGroupAction_LCS(_data_result input) {
        input.DataTable = SortGroupAll(input.DataTable);
        input.DataTable = SortGroupName(input.DataTable);
        return input;
    }

    /*
    SUPPROT Function
     */
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

    private List<String> CreateGList(String keyword) {
        try {
            String tmpK = keyword.trim();
//            tmpK = tmpK.replace("?", "[?]");
//            tmpK = tmpK.replace("{", "\\{");
//            tmpK = tmpK.replace("}", "\\}");

            List<String> out = new ArrayList<>();
            boolean prefixFound = false;
            boolean subfixFound = false;
            for (int i = 0; i < G_WORD_DATA.size(); i++) {
                String tmp = G_WORD_DATA.get(i).trim();
                if (CheckNullEmptyString(tmp)) {
                    continue;
                }
                //char tmpC = tmp.charAt(0);
                if (tmp.charAt(0) == '+') {
                    if (prefixFound) {
                        out.add(tmp);
                    } else {
                        String tmp2 = tmp.substring(1, tmp.length() - 1);
                        Pattern p = Pattern.compile("^" + tmp2);
                        Matcher m = p.matcher(tmpK);
                        if (!m.find()) {
                            out.add(tmp);
                            prefixFound = true;
                        }

                    }
                } else if (tmp.charAt(0) == '_') {
                    if (subfixFound) {
                        out.add(tmp);
                    } else {
                        String tmp2 = tmp.substring(1, tmp.length() - 1);
                        Pattern p = Pattern.compile(tmp2 + "$");
                        Matcher m = p.matcher(tmpK);
                        if (!m.find()) {
                            out.add(tmp);
                            subfixFound = true;
                        }

                    }
                }
            }

            return out;
        } catch (Exception exception) {
            return null;
        }
    }

    private String CutGeneralWord(String input, List<String> data) {
        try {
            String tmpK = input.trim();

            boolean prefixFound = false;
            boolean subfixFound = false;
            for (int i = 0; i < data.size(); i++) {
                String tmp = data.get(i).trim();
                if (CheckNullEmptyString(tmp)) {
                    continue;
                }

                if (tmp.charAt(0) == '+') {
                    if (prefixFound) {
                        continue;
                    } else {
                        String tmp2 = tmp.substring(1, tmp.length());
                        Pattern p = Pattern.compile("^" + tmp2);
                        Matcher m = p.matcher(tmpK);
                        if (m.find()) {
                            tmpK = tmpK.replaceAll("^" + tmp2, "").trim();
                            prefixFound = true;
                        }

                    }
                } else if (tmp.charAt(0) == '_') {
                    if (subfixFound) {
                        continue;
                    } else {
                        String tmp2 = tmp.substring(1, tmp.length());
                        Pattern p = Pattern.compile(tmp2 + "$");
                        Matcher m = p.matcher(tmpK);
                        if (m.find()) {
                            tmpK = tmpK.replaceAll(tmp2 + "$", "").trim();
                            subfixFound = true;
                        }

                    }
                }
            }

            return tmpK;
        } catch (Exception exception) {
            return input;
        }

    }

    //test TestFindThreshold
    private double[] FindThreshold(_data_result input) {
        //double[] data = {15.9,15.6,15.5,13.0,12.9,12.8,11.0,9.8,9.8,9.7,9.6,8.2,8.1,8.01,7.95};
        double[] data = new double[input.DataTable.length];
        for (int i = 0; i < input.DataTable.length; i++) {
            data[i] = Double.parseDouble(input.DataTable[i][INDEX_SCORE]);
            input.DataTable[i][INDEX_GROUP_NAME] = "0";
        }
        double[] cent = findThreshold(data, 4);
        for (int i = 0; i < cent.length; i++) {
            System.out.println(cent[i]);
        }
        return cent;
    }

    public double[] findThreshold(double[] data, int n) {
        double[] centroids = new double[n];
        double[] thresholds = new double[n - 1];
        double[] sums = new double[n];
        int[] counts = new int[n];
        for (int i = 0; i < n; i++) {
            sums[i] = 0.0;
            counts[i] = 0;
        }
        centroids[0] = data[0];
        centroids[n - 1] = data[data.length - 1];
        double width = (data[data.length - 1] - data[0]) / (n - 1);
        for (int i = 1; i < n - 1; i++) {
            centroids[i] = centroids[i - 1] + width;
        }
        for (int i = 0; i < data.length; i++) {
            int closestIndex = 0;
            for (int j = 1; j < n; j++) {
                if (Math.abs(data[i] - centroids[j]) < Math.abs(data[i] - centroids[closestIndex])) {
                    closestIndex = j;
                }
            }
            sums[closestIndex] += data[i];
            counts[closestIndex]++;
        }
        for (int i = 0; i < n; i++) {
            centroids[i] = sums[i] / counts[i];
        }
        for (int i = 0; i < n - 1; i++) {
            thresholds[i] = (centroids[i] + centroids[i + 1]) / 2.0;
        }
        return thresholds;
    }

    public _data_result SplitResultSet(_data_result input) {
        double[] tmpSplit = FindThreshold(input);
        List<Double> tmp2 = new ArrayList<Double>();

        for (int i = 0; i < tmpSplit.length; i++) {
            if (!Double.isNaN(tmpSplit[i])) {
                tmp2.add(tmpSplit[i]);
            }
        }

        if (tmp2.size() == 0) {
            return input;
        }

//        for (int i = 0; i < input.DataTable.length; i++) {
//            for (int j = tmpSplit.length; j > 0; j++) {
//                if (tmpSplit[j-1] > Double.parseDouble(input.DataTable[i][INDEX_SCORE])) {
//                    input.DataTable[i][INDEX_GROUP_NAME] =  Double.toString(j);
//                    break;
//                }
//            }
//        }
        for (int i = 0; i < input.DataTable.length; i++) {
            for (int j = tmp2.size(); j > 0; j--) {
                if (tmp2.get(j - 1) > Double.parseDouble(input.DataTable[i][INDEX_SCORE])) {
                    input.DataTable[i][INDEX_GROUP_NAME] = Double.toString(j);
                    break;
                }
            }
        }

        return input;
    }

    private int CheckCaseAdmin(String admin1, String admin2, String admin3) {
        int tmp = 0;
        int tmp_admin1 = CASE_NO_ADMIN;
        int tmp_admin2 = CASE_NO_ADMIN;
        int tmp_admin3 = CASE_NO_ADMIN;
        boolean have_code = false;
        boolean have_string = false;

        if (CheckNullEmptyString(admin1)
                && CheckNullEmptyString(admin2)
                && CheckNullEmptyString(admin3)) {
            return CASE_NO_ADMIN;
        } else {
            if (!CheckNullEmptyString(admin1)) {
                try {
                    tmp = Integer.parseInt(admin1);
                    tmp_admin1 = CASE_ADMIN_CODE_ONLY;
                    have_code = true;
                } catch (Exception e) {
                    tmp_admin1 = CASE_ADMIN_STRING_ONLY;
                    have_string = true;
                }
            }

            if (!CheckNullEmptyString(admin2)) {
                try {
                    tmp = Integer.parseInt(admin2);
                    tmp_admin2 = CASE_ADMIN_CODE_ONLY;
                    have_code = true;
                } catch (Exception e) {
                    tmp_admin2 = CASE_ADMIN_STRING_ONLY;
                    have_string = true;
                }
            }

            if (!CheckNullEmptyString(admin3)) {
                try {
                    tmp = Integer.parseInt(admin3);
                    tmp_admin3 = CASE_ADMIN_CODE_ONLY;
                    have_code = true;
                } catch (Exception e) {
                    tmp_admin3 = CASE_ADMIN_STRING_ONLY;
                    have_string = true;
                }
            }
        }

        if (have_code && have_string) {
            return CASE_ADMIN_MIX;
        } else if (have_code) {
            return CASE_ADMIN_CODE_ONLY;
        } else if (have_string) {
            return CASE_ADMIN_STRING_ONLY;
        } else {
            return CASE_NO_ADMIN;
        }

    }

}
