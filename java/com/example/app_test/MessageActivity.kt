package com.example.app_test


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


// 메시지를 입력하고 저장하는 액티비티
class MessageActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper // SQLite 데이터베이스 관리용 Helper 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms) // 레이아웃 설정

        dbHelper = DBHelper(this) // DBHelper 초기화

        val editText = findViewById<EditText>(R.id.message_text) // 메시지 입력란
        val saveButton = findViewById<Button>(R.id.primary_button) // 저장 버튼

        // 저장 버튼 클릭 시 동작
        saveButton.setOnClickListener {
            val message = editText.text.toString().trim() // 입력된 메시지 텍스트를 가져옴
            if (message.isNotEmpty()) { // 메시지가 비어있지 않은 경우
                val id = dbHelper.addMessage(message) // 데이터베이스에 메시지 추가
                if (id != -1L) { // 메시지가 성공적으로 추가된 경우
                    Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show() // 저장 완료 메시지 표시
                } else { // 메시지 추가에 실패한 경우
                    Toast.makeText(this, "저장에 실패하였습니다.", Toast.LENGTH_SHORT).show() // 저장 실패 메시지 표시
                }
            } else { // 입력된 메시지가 없는 경우
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show() // 입력 요청 메시지 표시
            }
        }

        // 앱이 시작될 때 이전에 입력된 메시지가 있으면 해당 메시지를 표시
        val storedMessage = dbHelper.getMessage()
        editText.setText(storedMessage)
    }

    override fun onDestroy() {
        dbHelper.close() // DBHelper 사용 종료
        super.onDestroy()
    }
}

// SQLite 데이터베이스를 관리하는 Helper 클래스
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MessageDB"
        private const val TABLE_MESSAGES = "messages"
        private const val KEY_ID = "id"
        private const val KEY_MESSAGE = "message"
    }

    // 데이터베이스 생성
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_MESSAGES($KEY_ID INTEGER PRIMARY KEY, $KEY_MESSAGE TEXT)")
        db.execSQL(createTableQuery)
    }

    // 데이터베이스 업그레이드
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        onCreate(db)
    }

    // 메시지 추가
    fun addMessage(message: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_MESSAGE, message)
        return db.insert(TABLE_MESSAGES, null, contentValues)
    }

    // 저장된 메시지 불러오기
    fun getMessage(): String? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_MESSAGES"
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        var message: String? = null
        if (cursor.moveToFirst()) { // 커서 이동 성공 시
            val messageIndex = cursor.getColumnIndex(KEY_MESSAGE)
            if (messageIndex != -1) { // 인덱스가 유효한 경우
                message = cursor.getString(messageIndex) // 메시지 값 가져오기
            }
        }
        cursor.close() // 커서 닫기
        return message // 메시지 반환
    }
}