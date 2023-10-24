package com.piwew.mystudentdata.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.piwew.mystudentdata.database.Student
import com.piwew.mystudentdata.database.StudentAndUniversity
import com.piwew.mystudentdata.database.StudentWithCourse
import com.piwew.mystudentdata.database.UniversityAndStudent
import com.piwew.mystudentdata.repo.StudentRepository

class MainViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    fun getAllStudent(): LiveData<List<Student>> = studentRepository.getAllStudent()

    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> =
        studentRepository.getAllStudentAndUniversity()

    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> =
        studentRepository.getAllUniversityAndStudent()

    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> =
        studentRepository.getAllStudentWithCourse()

}

class ViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}