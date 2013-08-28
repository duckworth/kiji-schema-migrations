package com.mobileposse.maven.kiji.schema.migrations

import org.codehaus.gmaven.mojo.GroovyMojo

/**
 * Run the migration scripts
 *
 * @goal migrate
 */
public class MigrateMojo
extends GroovyMojo {
    void execute() {
        log.info('migrating kiji schema...')
    }
}