#!/usr/bin/perl

use Number::Bytes::Human qw(format_bytes);
use Data::Dumper;
use feature qw(say);
$log=$ARGV[0];
$directory=$ARGV[1];
if(!$log)
{
		print "Usage: \n";
		print "        " . $0 . " <logfile> [directoryname] \n";
		print "[directoryname] is optional, if not specified counts total\n";
}
else
{
        my %dir_map;
        $count = 0;
		open (my $logfile, '<:encoding(UTF-8)', $log);

		while (my $line = <$logfile>) {
				if(!($line=~/\/hbase\/archive/)) {
				        next;
				}
		        if($directory) {
				        if(!($line=~/$directory/)) {
								next;
						}
				}
				my @values = split(/\s+/,$line);
				my $size = $values[4];
				my $directory = $values[7];
				my @dirsplit  = split('/',$directory);
				my $name = $dirsplit[5];
				my $count_dir = 0;
				if (exists $dir_map{$name}) {
						$count_dir = $dir_map{$name}	
				}
				$count_dir += $size;

                $dir_map{$name} = $count_dir;
		}
		foreach my $dirname (sort {$dir_map{$a} <=> $dir_map{$b} } keys %dir_map) {
				printf "%-8s %s\n", format_bytes($dir_map{$dirname}), $dirname ;
		}
}
