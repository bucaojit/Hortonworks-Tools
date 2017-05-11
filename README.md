# Troubleshooting Tools
## HBase
### __Scripts__
#### SnapshotSizes.pl
- Purpose: To calculate the disk usage of each table snapshot
- Inputs:  Output from `hdfs dfs -ls -R /`
- Usage:   `./snapshot_sizes.pl hdfs_ls.out`
- Notes:   Sorts by size, prints disk usage in human readable format

#### getHBaseProperty
- Purpose: To obtain the value in the HBase configuration for given property
- Input:   Property Name 
- Output:  Property Value
- Usage:   `getHBaseProperty zookeeper.znode.parent`

### Java Applications
#### SimpleLoad
Java client application that creates HBase table 'table_example' with column family 'cf1' and loads in an integer row

## HDFS

## Java

## Phoenix

## Spark

