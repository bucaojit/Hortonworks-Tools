#!/bin/sh

if [ "$#" -gt 2 ] || [ "$#" -eq 0 ]; then
  prog=`basename $0`
  echo "Usage: "
  echo "        $prog <classname>"
  echo "        $prog <directory> <classname>"
  exit 1
fi

if [ "$#" -eq 1 ]; then 
  find . -name '*.jar' -exec grep -Hls $1 {} \; 
else
  find $1 -name '*.jar' -exec grep -Hls $2 {} \; 
fi

