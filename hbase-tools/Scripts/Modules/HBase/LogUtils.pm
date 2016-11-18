package HBase::LogUtils;

use warnings;
use feature qw(say);

use Exporter qw(import);
use File::Find;
use GetOpt::Long;
use English qw' -no_match_vars ';

our @EXPORT_OK = qw(getMasterLogs getRSLogs);

# Remember to utilize on mac: open 'http://www.yahoo.com'
sub isMac {
  if("darwin" eq $OSNAME) {
    return 1;
  }   
  else  {
    return 0;
  }   
}  

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

sub processRSLog {
  
}

sub processMasterLog {

}

sub getConfigVals {

}

sub getJvmPause {

}

sub checkJvmPause {
  # Configurable value default 20000ms
  # If Error, print out the configvals, namely GC_OPTS (vmInput)

}

sub getErrors {

}

sub getZKTimeouts {

}

sub getAborts {

}

sub getPriorToClosing {
  # Need to figure out how to obtain this.  
  #   - Maybe it's own run 
  #   - Save last 100 lines 
  #   - If you get 20 closings, then print the last 120 to 20 lines
  return;
}

1;
