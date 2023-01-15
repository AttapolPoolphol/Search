/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.document.Document;
import static org.apache.lucene.document.LatLonDocValuesField.newDistanceSort;
import static org.apache.lucene.document.LatLonPoint.newDistanceQuery;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author GT_4953
 */
public class _cl_auto {

    //public String[] query(String keyword, String FolderPath, int MaxReturn) throws IOException, ParseException {
    public String[] query(String keyword, IndexSearcher searcher, int MaxReturn) throws IOException, ParseException {
        //Analyzer analyzer_th = new ThaiAnalyzer();
        int min = 1;
        int max = 20;
        Analyzer analyzer = new Analyzer() {
            @Override
            protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new NGramTokenizer(min, max);
                return new Analyzer.TokenStreamComponents(tokenizer, tokenizer);
            }
        };
        
        Analyzer analyzer_en = new Analyzer() {
            @Override
            protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new NGramTokenizer(1, 10);//min max for english
                return new Analyzer.TokenStreamComponents(tokenizer, tokenizer);
            }
        };
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        try {

            if (This_thai(keyword)) {
                //booleanQuery.add(new QueryParser("name", analyzer).parse(keyword + "*"), BooleanClause.Occur.SHOULD);
                booleanQuery.add(new QueryParser("name_s", analyzer).parse(keyword), BooleanClause.Occur.SHOULD);
            } else {
                //booleanQuery.add(new QueryParser("name", analyzer_th).parse(keyword), BooleanClause.Occur.MUST);
                //booleanQuery.add(new QueryParser("name", analyzer_th).parse(keyword + "*"), BooleanClause.Occur.SHOULD);
                booleanQuery.add(new QueryParser("name_s", analyzer_en).parse(keyword), BooleanClause.Occur.SHOULD);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        Query qq = booleanQuery.build();
        TopDocs TFD = searcher.search(qq, 10000);

        ScoreDoc[] hits = TFD.scoreDocs;
        int idx = 0;
        if (hits.length <= 0) {
            return null;
        } else if (hits.length > MaxReturn) {
            idx = MaxReturn;
        } else {
            idx = hits.length;
        }

        String[] res = new String[idx];
        int j = 0;
        try {
            for (int i = 0; i < hits.length; i++) {

                Document d = searcher.doc(hits[i].doc);
                String look = d.get("name");
                System.out.println(look);
                if (d.get("name").toLowerCase().contains(keyword.toLowerCase())) {
                    res[j++] = d.get("name");
                }
                if (j == idx) {
                    break;
                }
            }
            //reader.close();
            //directory.close();

            return res;
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        return null;

    }

    public boolean This_thai(String input) {
        Pattern r = Pattern.compile("[ก-ฮฯ-ูเ-ํ๑-๙]+");
        Matcher m = r.matcher(input);
        return m.find();
    }

    public boolean This_Prefix(String data,String prefix) {
        Pattern r = Pattern.compile("(^" + prefix + "$)|(^" + prefix + "[\\W+|\\w+])");
        Matcher m = r.matcher(data);
        return m.find();
    }

    public boolean This_Eng(String input) {
        Pattern r = Pattern.compile("[A-Za-z]+");
        Matcher m = r.matcher(input);
        return m.find();
    }

    public List<String> query(_data_input input, IndexSearcher searcher) throws IOException, ParseException {
        //Analyzer analyzer_th = new ThaiAnalyzer();
        int min = 1;
        int max = 20;
        Analyzer analyzer = new Analyzer() {
            @Override
            protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new NGramTokenizer(min, max);
                return new Analyzer.TokenStreamComponents(tokenizer, tokenizer);
            }
        };
        
        Analyzer analyzer_en = new Analyzer() {
            @Override
            protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
                Tokenizer tokenizer = new NGramTokenizer(1, 10);//min max for english
                return new Analyzer.TokenStreamComponents(tokenizer, tokenizer);
            }
        };

        double r = 16200 * 1000;
        int maxR = 10000;

        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        try {
            String tmpInput = "";
            tmpInput = input.keyword;
            tmpInput = tmpInput.replace("/", "\\/");
            //tmpInput = tmpInput.replace("&", " & ");
            tmpInput = tmpInput.replace("-", "\\-");
            tmpInput = tmpInput.replace("(", "\\(");
            tmpInput = tmpInput.replace(")", "\\)");
            tmpInput = tmpInput.replace("@", "\\@");
            //select index
            if (This_thai(tmpInput)) {
//                booleanQuery.add(new QueryParser("name", analyzer).parse(input.keyword.toLowerCase().replace(" ", "")), BooleanClause.Occur.MUST);
//                if (input.prefixMode) {
//                    booleanQuery = GenQuery_Prefix(booleanQuery, input.keyword, "name");
//                }
                booleanQuery.add(new QueryParser("name_s", analyzer).parse(tmpInput.toLowerCase()), BooleanClause.Occur.MUST);
                if (input.prefixMode) {
                    booleanQuery = GenQuery_Prefix(booleanQuery, tmpInput, "name_s");
                }
            } else {
                //String tmp_input = tmpInput.toLowerCase().replace(" ", "");
                //booleanQuery.add(new QueryParser("name", analyzer_th).parse( tmp_input + "*"), BooleanClause.Occur.MUST);
                //booleanQuery.add(new QueryParser("name", analyzer_th).parse(tmp_input + "*"), BooleanClause.Occur.SHOULD);
                booleanQuery.add(new QueryParser("name_s", analyzer_en).parse(tmpInput.toLowerCase()), BooleanClause.Occur.MUST);
//booleanQuery.add(new QueryParser("name_s", analyzer_th).parse(tmpInput.toLowerCase() + "*"), BooleanClause.Occur.SHOULD);
                if (input.prefixMode) {
                    booleanQuery = GenQuery_Prefix(booleanQuery, tmpInput, "name_s");
                }
            }

            //add lat lon
            booleanQuery = GenQuery_Filter_Radius(booleanQuery, input.Lat, input.Lon, r);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        ScoreDoc[] hits = Search_Action(searcher, booleanQuery, maxR,input.Lat,input.Lon).scoreDocs;

        int idx = 0;
        if (hits.length <= 0) {
            return null;
        } else if (hits.length > input.MaxReturn) {
            idx = input.MaxReturn;
        } else {
            idx = hits.length;
        }

        List<String> resList = new ArrayList<>();
        List<String> resList_noSpace = new ArrayList<>();
        int j = 0;
        try {
            for (int i = 0; i < hits.length; i++) {

                Document d = searcher.doc(hits[i].doc);
                //String look = d.get("name_s") + "_" + d.get("name");
                //System.out.println(look);
                float tmp = hits[i].score;
                if (input.prefixMode) {
                    String tmpGet = d.get("name_s").toLowerCase();
                    String debug = "Engineering Operation Building, Thai Chamber Of Commerce University";
                    if(tmpGet.toLowerCase().equals(debug.toLowerCase()))
                    {
                        int xxx = 0;
                    }
                    if (This_Prefix(tmpGet,input.keyword)) {
                        if (!resList_noSpace.contains(d.get("name_s").replace(" ", ""))) {
                            if(This_thai(input.keyword))
                                resList.add(d.get("name_s"));
                            else
                                resList.add(d.get("name_return"));
                            resList_noSpace.add(d.get("name_s").replace(" ", ""));
                        }
                    }
                } else if (d.get("name_s").toLowerCase().contains(input.keyword.toLowerCase())) {
                    //res[j++] = d.get("name");

                    if (!resList_noSpace.contains(d.get("name_s").replace(" ", ""))) {
                        if(This_thai(input.keyword))
                                resList.add(d.get("name_s"));// + hits[i].score);
                            else
                                resList.add(d.get("name_return"));
                        resList_noSpace.add(d.get("name_s").replace(" ", ""));
                    }

                }
                if (j == idx) {
                    break;
                }
            }
            return resList;
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
        return null;

    }

    private BooleanQuery.Builder GenQuery_Prefix(BooleanQuery.Builder booleanQuery, String keyword, String fieldName) {
        //thai filter
        Term tmp_Term_TH = new Term(fieldName, "[ก-๛]" + keyword);
        booleanQuery.add(new RegexpQuery(tmp_Term_TH), BooleanClause.Occur.MUST_NOT);

        //Eng filter
        Term tmp_Term_EN = new Term(fieldName, "[a-z]" + keyword);
        booleanQuery.add(new RegexpQuery(tmp_Term_EN), BooleanClause.Occur.MUST_NOT);

        //SP character filter
        Term tmp_Term_Space = new Term(fieldName, "[ ]" + keyword);
        booleanQuery.add(new RegexpQuery(tmp_Term_Space), BooleanClause.Occur.MUST_NOT);
        Term tmp_Term_SP = new Term(fieldName, "[!-_]" + keyword);
        booleanQuery.add(new RegexpQuery(tmp_Term_SP), BooleanClause.Occur.MUST_NOT);

        return booleanQuery;
    }

    private BooleanQuery.Builder GenQuery_Filter_Radius(BooleanQuery.Builder q, String Lat, String Lon, double radius) {
        Query q_geodis = newDistanceQuery("LatLonPoint", Double.parseDouble(Lat), Double.parseDouble(Lon), radius);
        q.add(q_geodis, BooleanClause.Occur.FILTER);
        return q;
    }

//    private TopFieldDocs Search_Action(IndexSearcher searcher, BooleanQuery.Builder booleanQuery, int MaxResult) {
//        int hitsPerPage = MaxResult;
//
//        try {
//            Sort sort;
//            sort = new Sort(
//                    new SortField[]{SortField.FIELD_SCORE//,
//                    //newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
//                    });
////            sort = new Sort(
////                    new SortField[]{SortField.FIELD_SCORE,
////                        newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
////                    });
//
//            TopFieldDocs TFD = searcher.search(booleanQuery.build(), hitsPerPage, sort, true, true);
//            return TFD;
//        } catch (Exception exception) {
//            System.out.println(exception.toString());
//            return null;
//        }
//    }
    
    private TopDocs Search_Action(IndexSearcher searcher, BooleanQuery.Builder booleanQuery, int MaxResult,String lat, String lon) {
        int hitsPerPage = MaxResult;

        try {
            Sort sort;
            sort = new Sort(
                    new SortField[]{SortField.FIELD_SCORE,
                    newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
                    });
//            sort = new Sort(
//                    new SortField[]{SortField.FIELD_SCORE,
//                        newDistanceSort("LatLonDoc", Double.parseDouble(lat), Double.parseDouble(lon))
//                    });
            Query qq = booleanQuery.build();
            TopDocs TFD = searcher.search(qq, 10000);

            ScoreDoc[] hits = TFD.scoreDocs;
            return TFD;
        } catch (Exception exception) {
            System.out.println(exception.toString());
            return null;
        }
    }
}
