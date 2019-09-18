

## Migrations
When adding a migration, remember to add a `logicalFilePath="RELATIVE_PATH_TO_FILE"` as a parameter to each changelog
files root element. This will ensure that the changelog file has a unique path to it. **We require a unique changelog 
path**, because otherwise migrations ran by the maven liquibase plugin and migrations ran by application start might
get slightly different paths.

The different paths issue shows itself by migrations being run again, even though you have executed them already. This
is because a migration is identified by the triple `(id, author, filepath)`.  