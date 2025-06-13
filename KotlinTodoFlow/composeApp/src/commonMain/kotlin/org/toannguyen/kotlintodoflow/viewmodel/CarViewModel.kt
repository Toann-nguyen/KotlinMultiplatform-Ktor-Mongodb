package org.toannguyen.kotlintodoflow.viewmodel

/*
class CarViewModel {
    private val apiClient = ApiClient()
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val cars: StateFlow<List<Car>> = _cars

    fun loadCars() {
        viewModelScope.launch {
            try {
                val result = apiClient.getCars()
                _cars.value = result
            } catch (e: Exception) {
                // Xử lý lỗi
                println("Error loading cars: ${e.message}")
            }
        }
    }

    fun createCar(brandName: String, model: String, number: String) {
        viewModelScope.launch {
            try {
                val car = Car(brandName = brandName, model = model, number = number)
                apiClient.createCar(car)
                loadCars() // Reload danh sách sau khi tạo
            } catch (e: Exception) {
                // Xử lý lỗi
                println("Error creating car: ${e.message}")
            }
        }
    }
}*/