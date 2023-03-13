package app.allever.android.lib.core.helper

import android.app.ActivityManager
import android.content.Context
import android.os.Environment
import android.os.StatFs
import android.text.format.Formatter
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import okio.FileNotFoundException
import okio.IOException
import java.io.BufferedReader
import java.io.File
import java.io.FileFilter
import java.io.FileReader
import java.util.regex.Pattern

/**
 * 设备相关：厂商/型号/设备id/系统版本号/mac..
 */
object DeviceHelper {

    fun getAvailableMemory(): Long {
        val am = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memoryInfo)
        log("可用内存：" + Formatter.formatFileSize(App.context, memoryInfo.availMem))
        return memoryInfo.availMem
    }

    fun getTotalMemory(): Long {
        val am = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        am.getMemoryInfo(memoryInfo)
        log("总共内存：" + Formatter.formatFileSize(App.context, memoryInfo.totalMem))
        return memoryInfo.totalMem
    }

    fun getUsedMemory(): Long {
        val result = getTotalMemory() - getAvailableMemory()
        log("已用内存：" + Formatter.formatFileSize(App.context, result))
        return result
    }

    /**
     * 获取手机内部空间总大小
     *
     * @return 大小，字节为单位
     */
    fun getTotalInternalMemorySize(): Long {
        //获取内部存储根目录
        val path = Environment.getDataDirectory()
        //系统的空间描述类
        val stat = StatFs(path.path)
        //每个区块占字节数
        val blockSize = stat.blockSizeLong
        //区块总数
        val totalBlocks = stat.blockCountLong
        val result = totalBlocks * blockSize
        log("总共内部存储：" + Formatter.formatFileSize(App.context, result))
        return result
    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return 大小，字节为单位
     */
    fun getAvailableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        //获取可用区块数量
        val availableBlocks = stat.availableBlocksLong
        val result = availableBlocks * blockSize
        log("可用内部存储：" + Formatter.formatFileSize(App.context, result))
        return result
    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return 大小，字节为单位
     */
    fun getUsedInternalMemorySize(): Long {
        val result = getTotalInternalMemorySize() - getAvailableInternalMemorySize()
        log("已用内部存储：" + Formatter.formatFileSize(App.context, result))
        return result
    }

    fun getCpuTemInfo() : Float{
        try {
            //cpu温度
            val tempInfoPath = "/sys/class/thermal/thermal_zone9/subsystem/thermal_zone9/temp"
            val temReader = FileReader(tempInfoPath)

            val temBf = BufferedReader(temReader)

            val temInfo = temBf.readLine().toFloat()
            val result = if (temInfo > 1000) {
                temInfo / 1000.0
            } else {
                temInfo / 1.0
            }
            //单位自行转换
            log("CPU温度：${result}")
            return result.toFloat()
        } catch (e: Exception) {
            e.printStackTrace()
            return 0F
        }



    }

    fun getThermalInfo(): List<String>? {
        val result: MutableList<String> = ArrayList()
        var br: BufferedReader? = null
        try {
            val dir = File("/sys/class/thermal/")
            val files: Array<File> = dir.listFiles(object : FileFilter {
                override fun accept(file: File): Boolean {
                    return if (Pattern.matches("thermal_zone[0-9]+", file.getName())) {
                        true
                    } else false
                }
            })
            val SIZE = files.size
            var line: String? = null
            var type: String? = null
            var temp: String? = null
            for (i in 0 until SIZE) {
                br = BufferedReader(FileReader("/sys/class/thermal/thermal_zone$i/type"))
                line = br.readLine()
                if (line != null) {
                    type = line
                }
                br = BufferedReader(FileReader("/sys/class/thermal/thermal_zone$i/temp"))
                line = br.readLine()
                if (line != null) {
                    val temperature = line.toLong()
                    temp = if (temperature < 0) {
                        "0 °C"
                    } else {
                        (temperature / 1000.0).toString()+"°C"
                    }
                }
                result.add("$type : $temp")
                log("温度 ${type}：${temp}")
            }
            br?.close()
        } catch (e: FileNotFoundException) {
            result.add(e.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                br?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }
}