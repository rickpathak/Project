package com.java.bank.dao;

import com.java.bank.model.Accounts;
import com.java.bank.util.ConnectionHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankDaoImpl implements BankDao {

  Connection conn = null;
  PreparedStatement pstmt = null;

  public int generateAccountNo() throws SQLException, ClassNotFoundException {
    conn = ConnectionHelper.getConnection();
    String cmd = "SELECT CASE WHEN MAX(AccountNo) IS NULL THEN 1 ELSE MAX(AccountNo)+1 END accno FROM Accounts";
    pstmt = conn.prepareStatement(cmd);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    return rs.getInt("accno");
  }

  @Override
  public String createAccount(Accounts account) throws SQLException, ClassNotFoundException {
    int accno = generateAccountNo();
    account.setAccountNo(accno);

    conn = ConnectionHelper.getConnection();
    String cmd = "INSERT INTO Accounts(AccountNo, FirstName, LastName, City, State, Amount, CheqFacil, AccountType, Status)"
      + " VALUES(?,?,?,?,?,?,?,?, 'active')";
    pstmt = conn.prepareStatement(cmd);

    pstmt.setInt(1, accno);
    pstmt.setString(2, account.getFirstName());
    pstmt.setString(3, account.getLastName());
    pstmt.setString(4, account.getCity());
    pstmt.setString(5, account.getState());
    pstmt.setDouble(6, account.getAmount());
    pstmt.setString(7, account.getCheqFacil());
    pstmt.setString(8, account.getAccountType());

    pstmt.executeUpdate();
    return "Bank Account Created Successfully...";
  }

  @Override
  public Accounts searchAccount(int accountNo) throws SQLException, ClassNotFoundException {
    conn = ConnectionHelper.getConnection();
    String cmd = "SELECT * FROM Accounts WHERE AccountNo = ?";
    pstmt = conn.prepareStatement(cmd);
    pstmt.setInt(1, accountNo);
    ResultSet rs = pstmt.executeQuery();

    Accounts accounts = null;

    if (rs.next()) {
      accounts = new Accounts();
      accounts.setAccountNo(rs.getInt("AccountNo"));
      accounts.setFirstName(rs.getString("FirstName"));
      accounts.setLastName(rs.getString("LastName"));
      accounts.setCity(rs.getString("City"));
      accounts.setState(rs.getString("State"));
      accounts.setAmount(rs.getDouble("Amount"));
      accounts.setCheqFacil(rs.getString("CheqFacil"));
      accounts.setAccountType(rs.getString("AccountType"));
      accounts.setStatus(rs.getString("Status"));     // NEW
    }
    return accounts;
  }

  @Override
  public String updateAccount(int accountNo, String city, String state) throws SQLException, ClassNotFoundException {
    Accounts accountFound = searchAccount(accountNo);

    if (accountFound != null) {

      if ("inactive".equalsIgnoreCase(accountFound.getStatus())) {
        return "Cannot update — Account is CLOSED.";
      }

      conn = ConnectionHelper.getConnection();
      String cmd = "UPDATE Accounts SET City = ?, State = ? WHERE AccountNo = ?";
      pstmt = conn.prepareStatement(cmd);
      pstmt.setString(1, city);
      pstmt.setString(2, state);
      pstmt.setInt(3, accountNo);
      pstmt.executeUpdate();
      return "Account Updated Successfully...";
    }
    return "Account No Not Found...";
  }

  @Override
  public String closeAccount(int accountNo) throws SQLException, ClassNotFoundException {
    Accounts account = searchAccount(accountNo);

    if (account != null) {
      conn = ConnectionHelper.getConnection();
      String cmd = "UPDATE Accounts SET Status = 'inactive' WHERE AccountNo = ?";
      pstmt = conn.prepareStatement(cmd);
      pstmt.setInt(1, accountNo);
      pstmt.executeUpdate();
      return "Account Closed Successfully...";
    }
    return "Account No Not Found...";
  }

  @Override
  public String depositAccount(int accountNo, double amount) throws SQLException, ClassNotFoundException {
    Accounts accounts = searchAccount(accountNo);

    if (accounts != null) {

      if ("inactive".equalsIgnoreCase(accounts.getStatus())) {
        return "Cannot deposit — Account is CLOSED.";
      }

      conn = ConnectionHelper.getConnection();
      String cmd = "UPDATE Accounts SET Amount = Amount + ? WHERE AccountNo = ?";
      pstmt = conn.prepareStatement(cmd);
      pstmt.setDouble(1, amount);
      pstmt.setInt(2, accountNo);
      pstmt.executeUpdate();

      cmd = "INSERT INTO Trans(AccountNo, TransAmount, TransType) VALUES(?,?,?)";
      pstmt = conn.prepareStatement(cmd);
      pstmt.setInt(1, accountNo);
      pstmt.setDouble(2, amount);
      pstmt.setString(3, "C");
      pstmt.executeUpdate();

      return "Amount Deposited Successfully...";
    }

    return "Account No Not Found...";
  }

  @Override
  public String withdrawAccount(int accountNo, double amount) throws SQLException, ClassNotFoundException {
    Accounts accounts = searchAccount(accountNo);

    if (accounts != null) {

      if ("inactive".equalsIgnoreCase(accounts.getStatus())) {
        return "Cannot withdraw — Account is CLOSED.";
      }

      double balance = accounts.getAmount();

      if (balance - amount >= 0) {
        conn = ConnectionHelper.getConnection();
        String cmd = "UPDATE Accounts SET Amount = Amount - ? WHERE AccountNo = ?";
        pstmt = conn.prepareStatement(cmd);
        pstmt.setDouble(1, amount);
        pstmt.setInt(2, accountNo);
        pstmt.executeUpdate();

        cmd = "INSERT INTO Trans(AccountNo, TransAmount, TransType) VALUES(?,?,?)";
        pstmt = conn.prepareStatement(cmd);
        pstmt.setInt(1, accountNo);
        pstmt.setDouble(2, amount);
        pstmt.setString(3, "D");
        pstmt.executeUpdate();

        return "Amount Withdrawn Successfully...";
      }
      return "Insufficient Balance...";
    }

    return "Account No Not Found...";
  }
}
