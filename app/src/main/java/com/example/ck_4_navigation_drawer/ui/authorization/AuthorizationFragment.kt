package com.example.ck_4_navigation_drawer.ui.authorization

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ck_4_navigation_drawer.*
import com.example.ck_4_navigation_drawer.HashPass.hashWithSHA512
//import com.example.ck_4_navigation_drawer.account.authorization
//import com.example.ck_4_navigation_drawer.account.nickName
import com.example.ck_4_navigation_drawer.databinding.FragmentAuthorizationBinding
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*

class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null


    private val binding get() = _binding!!

    private val client = HttpClient {
        install(JsonFeature)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val EditTextEmail:EditText = binding.editTextTextEmailAddress

        EditTextEmail.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (EditTextEmail.right - EditTextEmail.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Обработка нажатия на кнопку очистки
                    EditTextEmail.setText("") // Очистка текста в поле
                    return@setOnTouchListener true
                }
            }
            false
        }



        val EditTextPass:EditText = binding.editTextTextPassword



        EditTextPass.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (EditTextPass.right - EditTextPass.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Обработка нажатия на кнопку очистки
                    EditTextPass.setText("") // Очистка текста в поле
                    return@setOnTouchListener true
                }
            }
            false
        }


        val textViewRez:TextView = binding.textViewRez

        val ButtonVxod:Button = binding.buttonVxod
        ButtonVxod.setOnClickListener{
            var correctInput = true
            if(EditTextEmail.text.isEmpty())
            {
                correctInput = false
                textViewRez.text = "Заполните поле ввода логина"
            }
            else{
                if (EditTextPass.text.toString().isEmpty())
                {
                    correctInput = false
                    textViewRez.text = "Заполните поле ввода пароля"

                }
            }



            if(correctInput) {
                val hashedPassword =
                    hashWithSHA512(EditTextPass.text.toString())  // Хэширование пароля


                //отправка на сравнение логина и пароля существующего пользователя
                try {
                    var Resp : HttpResponse? = null
                    var asText :String =""
                    GlobalScope.launch(Dispatchers.Main)
                    {
                        Resp =
                            client.get<HttpResponse>("${URL.URL}/authorization?email=${EditTextEmail.text.toString()}&passwd=${hashedPassword}")
                        asText = Resp!!.readText()
                        when (Resp!!.status) {
                            HttpStatusCode.OK -> {

                                val db = AppDatabase.get(requireContext())
                                runBlocking {
                                    val job =/*GlobalScope*/
                                        CoroutineScope(Dispatchers.IO).launch() {
                                            db.userDao().delete()
                                            db.userDao().insertAll(
                                                User(
                                                    asText,
                                                    EditTextEmail.text.toString(),
                                                    true
                                                )
                                            )
                                        }
                                    job.join()

                                    textViewRez.text =
                                        "Пользователь ${asText} успешно авторизировался"
                                    //authorization = true
                                    account.UserInfo()
                                }
                            }

                            HttpStatusCode.NoContent -> {
                                textViewRez.text = asText
                            }
                        }
                    }



                } catch (e: Exception) {
                    textViewRez.text = "Авторизация провалена"
                    println("Ошибка при выполнении запроса: ${e.message}")
                    Log.d("RRR", e.toString())
                }
            }





        }

        val ButtonReg:Button = binding.buttonReg

        ButtonReg.setOnClickListener {
            findNavController().navigate(R.id.action_AuthorizationFragment_to_RegFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}