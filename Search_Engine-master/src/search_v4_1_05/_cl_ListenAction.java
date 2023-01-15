/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GT_4953
 */
public class _cl_ListenAction extends Thread {

    private String indexPath;
    private String Au_THPath;
    private String Au_ENPath;
    private int PROCESS_ROW;

    private Socket socket;
    String message = "";

    private int MODE_SEARCH = 1;
    private int MODE_AUTOCOMPLETE_TH = 2;
    private int MODE_AUTOCOMPLETE_EN = 3;
    private int MODE_IDEN = 4;
    private int MODE_SUM_CAT = 5;
    private int MODE_SUM_SUB = 6;

    private IndexSearcher SEARCHER;
    private IndexSearcher SEARCHER_auto_TH;
    private IndexSearcher SEARCHER_auto_EN;
    private IndexSearcher SEARCHER_BIG;
    private int TIMEOUT;
    private List<String> CHAR_DATA;
    private List<String> G_WORD_DATA;
    private _data_database DB = new _data_database();
  
    /*
    public _cl_ListenAction(Socket socket, String PathIndex, String PathAutoTh, String PathAutoEn,
            IndexSearcher searcher, int timeout, int processRow, IndexSearcher searcher_autoEN, IndexSearcher searcher_autoTH, IndexSearcher searcher_big
            ,List<String> charData , List<String> dataGeneralWord) {
        this.socket = socket;
        this.indexPath = PathIndex;
        this.Au_THPath = PathAutoTh;
        this.Au_ENPath = PathAutoEn;

        this.SEARCHER = searcher;
        this.TIMEOUT = timeout;
        this.PROCESS_ROW = processRow;

        this.SEARCHER_auto_EN = searcher_autoEN;
        this.SEARCHER_auto_TH = searcher_autoTH;
        this.SEARCHER_BIG = searcher_big;
        
        this.CHAR_DATA = charData;
        this.G_WORD_DATA = dataGeneralWord;
    }*/
    
    public _cl_ListenAction(Socket socket,_cl_Search_init init) {
        this.socket = socket;
        this.indexPath = init.INDEXPATH;
        this.Au_THPath = init.INDEX_AUTO_TH;
        this.Au_ENPath = init.INDEX_AUTO_EN;

        this.SEARCHER = init.searcher;
        this.TIMEOUT = init.TIMEOUT;
        this.PROCESS_ROW = init.PROCESS_ROW;

        this.SEARCHER_auto_EN = init.searcher_auto_EN;
        this.SEARCHER_auto_TH = init.searcher_auto_TH;
        this.SEARCHER_BIG = init.searcher_BIG;
        
        this.CHAR_DATA = init.dataList;
        this.G_WORD_DATA = init.dataGeneralWord;
        
        this.DB.DB_DATABASENAME = init.DB_DATABASENAME;
        this.DB.DB_SERVER_NAME = init.DB_SERVER_NAME;
        this.DB.DB_USERNAME = init.DB_USERNAME;
        this.DB.DB_PASSWORD =init.DB_PASSWORD;
        this.DB.DB_TABLE_ADMIN =init.DB_TABLE_ADMIN;
    }

    public void run() {
        handleMessage();
    }

    public void handleMessage() {
        try {
            socket.setSoTimeout(TIMEOUT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            message = in.readLine();
            String result = "";
            System.out.println("Keyword=" + message);

            if (message != null) {
                _cl_EngineFormat Message_mode = new _cl_EngineFormat();

                _cl_EngineFormat.MESSAGE_MODE m = Message_mode.Message_mode(message);

                if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH) {
                    try {
                        /*_data_input input = new _data_input();
                        input = Message_mode.Message_split(message);
                        System.out.println("split OK");

                        _cl_GetData get = new _cl_GetData(PROCESS_ROW);
                        _data_result dr = get.NostraSearch(input, SEARCHER);
                         */
                        _data_input input = new _data_input();
                        input = Message_mode.Message_split(message);
                        System.out.println("split OK");

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);
                        _data_result dr = get.NostraSearch(input, SEARCHER);

                        _cl_SortResult sort_res = new _cl_SortResult();
                        if (!get.CheckNullEmptyString(input.keyword)) {
                            //dr = sort_res.SortAction(dr);
                            dr = sort_res.SortAction_LCS(dr);
                        } else {
                            dr = sort_res.Sort_Distance_Action(dr);
                        }
                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        ArrayList<String> nostra_list = new ArrayList<String>();
                        int skip = 0;
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            if (nostra_list.contains(dr.DataTable[i][0].trim())) {
                                skip++;
                            } else {
                                nostra_list.add(dr.DataTable[i][0].trim());

                                for (int j = 0; j < dr.DataTable[i].length; j++) {
                                    try {
                                        result += dr.DataTable[i][j].trim() + "^";
                                    } catch (Exception ex) {
                                        result += "" + "^";
                                    }
                                }

                                result = result.substring(0, result.length() - 1);
                                result += "|";
                                if ((i - skip) == input.MaxReturn) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }

                }else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH_BIG) {
                    try {
                        _data_input input = new _data_input();
                        input = Message_mode.Message_split(message);
                        System.out.println("split OK");
                        _cl_WB_thai wb_thai = new _cl_WB_thai();
                        input.search_keyword_WB = wb_thai.AllAction(input.search_keyword);

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);

                        _data_result dr = get.NostraSearch_big(input, SEARCHER_BIG);

//                        _cl_GetData get = new _cl_GetData(PROCESS_ROW);
//                        _data_result dr = get.NostraSearch(input, SEARCHER);
                        _cl_SortResult sort_res = new _cl_SortResult();
                        if (!get.CheckNullEmptyString(input.keyword)) {
                            dr = sort_res.SortAction(dr);
                        } else {
                            dr = sort_res.Sort_Distance_Action(dr);
                        }
                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        ArrayList<String> nostra_list = new ArrayList<String>();
                        int skip = 0;
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            if (nostra_list.contains(dr.DataTable[i][0].trim())) {
                                skip++;
                            } else {
                                nostra_list.add(dr.DataTable[i][0].trim());

                                for (int j = 0; j < dr.DataTable[i].length; j++) {
                                    try {
                                        result += dr.DataTable[i][j].trim() + "^";
                                    } catch (Exception ex) {
                                        result += "" + "^";
                                    }
                                }

                                result = result.substring(0, result.length() - 1);
                                result += "|";
                                if ((i - skip) == input.MaxReturn) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }

                }  
                else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_AUTOCOMPLETE_EN) {
                    try {
                        _data_input input = new _data_input();
                        input = Message_mode.Message_autocomplete_split(message);
                        _cl_auto a = new _cl_auto();
                        List<String> res = a.query(input, this.SEARCHER_auto_EN);
                        if (res != null) {
                            for (int i = 0; i < res.size(); i++) {
                                result += res.get(i) + "|";
                                if (i == input.MaxReturn - 1) {
                                    break;
                                }
                            }
                            result = result.substring(0, result.length() - 1);
                        }
                    } catch (Exception ex) {

                    }
                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_AUTOCOMPLETE_TH) {
                    try {
                        _data_input input = new _data_input();
                        input = Message_mode.Message_autocomplete_split(message);
                        _cl_auto a = new _cl_auto();
                        List<String> res = a.query(input, this.SEARCHER_auto_TH);
                        if (res != null) {
                            for (int i = 0; i < res.size(); i++) {
                                result += res.get(i) + "|";
                                if (i == input.MaxReturn - 1) {
                                    break;
                                }
                            }
                            result = result.substring(0, result.length() - 1);
                        }

                    } catch (Exception ex) {

                    }
                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_IDEN) {
                    try {
                        _data_input input = Message_mode.Message_iden_split(message);
                        _cl_ident iden = new _cl_ident(indexPath, input.radius, SEARCHER);
                        _data_result dr = iden.query(input.Lat, input.Lon);

                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";

                        for (int i = 0; i < dr.DataTable.length; i++) {

                            for (int j = 0; j < dr.DataTable[0].length; j++) {
                                if (dr.DataTable[i][j] != null) {
                                    result += dr.DataTable[i][j].trim() + "^";
                                } else {
                                    result += "" + "^";
                                }
                            }
                            result = result.substring(0, result.length() - 1);
                            result += "|";

                        }

                    } catch (Exception ex) {

                    }

                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SUM_CAT) {
                    try {
                        _data_input input = Message_mode.Message_sumcat_split(message);
                        _cl_ident iden = new _cl_ident(indexPath, input.radius, SEARCHER);

                        int sum = iden.query_sum_by_cat(input.Lat, input.Lon, String.valueOf(input.radius), input.Category);

                        result = String.valueOf(sum);

                    } catch (Exception ex) {

                    }
                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SUM_SUB) {
                    try {
                        _data_input input = Message_mode.Message_sumcat_split(message);
                        _cl_ident iden = new _cl_ident(indexPath, input.radius, SEARCHER);

                        int sum = iden.query_sum_by_sub(input.Lat, input.Lon, String.valueOf(input.radius), input.Category);

                        result = String.valueOf(sum);

                    } catch (Exception ex) {

                    }
                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_TOTAL_SEARCH_RESULT) {
                    try {
                        _data_input input = new _data_input();
                        input = Message_mode.Message_split(message);
                        System.out.println("split OK");

                        _cl_GetData get = new _cl_GetData(PROCESS_ROW);
                        int res = get.NostraSearch_TotalResult(input, SEARCHER);

                        System.out.println("call TOTAL end");

                        result = String.valueOf(res);
                    } catch (Exception ex) {
                        result = "0";
                    }
                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH_NEARBY) {
                    try {

                        _data_input_nearby input = new _data_input_nearby();
                        input = Message_mode.Message_splitNearBy(message);
                        System.out.println("split OK");

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);
                        
                        _data_result dr = get.NostraSearch(input, SEARCHER);
                        _cl_SortResult sort_res = new _cl_SortResult();
                        if (!get.CheckNullEmptyString(input.keyword)) {
                            dr = sort_res.SortAction(dr);
                        } else {
                            dr = sort_res.Sort_Distance_Action(dr);
                        }
                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        ArrayList<String> nostra_list = new ArrayList<String>();
                        int skip = 0;
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            if (nostra_list.contains(dr.DataTable[i][0].trim())) {
                                skip++;
                            } else {
                                nostra_list.add(dr.DataTable[i][0].trim());

                                for (int j = 0; j < dr.DataTable[i].length; j++) {
                                    try {
                                        result += dr.DataTable[i][j].trim() + "^";
                                    } catch (Exception ex) {
                                        result += "" + "^";
                                    }
                                }

                                result = result.substring(0, result.length() - 1);
                                result += "|";
                                if ((i - skip) == input.MaxReturn - 1) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }

                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH_CLOSET) {
                    try {

                        _data_input_close input = new _data_input_close();
                        input = Message_mode.Message_splitCloset(message);
                        System.out.println("split OK");

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);

                        _data_result dr = get.NostraSearch(input, SEARCHER);

                        _cl_SortResult sort_res = new _cl_SortResult();
                        if (!get.CheckNullEmptyString(input.keyword)) {
                            dr = sort_res.SortAction(dr);
                        } else {
                            dr = sort_res.Sort_Distance_Action(dr);
                        }
                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        ArrayList<String> nostra_list = new ArrayList<String>();
                        int skip = 0;
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            if (nostra_list.contains(dr.DataTable[i][0].trim())) {
                                skip++;
                            } else {
                                nostra_list.add(dr.DataTable[i][0].trim());

                                for (int j = 0; j < dr.DataTable[i].length; j++) {
                                    try {
                                        result += dr.DataTable[i][j].trim() + "^";
                                    } catch (Exception ex) {
                                        result += "" + "^";
                                    }
                                }

                                result = result.substring(0, result.length() - 1);
                                result += "|";
                                if ((i - skip) == input.MaxReturn - 1) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }

                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH_SOUND) {
                    try {
                        _data_input_soundex input = new _data_input_soundex();
                        input = Message_mode.Message_split_sound(message);
                        System.out.println("split OK");

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        input.MaxReturn = 1000;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);
                        _data_result dr = get.NostraSearch(input, SEARCHER);

//                        _cl_GetData get = new _cl_GetData(PROCESS_ROW);
//                        _data_result dr = get.NostraSearch(input, SEARCHER);
                        _cl_SortResult sort_res = new _cl_SortResult();
                        if (!get.CheckNullEmptyString(input.keyword)) {
                            dr = sort_res.SortAction(dr);
                            //dr = sort_res.SortAction_LCS(dr);
                        } else {
                            dr = sort_res.Sort_Distance_Action(dr);
                        }
                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        ArrayList<String> nostra_list = new ArrayList<String>();
                        int skip = 0;
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            if (nostra_list.contains(dr.DataTable[i][0].trim())) {
                                skip++;
                            } else {
                                nostra_list.add(dr.DataTable[i][0].trim());

                                for (int j = 0; j < dr.DataTable[i].length; j++) {
                                    try {
                                        result += dr.DataTable[i][j].trim() + "^";
                                    } catch (Exception ex) {
                                        result += "" + "^";
                                    }
                                }

                                result = result.substring(0, result.length() - 1);
                                result += "|";
                                if ((i - skip) == input.MaxReturn) {
                                //if ((i - skip) == 1000) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }

                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_TOTAL_SEARCH_RESULT_SOUND) {
                    try {
                        _data_input_soundex input = new _data_input_soundex();
                        input = Message_mode.Message_split_sound(message);
                        System.out.println("split OK");

                        //_cl_GetData get = new _cl_GetData(PROCESS_ROW);
                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);
                        int res = get.NostraSearch_TotalResult(input, SEARCHER);

                        System.out.println("call TOTAL end");

                        result = String.valueOf(res);
                    } catch (Exception ex) {
                        result = "0";
                    }
                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH_NEARBY_SOUND) {
                    try {
                        _data_input_nearby_soundex input = new _data_input_nearby_soundex();
                        input = Message_mode.Message_splitNearBy_Sound(message);
                        System.out.println("split OK");

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);
                        _data_result dr = get.NostraSearch(input, SEARCHER);

                        _cl_SortResult sort_res = new _cl_SortResult();
                        if (!get.CheckNullEmptyString(input.keyword)) {
                            dr = sort_res.SortAction(dr);
                        } else {
                            dr = sort_res.Sort_Distance_Action(dr);
                        }
                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        ArrayList<String> nostra_list = new ArrayList<String>();
                        int skip = 0;
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            if (nostra_list.contains(dr.DataTable[i][0].trim())) {
                                skip++;
                            } else {
                                nostra_list.add(dr.DataTable[i][0].trim());

                                for (int j = 0; j < dr.DataTable[i].length; j++) {
                                    try {
                                        result += dr.DataTable[i][j].trim() + "^";
                                    } catch (Exception ex) {
                                        result += "" + "^";
                                    }
                                }

                                result = result.substring(0, result.length() - 1);
                                result += "|";
                                if ((i - skip) == input.MaxReturn - 1) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }

                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH_CLOSET_SOUND) {
                    try {
                        _data_input_close_soundex input = new _data_input_close_soundex();
                        input = Message_mode.Message_splitCloset_Sound(message);
                        System.out.println("split OK");

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = PROCESS_ROW;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);
                        _data_result dr = get.NostraSearch(input, SEARCHER);

                        _cl_SortResult sort_res = new _cl_SortResult();
                        if (!get.CheckNullEmptyString(input.keyword)) {
                            dr = sort_res.SortAction(dr);
                        } else {
                            dr = sort_res.Sort_Distance_Action(dr);
                        }
                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        ArrayList<String> nostra_list = new ArrayList<String>();
                        int skip = 0;
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            if (nostra_list.contains(dr.DataTable[i][0].trim())) {
                                skip++;
                            } else {
                                nostra_list.add(dr.DataTable[i][0].trim());

                                for (int j = 0; j < dr.DataTable[i].length; j++) {
                                    try {
                                        result += dr.DataTable[i][j].trim() + "^";
                                    } catch (Exception ex) {
                                        result += "" + "^";
                                    }
                                }

                                result = result.substring(0, result.length() - 1);
                                result += "|";
                                if ((i - skip) == input.MaxReturn - 1) {
                                    break;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }

                } else if (m == _cl_EngineFormat.MESSAGE_MODE.MODE_SEARCH_NOSTRA_ID) {
                    try {
                        _data_input_nid input = new _data_input_nid();
                        input = Message_mode.Message_nid_split(message);
                        System.out.println("split OK");

                        //parameter for _cl_nostrasearch
                        _data_InitNostraSearch initNostraSearch= new _data_InitNostraSearch();
                        initNostraSearch.CHAR_DATA = CHAR_DATA;
                        initNostraSearch.PROCESS_ROW = 1;
                        initNostraSearch.G_WORD_DATA = G_WORD_DATA;
                        initNostraSearch.DB = DB;
                        _cl_NostraSearch get = new _cl_NostraSearch(initNostraSearch);
                        _data_result dr = get.NostraSearch_ID(input, SEARCHER);

                        System.out.println("call search end");

                        System.out.println("Rows of result" + dr.DataTable.length);

                        result = dr.TotalSearch + "@##@";
                        for (int i = 0; i < dr.HeaderTable.length; i++) {

                            result += dr.HeaderTable[i].trim() + "^";
                        }
                        result = result.substring(0, result.length() - 1);
                        result += "|";
                        for (int i = 0; i < dr.DataTable.length; i++) {

                            for (int j = 0; j < dr.DataTable[i].length; j++) {
                                try {
                                    result += dr.DataTable[i][j].trim() + "^";
                                } catch (Exception ex) {
                                    result += "" + "^";
                                }
                            }

                            result = result.substring(0, result.length() - 1);
                            result += "|";

                        }
                    } catch (Exception ex) {

                    }

                }
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                //result = result.length() + "#@@#" + result;
                out.println(result);

                System.out.println(result.length());
                out.flush();

            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
