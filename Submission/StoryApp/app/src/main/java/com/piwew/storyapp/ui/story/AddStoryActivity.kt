package com.piwew.storyapp.ui.story

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import com.piwew.storyapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    private var currentImageUri: Uri? = null

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGalleryOrOpenDocument() }
        binding.cameraButton.setOnClickListener { }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun startGalleryOrOpenDocument() {
        if (!isPhotoPickerAvailable()) {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            Toast.makeText(
                this,
                "Photo Picker is not available on this device.",
                Toast.LENGTH_LONG
            ).show()
            openDocumentPicker()
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun isPhotoPickerAvailable(): Boolean {
        val photoPickerIntent = Intent(MediaStore.ACTION_PICK_IMAGES)
        val packageManager = packageManager
        return photoPickerIntent.resolveActivity(packageManager) != null
    }

    private fun openDocumentPicker() {
        val openDocumentIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        openDocumentIntent.type = "image/*"
        openDocumentIntent.addCategory(Intent.CATEGORY_OPENABLE)
        launcherOpenDocument.launch(openDocumentIntent)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "Photo Picker: No media selected", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherOpenDocument = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Toast.makeText(this, "Open Document: No media selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.previewImageView.setImageURI(uri)
        }
    }
}