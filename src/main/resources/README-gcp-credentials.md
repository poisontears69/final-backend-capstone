# Google Cloud Storage Credentials

To use Google Cloud Storage in this application, you need to provide a credentials file from your Google Cloud project.

## Steps to Create and Configure the Credentials

1. **Create a Google Cloud Platform account** (if you haven't already)
   - Go to https://console.cloud.google.com
   - Sign up or sign in with your Google account

2. **Create a new project**
   - Click on the project dropdown at the top of the page
   - Click "New Project"
   - Enter a name for your project and click "Create"

3. **Enable Cloud Storage API**
   - Go to "APIs & Services" > "Library"
   - Search for "Cloud Storage"
   - Click on "Cloud Storage API"
   - Click "Enable"

4. **Create a service account**
   - Go to "IAM & Admin" > "Service Accounts"
   - Click "Create Service Account"
   - Enter a name and description for the service account
   - Click "Create and Continue"
   - For the role, select "Storage" > "Storage Admin" to give full access to storage
   - Click "Continue" and then "Done"

5. **Create a key for the service account**
   - Find your service account in the list and click on it
   - Go to the "Keys" tab
   - Click "Add Key" > "Create New Key"
   - Choose "JSON" format
   - Click "Create" - this will download the key file to your computer

6. **Add credentials to the application**
   - Rename the downloaded key file to `gcp-credentials.json`
   - Place it in the `src/main/resources` directory of this project

## Alternatively: Use Environment Variables

Instead of placing the credentials file in the resources directory, you can:

1. Store the credentials file securely outside the project
2. Set the `GCP_CREDENTIALS_PATH` environment variable to point to your file
3. Update the `GCP_BUCKET_NAME` and `GCP_BASE_URL` environment variables

## Example Environment Variables

```
GCP_CREDENTIALS_PATH=/path/to/your/gcp-credentials.json
GCP_BUCKET_NAME=your-bucket-name
GCP_BASE_URL=https://storage.googleapis.com
```

## Security Note

Never commit your credentials file to version control! The file contains sensitive information that should be kept private.

Make sure to add `gcp-credentials.json` to your `.gitignore` file to prevent it from being accidentally committed. 