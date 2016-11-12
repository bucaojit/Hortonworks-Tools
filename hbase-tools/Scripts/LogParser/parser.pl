#!/usr/bin/perl
use warnings;
use feature qw(say);

use File::Basename qw(dirname);
use Cwd qw(abs_path);

use lib dirname(dirname abs_path $0) . '/Modules';

use HBase::LogUtils qw(getMasterLogs);

@file1 = getMasterLogs(".");

foreach my $filename (@file1) {
  say ($filename);
}
