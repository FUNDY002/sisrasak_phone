package com.example.mobile_price_sirasak

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    // ประกาศตัวแปรสำหรับเก็บข้อมูลจาก UI
    private lateinit var batteryPower: EditText
    private lateinit var blue: EditText
    private lateinit var clockSpeed: EditText
    private lateinit var dualSim: EditText
    private lateinit var fc: EditText
    private lateinit var fourG: EditText
    private lateinit var intMemory: EditText
    private lateinit var mDep: EditText
    private lateinit var mobileWt: EditText
    private lateinit var nCores: EditText
    private lateinit var pc: EditText
    private lateinit var pxHeight: EditText
    private lateinit var pxWidth: EditText
    private lateinit var ram: EditText
    private lateinit var scH: EditText
    private lateinit var scW: EditText
    private lateinit var talkTime: EditText
    private lateinit var threeG: EditText
    private lateinit var touchScreen: EditText
    private lateinit var wifi: EditText
    private lateinit var predictButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ผูกตัวแปรกับ View
        batteryPower = findViewById(R.id.batteryPower)
        blue = findViewById(R.id.blue)
        clockSpeed = findViewById(R.id.clockSpeed)
        dualSim = findViewById(R.id.dualSim)
        fc = findViewById(R.id.fc)
        fourG = findViewById(R.id.fourG)
        intMemory = findViewById(R.id.intMemory)
        mDep = findViewById(R.id.mDep)
        mobileWt = findViewById(R.id.mobileWt)
        nCores = findViewById(R.id.nCores)
        pc = findViewById(R.id.pc)
        pxHeight = findViewById(R.id.pxHeight)
        pxWidth = findViewById(R.id.pxWidth)
        ram = findViewById(R.id.ram)
        scH = findViewById(R.id.scH)
        scW = findViewById(R.id.scW)
        talkTime = findViewById(R.id.talkTime)
        threeG = findViewById(R.id.threeG)
        touchScreen = findViewById(R.id.touchScreen)
        wifi = findViewById(R.id.wifi)
        predictButton = findViewById(R.id.predictButton)
        resultTextView = findViewById(R.id.resultTextView)

        // กำหนด listener สำหรับปุ่มทำนาย
        predictButton.setOnClickListener {
            PredictTask().execute(
                batteryPower.text.toString(),
                blue.text.toString(),
                clockSpeed.text.toString(),
                dualSim.text.toString(),
                fc.text.toString(),
                fourG.text.toString(),
                intMemory.text.toString(),
                mDep.text.toString(),
                mobileWt.text.toString(),
                nCores.text.toString(),
                pc.text.toString(),
                pxHeight.text.toString(),
                pxWidth.text.toString(),
                ram.text.toString(),
                scH.text.toString(),
                scW.text.toString(),
                talkTime.text.toString(),
                threeG.text.toString(),
                touchScreen.text.toString(),
                wifi.text.toString()
            )
        }
    }

    private inner class PredictTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            return try {
                // สร้าง URL สำหรับเชื่อมต่อกับ API
                val url = URL("http://10.13.1.26:3000/api/predict")  // แก้ไข URL ให้ตรงกับเซิร์ฟเวอร์ Flask ของคุณ

                // สร้างการเชื่อมต่อ HTTP
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json; utf-8")
                connection.setRequestProperty("Accept", "application/json")
                connection.doOutput = true

                // สร้าง JSON object ที่จะส่งไปยัง API
                val jsonObject = JSONObject()
                jsonObject.put("battery_power", params[0]?.toInt() ?: 0)
                jsonObject.put("blue", params[1]?.toInt() ?: 0)
                jsonObject.put("clock_speed", params[2]?.toFloat() ?: 0f)
                jsonObject.put("dual_sim", params[3]?.toInt() ?: 0)
                jsonObject.put("fc", params[4]?.toInt() ?: 0)
                jsonObject.put("four_g", params[5]?.toInt() ?: 0)
                jsonObject.put("int_memory", params[6]?.toInt() ?: 0)
                jsonObject.put("m_dep", params[7]?.toFloat() ?: 0f)
                jsonObject.put("mobile_wt", params[8]?.toInt() ?: 0)
                jsonObject.put("n_cores", params[9]?.toInt() ?: 0)
                jsonObject.put("pc", params[10]?.toInt() ?: 0)
                jsonObject.put("px_height", params[11]?.toInt() ?: 0)
                jsonObject.put("px_width", params[12]?.toInt() ?: 0)
                jsonObject.put("ram", params[13]?.toInt() ?: 0)
                jsonObject.put("sc_h", params[14]?.toInt() ?: 0)
                jsonObject.put("sc_w", params[15]?.toInt() ?: 0)
                jsonObject.put("talk_time", params[16]?.toInt() ?: 0)
                jsonObject.put("three_g", params[17]?.toInt() ?: 0)
                jsonObject.put("touch_screen", params[18]?.toInt() ?: 0)
                jsonObject.put("wifi", params[19]?.toInt() ?: 0)

                // ส่ง JSON ไปยัง API
                val outputStream: OutputStream = connection.outputStream
                val input = jsonObject.toString().toByteArray(Charsets.UTF_8)
                outputStream.write(input, 0, input.size)

                // อ่านผลลัพธ์จาก API
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val responseJson = JSONObject(response)
                val priceRange = responseJson.getInt("price_range")

                "Predicted Price Range: $priceRange"

            } catch (e: Exception) {
                e.printStackTrace()
                "Exception: ${e.message}"
            }
        }

        override fun onPostExecute(result: String?) {
            // แสดงผลลัพธ์บนหน้าจอ
            resultTextView.text = result
        }
    }
}
