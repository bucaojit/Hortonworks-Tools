#!/usr/bin/perl
use Data::Dumper;

# Based off script to process hdfs-audit.log
# https://github.com/hortonworks/support-tooling
#
# Processes Ranger Audit log
# Determines number of accesses/NameNode RPC per second

$log=$ARGV[0];
$MINIMUM_OPS=1;
if(!$log)
{
  	print "Please provide a log file\n";
}
else
{
	@arr= `cut -d " " -f 2 $log | cut -d "," -f 1 | cut -d "." -f 1 | uniq -c`;
	chomp(@arr);

	foreach $rec(@arr)
	{
    	$rec =~ s/^\s+//;
    	@arr1=split / /,$rec;
    	chomp(@arr1[0]);
    	chomp(@arr1[1]);
    	if(@arr1[0] >$MINIMUM_OPS && @arr1[1])
    	{
        	$var="@arr1[0] => @arr1[1]";
        	print "$var\n";
    	}
	}
}
