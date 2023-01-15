/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_v4_1_05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author GT_4953
 */
public class _cl_database {

    private String SERVER_NAME;
    private String USERNAME;
    private String PASSWORD;
    private String DATABASENAME;

    private String TABLE_ADMIN1;
    private String TABLE_ADMIN2;
    private String TABLE_ADMIN3;

    private String TABLE_ADMIN;

    public _cl_database(_data_database init) {
        this.SERVER_NAME = init.DB_SERVER_NAME;
        this.USERNAME = init.DB_USERNAME;
        this.PASSWORD = init.DB_PASSWORD;
        this.DATABASENAME = init.DB_DATABASENAME;
        this.TABLE_ADMIN = init.DB_TABLE_ADMIN;

    }

    public ResultSet QueryAdminID_1(String Admin1String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement("SELECT * FROM " + TABLE_ADMIN + " where "
                    + " admin1_local = ? "
                    + " OR admin1_english = ?");
            pst.setString(1, Admin1String);
            pst.setString(2, RemoveSP_char(Admin1String));
            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public ResultSet QueryAdminID_2(String Admin2String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement("SELECT * FROM " + TABLE_ADMIN + " where "
                    + " admin2_local = ? "
                    + " OR admin2_english = ?");
            pst.setString(1, Admin2String);
            pst.setString(2, RemoveSP_char(Admin2String));
            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public ResultSet QueryAdminID_3(String Admin3String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement("SELECT * FROM " + TABLE_ADMIN + " where "
                    + " admin3_local = ? "
                    + " OR admin3_english = ?");
            pst.setString(1, Admin3String);
            pst.setString(2, RemoveSP_char(Admin3String));
            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public ResultSet QueryAdminID_12(String Admin1String, String Admin2String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement("SELECT * FROM " + TABLE_ADMIN + " where "
                    + " (admin1_local = ? "
                    + " OR admin1_english = ? )"
                    + " AND (admin2_local = ? "
                    + " OR admin2_english = ? )");
            pst.setString(1, Admin1String);
            pst.setString(2, RemoveSP_char(Admin1String));
            pst.setString(3, Admin2String);
            pst.setString(4, RemoveSP_char(Admin2String));
            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public ResultSet QueryAdminID_123(String Admin1String, String Admin2String, String Admin3String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement("SELECT * FROM " + TABLE_ADMIN + " where "
                    + " (admin1_local = ? "
                    + " OR admin1_english = ? )"
                    + " AND (admin2_local = ? "
                    + " OR admin2_english = ? )"
                    + " AND (admin3_local = ? "
                    + " OR admin3_english = ? )");
            pst.setString(1, Admin1String);
            pst.setString(2, RemoveSP_char(Admin1String));
            pst.setString(3, Admin2String);
            pst.setString(4, RemoveSP_char(Admin2String));
            pst.setString(5, Admin3String);
            pst.setString(6, RemoveSP_char(Admin3String));
            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public ResultSet QueryAdminID_23(String Admin2String, String Admin3String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement("SELECT * FROM " + TABLE_ADMIN + " where "
                    + "("
                    + "(admin2_local = ? "
                    + " OR admin2_english = ? )"
                    + " AND (admin3_local = ? "
                    + " OR admin3_english = ? )"
                    + ")"
                    + " OR (admin_ID2 = ? "
                    + " AND admin_ID3 = ? )"
            );
            int i = 1;
            pst.setString(i++, Admin2String);
            pst.setString(i++, RemoveSP_char(Admin2String));
            pst.setString(i++, Admin3String);
            pst.setString(i++, RemoveSP_char(Admin3String));
            pst.setString(i++, Admin2String);
            pst.setString(i++, Admin3String);

            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public ResultSet QueryAdminID_13(String Admin1String, String Admin3String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement("SELECT * FROM " + TABLE_ADMIN + " where "
                    + "("
                    + "(admin1_local = ? "
                    + " OR admin1_english = ? )"
                    + " AND (admin3_local = ? "
                    + " OR admin3_english = ? )"
                    + ")"
                    + " OR (admin_ID1 = ? "
                    + " AND admin_ID3 = ? )"
            );
            int i = 1;
            pst.setString(i++, Admin1String);
            pst.setString(i++, RemoveSP_char(Admin1String));
            pst.setString(i++, Admin3String);
            pst.setString(i++, RemoveSP_char(Admin3String));
            pst.setString(i++, Admin1String);
            pst.setString(i++, Admin3String);

            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public ResultSet QueryAdminID_Dynamic(String Admin1String, String Admin2String, String Admin3String) throws SQLException {
        Connection connect = null;
        ResultSet rs = null;
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(""
                    + "jdbc:sqlserver://" + SERVER_NAME + ":1433;databaseName=" + DATABASENAME + ";user=" + USERNAME + ";password=" + PASSWORD + "");
            if (connect != null) {
                System.out.println("Database Connected.");
            } else {
                System.out.println("Database Connect Failed.");
            }

            PreparedStatement pst = null;
            pst = connect.prepareStatement(
                    " declare @input_ad1 nvarchar(255)"
                    + " declare @input_ad2 nvarchar(255)"
                    + " declare @input_ad3 nvarchar(255)"
                    + " set @input_ad1 = ?"
                    + " set @input_ad2 = ?"
                    + " set @input_ad3 = ?"
                    + " SELECT * FROM " + TABLE_ADMIN + " where "
                    + "( @input_ad1  = '' or   (admin_id1 = @input_ad1 or admin1_local = @input_ad1 or admin1_english = @input_ad1))"
                    + " AND "
                    + "( @input_ad2  = '' or   (admin_id2 = @input_ad2 or admin2_local = @input_ad2 or admin2_english = @input_ad2))"
                    + " AND "
                    + "( @input_ad3 = '' or (admin_id3 = @input_ad3 or admin3_local = @input_ad3 or admin3_english = @input_ad3))"
            );
            int i = 1;
            pst.setString(i++, RemoveSP_char(Admin1String));
            pst.setString(i++, RemoveSP_char(Admin2String));
            pst.setString(i++, RemoveSP_char(Admin3String));

            rs = pst.executeQuery();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return rs;
    }

    public String RemoveSP_char(String input) {
        String output = input;
        output = output.replace("-", "");
        output = output.replace(" ", "");
        return output.trim().toUpperCase();

    }
}
