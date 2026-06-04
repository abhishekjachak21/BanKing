package com.bank.transaction.repository;

import com.bank.transaction.entity.Account;
import com.bank.transaction.dto.FundTransferResponse;
import com.bank.transaction.dto.DailyTransactionReportResponse;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                                "SELECT * FROM find_account(?)"
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
                        resultSet.getBigDecimal(
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
            BigDecimal updatedBalance
    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "CALL update_balance(?, ?)"
                        )

        ) {

            callableStatement.setString(
                    1,
                    accountNumber
            );

            callableStatement.setBigDecimal(
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
            BigDecimal amount
    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "CALL insert_transaction(?, ?, ?)"
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

            callableStatement.setBigDecimal(
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
                                "CALL update_customer_kyc(?, ?, ?)"
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
            BigDecimal amount

    ) {

        try (

                CallableStatement callableStatement =
                        connection.prepareCall(
                                "CALL fund_transfer(?, ?, ?, ?, ?, ?)"
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

            callableStatement.setBigDecimal(
                    3,
                    amount
            );

            callableStatement.registerOutParameter(
                    4,
                    Types.INTEGER
            );

            callableStatement.registerOutParameter(
                    5,
                    Types.VARCHAR
            );

            callableStatement.registerOutParameter(
                    6,
                    Types.VARCHAR
            );


            callableStatement.registerOutParameter(
                    4,
                    Types.INTEGER
            );

            callableStatement.registerOutParameter(
                    5,
                    Types.VARCHAR
            );

            callableStatement.registerOutParameter(
                    6,
                    Types.VARCHAR
            );

            callableStatement.execute();

            Integer transactionId =
                    callableStatement.getInt(4);

            String status =
                    callableStatement.getString(5);

            String message =
                    callableStatement.getString(6);


            FundTransferResponse response = new FundTransferResponse();

            response.setTransactionId(Long.valueOf(transactionId));

            response.setStatus(status);

            response.setMessage(message);

            return response;


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
                                "CALL process_daily_interest()"
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
                                "CALL get_daily_transaction_report(?, ?)"
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

            BigDecimal totalAmount =
                    callableStatement.getBigDecimal(2);

            DailyTransactionReportResponse response =
                    new DailyTransactionReportResponse();

            response.setTotalTransactions(totalTransactions);

            response.setTotalDepositAmount(
                    totalAmount.setScale(2, RoundingMode.HALF_UP).toString()
            );

            return response;

        } catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }
}