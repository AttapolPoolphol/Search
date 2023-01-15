/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author GT_4953
 */
public class _cl_SortResult {
    private final int INDEX_DIS = 23;
    private final int INDEX_SCORE = 24;
    private final int INDEX_POP = 25;
    private final int INDEX_HITSCORE = 26;
    
    private final int INDEX_GROUP_ALL = 38;
    private final int INDEX_GROUP_NAME = 39;
    
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
    private String[][] SortHit(String[][] input) {

        Arrays.sort(input, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final Double time1 = Double.parseDouble(entry1[INDEX_HITSCORE]);
                final Double time2 = Double.parseDouble(entry2[INDEX_HITSCORE]);
                return time2.compareTo(time1);
            }
        });

        return input;
    }
    private String[][] SortPop(String[][] input) {

        Arrays.sort(input, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final Double time1 = Double.parseDouble(entry1[INDEX_POP]);
                final Double time2 = Double.parseDouble(entry2[INDEX_POP]);
                return time2.compareTo(time1);
            }
        });

        return input;
    }
    private String[][] SortScore(String[][] input) {

        Arrays.sort(input, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                final Double time1 = Double.parseDouble(entry1[INDEX_SCORE]);
                final Double time2 = Double.parseDouble(entry2[INDEX_SCORE]);
                return time2.compareTo(time1);
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
    
    public _data_result SortAction(_data_result input)
    {
        input.DataTable = SortDistnce(input.DataTable);
        input.DataTable = SortHit(input.DataTable);
        input.DataTable = SortPop(input.DataTable);
        input.DataTable = SortScore(input.DataTable);
        input.DataTable = SortGroupName(input.DataTable);
        input.DataTable = SortGroupAll(input.DataTable);
        return input;
    }
    
    public _data_result SortAction_LCS(_data_result input)
    {
        input.DataTable = SortDistnce(input.DataTable);
        input.DataTable = SortHit(input.DataTable);
        input.DataTable = SortPop(input.DataTable);
        input.DataTable = SortScore(input.DataTable);
        //input.DataTable = SortGroupName(input.DataTable);
        input.DataTable = SortGroupAll(input.DataTable);
        //input.DataTable = SortGroupName(input.DataTable);
        return input;
    }
    
    public _data_result Sort_Distance_Action(_data_result input)
    {
        input.DataTable = SortDistnce(input.DataTable);
        return input;
    }
}
