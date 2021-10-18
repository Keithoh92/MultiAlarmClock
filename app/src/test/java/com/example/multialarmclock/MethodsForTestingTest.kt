package com.example.multialarmclock

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.*

class MethodsForTestingTest{

    val cal = Calendar.getInstance()

    @Test
    fun `time given returns string in correct format for textview when time is greater than 12`()
    {
        cal.set(Calendar.HOUR, 13).toString()
        cal.set(Calendar.MINUTE, 54).toString()

        val result = MethodsForTesting.formatTimeForDB1(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE))
        assertThat(result).isEqualTo("13:54")
    }

    @Test
    fun `time given returns string in correct format for textview when time is less than 12`()
    {
        cal.set(Calendar.HOUR, 8).toString()
        cal.set(Calendar.MINUTE, 0).toString()

        val result = MethodsForTesting.formatTimeForDB1(cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE))
        assertThat(result).isEqualTo("08:00")
    }
}