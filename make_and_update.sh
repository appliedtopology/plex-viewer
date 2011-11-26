#!//bin/sh

echo "Getting newest javaPlex library ..."
echo "-----------------------------------"
cd ./scripts/
./update_plex_viewer_lib.sh


echo "Building ..."
echo "------------"
cd ..
ant

echo "Copying compiled jar to javaPlex Matlab folder ..."
echo "--------------------------------------------------"

cd ./scripts/
./update_matlab_pv_lib.sh

echo "Cleaning ..."
echo "------------"
cd ..
ant clean_all




