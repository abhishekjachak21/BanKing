package com.bank.transaction.repository;

import com.bank.transaction.entity.Account;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Repository
public class AccountRepository {

    public Account findByAccountNumber(
            Connection connection,
            String accountNumber
    ) {

        String sql = """
            SELECT *
            FROM account
            WHERE account_number = ?
            """;

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    accountNumber
            );

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            if (resultSet.next()) {

                Account account =
                        new Account();

                account.setId(
                        resultSet.getLong("id")
                );

                account.setAccountNumber(
                        resultSet.getString(
                                "account_number"
                        )
                );

                account.setHolderName(
                        resultSet.getString(
                                "holder_name"
                        )
                );

                account.setEmail(
                        resultSet.getString("email")
                );

                account.setIfscCode(
                        resultSet.getString(
                                "ifsc_code"
                        )
                );

                account.setBalance(
                        resultSet.getDouble(
                                "balance"
                        )
                );

                return account;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }


    public void updateBalance(
            Connection connection,
            String accountNumber,
            Double updatedBalance
    ) {

        String sql = """
            UPDATE account
            SET balance = ?
            WHERE account_number = ?
            """;

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setDouble(
                    1,
                    updatedBalance
            );

            preparedStatement.setString(
                    2,
                    accountNumber
            );

            preparedStatement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public void insertTransaction(
            Connection connection,
            String accountNumber,
            String transactionType,
            Double amount
    ) {

        String sql = """
            INSERT INTO transactions
            (
                account_number,
                transaction_type,
                amount
            )
            VALUES (?, ?, ?)
            """;

        try (
                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(
                    1,
                    accountNumber
            );

            preparedStatement.setString(
                    2,
                    transactionType
            );

            preparedStatement.setDouble(
                    3,
                    amount
            );

            preparedStatement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}