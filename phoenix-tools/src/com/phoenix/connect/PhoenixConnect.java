package com.phoenix.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.codec.binary.Hex;

public class PhoenixConnect {
	public static void main(String[] strings) throws Exception {
		
		Hex myhex = new Hex();
		byte[] mybyte = myhex.decodeHex("".toCharArray());
		
		Statement stmt = null;
		ResultSet rset = null;
		System.out.println("Before getting connection");
		Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
		Connection con = DriverManager.getConnection("jdbc:phoenix:localhost:2181:/hbase-unsecure");
		System.out.println("Got connection");
		stmt = con.createStatement();
		
		stmt.executeUpdate("create table test (mykey integer not null primary key, mycolumn varchar)");
		stmt.executeUpdate("upsert into test values (1,'Hello')");
		stmt.executeUpdate("upsert into test values (2,'World!')");
		con.commit();
		System.out.println("After commit");
		PreparedStatement statement = con.prepareStatement("select * from test");
		rset = statement.executeQuery();
		System.out.println("After query");
		while (rset.next()) {
			System.out.println(rset.getString("mycolumn"));
		}
		statement.close();
		con.close();
	}
}
