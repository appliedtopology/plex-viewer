#!/bin/sh

CPATH="plex-viewer.jar:../lib/ext/gluegen-rt.jar:../lib/ext/jogl.jar"

java -cp $CPATH \
  edu.stanford.math.plex_viewer.PovGenerator \
  $@
