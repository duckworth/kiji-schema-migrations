package com.mobileposse.maven.kiji.schema.migrations;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * Run the migration scripts
 */
public class MigrateMojoTest extends AbstractMojoTestCase {

    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /** {@inheritDoc} */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * @throws Exception if any
     */
    public void testMigrationDefault() throws Exception {
        File testBasedir = getTestFile("src/test/resources/unit/project-to-test");
        File pom = new File(testBasedir, "pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        MigrateMojo migrateMojo = (MigrateMojo) lookupMojo("migrate", pom);
        assertNotNull(migrateMojo);
        migrateMojo.migrateFolder = new File(testBasedir, "src/main/resources/schema/migrate").getCanonicalPath();
        migrateMojo.kijiURI = "kiji://.env/default";
        migrateMojo.execute();
    }
}
