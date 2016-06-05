#!/bin/bash


octave gen_plot.m

octave_err=$?
if [ $octave_err -ne 0 ]; then
	echo "octave error: $octave_err"
	exit 1
fi

#gwenview image.jpg
