package atm;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

public class JDBC {
    static String url;
    static String user;
    static String password;

    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileReader("src/cfg.properties"));
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getUserInfo(String sql) throws SQLException {
        String[] userInfo;
        Connection conn;
        {
            try {
                conn = DriverManager.getConnection(url, user, password);
                PreparedStatement ps;
                ResultSet set;
                {
                    try {
                        ps = conn.prepareStatement(sql);
                        set = ps.executeQuery();
                        set.next();
                        String uID = set.getString("uid");
                        String name = set.getString("userName");
                        String passwd = set.getString("passwd");
                        double balance = set.getDouble("balance");
                        double xianE = set.getDouble("xianE");
                        String gender = set.getString("gender");
                        userInfo = new String[]{uID, name, passwd, Double.toString(balance), Double.toString(xianE), gender};
                    } catch (SQLException e) {
                        return null;
                    }
                    try {
                        set.close();
                        ps.close();
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return userInfo;
    }

    public void createAccount() throws SQLException {
        Account account = new Account();
        account.createName();
        account.createGender();
        account.createPasswd();
        account.createBalance();
        account.createXianE();

        // 随机生成3个3位数字
        Random ran = new Random();
        int ID1 = ran.nextInt(100, 1000);
        int ID2 = ran.nextInt(100, 1000);
        int ID3 = ran.nextInt(100, 1000);
        String ID = "" + ID1 + ID2 + ID3;
        Integer uID = Integer.parseInt(ID);
        account.setuID(uID);
        Connection conn;
        {
            try {
                conn = DriverManager.getConnection(url, user, password);
                PreparedStatement ps;
                {
                    try {
                        String sql = "insert into account (uid,userName,gender, passwd, balance, xianE) values ( ?,?, ?, ?, ?, ?)";
                        ps = conn.prepareStatement(sql);
                        ps.setString(1, String.valueOf(account.getuID()));
                        ps.setString(2, account.getUserName());
                        ps.setString(3, account.getGender());
                        ps.setString(4, account.getPasswd());
                        ps.setDouble(5, account.getBalance());
                        ps.setDouble(6, account.getXianE());
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ps.close();
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("注册成功！");
        System.out.println(account);
        System.out.println("即将返回主界面...");
    }

    public void update(String sql) {
        Connection conn;
        {
            try {
                conn = DriverManager.getConnection(url, user, password);
                PreparedStatement ps;
                {
                    try {
                        ps = conn.prepareStatement(sql);
                        ps.executeUpdate();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ps.close();
                        conn.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
