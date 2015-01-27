package org.ado.biblio.update.kimono;

import org.ado.biblio.update.kimono.json.KimonoRelease;

import java.util.List;

public class KimonoBiblioApiTest {

    private KimonoBiblioApi unitUnderTest;

    @org.junit.Before
    public void setUp() throws Exception {
        unitUnderTest = new KimonoBiblioApi();
    }

    @org.junit.Test
    public void testGetResults() throws Exception {
        final List<KimonoRelease> releases = unitUnderTest.getReleases();
    }
}