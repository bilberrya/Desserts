package com.example.desserts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.desserts.DBHelper;
import com.example.desserts.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnAdd, btnClear;
    EditText etName, etWeight, etCcal, etPrice;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etCcal = (EditText) findViewById(R.id.etCcal);
        etPrice = (EditText) findViewById(R.id.etPrice);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        UpdateTable();
    }


    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_DESSERTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int weightIndex = cursor.getColumnIndex(DBHelper.KEY_WEIGHT);
            int ccalIndex = cursor.getColumnIndex(DBHelper.KEY_CCAL);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputWeight = new TextView(this);
                params.weight = 1.0f;
                outputWeight.setLayoutParams(params);
                outputWeight.setText(cursor.getString(weightIndex));
                dbOutputRow.addView(outputWeight);

                TextView outputCcal = new TextView(this);
                params.weight = 1.0f;
                outputCcal.setLayoutParams(params);
                outputCcal.setText(cursor.getString(ccalIndex));
                dbOutputRow.addView(outputCcal);

                TextView outputPrice = new TextView(this);
                params.weight = 1.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                dbOutputRow.addView(outputPrice);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить запись");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);

            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnAdd:
                String name = etName.getText().toString();
                String weight = etWeight.getText().toString();
                String ccal = etCcal.getText().toString();
                String price = etPrice.getText().toString();

                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_WEIGHT, weight);
                contentValues.put(DBHelper.KEY_CCAL, ccal);
                contentValues.put(DBHelper.KEY_PRICE, price);

                database.insert(DBHelper.TABLE_DESSERTS, null, contentValues);
                UpdateTable();
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_DESSERTS, null, null);
                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;

            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_DESSERTS, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});

                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_DESSERTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                    int weightIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_WEIGHT);
                    int ccalIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_CCAL);
                    int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                    int realID = 1;
                    do{
                        if(cursorUpdater.getInt(idIndex) > realID){
                            contentValues.put(DBHelper.KEY_ID, realID);
                            contentValues.put(DBHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_WEIGHT, cursorUpdater.getString(weightIndex));
                            contentValues.put(DBHelper.KEY_CCAL, cursorUpdater.getString(ccalIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                            database.replace(DBHelper.TABLE_DESSERTS, null, contentValues);
                        }
                        realID++;
                    }while (cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast()){
                        database.delete(DBHelper.TABLE_DESSERTS, DBHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}