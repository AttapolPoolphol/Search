/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import java.util.Properties;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author GT_4953
 */
public class _cl_Search_init {

    public boolean INIT_STATUS = false;

    public int SOCKET_PORT;
    public int TIMEOUT;
    public int PROCESS_ROW;
    //public String WB_URL_SERVICE;
    public String INDEXPATH;
    public String INDEX_AUTO_TH;
    public String INDEX_AUTO_EN;
    public String INDEX_BIG;

    public String RCHAR_FILE;
    public String GWORD_FiLE;

    private IndexReader reader;
    private IndexReader reader_auto_TH;
    private IndexReader reader_auto_EN;
    public IndexSearcher searcher;
    public IndexSearcher searcher_auto_TH;
    public IndexSearcher searcher_auto_EN;
    public IndexSearcher searcher_BIG;

    public List<String> dataList;
    public List<String> dataGeneralWord;

    public String DB_SERVER_NAME;
    public String DB_USERNAME;
    public String DB_PASSWORD;
    public String DB_DATABASENAME;

    public String DB_TABLE_ADMIN1;
    public String DB_TABLE_ADMIN2;
    public String DB_TABLE_ADMIN3;

    public String DB_TABLE_ADMIN;

    public _cl_Search_init(String ConfigFile) {
        try {
            //File configFile = new File("path_index.properties");
            File configFile = new File(ConfigFile);//("path_index.properties");
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            //WB_URL_SERVICE = props.getProperty("webservice_wb");
            SOCKET_PORT = Integer.parseInt(props.getProperty("SOCKET_PORT"));
            TIMEOUT = Integer.parseInt(props.getProperty("TIMEOUT"));
            INDEXPATH = props.getProperty("index");
            INDEX_AUTO_TH = props.getProperty("auto_th");
            INDEX_AUTO_EN = props.getProperty("auto_en");
            PROCESS_ROW = Integer.parseInt(props.getProperty("ProcessRow"));
            INDEX_BIG = props.getProperty("big");

            RCHAR_FILE = props.getProperty("r_char");
            GWORD_FiLE = props.getProperty("generalWord");

            DB_SERVER_NAME = props.getProperty("DB_SERVER_NAME");
            DB_USERNAME = props.getProperty("DB_USERNAME");
            DB_PASSWORD = props.getProperty("DB_PASSWORD");
            DB_DATABASENAME = props.getProperty("DB_DATABASENAME");

            DB_TABLE_ADMIN1 = props.getProperty("DB_TABLE_ADMIN1");
            DB_TABLE_ADMIN2 = props.getProperty("DB_TABLE_ADMIN2");
            DB_TABLE_ADMIN3 = props.getProperty("DB_TABLE_ADMIN3");

            DB_TABLE_ADMIN = props.getProperty("DB_TABLE_ADMIN");

            INIT_STATUS = true;
        } catch (Exception e) {
            INIT_STATUS = false;
        }

    }

    public boolean Init_SearchIndex() {
        try {
            Path path = FileSystems.getDefault().getPath(INDEXPATH);
            Directory directory = FSDirectory.open(path);
            reader = DirectoryReader.open(directory);
            searcher = new IndexSearcher(reader);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean Init_AutoTHIndex() {
        try {
            Path path = FileSystems.getDefault().getPath(INDEX_AUTO_TH);
            Directory directory = FSDirectory.open(path);
            reader_auto_TH = DirectoryReader.open(directory);
            searcher_auto_TH = new IndexSearcher(reader_auto_TH);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean Init_AutoENIndex() {
        try {
            Path path = FileSystems.getDefault().getPath(INDEX_AUTO_EN);
            Directory directory = FSDirectory.open(path);
            reader_auto_EN = DirectoryReader.open(directory);
            searcher_auto_EN = new IndexSearcher(reader_auto_EN);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean Init_SearchBIG() {
        try {
            Path path = FileSystems.getDefault().getPath(INDEX_BIG);
            Directory directory = FSDirectory.open(path);
            reader = DirectoryReader.open(directory);
            searcher_BIG = new IndexSearcher(reader);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean Init_CHAR_RELATE() {
        //String path = "D:\\syno\\char.txt";
        File file = new File(RCHAR_FILE);
        dataList = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                dataList.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean Init_GeneralWord() {
        //String path = "D:\\syno\\char.txt";
        File file = new File(GWORD_FiLE);
        dataGeneralWord = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                dataGeneralWord.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
