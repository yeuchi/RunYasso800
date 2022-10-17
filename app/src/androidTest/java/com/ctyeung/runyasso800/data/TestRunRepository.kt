package com.ctyeung.runyasso800.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TestRunRepository @Inject constructor(
    @ApplicationContext context: Context
) : RunRepository(context) {
}