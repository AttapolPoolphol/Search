/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

/**
 *
 * @author GT_4953
 */
public class _cl_EngineFormat {

    public enum MESSAGE_MODE {
        NONE,
        MODE_SEARCH,
        MODE_AUTOCOMPLETE_TH,
        MODE_AUTOCOMPLETE_EN,
        MODE_IDEN,
        MODE_SUM_CAT,
        MODE_SUM_SUB,
        MODE_TOTAL_SEARCH_RESULT,
        MODE_SEARCH_NEARBY,
        MODE_SEARCH_CLOSET,
        MODE_SEARCH_SOUND,
        MODE_TOTAL_SEARCH_RESULT_SOUND,
        MODE_SEARCH_NEARBY_SOUND,
        MODE_SEARCH_CLOSET_SOUND,
        MODE_SEARCH_NOSTRA_ID,
        MODE_SEARCH_BIG
    }
/*
    public _data_input Message_autocomplete_split(String input) {
        _data_input res = new _data_input();
        try {
            String[] tmp = input.split(";");
            res.keyword = tmp[1];
            res.MaxReturn = Integer.parseInt(tmp[2].trim());
            return res;

        } catch (Exception e) {
            return null;
        }
    }
*/
    public _data_input Message_autocomplete_split(String input) {
        _data_input res = new _data_input();
        try {
            String[] tmp = input.split(";");
            res.keyword = tmp[1];
            res.MaxReturn = Integer.parseInt(tmp[2].trim());
            res.Lat = tmp[3].trim();
            res.Lon = tmp[4].trim();
            try {
                String tmpFlag = tmp[5].trim().toUpperCase();
                if (tmpFlag.equals("PREFIX")) {
                    res.prefixMode = Boolean.TRUE;
                } else {
                    res.prefixMode = Boolean.FALSE;
                }
            } catch (Exception e) {
                res.prefixMode = Boolean.FALSE;
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }
    public _data_input Message_iden_split(String input) {
        _data_input res = new _data_input();
        try {
            String[] tmp = input.split(";");
            res.Lat = tmp[1].trim();
            res.Lon = tmp[2].trim();
            res.radius = Double.parseDouble(tmp[3].trim());
            return res;

        } catch (Exception e) {
            return null;
        }
    }

    public _data_input Message_sumcat_split(String input) {
        _data_input res = new _data_input();
        try {
            String[] tmp = input.split(";");
            res.Lat = tmp[1].trim();
            res.Lon = tmp[2].trim();
            res.radius = Double.parseDouble(tmp[3].trim());
            res.Category = tmp[4].trim();
            return res;

        } catch (Exception e) {
            return null;
        }
    }

    public MESSAGE_MODE Message_mode(String input) {
        try {
            String[] tmp = input.split(";");
            int tmpInt = Integer.parseInt(tmp[0]);
            return MESSAGE_MODE.values()[tmpInt];
        } catch (Exception e) {
            return MESSAGE_MODE.NONE;
        }
    }

    public _data_input Message_split(String input) {
        _data_input tmp = new _data_input();
        String[] s = input.split(";");
        System.out.println(s.length);
        System.out.println(input);
        int i = 0;

        try {
            tmp.input_type = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.input_type = 0;
            i++;
        }

        tmp.keyword = s[i++].trim();
        tmp.search_keyword = s[i++].trim();
        tmp.search_keyword_WB = s[i++].trim();
        tmp.keyword_th = s[i++].trim();
        tmp.keyword_other = s[i++].trim();
        tmp.NostraID = s[i++].trim();
        tmp.AdminLevel_4 = s[i++].trim();
        tmp.AdminLevel_3 = s[i++].trim();
        tmp.AdminLevel_2 = s[i++].trim();
        tmp.AdminLevel_1 = s[i++].trim();
        tmp.hno = s[i++].trim();
        tmp.telephone = s[i++].trim();
        tmp.Category = s[i++].trim();
        tmp.LocalCatCode = s[i++].trim();
        tmp.Tag = s[i++].trim();
        tmp.PostCode = s[i++].trim();
        tmp.Lat = s[i++].trim();
        tmp.Lon = s[i++].trim();
        try {
            tmp.radius = Double.parseDouble(s[i++].trim());
        } catch (Exception ex) {
            tmp.radius = 0;
            i++;
        }
        try {
            tmp.MaxReturn = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.MaxReturn = 20;
            i++;
        }

        return tmp;
    }

    public _data_input_nearby Message_splitNearBy(String input) {
        _data_input_nearby tmp = new _data_input_nearby();
        String[] s = input.split(";");
        System.out.println(s.length);
        System.out.println(input);
        int i = 0;

        try {
            tmp.input_type = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.input_type = 0;
            i++;
        }

        tmp.keyword = s[i++].trim();
        tmp.search_keyword = s[i++].trim();
        tmp.search_keyword_WB = s[i++].trim();
        tmp.Category = s[i++].trim();
        tmp.LocalCatCode = s[i++].trim();
        tmp.Lat = s[i++].trim();
        tmp.Lon = s[i++].trim();

        String tmp_poly_all = s[i++].trim();
        String[] tmp_poly_arr = tmp_poly_all.split("\\?");
        tmp.PolygString_arr = new String[tmp_poly_arr.length];
        for (int arr = 0; arr < tmp_poly_arr.length; arr++) {
            tmp.PolygString_arr[arr] = tmp_poly_arr[arr];
        }
        try {
            tmp.MaxReturn = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.MaxReturn = 20;
            i++;
        }
        //tmp.Route = s[i++].trim();
        return tmp;
    }

    public _data_input_close Message_splitCloset(String input) {
        _data_input_close tmp = new _data_input_close();
        String[] s = input.split(";");
        System.out.println(s.length);
        System.out.println(input);
        int i = 0;

        try {
            tmp.input_type = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.input_type = 0;
            i++;
        }

        tmp.keyword = s[i++].trim();
        tmp.search_keyword = s[i++].trim();
        tmp.search_keyword_WB = s[i++].trim();
        tmp.Category = s[i++].trim();
        tmp.LocalCatCode = s[i++].trim();
        tmp.Lat = s[i++].trim();
        tmp.Lon = s[i++].trim();

        tmp.routeID = s[i++].trim();

        try {
            tmp.MaxReturn = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.MaxReturn = 20;
            i++;
        }
        //tmp.Route = s[i++].trim();
        return tmp;
    }

    public _data_input_soundex Message_split_sound(String input) {
        _data_input_soundex tmp = new _data_input_soundex();
        String[] s = input.split(";");
        System.out.println(s.length);
        System.out.println(input);
        int i = 0;

        try {
            tmp.input_type = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.input_type = 0;
            i++;
        }

        tmp.keyword = s[i++].trim();
        tmp.soundex_keyword = s[i++].trim();
        tmp.soundex_keyword_delimeter = s[i++].trim();
        tmp.keyword_th = s[i++].trim();
        tmp.keyword_other = s[i++].trim();
        tmp.NostraID = s[i++].trim();
        tmp.AdminLevel_4 = s[i++].trim();
        tmp.AdminLevel_3 = s[i++].trim();
        tmp.AdminLevel_2 = s[i++].trim();
        tmp.AdminLevel_1 = s[i++].trim();
        tmp.hno = s[i++].trim();
        tmp.telephone = s[i++].trim();
        tmp.Category = s[i++].trim();
        tmp.LocalCatCode = s[i++].trim();
        tmp.Tag = s[i++].trim();
        tmp.PostCode = s[i++].trim();
        tmp.Lat = s[i++].trim();
        tmp.Lon = s[i++].trim();
        try {
            tmp.radius = Double.parseDouble(s[i++].trim());
        } catch (Exception ex) {
            tmp.radius = 0;
            i++;
        }
        try {
            tmp.MaxReturn = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.MaxReturn = 20;
            i++;
        }

        return tmp;
    }

    public _data_input_nearby_soundex Message_splitNearBy_Sound(String input) {
        _data_input_nearby_soundex tmp = new _data_input_nearby_soundex();
        String[] s = input.split(";");
        System.out.println(s.length);
        System.out.println(input);
        int i = 0;

        try {
            tmp.input_type = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.input_type = 0;
            i++;
        }

        tmp.keyword = s[i++].trim();
        tmp.soundex_keyword = s[i++].trim();
        tmp.soundex_keyword_delimeter = s[i++].trim();
        tmp.Category = s[i++].trim();
        tmp.LocalCatCode = s[i++].trim();
        tmp.Lat = s[i++].trim();
        tmp.Lon = s[i++].trim();

        String tmp_poly_all = s[i++].trim();
        String[] tmp_poly_arr = tmp_poly_all.split("\\?");
        tmp.PolygString_arr = new String[tmp_poly_arr.length];
        for (int arr = 0; arr < tmp_poly_arr.length; arr++) {
            tmp.PolygString_arr[arr] = tmp_poly_arr[arr];
        }
        try {
            tmp.MaxReturn = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.MaxReturn = 20;
            i++;
        }
        //tmp.Route = s[i++].trim();
        return tmp;
    }

    public _data_input_close_soundex Message_splitCloset_Sound(String input) {
        _data_input_close_soundex tmp = new _data_input_close_soundex();
        String[] s = input.split(";");
        System.out.println(s.length);
        System.out.println(input);
        int i = 0;

        try {
            tmp.input_type = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.input_type = 0;
            i++;
        }

        tmp.keyword = s[i++].trim();
        tmp.soundex_keyword = s[i++].trim();
        tmp.soundex_keyword_delimeter = s[i++].trim();
        tmp.Category = s[i++].trim();
        tmp.LocalCatCode = s[i++].trim();
        tmp.Lat = s[i++].trim();
        tmp.Lon = s[i++].trim();

        tmp.routeID = s[i++].trim();

        try {
            tmp.MaxReturn = Integer.parseInt(s[i++].trim());
        } catch (Exception ex) {
            tmp.MaxReturn = 20;
            i++;
        }
        //tmp.Route = s[i++].trim();
        return tmp;
    }

    public _data_input_nid Message_nid_split(String input) {
        try {
            _data_input_nid tmp = new _data_input_nid();
            String[] s = input.split(";");
            System.out.println(input);
            tmp.input_type = 14;
            tmp.NostraID = s[1].toString().trim();

            return tmp;
        } catch (Exception ex) {
            return null;
        }

    }
}
