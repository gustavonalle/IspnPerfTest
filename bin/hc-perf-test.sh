#!/bin/bash

conf_dir=`dirname $0`/../conf

`dirname $0`/perf-test.sh -cfg $conf_dir/hazelcast.xml -factory hc $*
