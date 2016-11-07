package com.phoenix.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PhoenixQStest{
	public static void main(String args[]) throws Exception{
		Connection conn = getConnection();
		conn.setAutoCommit(true);
		checkTableSchema(conn);
	}
	
	private static Connection getConnection() throws Exception {
		Class.forName("org.apache.phoenix.queryserver.client.Driver");
		return DriverManager.getConnection("jdbc:phoenix:thin:url=http://172.26.76.190:8765");
	}
	
		private static void checkTableSchema(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		statement.execute("create table if not exists Tests.AggregateStg (ID VARCHAR not null primary key,count BIGINT)");
		
	}
}
