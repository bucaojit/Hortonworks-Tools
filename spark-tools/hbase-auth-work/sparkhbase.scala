import org.apache.hadoop.hbase.client.{HBaseAdmin, Result}
import org.apache.hadoop.hbase.{ HBaseConfiguration, HTableDescriptor }
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.{HColumnDescriptor, HTableDescriptor, HBaseConfiguration}
import org.apache.hadoop.hbase.util.Bytes

import org.apache.spark._

object HBaseRead {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("HBaseRead").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val conf = HBaseConfiguration.create()
    val tableName = "table1"
    

    System.setProperty("user.name", "hdfs")
    System.setProperty("HADOOP_USER_NAME", "hdfs")
    conf.set("hbase.master", "172.26.75.233:16000")
    conf.setInt("timeout", 12000)
    conf.set("hbase.zookeeper.quorum", "localhost")
    conf.set("zookeeper.znode.parent", "/hbase-unsecure")
    conf.set(TableInputFormat.INPUT_TABLE, tableName)

    val admin = new HBaseAdmin(conf)
    while(true) {
    if (!admin.isTableAvailable(tableName)) {
      println("before create table")
      val tableDesc = new HTableDescriptor(tableName)
      val idsColumnFamilyDesc = new HColumnDescriptor(Bytes.toBytes("ids"))
      tableDesc.addFamily(idsColumnFamilyDesc)
      admin.createTable(tableDesc)
    }
    println("after create table")

    val hBaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    println("Number of Records found : " + hBaseRDD.count())
    Thread sleep 10000
    }
    sc.stop()
  }
}
