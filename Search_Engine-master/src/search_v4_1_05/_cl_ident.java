/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.io.IOException;
import static java.lang.System.arraycopy;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import static org.apache.lucene.document.LatLonDocValuesField.newDistanceSort;
import static org.apache.lucene.document.LatLonPoint.newDistanceQuery;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TopScoreDocCollector;
//import org.apache.lucene.spatial.geopoint.document.GeoPointField;
//import org.apache.lucene.spatial.geopoint.search.GeoPointDistanceQuery;
//import org.apache.lucene.spatial.util.MortonEncoder;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author GT_4953
 */
public class _cl_ident {

    private IndexReader reader;
    private IndexSearcher searcher;
    private String FolderPath = "";
    private double RADIUS = 20;
    private int NUM_OF_RES_INDEX = 24;
    private int INDEX_DIS = 18;
    private Analyzer analyzer_th = new WhitespaceAnalyzer();

    public _cl_ident(String FolderP, Double r, IndexSearcher IndexS) throws IOException {
        FolderPath = FolderP;
        RADIUS = r;
        searcher = IndexS;
    }

    public boolean CheckNullEmptyString(String input) {
        if (input == null) {
            return true;
        } else if (input.toUpperCase().trim().equals("N/A")) {
            return true;
        }
        return input.trim().length() == 0;
    }

    public _data_result query(String lat, String lon) throws IOException, ParseException {

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        booleanQuery.add(new QueryParser("Popularity", analyzer_th).parse("1"), BooleanClause.Occur.MUST);
        booleanQuery.add(new QueryParser("Master_ID", analyzer_th).parse("0"), BooleanClause.Occur.MUST);
        booleanQuery.add(new QueryParser("layer", analyzer_th).parse("L_TRANS LANDMARK KM HYDROLOGY"), BooleanClause.Occur.FILTER);
        Query q_geodis = newDistanceQuery("LatLonPoint", Double.parseDouble(lat), Double.parseDouble(lon), RADIUS);
        booleanQuery.add(q_geodis, BooleanClause.Occur.FILTER);

        //Query q_geodis = new GeoPointDistanceQuery("LatLonGeo", GeoPointField.TermEncoding.PREFIX, Double.parseDouble(lat), Double.parseDouble(lon), RADIUS);
        //booleanQuery.add(q_geodis, BooleanClause.Occur.FILTER);
/*
        TopScoreDocCollector collector = TopScoreDocCollector.create(100);
        searcher.search(booleanQuery.build(), collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
         */
        Sort sort;

        sort = new Sort(
                new SortField[]{
                    newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
                });
        Query qq = booleanQuery.build();
        TopFieldDocs TFD = searcher.search(qq, 1, sort);
        //TopFieldDocs TFD = searcher.search(booleanQuery.build(), 100, sort, true, true);
        ScoreDoc[] hits = TFD.scoreDocs;
        //int total_Res = TFD.totalHits;

        if (hits.length <= 0) {
            return null;
        }

        String res[][] = new String[hits.length][NUM_OF_RES_INDEX];
        //int i = 0;
        for (int i = 0; i < hits.length; i++) {
            Document d = searcher.doc(hits[i].doc);
            int j = 0;

            res[i][j++] = d.get("NostraID");

            //name E
            if (CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                res[i][j++] = d.get("Name_English").trim();
            } else {
                res[i][j++] = d.get("Name_English").trim() + " " + d.get("Name_Branch_English").trim();
            }
            //branchE
            if (!CheckNullEmptyString(d.get("Name_Branch_English").trim())) {
                res[i][j++] = d.get("Name_Branch_English").trim();
            } else {
                res[i][j++] = "";
            }
            //name L
            if (CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                res[i][j++] = d.get("Name_Local").trim();
            } else {
                res[i][j++] = d.get("Name_Local").trim() + " " + d.get("Name_Branch_Local").trim();
            }
            //branchL
            if (!CheckNullEmptyString(d.get("Name_Branch_Local").trim())) {
                res[i][j++] = d.get("Name_Branch_Local").trim();
            } else {
                res[i][j++] = "";
            }

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

            res[i][j++] = d.get("Category_Return");
            res[i][j++] = d.get("LocalCatCode");

            Double tmplat = Double.parseDouble(d.get("Lat"));
            Double tmplon = Double.parseDouble(d.get("Lon"));
            //dist
            if (!CheckNullEmptyString(lat) && !CheckNullEmptyString(lon)) {
                Double dis = distance(tmplat, tmplon, Double.parseDouble(lat), Double.parseDouble(lon), "K");
                res[i][j++] = dis.toString();
            } else {
                res[i][j++] = "0";
            }

            res[i][j++] = d.get("public_id");
            res[i][j++] = d.get("Admin_Code");
            res[i][j++] = d.get("Admin_ID1");
            res[i][j++] = d.get("Admin_ID2");
            res[i][j++] = d.get("Admin_ID3");
        }
        String header[] = {
            "NostraId", "Name_E", "Branch_E", "Name_L", "Branch_L", "AdminLevel1_E", "AdminLevel2_E", "AdminLevel3_E", "AdminLevel4_E", "AdminLevel1_L", "AdminLevel2_L", "AdminLevel3_L", "AdminLevel4_L", "HouseNo", "Telephone", "PostCode", "Catcode", "LocalCatCode", "dist", "public_id" ,"Admin_Code","Admin_ID1","Admin_ID2","Admin_ID3"
        };

        _data_result output = new _data_result();
        res = SortDistnce(res);
        String[][] res_tmp = new String[1][NUM_OF_RES_INDEX];

        arraycopy(res, 0, res_tmp, 0, 1);

        output.DataTable = res_tmp;
        output.HeaderTable = header;

        return output;
    }

    public int query_sum_by_cat(String lat, String lon, String r, String code) throws IOException, ParseException {

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        booleanQuery.add(new QueryParser("Category_Return", analyzer_th).parse(code), BooleanClause.Occur.MUST);
        booleanQuery.add(new QueryParser("Master_ID", analyzer_th).parse("0"), BooleanClause.Occur.FILTER);
        //Query q_geodis = new GeoPointDistanceQuery("LatLonGeo", GeoPointField.TermEncoding.PREFIX, Double.parseDouble(lat), Double.parseDouble(lon), Double.parseDouble(r));
        Query q_geodis = newDistanceQuery("LatLonPoint", Double.parseDouble(lat), Double.parseDouble(lon), Double.parseDouble(r));
        booleanQuery.add(q_geodis, BooleanClause.Occur.FILTER);
        Query qq = booleanQuery.build();
        TopDocs td = searcher.search(qq, 1);
        long s = td.totalHits.value;
        if (s<=0)
            return 0;
        else
            return (int)s;
//TopScoreDocCollector collector = TopScoreDocCollector.create(100);
//        searcher.search(booleanQuery.build(), collector);
//        ScoreDoc[] hits = collector.topDocs().scoreDocs;
//
//        if (hits.length <= 0) {
//            return 0;
//        }
//
//        int sum = collector.getTotalHits();
//
//        return sum;
    }

    public int query_sum_by_sub(String lat, String lon, String r, String code) throws IOException, ParseException {
       
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        booleanQuery.add(new QueryParser("LocalCatCode", analyzer_th).parse(code), BooleanClause.Occur.MUST);
        booleanQuery.add(new QueryParser("Master_ID", analyzer_th).parse("0"), BooleanClause.Occur.FILTER);
        //Query q_geodis = new GeoPointDistanceQuery("LatLonGeo", GeoPointField.TermEncoding.PREFIX, Double.parseDouble(lat), Double.parseDouble(lon), Double.parseDouble(r));
        Query q_geodis = newDistanceQuery("LatLonPoint", Double.parseDouble(lat), Double.parseDouble(lon), Double.parseDouble(r));
        booleanQuery.add(q_geodis, BooleanClause.Occur.FILTER);
        Query qq = booleanQuery.build();
        TopDocs td = searcher.search(qq, 1);
        long s = td.totalHits.value;
        if (s<=0)
            return 0;
        else
            return (int)s;
    }

    private String[][] SortDistnce(String[][] input) {

        Arrays.sort(input, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final Double time1 = Double.parseDouble(entry1[INDEX_DIS]);
                final Double time2 = Double.parseDouble(entry2[INDEX_DIS]);
                return time1.compareTo(time2);
            }
        });

        return input;
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

//    public boolean init() throws IOException {
//
//        try {
//            Path path = FileSystems.getDefault().getPath(FolderPath);
//            Directory directory = FSDirectory.open(path);
//            reader = DirectoryReader.open(directory);
//            searcher = new IndexSearcher(reader);
//
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//
//    }
}
