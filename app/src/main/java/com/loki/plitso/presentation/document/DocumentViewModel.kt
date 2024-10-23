package com.loki.plitso.presentation.document

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.plitso.data.local.dao.FoodDocumentDao
import com.loki.plitso.data.local.models.FoodDocument
import com.loki.plitso.util.TimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DocumentViewModel(
    private val foodDocumentDao: FoodDocumentDao,
) : ViewModel() {
    val foodDocuments =
        foodDocumentDao.getFoodDocuments().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList(),
        )

    private val _foodDocument =
        MutableStateFlow(
            FoodDocument(
                type = "",
                servedOn = "",
                description = "",
            ),
        )
    val foodDocument = _foodDocument.asStateFlow()

    private val _time = MutableStateFlow(TimeUtil.currentTime())
    val time = _time.asStateFlow()
    private val _date = MutableStateFlow(TimeUtil.currentDate())
    val date = _date.asStateFlow()
    val error = mutableStateOf("")

    fun onTypeChange(newValue: String) {
        if (_foodDocument.value.type.isNotEmpty()) {
            error.value = ""
        }
        _foodDocument.value =
            _foodDocument.value.copy(
                type = newValue,
            )
    }

    fun onTimeChange(newValue: String) {
        _time.value = newValue
    }

    fun onDateChange(newValue: String) {
        _date.value = newValue
    }

    fun onPictureChangeChange(newValue: String) {
        _foodDocument.value =
            _foodDocument.value.copy(
                picture = newValue,
            )
    }

    fun onDescriptionChange(newValue: String) {
        if (_foodDocument.value.description.isNotEmpty()) {
            error.value = ""
        }
        _foodDocument.value =
            _foodDocument.value.copy(
                description = newValue,
            )
    }

    fun saveFoodDocument() {
        if (_foodDocument.value.type.isEmpty()) {
            error.value = "Please select meal type"
            return
        }

        if (_foodDocument.value.description.isEmpty()) {
            error.value = "Please write something that you ate"
            return
        }

        error.value = ""

        viewModelScope.launch {
            foodDocumentDao.insert(
                foodDocument.value.copy(
                    servedOn = "${time.value},${date.value}",
                ),
            )

            _foodDocument.value =
                FoodDocument(
                    servedOn = "",
                    description = "",
                    type = "",
                )
            _time.value = ""
            _date.value = ""
        }
    }

    fun deleteFoodDocument(foodDocument: FoodDocument) {
        viewModelScope.launch {
            foodDocumentDao.delete(foodDocument)
        }
    }
}
