package com.example.consecutivepractices

import com.example.consecutivepractices.domain.model.isValidResumeUrl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResumeUrlTest {
    @Test
    fun acceptsHttpAndHttpsWithHost() {
        assertTrue(isValidResumeUrl("https://example.com/resume.pdf"))
        assertTrue(isValidResumeUrl("http://example.com/resume.pdf"))
    }

    @Test
    fun rejectsBlankUnsupportedAndMalformedUrls() {
        assertFalse(isValidResumeUrl(""))
        assertFalse(isValidResumeUrl("file:///resume.pdf"))
        assertFalse(isValidResumeUrl("https:///resume.pdf"))
        assertFalse(isValidResumeUrl("https://example.com/bad path"))
    }
}
