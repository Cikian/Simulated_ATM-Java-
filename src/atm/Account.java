package atm;

import java.sql.SQLException;
import java.util.Scanner;

public class Account {
    private String userName;
    private String gender;
    private Integer uID;
    private String passwd;
    private Double balance;
    private Double xianE;

    public Account() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer uID) {
        this.uID = uID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getXianE() {
        return xianE;
    }

    public void setXianE(Double xianE) {
        this.xianE = xianE;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // 创建用户信息
    public void createName() {
        System.out.println("请输入用户名：");
        this.setUserName(new Scanner(System.in).next());
    }

    public void createGender() {
        System.out.println("请输入性别：");
        String gender = new Scanner(System.in).next();
        if (gender.equals("男") || gender.equals("女")) {
            this.setGender(gender);
        } else {
            System.out.println("性别只能输入汉字“男”“女”哟~，请重新输入");
            createGender();
        }
    }

    public void createPasswd() {
        System.out.println("请输入密码：");
        String password = new Scanner(System.in).next();
        System.out.println("请再次输入密码：");
        String password2 = new Scanner(System.in).next();
        if (password.equals(password2)) {
            this.setPasswd(password);
        } else {
            System.out.println("两次密码不一致，请重新设置密码");
            createPasswd();
        }
    }

    public void createBalance() {
        System.out.println("请输入当前余额：");
        this.setBalance(new Scanner(System.in).nextDouble());
    }

    public void createXianE() {
        System.out.println("请输入单次取款限额：");
        this.setXianE(new Scanner(System.in).nextDouble());
    }

    @Override
    public String toString() {
        System.out.println("-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-");
        return "用户信息如下：" + '\n' + "{" +
                "用户名： '" + userName + '\'' +
                ", ID： " + uID +
                ", 密码： '" + passwd + '\'' +
                ", 当前余额： " + balance +
                ", 取款限额： " + xianE +
                '}' + '\n' + "***请牢记您的ID和密码，以便于下次登录***" + '\n' +
                "-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-·-";
    }

    public Account login() {
        Account account = new Account();
        String[] userInfo;
        System.out.println("请输入您的ID：");
        int uID = new Scanner(System.in).nextInt();
        // 接收用户输入后去数据库里查询信息
        JDBC jdbc = new JDBC();
        String sql = "select * from account where uid = " + uID;
        try {
            // 将查询到的用户信息保存到userInfo中
            userInfo = jdbc.getUserInfo(sql);
            if (userInfo == null) {
                System.out.println("无此用户，请重新输入");
                account = login();
            } else {
                // 密码认证
                boolean autFlag = autPasswd(userInfo);
                for (int i = 0; true; i++) {
                    // 若认证密码返回true，将查询到的信息赋给account对象
                    if (autFlag) {
                        account.setuID(Integer.parseInt(userInfo[0]));
                        account.setUserName(userInfo[1]);
                        account.setPasswd(userInfo[2]);
                        account.setBalance(Double.parseDouble(userInfo[3]));
                        account.setXianE(Double.parseDouble(userInfo[4]));
                        account.setGender(userInfo[5]);
                        System.out.println("登录成功...");
                        break;
                    } else {
                        System.out.println("密码错误，请重新输入!");
                        System.out.println("您还有" + (2 - i) + "次机会");
                        autFlag = autPasswd(userInfo);
                    }
                    if (i == 2) {
                        System.out.println("密码错误次数过多，程序退出，感谢您的使用！");
                        System.exit(0);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return account;
    }

    // 认证密码
    public boolean autPasswd(String[] userInfo) {
        System.out.println("请输入您的密码：");
        String passwd = new Scanner(System.in).next();
        return passwd.equals(userInfo[2]);
    }

    // 登录成功后显示操作菜单
    public void showMenu(Account account) {
        if (this.getGender().equals("男")) {
            System.out.println("您好，" + account.getUserName() + "先生，欢迎使用ATM机！");
        } else if (this.getGender().equals("女")) {
            System.out.println("您好，" + account.getUserName() + "女士，欢迎使用ATM机！");
        } else {
            System.out.println("您好，" + account.getUserName() + "，欢迎使用ATM机！");
        }
        System.out.println("请选择您要进行的操作：");
        System.out.println("1.存款");
        System.out.println("2.取款");
        System.out.println("3.转账");
        System.out.println("4.查询余额");
        System.out.println("5.修改账户信息");
        System.out.println("6.退出");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                saveMoney(account);
                break;
            case 2:
                getMoney(account);
                break;
            case 3:
                transferMoney(account);
                break;
            case 4:
                showBalance();
                break;
            case 5:
                updateAccount(account);
                break;
            case 6:
                System.out.println("感谢您的使用，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("输入错误，请重新输入！");
                showMenu(account);
                break;
        }
    }

    private void updateAccount(Account account) {
        JDBC jdbc = new JDBC();
        System.out.println("更新账户信息：");
        System.out.println(account.getUserName());
        System.out.println(account.getPasswd());
        System.out.println(account.getXianE());
        System.out.println("请输入您要更新的信息：");
        System.out.println("1.用户名");
        System.out.println("2.密码");
        System.out.println("3.取款限额");
        System.out.println("4.返回上级菜单");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.println("请输入您的新用户名：");
                String newUserName = scanner.next();
                account.setUserName(newUserName);
                String sql = "update account set username = '" + account.getUserName() + "' where uid = " + account.getuID();
                jdbc.update(sql);
                System.out.println("更新成功！");
                updateAccount(account);
                break;
            case 2:
                System.out.println("请输入您的新密码：");
                String newPasswd = scanner.next();
                account.setPasswd(newPasswd);
                String sql1 = "update account set passwd = '" + account.getPasswd() + "' where uid = " + account.getuID();
                jdbc.update(sql1);
                System.out.println("更新成功！");
                updateAccount(account);
                break;
            case 3:
                System.out.println("请输入您的新取款限额：");
                double newXianE = scanner.nextDouble();
                account.setXianE(newXianE);
                String sql2 = "update account set xiane = " + account.getXianE() + " where uid = " + account.getuID();
                jdbc.update(sql2);
                System.out.println("更新成功！");
                updateAccount(account);
                break;
            case 4:
                showMenu(account);
                break;
            default:
                System.out.println("输入错误，请重新输入！");
                updateAccount(account);
                break;
        }
    }

    public void saveMoney(Account account) {
        System.out.println("请输入您要存入的金额：");
        double money = new Scanner(System.in).nextDouble();
        double balance = account.getBalance();
        balance += money;
        account.setBalance(balance);
        JDBC jdbc = new JDBC();
        String sql = "update account set balance = " + balance + " where uid = " + account.getuID();
        jdbc.update(sql);
        System.out.println("存款成功，您的余额为：" + balance);
        showMenu(account);
    }

    public void getMoney(Account account) {
        System.out.println("请输入您要取出的金额：");
        double money = new Scanner(System.in).nextDouble();
        double balance = this.getBalance();
        if (money > account.getXianE()) {
            System.out.println("您的单次取款限额为：" + account.getXianE() + "，请重新输入！");
            getMoney(account);
        } else if (money > balance) {
            System.out.println("您的余额不足，请重新输入！");
            getMoney(account);
        } else {
            balance -= money;
            account.setBalance(balance);
            JDBC jdbc = new JDBC();
            String sql = "update account set balance = " + balance + " where uid = " + account.getuID();
            jdbc.update(sql);
            System.out.println("取款成功，您的余额为：" + balance);
            showMenu(account);
        }
    }

    public void transferMoney(Account account) {
        Account transferAccount = new Account();
        System.out.println("请输入您要转账的账户ID：");
        int uID = new Scanner(System.in).nextInt();
        String[] transferUserInfo;


        JDBC jdbc = new JDBC();
        String sql = "select * from account where uid = " + uID;
        try {
            transferUserInfo = jdbc.getUserInfo(sql);
            if (transferUserInfo == null) {
                System.out.println("无此用户，请重新输入");
                transferMoney(account);
            } else {
                transferAccount.setuID(Integer.parseInt(transferUserInfo[0]));
                transferAccount.setUserName(transferUserInfo[1]);
                transferAccount.setPasswd(transferUserInfo[2]);
                transferAccount.setBalance(Double.parseDouble(transferUserInfo[3]));
                transferAccount.setXianE(Double.parseDouble(transferUserInfo[4]));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("请确认您要转账的账户信息：");
        System.out.println("账户ID：" + transferAccount.getuID());
        System.out.println("用户名：" + transferAccount.getUserName());
        System.out.println("请输入您要转账的金额：");
        double money = new Scanner(System.in).nextDouble();
        double thisBalance = account.getBalance();
        double thatBalance = transferAccount.getBalance();
        thisBalance -= money;
        thatBalance += money;
        account.setBalance(thisBalance);
        transferAccount.setBalance(thatBalance);
        sql = "update account set balance = " + thisBalance + " where uid = " + account.getuID();
        jdbc.update(sql);
        sql = "update account set balance = " + thatBalance + " where uid = " + transferAccount.getuID();
        jdbc.update(sql);
        System.out.println("转账成功，您的余额为：" + thisBalance);
        showMenu(account);
    }

    private void showBalance() {
        System.out.println("您的余额为：" + this.getBalance());
        showMenu(this);
    }
}