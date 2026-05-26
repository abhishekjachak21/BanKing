CREATE TABLE IF NOT EXISTS customers (

                                         customer_id SERIAL PRIMARY KEY,

                                         mobile VARCHAR(15),

    email VARCHAR(100)

    ) @@



    CREATE TABLE IF NOT EXISTS accounts (

                                            id SERIAL,

                                            account_number VARCHAR(20) PRIMARY KEY,

    holder_name VARCHAR(100),

    email VARCHAR(100),

    ifsc_code VARCHAR(20),

    balance DECIMAL(10,2),

    account_type VARCHAR(20)

    ) @@



    CREATE TABLE IF NOT EXISTS transactions (

                                                id SERIAL PRIMARY KEY,

                                                account_number VARCHAR(20),

    transaction_type VARCHAR(20),

    amount DECIMAL(10,2),

    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP

    ) @@



    -- =========================================
-- FIND ACCOUNT
-- RETURNS DATA
-- =========================================

    CREATE OR REPLACE FUNCTION find_account(

                                               p_account_number VARCHAR(20)

    )

    RETURNS TABLE(

                     id INT,
                     account_number VARCHAR(20),
    holder_name VARCHAR(100),
    email VARCHAR(100),
    ifsc_code VARCHAR(20),
    balance DECIMAL(10,2)

    )

    LANGUAGE plpgsql

AS
    $$

BEGIN

RETURN QUERY

SELECT
    a.id,
    a.account_number,
    a.holder_name,
    a.email,
    a.ifsc_code,
    a.balance

FROM accounts a

WHERE a.account_number = p_account_number;

END;

$$ @@



-- =========================================
-- UPDATE BALANCE
-- =========================================

CREATE OR REPLACE PROCEDURE update_balance(

    IN p_account_number VARCHAR(20),
    IN p_updated_balance DECIMAL(10,2)

)

LANGUAGE plpgsql

AS
$$

BEGIN

UPDATE accounts

SET balance = p_updated_balance

WHERE account_number = p_account_number;

END;

$$ @@



-- =========================================
-- INSERT TRANSACTION
-- =========================================

CREATE OR REPLACE PROCEDURE insert_transaction(

    IN p_account_number VARCHAR(20),
    IN p_transaction_type VARCHAR(20),
    IN p_amount DECIMAL(10,2)

)

LANGUAGE plpgsql

AS
$$

BEGIN

INSERT INTO transactions(

    account_number,
    transaction_type,
    amount

)

VALUES(

          p_account_number,
          p_transaction_type,
          p_amount

      );

END;

$$ @@

-- =========================================
-- Scenario 1
-- Takes input returns nothing
-- =========================================

CREATE OR REPLACE PROCEDURE update_customer_kyc(

    IN p_customer_id INT,
    IN p_mobile VARCHAR(15),
    IN p_email VARCHAR(100)

)

LANGUAGE plpgsql

AS
$$

BEGIN

UPDATE customers

SET
    mobile = p_mobile,
    email = p_email

WHERE customer_id = p_customer_id;

END;

$$ @@



-- =========================================
-- Scenario 2
-- Takes input and returns output
-- =========================================

DROP PROCEDURE IF EXISTS fund_transfer;

CREATE OR REPLACE PROCEDURE fund_transfer(

    IN p_from_account VARCHAR(20),
    IN p_to_account VARCHAR(20),
    IN p_amount DECIMAL(10,2),

    INOUT p_transaction_id INT,
    INOUT p_status VARCHAR(20),
    INOUT p_message VARCHAR(255)

)

LANGUAGE plpgsql

AS
$$

DECLARE

v_balance DECIMAL(10,2);

BEGIN

SELECT balance

INTO v_balance

FROM accounts

WHERE account_number = p_from_account;

IF v_balance < p_amount THEN

        p_transaction_id := 0;

        p_status := 'FAILED';

        p_message := 'Insufficient balance';

ELSE

UPDATE accounts

SET balance = balance - p_amount

WHERE account_number = p_from_account;

UPDATE accounts

SET balance = balance + p_amount

WHERE account_number = p_to_account;

INSERT INTO transactions(

    account_number,
    transaction_type,
    amount

)

VALUES(

          p_from_account,
          'TRANSFER',
          p_amount

      )

    RETURNING id INTO p_transaction_id;

p_status := 'SUCCESS';

        p_message := 'Transfer completed';

END IF;

END;

$$ @@

-- =========================================
-- Scenario 3
-- No input returns nothing
-- =========================================

CREATE OR REPLACE PROCEDURE process_daily_interest()

LANGUAGE plpgsql

AS
$$

BEGIN

UPDATE accounts

SET balance = balance + (balance * 0.03 / 365)

WHERE account_type = 'SAVINGS';

END;

$$ @@



-- =========================================
-- Scenario 4
-- No input returns output
-- =========================================

CREATE OR REPLACE PROCEDURE get_daily_transaction_report(

    INOUT p_total_transactions INT,
    INOUT p_total_amount DECIMAL(15,2)

)

LANGUAGE plpgsql

AS
$$

BEGIN

SELECT
    COUNT(*),
    COALESCE(SUM(amount), 0)

INTO
    p_total_transactions,
    p_total_amount

FROM transactions

WHERE DATE(transaction_date) = CURRENT_DATE;

END;

$$ @@



-- =========================================
-- SAMPLE DATA
-- =========================================

-- INSERT INTO customers(
--
--     mobile,
--     email
--
-- )
--
-- VALUES
--
-- ('9999999999', 'abc@gmail.com'),
--
-- ('8888888888', 'xyz@gmail.com') @@
--
--
--
-- INSERT INTO accounts(
--
--     account_number,
--     holder_name,
--     email,
--     ifsc_code,
--     balance,
--     account_type
--
-- )
--
-- VALUES
--
-- (
--     'ACC1001',
--     'Abhishek Sharma',
--     'abhi@gmail.com',
--     'SBIN0001234',
--     10000,
--     'SAVINGS'
-- ),
--
-- (
--     'ACC1002',
--     'Rahul Verma',
--     'rahul@gmail.com',
--     'HDFC0005678',
--     5000,
--     'SAVINGS'
-- ) @@
--
--
--
-- INSERT INTO transactions(
--
--     account_number,
--     transaction_type,
--     amount
--
-- )
--
-- VALUES
--
-- ('ACC1001', 'DEPOSIT', 1000),
--
-- ('ACC1002', 'WITHDRAW', 2000),
--
-- ('ACC1001', 'DEPOSIT', 500) @@