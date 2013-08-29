kiji-schema-migrations
======================

This is simple maven plugin that allows to execute sequence of ddl statements using `kiji-schema-shell`
as part of CI build process or from command line.

### Usage
Place you ddl scripts into `src/main/resources/schema/migrate`, name them using `${migration-id}_${migration-name}.ddl`
as a convention, where `${migration-id}` is date / time, i.e. "20130828163000"
and `${migration-name}` is some description of a change, i.e. "Add_Record_Label_Field".

Add the following to your pom:

      <build>
        <plugins>
          ...
          <plugin>
            <groupId>com.mobileposse.maven.plugins</groupId>
            <artifactId>kiji-schema-migrations-maven-plugin</artifactId>
            <version>0.1.0</version>
            <executions>
              <execution>
                <goals>
                  <goal>migrate</goal>
                </goals>
                <phase>pre-integration-test</phase>
              </execution>
            </executions>
            <configuration>
              <kijiURI>kiji://.env/sandbox</kijiURI>
              <migrateFolder>${project.basedir}/src/main/resources/migrations</migrateFolder>
            </configuration>
          </plugin>
          ...
        </plugins>
      </build>

Default values for `kijiURL` and `migrateFolder` are `kiji://.env/default` and `${project.basedir}/src/main/resources/schema/migrate`

You can explicitly invoke the plugin by running one of the following goals:

> `mvn kiji-schema-migrations:generate -DmigrationName=A_Lot_Of_Changes`


> `mvn kiji-schema-migrations:migrate`


> `mvn kiji-schema-migrations:reset`

The plugin keeps latest applied migration in `user.schema.migration.version` property of kiji system.
To use different property you can specify `migrationKey` configuration parameter.

Execution of `migrate` goal runs all the ddl scripts that have `${migration-id}` higher than latest saved one,
updating migration version in kiji system after each successful execution.

Execution of `reset` goal sets the migration version to empty string, leaving cleanup of the schema to you.
You can override the migration version to reset to by specifying `migrationVersion` configuration property.

