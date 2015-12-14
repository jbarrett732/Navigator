#!/usr/bin/perl
use strict;
use Getopt::Long; 

my $name;
my $ip_address;

GetOptions("n=s" => \$name,
           "a=s" => \$ip_address)
or die("Missing command line arguments\n");

print "success\n";

