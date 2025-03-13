#!/bin/bash

buildOutputDirectory="BuildOutput"
package=BuildOutput.zip

rm $buildOutputDirectory -R -f
rm $package -f

dotnet publish "src" -o $buildOutputDirectory --self-contained

zip -r $package $buildOutputDirectory