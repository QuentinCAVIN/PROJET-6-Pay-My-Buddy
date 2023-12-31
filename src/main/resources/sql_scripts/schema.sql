    CREATE TABLE bank_account_seq (
        next_val bigint
    );

    CREATE TABLE paymybuddy_bank_account (
        id INTEGER NOT NULL PRIMARY KEY,
        account_balance DECIMAL(25, 2) NOT NULL
    );

    CREATE TABLE personal_bank_account (
        id INTEGER NOT NULL PRIMARY KEY,
        account_balance DECIMAL(25, 2) NOT NULL,
        iban VARCHAR(255) NOT NULL UNIQUE
    );

    CREATE TABLE transfer (
        id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
        amount DECIMAL(25, 2) NOT NULL,
        date VARCHAR(255) NOT NULL,
        description VARCHAR(255) NOT NULL,
        recipient_account_id INTEGER NOT NULL,
        sender_account_id INTEGER NOT NULL
    );

    CREATE TABLE user (
        id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
        email VARCHAR(255) NOT NULL UNIQUE,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        paymybuddy_bank_account_id INTEGER NOT NULL UNIQUE,
        personal_bank_account_id INTEGER UNIQUE
    );

    CREATE TABLE user_user (
        user1_id INTEGER NOT NULL,
        user2_id INTEGER NOT NULL,
        UNIQUE (user1_id, user2_id)
    );

    ALTER TABLE user
    ADD CONSTRAINT FK_paymybuddy_bank_account
      FOREIGN KEY (paymybuddy_bank_account_id)
       REFERENCES paymybuddy_bank_account (id);

    ALTER TABLE user
    ADD CONSTRAINT FK_personal_bank_account
       FOREIGN KEY (personal_bank_account_id)
       REFERENCES personal_bank_account (id);

    ALTER TABLE user_user
    ADD CONSTRAINT FK_user1
       FOREIGN KEY (user2_id)
       REFERENCES user (id);

    ALTER TABLE user_user
    ADD CONSTRAINT FK_user2
       FOREIGN KEY (user1_id)
       REFERENCES user (id);