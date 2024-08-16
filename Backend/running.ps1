# Set the current directory
$current_dir = Get-Location

# Start Redis server if the port 6379 is not in use
if (-not (Get-NetTCPConnection -LocalPort 6379 -ErrorAction SilentlyContinue)) {
    Write-Host "Port 6379 is available, starting Redis..."
    Start-Process -NoNewWindow -FilePath "C:\Program Files\Redis\redis-server.exe" -PassThru | Out-Null
} else {
    Write-Host "Port 6379 is already in use, skipping Redis start."
}

# Start Python HTTP server in the QR code directory
Set-Location "$current_dir\OrderQRCodes"
Start-Process -NoNewWindow -FilePath "C:\Python312\python.exe" -ArgumentList "-m http.server 8000" -PassThru | Out-Null

# Start ngrok tunnel for the Python server
Start-Process -NoNewWindow -FilePath "C:\Program Files\Ngrok\ngrok.exe" -ArgumentList "http 8000" -PassThru | Out-Null

# Wait for ngrok to initialize
Start-Sleep -Seconds 5

# Fetch the ngrok public URL
$ngrokResponse = Invoke-RestMethod -Uri http://localhost:4040/api/tunnels
$NGROK_URL = $ngrokResponse.tunnels[0].public_url
Write-Host "ngrok public URL: $NGROK_URL"

# Export the ngrok URL as an environment variable for the current session
[System.Environment]::SetEnvironmentVariable('NGROK_URL', $NGROK_URL, 'Process')

# Optionally, write it to a file that Spring Boot can read
$ngrokFile = Join-Path -Path $current_dir -ChildPath "ngrok_url.txt"
$NGROK_URL | Out-File -FilePath $ngrokFile

# Start the Spring Boot application using Maven
Set-Location $current_dir
Start-Process -NoNewWindow -FilePath "C:\Program Files\Apache\apache-maven-3.9.8\bin\mvn.cmd" -ArgumentList "spring-boot:run" -PassThru | Out-Null


# Wait for all background processes to finish
# This will not terminate the script, it will just wait a few seconds
Start-Sleep -Seconds 10  # Adjust wait time if necessary
