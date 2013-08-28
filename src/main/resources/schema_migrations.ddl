
CREATE TABLE schema_migrations WITH DESCRIPTION 'schema migration versions from kiji-schema-migrations'
ROW KEY FORMAT (instance)
WITH
  LOCALITY GROUP default
    WITH DESCRIPTION 'Main locality group' (
    MAXVERSIONS = INFINITY,
    TTL = FOREVER,
    INMEMORY = false,
    COMPRESSED WITH NONE,
    FAMILY values WITH DESCRIPTION 'main family' (
      version "long" WITH DESCRIPTION 'latest schema version',
    )
  );