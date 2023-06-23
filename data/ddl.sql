
CREATE TABLE IF NOT EXISTS bank_account(
        bank_account_id uuid,
        account_number varchar(10),
        agency varchar(4),
        bank_code varchar(3),
        primary key(bank_account_id)
);
CREATE TABLE IF NOT EXISTS card_holder(
        card_holder_id UUID,
        client_id UUID,
        credit_analysis_id UUID,
        status varchar(8),
        limit_value decimal(10,2),
        bank_account_fk uuid,
        created_at timestamp,
        updated_at timestamp,
        primary key (card_holder_id),
        foreign key (bank_account_fk) references bank_account (bank_account_id)
);
CREATE TABLE IF NOT EXISTS card(
        card_id UUID,
        card_holder_fk UUID,
        limit_value decimal(10,2),
        card_number varchar(19),
        cvv integer,
        due_date DATE,
        created_at timestamp,
        updated_at timestamp,
        primary key (card_id),
        foreign key (card_holder_fk) references card_holder (card_holder_id)
);
