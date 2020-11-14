package com.nimitsajal.drawingapp

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import kotlinx.android.synthetic.main.dialog_colors.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.Exception

class MainActivity : AppCompatActivity() {
    var eraserColor: String = "BlackLight"
    var brushColor: String = "BlackDark"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView.setBrushSize(8.toFloat())

        ibBrush.setOnClickListener{
            showBrushSizeChooserDialog()
        }

        ibEraser.setOnClickListener{
            when(eraserColor){
                "BlueDark" -> drawingView.setBrushColor("#00008b")
                "BlueLight" -> drawingView.setBrushColor("#add8e6")
                "BlackDark" -> drawingView.setBrushColor("#000000")
                "BlackLight" -> drawingView.setBrushColor("#ffffff")
                "RedDark" -> drawingView.setBrushColor("#8b0000")
                "RedLight" -> drawingView.setBrushColor("#ff0000")
                "GreenDark" -> drawingView.setBrushColor("#013220")
                "GreenLight" -> drawingView.setBrushColor("#90ee90")
                "PinkDark" -> drawingView.setBrushColor("#d11d53")
                "PinkLight" -> drawingView.setBrushColor("#ffb6c1")
                "PurpleDark" -> drawingView.setBrushColor("#800080")
                "PurpleLight" -> drawingView.setBrushColor("#cd00cd")
                "YellowDark" -> drawingView.setBrushColor("#ff9500")
                "YellowLight" -> drawingView.setBrushColor("#fff44f")
                "OrangeDark" -> drawingView.setBrushColor("#ff4c00")
                "OrangeLight" -> drawingView.setBrushColor("#ff9b1a")
                "GreyDark" -> drawingView.setBrushColor("#2f4f4f")
                "GreyLight" -> drawingView.setBrushColor("#d3d3d3")
            }
        }

        ibPencil.setOnClickListener{
            when(brushColor){
                "BlueDark" -> drawingView.setBrushColor("#00008b")
                "BlueLight" -> drawingView.setBrushColor("#add8e6")
                "BlackDark" -> drawingView.setBrushColor("#000000")
                "BlackLight" -> drawingView.setBrushColor("#ffffff")
                "RedDark" -> drawingView.setBrushColor("#8b0000")
                "RedLight" -> drawingView.setBrushColor("#ff0000")
                "GreenDark" -> drawingView.setBrushColor("#013220")
                "GreenLight" -> drawingView.setBrushColor("#90ee90")
                "PinkDark" -> drawingView.setBrushColor("#d11d53")
                "PinkLight" -> drawingView.setBrushColor("#ffb6c1")
                "PurpleDark" -> drawingView.setBrushColor("#800080")
                "PurpleLight" -> drawingView.setBrushColor("#cd00cd")
                "YellowDark" -> drawingView.setBrushColor("#ff9500")
                "YellowLight" -> drawingView.setBrushColor("#fff44f")
                "OrangeDark" -> drawingView.setBrushColor("#ff4c00")
                "OrangeLight" -> drawingView.setBrushColor("#ff9b1a")
                "GreyDark" -> drawingView.setBrushColor("#2f4f4f")
                "GreyLight" -> drawingView.setBrushColor("#d3d3d3")
            }
        }

        ibImport.setOnClickListener{
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

        ibUndo.setOnClickListener{
            drawingView.onClickUndo()
        }

        ibRedo.setOnClickListener{
            drawingView.onClickRedo()
        }

        ibEraseAll.setOnClickListener{
            drawingView.onClickClear()
            ivBackground.setBackgroundColor(Color.parseColor("#ffffff"))
            eraserColor = "BlackLight"
            brushColor = "BlackDark"
        }

        ibShare.setOnClickListener{
            if(isReadStorageAllowed()){
                shareImage(getBitmapFromView(drawingView))
            }
            else{
                requestStoragePermission()
            }
        }

        ibSave.setOnClickListener{
            if(isReadStorageAllowed()){
                saveImageToStorage(getBitmapFromView(drawingView))
            }
            else{
                requestStoragePermission()
            }
        }

        ibColor.setOnClickListener{
            showColorChooserDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY){
              try{
                  if(data!!.data != null){
                      ivBackground.visibility = View.VISIBLE
                      ivBackground.setImageURI(data.data)
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
        backgroundColorDialog.setContentView(R.layout.dialog_background_color)
        backgroundColorDialog.setTitle("Brush Size: ")

        val blueDark = backgroundColorDialog.btnColorBlueDark
        val blueLight = backgroundColorDialog.btnColorBlueLight
        val blackDark = backgroundColorDialog.btnColorBlackDark
        val blackLight = backgroundColorDialog.btnColorBlackLight
        val redDark = backgroundColorDialog.btnColorRedDark
        val redLight = backgroundColorDialog.btnColorRedLight
        val greenDark = backgroundColorDialog.btnColorGreenDark
        val greenLight = backgroundColorDialog.btnColorGreenLight
        val pinkDark = backgroundColorDialog.btnColorPinkDark
        val pinkLight = backgroundColorDialog.btnColorPinkLight
        val purpleDark = backgroundColorDialog.btnColorPurpleDark
        val purpleLight = backgroundColorDialog.btnColorPurpleLight
        val yellowDark = backgroundColorDialog.btnColorYellowDark
        val yellowLight = backgroundColorDialog.btnColorYellowLight
        val orangeDark = backgroundColorDialog.btnColorOrangeDark
        val orangeLight = backgroundColorDialog.btnColorOrangeLight
        val greyDark = backgroundColorDialog.btnColorGreyDark
        val greyLight = backgroundColorDialog.btnColorGreyLight

        blueDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#00008b"))
            eraserColor = "BlueDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        blueLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#add8e6"))
            eraserColor = "BlueLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        blackDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#000000"))
            eraserColor = "BlackDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        blackLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#ffffff"))
            eraserColor = "BlackLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        redDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#8b0000"))
            eraserColor = "RedDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        redLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#ff0000"))
            eraserColor = "RedLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greenDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#013220"))
            eraserColor = "GreenDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greenLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#90ee90"))
            eraserColor = "#GreenLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        pinkDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#d11d53"))
            eraserColor = "PinkDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        pinkLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#ffb6c1"))
            eraserColor = "PinkLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        purpleDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#800080"))
            eraserColor = "PurpleDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        purpleLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#cd00cd"))
            eraserColor = "PurpleLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        yellowDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#ff9500"))
            eraserColor = "YellowDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        yellowLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#fff44f"))
            eraserColor = "YellowLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        orangeDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#ff4c00"))
            eraserColor = "OrangeDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        orangeLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#ff9b1a"))
            eraserColor = "OrangeLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greyDark.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#2f4f4f"))
            eraserColor = "GreyDark"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        greyLight.setOnClickListener{
            ivBackground.setBackgroundColor(Color.parseColor("#d3d3d3"))
            eraserColor = "GreyLight"
            drawingView.onClickClear()
            backgroundColorDialog.dismiss()
        }
        backgroundColorDialog.show()
    }

    fun showColorChooserDialog(){
        val colorDialog = Dialog(this)
        colorDialog.setContentView(R.layout.dialog_colors)
        colorDialog.setTitle("Colors: ")

        val blueDark = colorDialog.btnColorBlueDark
        val blueLight = colorDialog.btnColorBlueLight
        val blackDark = colorDialog.btnColorBlackDark
        val blackLight = colorDialog.btnColorBlackLight
        val redDark = colorDialog.btnColorRedDark
        val redLight = colorDialog.btnColorRedLight
        val greenDark = colorDialog.btnColorGreenDark
        val greenLight = colorDialog.btnColorGreenLight
        val pinkDark = colorDialog.btnColorPinkDark
        val pinkLight = colorDialog.btnColorPinkLight
        val purpleDark = colorDialog.btnColorPurpleDark
        val purpleLight = colorDialog.btnColorPurpleLight
        val yellowDark = colorDialog.btnColorYellowDark
        val yellowLight = colorDialog.btnColorYellowLight
        val orangeDark = colorDialog.btnColorOrangeDark
        val orangeLight = colorDialog.btnColorOrangeLight
        val greyDark = colorDialog.btnColorGreyDark
        val greyLight = colorDialog.btnColorGreyLight

        blueDark.setOnClickListener{
            drawingView.setBrushColor("#00008b")
            brushColor = "BlueDark"
            colorDialog.dismiss()
        }
        blueLight.setOnClickListener{
            drawingView.setBrushColor("#add8e6")
            brushColor = "BlueLight"
            colorDialog.dismiss()
        }
        blackDark.setOnClickListener{
            drawingView.setBrushColor("#000000")
            brushColor = "BlackDark"
            colorDialog.dismiss()
        }
        blackLight.setOnClickListener{
            drawingView.setBrushColor("#ffffff")
            brushColor = "BlackLight"
            colorDialog.dismiss()
        }
        redDark.setOnClickListener{
            drawingView.setBrushColor("#8b0000")
            brushColor = "RedDark"
            colorDialog.dismiss()
        }
        redLight.setOnClickListener{
            drawingView.setBrushColor("#ff0000")
            brushColor = "RedLight"
            colorDialog.dismiss()
        }
        greenDark.setOnClickListener{
            drawingView.setBrushColor("#013220")
            brushColor = "GreenDark"
            colorDialog.dismiss()
        }
        greenLight.setOnClickListener{
            drawingView.setBrushColor("#90ee90")
            brushColor = "GreenLight"
            colorDialog.dismiss()
        }
        pinkDark.setOnClickListener{
            drawingView.setBrushColor("#d11d53")
            brushColor = "PinkDark"
            colorDialog.dismiss()
        }
        pinkLight.setOnClickListener{
            drawingView.setBrushColor("#ffb6c1")
            brushColor = "PinkLight"
            colorDialog.dismiss()
        }
        purpleDark.setOnClickListener{
            drawingView.setBrushColor("#800080")
            brushColor = "PurpleDark"
            colorDialog.dismiss()
        }
        purpleLight.setOnClickListener{
            drawingView.setBrushColor("#cd00cd")
            brushColor = "PurpleLight"
            colorDialog.dismiss()
        }
        yellowDark.setOnClickListener{
            drawingView.setBrushColor("#ff9500")
            brushColor = "YellowDark"
            colorDialog.dismiss()
        }
        yellowLight.setOnClickListener{
            drawingView.setBrushColor("#fff44f")
            brushColor = "YellowLight"
            colorDialog.dismiss()
        }
        orangeDark.setOnClickListener{
            drawingView.setBrushColor("#ff4c00")
            brushColor = "OrangeDark"
            colorDialog.dismiss()
        }
        orangeLight.setOnClickListener{
            drawingView.setBrushColor("#ff9b1a")
            brushColor = "OrangeLight"
            colorDialog.dismiss()
        }
        greyDark.setOnClickListener{
            drawingView.setBrushColor("#2f4f4f")
            brushColor = "GreyDark"
            colorDialog.dismiss()
        }
        greyLight.setOnClickListener{
            drawingView.setBrushColor("#d3d3d3")
            brushColor = "GreyLight"
            colorDialog.dismiss()
        }
        colorDialog.show()
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush Size: ")
        val extraSmallBtn = brushDialog.ibExtraSmallBrushSize
        val smallBtn = brushDialog.ibSmallBrushSize
        val mediumBtn = brushDialog.ibMediumBrushSize
        val largeBtn = brushDialog.ibLargeBrushSize
        val extraLargeBtn = brushDialog.ibExtraLargeBrushSize
        extraSmallBtn.setOnClickListener{
            drawingView.setBrushSize(2.toFloat())
            brushDialog.dismiss()
        }
        smallBtn.setOnClickListener{
            drawingView.setBrushSize(6.toFloat())
            brushDialog.dismiss()
        }
        mediumBtn.setOnClickListener{
            drawingView.setBrushSize(14.toFloat())
            brushDialog.dismiss()
        }
        largeBtn.setOnClickListener{
            drawingView.setBrushSize(24.toFloat())
            brushDialog.dismiss()
        }
        extraLargeBtn.setOnClickListener{
            drawingView.setBrushSize(32.toFloat())
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

    private fun saveImageToStorage(mBitmap: Bitmap){
        val externalStorage = Environment.getExternalStorageState()
        if(externalStorage == Environment.MEDIA_MOUNTED){
            val storageDirectory = Environment.getExternalStorageDirectory().absolutePath
            val dir = File("${storageDirectory}/DrawingApp")
            val created = dir.mkdirs()
            //Toast.makeText(this, "$created", Toast.LENGTH_SHORT).show()
            val filename = String.format("%d.png", System.currentTimeMillis())
            val outFile = File(dir.toString() + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
            //Toast.makeText(this, "Image location -> ${Uri.parse(outFile.absolutePath)}", Toast.LENGTH_SHORT).show()
            //val file = File(storageDirectory + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
            try{
                val stream:OutputStream = FileOutputStream(outFile)
                //Toast.makeText(this, "reached till here", Toast.LENGTH_SHORT).show()
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                Toast.makeText(this, "Image saved successfully at ${Uri.parse(outFile.absolutePath)}", Toast.LENGTH_SHORT).show()
            }
            catch(e:java.lang.Exception){
                e.printStackTrace()
                Toast.makeText(this, "There is some problem", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Unable, to access storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareImage(mBitmap: Bitmap){
        var result = ""
        val externalStorage = Environment.getExternalStorageState()
        if(externalStorage == Environment.MEDIA_MOUNTED){
            val storageDirectory = Environment.getExternalStorageDirectory().absolutePath
            val dir = File("${storageDirectory}/DrawingApp")
            val created = dir.mkdirs()
            //Toast.makeText(this, "$created", Toast.LENGTH_SHORT).show()
            val filename = String.format("%d.png", System.currentTimeMillis())
            val outFile = File(dir.toString() + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
            //Toast.makeText(this, "Image location -> ${Uri.parse(outFile.absolutePath)}", Toast.LENGTH_SHORT).show()
            //val file = File(storageDirectory + File.separator + "DrawingApp_" + System.currentTimeMillis()/1000 + ".jpg")
            try{
                val stream:OutputStream = FileOutputStream(outFile)
                //Toast.makeText(this, "reached till here", Toast.LENGTH_SHORT).show()
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                Toast.makeText(this, "Image saved successfully at ${Uri.parse(outFile.absolutePath)}", Toast.LENGTH_SHORT).show()
                result = outFile.absolutePath
            }
            catch(e:java.lang.Exception){
                e.printStackTrace()
                Toast.makeText(this, "There is some problem", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Unable, to access storage", Toast.LENGTH_SHORT).show()
        }
        if(result != ""){
            MediaScannerConnection.scanFile(this@MainActivity, arrayOf(result), null){
                    path, uri -> val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    shareIntent.type = "image/jpeg"
                    startActivity(Intent.createChooser(shareIntent, "Share"))
            }
        }
        else{
            Toast.makeText(this, "Cannot share", Toast.LENGTH_SHORT).show()
        }
    }

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