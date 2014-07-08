insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('9eb34dbe-ed4c-4698-9539-2bd39a97e42e', '8539e453-d979-471e-b00b-a26eb8706507', 'courseOfferingCode', 'CHEM399B-P', '08a53a94-2a3c-4a4a-b578-f87529956e84')
/
insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('5a938ee2-7b1f-4487-9ad8-c0dbe24d56c8', 'A', 'courseOfferingCode', 'CHEM399B', 'f2b22ac8-c5d4-48d8-963d-2d2da336b036')
/
delete from KSEN_CODE_GENERATOR_LOCKS where CODE='8539e453-d979-471e-b00b-a26eb8706507' and UNIQUE_KEY='CHEM399B-P' and NAMESPACE='courseOfferingCode'
/
delete from KSEN_CODE_GENERATOR_LOCKS where UNIQUE_KEY='CHEM399B' and NAMESPACE='courseOfferingCode' and 0=(select count(*) from KSEN_CODE_GENERATOR_LOCKS codegenera1_ where codegenera1_.UNIQUE_KEY='CHEM399B-P' and codegenera1_.NAMESPACE='courseOfferingCode')
/
insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('914ab501-0cd9-4674-851b-9ed4d622cfeb', '7d8d1cdb-f1d9-4758-a136-5fb75d93ac08', 'courseOfferingCode', 'HIST329N-P', '67d184ae-1c9b-4305-9ba3-2df8e3c78373')
/
insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('4d74364a-41b4-4a01-ba60-e72df3184c79', 'A', 'courseOfferingCode', 'HIST329N', 'cf4a2ea3-9738-4a7e-8c4a-482759c0ce45')
/
delete from KSEN_CODE_GENERATOR_LOCKS where CODE='7d8d1cdb-f1d9-4758-a136-5fb75d93ac08' and UNIQUE_KEY='HIST329N-P' and NAMESPACE='courseOfferingCode'
/
delete from KSEN_CODE_GENERATOR_LOCKS where UNIQUE_KEY='HIST329N' and NAMESPACE='courseOfferingCode' and 0=(select count(*) from KSEN_CODE_GENERATOR_LOCKS codegenera1_ where codegenera1_.UNIQUE_KEY='HIST329N-P' and codegenera1_.NAMESPACE='courseOfferingCode')
/
insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('9482d50c-5d19-4399-a405-2aa608a9defd', '356dae52-9505-4302-a7a2-2a0471306672', 'courseOfferingCode', 'HIST319D-P', '4efc1f3e-2c58-47be-b0ed-6fbbbb721fd2')
/
insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('1ebb3bf0-6562-4b8e-aaa2-5d96ae648dcd', 'A', 'courseOfferingCode', 'HIST319D', '788bec62-3aef-4221-b6e7-6d011316bd8b')
/
delete from KSEN_CODE_GENERATOR_LOCKS where CODE='356dae52-9505-4302-a7a2-2a0471306672' and UNIQUE_KEY='HIST319D-P' and NAMESPACE='courseOfferingCode'
/
delete from KSEN_CODE_GENERATOR_LOCKS where UNIQUE_KEY='HIST319D' and NAMESPACE='courseOfferingCode' and 0=(select count(*) from KSEN_CODE_GENERATOR_LOCKS codegenera1_ where codegenera1_.UNIQUE_KEY='HIST319D-P' and codegenera1_.NAMESPACE='courseOfferingCode')
/
insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('b800a651-56eb-4359-bbd5-7147ef95f2d7', '17ba22bb-527f-475b-87fc-acd07ffcc4c4', 'courseOfferingCode', 'HIST429G-P', '4b70f3b1-d58e-4015-8429-71857ad48f9f')
/
insert into KSEN_CODE_GENERATOR_LOCKS (OBJ_ID, CODE, NAMESPACE, UNIQUE_KEY, ID) values ('1c9ec0b6-592b-4af0-bc4b-1669bb69675d', 'A', 'courseOfferingCode', 'HIST429G', 'b2d8fd9c-4462-42ea-a1a3-8c6e816fd419')
/
delete from KSEN_CODE_GENERATOR_LOCKS where CODE='17ba22bb-527f-475b-87fc-acd07ffcc4c4' and UNIQUE_KEY='HIST429G-P' and NAMESPACE='courseOfferingCode'
/
delete from KSEN_CODE_GENERATOR_LOCKS where UNIQUE_KEY='HIST429G' and NAMESPACE='courseOfferingCode' and 0=(select count(*) from KSEN_CODE_GENERATOR_LOCKS codegenera1_ where codegenera1_.UNIQUE_KEY='HIST429G-P' and codegenera1_.NAMESPACE='courseOfferingCode')
/
