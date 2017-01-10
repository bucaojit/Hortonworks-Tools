#!/bin/sh

if [ "$#" -gt 2 ] || [ "$#" -eq 0 ]; then
  prog=`basename $0`
  echo "Usage: "
  echo "        $prog <classname>"
  echo "        $prog <hdfs | hbases> <classname>"
  echo "Defaults to hdfs"
  exit 1
fi

if [ "$#" -eq 1 ]; then 
  hdfs classpath | tr ":" "\n" | grep $1
else
  $1 classpath | tr ":" "\n" | grep $2
fi

