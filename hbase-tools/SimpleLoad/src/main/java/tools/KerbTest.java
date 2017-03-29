package main.java.tools;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

public class KerbTest 
{

	public static void main(String[] args) {
	    if(args.length != 2 || args.length != 4) {
                System.out.println("Usage: KerbTest <PrincipalName> <KeyTab>");
                return;
            }
            Integer start = 0;
            Integer end   = 100000;
            String principal, keytab;
            if(args.length == 2) {
                principal = args[0];
                keytab = args[1];
            }
            else if(args.length == 4) {
                principal = args[0];
                keytab = args[1];
                start = Integer.parseInt(args[3]);
                end   = Integer.parseInt(args[4]);
            }
              
            principal = args[0];
            keytab = args[1];

	    Table table = null;
	    Configuration conf = HBaseConfiguration.create();
	    conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"));
	   
	    conf.set("hadoop.security.authentication", "Kerberos");
	    
	    UserGroupInformation.setConfiguration(conf);
	    
	    
	    
	    Connection connection = null;
	    Admin admin = null;
	    TableName tablename = null;
	    try {
	      UserGroupInformation.loginUserFromKeytab(principal, keytab);
	      // hdfs.headless.keytab
	      connection = ConnectionFactory.createConnection(conf);
	      admin = connection.getAdmin();
	      tablename = TableName.valueOf("table_example");

	      if(!admin.isTableAvailable(tablename)) {
	        HTableDescriptor desc = new HTableDescriptor(tablename);
	        desc.addFamily(new HColumnDescriptor("cf1"));
	        admin.createTable(desc);
	        
	      }
	      table = connection.getTable(tablename);
	      
	      
	    } catch(Exception e) {
	      e.printStackTrace();
	    }
	    
	    Put put = new Put(Bytes.toBytes("Value to enter"));
	    put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("newQualifier"), Bytes.toBytes("NewValue"));
	    try {
	      table.put(put);    
	    } catch(Exception e) {
	      e.printStackTrace();
	    }
	    // Insert rows for RowCounter
	    for(int i = start; i < end; i++) {
	    	String strVal = "'" + i + "'";
	    	Put insert = new Put(Bytes.toBytes(strVal));
	    	try {
	    		insert.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("newQualifier"), Bytes.toBytes("NewValue"));
	    		table.put(insert);
	    	} catch (Exception e) {}
	    }
	  }
}
