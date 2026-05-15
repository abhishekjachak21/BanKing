package com.bank.transaction.repository;

import com.bank.transaction.dto.DepositRequest;
import com.bank.transaction.util.DBConnectionUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class TransactionRepository {

    public void depositAmount(DepositRequest request){

            Connection connection = null;

            try {

                connection = DBConnectionUtil.getConnection();

                connection.setAutoCommit(false);

                String updateBalanceQuery = "UPDATE account " + "SET balance = balance + ? " + "WHERE account_number = ?";

                PreparedStatement updateStmt = connection.prepareStatement(updateBalanceQuery);

                updateStmt.setDouble(1, request.getAmount());

                updateStmt.setString(2, request.getAccountNumber());

                updateStmt.executeUpdate();

                String insertTransactionQuery =
                        "INSERT INTO transactions " +
                                "(account_number, transaction_type, amount) " +
                                "VALUES (?, ?, ?)";

                PreparedStatement insertStmt =
                        connection.prepareStatement(
                                insertTransactionQuery
                        );

                insertStmt.setString(
                        1,
                        request.getAccountNumber()
                );

                insertStmt.setString(
                        2,
                        "DEPOSIT"
                );

                insertStmt.setDouble(
                        3,
                        request.getAmount()
                );

                insertStmt.executeUpdate();

                connection.commit();

            } catch (Exception e) {

                try {

                    if (connection != null) {
                        connection.rollback();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                e.printStackTrace();

            } finally {

                try {

                    if (connection != null) {
                        connection.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

}




