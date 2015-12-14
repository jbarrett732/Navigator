#!/usr/bin/perl
use strict;
use Getopt::Long; 

my $name;
my $disk_size;
my $os;
my $image;
my $memory_size;
my $vram_size;

GetOptions("n=s" => \$name,
           "d=s" => \$disk_size,
           "o=s" => \$os,
           "i=s" => \$image,
           "m=s" => \$memory_size,
           "r=s" => \$vram_size)
or die("Missing command line arguments\n");

print "success\n";

