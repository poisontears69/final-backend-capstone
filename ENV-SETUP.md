# Environment Variable Setup

This application uses environment variables for configuration. Here's how to set them up:

## Required Environment Variables

```
# Database Configuration
DB_URL=jdbc:mysql://mainline.proxy.rlwy.net:28437/railway
DB_USERNAME=root
DB_PASSWORD=zbPJGPMNyPvurRvJZqLBFZpxMYYwDyzd

# Application Configuration
SERVER_PORT=8080

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:4200

# JWT Configuration
JWT_SECRET=yourSecretKey123!@#
JWT_EXPIRATION=86400000
```

## Setting Environment Variables

### Windows (Command Prompt)
```
set DB_URL=jdbc:mysql://mainline.proxy.rlwy.net:28437/railway
set DB_USERNAME=root
set DB_PASSWORD=zbPJGPMNyPvurRvJZqLBFZpxMYYwDyzd
set SERVER_PORT=8080
set CORS_ALLOWED_ORIGINS=http://localhost:4200
set JWT_SECRET=yourSecretKey123!@#
set JWT_EXPIRATION=86400000
```

### Windows (PowerShell)
```
$env:DB_URL="jdbc:mysql://mainline.proxy.rlwy.net:28437/railway"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="zbPJGPMNyPvurRvJZqLBFZpxMYYwDyzd"
$env:SERVER_PORT="8080"
$env:CORS_ALLOWED_ORIGINS="http://localhost:4200"
$env:JWT_SECRET="yourSecretKey123!@#"
$env:JWT_EXPIRATION="86400000"
```

### Linux/macOS
```
export DB_URL=jdbc:mysql://mainline.proxy.rlwy.net:28437/railway
export DB_USERNAME=root
export DB_PASSWORD=zbPJGPMNyPvurRvJZqLBFZpxMYYwDyzd
export SERVER_PORT=8080
export CORS_ALLOWED_ORIGINS=http://localhost:4200
export JWT_SECRET=yourSecretKey123!@#
export JWT_EXPIRATION=86400000
```

## Using with IDE

For IntelliJ IDEA, you can set these environment variables in your run configuration:

1. Go to Run > Edit Configurations
2. Select your Spring Boot configuration
3. Go to the "Environment" tab
4. Add these environment variables in the "Environment variables" field

## Railway Deployment

When deploying to Railway, set these variables in the Railway dashboard under the "Variables" tab.

## Note on Security

- Never commit your actual credentials to version control
- Generate a strong random JWT secret for production
- Consider using a different password for local development and production
- Update the `.gitignore` file to exclude any local environment files 