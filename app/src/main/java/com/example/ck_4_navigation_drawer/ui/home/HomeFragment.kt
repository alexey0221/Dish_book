package com.example.ck_4_navigation_drawer.ui.home


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ck_4_navigation_drawer.*
import com.example.ck_4_navigation_drawer.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.util.*



object DataHolder {
    lateinit var dish: Dish
}
class HomeFragment : Fragment()
{
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    var ChipGroupList:MutableList<String> = mutableListOf()

    var dishes: MutableList<Dish> = mutableListOf()


    private val client = HttpClient {
        install(JsonFeature)
    }


    var executeGetRequest: (urlGet:String) -> Unit = {
        //  функция по умолчанию

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//===========================================================================================



        fun dpToPx(dp: Float): Int {
            return (dp * resources.displayMetrics.density).toInt()
        }


        var deleteDishArray = listOf<LinearLayout>()


        val scrollView = ScrollView(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(30, 30, 30, 30)
        scrollView.layoutParams = layoutParams
        val linearLayout = LinearLayout(requireContext())
        val linearParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = linearParams
        scrollView.addView(linearLayout)




        val editText = EditText(requireContext())
        val editTextParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        editText.layoutParams = editTextParams
        editText.gravity = Gravity.CENTER_HORIZONTAL
        editText.hint = "Введите название блюда"

        var drawableSearch = resources.getDrawable(android.R.drawable.ic_search_category_default)

        var drawableSearchBit = (drawableSearch as BitmapDrawable).bitmap

        drawableSearchBit = Bitmap.createScaledBitmap(drawableSearchBit, 60, 60, false)

        drawableSearch = BitmapDrawable(resources, drawableSearchBit)

        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableSearch, null)

        linearLayout.addView(editText)





        val chipGroup = ChipGroup(requireContext())
        val chipGroupParams  = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.layoutParams = chipGroupParams
        linearLayout.addView(chipGroup)

        val spinner_tags = Spinner(requireContext())
        val spinner_tagsParams =  LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        spinner_tags.layoutParams = spinner_tagsParams
        val items_spinTag = resources.getStringArray(R.array.tags_sort)
        val adapter_spinTag = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items_spinTag)

        spinner_tags.adapter = adapter_spinTag
        linearLayout.addView(spinner_tags)
        var dublikat_pervoe = true
        var dublikat_vtoroe = true
        var dublikat_decert = true
        var dublikat_zakuska = true
        var dublikat_napitok = true
        var dublikat_zavtrak = true



        spinner_tags.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                // Обработайте выбор элемента
                val selectedItem_tags = items_spinTag[position]
                // код обработки выбора элемента здесь
                when(selectedItem_tags)
                {
                    "Выберите фильтр тэга"->
                    {

                    }
                    "Первое блюдо"->
                    {
                        spinner_tags.setSelection(0)

                        if (dublikat_pervoe != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "Первое блюдо"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_pervoe = true
                                chipGroup.removeView(chip) // убираем чип из родительского макета
                                ChipGroupList -= "Первое блюдо"
                                formStyleString(ChipGroupList)

                            }
                            chip.text = "Первое блюдо"
                            chipGroup.addView(chip)
                            dublikat_pervoe = false
                        }
                    }
                    "Второе блюдо"->
                    {
                        spinner_tags.setSelection(0)

                        if (dublikat_vtoroe != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "Второе блюдо"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_vtoroe = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "Второе блюдо"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "Второе блюдо"
                            chipGroup.addView(chip)
                            dublikat_vtoroe = false
                        }
                    }

                    "Завтрак"->
                    {
                        spinner_tags.setSelection(0)

                        if (dublikat_zavtrak != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "Завтрак"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_zavtrak = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "Завтрак"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "Завтрак"
                            chipGroup.addView(chip)
                            dublikat_zavtrak = false
                        }
                    }
                    "Напитки"->
                    {
                        spinner_tags.setSelection(0)

                        if (dublikat_napitok != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "Напитки"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_napitok = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "Напитки"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "Напитки"
                            chipGroup.addView(chip)
                            dublikat_napitok = false
                        }
                    }
                    "Десерт"->
                    {
                        spinner_tags.setSelection(0)

                        if (dublikat_decert != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "Десерт"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_decert = true
                                chipGroup.removeView(chip)
                                ChipGroupList -= "Десерт"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "Десерт"
                            chipGroup.addView(chip)
                            dublikat_decert = false
                        }
                    }
                    "Закуска"->
                    {
                        spinner_tags.setSelection(0)

                        if (dublikat_zakuska != false)
                        {
                            val chip = Chip(requireContext())
                            val chipParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            chip.layoutParams = chipParams
                            chip.isCloseIconVisible = true
                            ChipGroupList += "Закуска"
                            ChipGroupList.sorted()
                            formStyleString(ChipGroupList)
                            chip.setOnCloseIconClickListener {
                                // обработка закрытия чипа
                                dublikat_zakuska = true
                                chipGroup.removeView(chip) // убираем чип из родительского макета
                                ChipGroupList -= "Закуска"
                                formStyleString(ChipGroupList)
                            }
                            chip.text = "Закуска"
                            chipGroup.addView(chip)
                            dublikat_zakuska = false
                        }
                    }


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Действие при отсутствии выбора элемента
            }
        }

        val spinner_all = Spinner(requireContext())
        val spinner_allParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        spinner_all.layoutParams = spinner_allParams
        val items_spinAll = resources.getStringArray(R.array.all_sort)

        val adapter_spinAll = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items_spinAll)
        spinner_all.adapter = adapter_spinAll
        linearLayout.addView(spinner_all)




        fun scrollDishes(dishes:List<Dish>)
        {
            for (dish in dishes)
            {
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
                val params1 = LinearLayout.LayoutParams(dpToPx(140F),
                    dpToPx(133F))
                //params1.setMargins(0, 30, 0, 30)
                //dpToPxparams1.gravity = Gravity.CENTER
                imageView1.layoutParams = params1
                // Преобразование строки base64 в байты
                val imageBytes = android.util.Base64.decode(dish.photo, android.util.Base64.DEFAULT)
            // Преобразование массива байтов в изображение
                val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageView1.setImageBitmap(bitmap)
                linearLayoutHor.addView(imageView1)

                val textViewNazvanie = TextView(requireContext())
                textViewNazvanie.setTypeface(null, Typeface.BOLD)
                textViewNazvanie.textSize = 16f
                textViewNazvanie.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                textViewNazvanie.setText(dish.nameDish)
                val textViewRating = TextView(requireContext())
                textViewRating.text = "Рейтинг: ${dish.rating}"
                textViewRating.setTypeface(null, Typeface.BOLD)


                val textView2 = TextView(requireContext())
                textView2.text = "Тэгги:"
                textView2.setTypeface(null, Typeface.BOLD)

                val textViewZhanr = TextView(requireContext())
                textViewZhanr.text = dish.tags
                val textViewParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textViewNazvanie.layoutParams = textViewParams
                textViewRating.layoutParams = textViewParams

                textView2.layoutParams = textViewParams
                textViewZhanr.layoutParams = textViewParams

                textViewNazvanie.setOnClickListener{
                    DataHolder.dish = dish
                    findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
                }

                imageView1.setOnClickListener{
                    DataHolder.dish = dish
                    findNavController().navigate(R.id.action_homeFragment_to_secondFragment)
                }

                // Получить текст из TextView
                val text = textViewZhanr.text.toString()


                val words = text.split(", ")



                val spannableBuilder = SpannableStringBuilder()




                for (word in words)
                {
                    val clickableSpan = object : ClickableSpan()
                    {
                        override fun onClick(view: View)
                        {
                            when(word)
                            {
                                "Первое блюдо"->
                                {
                                    spinner_tags.setSelection(0)

                                    if (dublikat_pervoe != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "Первое блюдо"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_pervoe = true
                                            chipGroup.removeView(chip) // убираем чип из родительского макета
                                            ChipGroupList -= "Первое блюдо"
                                            formStyleString(ChipGroupList)

                                        }
                                        chip.text = "Первое блюдо"
                                        chipGroup.addView(chip)
                                        dublikat_pervoe = false
                                    }
                                }
                                "Второе блюдо"->
                                {
                                    spinner_tags.setSelection(0)

                                    if (dublikat_vtoroe != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "Второе блюдо"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_vtoroe = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "Второе блюдо"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "Второе блюдо"
                                        chipGroup.addView(chip)
                                        dublikat_vtoroe = false
                                    }
                                }

                                "Завтрак"->
                                {
                                    spinner_tags.setSelection(0)

                                    if (dublikat_zavtrak != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "Завтрак"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_zavtrak = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "Завтрак"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "Завтрак"
                                        chipGroup.addView(chip)
                                        dublikat_zavtrak = false
                                    }
                                }
                                "Напитки"->
                                {
                                    spinner_tags.setSelection(0)

                                    if (dublikat_napitok != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "Напитки"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_napitok = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "Напитки"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "Напитки"
                                        chipGroup.addView(chip)
                                        dublikat_napitok = false
                                    }
                                }
                                "Десерт"->
                                {
                                    spinner_tags.setSelection(0)

                                    if (dublikat_decert != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "Десерт"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_decert = true
                                            chipGroup.removeView(chip)
                                            ChipGroupList -= "Десерт"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "Десерт"
                                        chipGroup.addView(chip)
                                        dublikat_decert = false
                                    }
                                }
                                "Закуска"->
                                {
                                    spinner_tags.setSelection(0)

                                    if (dublikat_zakuska != false)
                                    {
                                        val chip = Chip(requireContext())
                                        val chipParams = LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        chip.layoutParams = chipParams
                                        chip.isCloseIconVisible = true
                                        ChipGroupList += "Закуска"
                                        ChipGroupList.sorted()
                                        formStyleString(ChipGroupList)
                                        chip.setOnCloseIconClickListener {
                                            // обработка закрытия чипа
                                            dublikat_zakuska = true
                                            chipGroup.removeView(chip) // убираем чип из родительского макета
                                            ChipGroupList -= "Закуска"
                                            formStyleString(ChipGroupList)
                                        }
                                        chip.text = "Закуска"
                                        chipGroup.addView(chip)
                                        dublikat_zakuska = false
                                    }
                                }


                            }
                        }

                        override fun updateDrawState(ds: TextPaint)
                        {
                            // Опционально: настройка стиля отображения текста в нажимном элементе
                            ds.isUnderlineText = true // Убраванить подчеркие
                            ds.color = Color.BLUE // Изменить цвет текста на синий
                            // Дополнительные настройки, если необходимо
                        }
                    }

                    spannableBuilder.append(word)
                    spannableBuilder.append(" ")
                    spannableBuilder.setSpan(clickableSpan, spannableBuilder.length - word.length - 1, spannableBuilder.length - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                textViewZhanr.text = spannableBuilder


                textViewZhanr.movementMethod = LinkMovementMethod.getInstance()

                linearLayoutHor_vert.addView(textViewNazvanie)
                linearLayoutHor_vert.addView(textViewRating)

                linearLayoutHor_vert.addView(textView2)
                linearLayoutHor_vert.addView(textViewZhanr)
                linearLayoutHor.addView(linearLayoutHor_vert)



                linearLayout.addView(linearLayoutHor)

//                if(dish == dishes.last())
//                {
//                    val button_podgruz = Button(requireContext())
//                    val button_podgruzParams = LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//                    button_podgruz.layoutParams = button_podgruzParams
//                    button_podgruz.text = "Показать ещё"
//                    linearLayout.addView(button_podgruz)
//
//                }


                deleteDishArray += linearLayoutHor

            }


        }


        //*********************************************************************
        fun delete_scrollDishes(films:List<LinearLayout>)
        {
            for(DelDish in deleteDishArray)
            {
                //DelFilm.removeView(linearLayout)
                DelDish?.let {
                    linearLayout.removeView(it)
                }
            }
//            if(deleteDishArray.isNotEmpty())
//            {
//                val lastView = linearLayout.getChildAt(linearLayout.childCount-1)
//                linearLayout.removeView(lastView)
//            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun handleResponse(response: String)
        {
            val gson = Gson()
            val dishListType = object : TypeToken<List<Dish>>() {}.type
            val dishesToAdd: List<Dish> = gson.fromJson(response, dishListType)
            dishes .addAll(dishesToAdd)
            scrollDishes(dishes)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        executeGetRequest= {urlGet ->

            delete_scrollDishes(deleteDishArray)
            dishes.clear()

            GlobalScope.launch(Dispatchers.Main)
            {
                try {
                    val response: String = client.get(urlGet)
                    handleResponse(response)
                } catch (e: Exception) {
                    println("Ошибка при выполнении запроса: ${e.message}")
                }
            }
        }

        editText.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2

            if (event.action == MotionEvent.ACTION_UP)
            {
                if (event.rawX >= (editText.right - editText.compoundDrawables[DRAWABLE_RIGHT].bounds.width()))
                {
                    // Обработка нажатия на кнопку поиска
                    delete_scrollDishes(deleteDishArray)
                    dishes.clear()
                    chipGroup.removeAllViews()
                    GlobalScope.launch(Dispatchers.Main)
                    {
                        executeGetRequest("${URL.URL}/findDishByName?name=${editText.text}")
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        spinner_all.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                // Обработайте выбор элемента
                val selectedItem_all = items_spinAll[position]
                // код обработки выбора элемента здесь
                when(selectedItem_all)
                {
                    "рейтинг(по убыванию)"->
                    {
                        executeGetRequest("${URL.URL}/getAllDishesRatingDESC?offset=0")
                    }
                    "рейтинг(по возрастанию)"->
                    {
                        executeGetRequest("${URL.URL}/getAllDishesRatingASC?offset=0")
                    }

                    "название(по алфавиту)"->
                    {
                        executeGetRequest("${URL.URL}/getAllDishesNameDESС?offset=0")
                    }
                    "название(в обратном порядке)"->
                    {
                        executeGetRequest("${URL.URL}/getAllDishesNameASС?offset=0")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Действие при отсутствии выбора элемента
            }
        }

        val linearLayout1 =  binding.root.findViewById<LinearLayout>(R.id.rootContainer)
        linearLayout1?.addView(scrollView)

        return root
    }

    fun formStyleString(ChipGroupList:MutableList<String>)
    {
        if(ChipGroupList.isEmpty())
        {
            executeGetRequest("${URL.URL}/getAllDishesRatingDESC?offset=0")
        }
        else{
            var StyleString = ""
            for(style in ChipGroupList)
            {
                StyleString += "${style},"
            }

            executeGetRequest("${URL.URL}/findDishesByTag?tag=${StyleString}")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
