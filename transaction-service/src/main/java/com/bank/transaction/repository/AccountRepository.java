package com.bank.transaction.repository;

import com.bank.transaction.dto.AccountCreatedEvent;
import com.bank.transaction.dto.DailyTransactionReportResponse;
import com.bank.transaction.dto.FundTransferResponse;
import com.bank.transaction.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    // =========================================
    // FIND ACCOUNT
    // =========================================

    public Account findByAccountNumber(String accountNumber) {

        try {

            return jdbcTemplate.query(

                    "SELECT * FROM find_account(?)",

                    new Object[]{accountNumber},

                    (rs, rowNum) -> {

                        Account account = new Account();

                        account.setId(
                                rs.getLong("id")
                        );

                        account.setAccountNumber(
                                rs.getString("account_number")
                        );

                        account.setHolderName(
                                rs.getString("holder_name")
                        );

                        account.setEmail(
                                rs.getString("email")
                        );

                        account.setIfscCode(
                                rs.getString("ifsc_code")
                        );

                        account.setBalance(
                                rs.getBigDecimal("balance")
                        );

                        return account;

                    }

            ).stream().findFirst().orElse(null);

        }

        catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // UPDATE BALANCE
    // =========================================

    public void updateBalance(
            String accountNumber,
            BigDecimal updatedBalance
    ) {

        try {

            jdbcTemplate.update(

                    "CALL update_balance(?, ?)",

                    accountNumber,
                    updatedBalance

            );

        }

        catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // INSERT TRANSACTION
    // =========================================

    public void insertTransaction(

            String accountNumber,
            String transactionType,
            BigDecimal amount

    ) {

        try {

            jdbcTemplate.update(

                    "CALL insert_transaction(?, ?, ?)",

                    accountNumber,
                    transactionType,
                    amount

            );

        }

        catch (Exception exception) {

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

            Integer customerId,
            String mobile,
            String email

    ) {

        try {

            jdbcTemplate.update(

                    "CALL update_customer_kyc(?, ?, ?)",

                    customerId,
                    mobile,
                    email

            );

        }

        catch (Exception exception) {

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

            String fromAccount,
            String toAccount,
            BigDecimal amount

    ) {

        try {

            SimpleJdbcCall jdbcCall =
                    new SimpleJdbcCall(jdbcTemplate)

                            .withProcedureName(
                                    "fund_transfer"
                            )

                            .declareParameters(

                                    new SqlParameter(
                                            "p_from_account",
                                            Types.VARCHAR
                                    ),

                                    new SqlParameter(
                                            "p_to_account",
                                            Types.VARCHAR
                                    ),

                                    new SqlParameter(
                                            "p_amount",
                                            Types.NUMERIC
                                    ),

                                    new SqlOutParameter(
                                            "p_transaction_id",
                                            Types.INTEGER
                                    ),

                                    new SqlOutParameter(
                                            "p_status",
                                            Types.VARCHAR
                                    ),

                                    new SqlOutParameter(
                                            "p_message",
                                            Types.VARCHAR
                                    )

                            );

            Map<String, Object> inParams =
                    new HashMap<>();

            inParams.put(
                    "p_from_account",
                    fromAccount
            );

            inParams.put(
                    "p_to_account",
                    toAccount
            );

            inParams.put(
                    "p_amount",
                    amount
            );

            Map<String, Object> result =
                    jdbcCall.execute(
                            inParams
                    );

            FundTransferResponse response =
                    new FundTransferResponse();

            response.setTransactionId(

                    ((Number) result.get(
                            "p_transaction_id"
                    )).longValue()

            );

            response.setStatus(

                    (String) result.get(
                            "p_status"
                    )

            );

            response.setMessage(

                    (String) result.get(
                            "p_message"
                    )

            );

            return response;

        }

        catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }

    }

    // =========================================
    // SCENARIO 3
    // NO INPUT RETURNS NOTHING
    // =========================================

    public void processDailyInterest() {

        try {

            jdbcTemplate.update(

                    "CALL process_daily_interest()"

            );

        }

        catch (Exception exception) {

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
    getDailyTransactionReport() {

        try {

            SimpleJdbcCall jdbcCall =
                    new SimpleJdbcCall(
                            jdbcTemplate
                    )

                            .withProcedureName(
                                    "get_daily_transaction_report"
                            )

                            .declareParameters(

                                    new SqlOutParameter(
                                            "p_total_transactions",
                                            Types.INTEGER
                                    ),

                                    new SqlOutParameter(
                                            "p_total_amount",
                                            Types.NUMERIC
                                    )

                            );

            Map<String, Object> result =
                    jdbcCall.execute();

            DailyTransactionReportResponse response =
                    new DailyTransactionReportResponse();

            response.setTotalTransactions(

                    (Integer) result.get(
                            "p_total_transactions"
                    )

            );

            response.setTotalDepositAmount(

                    ((BigDecimal) result.get(
                            "p_total_amount"
                    ))

                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            )

                            .toString()

            );

            return response;

        }

        catch (Exception exception) {

            throw new RuntimeException(
                    exception.getMessage()
            );
        }
    }

    // =========================================
    // CREATE CUSTOMER
    // =========================================


    public Integer createCustomer(

            String customerName,
            String mobile,
            String email,
            String panNumber

    ) {

        return jdbcTemplate.queryForObject(

                "SELECT create_customer(?, ?, ?, ?)",

                Integer.class,

                customerName,
                mobile,
                email,
                panNumber

        );

    }

    // =========================================
    // CREATE ACCOUNT
    // =========================================

    public String createAccount(
            Integer customerId,
            String accountType,
            BigDecimal balance
    ) {

        return jdbcTemplate.queryForObject(

                "SELECT create_account(?, ?, ?)",

                String.class,

                customerId,
                accountType,
                balance

        );

    }


    public AccountCreatedEvent getAccountCreatedEventData(
            Integer customerId,
            String accountNumber
    ) {

        String sql = """
        SELECT
            c.customer_name,
            c.email,
            a.account_type
        FROM customers c
        JOIN accounts a
            ON c.customer_id = a.customer_id
        WHERE c.customer_id = ?
        AND a.account_number = ?
        """;

        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new AccountCreatedEvent(
                        rs.getString("customer_name"),
                        rs.getString("email"),
                        accountNumber,
                        rs.getString("account_type"),
                        "SBIN0001234",
                        null
                ),
                customerId,
                accountNumber
        );
    }

    //COMMON Small methods

    public boolean existsByEmail(String email) {

        Integer count = jdbcTemplate.queryForObject(

                "SELECT COUNT(*) FROM customers WHERE email = ?",

                Integer.class,

                email

        );

        return count != null && count > 0;

    }


    public boolean existsByMobile(String mobile) {

        Integer count = jdbcTemplate.queryForObject(

                "SELECT COUNT(*) FROM customers WHERE mobile = ?",

                Integer.class,

                mobile

        );

        return count != null && count > 0;

    }


    public boolean existsByPanNumber(String panNumber) {

        Integer count = jdbcTemplate.queryForObject(

                "SELECT COUNT(*) FROM customers WHERE pan_number = ?",

                Integer.class,

                panNumber

        );

        return count != null && count > 0;

    }

    public boolean existsByCustomerId(Integer customerId) {

        Integer count = jdbcTemplate.queryForObject(

                "SELECT COUNT(*) FROM accounts WHERE customer_id = ?",

                Integer.class,

                customerId

        );

        return count != null && count > 0;

    }


    public boolean customerExists(Integer customerId) {

        Integer count = jdbcTemplate.queryForObject(

                "SELECT COUNT(*) FROM customers WHERE customer_id = ?",

                Integer.class,

                customerId

        );

        return count != null && count > 0;

    }

}