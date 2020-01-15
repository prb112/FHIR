###############################################################################
# (C) Copyright IBM Corp. 2020
# 
# SPDX-License-Identifier: Apache-2.0
###############################################################################

<# 
 This script will install the fhir server on the local machine and then
 start it up so that we can run server integration tests
 . - top level directory of the GitHub Actions workspace
 .\SIT - holds everything related to server integration tests
 .\SIT\fhir-server-dist - installer contents (after unzipping)
 .\SIT\wlp - fhir server installation 
#>

# Initial wait time after the "server start" command returns
$SERVER_WAITTIME=30

# Sleep interval after each "metadata" invocation
$SLEEP_INTERVAL=10

# Max number of "metadata" tries to detect server is running
$MAX_TRIES=10

# The default path to the workspace
# we have to account for the build directory
# Reference https://stackoverflow.com/a/34089841/1873438
Write-Host 'Preparing environment for fhir-server integration tests'
$DIR_WORKSPACE= Split-Path $PSScriptRoot -Parent

# Remove the entire SIT file tree If it exists
[string]$SIT=$DIR_WORKSPACE + '\SIT'
If ( Test-Path -Path $SIT ) {
    Write-Host 'Removing -> ' $SIT
    Remove-Item -Path $SIT -Recurse -Force
}

# Create the directory
New-Item -Path $DIR_WORKSPACE -Name 'SIT' -ItemType 'directory'
[string]$SIT = Resolve-Path $SIT

# Install a fresh copy of the fhir server
Write-Host 'Unzipping fhir-server installer...'
[string]$ZIP_FILE=$DIR_WORKSPACE + '\fhir-install\target\fhir-server-distribution.zip'
Expand-Archive -LiteralPath $ZIP_FILE -DestinationPath $SIT

# Setup and install the FHIR Server
Write-Host 'Installing fhir server in Integration Tests'
Write-Host ' - Directory - ' $SIT
& $SIT\fhir-server-dist\install.bat $SIT

# If the Config Exists, let's wipe it outfind 
Write-Host 'Copying configuration to install location'
$RM_ITEM=[string]$SIT + '\wlp\usr\servers\fhir-server\config'
If ( Test-Path $RM_ITEM ) {
    Write-Host 'Removing -> ' $RM_ITEM
    Remove-Item -path $RM_ITEM -Recurse -Force
}

$SERVER_ROOT=[string]$SIT + '\wlp\usr\servers\fhir-server\'
New-Item -Path $SERVER_ROOT -Name 'config' -ItemType 'directory'

# Copy over the Files for default, tenant1, tenant2
$DR_ITEM=[string]$DIR_WORKSPACE + '\fhir-server\liberty-config\config\*'
$DR_ITEM_DST=[string]$DIR_WORKSPACE + '\SIT\wlp\usr\servers\fhir-server\config\'
Copy-Item $DR_ITEM -Destination $DR_ITEM_DST -Recurse

$DR_ITEM1=[string]$DIR_WORKSPACE + '\fhir-server\liberty-config-tenants\config\*'
Copy-Item $DR_ITEM1 -Destination $DR_ITEM_DST -Recurse

Write-Host 'Copying test artifacts to install location'
$CP_ITEM=[string]$DIR_WORKSPACE + '\fhir-operation\target\fhir-operation-*-tests.jar'
$USERLIB_DST=[string]$DIR_WORKSPACE + '\SIT\wlp\usr\servers\fhir-server\userlib'
$USERLIB_DIR=[string]$DIR_WORKSPACE + '\SIT\wlp\usr\servers\fhir-server'

If (!(Test-Path -Path $USERLIB_DIR) ) {
    New-Item -Path $USERLIB_DIR -Name 'userlib' -ItemType 'directory'
}
Copy-Item $CP_ITEM -Destination $USERLIB_DST

# Start up the fhir server
$DATE_PS=[System.TimeZoneInfo]::ConvertTimeBySystemTimeZoneId((Get-Date), 'Greenwich Standard Time').ToString('t')
Write-Host '>>> Current time: ' $DATE_PS

Write-Host 'Starting fhir server'
$PROC=$SIT + '\wlp\bin\server'
& $PROC start fhir-server 

# Get the Updated Start Time
$DATE_PS=[System.TimeZoneInfo]::ConvertTimeBySystemTimeZoneId((Get-Date), 'Greenwich Standard Time').ToString('t')
Write-Host '>>> Current time: ' $DATE_PS

# Sleep for a bit to let the server startup
Write-Host 'Sleeping ' $SERVER_WAITTIME ' to let the server start'
Start-Sleep -s $SERVER_WAITTIME

# Ignore Self Signed Certificates and use TLS
# Reference https://stackoverflow.com/a/46254549/1873438
$AllProtocols = [System.Net.SecurityProtocolType]'Ssl3,Tls,Tls11,Tls12'
[System.Net.ServicePointManager]::SecurityProtocol = $AllProtocols
Add-Type -TypeDefinition @"
    using System.Net;
    using System.Security.Cryptography.X509Certificates;
    public class TrustAllCertsPolicy : ICertificatePolicy {
        public bool CheckValidationResult(
            ServicePoint srvPoint, X509Certificate certificate,
            WebRequest request, int certificateProblem) {
            return true;
        }
    }
"@
$AllProtocols = [System.Net.SecurityProtocolType]'Ssl3,Tls,Tls11,Tls12'
[System.Net.ServicePointManager]::SecurityProtocol = $AllProtocols
[System.Net.ServicePointManager]::CertificatePolicy = New-Object TrustAllCertsPolicy

# Next, we'll invoke the metadata API to detect when the
# server is ready to accept requests.
Write-Host 'Waiting for fhir-server to complete initialization'
$metadata_url='https://localhost:9443/fhir-server/api/v4/metadata'
$tries=1
$STATUS=0

while ( ($STATUS -ne 200) -and ($tries -le $MAX_TRIES) ) {
    Write-Host 'Executing[' $tries ']: server check'

    # Execute the connection to the HTTP Endpoint 
    $UserPassword = 'fhiruser:change-password'
    $bytes = [System.Text.Encoding]::ASCII.GetBytes($UserPassword)
    $base64 = [System.Convert]::ToBase64String($bytes)
    $req = Invoke-WebRequest -Method Get -Uri $metadata_url -Headers @{"Authorization" = "Basic $base64"; "Content-Type" = "application/json"} -UseBasicParsing    

    # Check the Status Code that comes back
    $STATUS = $req.StatusCode
    Write-Host 'Status code: ' $status

    # Wait If the Status is NE 200
    If ( $STATUS -ne 200 ) {
       Write-Host 'Sleeping for ' $SLEEP_INTERVAL ' secs'
       Start-Sleep -s $SLEEP_INTERVAL
    }
    $tries++
}

# Gather server logs in case there was a problem starting up the server
Write-Host 'Collecting pre-test server logs'
$pre_it_logs=[string]$SIT + '\pre-it-logs\'
$zip_file= [string]$DIR_WORKSPACE + '\pre-it-logs.zip'

If (Test-Path -Path $pre_it_logs) {
    Remove-Item -path $pre_it_log -recurse -force
    Remove-Item -path $zip_file -force
}

New-Item -Path $SIT -Name 'pre-it-logs' -ItemType 'directory'

$LOG_DIR=[string]$SIT + '\wlp\usr\servers\fhir-server\logs'
Copy-Item -Path $LOG_DIR -destination $pre_it_logs -Recurse

# Create the Zip File 
$compress = @{
LiteralPath= $pre_it_logs
CompressionLevel = 'Fastest'
DestinationPath = $zip_file
}
Compress-Archive @compress -Force

# If we weren't able to detect the fhir server ready within the allotted timeframe,
# then exit now...
If ( $STATUS -ne 200 ) {
    [string]$ERROR_MSG = 'Could not establish a connection to the fhir-server within ' + $tries + 'REST API invocations'
    Write-Error $ERROR_MSG
    exit 1
} 

Write-Host 'The fhir-server is apparently running'

exit 0

# End of Script 
