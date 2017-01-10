#!/usr/bin/perl
use warnings;
use feature qw(say);

use File::Basename qw(dirname);
use Cwd qw(abs_path);

use lib dirname(dirname abs_path $0) . '/Modules';

use HBase::LogUtils qw(processRSLogs processMasterLogs getMasterLogs getRSLogs);

my $filename = $ARGV[0];
if (!$filename) {
  say "No filename specified";
  say "Usage: ";
  say "       " . $0 . " <Directory> [Tablename]";
  exit 1;
}
processMasterLogs($filename);
say "";
processRSLogs($filename);

