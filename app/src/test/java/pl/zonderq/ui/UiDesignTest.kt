package pl.zonderq.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UiDesignTest {
    @Test fun designTokensAreSensible() {
        assertEquals(4, UiDesign.appGridColumns)
        assertTrue(UiDesign.cornerDp in 16..40)
        assertTrue(UiDesign.maxVisibleApps >= UiDesign.appGridColumns)
    }
}
