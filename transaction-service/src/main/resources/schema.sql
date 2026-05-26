CREATE TABLE customers (

                           customer_id SERIAL PRIMARY KEY,

                           mobile VARCHAR(15),

                           email VARCHAR(100)

);



CREATE TABLE accounts (

    account_number VARCHAR(20) PRIMARY KEY,

    balance DECIMAL(10,2),

    account_type VARCHAR(20)

);




CREATE TABLE transactions (

                              id SERIAL PRIMARY KEY,

                              amount DECIMAL(10,2),

                              transaction_date TIMESTAMP
                                  DEFAULT CURRENT_TIMESTAMP

);



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

$$;



-- =========================================
-- Scenario 2
-- Takes input and returns output
-- =========================================

CREATE OR REPLACE PROCEDURE fund_transfer(

    IN p_from_account VARCHAR,
    IN p_to_account VARCHAR,
    IN p_amount NUMERIC,

    INOUT p_status VARCHAR,
    INOUT p_message VARCHAR

)

LANGUAGE plpgsql

AS
$$

DECLARE

    v_balance NUMERIC;

BEGIN

    SELECT balance

    INTO v_balance

    FROM public.accounts

    WHERE account_number = p_from_account;

    IF v_balance < p_amount THEN

        p_status := 'FAILED';

        p_message := 'Insufficient balance';

    ELSE

        UPDATE public.accounts

        SET balance = balance - p_amount

        WHERE account_number = p_from_account;

        UPDATE public.accounts

        SET balance = balance + p_amount

        WHERE account_number = p_to_account;

        p_status := 'SUCCESS';

        p_message := 'Transfer completed';

    END IF;

END;

$$;



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

$$;



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
    SUM(amount)

INTO
    p_total_transactions,
    p_total_amount

FROM transactions

WHERE DATE(transaction_date) = CURRENT_DATE;

END;

$$;



-- =========================================
-- SAMPLE DATA
-- =========================================

INSERT INTO customers(mobile, email)

VALUES
    ('9999999999', 'abc@gmail.com'),
    ('8888888888', 'xyz@gmail.com');



INSERT INTO accounts(account_number, balance, account_type)

VALUES
   ('ACC1001', 10000, 'SAVINGS'),
   ('ACC1002', 5000, 'SAVINGS');




INSERT INTO transactions(amount)

VALUES
    (1000),
    (2000),
    (500);

