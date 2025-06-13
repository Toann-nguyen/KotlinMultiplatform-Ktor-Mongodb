package org.toannguyen.kotlintodoflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.toannguyen.kotlintodoflow.api.ApiClient
import org.toannguyen.kotlintodoflow.models.LoginRequest
import org.toannguyen.kotlintodoflow.models.RegisterRequest
import org.toannguyen.kotlintodoflow.models.User

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val errorMessage: String = "",
    val token: String? = null
)

public class AuthViewModel : ViewModel() {
    private val apiClient = ApiClient()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = ""
            )

            val response = apiClient.login(
                LoginRequest(username = username, password = password)
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isLoggedIn = response.success,
                user = response.user,
                token = response.token,
                errorMessage = if (!response.success) response.message else ""
            )
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = ""
            )

            val response = apiClient.register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isLoggedIn = response.success,
                user = response.user,
                token = response.token,
                errorMessage = if (!response.success) response.message else ""
            )
        }
    }

    fun logout() {
        _uiState.value = AuthUiState()
    }

    override fun onCleared() {
        super.onCleared()
        apiClient.close()
    }
}