package com.bank.transaction.repository;

import com.bank.transaction.entity.Account;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

@Repository
public class AccountRepository {

    public Account findByAccountNumber(
            Connection connection,
            String accountNumber
    ) {

        try (CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call find_account(?) }"
                        )
        ) {

            callableStatement.setString(
                    1,
                    accountNumber
            );

            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {

                Account account = new Account();

                account.setId(resultSet.getLong("id"));

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
                        resultSet.getString(
                                "email"
                        )
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

        try (
                CallableStatement callableStatement =
                        connection.prepareCall("CALL update_balance(?, ?)")
        ) {

            callableStatement.setString(1, accountNumber);

            callableStatement.setBigDecimal(
                    2,
                    BigDecimal.valueOf(updatedBalance)
            );

            callableStatement.execute();

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

        try (
                CallableStatement callableStatement =
                        connection.prepareCall("CALL insert_transaction(?, ?, ?)")
        ) {

            callableStatement.setString(1, accountNumber);
            callableStatement.setString(2, transactionType);

            callableStatement.setBigDecimal(
                    3,
                    BigDecimal.valueOf(amount)
            );

            callableStatement.execute();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}