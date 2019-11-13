# PingOneTokenRequest

The project focuses on integrate an OIDC application to PingOne enterprise.

## Instructions

1. First add a new application on PingOne
   * Create a free trial on the admin portal: https://admin.pingone.com/
   * Documentation to add new application: https://support.pingidentity.com/s/document-item?bundleId=pingone&topicId=agd1564020501024.html

2. Create certifications for https:
   * https://lightbend.github.io/ssl-config/CertificateGeneration.html
   * Save the certs in the `<project folder>/certs`
   * Update the info about the certs in: `<project folder>/conf/application.conf`

3. Install ngrok
   * Start command: `./ngrok http https://localhost:9443`
   * Set up the urls on the PingOne admin portal:
     * START SSO URL: `<your ngrok url>/init`
     * REDIRECT URIS: `<your ngrok url>/tokenexchange`

4. Add the constants from the GUI to the source file: `file: controllers.Constants`

5. Start the application: `env JAVA_OPTS="-Duser.timezone=UTC" sbt "start -Dhttps.port=9443"`

6. You can start the flow with the `INITIATE SINGLE SIGN-ON (SSO) URL` in the application settings or from the dock if you set it up.
You will need a mobile phone, during the first authentication it will help you to download the app and register.