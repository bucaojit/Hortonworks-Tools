package HBase::LogUtils;

use warnings;
use feature qw(say);

use Exporter qw(import);
use File::Find;

our @EXPORT_OK = qw(getMasterLogs getRSLogs);

sub getMasterLogs {
  $directory = shift;
  getAllFiles($directory);
}

sub getRSLogs {
  my $directory = shift;

}

sub getAllFiles {
  my $directory = shift;

  my @files;
  find( sub { push @files, $File::Find::name unless -d; }, $directory);
  @files;
}

1;
