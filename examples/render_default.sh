#!/bin/sh

pov_file=$1;
width="1200";
height="900";
quality="3";

echo "Rendering $pov_file ...";
`povray +I$pov_file +W$width +H$height +Q$quality +A`;
echo "Finished rendering $pov_file";

