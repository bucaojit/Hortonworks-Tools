package HBase::LogUtils;

use warnings;
use feature qw(say);

use Exporter qw(import);
use File::Find;
use GetOpt::Long;
use English qw' -no_match_vars ';
use Array::Unique;

our @EXPORT_OK = qw(processRSLogs processMasterLogs getMasterLogs getRSLogs);

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
  @returnfilenames = ();

  @searchstr = ();
  push @searchstr, "Starting master";
  push @searchstr, "MasterProcWAL";

  getMatching(\@filenames,\@searchstr);
  
}

sub getRSLogs {
  $directory = shift;
  @filenames = getAllFiles($directory);
  @returnfilenames = ();

  @searchstr = ();
  push @searchstr, "Starting regionserver";
  push @searchstr, "HRegion: Started memstore flush";

  getMatching(\@filenames,\@searchstr);
}

sub getMatching{
  my ($files, $strings )= @_;

  my @fileReturn;

  tie @fileReturn, 'Array::Unique';
  
  foreach $filename (@$files) {
    open (my $fh, '<:encoding(UTF-8)', $filename) 
	  or die "Unable to open file '$filename' $!";
  	while (my $line = <$fh>) {
  	  chomp($line);
      foreach $searchstr (@$strings) {
    	  if ($line =~ /$searchstr/) {
            push @fileReturn, $filename;
    		    last;
    	  }
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

sub processRSLogs {
  $directory = shift;
  say "Processing RegionServer logs in $directory";
  @files = getRSLogs($directory);
  foreach my $filename (@files) {
    say "RS Log File: $filename";
    processRSLogSingle($filename);
  }
}

sub processRSLogSingle {
  $filename = shift;
  getStarting($filename, 'regionserver');
  checkJvmPause($filename);
}

sub processMasterLogs {
  $directory = shift;
  say "Processing Master logs in $directory";
  @files = getMasterLogs($directory);
  foreach my $filename (@files) {
    say "Master Log File: $filename";
    processMasterLogSingle($filename);
  }
}

sub processMasterLogSingle {
  $filename = shift;
  getStarting($filename, 'master');

}

sub getStarting {
  my ($filename, $type) = @_;
  open (my $fh, '<:encoding(UTF-8)', $filename) 
    or die "Unable to open file '$filename' $!";
  while (my $line = <$fh>) {
    chomp($line);
    if ($line =~ /Starting $type/) {
	  print "$line\n";
    }
  }

}

sub getConfigVals {

}

sub getJvmPause {

}

sub checkJvmPause {
  my ($filename) = @_;
  open (my $fh, '<:encoding(UTF-8)', $filename) 
    or die "Unable to open file '$filename' $!";
  while (my $line = <$fh>) {
    chomp($line);
    if ($line =~ /JvmPause/) {
      my $pausetime = 0;
      @values = split(/\s+/,$line);
      for my $value(@values) {
        next unless $value =~ /ms/;
        chomp $value;
        
        $pausetime = $value;
        $pausetime =~ s/ms//g;
      }
      if($pausetime > 10000) {
        print $line . "\n";
      }
    }
  }

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
