#!/usr/bin/perl

use warnings;

use Term::ANSIColor;
use feature qw(say);

# Parser for hbase shell> status 'detailed' output

my $filename = $ARGV[0];
my $tablename;


my $status_detailed;

if (!$filename) {
  say "No filename specified";
  say "Usage: ";
  say "       " . $0 . " <Filename> [Tablename]";
  exit 1;
}

say "";
say "***************** Processing " . $filename . " *****************";
say "";

if ($ARGV[1]) {
  $tablename = $ARGV[1];
  say "Tablename: " . $tablename;
}
open ($status_detailed, $filename) or die $!;

# Keywords:
#  live servers, dead servers, 60020, requestsPerSecond, enter 'tablename'

my $printNext = 0;
while (my $line = <$status_detailed>) {
  if(($line =~ /regionsInTransition/) ||
      ($line =~ /live servers/) || 
      ($line =~ /dead servers/) || 
      ($line =~ /requestsPerSecond/)) {
	  
	  print $line;
   }
   if ($printNext) {
	 print color ('blue');
	 print $line;
	 print color ('reset');
	 $printNext = 0;
     
   }
   if($line =~ /60020/) {
     say "";
     print color ('yellow');
	 print $line;
	 print color ('reset');
   }
   if($tablename) {
     if($line =~ /$tablename/) {
	   print color ('blue');
	   print $line;
	   print color ('reset');
	   $printNext = 1;
	 }
   }
}
