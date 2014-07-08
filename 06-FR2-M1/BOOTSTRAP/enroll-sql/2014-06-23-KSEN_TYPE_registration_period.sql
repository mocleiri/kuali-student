--  KSENROLL-13225
INSERT INTO KSEN_TYPE (CREATEID, CREATETIME, DESCR_FORMATTED, DESCR_PLAIN, EFF_DT, NAME, OBJ_ID, REF_OBJECT_URI, SERVICE_URI, TYPE_KEY, VER_NBR)
  VALUES ('SYSTEMLOADER', TO_DATE( '20120912000000', 'YYYYMMDDHH24MISS' ), 'Registration Open', 'Registration Open', TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ), 'Registration Open', SYS_GUID(), 'http://student.kuali.org/wsdl/atp/MilestoneInfo', 'http://student.kuali.org/wsdl/atp/AtpService', 'kuali.atp.milestone.regstrationservicesopen', 0)
/
INSERT INTO KSEN_TYPE (CREATEID, CREATETIME, DESCR_FORMATTED, DESCR_PLAIN, EFF_DT, NAME, OBJ_ID, REF_OBJECT_URI, SERVICE_URI, TYPE_KEY, VER_NBR)
  VALUES ('SYSTEMLOADER', TO_DATE( '20120912000000', 'YYYYMMDDHH24MISS' ), 'Pre-Registration Period', 'Pre-Registration Period', TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ), 'Pre-Registration Period', SYS_GUID(), 'http://student.kuali.org/wsdl/atp/MilestoneInfo', 'http://student.kuali.org/wsdl/atp/AtpService', 'kuali.atp.milestone.preregistrationperiod', 0)
/
INSERT INTO KSEN_TYPE (CREATEID, CREATETIME, DESCR_FORMATTED, DESCR_PLAIN, EFF_DT, NAME, OBJ_ID, REF_OBJECT_URI, SERVICE_URI, TYPE_KEY, VER_NBR)
  VALUES ('SYSTEMLOADER', TO_DATE( '20120912000000', 'YYYYMMDDHH24MISS' ), 'Schedule Adjustment Period', 'Schedule Adjustment Period', TO_DATE( '20000101000000', 'YYYYMMDDHH24MISS' ), 'Schedule Adjustment Period', SYS_GUID(), 'http://student.kuali.org/wsdl/atp/MilestoneInfo', 'http://student.kuali.org/wsdl/atp/AtpService', 'kuali.atp.milestone.scheduleadjustmentperiod', 0)
/
INSERT INTO KSEN_TYPETYPE_RELTN (CREATEID,CREATETIME,ID,OWNER_TYPE_ID,RANK,RELATED_TYPE_ID,TYPETYPE_RELTN_STATE,TYPETYPE_RELTN_TYPE,VER_NBR)
  VALUES ('SYSTEMLOADER',TO_DATE( '20120301000000', 'YYYYMMDDHH24MISS' ),'kuali.type.type.relation.type.group.kuali.milestone.type.group.registration.kuali.atp.milestone.regstrationservicesopen','kuali.milestone.type.group.registration',0,'kuali.atp.milestone.regstrationservicesopen','kuali.type.type.relation.state.active','kuali.type.type.relation.type.group',0)
/
INSERT INTO KSEN_TYPETYPE_RELTN (CREATEID,CREATETIME,ID,OWNER_TYPE_ID,RANK,RELATED_TYPE_ID,TYPETYPE_RELTN_STATE,TYPETYPE_RELTN_TYPE,VER_NBR)
  VALUES ('SYSTEMLOADER',TO_DATE( '20120301000000', 'YYYYMMDDHH24MISS' ),'kuali.type.type.relation.type.group.kuali.milestone.type.group.registration.kuali.atp.milestone.preregistrationperiod','kuali.milestone.type.group.registration',0,'kuali.atp.milestone.preregistrationperiod','kuali.type.type.relation.state.active','kuali.type.type.relation.type.group',0)
/
INSERT INTO KSEN_TYPETYPE_RELTN (CREATEID,CREATETIME,ID,OWNER_TYPE_ID,RANK,RELATED_TYPE_ID,TYPETYPE_RELTN_STATE,TYPETYPE_RELTN_TYPE,VER_NBR)
  VALUES ('SYSTEMLOADER',TO_DATE( '20120301000000', 'YYYYMMDDHH24MISS' ),'kuali.type.type.relation.type.group.kuali.milestone.type.group.registration.kuali.atp.milestone.scheduleadjustmentperiod','kuali.milestone.type.group.registration',0,'kuali.atp.milestone.scheduleadjustmentperiod','kuali.type.type.relation.state.active','kuali.type.type.relation.type.group',0)
/
