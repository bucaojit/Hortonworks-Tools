CREATE TABLE IF NOT EXISTS test ("iso_8601" DATE NOT NULL PRIMARY KEY);

upsert into test values(TO_DATE('2016-04-01 22:45:00'));

select  CONVERT_TZ("iso_8601",'UTC','America/New_York') from test;
