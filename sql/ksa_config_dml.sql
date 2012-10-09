--------------------------------------------------------
--  File created - Tuesday-April-10-2012   
--------------------------------------------------------

--  SYSTEM CONFIGURATION PARAMETERS --
-- Rice-specific parameters --
insert into KSSA_CONFIG (NAME, VALUE) values ('application.host', 'localhost');

-- KSA-specific parameters --
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.locale.lang', 'en');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.locale.country', 'US');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.import.single.batch.failure', 'false');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.logging.operation', 'true');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.deferment.type.id', 'DEF');

-- General Ledger settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.general.ledger.type', 'SAR');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.general.ledger.mode', 'Individual');

-- Combined Cash Limit Tracking Settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.system', 'On');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.amount', '10000');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.days', '365');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.tag', 'cash');

-- Memo settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.memo.level', '2');

-- Account settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.account.status.type', 'N/A');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.account.credit.limit', '20000');

-- Drools settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.drools.dsl', 'ksa.dsl');

