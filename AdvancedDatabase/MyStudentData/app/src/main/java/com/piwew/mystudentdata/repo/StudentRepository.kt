package com.piwew.mystudentdata.repo

import androidx.lifecycle.LiveData
import com.piwew.mystudentdata.database.Student
import com.piwew.mystudentdata.database.StudentDao
import com.piwew.mystudentdata.helper.InitialDataSource

class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudent(): LiveData<List<Student>> = studentDao.getAllStudent()

    suspend fun insertAllData() {
        studentDao.insertStudent(InitialDataSource.getStudents())
        studentDao.insertUniversity(InitialDataSource.getUniversities())
        studentDao.insertCourse(InitialDataSource.getCourses())
    }
}