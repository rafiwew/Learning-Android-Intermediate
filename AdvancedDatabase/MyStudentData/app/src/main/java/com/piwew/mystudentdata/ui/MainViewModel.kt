package com.piwew.mystudentdata.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.piwew.mystudentdata.database.Student
import com.piwew.mystudentdata.database.StudentAndUniversity
import com.piwew.mystudentdata.repo.StudentRepository
import kotlinx.coroutines.launch

class MainViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    init {
        insertAllData()
    }

    fun getAllStudent(): LiveData<List<Student>> = studentRepository.getAllStudent()
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> =
        studentRepository.getAllStudentAndUniversity()

    private fun insertAllData() = viewModelScope.launch {
        studentRepository.insertAllData()
    }
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