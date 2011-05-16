#!/bin/sh

CPATH="plex-viewer.jar:../lib/ext/gluegen-rt.jar:../lib/ext/jogl.jar"
LPATH="../lib/ext/x86/"

java -cp $CPATH \
  -Djava.library.path=$LPATH \
  edu.stanford.math.plex_viewer.PlexViewer \
  $@
