/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.util.List;

/**
 *
 * @author GT_4953
 */
public class _cl_NostraRanking {
    private List<String> DATA_CHAR_RELATE;
    public _cl_NostraRanking(List<String> data)
    {
        this.DATA_CHAR_RELATE = data;
    }
    
    public String LCS(String str1, String str2)
    {
        int l1 = str1.length();
        int l2 = str2.length();
 
        int[][] arr = new int[l1 + 1][l2 + 1];
 
        for (int i = l1 - 1; i >= 0; i--)
        {
            for (int j = l2 - 1; j >= 0; j--)
            {
                //if (str1.charAt(i) == str2.charAt(j))
                if (CharRelate(str1.charAt(i),str2.charAt(j)))
                    arr[i][j] = arr[i + 1][j + 1] + 1;
                else 
                    arr[i][j] = Math.max(arr[i + 1][j], arr[i][j + 1]);
            }
        }
 
        int i = 0, j = 0;
        StringBuffer sb = new StringBuffer();
        while (i < l1 && j < l2) 
        {
            //if (str1.charAt(i) == str2.charAt(j))
            if (CharRelate(str1.charAt(i),str2.charAt(j)))
            {
                sb.append(str1.charAt(i));
                i++;
                j++;
            }
            else if (arr[i + 1][j] >= arr[i][j + 1]) 
                i++;
            else
                j++;
        }
        return sb.toString();
    }
    
    public double LCS_Socre(String data,String keyword)
    {
        try{
        double data_d = data.trim().length();
        double keyword_d = keyword.trim().length();
        double lcs_d = LCS(data, keyword).trim().length();
        double lcs_score = lcs_d / Math.sqrt((lcs_d * data_d));
        //double lcs_score = lcs_d / Math.sqrt((lcs_d * keyword_d));
        return lcs_score;
        }catch(Exception ex){return 0;}
    }
    
    public double LCS_Socre2(String data,String keyword)
    {
        try{
        double data_d = data.trim().length();
        double keyword_d = keyword.trim().length();
        double lcs_d = LCS(data, keyword).trim().length();
        double lcs_score = lcs_d / Math.sqrt((lcs_d * data_d));
        //double lcs_score = lcs_d / Math.sqrt((lcs_d * keyword_d));
        return lcs_score;
        }catch(Exception ex){return 0;}
    }
    
    public boolean CharRelate(char a , char b)
    {
        if( a == b)
            return true;
        
        for(int i = 0 ;i<DATA_CHAR_RELATE.size();i++)
        {
            String tmp = DATA_CHAR_RELATE.get(i).trim();
            if(tmp.trim() == "" || tmp.length() !=3)
                continue;
                        
            if((a == tmp.charAt(0) && b == tmp.charAt(2)) 
               || 
               (b == tmp.charAt(0) && a == tmp.charAt(2))) 
            {
                    return true;
            }
            
        }
        
        return false;
    }
    
}
