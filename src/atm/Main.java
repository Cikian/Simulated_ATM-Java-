package atm;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 主界面
        while (true) {
            System.out.println("------欢迎使用ATM------");
            System.out.println("请先登录或者注册");
            System.out.println("1.登录");
            System.out.println("2.注册");
            System.out.println("3.退出");
            System.out.println("请输入你的选择：");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Account account_login = new Account();
                    account_login = account_login.login();
                    account_login.showMenu(account_login);
                    break;
                case 2:
                    JDBC jdbc = new JDBC();
                    try {
                        jdbc.createAccount();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:
                    System.out.println("感谢使用，再见！");
                    System.exit(0);
                    break;
            }
        }
    }
}