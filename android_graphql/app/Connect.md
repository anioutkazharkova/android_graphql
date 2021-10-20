### Download Schema

```
## Create folder structure
mkdir -p ./app/src/main/graphql/com/ex2/hasura/gql/
```

```
./gradlew downloadApolloSchema \
--endpoint="https://star-hen-48.hasura.app/v1/graphql" \
--schema="./app/src/main/graphql/com/ex2/hasura/gql/schema.json" \
--header="x-hasura-admin-secret: <key>"
```

### Codegen

```
apollo-codegen download-schema "https://star-hen-48.hasura.app/v1/graphql" 
--output schema.json --header "x-hasura-admin-secret: <Key>"
```

### GraphQL config

```
{
  "name": "Expenses Schema",
  "schemaPath": "schema.json",
  "extensions": {
    "endpoints": {
      "Default GraphQL Endpoint": {
        "url": "https://star-hen-48.hasura.app/v1/graphql",
        "headers": {
          "user-agent": "JS GraphQL",
          "x-hasura-admin-secret" : "<Key>"
        },
        "introspect": false
      }
    }
  }
}
```