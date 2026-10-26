$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

Write-Host "Iniciando Medina Fragrances con base de datos local embebida..."
Write-Host "URL: http://localhost:8080"
Write-Host "Consola DB: http://localhost:8080/h2-console"
Write-Host ""

$env:SPRING_PROFILES_ACTIVE = "local"
& .\mvnw.cmd spring-boot:run
