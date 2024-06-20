import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import com.entsh118.housespot.ui.layananjasa.viewmodel.FormPesananViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response

class FormFeedbackViewModel : ViewModel() {

    private val _feedbackResult = MutableLiveData<Result<String>>()
    val feedbackResult: LiveData<Result<String>> get() = _feedbackResult


    private val _isFeedbackUploaded = MutableLiveData<Boolean>()
    val isFeedbackUploaded: LiveData<Boolean> get() = _isFeedbackUploaded

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun uploadFeedback(
        idClient: String,
        idVendor: String,
        image: MultipartBody.Part,
        message: String,
        rating: String
    ) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response: Response<Void> = ApiConfig.getClientService().uploadFeedback(
                    idClient,
                    idVendor,
                    image,
                    message.toRequestBody(MultipartBody.FORM),
                    rating.toRequestBody(MultipartBody.FORM)
                ).execute()

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
                Log.e(FormFeedbackViewModel.TAG, "HttpException: ${e.message}")
            } catch (e: Exception) {
                _feedbackResult.postValue(Result.failure(e))
                Log.e(FormFeedbackViewModel.TAG, "Exception: ${e.message}")
            } finally {
                _loading.postValue(false)
            }
        }
    }

    companion object {
        private const val TAG = "FormFeedbackViewModel"
    }

}
