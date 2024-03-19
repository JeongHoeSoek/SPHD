package com.example.app_test

    import android.content.ContentValues
    import android.database.Cursor
    import android.database.sqlite.SQLiteDatabase
    import android.database.sqlite.SQLiteOpenHelper
    import android.os.Bundle
    import android.provider.BaseColumns
    import android.widget.EditText
    import android.widget.ImageButton
    import android.widget.LinearLayout
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity


    class NumActivity: AppCompatActivity() {

        private lateinit var dbHelper: SQLiteOpenHelper // SQLiteOpenHelper를 사용하여 데이터베이스를 관리할 변수
        private val infoList = mutableListOf<Pair<String, String>>() // 이름과 전화번호 쌍을 저장할 리스트

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_num)

            // 데이터베이스 헬퍼를 초기화하고 데이터베이스를 생성 또는 업그레이드하는 람다 함수를 정의하여 dbHelper에 할당
            dbHelper = object : SQLiteOpenHelper(this, DATABASE_NAME, null, DATABASE_VERSION) {
                override fun onCreate(db: SQLiteDatabase) {
                    // 데이터베이스가 생성될 때 호출되며, 테이블을 생성하는 SQL 쿼리를 실행
                    db.execSQL(SQL_CREATE_ENTRIES)
                }

                override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                    // 데이터베이스가 업그레이드 될 때 호출되며, 기존 테이블을 삭제하고 새로운 버전의 테이블을 생성
                    db.execSQL(SQL_DELETE_ENTRIES)
                    onCreate(db)
                }
            }

            val plusButton = findViewById<ImageButton>(R.id.plus_Button)

            plusButton.setOnClickListener {
                showCustomDialog() // 사용자 정의 대화 상자 표시
            }

            loadInfoFromDatabase() // 앱 시작시 데이터베이스에서 정보 로드
            updateUI() // UI 업데이트
        }

        private fun showCustomDialog() {
            // 사용자 정의 대화 상자의 레이아웃을 가져오고 필요한 뷰들을 찾음
            val dialogView = layoutInflater.inflate(R.layout.dialog, null)
            val nameEditText = dialogView.findViewById<EditText>(R.id.name_edit_text)
            val phoneEditText = dialogView.findViewById<EditText>(R.id.phone_edit_text)
            val cancelButton = dialogView.findViewById<ImageButton>(R.id.cancel_button)
            val saveButton = dialogView.findViewById<ImageButton>(R.id.save_button)

            // 대화 상자 생성 및 표시
            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .show()

            cancelButton.setOnClickListener {
                alertDialog.dismiss() // 취소 버튼 클릭 시 대화 상자 닫기
            }

            saveButton.setOnClickListener {
                val name = nameEditText.text.toString()
                val phone = phoneEditText.text.toString()
                addInfoToList(name, phone) // 정보를 리스트에 추가
                saveInfoToDatabase(name, phone) // 정보를 데이터베이스에 저장
                updateUI() // UI 업데이트
                alertDialog.dismiss() // 저장 버튼 클릭 시 대화 상자 닫기
            }
        }

        private fun addInfoToList(name: String, phone: String) {
            infoList.add(name to phone) // 이름과 전화번호 쌍을 리스트에 추가
        }

        private fun updateUI() {
            val layout = findViewById<LinearLayout>(R.id.info_layout)
            layout.removeAllViews() // 기존 뷰 제거

            // 리스트에 있는 정보를 화면에 추가
            for (info in infoList) {
                val infoView = layoutInflater.inflate(R.layout.info_data, null)
                val nameLabel = infoView.findViewById<EditText>(R.id.name_label)
                val phoneLabel = infoView.findViewById<EditText>(R.id.phone_label)
                val deleteButton = infoView.findViewById<ImageButton>(R.id.delete_button)

                nameLabel.setText(info.first) // 이름 텍스트 설정
                phoneLabel.setText(info.second) // 전화번호 텍스트 설정

                deleteButton.setOnClickListener {
                    infoList.remove(info) // 리스트에서 정보 제거
                    deleteInfoFromDatabase(info.first) // 데이터베이스에서 정보 삭제
                    updateUI() // 삭제 후 UI 업데이트
                }

                layout.addView(infoView) // 정보 뷰를 LinearLayout에 추가
            }
        }

        private fun saveInfoToDatabase(name: String, phone: String) {
            val db = dbHelper.writableDatabase // 쓰기 가능한 데이터베이스 가져오기
            val values = ContentValues().apply {
                put(COLUMN_NAME_NAME, name) // 이름 컬럼에 이름 값 할당
                put(COLUMN_NAME_PHONE, phone) // 전화번호 컬럼에 전화번호 값 할당
            }
            db.insert(TABLE_NAME, null, values) // 데이터베이스에 행 삽입
        }

        private fun loadInfoFromDatabase() {
            val db = dbHelper.readableDatabase // 읽기 전용 데이터베이스 가져오기
            val projection = arrayOf(COLUMN_NAME_NAME, COLUMN_NAME_PHONE) // 검색할 열 이름 배열
            val cursor: Cursor = db.query(TABLE_NAME, projection, null, null, null, null, null)

            with(cursor) {
                while (moveToNext()) {
                    val name = getString(getColumnIndexOrThrow(COLUMN_NAME_NAME)) // 이름 가져오기
                    val phone = getString(getColumnIndexOrThrow(COLUMN_NAME_PHONE)) // 전화번호 가져오기
                    addInfoToList(name, phone) // 리스트에 정보 추가
                }
            }
            cursor.close() // 커서 닫기
        }

        private fun deleteInfoFromDatabase(name: String) {
            val db = dbHelper.writableDatabase // 쓰기 가능한 데이터베이스 가져오기
            val selection = "$COLUMN_NAME_NAME = ?" // 삭제할 행을 선택하기 위한 조건
            val selectionArgs = arrayOf(name) // 조건에 해당하는 값들의 배열
            db.delete(TABLE_NAME, selection, selectionArgs) // 데이터베이스에서 행 삭제
        }

        companion object {
            // 데이터베이스 버전 상수: 데이터베이스 스키마가 변경될 때 업그레이드 필요
            const val DATABASE_VERSION = 1
            // 데이터베이스 이름 상수: 데이터베이스 파일의 이름
            const val DATABASE_NAME = "Info.db"
            // 테이블 이름 상수: 데이터베이스 내의 테이블 이름
            const val TABLE_NAME = "info"
            // 열 이름 상수: 'info' 테이블 내의 열 이름
            const val COLUMN_NAME_NAME = "name" // 이름 열
            const val COLUMN_NAME_PHONE = "phone" // 전화번호 열
            // 테이블 생성 SQL 문: 'info' 테이블을 생성하는 SQL 쿼리
            const val SQL_CREATE_ENTRIES =
                "CREATE TABLE $TABLE_NAME (" + // 'info' 테이블 생성
                        "${BaseColumns._ID} INTEGER PRIMARY KEY," + // 기본 키 열
                        "$COLUMN_NAME_NAME TEXT," + // 이름 열
                        "$COLUMN_NAME_PHONE TEXT)" // 전화번호 열
            // 테이블 삭제 SQL 문: 'info' 테이블을 삭제하는 SQL 쿼리
            const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME" // 'info' 테이블 삭제
        }
    }