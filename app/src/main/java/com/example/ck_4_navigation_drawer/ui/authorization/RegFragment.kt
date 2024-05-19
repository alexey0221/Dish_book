package com.example.ck_4_navigation_drawer.ui.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ck_4_navigation_drawer.*
import com.example.ck_4_navigation_drawer.databinding.FragmentRegBinding
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*


class RegFragment : Fragment()
{
    private var _binding: FragmentRegBinding? = null
    private val binding get() = _binding!!

    private val client = HttpClient {
        install(JsonFeature)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentRegBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val editTextСreatTextEmailAddress:EditText = binding.editTextReatTextEmailAddress
        editTextСreatTextEmailAddress.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (editTextСreatTextEmailAddress.right - editTextСreatTextEmailAddress.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Обработка нажатия на кнопку очистки
                    editTextСreatTextEmailAddress.setText("") // Очистка текста в поле
                    return@setOnTouchListener true
                }
            }
            false
        }

        val editTextСreatTextPassword:EditText = binding.editTextReatTextPassword
        editTextСreatTextPassword.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (editTextСreatTextPassword.right - editTextСreatTextPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Обработка нажатия на кнопку очистки
                    editTextСreatTextPassword.setText("") // Очистка текста в поле
                    return@setOnTouchListener true
                }
            }
            false
        }

        val editTextReplayTextPassword:EditText = binding.editTextReplayTextPassword
        editTextReplayTextPassword.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (editTextReplayTextPassword.right - editTextReplayTextPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Обработка нажатия на кнопку очистки
                    editTextReplayTextPassword.setText("") // Очистка текста в поле
                    return@setOnTouchListener true
                }
            }
            false
        }

        val editTextName:EditText = binding.editTextPersonName
        editTextName.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (editTextName.right - editTextName.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Обработка нажатия на кнопку очистки
                    editTextName.setText("") // Очистка текста в поле
                    return@setOnTouchListener true
                }
            }
            false
        }

        val textViewResult:TextView = binding.textViewResult


        val buttonCreation:Button = binding.buttonCreation
        buttonCreation.setOnClickListener {
            var correctData = true

            textViewResult.text=""
            if (editTextСreatTextEmailAddress.text.isEmpty())
            {
                textViewResult.text = "Заполните поле ввода логина"
                correctData = false
            }
            else
            {
                if(editTextСreatTextPassword.text.isEmpty())
                {
                    textViewResult.text = "Заполните поле ввода пароля"
                    correctData = false
                }
                else
                {
                    if(editTextReplayTextPassword.text.isEmpty())
                    {
                        textViewResult.text = "Заполните поле повтора пароля"
                        correctData = false
                    }
                    else
                    {
                        if (editTextСreatTextPassword.text.toString().compareTo(editTextReplayTextPassword.text.toString())==0)
                        {
                            if (editTextName.text.isEmpty())
                            {
                                textViewResult.text = "Заполните поле ввода имени"
                                correctData = false
                            }

                        }
                        else
                        {
                            textViewResult.text = "Пароли не совпадают"
                            correctData = false
                        }
                    }
                }
            }




            if(correctData)
            {
                GlobalScope.launch(Dispatchers.Main)
                {
                    val Passwd = editTextСreatTextPassword.text.toString()
                    val hashedPassword = HashPass.hashWithSHA512(Passwd)
                    //добавление в базу данных нового пользователя
                    try {
                        val responseStatus = client.post<HttpResponse>("${URL.URL}/register?name=${editTextName.text}&email=${editTextСreatTextEmailAddress.text}&passwd=${hashedPassword}"){}
                        when(responseStatus.status){
                            HttpStatusCode.OK->
                            {
                                textViewResult.text = responseStatus.readText()
                                val db = AppDatabase.get(requireContext())
                                runBlocking {
                                    val job = CoroutineScope(Dispatchers.IO).launch() {
                                        db.userDao().delete()
                                        db.userDao().insertAll(
                                            User(
                                                editTextName.text.toString(),
                                                editTextСreatTextEmailAddress.text.toString(),
                                                true
                                            )
                                        )
                                    }
                                    job.join()
                                    account.UserInfo()
                                }
                            }

                            HttpStatusCode.NoContent ->{
                                textViewResult.text = responseStatus.readText()
                            }
//
                        }

                    }catch (e:Exception){
                        println("Ошибка при выполнении запроса: ${e.message}")
                    }
                }
            }



        }




        return root
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }

}


