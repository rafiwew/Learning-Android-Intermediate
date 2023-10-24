package com.piwew.mystudentdata

import android.app.Application
import com.piwew.mystudentdata.database.StudentDatabase
import com.piwew.mystudentdata.repo.StudentRepository

class MyApplication : Application() {
    val database by lazy { StudentDatabase.getDatabase(this) }
    val repository by lazy { StudentRepository(database.studentDao()) }
}