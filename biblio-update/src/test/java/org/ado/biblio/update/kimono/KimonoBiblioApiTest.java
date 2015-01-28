package org.ado.biblio.update.kimono;

import org.ado.biblio.update.kimono.json.KimonoRelease;
import org.junit.Ignore;

@Ignore
public class KimonoBiblioApiTest {

    private KimonoBiblioApi unitUnderTest;

    @org.junit.Before
    public void setUp() throws Exception {
        unitUnderTest = new KimonoBiblioApi();
    }

    @org.junit.Test
    public void testGetResults() throws Exception {
        final KimonoRelease latestRelease = unitUnderTest.getLatestRelease();
    }
}