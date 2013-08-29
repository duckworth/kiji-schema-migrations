package com.mobileposse.maven.kiji.schema.migrations;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * Resets the migration version
 */
public class ResetMojoTest extends AbstractMojoTestCase {

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
    public void testResetDefault() throws Exception {
        File testBasedir = getTestFile("src/test/resources/unit/project-to-test");
        File pom = new File(testBasedir, "pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        ResetMojo resetMojo = (ResetMojo) lookupMojo("reset", pom);
        assertNotNull(resetMojo);
        resetMojo.kijiURI = "kiji://.env/sandbox";
        resetMojo.execute();
    }
}
