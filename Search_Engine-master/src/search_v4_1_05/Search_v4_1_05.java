/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.net.ServerSocket;
import java.net.Socket;
// beta 3
// new class _cl_NostraSearch

/**
 *
 * @author GT_4953
 */
public class Search_v4_1_05 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //_cl_Search_init init = new _cl_Search_init("D:\\[github]\\netbean\\search_v5\\path_index.properties");

            _cl_Search_init init = new _cl_Search_init(args[0]);
            try {
                System.out.println(args[0]);
            } catch (Exception e) {
            }

            boolean b = init.Init_SearchIndex();

            if (b) {
                System.out.println("index=" + init.INDEXPATH);
            } else {
                System.out.println("index=ERROR");
            }

            b = init.Init_AutoTHIndex();

            if (b) {
                System.out.println("index auto th=" + init.INDEX_AUTO_TH);
            } else {
                System.out.println("index auto th=ERROR");
            }

            b = init.Init_AutoENIndex();

            if (b) {
                System.out.println("index auto en=" + init.INDEX_AUTO_EN);
            } else {
                System.out.println("index auto en=ERROR");
            }

            b = init.Init_SearchBIG();

            if (b) {
                System.out.println("index BIG =" + init.INDEX_BIG);
            } else {
                System.out.println("index BIG=ERROR");
            }

            b = init.Init_CHAR_RELATE();
            if (b) {
                System.out.println("char OK");
            } else {
                System.out.println("char = ERROR");
            }

            b = init.Init_GeneralWord();
            if (b) {
                System.out.println("GWORD OK");
            } else {
                System.out.println("GWORD = ERROR");
            }

            int port = init.SOCKET_PORT;
            System.out.println("port=" + init.SOCKET_PORT);

            ServerSocket socketServeur = new ServerSocket(port);
            socketServeur.setSoTimeout(0);
            while (true) {
                try {
                    Socket socketClient = socketServeur.accept();
                    //_cl_ListenAction t = new _cl_ListenAction(socketClient, init.INDEXPATH, init.INDEX_AUTO_TH, init.INDEX_AUTO_EN, init.searcher, init.SOCKET_PORT, init.PROCESS_ROW, init.searcher_auto_EN, init.searcher_auto_TH, init.searcher_BIG, init.dataList,init.dataGeneralWord);
                    _cl_ListenAction t = new _cl_ListenAction(socketClient,init);
                    t.start();
                    System.out.println("Connected to client : " + socketClient.getInetAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
