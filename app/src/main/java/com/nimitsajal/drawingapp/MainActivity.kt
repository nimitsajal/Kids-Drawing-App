package com.nimitsajal.drawingapp

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.ads.MobileAds
import com.nimitsajal.drawingapp.databinding.ActivityMainBinding
import com.nimitsajal.drawingapp.databinding.DialogBackgroundColorBinding
import com.nimitsajal.drawingapp.databinding.DialogBrushSizeBinding
import com.nimitsajal.drawingapp.databinding.DialogColorsBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    var eraserColor: String = "BlackLight"
    var brushColor: String = "BlackDark"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("OpenAd", "MainActivity class onCreate started")

        MobileAds.initialize(this) {}

        binding.drawingView.setBrushSize(8.toFloat())

        binding.ibBrush.setOnClickListener{
            showBrushSizeChooserDialog()
        }

        binding.ibEraser.setOnClickListener{
            when(eraserColor){
                "BlueDark" -> binding.drawingView.setBrushColor("#00008b")
                "BlueLight" -> binding.drawingView.setBrushColor("#add8e6")
                "BlackDark" -> binding.drawingView.setBrushColor("#000000")
                "BlackLight" -> binding.drawingView.setBrushColor("#ffffff")
                "RedDark" -> binding.drawingView.setBrushColor("#8b0000")
                "RedLight" -> binding.drawingView.setBrushColor("#ff0000")
                "GreenDark" -> binding.drawingView.setBrushColor("#013220")
                "GreenLight" -> binding.drawingView.setBrushColor("#90ee90")
                "PinkDark" -> binding.drawingView.setBrushColor("#d11d53")
                "PinkLight" -> binding.drawingView.setBrushColor("#ffb6c1")
                "PurpleDark" -> binding.drawingView.setBrushColor("#800080")
                "PurpleLight" -> binding.drawingView.setBrushColor("#cd00cd")
                "YellowDark" -> binding.drawingView.setBrushColor("#ff9500")
                "YellowLight" -> binding.drawingView.setBrushColor("#fff44f")
                "OrangeDark" -> binding.drawingView.setBrushColor("#ff4c00")
                "OrangeLight" -> binding.drawingView.setBrushColor("#ff9b1a")
                "GreyDark" -> binding.drawingView.setBrushColor("#2f4f4f")
                "GreyLight" -> binding.drawingView.setBrushColor("#d3d3d3")
            }
        }

        binding.ibPencil.setOnClickListener{
            when(brushColor){
                "BlueDark" -> binding.drawingView.setBrushColor("#00008b")
                "BlueLight" -> binding.drawingView.setBrushColor("#add8e6")
                "BlackDark" -> binding.drawingView.setBrushColor("#000000")
                "BlackLight" -> binding.drawingView.setBrushColor("#ffffff")
                "RedDark" -> binding.drawingView.setBrushColor("#8b0000")
                "RedLight" -> binding.drawingView.setBrushColor("#ff0000")
                "GreenDark" -> binding.drawingView.setBrushColor("#013220")
                "GreenLight" -> binding.drawingView.setBrushColor("#90ee90")
                "PinkDark" -> binding.drawingView.setBrushColor("#d11d53")
                "PinkLight" -> binding.drawingView.setBrushColor("#ffb6c1")
                "PurpleDark" -> binding.drawingView.setBrushColor("#800080")
                "PurpleLight" -> binding.drawingView.setBrushColor("#cd00cd")
                "YellowDark" -> binding.drawingView.setBrushColor("#ff9500")
                "YellowLight" -> binding.drawingView.setBrushColor("#fff44f")
                "OrangeDark" -> binding.drawingView.setBrushColor("#ff4c00")
                "OrangeLight" -> binding.drawingView.setBrushColor("#ff9b1a")
                "GreyDark" -> binding.drawingView.setBrushColor("#2f4f4f")
                "GreyLight" -> binding.drawingView.setBrushColor("#d3d3d3")
            }
        }

        binding.ibImport.setOnClickListener{
//            if(isReadStorageAllowed()){
//                val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(pickPhotoIntent, GALLERY)
//            }
//            else{
//                requestStoragePermission()
//            }
            //ivBackground.setBackgroundColor(Color.parseColor("#add8e6"))
            showBackgroundChooserDialog()
        }

        binding.ibUndo.setOnClickListener{
            binding.drawingView.onClickUndo()
        }

        binding.ibRedo.setOnClickListener{
            binding.drawingView.onClickRedo()
        }

        binding.ibEraseAll.setOnClickListener{
            binding.drawingView.onClickClear()
            binding.ivBackground.setBackgroundColor(Color.parseColor("#ffffff"))
            eraserColor = "BlackLight"
            brushColor = "BlackDark"
        }

        binding.ibShare.setOnClickListener{
            if(isReadStorageAllowed()){
                shareImage(getBitmapFromView(binding.drawingView))
            }
            else{
                requestStoragePermission()
            }
        }

        binding.ibSave.setOnClickListener{
            if(isReadStorageAllowed()){
                saveImageToStorage(getBitmapFromView(binding.drawingView), StringBuilder(""))
            }
            else{
                requestStoragePermission()
            }
        }

        binding.ibColor.setOnClickListener{
            showColorChooserDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY){
              try{
                  if(data!!.data != null){
                      binding.ivBackground.visibility = View.VISIBLE
                      binding.ivBackground.setImageURI(data.data)
                  }
                  else{
                      Toast.makeText(this, "Error in importing the image.", Toast.LENGTH_SHORT).show()
                  }
              }
              catch (e: Exception){
                e.printStackTrace()
              }
            }
        }
    }

    fun showBackgroundChooserDialog(){
        val backgroundColorDialog = Dialog(this)
        val dialogBinding = DialogBackgroundColorBinding.inflate(layoutInflater)
        backgroundColorDialog.setContentView(dialogBinding.root)
        backgroundColorDialog.setTitle("Brush Size: ")

        val blueDark = dialogBinding.btnColorBlueDark
        val blueLight = dialogBinding.btnColorBlueLight
        val blackDark = dialogBinding.btnColorBlackDark
        val blackLight = dialogBinding.btnColorBlackLight
        val redDark = dialogBinding.btnColorRedDark
        val redLight = dialogBinding.btnColorRedLight
        val greenDark = dialogBinding.btnColorGreenDark
        val greenLight = dialogBinding.btnColorGreenLight
        val pinkDark = dialogBinding.btnColorPinkDark
        val pinkLight = dialogBinding.btnColorPinkLight
        val purpleDark = dialogBinding.btnColorPurpleDark
        val purpleLight = dialogBinding.btnColorPurpleLight
        val yellowDark = dialogBinding.btnColorYellowDark
        val yellowLight = dialogBinding.btnColorYellowLight
        val orangeDark = dialogBinding.btnColorOrangeDark
        val orangeLight = dialogBinding.btnColorOrangeLight
        val greyDark = dialogBinding.btnColorGreyDark
        val greyLight = dialogBinding.btnColorGreyLight

        blueDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#00008b"))
            eraserColor = "BlueDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        blueLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#add8e6"))
            eraserColor = "BlueLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        blackDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#000000"))
            eraserColor = "BlackDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        blackLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#ffffff"))
            eraserColor = "BlackLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        redDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#8b0000"))
            eraserColor = "RedDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        redLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#ff0000"))
            eraserColor = "RedLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greenDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#013220"))
            eraserColor = "GreenDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greenLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#90ee90"))
            eraserColor = "#GreenLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        pinkDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#d11d53"))
            eraserColor = "PinkDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        pinkLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#ffb6c1"))
            eraserColor = "PinkLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        purpleDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#800080"))
            eraserColor = "PurpleDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        purpleLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#cd00cd"))
            eraserColor = "PurpleLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        yellowDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#ff9500"))
            eraserColor = "YellowDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        yellowLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#fff44f"))
            eraserColor = "YellowLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        orangeDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#ff4c00"))
            eraserColor = "OrangeDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        orangeLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#ff9b1a"))
            eraserColor = "OrangeLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greyDark.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#2f4f4f"))
            eraserColor = "GreyDark"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greyLight.setOnClickListener{
            binding.ivBackground.setBackgroundColor(Color.parseColor("#d3d3d3"))
            eraserColor = "GreyLight"
            binding.drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        backgroundColorDialog.show()
    }

    fun showColorChooserDialog(){
        val colorDialog = Dialog(this)
        val dialogBinding = DialogColorsBinding.inflate(layoutInflater)
        colorDialog.setContentView(dialogBinding.root)
        colorDialog.setTitle("Colors: ")

        val blueDark = dialogBinding.btnColorBlueDark
        val blueLight = dialogBinding.btnColorBlueLight
        val blackDark = dialogBinding.btnColorBlackDark
        val blackLight = dialogBinding.btnColorBlackLight
        val redDark = dialogBinding.btnColorRedDark
        val redLight = dialogBinding.btnColorRedLight
        val greenDark = dialogBinding.btnColorGreenDark
        val greenLight = dialogBinding.btnColorGreenLight
        val pinkDark = dialogBinding.btnColorPinkDark
        val pinkLight = dialogBinding.btnColorPinkLight
        val purpleDark = dialogBinding.btnColorPurpleDark
        val purpleLight = dialogBinding.btnColorPurpleLight
        val yellowDark = dialogBinding.btnColorYellowDark
        val yellowLight = dialogBinding.btnColorYellowLight
        val orangeDark = dialogBinding.btnColorOrangeDark
        val orangeLight = dialogBinding.btnColorOrangeLight
        val greyDark = dialogBinding.btnColorGreyDark
        val greyLight = dialogBinding.btnColorGreyLight

        blueDark.setOnClickListener{
            binding.drawingView.setBrushColor("#00008b")
            brushColor = "BlueDark"
            colorDialog.dismiss()
        }
        blueLight.setOnClickListener{
            binding.drawingView.setBrushColor("#add8e6")
            brushColor = "BlueLight"
            colorDialog.dismiss()
        }
        blackDark.setOnClickListener{
            binding.drawingView.setBrushColor("#000000")
            brushColor = "BlackDark"
            colorDialog.dismiss()
        }
        blackLight.setOnClickListener{
            binding.drawingView.setBrushColor("#ffffff")
            brushColor = "BlackLight"
            colorDialog.dismiss()
        }
        redDark.setOnClickListener{
            binding.drawingView.setBrushColor("#8b0000")
            brushColor = "RedDark"
            colorDialog.dismiss()
        }
        redLight.setOnClickListener{
            binding.drawingView.setBrushColor("#ff0000")
            brushColor = "RedLight"
            colorDialog.dismiss()
        }
        greenDark.setOnClickListener{
            binding.drawingView.setBrushColor("#013220")
            brushColor = "GreenDark"
            colorDialog.dismiss()
        }
        greenLight.setOnClickListener{
            binding.drawingView.setBrushColor("#90ee90")
            brushColor = "GreenLight"
            colorDialog.dismiss()
        }
        pinkDark.setOnClickListener{
            binding.drawingView.setBrushColor("#d11d53")
            brushColor = "PinkDark"
            colorDialog.dismiss()
        }
        pinkLight.setOnClickListener{
            binding.drawingView.setBrushColor("#ffb6c1")
            brushColor = "PinkLight"
            colorDialog.dismiss()
        }
        purpleDark.setOnClickListener{
            binding.drawingView.setBrushColor("#800080")
            brushColor = "PurpleDark"
            colorDialog.dismiss()
        }
        purpleLight.setOnClickListener{
            binding.drawingView.setBrushColor("#cd00cd")
            brushColor = "PurpleLight"
            colorDialog.dismiss()
        }
        yellowDark.setOnClickListener{
            binding.drawingView.setBrushColor("#ff9500")
            brushColor = "YellowDark"
            colorDialog.dismiss()
        }
        yellowLight.setOnClickListener{
            binding.drawingView.setBrushColor("#fff44f")
            brushColor = "YellowLight"
            colorDialog.dismiss()
        }
        orangeDark.setOnClickListener{
            binding.drawingView.setBrushColor("#ff4c00")
            brushColor = "OrangeDark"
            colorDialog.dismiss()
        }
        orangeLight.setOnClickListener{
            binding.drawingView.setBrushColor("#ff9b1a")
            brushColor = "OrangeLight"
            colorDialog.dismiss()
        }
        greyDark.setOnClickListener{
            binding.drawingView.setBrushColor("#2f4f4f")
            brushColor = "GreyDark"
            colorDialog.dismiss()
        }
        greyLight.setOnClickListener{
            binding.drawingView.setBrushColor("#d3d3d3")
            brushColor = "GreyLight"
            colorDialog.dismiss()
        }
        colorDialog.show()
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        val dialogBinding = DialogBrushSizeBinding.inflate(layoutInflater)
        brushDialog.setContentView(dialogBinding.root)
        brushDialog.setTitle("Brush Size: ")
        val extraSmallBtn = dialogBinding.ibExtraSmallBrushSize
        val smallBtn = dialogBinding.ibSmallBrushSize
        val mediumBtn = dialogBinding.ibMediumBrushSize
        val largeBtn = dialogBinding.ibLargeBrushSize
        val extraLargeBtn = dialogBinding.ibExtraLargeBrushSize
        extraSmallBtn.setOnClickListener{
            binding.drawingView.setBrushSize(2.toFloat())
            brushDialog.dismiss()
        }
        smallBtn.setOnClickListener{
            binding.drawingView.setBrushSize(6.toFloat())
            brushDialog.dismiss()
        }
        mediumBtn.setOnClickListener{
            binding.drawingView.setBrushSize(14.toFloat())
            brushDialog.dismiss()
        }
        largeBtn.setOnClickListener{
            binding.drawingView.setBrushSize(24.toFloat())
            brushDialog.dismiss()
        }
        extraLargeBtn.setOnClickListener{
            binding.drawingView.setBrushSize(32.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

//    private fun showEraserSizeChooserDialog(){
//        val eraserDialog = Dialog(this)
//        eraserDialog.setContentView(R.layout.dialog_eraser_size)
//        eraserDialog.setTitle("Eraser Size: ")
//        val smallBtn = eraserDialog.ibSmallBrushSize
//        val mediumBtn = eraserDialog.ibMediumBrushSize
//        val largeBtn = eraserDialog.ibLargeBrushSize
//        smallBtn.setOnClickListener{
//            drawingView.setBrushSize(6.toFloat())
//            eraserDialog.dismiss()
//        }
//        mediumBtn.setOnClickListener{
//            drawingView.setBrushSize(14.toFloat())
//            eraserDialog.dismiss()
//        }
//        largeBtn.setOnClickListener{
//            drawingView.setBrushSize(24.toFloat())
//            eraserDialog.dismiss()
//        }
//        eraserDialog.show()
//    }

    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())){
            Toast.makeText(this, "Need permission to add a Background Image.", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Storage permission granted.", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Storage permission denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isReadStorageAllowed(): Boolean{
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

//    private fun setDefaultColorButtons(){
//        btnColorBlue.background = ContextCompat.getDrawable(this, R.drawable.color_blue)
//        btnColorBlack.background = ContextCompat.getDrawable(this, R.drawable.color_black_dark)
//        btnColorPink.background = ContextCompat.getDrawable(this, R.drawable.color_pink)
//        btnColorPurple.background = ContextCompat.getDrawable(this, R.drawable.color_purple)
//        btnColorRed.background = ContextCompat.getDrawable(this, R.drawable.color_red)
//        btnColorYellow.background = ContextCompat.getDrawable(this, R.drawable.color_yellow)
//        btnColorGreen.background = ContextCompat.getDrawable(this, R.drawable.color_green)
//        btnColorTeal.background = ContextCompat.getDrawable(this, R.drawable.color_teal)
//        btnColorWhite.background = ContextCompat.getDrawable(this, R.drawable.color_white)
//    }

//    private fun setCanvasColor(view: View){
//        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(returnedBitmap)
//        canvas.drawColor(Color.WHITE)
//        view.draw(canvas)
//    }

    private fun getBitmapFromView(view: View):Bitmap{
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
//        val bgDrawable = view.background
//        if(bgDrawable != null){
//            bgDrawable.draw(canvas)
//        }
//        else{
//            canvas.drawColor(Color.WHITE)
//            }
        when(eraserColor){
            "BlueDark" -> canvas.drawColor(Color.parseColor("#00008b"))
            "BlueLight" -> canvas.drawColor(Color.parseColor("#add8e6"))
            "BlackDark" -> canvas.drawColor(Color.parseColor("#000000"))
            "BlackLight" -> canvas.drawColor(Color.parseColor("#ffffff"))
            "RedDark" -> canvas.drawColor(Color.parseColor("#8b0000"))
            "RedLight" -> canvas.drawColor(Color.parseColor("#ff0000"))
            "GreenDark" -> canvas.drawColor(Color.parseColor("#013220"))
            "GreenLight" -> canvas.drawColor(Color.parseColor("#90ee90"))
            "PinkDark" -> canvas.drawColor(Color.parseColor("#d11d53"))
            "PinkLight" -> canvas.drawColor(Color.parseColor("#ffb6c1"))
            "PurpleDark" -> canvas.drawColor(Color.parseColor("#800080"))
            "PurpleLight" -> canvas.drawColor(Color.parseColor("#cd00cd"))
            "YellowDark" -> canvas.drawColor(Color.parseColor("#ff9500"))
            "YellowLight" -> canvas.drawColor(Color.parseColor("#fff44f"))
            "OrangeDark" -> canvas.drawColor(Color.parseColor("#ff4c00"))
            "OrangeLight" -> canvas.drawColor(Color.parseColor("#ff9b1a"))
            "GreyDark" -> canvas.drawColor(Color.parseColor("#2f4f4f"))
            "GreyLight" -> canvas.drawColor(Color.parseColor("#d3d3d3"))
        }
        //canvas.drawColor(Color.CYAN)
        view.draw(canvas)

        return returnedBitmap
    }

//    private fun saveImageToStorage(mBitmap: Bitmap){
//        val externalStorage = Environment.getExternalStorageState()
//        if(externalStorage == Environment.MEDIA_MOUNTED){
//            val storageDirectory = Environment.getExternalStorageDirectory().absolutePath
//            val dir = File("${storageDirectory}/DrawingApp")
//            val created = dir.mkdirs()
//            //Toast.makeText(this, "$created", Toast.LENGTH_SHORT).show()
//            val filename = String.format("%d.png", System.currentTimeMillis())
//            val outFile = File(dir.toString() + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
//            //Toast.makeText(this, "Image location -> ${Uri.parse(outFile.absolutePath)}", Toast.LENGTH_SHORT).show()
//            //val file = File(storageDirectory + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
//            try{
//                val stream:OutputStream = FileOutputStream(outFile)
//                //Toast.makeText(this, "reached till here", Toast.LENGTH_SHORT).show()
//                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//                stream.flush()
//                stream.close()
//                Toast.makeText(this, "Image saved successfully at ${Uri.parse(outFile.absolutePath)}", Toast.LENGTH_SHORT).show()
//            }
//            catch(e:java.lang.Exception){
//                e.printStackTrace()
//                Toast.makeText(this, "There is some problem", Toast.LENGTH_SHORT).show()
//            }
//        }
//        else{
//            Toast.makeText(this, "Unable, to access storage", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun saveImageToStorage(mBitmap: Bitmap, result: StringBuilder) {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null) {
            val timestamp = System.currentTimeMillis()
            val fileName = "DrawingApp_$timestamp.jpg"
            val file = File(storageDir, fileName)

            try {
                val stream: OutputStream = FileOutputStream(file)
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                result.clear()
                result.append(file.absolutePath)
                Toast.makeText(this, "Image saved successfully at ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "There is some problem", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "External storage is not available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareImage(mBitmap: Bitmap) {
        val uri: Uri = generateImageUri(mBitmap)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/jpeg"
        startActivity(Intent.createChooser(shareIntent, "Share"))
    }

    private fun generateImageUri(mBitmap: Bitmap): Uri {
        val cacheDir = getExternalCacheDir()
        val file = File(cacheDir, "shared_image.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: Exception) {
            Log.e("ShareImage", "Failed to generate image URI: ${e.message}")
            return Uri.EMPTY
        }

        return FileProvider.getUriForFile(this, "com.nimitsajal.drawingapp.fileprovider", file)
    }


//    private fun shareImage(mBitmap: Bitmap) {
//        var filePathBuilder: StringBuilder = StringBuilder("")
//        saveImageToStorage(mBitmap, filePathBuilder)
//        val filePath: String = filePathBuilder.toString()
//        if (filePath != "") {
//            try {
//                MediaScannerConnection.scanFile(this@MainActivity, arrayOf(filePath), null) { _, uri ->
//
//                        val shareIntent = Intent()
//                        shareIntent.action = Intent.ACTION_SEND
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
//                        shareIntent.type = "image/jpeg"
//                        startActivity(Intent.createChooser(shareIntent, "Share"))
//
//                }
//            } catch (e: Exception) {
//                Log.e("ShareImage", "Sharing failed: ${e.message}")
//                Toast.makeText(this, "Sharing failed, please try again", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(this, "Cannot share", Toast.LENGTH_SHORT).show()
//        }
//    }


//    @SuppressLint("StaticFieldLeak")
//    private inner class BitmapAsyncTask(val mBitmap: Bitmap): AsyncTask<Any, Void, String>(){
//        override fun doInBackground(vararg p0: Any?): String {
//            var result = ""
//            try{
//                val bytes = ByteArrayOutputStream()
//                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//                val file = File(externalCacheDir!!.absoluteFile.toString() + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
//                val fos = FileOutputStream(file)
//                fos.write(bytes.toByteArray())
//                fos.close()
//                result = file.absolutePath
//            }
//            catch(e: Exception){
//                result = ""
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            if(result!!.isNotEmpty()){
//                Toast.makeText(this@MainActivity, "File saved successfully.", Toast.LENGTH_SHORT).show()
//            }
//            else{
//                Toast.makeText(this@MainActivity, "Something went wrong while saving the file.", Toast.LENGTH_SHORT).show()
//            }
//            MediaScannerConnection.scanFile(this@MainActivity, arrayOf(result), null){
//                path, uri -> val shareIntent = Intent()
//                shareIntent.action = Intent.ACTION_SEND
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
//                shareIntent.type = "image/jpeg"
//                startActivity(Intent.createChooser(shareIntent, "Share"))
//            }
//
//        }
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private inner class BitmapAsyncTaskOnlySave(val mBitmap: Bitmap): AsyncTask<Any, Void, String>(){
//        override fun doInBackground(vararg p0: Any?): String {
//            var result = ""
//            try{
//                val bytes = ByteArrayOutputStream()
//                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//                val file = File(externalCacheDir!!.absoluteFile.toString() + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
//                val fos = FileOutputStream(file)
//                fos.write(bytes.toByteArray())
//                fos.close()
//                result = file.absolutePath
//            }
//            catch(e: Exception){
//                result = ""
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            if(result!!.isNotEmpty()){
//                Toast.makeText(this@MainActivity, "File saved successfully.", Toast.LENGTH_SHORT).show()
//            }
//            else{
//                Toast.makeText(this@MainActivity, "Something went wrong while saving the file.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    companion object{
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }
}