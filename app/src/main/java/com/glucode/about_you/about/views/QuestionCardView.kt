package com.glucode.about_you.about.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.glucode.about_you.R
import com.glucode.about_you.databinding.ViewQuestionCardBinding

class QuestionCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    private val binding: ViewQuestionCardBinding =
        ViewQuestionCardBinding.inflate(LayoutInflater.from(context), this)

    var title: String? = null
        set(value) {
            field = value
            binding.title.text = value
        }

    var answers: List<String> = listOf()
        set(value) {
            field = value
            binding.answers.removeAllViews()
            value.forEach { answer ->
                addAnswer(answer)
            }
        }

    var selection: Int? = null
        set(value) {
            field = value
            value ?: return
            binding.answers.children.forEachIndexed{index, view ->
                view.isSelected = (index == value)
                if (index == value){
                    view.post{setAnswerVisibilityAndColor(view,index == value)}
                }
                setSelection()
            }
        }

    init {
        radius = resources.getDimension(R.dimen.corner_radius_normal)
        elevation = resources.getDimension(R.dimen.elevation_normal)
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.black))
        setAllTextVisible()
    }

    private fun addAnswer(title: String) {
        val answerView = AnswerCardView(context)
        answerView.title = title
        answerView.setOnClickListener { onAnswerClick(it) }
        binding.answers.addView(answerView)
    }

    private fun onAnswerClick(view: View) {
        if (!view.isSelected) {
            binding.answers.children.forEachIndexed { index, childView ->
                childView.isSelected = (childView == view)
                setAnswerVisibilityAndColor(childView, childView == view)
            }
        }
       // view.isSelected = true
       // setAnswerVisibilityAndColor(view,true)

    }
    private fun setAllTextVisible(){
        binding.answers.children.forEach { view ->
            setAnswerVisibilityAndColor(view, true)
        }
    }

    private fun setAnswerVisibilityAndColor(view: View, isSelected: Boolean){
        view.visibility = View.VISIBLE
        if (view is TextView){
            val textColor = if (isSelected){
                ContextCompat.getColor(context,R.color.white)
            } else {
                ContextCompat.getColor(context,R.color.black)
            }
            val backgroundColor = if (isSelected){
                ContextCompat.getColor(context, R.color.text_grey)
            } else {
                ContextCompat.getColor(context,android.R.color.transparent)
            }
            view.setTextColor(textColor)
            view.setBackgroundColor(backgroundColor)
        }
    }

    private fun setSelection() {
        val selectedView = binding.answers.getChildAt(selection ?: return)
        selectedView.isSelected = true
        setAnswerVisibilityAndColor(selectedView,true)
    }

}