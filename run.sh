#!/bin/bash

ant build
BUILT=$?

if [ $BUILT -ne 0 ]; then
	echo -e "\nBuild failed...\n"
	exit 1
fi

echo -e "\n\nSucceed - starting environment\n"

## cd ../SampleExperimentRLVizApp-Java-R1207
## bash run.bash

bash ../SampleExperimentRLVizApp-Java-R1207/run.bash
