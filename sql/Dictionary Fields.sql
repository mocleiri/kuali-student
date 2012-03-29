-- Creating basic flag types.

INSERT INTO KSA."KSSA_FLAG_TYPE" (ID, CREATOR_ID, DESCRIPTION, LAST_UPDATE, NAME, ACCESS_LEVEL) VALUES ('1', 'pheald', 'Account is in good standing.', TO_TIMESTAMP('27-MAR-12', 'DD-MON-RR HH.MI.SSXFF AM'), 'In Good Standing', '1');
INSERT INTO KSA."KSSA_FLAG_TYPE" (ID, CREATOR_ID, DESCRIPTION, LAST_UPDATE, NAME, ACCESS_LEVEL) VALUES ('2', 'pheald', 'Account is currently in good standing but has had a history of problems.', TO_TIMESTAMP('27-MAR-12', 'DD-MON-RR HH.MI.SSXFF AM'), 'Currently in Good Standing', '2');
INSERT INTO KSA."KSSA_FLAG_TYPE" (ID, CREATOR_ID, DESCRIPTION, LAST_UPDATE, NAME, ACCESS_LEVEL) VALUES ('3', 'pheald', 'Account is in collections', TO_TIMESTAMP('27-MAR-12', 'DD-MON-RR HH.MI.SSXFF AM'), 'In Collections', '2');
