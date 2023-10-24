package com.piwew.mystudentdata.repo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.piwew.mystudentdata.database.Student
import com.piwew.mystudentdata.database.StudentAndUniversity
import com.piwew.mystudentdata.database.StudentDao
import com.piwew.mystudentdata.database.StudentWithCourse
import com.piwew.mystudentdata.database.UniversityAndStudent
import com.piwew.mystudentdata.helper.SortType
import com.piwew.mystudentdata.helper.SortUtils

class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudent(sortType: SortType): LiveData<PagedList<Student>> {
        val query = SortUtils.getSortedQuery(sortType)
        val student = studentDao.getAllStudent(query)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(30)
            .setPageSize(10)
            .build()

        return LivePagedListBuilder(student, config).build()
    }

    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> =
        studentDao.getAllStudentAndUniversity()

    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> =
        studentDao.getAllUniversityAndStudent()

    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> =
        studentDao.getAllStudentWithCourse()

}