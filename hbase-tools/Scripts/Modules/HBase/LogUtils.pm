package HBase::LogUtils;

use warnings;
use feature qw(say);

use Exporter qw(import);
use File::Find;

our @EXPORT_OK = qw(getMasterLogs getRSLogs);

sub getMasterLogs {
  $directory = shift;
  @filenames = getAllFiles($directory);
  getMatching(\@filenames,"Starting master");
  
}

sub getRSLogs {
  $directory = shift;
  @filenames = getAllFiles($directory);
  getMatching(\@filenames,"Starting regionserver");

}

sub getMatching{
  my ($files, $string )= @_;
  # @files  = @{%_[1]};

  my @fileReturn;
  
  foreach $filename (@$files) {
    open (my $fh, '<:encoding(UTF-8)', $filename) 
	  or die "Unable to open file '$filename' $!";
	while (my $line = <$fh>) {
	  chomp($line);
	  if ($line =~ /$string/) {
        push @fileReturn, $filename;
		last;
	  }
	}
  }
  return @fileReturn;
}

sub getAllFiles {
  my $directory = shift;

  my @files;
  find( sub { push @files, $File::Find::name unless -d; }, $directory);
  return @files;
}

1;
