package com.bank.transaction.repository;

import com.bank.transaction.dto.DailyTransactionReportResponse;
import com.bank.transaction.dto.FundTransferResponse;
import com.bank.transaction.entity.Account;

import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

@Repository
public class AccountRepository {

    // =========================================
    // FIND ACCOUNT
    // =========================================

    public Account findByAccountNumber(
            Connection connection,
            String accountNumber
    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call find_account(?) }"
                        )

        ) {

            callableStatement.setString(
                    1,
                    accountNumber
            );

            ResultSet resultSet =
                    callableStatement.executeQuery();

            if (resultSet.next()) {

                Account account = new Account();

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

            throw new RuntimeException(
                    exception.getMessage()
            );
        }

        return null;
    }

    // =========================================
    // UPDATE BALANCE
    // =========================================

    public void updateBalance(
            Connection connection,
            String accountNumber,
            Double updatedBalance
    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call update_balance(?, ?) }"
                        )

        ) {

            callableStatement.setString(
                    1,
                    accountNumber
            );

            callableStatement.setDouble(
                    2,
                    updatedBalance
            );

            callableStatement.execute();

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // INSERT TRANSACTION
    // =========================================

    public void insertTransaction(
            Connection connection,
            String accountNumber,
            String transactionType,
            Double amount
    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call insert_transaction(?, ?, ?) }"
                        )

        ) {

            callableStatement.setString(
                    1,
                    accountNumber
            );

            callableStatement.setString(
                    2,
                    transactionType
            );

            callableStatement.setDouble(
                    3,
                    amount
            );

            callableStatement.execute();

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // SCENARIO 1
    // TAKES INPUT RETURNS NOTHING
    // =========================================

    public void updateCustomerKyc(

            Connection connection,
            Integer customerId,
            String mobile,
            String email

    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call update_customer_kyc(?, ?, ?) }"
                        )

        ) {

            callableStatement.setInt(
                    1,
                    customerId
            );

            callableStatement.setString(
                    2,
                    mobile
            );

            callableStatement.setString(
                    3,
                    email
            );

            callableStatement.execute();

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // SCENARIO 2
    // TAKES INPUT RETURNS OUTPUT
    // =========================================

    public FundTransferResponse fundTransfer(

            Connection connection,
            String fromAccount,
            String toAccount,
            Double amount

    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call fund_transfer(?, ?, ?, ?, ?) }"
                        )

        ) {

            callableStatement.setString(
                    1,
                    fromAccount
            );

            callableStatement.setString(
                    2,
                    toAccount
            );

            callableStatement.setDouble(
                    3,
                    amount
            );

            callableStatement.registerOutParameter(
                    4,
                    Types.VARCHAR
            );

            callableStatement.registerOutParameter(
                    5,
                    Types.VARCHAR
            );

            callableStatement.execute();

            String status =
                    callableStatement.getString(4);

            String message =
                    callableStatement.getString(5);

            return new FundTransferResponse(
                    status,
                    message
            );

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // SCENARIO 3
    // NO INPUT RETURNS NOTHING
    // =========================================

    public void processDailyInterest(
            Connection connection
    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call process_daily_interest() }"
                        )

        ) {

            callableStatement.execute();

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // SCENARIO 4
    // NO INPUT RETURNS OUTPUT
    // =========================================

    public DailyTransactionReportResponse
    getDailyTransactionReport(
            Connection connection
    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "{ call get_daily_transaction_report(?, ?) }"
                        )

        ) {

            callableStatement.registerOutParameter(
                    1,
                    Types.INTEGER
            );

            callableStatement.registerOutParameter(
                    2,
                    Types.NUMERIC
            );

            callableStatement.execute();

            Integer totalTransactions =
                    callableStatement.getInt(1);

            Double totalAmount =
                    callableStatement.getDouble(2);

            return new DailyTransactionReportResponse(
                    totalTransactions,
                    totalAmount
            );

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }
}