#!/usr/bin/perl
use warnings;
use feature qw(say);

use File::Basename qw(dirname);
use Cwd qw(abs_path);

use lib dirname(dirname abs_path $0) . '/Modules';

use HBase::LogUtils qw(getMasterLogs getRSLogs);

@file1 = getMasterLogs("/tmp/mytest1/");

foreach my $filename (@file1) {
  say "Master logs:";
  say ($filename);
}

@file2 = getRSLogs("/tmp/mytest1/");

foreach my $filename (@file2) {
  say "RS logs:";
  say ($filename);
}
