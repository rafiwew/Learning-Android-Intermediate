package com.piwew.storyapp.ui.story

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.piwew.storyapp.R
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.databinding.ActivityAddStoryBinding
import com.piwew.storyapp.helper.getImageUri
import com.piwew.storyapp.helper.reduceFileImage
import com.piwew.storyapp.helper.uriToFile
import com.piwew.storyapp.ui.ViewModelFactory
import com.piwew.storyapp.ui.main.MainActivity
import com.piwew.storyapp.ui.story.CameraActivity.Companion.CAMERAX_RESULT

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast(getString(R.string.granted), Toast.LENGTH_LONG)
            } else {
                showToast(getString(R.string.denied), Toast.LENGTH_LONG)
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.ivActionBack.setOnClickListener {
            onSupportNavigateUp()
        }

        binding.galleryButton.setOnClickListener { startGalleryOrOpenDocument() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun startGalleryOrOpenDocument() {
        if (isPhotoPickerAvailable()) {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            showToast(getString(R.string.photo_picker_not_available), Toast.LENGTH_LONG)
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
            showToast(getString(R.string.photo_picker), Toast.LENGTH_SHORT)
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
                showToast(getString(R.string.open_document), Toast.LENGTH_SHORT)
            }
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.edAddDescription.text.toString()

            showLoading(true)

            viewModel.uploadStory(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showToast(result.error, Toast.LENGTH_LONG)
                            showLoading(false)
                        }
                    }
                }
            }

        } ?: showToast(getString(R.string.empty_image_warning), Toast.LENGTH_SHORT)
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun showToast(message: String, length: Int) {
        Toast.makeText(this, message, length).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}