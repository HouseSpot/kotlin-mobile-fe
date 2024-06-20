import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class FormFeedbackViewModel : ViewModel() {

    private val _feedbackResult = MutableLiveData<Result<String>>()
    val feedbackResult: LiveData<Result<String>> get() = _feedbackResult

    private val _isFeedbackUploaded = MutableLiveData<Boolean>()
    val isFeedbackUploaded: LiveData<Boolean> get() = _isFeedbackUploaded

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun uploadFeedback(
        idClient: RequestBody,
        idVendor: RequestBody,
        image: MultipartBody.Part,
        message: RequestBody,
        rating: RequestBody
    ) {
        _loading.value = true

        val requestBodyMap = hashMapOf(
            "id_client" to idClient,
            "id_vendor" to idVendor,
            "rating" to rating,
            "message" to message,
        )

        viewModelScope.launch {
            try {
                val response = ApiConfig.getClientService().uploadFeedback(
                    requestBodyMap["id_client"]!!,
                    requestBodyMap["id_vendor"]!!,
                    image,
                    requestBodyMap["message"]!!,
                    requestBodyMap["rating"]!!,
                )

                _feedbackResult.postValue(Result.success("Feedback submitted successfully"))
                _isFeedbackUploaded.postValue(true)
                Log.d(TAG, "Feedback submitted successfully")
            } catch (e: Exception) {
                _feedbackResult.postValue(Result.failure(Exception("Response not successful: ${e.message} ")))
                _isFeedbackUploaded.postValue(false)
                Log.e(TAG, "Response not successful: ${e.message} ")
            } finally {
                _loading.value = false
            }
        }

    }

    fun uploadFeedbackWithoutImage(
        idClient: RequestBody,
        idVendor: RequestBody,
        message: RequestBody,
        rating: RequestBody
    ) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiConfig.getClientService().uploadFeedbackWithoutImage(
                        idClient,
                        idVendor,
                        message,
                        rating
                    ).execute()
                }

                if (response.isSuccessful) {
                    _feedbackResult.postValue(Result.success("Feedback submitted successfully"))
                    _isFeedbackUploaded.postValue(true)
                    Log.d(TAG, "Feedback submitted successfully")
                } else {
                    _feedbackResult.postValue(Result.failure(Exception("Response not successful: ${response.code()} ${response.message()}")))
                    _isFeedbackUploaded.postValue(false)
                    Log.e(TAG, "Response not successful: ${response.code()} ${response.message()}")
                }
            } catch (e: HttpException) {
                _feedbackResult.postValue(Result.failure(e))
                Log.e(TAG, "HttpException: ${e.message}")
            } catch (e: Exception) {
                _feedbackResult.postValue(Result.failure(e))
                Log.e(TAG, "Exception: ${e.message}")
            } finally {
                _loading.postValue(false)
            }
        }
    }

    companion object {
        private const val TAG = "FormFeedbackViewModel"
    }
}
