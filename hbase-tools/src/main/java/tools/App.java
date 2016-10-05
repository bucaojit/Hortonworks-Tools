package main.java.tools;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class App 
{

	public static void main(String[] args) {
		
	    //HTable ht = null;
	    Table table = null;
	    Configuration conf = HBaseConfiguration.create();
	    conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"));
	    
	    HBaseAdmin hbadmin = null;
	    Connection connection = null;
	    Admin admin = null;
	    TableName tablename = null;
	    //HTableDescriptor htd = null;
	    try {
	      connection = ConnectionFactory.createConnection(conf);
	      admin = connection.getAdmin();
	      hbadmin = new HBaseAdmin(conf);
	      if(!hbadmin.isTableAvailable("table_example")) {
	         tablename = TableName.valueOf("table_example");
	  
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

	    Put put = new Put(Bytes.toBytes("Value to enter"));
	    put.add(Bytes.toBytes("cf1"), Bytes.toBytes("newQualifier"), Bytes.toBytes("NewValue"));
	    try {
	      table.put(put);    
	    } catch(Exception e) {
	      e.printStackTrace();
	    }
	  }
}
