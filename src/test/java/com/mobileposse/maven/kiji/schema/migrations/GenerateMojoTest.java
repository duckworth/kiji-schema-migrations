package com.mobileposse.maven.kiji.schema.migrations;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * generates the migration version
 */
public class GenerateMojoTest extends AbstractMojoTestCase {

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
    public void testgenerateDefault() throws Exception {
        File testBasedir = getTestFile("src/test/resources/unit/project-to-test");
        File pom = new File(testBasedir, "pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        GenerateMojo generateMojo = (GenerateMojo) lookupMojo("generate", pom);
        assertNotNull(generateMojo);
        generateMojo.migrateFolder = new File(testBasedir, "target/schema/migrate").getCanonicalPath();
        generateMojo.execute();
    }
}
