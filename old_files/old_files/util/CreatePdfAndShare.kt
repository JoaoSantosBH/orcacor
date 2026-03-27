package com.jomar.senhorpintor.util

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.App
import com.jomar.senhorpintor.base.PDF_NAME
import com.jomar.senhorpintor.base.PDF_TYPE
import com.jomar.senhorpintor.base.PROVIDER_SUFIX
import java.io.File
import java.io.FileOutputStream


open class CreatePdfAndShare {
    companion object {
        fun createPdf(bmp: Bitmap) {
            val context = App.instance
            val path = context.filesDir
            val file = File(path, PDF_NAME)
            val document = PdfDocument()
            val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(bmp.width, bmp.height, 2).create()
            val page: PdfDocument.Page = document.startPage(pageInfo)

            val canvas: Canvas = page.canvas
            canvas.drawBitmap(bmp, 0f, 0f, null)
            document.finishPage(page)
            document.writeTo(FileOutputStream(file))

            val intent = Intent(Intent.ACTION_SEND)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val TEXT_SHARE_SUBJECT = context.resources.getString(R.string.share_subject)
            val TEXT_SHARE_TEXT = context.resources.getString(R.string.share_subject_txt)
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(context, context.getApplicationContext()
                            .getPackageName().toString() + PROVIDER_SUFIX, file)
            )
            intent.type = PDF_TYPE
            intent.putExtra(Intent.EXTRA_SUBJECT, TEXT_SHARE_SUBJECT)
            intent.putExtra(Intent.EXTRA_TEXT, TEXT_SHARE_TEXT)
            context.startActivity(intent)
            document.close()

        }
    }
}