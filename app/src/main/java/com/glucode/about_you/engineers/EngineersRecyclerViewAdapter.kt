package com.glucode.about_you.engineers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.glucode.about_you.databinding.ItemEngineerBinding
import com.glucode.about_you.engineers.models.Engineer
import com.squareup.picasso.Picasso

class EngineersRecyclerViewAdapter(
    private var engineers: List<Engineer>,
    private val onClick: (Engineer) -> Unit

) : RecyclerView.Adapter<EngineersRecyclerViewAdapter.EngineerViewHolder>() {

    override fun getItemCount() = engineers.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineerViewHolder {
        return EngineerViewHolder(
            ItemEngineerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EngineerViewHolder, position: Int) {
        holder.bind(engineers[position], onClick)
    }

    inner class EngineerViewHolder(private val binding: ItemEngineerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val profileImage: ImageView = binding.profileImage

        init {
            binding.profileImage.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val engineer = engineers[position]

                }
            }
        }

        fun bind(engineer: Engineer, onClick: (Engineer) -> Unit) {
            binding.name.text = engineer.name
            binding.role.text = engineer.role
            binding.root.setOnClickListener {
                onClick(engineer)
            }


            //TODO - set profile picture

            binding.profileImage.setOnClickListener {
                if (engineer.defaultImageName.isNullOrEmpty()) {
                    openGallery(binding.profileImage, engineer)
                }
            }
//            statusIcon.setDrawable(item.icon)
            engineer.defaultImageName?.let { imageView ->
                if (imageView.isNotEmpty()) {
                    Picasso.get().load(imageView).into(binding.profileImage)
                }

            }
        }
        private val REQUEST_CODE_GALLERY = 1001

        private fun openGallery(imageView: ImageView, engineer: Engineer) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            (imageView.context as? Activity)?.let { activity ->
                activity.startActivityForResult(intent, REQUEST_CODE_GALLERY)
            }
            }
        }


    }



