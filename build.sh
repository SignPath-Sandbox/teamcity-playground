#!/bin/bash

buildOutputDirectory="BuildOutput"

rm $buildOutputDirectory -R -f

dotnet publish "HelloWorld" -o $buildOutputDirectory --self-contained