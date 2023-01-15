/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import java.util.Locale;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.th.ThaiAnalyzer;
import org.apache.lucene.analysis.th.ThaiTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;

/**
 *
 * @author GT_4953
 */
public class _cl_WB_thai {

    

    public List<String> TokenWord(String test) {

        //final String test = "This is a test. How about that?! Huh?";
        
        try{StringReader reader = new StringReader(test);
        ThaiTokenizer tokenizer = new ThaiTokenizer();
        tokenizer.setReader(reader);
        
        CharTermAttribute charTermAttrib = tokenizer.getAttribute(CharTermAttribute.class);
        TypeAttribute typeAtt = tokenizer.getAttribute(TypeAttribute.class);
        OffsetAttribute offset = tokenizer.getAttribute(OffsetAttribute.class);
        List<String> tokens = new ArrayList<String>();
        tokenizer.reset();
        while (tokenizer.incrementToken()) {
            tokens.add(charTermAttrib.toString());
            System.out.println(charTermAttrib.toString());
            //System.out.println(typeAtt.toString() + " " + offset.toString() + ": " + charTermAttrib.toString());
        }
        tokenizer.end();
        tokenizer.close();
        
        return tokens;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    public String WordB(String input)
    {
        try {
            List<String> tmp = TokenWord(input.toUpperCase());
            String output = "";
            for(int i = 0 ; i < tmp.size() ; i++)
            {
                output += tmp.get(i) + " ";
            }
            return  output.trim();
        } catch (Exception e) {
            return "";
        }
    }
    
    public String AllAction(String input)
    {
        String[] tmp = input.split("\\|");
        String output = "";
        for(int a = 0 ;a < tmp.length;a++)
        {
            output += WordB( tmp[a]).trim() + "|";
        }
        output = output.substring(0,output.length() - 1);
        return output;
    }

}
