#!/usr/bin/perl

use Number::Bytes::Human qw(format_bytes);
use Data::Dumper;
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
        $count = 0;
		open (my $logfile, '<:encoding(UTF-8)', $log);

		while (my $line = <$logfile>) {
		        if($directory) {
				        if(!($line=~/$directory/)) {
								next;
						}
				}
				my @values = split(/\s+/,$line);
				my $size = $values[4];

                $count += $size;

		}

		print format_bytes($count);
		print "\n";
		print $count;
		print "\n";
}
