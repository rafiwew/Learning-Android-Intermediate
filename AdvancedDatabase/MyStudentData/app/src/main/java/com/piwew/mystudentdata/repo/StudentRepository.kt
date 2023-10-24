package com.piwew.mystudentdata.repo

import androidx.lifecycle.LiveData
import com.piwew.mystudentdata.database.Student
import com.piwew.mystudentdata.database.StudentAndUniversity
import com.piwew.mystudentdata.database.StudentDao
import com.piwew.mystudentdata.database.StudentWithCourse
import com.piwew.mystudentdata.database.UniversityAndStudent

class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudent(): LiveData<List<Student>> = studentDao.getAllStudent()

    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> =
        studentDao.getAllStudentAndUniversity()

    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> =
        studentDao.getAllUniversityAndStudent()

    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> =
        studentDao.getAllStudentWithCourse()

}