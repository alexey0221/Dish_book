package com.example.ck_4_navigation_drawer.ui.favourites

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ck_4_navigation_drawer.*
//import com.example.ck_4_navigation_drawer.account.authorization
//import com.example.ck_4_navigation_drawer.account.nickName
import com.example.ck_4_navigation_drawer.databinding.FragmentFavouritesBinding
import com.example.ck_4_navigation_drawer.ui.home.DataHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

class FavouritesFragment : Fragment()
{

    private var _binding: FragmentFavouritesBinding? = null


    private val binding get() = _binding!!




    fun dpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private val client = HttpClient {
        install(JsonFeature)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {


        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var nickName = "Гость"
        var authorization :Boolean = false
        val db = AppDatabase.get(requireContext())
        var nickNameList = listOf <String>()
        var authorizationList =listOf <Boolean>()
        CoroutineScope(Dispatchers.IO).launch() {
            nickNameList = withContext(Dispatchers.IO) { db.userDao().getNickName() }
            authorizationList = withContext(Dispatchers.IO) { db.userDao().getAuthorization() }

        //===================================
            if(nickNameList.isEmpty())
            {
                authorization = false
            }
            else
            {
                nickName = nickNameList[0]
                authorization = authorizationList[0]
                Log.i("IN test авторизация", nickName+authorization.toString())
                println("1-$authorization")
            }
            println("2-$authorization")


            val scrollView = ScrollView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(100, 30, 100, 30)
            scrollView.layoutParams = layoutParams
            val linearLayout = LinearLayout(requireContext())
            val linearParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.layoutParams = linearParams
            scrollView.addView(linearLayout)

            val textViewParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            println("3-$authorization")
            if(authorization)
            {
                GlobalScope.launch(Dispatchers.Main)
                {
                    // Обработка полученного ответа
                    // Например, отображение ответа в текстовом поле или логирование

                    val response:String = client.get("${URL.URL}/getCollection?userName=${nickName}")
                    val gson = Gson() // Предполагается, что вы используете библиотеку Gson для работы с JSON

                    val dishSetType = object : TypeToken<Set<Dish>>() {}.type
                    val dishsToAdd: Set<Dish> = gson.fromJson(response, dishSetType)

                    FavouritesDishArray .addAll(dishsToAdd)

                }




                //================
                for (favDish in FavouritesDishArray) {
                    val linearLayoutHor = LinearLayout(requireContext())
                    val linearParamsHor = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dpToPx(161F)
                    )
                    linearLayoutHor.orientation = LinearLayout.HORIZONTAL
                    linearLayoutHor.layoutParams = linearParamsHor

                    val linearLayoutHor_vert = LinearLayout(requireContext())
                    val linearParamsHor_vert = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    linearLayoutHor_vert.orientation = LinearLayout.VERTICAL
                    linearLayoutHor_vert.layoutParams = linearParamsHor_vert

                    val imageView1 = ImageView(requireContext())
                    val params1 = LinearLayout.LayoutParams(
                        dpToPx(140F),
                        dpToPx(133F)
                    )
                    //params1.setMargins(0, 30, 0, 30)
                    //dpToPxparams1.gravity = Gravity.CENTER
                    imageView1.layoutParams = params1
                    val imageBytes2 = android.util.Base64.decode(favDish.photo, android.util.Base64.DEFAULT)
                    // Преобразование массива байтов в изображение
                    val bitmap2 = android.graphics.BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.size)
                    // Отображение изображения в ImageView
                    imageView1.setImageBitmap(bitmap2)
                    //imageView1.setImageResource(favFilm.wallpaper)
                    linearLayoutHor.addView(imageView1)

                    val textViewNazvanie = TextView(requireContext())
                    textViewNazvanie.setText(favDish.nameDish)
                    textViewNazvanie.setTypeface(null, Typeface.BOLD)
                    textViewNazvanie.textSize = 16f
                    textViewNazvanie.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                    val textViewRating = TextView(requireContext())
                    textViewRating.text = "Рейтинг: ${favDish.rating}"
                    textViewRating.setTypeface(null, Typeface.BOLD)


                    val textView2 = TextView(requireContext())
                    textView2.setText("Тэги:")
                    textView2.setTypeface(null, Typeface.BOLD)

                    val textViewZhanr = TextView(requireContext())
                    textViewZhanr.setText(favDish.tags)

                    textViewNazvanie.layoutParams = textViewParams
                    textViewRating.layoutParams = textViewParams

                    textView2.layoutParams = textViewParams
                    textViewZhanr.layoutParams = textViewParams

                    textViewNazvanie.setOnClickListener {
                        DataHolder.dish = favDish
                        findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
                    }
                    imageView1.setOnClickListener{
                        DataHolder.dish = favDish
                        findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
                    }

                    linearLayoutHor_vert.addView(textViewNazvanie)
                    linearLayoutHor_vert.addView(textViewRating)

                    linearLayoutHor_vert.addView(textView2)
                    linearLayoutHor_vert.addView(textViewZhanr)
                    linearLayoutHor.addView(linearLayoutHor_vert)

                    linearLayout.addView(linearLayoutHor)

                }
            }
            else
            {
                println("4-$authorization")
                val textViewNoFav = TextView(requireContext())
                textViewNoFav.text = "Авторизуйтесь чтобы просматривать свою коллекцию избранного"
                //layoutParams.gravity = Gravity.CENTER_VERTICAL
                textViewNoFav.layoutParams = textViewParams
                textViewNoFav.gravity = Gravity.CENTER


                linearLayout.addView(textViewNoFav)

            }



            val linearLayout1 =  binding.root.findViewById<LinearLayout>(R.id.rootContainer)
            linearLayout1?.addView(scrollView)
        }

        return root
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}