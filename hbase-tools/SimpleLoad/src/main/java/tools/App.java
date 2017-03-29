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
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class App 
{

	public static void main(String[] args) {
            String  tablenamestr = "table_example";
            Integer start = 0;
            Integer end   = 100000;
            if(args.length == 1) {
              end = Integer.parseInt(args[0]);
            }
            else if(args.length == 2) {
              start = Integer.parseInt(args[0]);
              end   = Integer.parseInt(args[1]);
            } 
            System.out.println("Loading into " + tablenamestr +
                               " numbers " + start + " to " +
                               end);
		
	    Table table = null;
	    Configuration conf = HBaseConfiguration.create();
	    conf.addResource(new Path("/etc/hbase/conf/hbase-site.xml"));
	    
	    Connection connection = null;
	    Admin admin = null;
	    TableName tablename = null;
	    try {
	      connection = ConnectionFactory.createConnection(conf);
	      admin = connection.getAdmin();
	      tablename = TableName.valueOf(tablenamestr);

	      if(!admin.isTableAvailable(tablename)) {
	        HTableDescriptor desc = new HTableDescriptor(tablename);
	        desc.addFamily(new HColumnDescriptor("cf1"));
	        admin.createTable(desc);
	      }
	      table = connection.getTable(tablename);
	      
	      
	    } catch(Exception e) {
	      e.printStackTrace();
	    }

	    // Insert rows for RowCounter
	    for(int i = start; i < end; i++) {
	    	Put insert = new Put(Bytes.toBytes(i));
	    	try {
	    		insert.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("newQualifier"), Bytes.toBytes("NewValue"));
	    		table.put(insert);
	    	} catch (Exception e) {}
	    }
	    
	  }
}
