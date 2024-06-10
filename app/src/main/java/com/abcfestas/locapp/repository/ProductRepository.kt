package com.abcfestas.locapp.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.abcfestas.locapp.data.remote.IApi
import com.abcfestas.locapp.data.remote.responses.product.ProductListResponse
import com.abcfestas.locapp.data.remote.responses.product.ProductResponse
import com.abcfestas.locapp.util.Resource
import com.abcfestas.locapp.viewmodel.product.ProductFormState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ProductRepository(
    private val api: IApi
) {
    suspend fun getProducts(
        search: String? = null,
        page: Int = 1,
        perPage: Int = 10
    ): Resource<ProductListResponse> {
        val response = try {
            api.getProducts(search, page, perPage)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::getProducts", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun getProductById(id: Int): Resource<ProductResponse> {
        val response = try {
            api.getProductById(id)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::getProductById", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun createProduct(product: ProductFormState): Resource<ProductResponse> {
        val response = try {
            api.createProduct(product)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::createProduct", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun updateProduct(id: Int, product: ProductFormState): Resource<ProductResponse> {
        val response = try {
            api.updateProduct(id, product)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::updateProduct", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    suspend fun syncImage(
        context: Context,
        id: Int,
        imageUri: Uri
    ): Resource<ProductResponse> {
        val response = try {
            val filePart = prepareFilePart(context, imageUri, "image")
            api.syncProductImage(id, filePart)
        } catch (e: Exception) {
            Log.d("ERROR: ProductRepository::createProduct", e.message.toString())
            return Resource.Error(e.message.toString()) // TODO: improvement message
        }

        return Resource.Success(response)
    }

    private fun prepareFilePart(context: Context, uri: Uri, partName: String): MultipartBody.Part {
        val file = File(getRealPathFromURI(context, uri))
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String {
        var realPath = ""
        val uriScheme = uri.scheme
        if (uriScheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val dataIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    realPath = if (dataIndex > -1) {
                        it.getString(dataIndex)
                    } else {
                        getPathFromInputStreamUri(context, uri)
                    }
                }
            }
        } else if (uriScheme == "file") {
            realPath = uri.path ?: ""
        }
        return realPath
    }
    private fun getPathFromInputStreamUri(context: Context, uri: Uri): String {
        var filePath = ""
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream != null) {
            val startIndex = uri.path!!.lastIndexOf('/') + 1
            val endIndex = uri.path!!.lastIndexOf('.')
            val fileName = uri.path!!.substring(startIndex, endIndex)
            val fileExtension = uri.path!!.substringAfterLast('.', "")
            val fileNameWithExtension = "${fileName}.${fileExtension}"
            val tempFile = File(context.cacheDir, fileNameWithExtension)
            tempFile.deleteOnExit()
            val outputStream = FileOutputStream(tempFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            filePath = tempFile.path
        }
        return filePath
    }


    private fun createPartFromString(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }
}