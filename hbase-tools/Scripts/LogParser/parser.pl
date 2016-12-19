#!/usr/bin/perl
use warnings;
use feature qw(say);

use File::Basename qw(dirname);
use Cwd qw(abs_path);

use lib dirname(dirname abs_path $0) . '/Modules';

use HBase::LogUtils qw(getMasterLogs getRSLogs);

my $filename = $ARGV[0];
if (!$filename) {
  say "No filename specified";
  say "Usage: ";
  say "       " . $0 . " <Filename> [Tablename]";
  exit 1;
}
@file1 = getMasterLogs($filename);

foreach my $filename (@file1) {
  say "Master logs:";
  say ($filename);
}

@file2 = getRSLogs($filename);

foreach my $filename (@file2) {
  say "RS logs:";
  say ($filename);
}
