# Script PowerShell para reorganizar repositorios en múltiples datasources
# Ejecutar desde la raíz del proyecto: .\reorganize-repositories.ps1

# Colores para output
$Success = 'Green'
$Info = 'Cyan'
$Warning = 'Yellow'
$Error = 'Red'

Write-Host "`n========================================" -ForegroundColor $Info
Write-Host "Reorganización de Repositorios" -ForegroundColor $Info
Write-Host "========================================`n" -ForegroundColor $Info

# Rutas base
$baseRepoPath = "src\main\java\com\eljumillano\loop\repository"
$postgresPath = "$baseRepoPath\postgres"
$sqlServerPath = "$baseRepoPath\sqlserver"

# Crear directorios si no existen
Write-Host "Creando estructura de carpetas..." -ForegroundColor $Info

if (-not (Test-Path $postgresPath)) {
    New-Item -Path $postgresPath -ItemType Directory -Force | Out-Null
    Write-Host "✓ Creado: $postgresPath" -ForegroundColor $Success
} else {
    Write-Host "✓ Ya existe: $postgresPath" -ForegroundColor $Warning
}

if (-not (Test-Path $sqlServerPath)) {
    New-Item -Path $sqlServerPath -ItemType Directory -Force | Out-Null
    Write-Host "✓ Creado: $sqlServerPath" -ForegroundColor $Success
} else {
    Write-Host "✓ Ya existe: $sqlServerPath" -ForegroundColor $Warning
}

# Lista de repositorios para PostgreSQL (la mayoría)
$postgresRepositories = @(
    "UserRepository.java",
    "OrderRepository.java",
    "OrderItemRepository.java",
    "ProductRepository.java",
    "SucursalRepository.java",
    "ControlRepository.java",
    "ControlProductRepository.java",
    "PackingSlipRepository.java",
    "DisposableRepository.java"
)

# Lista de repositorios para SQL Server (ajustar según necesidad)
$sqlServerRepositories = @(
    # "ExternalDataRepository.java",
    # Agrega aquí los repositorios que deben usar SQL Server
)

Write-Host "`n----------------------------------------" -ForegroundColor $Info
Write-Host "Repositorios a mover a PostgreSQL:" -ForegroundColor $Info
Write-Host "----------------------------------------" -ForegroundColor $Info

foreach ($repo in $postgresRepositories) {
    $sourcePath = "$baseRepoPath\$repo"
    $destPath = "$postgresPath\$repo"
    
    if (Test-Path $sourcePath) {
        Write-Host "Moviendo: $repo" -ForegroundColor $Info
        
        # Leer el contenido del archivo
        $content = Get-Content $sourcePath -Raw
        
        # Actualizar el package
        $newContent = $content -replace "package com\.eljumillano\.loop\.repository;", "package com.eljumillano.loop.repository.postgres;"
        
        # Guardar en la nueva ubicación
        Set-Content -Path $destPath -Value $newContent -NoNewline
        
        # Eliminar archivo original
        Remove-Item $sourcePath -Force
        
        Write-Host "  ✓ Movido a: $destPath" -ForegroundColor $Success
    } else {
        Write-Host "  ⚠ No encontrado: $repo (puede que ya esté movido o no exista)" -ForegroundColor $Warning
    }
}

if ($sqlServerRepositories.Count -gt 0) {
    Write-Host "`n----------------------------------------" -ForegroundColor $Info
    Write-Host "Repositorios a mover a SQL Server:" -ForegroundColor $Info
    Write-Host "----------------------------------------" -ForegroundColor $Info
    
    foreach ($repo in $sqlServerRepositories) {
        $sourcePath = "$baseRepoPath\$repo"
        $destPath = "$sqlServerPath\$repo"
        
        if (Test-Path $sourcePath) {
            Write-Host "Moviendo: $repo" -ForegroundColor $Info
            
            # Leer el contenido del archivo
            $content = Get-Content $sourcePath -Raw
            
            # Actualizar el package
            $newContent = $content -replace "package com\.eljumillano\.loop\.repository;", "package com.eljumillano.loop.repository.sqlserver;"
            
            # Guardar en la nueva ubicación
            Set-Content -Path $destPath -Value $newContent -NoNewline
            
            # Eliminar archivo original
            Remove-Item $sourcePath -Force
            
            Write-Host "  ✓ Movido a: $destPath" -ForegroundColor $Success
        } else {
            Write-Host "  ⚠ No encontrado: $repo" -ForegroundColor $Warning
        }
    }
} else {
    Write-Host "`n⚠ No hay repositorios configurados para SQL Server" -ForegroundColor $Warning
    Write-Host "  Edita este script y agrega repositorios a la variable `$sqlServerRepositories" -ForegroundColor $Info
}

Write-Host "`n========================================" -ForegroundColor $Success
Write-Host "Reorganización completada!" -ForegroundColor $Success
Write-Host "========================================`n" -ForegroundColor $Success

Write-Host "Próximos pasos:" -ForegroundColor $Info
Write-Host "1. ✓ Verifica que los archivos se movieron correctamente" -ForegroundColor $Info
Write-Host "2. ✓ Actualiza los imports en tus servicios" -ForegroundColor $Info
Write-Host "3. ✓ Ejecuta la aplicación y verifica que no hay errores" -ForegroundColor $Info
Write-Host "4. ✓ Revisa MULTIPLE_DATASOURCES_GUIDE.md para más detalles`n" -ForegroundColor $Info
