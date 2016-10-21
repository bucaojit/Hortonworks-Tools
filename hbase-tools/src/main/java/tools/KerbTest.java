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
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

public class KerbTest 
{

	public static void main(String[] args) {
		
	    //HTable ht = null;
	    Table table = null;
	    Configuration conf = HBaseConfiguration.create();
	    conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"));
	   
	    conf.set("hadoop.security.authentication", "Kerberos");
	    
	    UserGroupInformation.setConfiguration(conf);
	    
	    
	    
	    Connection connection = null;
	    Admin admin = null;
	    TableName tablename = null;
	    //HTableDescriptor htd = null;
	    try {
	      //UserGroupInformation.loginUserFromKeytab("oliver@OLIVER.COM", "/path/to/example_user.keytab");
	      connection = ConnectionFactory.createConnection(conf);
	      admin = connection.getAdmin();
	      tablename = TableName.valueOf("table_example");

	      if(!admin.isTableAvailable(tablename)) {
	        HTableDescriptor desc = new HTableDescriptor(tablename);
	        desc.addFamily(new HColumnDescriptor("cf1"));
	        admin.createTable(desc);
	        //admin.createTable(desc);
	        
	      }
	      //ht = new HTable(conf, "table_example");
	      //htd = admin.getTableDescriptor(tablename);
	      table = connection.getTable(tablename);
	      
	      
	    } catch(Exception e) {
	      e.printStackTrace();
	    }

	    //Put put = new Put(Bytes.toBytes("Value to enter"));
	    Scan scan = new Scan();
	    
	    scan.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("newQualifier"));
	    ResultScanner scanner = null;
	    try {
	         scanner = table.getScanner(scan);
	    } catch(Exception e) {
	      e.printStackTrace();
	    }
	    for(Result result : scanner) {
	    	System.out.println(result.getRow().toString());
	    }
	    
	  }
}