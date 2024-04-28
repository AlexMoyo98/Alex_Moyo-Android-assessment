package com.glucode.about_you.about.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.ktx.R
import org.w3c.dom.Attr
import java.time.format.DecimalStyle

class ProfileCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatImageView(context,attrs,defStyleAttr){
    private var selectedImageUri: Uri? = null
    private var engineerLayoutId: Int = -1


    private fun openImagePicker(){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val chooseIntent = Intent.createChooser(intent,"Select Image")
        (context as Activity).startActivityForResult(chooseIntent,REQUEST_IMAGE_PICKER)
    }

    fun setImageFromUri(uri: Uri){
        selectedImageUri = uri
        setImageURI(uri)
    }

    fun getSelectedImageUri():Uri?{
        return selectedImageUri
    }

    companion object{
        private const val REQUEST_IMAGE_PICKER = 123
    }
}