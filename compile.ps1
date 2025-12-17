javac -cp "lib/*" `
$(Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName })