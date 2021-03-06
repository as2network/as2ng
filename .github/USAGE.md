# Usage

## Configuration

### Server

| System Property | Environment Variable  | Description                                | Default   |
| :-------------- | :-------------------- | :----------------------------------------- | :-------- |
| `-Dhttp.host`   | `AS2NG_HTTP_HOST`     | Hostname that the server is reachable from | localhost |
| `-Dhttp.port`   | `AS2NG_HTTP_PORT`     | Port that the server is reachable from     | 8080      |
| `-Dhttp.https`  | `AS2NG_HTTPS_ENABLED` | Whether or not https is enabled            | false     |

### Postgres

| System Property              | Environment Variable          | Description                                                                | Default                                                     |
| :--------------------------- | :---------------------------- | :------------------------------------------------------------------------- | :---------------------------------------------------------- |
| `-Dpostgres.jdbcUrl`         | `AS2NG_JDBC_URL`              | JDBC style connection url                                                  | jdbc:postgresql://localhost/as2ng?user=as2ng&password=as2ng |
| `-Dpostgres.maximumPoolSize` | `AS2NG_JDBC_CONNECTION_LIMIT` | Maxmimum number of database connections to maintain in the connection pool | `30`                                                        |

### S3 Compliant Storage

| System Property                 | Environment Variable        | Description                                       | Default                 |
| :------------------------------ | :-------------------------- | :------------------------------------------------ | :---------------------- |
| `-Ds3.bucket`                   | `AS2NG_S3_BUCKET`           | Bucket to use when storing http request bodies    | `as2ng`                 |
| `-Ds3.endpoint.serviceEndpoint` | `AS2NG_S3_SERVICE_ENDPOINT` | Base service endpoint to use when making requests | `http://localhost:9000` |
| `-Ds3.endpoint.signingRegion`   | `AS2NG_S3_SIGNING_REGION`   | Signing region to use when making requests        | `us-east-1`             |
| `-Ds3.endpoint.accessKey`       | `AS2NG_S3_ACCESS_KEY`       | Access key for authentication                     | `minio`                 |
| `-Ds3.endpoint.secretKey`       | `AS2NG_S3_SECRET_KEY`       | Secret key for authentication                     | `12345678`              |
