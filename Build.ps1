$buildOutputDirectory = "BuildOutput"

Remove-Item $buildOutputDirectory -Recurse -Force -ErrorAction SilentlyContinue

dotnet publish "HelloWorld" -o $buildOutputDirectory --self-contained