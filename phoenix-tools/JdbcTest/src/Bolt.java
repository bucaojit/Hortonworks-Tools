//package com.thalesgroup.us.insyte.hadoop.storm.seatsession.Bolts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
/*
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.thalesgroup.us.insyte.hadoop.storm.common.PropertyUtils;
*/

public class Bolt {
/*
	public static final Logger LOG = LoggerFactory.getLogger(AccessAttemptFailures.class);
	Properties stormProperties = PropertyUtils.loadProperties(AccessAttemptFailures.class);
	private OutputCollector collector = null;
	Configuration conf;
	Configuration config;*/
	
	List<String> fields;
	Boolean getList;
	private Connection conn;
	private static LoadingCache<String, Long> cache;
	long totalCount ;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		conf = Configuration.defaultConfiguration();
		config = conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
		try {
			conn = getConnection();
			conn.setAutoCommit(true);
			checkTableSchema(conn);
		} catch (SQLException e) {
LOG.info("ERROR: "+e);
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(stormProperties.getProperty("phoenix.jdbc.url"));
	}
	
	private void checkTableSchema(Connection conn) throws SQLException {
		Statement statement = conn.createStatement();
		statement.execute("create table if not exists Insyte.AggregateStg (ID VARCHAR not null primary key,count BIGINT)");
	}

	@Override
	public void execute(Tuple tuple) {

		String msgData = tuple.getStringByField("msgData");
		String flightid = tuple.getStringByField("flightid");

		try{

			List<Object> vals = new ArrayList<Object>();
			vals.add(flightid);
			String seat_id = JsonPath.using(config).parse(msgData).read("$.seat_id");
			vals.add(seat_id);
			String id = flightid.concat("AccessAttemptFailures");
			vals.add(id);
			if(tuple.getStringByField("msgType").equals("InstantaneousEvent_json")){
				totalCount =  cache.get(id);
				if(JsonPath.using(config).parse(msgData).read("$.event.event_type").toString().equals("ACCESS_MEDIA")){
					if(JsonPath.using(config).parse(msgData).read("$.event.access_media.success").toString().equals("false")){
						totalCount=totalCount+1;
						cache.put(id,totalCount);

					}
				}

				vals.add(totalCount);
				collector.emit(vals);
			}else if(tuple.getStringByField("msgType").equals("TransitionEvent_json")){
				totalCount =  cache.get(id);
				if(JsonPath.using(config).parse(msgData).read("$.begin.event_type").toString().equals("ACCESS_MEDIA")){

					if(JsonPath.using(config).parse(msgData).read("$.begin.access_media.success").toString().equals("false")){
						totalCount=totalCount+1;
						cache.put(id,totalCount);
						vals.add(totalCount);
						collector.emit(vals);
					}
				}else if(JsonPath.using(config).parse(msgData).read("$.end.event_type").toString().equals("ACCESS_MEDIA")){

					if(JsonPath.using(config).parse(msgData).read("$.end.access_media.success").toString().equals("false")){
						totalCount=totalCount+1;
						cache.put(id,totalCount);
						vals.add(totalCount);
						collector.emit(vals);
					}
				}
			}
			putCount(id,totalCount);
			collector.ack(tuple);
		}catch(Exception e){
			LOG.error(e.getMessage());
			collector.fail(tuple);
		}
	}

	private void putCount(String id, long count) throws SQLException {
		if (conn == null || conn.isClosed()) {
			conn = getConnection();
		} 
		PreparedStatement updStmt = conn.prepareStatement("UPSERT into Insyte.AggregateStg (ID,count) Values(?,?)");
		updStmt.setString(1, id);
		updStmt.setLong(2, count);
		updStmt.executeUpdate();
		conn.commit();
	}

	private long getCount(String id) throws SQLException {
		if (conn == null || conn.isClosed()) {
			conn = getConnection();
		} 
		PreparedStatement getStmt = conn.prepareStatement("select count from Insyte.AggregateStg where id = ?");
		getStmt.setString(1, id);
		ResultSet rset = getStmt.executeQuery();
		long count = 0;
		if (rset.next()) {
			count = rset.getLong(1);
		}
		return count;
	}


	public void buildCache() {
		cache = CacheBuilder.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(24, TimeUnit.HOURS)
				.build(
						new CacheLoader<String, Long>() {
							@Override
							public Long load(String id) throws Exception {
								return getCount(id);
							}
						});
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

		declarer.declare(new Fields(Arrays.asList(stormProperties.getProperty("AccessAttemptFailures.fields").split(","))));

	}
}