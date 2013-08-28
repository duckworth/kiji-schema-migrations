CREATE TABLE schema_migrations_test WITH DESCRIPTION 'schema migration test table'
ROW KEY FORMAT (instance)
WITH
  LOCALITY GROUP default
    WITH DESCRIPTION 'Main locality group' (
    MAXVERSIONS = INFINITY,
    TTL = FOREVER,
    INMEMORY = false,
    COMPRESSED WITH NONE,
    FAMILY values WITH DESCRIPTION 'main family' (
      version "long" WITH DESCRIPTION 'latest schema version'
    )
  );