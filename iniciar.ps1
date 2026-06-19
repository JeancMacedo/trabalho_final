$root = Split-Path -Parent $MyInvocation.MyCommand.Path

Start-Process powershell.exe -ArgumentList @(
  '-NoExit',
  '-Command',
  "Set-Location '$root\ms-user'; java -jar target\ms-user-0.0.1-SNAPSHOT.jar"
)

Start-Process powershell.exe -ArgumentList @(
  '-NoExit',
  '-Command',
  "Set-Location '$root\ms-email'; java -jar target\ms-email-0.0.1-SNAPSHOT.jar"
)

Start-Process powershell.exe -ArgumentList @(
  '-NoExit',
  '-Command',
  "Set-Location '$root\frontend'; npm start"
)

Write-Host 'Servicos iniciados em janelas separadas.'